//package uz.anorbank.cryptography.service.mime;
//
//import org.bouncycastle.asn1.ASN1Encodable;
//import org.bouncycastle.asn1.ASN1EncodableVector;
//import org.bouncycastle.asn1.cms.AttributeTable;
//import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
//import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
//import org.bouncycastle.asn1.smime.SMIMECapability;
//import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
//import org.bouncycastle.asn1.smime.SMIMEEncryptionKeyPreferenceAttribute;
//import org.bouncycastle.asn1.x500.X500Name;
//import org.bouncycastle.cert.X509CertificateHolder;
//import org.bouncycastle.cert.jcajce.JcaCertStore;
//import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
//import org.bouncycastle.cms.*;
//import org.bouncycastle.cms.jcajce.*;
//import org.bouncycastle.mail.smime.*;
//import org.bouncycastle.operator.OperatorCreationException;
//import org.bouncycastle.operator.OutputEncryptor;
//import org.bouncycastle.util.Store;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.activation.CommandMap;
//import javax.activation.MailcapCommandMap;
//import javax.mail.*;
//import javax.mail.internet.*;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.security.PrivateKey;
//import java.security.Security;
//import java.security.cert.Certificate;
//import java.security.cert.CertificateEncodingException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.*;
//
//public final class SmimeUtil {
//    private static final Logger log = LoggerFactory.getLogger(SmimeUtil.class);
//
//    static {
//        if (null == Security.getProvider("BC"))
//            updateMailcapCommandMap();
//    }
//
//    public static MimeMessage encrypt(Session session, MimeMessage mimeMessage, X509Certificate certificate) throws MessagingException, CertificateEncodingException, CMSException, SMIMEException, IOException {
//        log.trace("encrypt started");
//        MimeMessage encryptedMimeMessage = new MimeMessage(session);
//        copyHeaders(mimeMessage, encryptedMimeMessage);
//        SMIMEEnvelopedGenerator generator = prepareGenerator(certificate);
//        OutputEncryptor encryptor = prepareEncryptor();
//        MimeBodyPart encryptedMimeBodyPart = generator.generate(mimeMessage, encryptor);
//        copyContent(encryptedMimeBodyPart, encryptedMimeMessage);
//        copyHeaders(encryptedMimeBodyPart, encryptedMimeMessage);
//        encryptedMimeMessage.saveChanges();
//        return encryptedMimeMessage;
//    }
//
//    public static MimeBodyPart encrypt(MimeBodyPart mimeBodyPart, X509Certificate certificate) throws CertificateEncodingException, CMSException, SMIMEException {
//        log.trace("encrypt started");
//        SMIMEEnvelopedGenerator generator = prepareGenerator(certificate);
//        OutputEncryptor encryptor = prepareEncryptor();
//        return generator.generate(mimeBodyPart, encryptor);
//    }
//
//    public static MimeMessage decrypt(Session session, MimeMessage mimeMessage, SmimeKey smimeKey) throws MessagingException, IOException, CMSException, SMIMEException {
//        log.trace("decrypt started");
//        byte[] content = decryptContent(new SMIMEEnveloped(mimeMessage), smimeKey);
//        MimeBodyPart mimeBodyPart = SMIMEUtil.toMimeBodyPart(content);
//        MimeMessage decryptedMessage = new MimeMessage(session);
//        copyHeaderLines(mimeMessage, decryptedMessage);
//        copyContent(mimeBodyPart, decryptedMessage);
//        decryptedMessage.setHeader("Content-Type", mimeBodyPart.getContentType());
//        return decryptedMessage;
//    }
//
//    public static MimeBodyPart decrypt(MimeBodyPart mimeBodyPart, SmimeKey smimeKey) throws MessagingException, CMSException, SMIMEException {
//        log.trace("decrypt started");
//        return SMIMEUtil.toMimeBodyPart(decryptContent(new SMIMEEnveloped(mimeBodyPart), smimeKey));
//    }
//
//    public static MimeBodyPart decrypt(MimeMultipart mimeMultipart, SmimeKey smimeKey) throws MessagingException, SMIMEException, CMSException {
//        log.trace("decrypt started");
//        MimeBodyPart mimeBodyPart = new MimeBodyPart();
//        mimeBodyPart.setContent((Multipart)mimeMultipart);
//        mimeBodyPart.setHeader("Content-Type", mimeMultipart.getContentType());
//        return decrypt(mimeBodyPart, smimeKey);
//    }
//
//    public static MimeBodyPart sign(MimeBodyPart mimeBodyPart, SmimeKey smimeKey) throws MessagingException, CertificateEncodingException, OperatorCreationException, IOException, SMIMEException {
//        log.trace("sign started");
//        SMIMESignedGenerator generator = getGenerator(smimeKey);
//        MimeMultipart signedMimeMultipart = generator.generate(MimeUtil.canonicalize(mimeBodyPart));
//        MimeBodyPart signedMimeBodyPart = new MimeBodyPart();
//        signedMimeBodyPart.setContent((Multipart)signedMimeMultipart);
//        return signedMimeBodyPart;
//    }
//
//    public static MimeMessage sign(Session session, MimeMessage mimeMessage, SmimeKey smimeKey) throws MessagingException, IOException, SMIMEException, CertificateEncodingException, OperatorCreationException {
//        log.trace("sign started");
//        MimeMessage signedMessage = new MimeMessage(session);
//        copyHeaderLines(mimeMessage, signedMessage);
//        copyContent(sign(extractMimeBodyPart(mimeMessage), smimeKey), signedMessage);
//        return signedMessage;
//    }
//
//    public static boolean checkSignature(MimeMultipart mimeMultipart) {
//        try {
//            return checkSignature(new SMIMESigned(mimeMultipart));
//        } catch (Exception e) {
//            throw handledException(e);
//        }
//    }
//
//    public static boolean checkSignature(MimePart mimePart) {
//        try {
//            if (mimePart.isMimeType("multipart/signed"))
//                return checkSignature(new SMIMESigned((MimeMultipart)mimePart.getContent()));
//            if (mimePart.isMimeType("application/pkcs7-mime") || mimePart.isMimeType("application/x-pkcs7-mime"))
//                return checkSignature(new SMIMESigned((Part)mimePart));
//            throw new SmimeException("Message not signed");
//        } catch (Exception e) {
//            throw handledException(e);
//        }
//    }
//
//    public static MimeBodyPart getSignedContent(MimeMultipart mimeMultipart) {
//        try {
//            return (new SMIMESigned(mimeMultipart)).getContent();
//        } catch (Exception e) {
//            throw handledException(e);
//        }
//    }
//
//    public static MimeBodyPart getSignedContent(MimePart mimePart) {
//        try {
//            if (mimePart.isMimeType("multipart/signed"))
//                return (new SMIMESigned((MimeMultipart)mimePart.getContent())).getContent();
//            if (mimePart.isMimeType("application/pkcs7-mime") || mimePart.isMimeType("application/x-pkcs7-mime"))
//                return (new SMIMESigned((Part)mimePart)).getContent();
//            throw new SmimeException("Message not signed");
//        } catch (Exception e) {
//            throw handledException(e);
//        }
//    }
//
//    public static SmimeState getStatus(MimeMultipart mimeMultipart) {
//        try {
//            return getStatus(new ContentType(mimeMultipart.getContentType()));
//        } catch (Exception e) {
//            throw handledException(e);
//        }
//    }
//
//    public static SmimeState getStatus(MimePart mimePart) {
//        try {
//            return getStatus(new ContentType(mimePart.getContentType()));
//        } catch (Exception e) {
//            throw handledException(e);
//        }
//    }
//
//    private static void updateMailcapCommandMap() {
//        log.trace("updateMailcapCommandMap started");
//        MailcapCommandMap map = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
//        map.addMailcap("application/pkcs7-signature;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_signature");
//        map.addMailcap("application/pkcs7-mime;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_mime");
//        map.addMailcap("application/x-pkcs7-signature;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_signature");
//        map.addMailcap("application/x-pkcs7-mime;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_mime");
//        map.addMailcap("multipart/signed;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.multipart_signed");
//        CommandMap.setDefaultCommandMap((CommandMap)map);
//    }
//
//    private static void copyHeaders(MimeBodyPart fromBodyPart, MimeMessage toMessage) throws MessagingException {
//        log.trace("copyHeaders started");
//        Enumeration<Header> headers = fromBodyPart.getAllHeaders();
//        copyHeaders(headers, toMessage);
//    }
//
//    private static void copyHeaders(MimeMessage fromMessage, MimeMessage toMessage) throws MessagingException {
//        log.trace("copyHeaders started");
//        Enumeration<Header> headers = fromMessage.getAllHeaders();
//        copyHeaders(headers, toMessage);
//    }
//
//    private static void copyHeaders(Enumeration<Header> headers, MimeMessage toMessage) throws MessagingException {
//        log.trace("copyHeaders started");
//        while (headers.hasMoreElements()) {
//            Header header = headers.nextElement();
//            toMessage.setHeader(header.getName(), header.getValue());
//        }
//    }
//
//    private static SMIMEEnvelopedGenerator prepareGenerator(X509Certificate certificate) throws CertificateEncodingException {
//        log.trace("prepareGenerator started");
//        JceKeyTransRecipientInfoGenerator infoGenerator = new JceKeyTransRecipientInfoGenerator(certificate);
//        infoGenerator.setProvider("BC");
//        SMIMEEnvelopedGenerator generator = new SMIMEEnvelopedGenerator();
//        generator.addRecipientInfoGenerator((RecipientInfoGenerator)infoGenerator);
//        return generator;
//    }
//
//    private static OutputEncryptor prepareEncryptor() throws CMSException {
//        log.trace("prepareEncryptor started");
//        return (new JceCMSContentEncryptorBuilder(CMSAlgorithm.DES_EDE3_CBC))
//                .setProvider("BC")
//                .build();
//    }
//
//    private static byte[] decryptContent(SMIMEEnveloped smimeEnveloped, SmimeKey smimeKey) throws MessagingException, CMSException {
//        log.trace("decryptContent started");
//        X509Certificate certificate = smimeKey.getCertificate();
//        PrivateKey privateKey = smimeKey.getPrivateKey();
//        RecipientInformationStore recipients = smimeEnveloped.getRecipientInfos();
//        RecipientInformation recipient = recipients.get((RecipientId)new JceKeyTransRecipientId(certificate));
//        if (null == recipient)
//            throw new MessagingException("no recipient");
//        JceKeyTransEnvelopedRecipient transportRecipient = new JceKeyTransEnvelopedRecipient(privateKey);
//        transportRecipient.setProvider("BC");
//        return recipient.getContent((Recipient)transportRecipient);
//    }
//
//    private static void copyHeaderLines(MimeMessage fromMessage, MimeMessage toMessage) throws MessagingException {
//        log.trace("copyHeaderLines started");
//        Enumeration<String> headerLines = fromMessage.getAllHeaderLines();
//        while (headerLines.hasMoreElements()) {
//            String nextElement = headerLines.nextElement();
//            toMessage.addHeaderLine(nextElement);
//        }
//    }
//
//    private static void copyContent(MimeBodyPart fromBodyPart, MimeMessage toMessage) throws MessagingException, IOException {
//        log.trace("copyContent started");
//        toMessage.setContent(fromBodyPart.getContent(), fromBodyPart.getContentType());
//    }
//
//    private static SMIMESignedGenerator getGenerator(SmimeKey smimeKey) throws CertificateEncodingException, OperatorCreationException {
//        log.trace("getGenerator started");
//        SMIMESignedGenerator generator = new SMIMESignedGenerator();
//        generator.addCertificates((Store)getCertificateStore(smimeKey));
//        generator.addSignerInfoGenerator(getInfoGenerator(smimeKey));
//        return generator;
//    }
//
//    private static SignerInfoGenerator getInfoGenerator(SmimeKey smimeKey) throws OperatorCreationException, CertificateEncodingException {
//        log.trace("getInfoGenerator started");
//        JcaSimpleSignerInfoGeneratorBuilder builder = new JcaSimpleSignerInfoGeneratorBuilder();
//        builder.setSignedAttributeGenerator(new AttributeTable(getSignedAttributes(smimeKey)));
//        builder.setProvider("BC");
//        PrivateKey privateKey = smimeKey.getPrivateKey();
//        X509Certificate certificate = smimeKey.getCertificate();
//        return builder.build("SHA256withRSA", privateKey, certificate);
//    }
//
//    private static ASN1EncodableVector getSignedAttributes(SmimeKey smimeKey) {
//        log.trace("getSignedAttributes started");
//        ASN1EncodableVector signedAttributes = new ASN1EncodableVector();
//        IssuerAndSerialNumber issuerAndSerialNumber = getIssuerAndSerialNumber(smimeKey);
//        signedAttributes.add((ASN1Encodable)new SMIMEEncryptionKeyPreferenceAttribute(issuerAndSerialNumber));
//        signedAttributes.add((ASN1Encodable)new SMIMECapabilitiesAttribute(getCapabilityVector()));
//        return signedAttributes;
//    }
//
//    private static SMIMECapabilityVector getCapabilityVector() {
//        log.trace("getCapabilityVector started");
//        SMIMECapabilityVector capabilityVector = new SMIMECapabilityVector();
//        capabilityVector.addCapability(SMIMECapability.dES_EDE3_CBC);
//        capabilityVector.addCapability(SMIMECapability.rC2_CBC, 128);
//        capabilityVector.addCapability(SMIMECapability.dES_CBC);
//        return capabilityVector;
//    }
//
//    private static IssuerAndSerialNumber getIssuerAndSerialNumber(SmimeKey smimeKey) {
//        log.trace("getIssuerAndSerialNumber started");
//        X509Certificate certificate = smimeKey.getCertificate();
//        BigInteger serialNumber = certificate.getSerialNumber();
//        X500Name issuerName = new X500Name(certificate.getIssuerDN().getName());
//        return new IssuerAndSerialNumber(issuerName, serialNumber);
//    }
//
//    private static JcaCertStore getCertificateStore(SmimeKey smimeKey) throws CertificateEncodingException {
//        List<Certificate> certificateList;
//        log.trace("getCertificateStore started");
//        X509Certificate[] certificateChain = smimeKey.getCertificateChain();
//        X509Certificate certificate = smimeKey.getCertificate();
//        if (certificateChain != null && certificateChain.length > 0) {
//            certificateList = Arrays.asList((Certificate[])certificateChain);
//        } else {
//            certificateList = new ArrayList<>();
//            certificateList.add(certificate);
//        }
//        return new JcaCertStore(certificateList);
//    }
//
//    private static MimeBodyPart extractMimeBodyPart(MimeMessage mimeMessage) throws IOException, MessagingException {
//        log.trace("extractMimeBodyPart started");
//        Object content = mimeMessage.getContent();
//        UpdatableMimeBodyPart updateableMimeBodyPart = new UpdatableMimeBodyPart();
//        if (content instanceof Multipart) {
//            updateableMimeBodyPart.setContent((Multipart)content);
//        } else {
//            updateableMimeBodyPart.setContent(content, mimeMessage.getDataHandler().getContentType());
//        }
//        updateableMimeBodyPart.updateHeaders();
//        return updateableMimeBodyPart;
//    }
//
//    private static boolean checkSignature(SMIMESigned smimeSigned) {
//        try {
//            boolean returnValue = true;
//            Store<X509CertificateHolder> certificates = smimeSigned.getCertificates();
//            Iterator<SignerInformation> signerInformations = smimeSigned.getSignerInfos().getSigners().iterator();
//            while (returnValue && signerInformations.hasNext()) {
//                SignerInformation signerInformation = signerInformations.next();
//                X509Certificate certificate = getCertificate(certificates, signerInformation.getSID());
//                SignerInformationVerifier verifier = getVerifier(certificate);
//                if (!signerInformation.verify(verifier))
//                    returnValue = false;
//            }
//            return returnValue;
//        } catch (Exception e) {
//            throw handledException(e);
//        }
//    }
//
//    private static X509Certificate getCertificate(Store certificates, SignerId signerId) throws CertificateException {
//        X509CertificateHolder certificateHolder = (X509CertificateHolder)certificates.getMatches(signerId).iterator().next();
//        JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter();
//        certificateConverter.setProvider("BC");
//        return certificateConverter.getCertificate(certificateHolder);
//    }
//
//    private static SignerInformationVerifier getVerifier(X509Certificate certificate) throws OperatorCreationException {
//        JcaSimpleSignerInfoVerifierBuilder builder = new JcaSimpleSignerInfoVerifierBuilder();
//        builder.setProvider("BC");
//        return builder.build(certificate);
//    }
//
//    private static SmimeState getStatus(ContentType contentType) {
//        try {
//            if (isSmimeSignatureContentType(contentType))
//                return SmimeState.SIGNED;
//            if (isSmimeEncryptionContenttype(contentType))
//                return SmimeState.ENCRYPTED;
//            return SmimeState.NEITHER;
//        } catch (Exception e) {
//            return SmimeState.NEITHER;
//        }
//    }
//
//    private static boolean isSmimeEncryptionContenttype(ContentType contentType) {
//        String baseContentType = contentType.getBaseType();
//        return (baseContentType.equalsIgnoreCase("application/pkcs7-mime") || baseContentType
//                .equalsIgnoreCase("application/x-pkcs7-mime"));
//    }
//
//    private static boolean isSmimeSignatureContentType(ContentType contentType) {
//        String baseContentType = contentType.getBaseType();
//        return (baseContentType.equalsIgnoreCase("multipart/signed") &&
//                isSmimeSignatureProtocoll(contentType.getParameter("protocol")));
//    }
//
//    private static boolean isSmimeSignatureProtocoll(String protocol) {
//        return (protocol.equalsIgnoreCase("application/pkcs7-signature") || protocol
//                .equalsIgnoreCase("application/x-pkcs7-signature"));
//    }
//
//    private static SmimeException handledException(Exception e) {
//        if (e instanceof SmimeException)
//            return (SmimeException)e;
//        return new SmimeException(e.getMessage(), e);
//    }
//}
