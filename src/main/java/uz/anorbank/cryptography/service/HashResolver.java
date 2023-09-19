package uz.anorbank.cryptography.service;

import org.springframework.validation.annotation.Validated;
import uz.anorbank.cryptography.enumeration.HashType;

import javax.validation.constraints.NotNull;


@Validated
public interface HashResolver {
    String computeHash(@NotNull String paramString, @NotNull HashType paramHashType);
}
