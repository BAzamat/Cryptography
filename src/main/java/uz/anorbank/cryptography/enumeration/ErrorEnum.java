package uz.anorbank.cryptography.enumeration;

public enum ErrorEnum {
        NO_ERROR("Xatolik yo'q", "", "No error", Integer.valueOf(200)),
                KEY_GENERATOR_CREATION_ERROR("Kalit generatorini yaratish hatoligi", "", "Key generator object creation error", Integer.valueOf(9001)),
                        KEY_PARSE_ERROR("Key parse error", "Key parse error", "Key parse error", Integer.valueOf(9002)),
                        KEY_CREATION_ERROR("Kalitni yaratish hatoligi", "", "Key creation error", Integer.valueOf(9003)),
                                ENCRYPTION_ERROR("Shifrlash xatoligi", "", "Encryption error", Integer.valueOf(9100)),
                                        ENCRYPTION_RESULT_EMPTY("Shifrlash natijasi bo'sh", "", "Encryption result is empty", Integer.valueOf(9101)),
                                                DECRYPTION_ERROR("Deshifrlash xatoligi", "", "Encryption error", Integer.valueOf(9201)),
                                                        MESSAGE_NOT_ENCRYPTED("Xabar shifrlanmagan", "", "Message not encrypted", Integer.valueOf(9202)),
                                                                DECRYPTION_RESULT_EMPTY("Deshifrlash natijasi bo'sh", "", "Decryption result is empty", Integer.valueOf(9202)),
                                                                        UID_DECRYPTION_ERROR("Uid deshifrlash xatoligi", "", "Uid decryption error", Integer.valueOf(9203)),
                                                                        CHARSET_NOT_FOUND("Ko'dirovka topilmadi", "", "Charset not found", Integer.valueOf(9301)),
                                                                                LICENSE_CREATION_ERROR("Litsenziya yaratish xatoligi", "", "License construction error", Integer.valueOf(9401)),
                                                                                        LICENSE_ENCRYPTION_ERROR("Litsenziya shifrlash xatoligi", "", "License encryption error", Integer.valueOf(9402)),
                                                                                                LICENSE_STATE_ENCRYPTION_ERROR("Litsenziya shifrlash xatoligi", "", "License encryption error", Integer.valueOf(9402)),
                                                                                                        REQUEST_PARAM_DESERIALIZATION_ERROR("So'rovda json parametri deseriallashtirish xatoligi", "" , "Request json parameter deserialization error", Integer.valueOf(9501)),
                                                                                                                ENCRYPTOR_NOT_EXIST("Mavjud bo'lmagan shifrlash algoritmi berilgan", "", "Specified encryptor type not exist", Integer.valueOf(9600)),
                                                                                                                        LICENSE_SPLIT_ERROR("Litsenziya bo'lish xatoligi", "", "License split error", Integer.valueOf(10001)),
                                                                                                                                LICENSE_INFO_DESERIALIZATION_ERROR("Litsenziya deseriallashtirish xatoligi", "", "License info deserialization error", Integer.valueOf(10002)),
                                                                                                                                        SIGNATURE_CREATION_ERROR("Elektron imzo yaratish xatoligi", "", "Signature creation error", Integer.valueOf(9701)),
                                                                                                                                                NOT_IMPLEMENTED("So'ralgan amaliyot yaratilmagan", "", "Requested operation not implemented", Integer.valueOf(9999));

    private final String descriptionUz;

    private final String descriptionRu;

    private final String descriptionEn;

    private final Integer code;

    public String getDescriptionUz() {
        return this.descriptionUz;
    }

    public String getDescriptionRu() {
        return this.descriptionRu;
    }

    public String getDescriptionEn() {
        return this.descriptionEn;
    }

    public Integer getCode() {
        return this.code;
    }

    ErrorEnum(String descriptionUz, String descriptionRu, String descriptionEn, Integer code) {
        this.descriptionUz = descriptionUz;
        this.descriptionRu = descriptionRu;
        this.descriptionEn = descriptionEn;
        this.code = code;
    }
    }