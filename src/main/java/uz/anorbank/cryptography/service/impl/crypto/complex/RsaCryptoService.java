package uz.anorbank.cryptography.service.impl.crypto.complex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import uz.anorbank.cryptography.dto.EncryptorSettings;
import uz.anorbank.cryptography.dto.PlainKeyDto;
import uz.anorbank.cryptography.enumeration.Charset;
import uz.anorbank.cryptography.enumeration.CryptoAlgorithm;
import uz.anorbank.cryptography.enumeration.EncryptorType;
import uz.anorbank.cryptography.enumeration.ErrorEnum;
import uz.anorbank.cryptography.exeptoons.CryptoException;
import uz.anorbank.cryptography.primitive.CryptoUtil;
import uz.anorbank.cryptography.service.CryptoService;
import uz.anorbank.cryptography.service.PrimitiveEncryptor;
import uz.anorbank.cryptography.wrapper.CryptoRequest;
import uz.anorbank.cryptography.wrapper.CryptoResponse;

import java.beans.ConstructorProperties;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Service("RSA_CRYPTO_SERVICE")
public class RsaCryptoService implements CryptoService<String, KeyPair, String, PlainKeyDto> {
    private static final Logger log = LoggerFactory.getLogger(RsaCryptoService.class);

    public static final String NAME = "RSA_CRYPTO_SERVICE";

    @Qualifier("RSA_ENCRYPTOR")
    private final PrimitiveEncryptor<String, KeyPair, String> encryptor;

    private final CryptoUtil cryptoUtil;

    @ConstructorProperties({"encryptor", "cryptoUtil"})
    public RsaCryptoService(@Qualifier("RSA_ENCRYPTOR") PrimitiveEncryptor<String, KeyPair, String> encryptor, CryptoUtil cryptoUtil) {
        this.encryptor = encryptor;
        this.cryptoUtil = cryptoUtil;
    }

    public CryptoResponse<KeyPair> createKey(int keySize) {
        KeyPairGenerator keygen;
        log.info("createKey started");
        try {
            log.debug("Creating key generator");
            keygen = KeyPairGenerator.getInstance(CryptoAlgorithm.RSA.getInstanceName());
        } catch (Exception e) {
            log.warn("Key generator object creation error : {}", e.getMessage());
            return getFailedKeyPairCryptoResponse(new CryptoException(ErrorEnum.KEY_GENERATOR_CREATION_ERROR, e));
        }
        keygen.initialize(keySize, new SecureRandom());
        KeyPair result = keygen.generateKeyPair();
        log.debug("Key generation succeed");
        return this.cryptoUtil.buildSuccessResult(result);
    }

    public CryptoResponse<KeyPair> loadKey(PlainKeyDto key) {
        log.info("parseKey started");
        try {
            KeyPair keyPair = resolveAndGetKey(key);
            log.debug("Key parsed successfully");
            return this.cryptoUtil.buildSuccessResult(keyPair);
        } catch (Exception e) {
            log.error("Parse key error : {}", e.getMessage());
            return this.cryptoUtil.buildFailResult(KeyPair.class, ErrorEnum.KEY_PARSE_ERROR, e);
        }
    }

//    public CryptoResponse<String> encrypt(CryptoRequest<String> request, KeyPair key, Charset charset) {
//        log.info("encrypt method started");
//        log.debug("encrypt method data : {}", request.getData());
//        CryptoResponse<String> result = this.encryptor.encrypt(request, key, new EncryptorSettings(EncryptorType.ENCRYPTION, charset));
//        log.debug("Data successfully encrypted. Encrypted data size : {}", Integer.valueOf(((String)result.getResult()).length()));
//        return result;
//    }
//
//    public CryptoResponse<String> decrypt(CryptoRequest<String> request, KeyPair key, Charset charset) {
//        log.info("decrypt started");
//        CryptoResponse<String> result = this.encryptor.decrypt(request, key, new EncryptorSettings(EncryptorType.ENCRYPTION, charset));
//        log.debug("Data successfully decrypted. Decrypted data size : {}", Integer.valueOf(((String)result.getResult()).length()));
//        return result;
//    }
//
//    private KeyPair resolveAndGetKey(PlainKeyDto key) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        switch (key.getKeyType()) {
//            case PUBLIC:
//                keyPair = new KeyPair(parsePublicKey(key.getKey()), null);
//                return keyPair;
//        }
//        KeyPair keyPair = new KeyPair(null, parsePrivateKey(key.getKey()));
//        return keyPair;
//    }
    public CryptoResponse<String> encrypt(CryptoRequest<String> request, KeyPair key, Charset charset) {
        log.info("encrypt method started");
        log.debug("encrypt method data : {}", request.getData());
        CryptoResponse<String> result = this.encryptor.encrypt(request, key, new EncryptorSettings(EncryptorType.ENCRYPTION, charset));
        log.debug("Data successfully encrypted. Encrypted data size : {}", ((String)result.getResult()).length());
        return result;
    }

    public CryptoResponse<String> decrypt(CryptoRequest<String> request, KeyPair key, Charset charset) {
        log.info("decrypt started");
        CryptoResponse<String> result = this.encryptor.decrypt(request, key, new EncryptorSettings(EncryptorType.ENCRYPTION, charset));
        log.debug("Data successfully decrypted. Decrypted data size : {}", ((String)result.getResult()).length());
        return result;
    }

    private KeyPair resolveAndGetKey(PlainKeyDto key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair keyPair;
        switch (key.getKeyType()) {
            case PUBLIC:
                keyPair = new KeyPair(this.parsePublicKey(key.getKey()), (PrivateKey)null);
                break;
            case PRIVATE:
            default:
                keyPair = new KeyPair((PublicKey)null, this.parsePrivateKey(key.getKey()));
        }

        return keyPair;
    }

    private RSAPublicKey parsePublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("parsePublicKey started");
        byte[] encoded = encodePlainKey(key);
        log.debug("Creating key factory object");
        KeyFactory keyFactory = KeyFactory.getInstance(CryptoAlgorithm.RSA.getInstanceName());
        log.debug("Parsing to pubic key");
        RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
        log.debug("Key parse process succeed");
        return publicKey;
    }

    private RSAPrivateKey parsePrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("parsePrivateKey started");
        byte[] encoded = encodePlainKey(key);
        log.debug("Creating key factory object");
        KeyFactory keyFactory = KeyFactory.getInstance(CryptoAlgorithm.RSA.getInstanceName());
        log.debug("Parsing to private key");
        RSAPrivateKey privateKey = (RSAPrivateKey)keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded));
        log.debug("Key parse process succeed");
        return privateKey;
    }

    private byte[] encodePlainKey(String key) {
        log.debug("encodePlainKey started");
        log.debug("Encoding key : {}", key);
        log.debug("Removing headers");
        String keyCopy = removeKeyHeader(key);
        log.debug("Decoding plain key");
        byte[] encoded = Base64.getDecoder().decode(keyCopy);
        log.debug("Encoding result : {}", encoded);
        return encoded;
    }

    private String removeKeyHeader(String key) {
        log.trace("removeKeyHeader started");
        return key.replaceAll("(--)(-)*(\\w|\\s|\\d)*(\\2)*", "");
    }

    private CryptoResponse<KeyPair> getFailedKeyPairCryptoResponse(CryptoException e) {
        log.trace("getFailedKeyPairCryptoResponse started");
        return this.cryptoUtil.buildFailResult(null, e.getError(), (Exception)e);
    }
}
