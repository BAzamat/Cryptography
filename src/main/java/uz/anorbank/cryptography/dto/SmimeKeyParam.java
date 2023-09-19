package uz.anorbank.cryptography.dto;


import java.beans.ConstructorProperties;
import java.nio.file.Path;

public final class SmimeKeyParam {
    private final Path keystorePath;

    private final String password;

    private final String alias;

    @ConstructorProperties({"keystorePath", "password", "alias"})
    public SmimeKeyParam(Path keystorePath, String password, String alias) {
        this.keystorePath = keystorePath;
        this.password = password;
        this.alias = alias;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SmimeKeyParam))
            return false;
        SmimeKeyParam other = (SmimeKeyParam)o;
        Object this$keystorePath = getKeystorePath(), other$keystorePath = other.getKeystorePath();
        if ((this$keystorePath == null) ? (other$keystorePath != null) : !this$keystorePath.equals(other$keystorePath))
            return false;
        Object this$password = getPassword(), other$password = other.getPassword();
        if ((this$password == null) ? (other$password != null) : !this$password.equals(other$password))
            return false;
        Object this$alias = getAlias(), other$alias = other.getAlias();
        return !((this$alias == null) ? (other$alias != null) : !this$alias.equals(other$alias));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $keystorePath = getKeystorePath();
        result = result * 59 + (($keystorePath == null) ? 43 : $keystorePath.hashCode());
        Object $password = getPassword();
        result = result * 59 + (($password == null) ? 43 : $password.hashCode());
        Object $alias = getAlias();
        return result * 59 + (($alias == null) ? 43 : $alias.hashCode());
    }

    public String toString() {
        return "SmimeKeyParam(keystorePath=" + getKeystorePath() + ", password=" + getPassword() + ", alias=" + getAlias() + ")";
    }

    public Path getKeystorePath() {
        return this.keystorePath;
    }

    public String getPassword() {
        return this.password;
    }

    public String getAlias() {
        return this.alias;
    }
}