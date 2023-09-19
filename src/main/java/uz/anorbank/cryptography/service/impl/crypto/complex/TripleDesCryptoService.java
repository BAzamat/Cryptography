package uz.anorbank.cryptography.service.impl.crypto.complex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import uz.anorbank.cryptography.dto.PlainKeyDto;
import uz.anorbank.cryptography.dto.TripleDesKeyDto;
import uz.anorbank.cryptography.enumeration.Charset;
import uz.anorbank.cryptography.enumeration.CryptoActionType;
import uz.anorbank.cryptography.enumeration.ErrorEnum;
import uz.anorbank.cryptography.enumeration.HashType;
import uz.anorbank.cryptography.exeptoons.CryptoException;
import uz.anorbank.cryptography.primitive.CryptoUtil;
import uz.anorbank.cryptography.service.CryptoService;
import uz.anorbank.cryptography.service.HashResolver;
import uz.anorbank.cryptography.wrapper.CryptoRequest;
import uz.anorbank.cryptography.wrapper.CryptoResponse;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.beans.ConstructorProperties;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Base64;


@Service("TRIPLE_DES_CRYPTO_SERVICE")
@Validated
public class TripleDesCryptoService implements CryptoService<String, TripleDesKeyDto, String, PlainKeyDto> {
    private static final Logger log = LoggerFactory.getLogger(TripleDesCryptoService.class);

    public static final String NAME = "TRIPLE_DES_CRYPTO_SERVICE";

    private final Base64.Encoder encoder;

    private final Base64.Decoder decoder;

    private final HashResolver hashResolver;

    private final CryptoUtil cryptoUtil;

    @ConstructorProperties({"hashResolver", "cryptoUtil"})
    public TripleDesCryptoService(HashResolver hashResolver, CryptoUtil cryptoUtil) {
        this.encoder = Base64.getEncoder();
        this.decoder = Base64.getDecoder();
        this.hashResolver = hashResolver;
        this.cryptoUtil = cryptoUtil;
    }

    public CryptoResponse<TripleDesKeyDto> createKey(int keySize) {
        String key;
        log.info("createKey method started");
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DESede");
            SecretKey secretKey = generator.generateKey();
            key = String.format("%040x", new Object[] { new BigInteger(1, secretKey.getEncoded()) });
        } catch (Exception e) {
            log.error("Key generation error : {}", e.getMessage());
            return getKeyCreationFailCryptoResponse(new CryptoException(ErrorEnum.KEY_GENERATOR_CREATION_ERROR, e));
        }
        return getKeyCreationSuccessCryptoResponse(key);
    }

    public CryptoResponse<TripleDesKeyDto> loadKey(PlainKeyDto key) {
        log.info("parseKey method started");
        return getKeyCreationSuccessCryptoResponse(key.getKey());
    }

    public CryptoResponse<String> encrypt(CryptoRequest<String> request, TripleDesKeyDto key, Charset charset) {
        byte[] resultBytes;
        log.info("encrypt method started");
        log.debug("encrypt method data : {}", request.getData());
        try {
            log.debug("Trying to encrypt data");
            resultBytes = encrypt(((String)request.getData()).getBytes(charset.getCharsetName()), key.getKey(), key
                    .getKeyHashingUsed(), key.getHashType(), CryptoActionType.ENCRYPTION);
        } catch (Exception e) {
            log.warn("TripleDes encryption error : {}", e.getMessage());
            return this.cryptoUtil.buildFailResult(String.class, ErrorEnum.ENCRYPTION_ERROR, e);
        }
        if (resultBytes == null || resultBytes.length <= 0) {
            log.warn("Encryption operation result is empty");
            return this.cryptoUtil.buildFailResult(String.class, ErrorEnum.ENCRYPTION_RESULT_EMPTY, null);
        }
        log.debug("Trying to convert result bytes to string format");
        String resultString = this.encoder.encodeToString(resultBytes);
        log.debug("Encryption successfully ended with result : {}", resultString);
        return this.cryptoUtil.buildSuccessResult(resultString);
    }

    public CryptoResponse<String> decrypt(CryptoRequest<String> request, TripleDesKeyDto key, Charset charset) {
        byte[] resultBytes;
        String resultString;
        log.info("decrypt method started");
        log.debug("decrypt method data : {}", request.getData());
        try {
            log.debug("Trying to decrypt data");
            resultBytes = encrypt(this.decoder.decode((String)request.getData()), key.getKey(), key
                    .getKeyHashingUsed(), key.getHashType(), CryptoActionType.DECRYPTION);
        } catch (Exception e) {
            log.warn("TripleDes decryption error : {}", e.getMessage());
            return this.cryptoUtil.buildFailResult(String.class, ErrorEnum.DECRYPTION_ERROR, e);
        }
        if (resultBytes == null || resultBytes.length <= 0) {
            log.error("Decryption operation result is empty");
            return this.cryptoUtil.buildFailResult(String.class, ErrorEnum.DECRYPTION_RESULT_EMPTY, null);
        }
        log.debug("Trying to convert result bytes to string format");
        try {
            resultString = new String(resultBytes, charset.getCharsetName());
        } catch (UnsupportedEncodingException e) {
            log.error("Charset type not found : {}", e.getMessage());
            return this.cryptoUtil.buildFailResult(String.class, ErrorEnum.CHARSET_NOT_FOUND, e);
        }
        log.debug("Encryption successfully ended with result : {}", resultString);
        return this.cryptoUtil.buildSuccessResult(resultString);
    }

    private byte[] encrypt(byte[] text, String key, Boolean keyHashingUsed, HashType hashType, CryptoActionType type) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        log.trace("{} started for text with size : {} bytes, crypto key : {}, hashing usage : {}, hash type : {}", new Object[] { type, Integer.valueOf(text.length), key, keyHashingUsed, hashType });
        log.trace("key decoding started");
        byte[] cryptoKey = this.decoder.decode(keyHashingUsed.booleanValue() ? this.hashResolver.computeHash(key, hashType) : key);
        log.trace("decoded key size : {}", Integer.valueOf(cryptoKey.length));
        byte[] keyDecoded = Arrays.copyOf(cryptoKey, 24);
        IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyDecoded, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
        log.trace("initializing cryptography settings");
        cipher.init((type == CryptoActionType.DECRYPTION) ? 2 : 1, secretKeySpec, iv);
        log.trace("{} started", type);
        return cipher.doFinal(text);
    }

    private CryptoResponse<TripleDesKeyDto> getKeyCreationFailCryptoResponse(CryptoException e) {
        log.trace("getKeyCreationFailCryptoResponse started for exception", (Throwable)e);
        return this.cryptoUtil.buildFailResult(new TripleDesKeyDto("", Boolean.valueOf(false), HashType.PLAIN), ErrorEnum.KEY_CREATION_ERROR, (Exception)e);
    }

    private CryptoResponse<TripleDesKeyDto> getKeyCreationSuccessCryptoResponse(String key) {
        log.trace("getKeyCreationSuccessCryptoResponse started for key : {}", key);
        return this.cryptoUtil.buildSuccessResult(new TripleDesKeyDto(key, Boolean.valueOf(true), HashType.SHA1));
    }
}

