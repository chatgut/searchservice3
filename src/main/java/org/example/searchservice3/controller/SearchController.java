package org.example.searchservice3.controller;


import org.example.searchservice3.entities.MessageDocument;
import org.example.searchservice3.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {


    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }




    @GetMapping("/search")
    public List<MessageDocument> search(@RequestHeader(name = "UserID") String userID,
                                        @RequestParam String text,
                                        @RequestParam(required = false) String otherUserID) {

        if (otherUserID != null) {
            return searchService.searchMessagesInConversation(text, userID, otherUserID);
        } else {
            return searchService.search(text, userID);
        }
    }


    @GetMapping("/find/{text}")
    public List<MessageDocument> findAsYouType(@RequestHeader(name = "UserID") String userId, @PathVariable String text) {
        return searchService.findByMessageAsYouType(text, userId);
    }


    @GetMapping("/message/{id}")
    public String message(@PathVariable String id) {

        return searchService.findMessageById(id);
    }

    @GetMapping("/all")
    public String all() {
        return searchService.findAllMessages().toString();
    }


}
