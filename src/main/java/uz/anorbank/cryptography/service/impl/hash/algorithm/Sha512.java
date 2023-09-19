package uz.anorbank.cryptography.service.impl.hash.algorithm;


import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.anorbank.cryptography.service.HashAlgorithm;

@Service("SHA512_HASH_ALGORITHM")
public class Sha512 implements HashAlgorithm {
    private static final Logger log = LoggerFactory.getLogger(Sha512.class);

    public static final String NAME = "SHA512_HASH_ALGORITHM";

    public String compute(String data) {
        log.debug("Calculating SHA1 hash value for data with size : {}", Integer.valueOf(data.length()));
        return DigestUtils.sha512Hex(data);
    }
}
