package uz.anorbank.cryptography.dto;

import uz.anorbank.cryptography.enumeration.HashType;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;


public final class TripleDesKeyDto {
    @NotNull
    private final String key;

    @NotNull
    private final Boolean keyHashingUsed;

    @NotNull
    private final HashType hashType;

    @ConstructorProperties({"key", "keyHashingUsed", "hashType"})
    public TripleDesKeyDto(String key, Boolean keyHashingUsed, HashType hashType) {
        this.key = key;
        this.keyHashingUsed = keyHashingUsed;
        this.hashType = hashType;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TripleDesKeyDto))
            return false;
        TripleDesKeyDto other = (TripleDesKeyDto)o;
        Object this$keyHashingUsed = getKeyHashingUsed(), other$keyHashingUsed = other.getKeyHashingUsed();
        if ((this$keyHashingUsed == null) ? (other$keyHashingUsed != null) : !this$keyHashingUsed.equals(other$keyHashingUsed))
            return false;
        Object this$key = getKey(), other$key = other.getKey();
        if ((this$key == null) ? (other$key != null) : !this$key.equals(other$key))
            return false;
        Object this$hashType = getHashType(), other$hashType = other.getHashType();
        return !((this$hashType == null) ? (other$hashType != null) : !this$hashType.equals(other$hashType));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $keyHashingUsed = getKeyHashingUsed();
        result = result * 59 + (($keyHashingUsed == null) ? 43 : $keyHashingUsed.hashCode());
        Object $key = getKey();
        result = result * 59 + (($key == null) ? 43 : $key.hashCode());
        Object $hashType = getHashType();
        return result * 59 + (($hashType == null) ? 43 : $hashType.hashCode());
    }

    public String toString() {
        return "TripleDesKeyDto(key=" + getKey() + ", keyHashingUsed=" + getKeyHashingUsed() + ", hashType=" + getHashType() + ")";
    }

    public String getKey() {
        return this.key;
    }

    public Boolean getKeyHashingUsed() {
        return this.keyHashingUsed;
    }

    public HashType getHashType() {
        return this.hashType;
    }
}
