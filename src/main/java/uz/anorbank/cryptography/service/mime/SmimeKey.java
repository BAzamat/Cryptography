package uz.anorbank.cryptography.service.mime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class SmimeKey {
    private static final Logger log = LoggerFactory.getLogger(SmimeKey.class);

    private final PrivateKey privateKey;

    private final X509Certificate[] certificateChain;

    private List<String> addresses;

    public SmimeKey(PrivateKey privateKey, X509Certificate... certificateChain) {
        this.privateKey = privateKey;
        this.certificateChain = certificateChain;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public X509Certificate getCertificate() {
        return this.certificateChain[0];
    }

    public X509Certificate[] getCertificateChain() {
        return this.certificateChain;
    }

    public List<String> getAssociatedAddresses() {
        if (this.addresses == null)
            extractAssociatedAddresses();
        return this.addresses;
    }

    private void extractAssociatedAddresses() {
        ArrayList<String> addresses = new ArrayList<>();
        try {
            X509Certificate certificate = getCertificate();
            if (null != certificate) {
                Principal principal = certificate.getSubjectDN();
                if (null != principal) {
                    String name = principal.getName();
                    StringTokenizer tokenizer = new StringTokenizer(name, ",");
                    while (tokenizer.hasMoreTokens()) {
                        String next = tokenizer.nextToken();
                        if (next.startsWith("E="))
                            addresses.add(next.substring(2));
                    }
                }
            }
        } catch (Exception e) {
            log.debug("error occurred", e);
        }
        this.addresses = Collections.unmodifiableList(addresses);
    }
}
