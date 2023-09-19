package uz.anorbank.cryptography.primitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uz.anorbank.cryptography.enumeration.ErrorEnum;
import uz.anorbank.cryptography.enumeration.ResultStatus;
import uz.anorbank.cryptography.exeptoons.CryptoException;
import uz.anorbank.cryptography.wrapper.CryptoResponse;


@Component
public class CryptoUtil {
    private static final Logger log = LoggerFactory.getLogger(CryptoUtil.class);

    public <TContent> CryptoResponse<TContent> buildFailResult(Class<TContent> contentType, ErrorEnum error, Exception exception) {
        log.warn("getFailResponse started for type : {}", contentType);
        return new CryptoResponse((Object)null, ResultStatus.FAIL, this.resolveException(error, exception));
    }

    public <TContent> CryptoResponse<TContent> buildFailResult(TContent content, ErrorEnum error, Exception exception) {
        log.warn("getFailResponse started");
        return new CryptoResponse(content, ResultStatus.FAIL, this.resolveException(error, exception));
    }

    public <TContent> CryptoResponse<TContent> buildSuccessResult(TContent content) {
        log.debug("buildSuccessResult started");
        return new CryptoResponse(content, ResultStatus.SUCCESS, (CryptoException)null);
    }

    private CryptoException resolveException(ErrorEnum error, Exception exception) {
        log.trace("resolveException started");
        return exception == null ? new CryptoException(error) : new CryptoException(error, exception);
    }

    public CryptoUtil() {
    }
}
