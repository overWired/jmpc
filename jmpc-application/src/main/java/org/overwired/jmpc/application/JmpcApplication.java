package org.overwired.jmpc.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.overwired.jmpc")
public class JmpcApplication {
	public static void main(String[] args) {
		SpringApplication.run(JmpcApplication.class, args);
	}
}
