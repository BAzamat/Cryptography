package uz.anorbank.cryptography.enumeration;


public enum CryptoAlgorithm {
    RSA("RSA");

    private final String instanceName;

    public String getInstanceName() {
        return this.instanceName;
    }

    CryptoAlgorithm(String instanceName) {
        this.instanceName = instanceName;
    }
}
