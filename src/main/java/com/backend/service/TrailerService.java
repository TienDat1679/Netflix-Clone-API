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
}
