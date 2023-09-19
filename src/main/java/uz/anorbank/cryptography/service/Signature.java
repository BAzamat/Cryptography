package uz.anorbank.cryptography.service;


import org.springframework.validation.annotation.Validated;
import uz.anorbank.cryptography.dto.SignDto;
import uz.anorbank.cryptography.enumeration.Charset;
import uz.anorbank.cryptography.wrapper.CryptoResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Validated
public interface Signature<TSigned, TToSign, TKey> {
    CryptoResponse<TSigned> sign(@Valid @NotNull SignDto<TToSign, TKey> signDto, @NotNull Charset charset);

    boolean verify(@NotNull TSigned signedData, @Valid @NotNull SignDto<TToSign, TKey> signDto, @NotNull Charset charset);
}

