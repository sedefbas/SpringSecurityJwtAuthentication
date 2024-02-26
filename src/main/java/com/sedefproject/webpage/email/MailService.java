package com.sedefproject.webpage.email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
//to alıcı
//from gönderen oluyo arkadaslar :)
//message: link baglantısını göndercegimiz html yapı.
   public void send(String to,String message){
       try {
           MimeMessage mimeMessage = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
           helper.setText(message, true);
           helper.setTo(to); //üye olacak kisi
           helper.setSubject("Confirm your email");
           helper.setFrom("bassedef98@gmail.com"); //kimden gönderilecek aktivasyon linki ?
           mailSender.send(mimeMessage);
       } catch (MessagingException e) {
           log.error("failed to send email", e);
           throw new IllegalStateException("failed to send email");
       }
   }
   //message dediğimiz bu aslında arkadaşlar. açıklamak istedim.
   public String buildAuthEmail(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Helvetica', Arial, sans-serif;\n" +
                "            font-size: 16px;\n" +
                "            margin: 0;\n" +
                "            color: #0b0c0c;\n" +
                "        }\n" +
                "        table {\n" +
                "            border-collapse: collapse;\n" +
                "            width: 100%;\n" +
                "            max-width: 580px;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        td {\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #0b0c0c;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .header-content {\n" +
                "            padding: 10px;\n" +
                "            font-size: 28px;\n" +
                "            font-weight: 700;\n" +
                "            color: #ffffff;\n" +
                "            text-decoration: none;\n" +
                "            vertical-align: top;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .blue-bar {\n" +
                "            background-color: #1D70B8;\n" +
                "            height: 10px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 30px 10px;\n" +
                "        }\n" +
                "        .content-paragraph {\n" +
                "            margin: 0 0 20px 0;\n" +
                "            font-size: 19px;\n" +
                "            line-height: 25px;\n" +
                "            color: #0b0c0c;\n" +
                "        }\n" +
                "        .link-block {\n" +
                "            border-left: 10px solid #b1b4b6;\n" +
                "            padding: 15px 0 0.1px 15px;\n" +
                "            font-size: 19px;\n" +
                "            line-height: 25px;\n" +
                "            background-color: #eaeaea; /* Gri arka plan */\n" +
                "        }\n" +
                "        .link {\n" +
                "            color: #1D70B8; /* Mavi renk */\n" +
                "            text-decoration: none; /* Alt çizgiyi kaldır */\n" +
                "        }\n" +
                "        .closing-paragraph {\n" +
                "            height: 30px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <table>\n" +
                "        <tr class=\"header\">\n" +
                "            <td>\n" +
                "                <span class=\"header-content\">Confirm your email</span>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"blue-bar\"></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"content\">\n" +
                "            <td>\n" +
                "                <p class=\"content-paragraph\">Hi " + name + ",</p>\n" +
                "                <p class=\"content-paragraph\">Thank you for registering. Please click on the below link to activate your account:</p>\n" +
                "                <blockquote class=\"link-block\">\n" +
                "                    <p class=\"content-paragraph\"><a href=\"" + link + "\" class=\"link\">Activate Now</a></p>\n" +
                "                </blockquote>\n" +
                "                <p class=\"content-paragraph\">Link will expire in 15 minutes.</p>\n" +
                "                <p>See you soon</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr class=\"closing-paragraph\">\n" +
                "            <td></td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";
    }

    public String buildResetEmail(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Helvetica', Arial, sans-serif;\n" +
                "            font-size: 16px;\n" +
                "            margin: 0;\n" +
                "            color: #0b0c0c;\n" +
                "        }\n" +
                "        table {\n" +
                "            border-collapse: collapse;\n" +
                "            width: 100%;\n" +
                "            max-width: 580px;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        td {\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #0b0c0c;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .header-content {\n" +
                "            padding: 10px;\n" +
                "            font-size: 28px;\n" +
                "            font-weight: 700;\n" +
                "            color: #ffffff;\n" +
                "            text-decoration: none;\n" +
                "            vertical-align: top;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .blue-bar {\n" +
                "            background-color: #1D70B8;\n" +
                "            height: 10px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 30px 10px;\n" +
                "        }\n" +
                "        .content-paragraph {\n" +
                "            margin: 0 0 20px 0;\n" +
                "            font-size: 19px;\n" +
                "            line-height: 25px;\n" +
                "            color: #0b0c0c;\n" +
                "        }\n" +
                "        .link-block {\n" +
                "            border-left: 10px solid #b1b4b6;\n" +
                "            padding: 15px 0 0.1px 15px;\n" +
                "            font-size: 19px;\n" +
                "            line-height: 25px;\n" +
                "            background-color: #eaeaea; /* Gri arka plan */\n" +
                "        }\n" +
                "        .link {\n" +
                "            color: #1D70B8; /* Mavi renk */\n" +
                "            text-decoration: none; /* Alt çizgiyi kaldır */\n" +
                "        }\n" +
                "        .closing-paragraph {\n" +
                "            height: 30px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <table>\n" +
                "        <tr class=\"header\">\n" +
                "            <td>\n" +
                "                <span class=\"header-content\">Şifrenizi Sıfırlayın</span>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"blue-bar\"></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"content\">\n" +
                "            <td>\n" +
                "                <p class=\"content-paragraph\">Merhaba " + name + ",</p>\n" +
                "                <p class=\"content-paragraph\">Şifrenizi sıfırlamak için lütfen aşağıdaki bağlantıya tıklayın:</p>\n" +
                "                <blockquote class=\"link-block\">\n" +
                "                    <p class=\"content-paragraph\"><a href=\"" + link + "\" class=\"link\">Şifre Sıfırlama Bağlantısı</a></p>\n" +
                "                </blockquote>\n" +
                "                <p class=\"content-paragraph\">Bağlantı 1 saat boyunca geçerlidir.</p>\n" +
                "                <p>İyi günler dileriz</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr class=\"closing-paragraph\">\n" +
                "            <td></td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";
    }

}
