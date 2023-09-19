//package uz.anorbank.cryptography.dto;
//
//import javax.mail.Session;
//import javax.mail.internet.MimeMessage;
//import java.beans.ConstructorProperties;
//
//public final class SmimeEncrypted {
//    private final Session session;
//
//    private final MimeMessage message;
//
//    @ConstructorProperties({"session", "message"})
//    public SmimeEncrypted(Session session, MimeMessage message) {
//        this.session = session;
//        this.message = message;
//    }
//
//    public boolean equals(Object o) {
//        if (o == this)
//            return true;
//        if (!(o instanceof SmimeEncrypted))
//            return false;
//        SmimeEncrypted other = (SmimeEncrypted) o;
//        Object this$session = getSession(), other$session = other.getSession();
//        if ((this$session == null) ? (other$session != null) : !this$session.equals(other$session))
//            return false;
//        Object this$message = getMessage(), other$message = other.getMessage();
//        return !((this$message == null) ? (other$message != null) : !this$message.equals(other$message));
//    }
//
//    public int hashCode() {
//        int PRIME = 59;
//        int result = 1;
//        Object $session = getSession();
//        result = result * 59 + (($session == null) ? 43 : $session.hashCode());
//        Object $message = getMessage();
//        return result * 59 + (($message == null) ? 43 : $message.hashCode());
//    }
//
//    public String toString() {
//        return "SmimeEncrypted(session=" + getSession() + ", message=" + getMessage() + ")";
//    }
//
//    public Session getSession() {
//        return this.session;
//    }
//
//    public MimeMessage getMessage() {
//        return this.message;
//    }
//}
