package lab.home.invoicetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class InvoiceTrackerApplication {

	private static final int ENCRYPTION_STRENGTH = 12;

    public static void main(String[] args) {
		SpringApplication.run(InvoiceTrackerApplication.class, args);
	}

	@Bean 
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder(ENCRYPTION_STRENGTH);
    }
}
