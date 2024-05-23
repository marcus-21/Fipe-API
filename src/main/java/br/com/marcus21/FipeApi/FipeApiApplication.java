package br.com.marcus21.FipeApi;

import br.com.marcus21.FipeApi.start.Start;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FipeApiApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(FipeApiApplication.class, args);
	}
	@Override
	public void run (String... args) throws Exception{
		Start start = new Start();
		start.exibirMenu();
	}
}
