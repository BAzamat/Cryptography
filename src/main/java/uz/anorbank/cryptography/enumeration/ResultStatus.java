package uz.anorbank.cryptography.enumeration;

public enum ResultStatus {
    SUCCESS("Amaliyot muvaffaqiyatli yakunlandi", "", "Operation successfully accomplished"),
            FAIL("Amaliyot xatoligi", "", "Operation fail");

    private final String descriptionUz;

    private final String descriptionRu;

    private final String descriptionEn;

    public String getDescriptionUz() {
        return this.descriptionUz;
    }

    public String getDescriptionRu() {
        return this.descriptionRu;
    }

    public String getDescriptionEn() {
        return this.descriptionEn;
    }

    ResultStatus(String descriptionUz, String descriptionRu, String descriptionEn) {
        this.descriptionUz = descriptionUz;
        this.descriptionRu = descriptionRu;
        this.descriptionEn = descriptionEn;
    }

    public Boolean isSuccess() {
        return Boolean.valueOf((this == SUCCESS));
    }
    }
