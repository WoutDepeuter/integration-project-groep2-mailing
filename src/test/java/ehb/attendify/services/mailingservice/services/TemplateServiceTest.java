package ehb.attendify.services.mailingservice.services;

import ehb.attendify.services.mailingservice.dto.TemplateDto;
import ehb.attendify.services.mailingservice.models.enums.ContentType;
import ehb.attendify.services.mailingservice.models.template.Template;
import ehb.attendify.services.mailingservice.models.template.UserLocationType;
import ehb.attendify.services.mailingservice.repositories.TemplateRepository;
import ehb.attendify.services.mailingservice.services.api.TemplateUpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    private TemplateServiceImpl templateService;

    private TemplateDto templateDto;
    private Template template;
    private final String exchange = "test-exchange";
    private final String routingKey = "test-routing-key";

    @BeforeEach
    void setUp() {
        templateDto = new TemplateDto();
        templateDto.setExchange(exchange);
        templateDto.setRoutingKey(routingKey);
        templateDto.setVersion(2);
        templateDto.setDisplayName("Test Template");
        templateDto.setContentType(ContentType.TEXT_HTML);
        templateDto.setSubject("Test Subject");
        templateDto.setTemplate("<html><body>Test Template</body></html>");
        templateDto.setUserLocation("user-location");
        templateDto.setUserLocationType(UserLocationType.SINGLE);

        template = Template.builder()
                .id(1L)
                .exchange(exchange)
                .routingKey(routingKey)
                .version(1)
                .displayName("Old Template")
                .contentType(ContentType.TEXT_HTML)
                .subject("Old Subject")
                .template("<html><body>Old Template</body></html>")
                .userLocation("old-user-location")
                .userLocationType(UserLocationType.SINGLE)
                .build();

        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(resolver);

        this.templateService = new TemplateServiceImpl(this.templateRepository, templateEngine);
    }

    @Test
    void updateTemplateNew() {
        when(templateRepository.getFirstByExchangeAndRoutingKey(exchange, routingKey))
                .thenReturn(Optional.empty());
        when(templateRepository.save(any(Template.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TemplateUpdateResponse response = templateService.updateTemplate(templateDto);

        assertTrue(response.isHasUpdated());
        assertNull(response.getUpdatedFrom());
        assertEquals(2, response.getUpdatedTo());

        verify(templateRepository).getFirstByExchangeAndRoutingKey(exchange, routingKey);
        verify(templateRepository).save(any(Template.class));
    }

    @Test
    void updateTemplateSameVersion() {
        template.setVersion(2);
        when(templateRepository.getFirstByExchangeAndRoutingKey(exchange, routingKey))
                .thenReturn(Optional.of(template));

        TemplateUpdateResponse response = templateService.updateTemplate(templateDto);

        assertFalse(response.isHasUpdated());
        assertEquals(2, response.getUpdatedFrom());
        assertEquals(2, response.getUpdatedTo());

        verify(templateRepository).getFirstByExchangeAndRoutingKey(exchange, routingKey);
        verify(templateRepository, never()).save(any(Template.class));
    }

    @Test
    void updateTemplateOlderVersion() {
        template.setVersion(3);
        when(templateRepository.getFirstByExchangeAndRoutingKey(exchange, routingKey))
                .thenReturn(Optional.of(template));

        TemplateUpdateResponse response = templateService.updateTemplate(templateDto);

        assertFalse(response.isHasUpdated());
        assertEquals(3, response.getUpdatedFrom());
        assertEquals(2, response.getUpdatedTo());

        verify(templateRepository).getFirstByExchangeAndRoutingKey(exchange, routingKey);
        verify(templateRepository, never()).save(any(Template.class));
    }

    @Test
    void updateTemplateNewerVersion() {
        template.setVersion(1);
        when(templateRepository.getFirstByExchangeAndRoutingKey(exchange, routingKey))
                .thenReturn(Optional.of(template));
        when(templateRepository.save(any(Template.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TemplateUpdateResponse response = templateService.updateTemplate(templateDto);

        assertTrue(response.isHasUpdated());
        assertEquals(1, response.getUpdatedFrom());
        assertEquals(2, response.getUpdatedTo());

        verify(templateRepository).getFirstByExchangeAndRoutingKey(exchange, routingKey);
        verify(templateRepository).save(any(Template.class));
    }

    @Test
    void updateTemplateUpdate() {
        template.setVersion(1);
        when(templateRepository.getFirstByExchangeAndRoutingKey(exchange, routingKey))
                .thenReturn(Optional.of(template));
        when(templateRepository.save(any(Template.class))).thenAnswer(invocation -> {
            Template savedTemplate = invocation.getArgument(0);
            assertEquals(templateDto.getTemplate(), savedTemplate.getTemplate());
            assertEquals(templateDto.getVersion(), savedTemplate.getVersion());
            assertEquals(templateDto.getDisplayName(), savedTemplate.getDisplayName());
            assertEquals(templateDto.getSubject(), savedTemplate.getSubject());
            assertEquals(templateDto.getContentType(), savedTemplate.getContentType());
            assertEquals(templateDto.getUserLocation(), savedTemplate.getUserLocation());
            assertEquals(templateDto.getUserLocationType(), savedTemplate.getUserLocationType());
            return savedTemplate;
        });

        TemplateUpdateResponse response = templateService.updateTemplate(templateDto);

        assertTrue(response.isHasUpdated());
        assertEquals(1, response.getUpdatedFrom());
        assertEquals(2, response.getUpdatedTo());

        verify(templateRepository).getFirstByExchangeAndRoutingKey(exchange, routingKey);
        verify(templateRepository).save(any(Template.class));
    }

    @Test
    void getTemplate() {
        when(templateRepository.getFirstByExchangeAndRoutingKey(exchange, routingKey))
                .thenReturn(Optional.of(template));

        Optional<Template> result = templateService.getTemplate(exchange, routingKey);

        assertTrue(result.isPresent());
        assertEquals(template, result.get());

        verify(templateRepository).getFirstByExchangeAndRoutingKey(exchange, routingKey);
    }
}
