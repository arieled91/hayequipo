package org.arieled91.hayequipo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Value("${frontend.host}")
    private final String frontendHost = "localhost";
    @Value("${frontend.port}")
    private final String frontendPort = "4200";

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://"+frontendHost+":"+frontendPort,"*")
                        .allowedMethods("GET","POST","PATCH","PUT","DELETE")
                        .allowedHeaders("*");
			}
		};
	}
}
