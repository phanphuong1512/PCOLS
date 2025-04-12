package fpt.swp.pcols.controller;

import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.service.ProductService;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final OllamaChatModel chatModel;
    private final ProductService productService;  // Tiêm ProductService

    @Autowired
    public ChatController(OllamaChatModel chatModel, ProductService productService) {
        this.chatModel = chatModel;
        this.productService = productService;
    }

    // Xây dựng prompt với thông tin sản phẩm lấy từ database
    private String buildAgentPrompt(String userMessage) {
        String agentInstruction;
        String productData = "";

        // Kiểm tra nếu câu hỏi có chứa từ khóa tư vấn sản phẩm
        String lowerMsg = userMessage.toLowerCase();
        if (lowerMsg.contains("recommend") || lowerMsg.contains("best") ||
                lowerMsg.contains("suggest") || lowerMsg.contains("advisor") ||
                lowerMsg.contains("advice")) {

            agentInstruction = "You are a product consultant and advisor. Provide detailed product recommendations including key features, pros and cons, and comparisons based on the product list below.";

            // Lấy danh sách sản phẩm từ database – giới hạn ví dụ 5 sản phẩm
            List<Product> products = productService.getAllProducts();
            StringBuilder sb = new StringBuilder();
            sb.append("Product List:\n");
            int count = 0;
            for (Product p : products) {
                // Giả sử Product có các phương thức getName() và getPrice()
                sb.append(String.format("%d. %s - Price: $%.2f\n", ++count, p.getName(), p.getPrice()));
                if (count >= 5) break; // chỉ lấy 5 sản phẩm đầu tiên
            }
            productData = sb.toString();
        } else {
            agentInstruction = "You are a friendly and knowledgeable customer service agent. Answer questions in a clear and helpful manner.";
        }

        return agentInstruction + "\n" + productData + "\nUser: " + userMessage + "\nAgent:";
    }

    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        // Ví dụ: set isEmail = false cho các câu hỏi từ web
        String prompt = buildAgentPrompt(message);
        return Map.of("generation", this.chatModel.call(prompt));
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        String promptText = buildAgentPrompt(message);
        Prompt prompt = new Prompt(new UserMessage(promptText));
        return this.chatModel.stream(prompt);
    }
}
