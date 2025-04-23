package ehb.attendify.services.mailingservice.configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridAPI;
import ehb.attendify.services.mailingservice.mappers.ByteArrayToStringMapper;
import ehb.attendify.services.mailingservice.mappers.StringToByteArrayMapper;
import ehb.attendify.services.mailingservice.services.external.DefaultSendGridAPI;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Optional;

@Log4j2
@Configuration
public class Providers {

    @Bean
    public ModelMapper modelMapperProvider() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new StringToByteArrayMapper());
        modelMapper.addConverter(new ByteArrayToStringMapper());

        return modelMapper;
    }

    @Bean
    public Jackson2XmlMessageConverter xmlMessageConverter() {
        return new Jackson2XmlMessageConverter();
    }

    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }

    @Bean
    public SendGridAPI sendGridProvider(@Value("${sendgrid.api-key:#{null}}") Optional<String> apiKey) {
        if (apiKey.isEmpty() || apiKey.get().isEmpty()) {
            log.warn("No SendGrid API key provided, falling back to a NOP implementation");
            return new DefaultSendGridAPI();
        }

        return new SendGrid(apiKey.get());
    }

    @Bean
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

}
