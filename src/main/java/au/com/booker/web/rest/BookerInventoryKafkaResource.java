package au.com.booker.web.rest;

import au.com.booker.service.BookerInventoryKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booker-inventory-kafka")
public class BookerInventoryKafkaResource {

    private final Logger log = LoggerFactory.getLogger(BookerInventoryKafkaResource.class);

    private BookerInventoryKafkaProducer kafkaProducer;

    public BookerInventoryKafkaResource(BookerInventoryKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.send(message);
    }
}
