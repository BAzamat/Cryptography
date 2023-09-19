//package uz.anorbank.cryptography.service.impl.crypto.complex;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import uz.anorbank.cryptography.dto.SmimeDecrypted;
//import uz.anorbank.cryptography.dto.SmimeEncrypted;
//import uz.anorbank.cryptography.dto.SmimeKeyParam;
//import uz.anorbank.cryptography.enumeration.Charset;
//import uz.anorbank.cryptography.enumeration.ErrorEnum;
//import uz.anorbank.cryptography.enumeration.ResultStatus;
//import uz.anorbank.cryptography.primitive.CryptoUtil;
//import uz.anorbank.cryptography.primitive.SmimeResultHandlerUtil;
//import uz.anorbank.cryptography.service.CryptoService;
//import uz.anorbank.cryptography.service.mime.SmimeKey;
//import uz.anorbank.cryptography.service.mime.SmimeKeyStore;
//import uz.anorbank.cryptography.service.mime.SmimeState;
//import uz.anorbank.cryptography.service.mime.SmimeUtil;
//import uz.anorbank.cryptography.wrapper.CryptoRequest;
//import uz.anorbank.cryptography.wrapper.CryptoResponse;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimePart;
//import java.beans.ConstructorProperties;
//import java.io.InputStream;
//import java.nio.file.Files;
//
//@Service("SBC_RSA_CRYPTO_SERVICE")
//public class SmimeService implements CryptoService<SmimeEncrypted, SmimeKey, SmimeDecrypted, SmimeKeyParam> {
//    private static final Logger log = LoggerFactory.getLogger(SmimeService.class);
//
//    public static final String NAME = "SBC_RSA_CRYPTO_SERVICE";
//
//    private final CryptoUtil cryptoUtil;
//
//    private final SmimeResultHandlerUtil handlerUtil;
//
//    @ConstructorProperties({"cryptoUtil", "handlerUtil"})
//    public SmimeService(CryptoUtil cryptoUtil, SmimeResultHandlerUtil handlerUtil) {
//        this.cryptoUtil = cryptoUtil;
//        this.handlerUtil = handlerUtil;
//    }
//
//    public CryptoResponse<SmimeKey> createKey(int keySize) {
//        log.info("createKey started");
//        return this.cryptoUtil.buildFailResult(SmimeKey.class, ErrorEnum.NOT_IMPLEMENTED, null);
//    }
//
//    public CryptoResponse<SmimeKey> loadKey(SmimeKeyParam keyParam) {
//        log.info("parseKey started");
//        try {
//            InputStream stream = Files.newInputStream(keyParam.getKeystorePath(), new java.nio.file.OpenOption[0]);
//            SmimeKeyStore keyStore = new SmimeKeyStore(stream, keyParam.getPassword().toCharArray());
//            SmimeKey result = keyStore.getPrivateKey(keyParam.getAlias(), keyParam.getPassword().toCharArray());
//            return new CryptoResponse(result, ResultStatus.SUCCESS, null);
//        } catch (Exception e) {
//            return this.cryptoUtil.buildFailResult(SmimeKey.class, ErrorEnum.KEY_PARSE_ERROR, e);
//        }
//    }
//
//    public CryptoResponse<SmimeEncrypted> encrypt(CryptoRequest<SmimeDecrypted> request, SmimeKey smimeKey, Charset charset) {
//        log.info("encrypt started");
//        try {
//            MimeMessage result = SmimeUtil.encrypt(((SmimeDecrypted)request.getData()).getSession(), ((SmimeDecrypted)request.getData()).getMessage(), smimeKey.getCertificate());
//            log.debug("encryption process successfully ended");
//            return this.handlerUtil.buildEncryptionSuccessResult(request, result);
//        } catch (MessagingException|java.io.IOException|org.bouncycastle.cms.CMSException|org.bouncycastle.mail.smime.SMIMEException|java.security.cert.CertificateEncodingException e) {
//            log.error("encryption error occurred", e);
//            return this.cryptoUtil.buildFailResult(SmimeEncrypted.class, ErrorEnum.DECRYPTION_ERROR, e);
//        }
//    }
//
//    public CryptoResponse<SmimeDecrypted> decrypt(CryptoRequest<SmimeEncrypted> request, SmimeKey key, Charset charset) {
//        log.info("decrypt started");
//        if (SmimeUtil.getStatus((MimePart)((SmimeEncrypted)request.getData()).getMessage()) != SmimeState.ENCRYPTED) {
//            log.debug("can't decrypt message (not encrypted). message id : {}", Integer.valueOf(((SmimeEncrypted)request.getData()).getMessage().getMessageNumber()));
//            return this.cryptoUtil.buildFailResult(SmimeDecrypted.class, ErrorEnum.MESSAGE_NOT_ENCRYPTED, null);
//        }
//        try {
//            MimeMessage result = SmimeUtil.decrypt(((SmimeEncrypted)request.getData()).getSession(), ((SmimeEncrypted)request.getData()).getMessage(), key);
//            log.debug("decryption process successfully ended");
//            return this.handlerUtil.buildDecryptionSuccessResult(request, result);
//        } catch (MessagingException|java.io.IOException|org.bouncycastle.cms.CMSException|org.bouncycastle.mail.smime.SMIMEException e) {
//            log.error("decryption error occurred", e);
//            return this.cryptoUtil.buildFailResult(SmimeDecrypted.class, ErrorEnum.DECRYPTION_ERROR, e);
//        }
//    }
//
//}
