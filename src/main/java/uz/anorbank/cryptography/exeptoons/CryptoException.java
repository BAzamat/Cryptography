package uz.anorbank.cryptography.exeptoons;


import uz.anorbank.cryptography.enumeration.ErrorEnum;

public class CryptoException extends Exception {
    private final ErrorEnum error;

    public String toString() {
        return "CryptoException(error=" + getError() + ")";
    }

    public ErrorEnum getError() {
        return this.error;
    }

    public CryptoException(ErrorEnum error) {
        super(error.getCode() + " : " + error.getDescriptionEn());
        this.error = error;
    }

    public CryptoException(ErrorEnum error, Throwable innerException) {
        super(error.getCode() + " : " + error.getDescriptionEn(), innerException);
        this.error = error;
    }
}
