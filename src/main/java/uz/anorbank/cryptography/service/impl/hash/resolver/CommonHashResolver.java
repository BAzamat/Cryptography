package uz.anorbank.cryptography.service.impl.hash.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import uz.anorbank.cryptography.enumeration.HashType;
import uz.anorbank.cryptography.service.HashAlgorithm;
import uz.anorbank.cryptography.service.HashResolver;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;


@Service
public class CommonHashResolver implements HashResolver {
    private static final Logger log = LoggerFactory.getLogger(CommonHashResolver.class);

    private final ApplicationContext applicationContext;

    @ConstructorProperties({"applicationContext"})
    public CommonHashResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String computeHash(@NotNull String data, HashType hashType) {
        log.debug("Calculate HashCode for : {}", data);
        HashAlgorithm algorithm = (HashAlgorithm)this.applicationContext.getBean(hashType.getInstanceName(), HashAlgorithm.class);
        String result = algorithm.compute(data);
        log.debug("Hash successfully calculated");
        return result;
    }
}
