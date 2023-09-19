package uz.anorbank.cryptography.service.impl.signature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.anorbank.cryptography.dto.EncryptorSettings;
import uz.anorbank.cryptography.dto.SignDto;
import uz.anorbank.cryptography.enumeration.Charset;
import uz.anorbank.cryptography.enumeration.EncryptorType;
import uz.anorbank.cryptography.service.HashResolver;
import uz.anorbank.cryptography.service.PrimitiveEncryptor;
import uz.anorbank.cryptography.service.Signature;
import uz.anorbank.cryptography.wrapper.CryptoRequest;
import uz.anorbank.cryptography.wrapper.CryptoResponse;

import java.beans.ConstructorProperties;
import java.security.KeyPair;
import java.util.Objects;


@Service("RSA_SIGNATURE")
public class RsaSignature implements Signature<String, String, KeyPair> {
    private static final Logger log = LoggerFactory.getLogger(RsaSignature.class);

    public static final String NAME = "RSA_SIGNATURE";

    private final PrimitiveEncryptor<String, KeyPair, String> encryptor;

    private final HashResolver hashResolver;

    @ConstructorProperties({"encryptor", "hashResolver"})
    public RsaSignature(PrimitiveEncryptor<String, KeyPair, String> encryptor, HashResolver hashResolver) {
        this.encryptor = encryptor;
        this.hashResolver = hashResolver;
    }

    public CryptoResponse<String> sign(SignDto<String, KeyPair> signDto, Charset charset) {
        log.info("sign started for data with size : {} bytes", ((String)signDto.getDataToSign()).length());
        String hash = this.hashResolver.computeHash((String)signDto.getDataToSign(), signDto.getHashType());
        CryptoResponse<String> sign = this.encryptor.encrypt(new CryptoRequest(hash), signDto.getKey(), new EncryptorSettings(EncryptorType.SIGNATURE, charset));
        log.debug("Data successfully signed. Signed data size : {}",((String)signDto.getDataToSign()).length());
        return sign;
    }

    public boolean verify(String signedData, SignDto<String, KeyPair> signDto, Charset charset) {
        log.info("isValid started for data with size : {} bytes", ((String)signDto.getDataToSign()).length());
        CryptoResponse<String> decrypted = this.encryptor.decrypt(new CryptoRequest(signedData), signDto.getKey(), new EncryptorSettings(EncryptorType.SIGNATURE, charset));
        String hash = this.hashResolver.computeHash((String)signDto.getDataToSign(), signDto.getHashType());
        boolean result = (decrypted.getStatus().isSuccess().booleanValue() && Objects.equals(decrypted.getResult(), hash));
        log.debug("Sign validation process ended with result : {}", Boolean.valueOf(result));
        return result;
    }
}
