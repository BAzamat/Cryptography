package uz.anorbank.cryptography.enumeration;

public enum Charset {
    UTF8("UTF-8"),
    WIN1251("Windows-1251");

    private final String charsetName;

    public String getCharsetName() {
        return this.charsetName;
    }

    Charset(String charsetName) {
        this.charsetName = charsetName;
    }
}
