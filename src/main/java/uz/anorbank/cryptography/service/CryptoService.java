package uz.anorbank.cryptography.service;


import org.springframework.validation.annotation.Validated;
import uz.anorbank.cryptography.enumeration.Charset;
import uz.anorbank.cryptography.wrapper.CryptoRequest;
import uz.anorbank.cryptography.wrapper.CryptoResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface CryptoService<TEncryptedData, TKey, TDecryptedData, TKeyParam> {
    CryptoResponse<TKey> createKey(@NotNull int paramInt);

    CryptoResponse<TKey> loadKey(@Valid @NotNull TKeyParam paramTKeyParam);

    CryptoResponse<TEncryptedData> encrypt(@Valid @NotNull CryptoRequest<TDecryptedData> paramCryptoRequest, @Valid @NotNull TKey paramTKey, @NotNull Charset paramCharset);

    CryptoResponse<TDecryptedData> decrypt(@Valid @NotNull CryptoRequest<TEncryptedData> paramCryptoRequest, @Valid @NotNull TKey paramTKey, @NotNull Charset paramCharset);
}
