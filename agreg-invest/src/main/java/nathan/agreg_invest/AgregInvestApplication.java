package nathan.agreg_invest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AgregInvestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgregInvestApplication.class, args);
	}

}
