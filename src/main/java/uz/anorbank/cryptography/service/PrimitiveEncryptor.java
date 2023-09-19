package uz.anorbank.cryptography.service;

import org.springframework.validation.annotation.Validated;
import uz.anorbank.cryptography.dto.EncryptorSettings;
import uz.anorbank.cryptography.wrapper.CryptoRequest;
import uz.anorbank.cryptography.wrapper.CryptoResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Validated
public interface PrimitiveEncryptor<TCryptoData, TKey, TRequestData> {
    CryptoResponse<TCryptoData> encrypt(@Valid @NotNull CryptoRequest<TRequestData> paramCryptoRequest, @Valid @NotNull TKey paramTKey, @Valid @NotNull EncryptorSettings paramEncryptorSettings);

    CryptoResponse<TCryptoData> decrypt(@Valid @NotNull CryptoRequest<TRequestData> paramCryptoRequest, @Valid @NotNull TKey paramTKey, @Valid @NotNull EncryptorSettings paramEncryptorSettings);
}

