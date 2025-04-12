package fpt.swp.pcols.util;

import fpt.swp.pcols.entity.Order;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailContentBuilder {

    private final TemplateEngine templateEngine;

    public EmailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildOrderSuccessEmail(Order order) {
        Context context = new Context();
        // Đánh dấu đây là render cho email
        context.setVariable("isEmail", true);

        // Các biến dùng cho meta, favicon, CSS với URL tuyệt đối
        context.setVariable("metaDescription", "Chi tiết đơn hàng");
        context.setVariable("faviconUrl", "http://localhost:8080/assets/images/favicon.ico");
        context.setVariable("cssPluginsAbsolute", "http://localhost:8080/css/plugins.css");
        context.setVariable("orderSuccessCssAbsolute", "http://localhost:8080/css/order-success.css");
        context.setVariable("responsiveCssAbsolute", "http://localhost:8080/css/responsive.css");
        context.setVariable("fontAwesomeAbsolute", "http://localhost:8080/css/font-awesome.min.css");

        // Biến cho URL chi tiết sản phẩm
        context.setVariable("productDetailAbsoluteUrlPrefix", "http://localhost:8080/product-detail");

        // Truyền dữ liệu đơn hàng
        context.setVariable("order", order);
        context.setVariable("orderDetails", order.getOrderDetails());

        // Render template order-success (file order-success.html nằm trong thư mục resources/templates)
        return templateEngine.process("order-success", context);
    }
}
