package ehb.attendify.services.mailingservice.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGridAPI;
import ehb.attendify.services.mailingservice.models.GenericEmail;
import ehb.attendify.services.mailingservice.models.enums.ContentType;
import ehb.attendify.services.mailingservice.models.mail.body.Body;
import ehb.attendify.services.mailingservice.models.mail.header.Header;
import ehb.attendify.services.mailingservice.models.mail.header.Recipient;
import ehb.attendify.services.mailingservice.services.api.MetricService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendGridEmailServiceTest {

    @Mock
    private SendGridAPI sendGrid;

    @Mock
    private MetricService metricService;

    @Mock
    private io.micrometer.core.instrument.Counter mailCounter;

    @Captor
    private ArgumentCaptor<Request> requestCaptor;

    @InjectMocks
    private SendGridEmailServiceImpl emailService;

    private final String fromEmail = "test@example.com";
    private GenericEmail email;
    private Response successResponse;
    private Response failureResponse;

    @BeforeEach
    void setUp() throws IOException {
        emailService = new SendGridEmailServiceImpl(sendGrid, fromEmail, metricService);

        successResponse = new Response();
        successResponse.setStatusCode(202);

        failureResponse = new Response();
        failureResponse.setStatusCode(400);
        failureResponse.setBody("Bad Request");

        Header header = Header.builder()
                .subject("Test Subject")
                .recipients(List.of(new Recipient("recipient@example.com")))
                .build();

        Body body = Body.builder()
                .content("Test content")
                .contentType(ContentType.TEXT_PLAIN)
                .build();

        email = GenericEmail.builder()
                .header(header)
                .body(body)
                .build();
    }

    @Test
    void sendEmail() throws IOException {
        when(metricService.getMailCounter()).thenReturn(mailCounter);
        when(sendGrid.api(any(Request.class))).thenReturn(successResponse);


        emailService.sendEmail(email);


        verify(sendGrid).api(requestCaptor.capture());
        Request capturedRequest = requestCaptor.getValue();

        assertEquals(Method.POST, capturedRequest.getMethod());
        assertEquals("mail/send", capturedRequest.getEndpoint());
        assertNotNull(capturedRequest.getBody());

        verify(metricService).getMailCounter();
        verify(mailCounter).increment();
    }

    @Test
    void sendEmailNullHeader() throws IOException {
        email.setHeader(null);

        emailService.sendEmail(email);

        verify(sendGrid, never()).api(any(Request.class));
        verify(metricService, never()).getMailCounter();
        verify(mailCounter, never()).increment();
    }

    @Test
    void sendEmailNoReceptions() throws IOException {
        email.getHeader().setRecipients(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendEmail(email);
        });

        assertEquals("No recipient", exception.getMessage());

        verify(sendGrid, never()).api(any(Request.class));
        verify(metricService, never()).getMailCounter();
        verify(mailCounter, never()).increment();
    }
}
