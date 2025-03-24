package ehb.attendify.services.mailingservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MailingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailingServiceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapperProvider() {
        return new ModelMapper();
    }

}
