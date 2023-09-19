package uz.anorbank.cryptography.enumeration;


public enum HashType {
    PLAIN("PLAIN_HASH_ALGORITHM"),
    SHA1("SHA1_HASH_ALGORITHM"),
    MD5("MD5_HASH_ALGORITHM"),
    SHA256("SHA256_HASH_ALGORITHM"),
    SHA512("SHA512_HASH_ALGORITHM");

    private final String instanceName;

    public String getInstanceName() {
        return this.instanceName;
    }

    HashType(String instanceName) {
        this.instanceName = instanceName;
    }
}
