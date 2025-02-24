package com.backend.service;


import com.backend.entity.TVSerie;
import com.backend.repository.TVSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TVSerieService {

    @Autowired
    private TVSerieRepository tvSerieRepository;

    public List<TVSerie> findAllTVSeries() {
        return tvSerieRepository.findAll();
    }

    public Optional<TVSerie> findTVSerieById(Long id) {
        return  tvSerieRepository.findById(id);
    }
}
