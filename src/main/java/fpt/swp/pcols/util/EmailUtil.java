package fpt.swp.pcols.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailUtil {

    private JavaMailSender javaMailSender;

    private void sendEmail(String email, String subject, String linkTemplate, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(email);
        helper.setSubject(subject);

        String htmlContent = getHtmlContent(email, otp, linkTemplate);
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    private String getHtmlContent(String email, String otp, String linkTemplate) {
        String verifyLink = String.format(linkTemplate, email, otp);
        return String.format("""
                        <html>
                          <body>
                            <p>Chào bạn,</p>
                            <p>Vui lòng bấm vào link bên dưới:</p>
                            <a href="%s" target="_blank">Click để thực hiện</a>
                            <p>OTP của bạn là: <b>%s</b></p>
                          </body>
                        </html>
                        """, verifyLink, otp);
    }

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        String linkTemplate = "http://localhost:8080/auth/verify-account?email=%s&otp=%s";
        sendEmail(email, "Email Verification", linkTemplate, otp);
    }

    public void sendSetPassword(String email, String otp) throws MessagingException {
        String linkTemplate = "http://localhost:8080/auth/reset-password?email=%s&otp=%s";
        sendEmail(email, "Reset Password", linkTemplate, otp);
    }
}
