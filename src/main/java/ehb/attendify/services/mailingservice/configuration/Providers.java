package ehb.attendify.services.mailingservice.configuration;

import com.sendgrid.SendGrid;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Providers {

    @Bean
    public ModelMapper modelMapperProvider() {
        return new ModelMapper();
    }

    @Bean
    public Jackson2XmlMessageConverter xmlMessageConverter() {
        return new Jackson2XmlMessageConverter();
    }

    @Bean
    public SendGrid sendGridProvider() {
        return new SendGrid(System.getenv("SENDGRID_API_KEY"));
    }

}
