package lk.dhanushkaTa.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebAppConfig {

    @Bean
    public ModelMapper setModelMapper(){
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper setObjectMapper(){
        return new ObjectMapper();
    }
}
