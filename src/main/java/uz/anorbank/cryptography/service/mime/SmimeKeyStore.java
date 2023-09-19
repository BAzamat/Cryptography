package uz.anorbank.cryptography.service.mime;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class SmimeKeyStore {
    private final KeyStore keyStore;

    public SmimeKeyStore(InputStream stream, char[] password) {
        this(stream, password, true);
    }

    public SmimeKeyStore(InputStream stream, char[] password, boolean discardPassword) {
        try {
            this.keyStore = KeyStore.getInstance("PKCS12", "BC");
            this.keyStore.load(stream, password);
        } catch (Exception e) {
            throw new SmimeException("Couldn't initialize SmimeKeyStore", e);
        } finally {
            if (discardPassword)
                overwrite(password);
        }
    }

    public int size() {
        try {
            return this.keyStore.size();
        } catch (KeyStoreException e) {
            throw new SmimeException("Couldn't retrieve the number of entries from SmimeKeyStore", e);
        }
    }

    public SmimeKey getPrivateKey(String alias, char[] password) {
        return getPrivateKey(alias, password, true);
    }

    public SmimeKey getPrivateKey(String alias, char[] password, boolean discardPassword) {
        try {
            if (containsPrivateKeyAlias(alias)) {
                PrivateKey privateKey = (PrivateKey)this.keyStore.getKey(alias, password);
                Certificate[] certificateChain = this.keyStore.getCertificateChain(alias);
                return new SmimeKey(privateKey, copy(certificateChain));
            }
            return null;
        } catch (Exception e) {
            throw new SmimeException("Couldn't recover SmimeKey from SmimeKeyStore", e);
        } finally {
            if (discardPassword)
                overwrite(password);
        }
    }

    public Set<String> getPrivateKeyAliases() {
        try {
            Enumeration<String> aliases = this.keyStore.aliases();
            HashSet<String> aliasSet = new HashSet<>();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                if (this.keyStore.isKeyEntry(alias))
                    aliasSet.add(alias);
            }
            return Collections.unmodifiableSet(aliasSet);
        } catch (Exception e) {
            throw new SmimeException("Couldn't recover aliases from SmimeKeyStore", e);
        }
    }

    public boolean containsPrivateKeyAlias(String alias) {
        try {
            return this.keyStore.isKeyEntry(alias);
        } catch (Exception e) {
            throw new SmimeException("Couldn't recover aliases from SmimeKeyStore", e);
        }
    }

    private void overwrite(char[] password) {
        if (null != password)
            for (int i = 0, n = password.length; i < n; i++)
                password[i] = Character.MIN_VALUE;
    }

    private X509Certificate[] copy(Certificate[] certificateChain) {
        X509Certificate[] x509certificateChain = new X509Certificate[certificateChain.length];
        for (int i = 0, n = certificateChain.length; i < n; i++)
            x509certificateChain[i] = (X509Certificate)certificateChain[i];
        return x509certificateChain;
    }
}
