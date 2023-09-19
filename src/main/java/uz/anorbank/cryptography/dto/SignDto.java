package uz.anorbank.cryptography.dto;

import uz.anorbank.cryptography.enumeration.HashType;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;


public class SignDto<TToSign, TKey> {
    @NotNull
    private final TToSign dataToSign;

    @NotNull
    private final TKey key;

    @NotNull
    private final HashType hashType;

    public String toString() {
        return "SignDto(dataToSign=" + getDataToSign() + ", key=" + getKey() + ", hashType=" + getHashType() + ")";
    }

    @ConstructorProperties({"dataToSign", "key", "hashType"})
    public SignDto(TToSign dataToSign, TKey key, HashType hashType) {
        this.dataToSign = dataToSign;
        this.key = key;
        this.hashType = hashType;
    }

    public TToSign getDataToSign() {
        return this.dataToSign;
    }

    public TKey getKey() {
        return this.key;
    }

    public HashType getHashType() {
        return this.hashType;
    }
}
