package com.swp392.PCOLS.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        //        SimpleMailMessage message = new SimpleMailMessage();
        //        message.setTo(email);
        //        message.setFrom("");
        //        message.setSubject("Verify OTP");
        //        message.setText("Your OTP is: " + otp);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(email);
        helper.setFrom("phuongpvhe170793@fpt.edu.vn");
        helper.setSubject("burrito muprup");
        String htmlContent = getHtmlContent(email, otp);
        // setText(content, true) => true để gửi dạng HTML
        helper.setText(htmlContent, true);
        // Thực hiện gửi
        javaMailSender.send(mimeMessage);
    }

    private String getHtmlContent(String email, String otp) {
        String verifyLink = String.format(
                "http://localhost:8080/auth/verify-account?email=%s&otp=%s",
                email, otp
        );

        // Nội dung email dạng HTML
        // Bạn có thể viết thêm nội dung, styling tuỳ ý
        return String.format(
                """
                       
                        <html>
                          <body>
                            <p>Chào bạn,</p>
                            <p>Vui lòng bấm vào link bên dưới để xác thực tài khoản:</p>
                            <a href="%s" target="_blank">Click để xác thực</a>
                            <p>OTP của bạn là: <b>%s</b></p>
                          </body>
                        </html>
                        """,
                verifyLink, otp
        );
    }
}
