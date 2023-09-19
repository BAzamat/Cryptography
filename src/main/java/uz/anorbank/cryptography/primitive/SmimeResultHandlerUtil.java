//package uz.anorbank.cryptography.primitive;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import uz.anorbank.cryptography.dto.SmimeDecrypted;
//import uz.anorbank.cryptography.dto.SmimeEncrypted;
//import uz.anorbank.cryptography.wrapper.CryptoRequest;
//import uz.anorbank.cryptography.wrapper.CryptoResponse;
//
//import javax.mail.internet.MimeMessage;
//import java.beans.ConstructorProperties;
//
//@Component
//public class SmimeResultHandlerUtil {
//    private static final Logger log = LoggerFactory.getLogger(SmimeResultHandlerUtil.class);
//    private final CryptoUtil cryptoUtil;
//
//    public CryptoResponse<SmimeEncrypted> buildEncryptionSuccessResult(CryptoRequest<SmimeDecrypted> request, MimeMessage result) {
//        log.debug("buildEncryptionSuccessResult started");
//        return this.cryptoUtil.buildSuccessResult(new SmimeEncrypted(((SmimeDecrypted)request.getData()).getMessage().getSession(), result));
//    }
//
//    public CryptoResponse<SmimeDecrypted> buildDecryptionSuccessResult(CryptoRequest<SmimeEncrypted> request, MimeMessage result) {
//        log.debug("buildDecryptionSuccessResult started");
//        return this.cryptoUtil.buildSuccessResult(new SmimeDecrypted(((SmimeEncrypted)request.getData()).getSession(), result));
//    }
//
//
//    public SmimeResultHandlerUtil(final CryptoUtil cryptoUtil) {
//        this.cryptoUtil = cryptoUtil;
//    }
//}
