package com.backend.service;

import com.backend.entity.Trailer;
import com.backend.repository.TrailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrailerService {
    @Autowired
    private TrailerRepository trailerRepository;

    public List<Trailer> getTrailersBySeriesId(Long seriesId) {
        return trailerRepository.findByTvSerie_Id(seriesId);
    }
    public List<Trailer> getTrailersMovie(Long movieId) {
        return trailerRepository.findByMovie_Id(movieId);
    }

    public List<Trailer> getTrailersByMediaId(Long mediaId) {
        List<Trailer> trailers = trailerRepository.findByMovie_Id(mediaId);
        if (trailers.isEmpty()) {
            trailers = trailerRepository.findByTvSerie_Id(mediaId);
        }
        return trailers;
    }
}
