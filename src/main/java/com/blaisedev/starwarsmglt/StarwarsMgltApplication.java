package com.blaisedev.starwarsmglt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@SpringBootApplication
public class StarwarsMgltApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarwarsMgltApplication.class, args);
	}

	@Autowired
	UserAgentInterceptor userAgentInterceptor;
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(userAgentInterceptor));
		return restTemplate;
	}

}

@Component
class UserAgentInterceptor implements ClientHttpRequestInterceptor {


	@Value("${spring.application.name}")
	private String applicationName;

	@Override
	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
		httpRequest.getHeaders().set(HttpHeaders.USER_AGENT, applicationName);
		return clientHttpRequestExecution.execute(httpRequest, bytes);
	}
}
