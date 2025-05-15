package com.backend.controller;

import java.util.List;
import java.util.Optional;

import com.backend.entity.Episode;
import com.backend.entity.TVSerie;
import com.backend.entity.Trailer;
import com.backend.repository.TVSerieRepository;
import com.backend.service.MediaService;
import com.backend.service.TVSerieService;
import com.backend.service.TrailerService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TVSerieController {
    TVSerieService tvSerieService;
    TrailerService trailerService;
    TVSerieRepository serieRepository;
    MediaService mediaService;

    @GetMapping("/{genreId}")
    public List<?> getSeriesByGenre(@PathVariable Long genreId) {
        List<TVSerie> tvSeries = serieRepository.findTvSeriesByGenreId(genreId);
        return mediaService.tvSeriesToMediaDTOList(tvSeries);
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


    @GetMapping("/esp")
    public ResponseEntity<?>  getEpsOfSeries(@RequestParam("seriesId") Long id) {
        List<Episode> epsList=tvSerieService.findAllEpisodesOfTVSerie(id);
    return ResponseEntity.ok(epsList);
    }


    @GetMapping("/trailer")
    public List<Trailer> getTrailersBySeriesId(@RequestParam("id") Long seriesId) {
        return trailerService.getTrailersBySeriesId(seriesId);
    }


}
