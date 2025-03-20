package fpt.swp.pcols;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Kiểm tra xem DB_URL có được load không
        System.out.println("🔍 DB_URL from .env: " + dotenv.get("DB_URL"));
        System.out.println("🔍 System.getenv DB_URL: " + System.getenv("DB_URL"));
        SpringApplication.run(Main.class, args);
        System.out.println("Build Successful :DD");
    }
}
