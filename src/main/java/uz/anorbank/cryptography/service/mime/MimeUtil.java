//package uz.anorbank.cryptography.service.mime;
//
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import java.io.*;
//
//final class MimeUtil {
//    static MimeMessage canonicalize(Session session, MimeMessage mimeMessage) throws MessagingException, IOException {
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        MimeCanonicalOutputStream out = new MimeCanonicalOutputStream(buffer);
//        mimeMessage.writeTo(out);
//        out.close();
//        return new MimeMessage(session, new ByteArrayInputStream(buffer.toByteArray()));
//    }
//
//    static MimeBodyPart canonicalize(MimeBodyPart mimeBodyPart) throws MessagingException, IOException {
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        MimeCanonicalOutputStream out = new MimeCanonicalOutputStream(buffer);
//        mimeBodyPart.writeTo(out);
//        out.close();
//        return new MimeBodyPart(new ByteArrayInputStream(buffer.toByteArray()));
//    }
//
//    private static class MimeCanonicalOutputStream extends FilterOutputStream {
//        int lastReadByte = -1;
//
//        byte[] crlf = new byte[] { 13, 10 };
//
//        public MimeCanonicalOutputStream(OutputStream os) {
//            super(os);
//        }
//
//        public void write(int b) throws IOException {
//            if (b == 13) {
//                this.out.write(this.crlf);
//            } else if (b == 10) {
//                if (this.lastReadByte != 13)
//                    this.out.write(this.crlf);
//            } else {
//                this.out.write(b);
//            }
//            this.lastReadByte = b;
//        }
//
//        public void write(byte[] b) throws IOException {
//            write(b, 0, b.length);
//        }
//
//        public void write(byte[] b, int off, int len) throws IOException {
//            int start = off;
//            len = off + len;
//            for (int i = start; i < len; i++) {
//                if (b[i] == 13) {
//                    this.out.write(b, start, i - start);
//                    this.out.write(this.crlf);
//                    start = i + 1;
//                } else if (b[i] == 10) {
//                    if (this.lastReadByte != 13) {
//                        this.out.write(b, start, i - start);
//                        this.out.write(this.crlf);
//                    }
//                    start = i + 1;
//                }
//                this.lastReadByte = b[i];
//            }
//            if (len - start > 0)
//                this.out.write(b, start, len - start);
//        }
//    }
//}
