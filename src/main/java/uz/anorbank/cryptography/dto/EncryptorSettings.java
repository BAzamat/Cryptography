package uz.anorbank.cryptography.dto;

import uz.anorbank.cryptography.enumeration.Charset;
import uz.anorbank.cryptography.enumeration.EncryptorType;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;


public final class EncryptorSettings {
    @NotNull
    private final EncryptorType type;

    @NotNull
    private final Charset charset;

    @ConstructorProperties({"type", "charset"})
    public EncryptorSettings(EncryptorType type, Charset charset) {
        this.type = type;
        this.charset = charset;
    }



    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EncryptorSettings))
            return false;
        EncryptorSettings other = (EncryptorSettings)o;
        Object this$type = getType(), other$type = other.getType();
        if ((this$type == null) ? (other$type != null) : !this$type.equals(other$type))
            return false;
        Object this$charset = getCharset(), other$charset = other.getCharset();
        return !((this$charset == null) ? (other$charset != null) : !this$charset.equals(other$charset));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $type = getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $charset = getCharset();
        return result * 59 + (($charset == null) ? 43 : $charset.hashCode());
    }

    public String toString() {
        return "EncryptorSettings(type=" + getType() + ", charset=" + getCharset() + ")";
    }

    public EncryptorType getType() {
        return this.type;
    }

    public Charset getCharset() {
        return this.charset;
    }
}