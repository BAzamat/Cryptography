package uz.anorbank.cryptography.service.impl.hash.algorithm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.anorbank.cryptography.service.HashAlgorithm;


@Service("PLAIN_HASH_ALGORITHM")
public class PlainHashAlgorithm implements HashAlgorithm {
    private static final Logger log = LoggerFactory.getLogger(PlainHashAlgorithm.class);

    public static final String NAME = "PLAIN_HASH_ALGORITHM";

    public String compute(String data) {
        log.debug("PlainHashAlgorithm started for data with size : {}", Integer.valueOf(data.length()));
        return data;
    }
}
