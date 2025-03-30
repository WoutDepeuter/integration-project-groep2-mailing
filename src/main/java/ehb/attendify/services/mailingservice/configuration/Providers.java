package ehb.attendify.services.mailingservice.configuration;

import com.sendgrid.SendGrid;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.beans.factory.annotation.Value;
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
    public SendGrid sendGridProvider(@Value("${sendgrid.api-key}") String apiKey) {
        return new SendGrid(apiKey);
    }

}
