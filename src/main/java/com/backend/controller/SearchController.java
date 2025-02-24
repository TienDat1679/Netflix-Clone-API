package com.backend.controller;


import com.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("")
    public ResponseEntity<?> getMoviesAndSeries(@RequestParam("key") String key){
        System.out.println(key);
        Map<String, Object> results = searchService.searchByTitleOrName(key);
        return ResponseEntity.ok(results);

    }
}
