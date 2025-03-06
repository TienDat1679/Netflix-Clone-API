package com.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.backend.dto.MediaDTO;
import com.backend.entity.TVSerie;
import com.backend.repository.TVSerieRepository;
import com.backend.service.TVSerieService;
import com.backend.util.MediaMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/series")
public class TVSerieController {

    @Autowired
    private TVSerieService tvSerieService;

    @Autowired
    private TVSerieRepository serieRepository;

    @GetMapping("/{genreId}")
    public List<?> getSeriesByGenre(@PathVariable Long genreId) {
        List<TVSerie> tvSeries = serieRepository.findTvSeriesByGenreId(genreId);
        List<MediaDTO> mediaList = new ArrayList<>();
        mediaList.addAll(MediaMapper.toMediaDTOListFromTvSeries(tvSeries));
        return mediaList;
    }    

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

    @GetMapping("/top10")
    public List<TVSerie> getTop10Movies() {
        return serieRepository.findTop10ByOrderByVoteCountDesc();
    }
}
