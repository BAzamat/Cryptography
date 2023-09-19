package uz.anorbank.cryptography.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface HashAlgorithm {
    String compute(@NotNull String paramString);
}
