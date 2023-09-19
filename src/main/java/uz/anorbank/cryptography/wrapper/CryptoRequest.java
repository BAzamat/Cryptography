package uz.anorbank.cryptography.wrapper;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;

public final class CryptoRequest<TData> {
    @NotNull
    private final TData data;

    @ConstructorProperties({"data"})
    public CryptoRequest(TData data) {
        this.data = data;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CryptoRequest))
            return false;
        CryptoRequest<?> other = (CryptoRequest)o;
        Object this$data = getData(), other$data = other.getData();
        return !((this$data == null) ? (other$data != null) : !this$data.equals(other$data));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "CryptoRequest(data=" + getData() + ")";
    }

    public TData getData() {
        return this.data;
    }
}
