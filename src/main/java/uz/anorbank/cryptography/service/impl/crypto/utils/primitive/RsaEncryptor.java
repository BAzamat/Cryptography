package uz.anorbank.cryptography.service.impl.crypto.utils.primitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.anorbank.cryptography.dto.EncryptorSettings;
import uz.anorbank.cryptography.enumeration.Charset;
import uz.anorbank.cryptography.enumeration.CryptoAlgorithm;
import uz.anorbank.cryptography.enumeration.ErrorEnum;
import uz.anorbank.cryptography.exeptoons.CryptoException;
import uz.anorbank.cryptography.primitive.CryptoUtil;
import uz.anorbank.cryptography.service.PrimitiveEncryptor;
import uz.anorbank.cryptography.wrapper.CryptoRequest;
import uz.anorbank.cryptography.wrapper.CryptoResponse;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.beans.ConstructorProperties;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyPair;
import java.util.Base64;



@Service("RSA_ENCRYPTOR")
public class RsaEncryptor implements PrimitiveEncryptor<String, KeyPair, String> {
    private static final Logger log = LoggerFactory.getLogger(RsaEncryptor.class);

    public static final String NAME = "RSA_ENCRYPTOR";

    private final CryptoUtil cryptoUtil;

    @ConstructorProperties({"cryptoUtil"})
    public RsaEncryptor(CryptoUtil cryptoUtil) {
        this.cryptoUtil = cryptoUtil;
    }

    public CryptoResponse<String> encrypt(CryptoRequest<String> request, KeyPair keyPair, EncryptorSettings settings) {
        log.debug("encrypt started");
        try {
            log.debug("Creating Rsa object");
            Cipher cipher = Cipher.getInstance(CryptoAlgorithm.RSA.getInstanceName());
            log.debug("Applying crypto key to Rsa object");
            cipher.init(1, getKey(keyPair, settings, Boolean.valueOf(true)));
            String result = Base64.getEncoder().encodeToString(computeCryptoData(request, settings.getCharset(), cipher));
            return this.cryptoUtil.buildSuccessResult(result);
        } catch (Exception e) {
            log.warn("Rsa encryption error : {}", e.getMessage());
            return this.cryptoUtil.buildFailResult(String.class, ErrorEnum.ENCRYPTION_ERROR, e);
        }
    }

    public CryptoResponse<String> decrypt(CryptoRequest<String> request, KeyPair keyPair, EncryptorSettings settings) {
        log.debug("decrypt started");
        try {
            log.debug("Creating Rsa object");
            Cipher cipher = Cipher.getInstance("RSA");
            log.debug("Applying crypto key to Rsa object");
            cipher.init(2, getKey(keyPair, settings, Boolean.valueOf(false)));
            String result = new String(cipher.doFinal(getBase64((String)request.getData())), settings.getCharset().getCharsetName());
            return this.cryptoUtil.buildSuccessResult(result);
        } catch (Exception e) {
            log.warn("Rsa decryption error : {}", e.getMessage());
            return this.cryptoUtil.buildFailResult(String.class, ErrorEnum.DECRYPTION_ERROR, e);
        }
    }

    private Key getKey(KeyPair keyPair, EncryptorSettings settings, Boolean isEncryptionKey) throws CryptoException {
        log.trace("getKey started");
        switch (settings.getType()) {
            case ENCRYPTION:
                return isEncryptionKey.booleanValue() ? keyPair.getPublic() : keyPair.getPrivate();
            case SIGNATURE:
                return isEncryptionKey.booleanValue() ? keyPair.getPrivate() : keyPair.getPublic();
        }
        log.error(ErrorEnum.ENCRYPTOR_NOT_EXIST.getDescriptionEn());
        throw new CryptoException(ErrorEnum.ENCRYPTOR_NOT_EXIST);
    }

    private byte[] getBase64(String data) {
        log.trace("getBase64 started");
        return Base64.getDecoder().decode(data);
    }

    private byte[] computeCryptoData(CryptoRequest<String> request, Charset charsetName, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        log.trace("computeCryptoData started");
        return cipher.doFinal(((String)request.getData()).getBytes(charsetName.getCharsetName()));
    }
}
