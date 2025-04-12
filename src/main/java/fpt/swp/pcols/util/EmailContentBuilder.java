package fpt.swp.pcols.util;

import fpt.swp.pcols.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailContentBuilder {

    private static final Logger logger = LoggerFactory.getLogger(EmailContentBuilder.class);

    private final TemplateEngine templateEngine;

    public EmailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildOrderSuccessEmail(Order order, boolean isEmail) {
        logger.debug("Building email content for order ID: {}", order.getId());

        Context context = new Context();

        // Quan trọng: Truyền biến isEmail xuống template để tránh lỗi chuyển đổi null -> boolean
        context.setVariable("isEmail", isEmail);

        // Thiết lập URL động dựa trên isEmail
        String baseUrl = isEmail ? "http://localhost:8080" : "";
        context.setVariable("metaDescription", "Chi tiết đơn hàng");
        context.setVariable("faviconUrl", baseUrl + "/assets/images/favicon.ico");
        context.setVariable("cssPluginsUrl", baseUrl + "/css/plugins.css");
        context.setVariable("orderSuccessCssUrl", baseUrl + "/css/order-success.css");
        context.setVariable("responsiveCssUrl", baseUrl + "/css/responsive.css");
        context.setVariable("fontAwesomeUrl", baseUrl + "/css/font-awesome.min.css");
        context.setVariable("productDetailUrlPrefix", baseUrl + "/product-detail");
        context.setVariable("homeUrl", baseUrl + "/home");

        // Truyền dữ liệu đơn hàng
        context.setVariable("order", order);
        context.setVariable("orderDetails", order.getOrderDetails());

        // Render template order-success
        String html = templateEngine.process("order-success", context);
        logger.debug("Email content built successfully for order ID: {}", order.getId());
        return html;
    }
}
