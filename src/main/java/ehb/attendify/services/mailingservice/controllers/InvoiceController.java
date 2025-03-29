package ehb.attendify.services.mailingservice.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@DependsOn("dynamic_bindings")
public class InvoiceController {

    @RabbitListener(queues = "#{rabbitMQConfig.queueNames[T(ehb.attendify.services.mailingservice.configuration.Constants).INVOICE_SEND]}")
    public void onInvoiceSend(String name) {
        log.info("On invoice send with name {}", name);
    }

}
