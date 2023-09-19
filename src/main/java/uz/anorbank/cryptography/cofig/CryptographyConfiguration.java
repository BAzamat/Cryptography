package uz.anorbank.cryptography.cofig;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.Provider;
import java.security.Security;

@Configuration
@EnableConfigurationProperties({CryptographyProperties.class})
public class CryptographyConfiguration {
    private static final Logger log = LoggerFactory.getLogger(CryptographyConfiguration.class);

    public CryptographyConfiguration() {
        initSecurityProviders();
    }

    public void initSecurityProviders() {
        log.trace("initSecurityProviders started");
        Security.addProvider((Provider)new BouncyCastleProvider());
    }
}
