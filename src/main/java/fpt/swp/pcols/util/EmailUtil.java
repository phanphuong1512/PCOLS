package fpt.swp.pcols.util;

import fpt.swp.pcols.entity.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    private final JavaMailSender javaMailSender;
    private final EmailContentBuilder emailContentBuilder;

    // Phương thức gửi email chung, nhận vào nội dung HTML đã được tạo sẵn
    private void sendEmail(String email, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }

    // Phương thức tạo nội dung HTML cho OTP (để giữ lại chức năng cũ)
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
        sendEmail(email, "Email Verification", getHtmlContent(email, otp, linkTemplate));
    }

    public void sendSetPassword(String email, String otp) throws MessagingException {
        String linkTemplate = "http://localhost:8080/auth/reset-password?email=%s&otp=%s";
        sendEmail(email, "Reset Password", getHtmlContent(email, otp, linkTemplate));
    }

    // Phương thức gửi email xác nhận đơn hàng sử dụng Thymeleaf để render template order-success.html
    @Async
    public void sendOrderSuccessEmail(String email, Order order) {
        try {
            logger.debug("Starting to send email to {} for order ID: {}", email, order.getId());
            String htmlContent = emailContentBuilder.buildOrderSuccessEmail(order, true);
            sendEmail(email, "Đơn hàng của bạn đã được xác nhận", htmlContent);
            logger.info("Email sent successfully to {} for order ID: {}", email, order.getId());
        } catch (MessagingException e) {
            logger.error("Failed to send email to {} for order ID: {}: {}", email, order.getId(), e.getMessage(), e);
        }
    }
}
