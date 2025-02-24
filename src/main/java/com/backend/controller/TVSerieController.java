package com.backend.controller;

import java.util.List;
import java.util.Optional;

import com.backend.entity.TVSerie;
import com.backend.service.TVSerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/series")
public class TVSerieController {

    @Autowired
    private TVSerieService tvSerieService;

    @GetMapping("")
    public ResponseEntity<?> getSeries(){

        List<TVSerie> listSeries=tvSerieService.findAllTVSeries();
        return ResponseEntity.ok(listSeries);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getEps(@RequestParam("id") Long id) {

        Optional<TVSerie> tvSerie=tvSerieService.findTVSerieById(id);
        return ResponseEntity.ok(tvSerie);
    }


}
