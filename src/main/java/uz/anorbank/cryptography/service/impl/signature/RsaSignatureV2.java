package uz.anorbank.cryptography.service.impl.signature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.anorbank.cryptography.dto.SignDto;
import uz.anorbank.cryptography.enumeration.Charset;
import uz.anorbank.cryptography.enumeration.ErrorEnum;
import uz.anorbank.cryptography.primitive.CryptoUtil;
import uz.anorbank.cryptography.service.Signature;
import uz.anorbank.cryptography.wrapper.CryptoResponse;

import java.beans.ConstructorProperties;
import java.security.KeyPair;
import java.util.Base64;


@Service("RSA_SIGNATURE_V2")
public class RsaSignatureV2 implements Signature<String, String, KeyPair> {
    private static final Logger log = LoggerFactory.getLogger(RsaSignatureV2.class);
    public static final String NAME = "RSA_SIGNATURE_V2";
    public static final String PROVIDER = "BC";
    private final CryptoUtil cryptoUtil;

    public CryptoResponse<String> sign(SignDto<String, KeyPair> signDto, Charset charset) {
        log.info("sign started for data with size : {} bytes", ((String)signDto.getDataToSign()).length());

        try {
            java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA", "BC");
            signature.initSign(((KeyPair)signDto.getKey()).getPrivate());
            signature.update(((String)signDto.getDataToSign()).getBytes(charset.getCharsetName()));
            String result = Base64.getEncoder().encodeToString(signature.sign());
            return this.cryptoUtil.buildSuccessResult(result);
        } catch (Exception var5) {
            log.error("Signing process error occurred", var5);
            return this.cryptoUtil.buildFailResult(String.class, ErrorEnum.SIGNATURE_CREATION_ERROR, var5);
        }
    }

    public boolean verify(String signedData, SignDto<String, KeyPair> signDto, Charset charset) {
        log.info("isValid started for data with size : {} bytes", ((String)signDto.getDataToSign()).length());

        try {
            java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA", "BC");
            signature.initVerify(((KeyPair)signDto.getKey()).getPublic());
            signature.update(((String)signDto.getDataToSign()).getBytes(charset.getCharsetName()));
            boolean result = signature.verify(Base64.getDecoder().decode(signedData));
            return result;
        } catch (Exception var6) {
            log.error("Sign verification process error occurred", var6);
            return false;
        }
    }

    @ConstructorProperties({"cryptoUtil"})
    public RsaSignatureV2(final CryptoUtil cryptoUtil) {
        this.cryptoUtil = cryptoUtil;
    }
}