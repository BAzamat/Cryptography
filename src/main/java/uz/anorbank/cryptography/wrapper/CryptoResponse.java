package uz.anorbank.cryptography.wrapper;

import lombok.NonNull;
import uz.anorbank.cryptography.enumeration.ResultStatus;
import uz.anorbank.cryptography.exeptoons.CryptoException;

import java.beans.ConstructorProperties;


public final class CryptoResponse<TResult> {

    private final TResult result;
    private final @NonNull ResultStatus status;
    private final CryptoException exception;

    @ConstructorProperties({"result", "status", "exception"})
    public CryptoResponse(final TResult result, final @NonNull ResultStatus status, final CryptoException exception) {
        if (status == null) {
            throw new NullPointerException("status is marked non-null but is null");
        } else {
            this.result = result;
            this.status = status;
            this.exception = exception;
        }
    }

    public TResult getResult() {
        return this.result;
    }

    public @NonNull ResultStatus getStatus() {
        return this.status;
    }

    public CryptoException getException() {
        return this.exception;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CryptoResponse)) {
            return false;
        } else {
            CryptoResponse other;
            label44: {
                other = (CryptoResponse)o;
                Object this$result = this.getResult();
                Object other$result = other.getResult();
                if (this$result == null) {
                    if (other$result == null) {
                        break label44;
                    }
                } else if (this$result.equals(other$result)) {
                    break label44;
                }

                return false;
            }

            Object this$status = this.getStatus();
            Object other$status = other.getStatus();
            if (this$status == null) {
                if (other$status != null) {
                    return false;
                }
            } else if (!this$status.equals(other$status)) {
                return false;
            }

            Object this$exception = this.getException();
            Object other$exception = other.getException();
            if (this$exception == null) {
                if (other$exception != null) {
                    return false;
                }
            } else if (!this$exception.equals(other$exception)) {
                return false;
            }

            return true;
        }
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Object $exception = this.getException();
        result = result * 59 + ($exception == null ? 43 : $exception.hashCode());
        return result;
    }

    public String toString() {
        return "CryptoResponse(result=" + this.getResult() + ", status=" + this.getStatus() + ", exception=" + this.getException() + ")";
    }
    }