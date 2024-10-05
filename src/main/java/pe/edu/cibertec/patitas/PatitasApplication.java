package pe.edu.cibertec.patitas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PatitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatitasApplication.class, args);
	}

}
