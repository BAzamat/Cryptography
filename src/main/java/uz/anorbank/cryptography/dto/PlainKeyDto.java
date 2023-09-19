package uz.anorbank.cryptography.dto;


import lombok.NonNull;
import uz.anorbank.cryptography.enumeration.KeyType;

import java.beans.ConstructorProperties;


public final class PlainKeyDto {
    @NonNull
    private final String key;

    @NonNull
    private final KeyType keyType;

    @ConstructorProperties({"key", "keyType"})
    public PlainKeyDto(@NonNull String key, @NonNull KeyType keyType) {
        if (key == null)
            throw new NullPointerException("key is marked non-null but is null");
        if (keyType == null)
            throw new NullPointerException("keyType is marked non-null but is null");
        this.key = key;
        this.keyType = keyType;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PlainKeyDto))
            return false;
        PlainKeyDto other = (PlainKeyDto)o;
        Object this$key = getKey(), other$key = other.getKey();
        if ((this$key == null) ? (other$key != null) : !this$key.equals(other$key))
            return false;
        Object this$keyType = getKeyType(), other$keyType = other.getKeyType();
        return !((this$keyType == null) ? (other$keyType != null) : !this$keyType.equals(other$keyType));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        Object $keyType = this.getKeyType();
        result = result * 59 + ($keyType == null ? 43 : $keyType.hashCode());
        return result;
    }

    public String toString() {
        return "PlainKeyDto(key=" + getKey() + ", keyType=" + getKeyType() + ")";
    }

    @NonNull
    public String getKey() {
        return this.key;
    }

    @NonNull
    public KeyType getKeyType() {
        return this.keyType;
    }
}
