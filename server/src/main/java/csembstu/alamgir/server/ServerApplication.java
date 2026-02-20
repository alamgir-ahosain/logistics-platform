package csembstu.alamgir.server;

import csembstu.alamgir.server.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {

		EnvConfig.loadEnv();
		SpringApplication.run(ServerApplication.class, args);
	}

}
