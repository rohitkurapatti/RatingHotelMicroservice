package com.lcwd.user.service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfiguration {

    @Bean
    @LoadBalanced /*Tells the template to use the service-name from eureka server as host and port instead of using the
    hardcoded host and port, so incase if in future the host and port for that particular service changes, it will not affect the
    flow of our application as we are calling it with the service name*/
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
