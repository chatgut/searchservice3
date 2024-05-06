package org.example.searchservice3.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.searchservice3.service.SearchService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

@Service
@RabbitListener(queues = "messages")
public class MyMessageHandler implements MessageListener {
    private final SearchService searchService;

    public MyMessageHandler(SearchService searchService) {
        this.searchService = searchService;
    }


    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            // Extract relevant information from the message body
            // For example, assuming the message body contains a JSON object with a "text" field:
            JsonNode messageJson = new ObjectMapper().readTree(messageBody);
            String text = messageJson.get("text").asText();

            // Print a test message to indicate that the queue is registered
            System.out.println("Queue 'messages' is registered. Received message: " + text);

            // Pass the extracted information to the search service for indexing
            searchService.indexMessage(text);
        } catch (IOException e) {
            // Handle any exceptions that occur during message processing
            e.printStackTrace();
        }
    }
}