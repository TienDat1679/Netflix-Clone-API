package com.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.backend.dto.TMDB.MovieDto;
import com.backend.dto.TMDB.TMDBResponse;
import com.backend.entity.Credit;
import com.backend.entity.Episode;
import com.backend.entity.Genre;
import com.backend.entity.Movie;
import com.backend.entity.TVSerie;
import com.backend.repository.CreditRepository;
import com.backend.repository.EpisodeRepository;
import com.backend.repository.GenreRepository;
import com.backend.repository.MovieRepository;
import com.backend.repository.TVSerieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;

@Service
public class TMDBService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private TVSerieRepository tvSerieRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    public void fetchAndSaveMovies() {
        try {
            // Gọi API để lấy danh sách phim phổ biến
            String url = apiUrl + "/movie/popular?api_key=" + apiKey + "&language=vi-VN&page=2";
            String response = restTemplate.getForObject(url, String.class);

            // Chuyển đổi JSON thành danh sách phim
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.get("results");

            List<Movie> moviesToSave = new ArrayList<>();

            for (JsonNode movieNode : results) {
                Long id = movieNode.get("id").asLong();
                String title = movieNode.get("title").asText();
                String overview = movieNode.get("overview").asText();
                String releaseDate = movieNode.get("release_date").asText();
                String posterPath = movieNode.get("poster_path").asText();
                String backdropPath = movieNode.get("backdrop_path").asText();
                boolean adult = movieNode.get("adult").asBoolean();
                boolean video = movieNode.get("video").asBoolean();
                int runtime = fetchMovieRuntime(movieNode.get("id").asLong());

                // Kiểm tra xem phim đã tồn tại trong database chưa
                Optional<Movie> existingMovie = movieRepository.findByTitle(title);
                if (existingMovie.isPresent())
                    continue;

                Movie movie = new Movie();
                movie.setId(id);
                movie.setTitle(title);
                movie.setOverview(overview);
                movie.setReleaseDate(releaseDate);
                movie.setPosterPath(posterPath);
                movie.setBackdropPath(backdropPath);
                movie.setAdult(adult);
                movie.setVideo(video);
                movie.setRuntime(runtime);

                // Lấy danh sách thể loại và credits
                movie.setGenres(fetchGenres(movieNode.get("id").asLong()));
                movie.setCredits(fetchCredits(movieNode.get("id").asLong()));

                moviesToSave.add(movie);
            }

            movieRepository.saveAll(moviesToSave);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int fetchMovieRuntime(Long movieId) {
        try {
            String url = apiUrl + "/movie/" + movieId + "?api_key=" + apiKey;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("runtime").asInt();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private List<Genre> fetchGenres(Long movieId) {
        List<Genre> genres = new ArrayList<>();
        try {
            String url = apiUrl + "/movie/" + movieId + "?api_key=" + apiKey + "&language=vi-VN";
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response).get("genres");

            for (JsonNode genreNode : jsonNode) {
                Long genreId = genreNode.get("id").asLong();
                String genreName = genreNode.get("name").asText();

                // Kiểm tra xem thể loại đã có chưa
                Optional<Genre> existingGenre = genreRepository.findById(genreId);
                Genre genre = existingGenre.orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setId(genreId);
                    newGenre.setName(genreName);
                    return genreRepository.save(newGenre);
                });

                genres.add(genre);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return genres;
    }

    private List<Credit> fetchCredits(Long movieId) {
        List<Credit> credits = new ArrayList<>();
        try {
            String url = apiUrl + "/movie/" + movieId + "/credits?api_key=" + apiKey + "&language=vi-VN";
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response).get("cast");

            for (JsonNode creditNode : jsonNode) {
                Long creditId = creditNode.get("id").asLong();
                String name = creditNode.get("name").asText();
                String character = creditNode.get("known_for_department").asText();
                String profilePath = creditNode.get("profile_path").asText();

                // Kiểm tra xem credit đã có chưa
                Optional<Credit> existingCredit = creditRepository.findById(creditId);
                Credit credit = existingCredit.orElseGet(() -> {
                    Credit newCredit = new Credit();
                    newCredit.setId(creditId);
                    newCredit.setName(name);
                    newCredit.setKnownForDepartment(character);
                    newCredit.setProfilePath(profilePath);
                    return creditRepository.save(newCredit);
                });

                credits.add(credit);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return credits;
    }

    public void fetchAndSaveTVSeries() {
        try {
            String url = apiUrl + "/tv/popular?api_key=" + apiKey + "&language=vi-VN&page=2";
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.get("results");

            List<TVSerie> tvSeriesList = new ArrayList<>();

            for (JsonNode seriesNode : results) {
                Long id = seriesNode.get("id").asLong();
                String name = seriesNode.get("name").asText();
                String overview = seriesNode.get("overview").asText();
                String firstAirDate = seriesNode.get("first_air_date").asText();
                String posterPath = seriesNode.get("poster_path").asText();
                String backdropPath = seriesNode.get("backdrop_path").asText();
                boolean adult = seriesNode.get("adult").asBoolean();

                Optional<TVSerie> existingSeries = tvSerieRepository.findByName(name);
                if (existingSeries.isPresent()) continue;

                TVSerie tvSeries = new TVSerie();
                tvSeries.setId(id);
                tvSeries.setName(name);
                tvSeries.setOverview(overview);
                tvSeries.setFirstAirDate(firstAirDate);
                tvSeries.setPosterPath(posterPath);
                tvSeries.setBackdropPath(backdropPath);
                tvSeries.setAdult(adult);

                tvSerieRepository.save(tvSeries);

                tvSeries.setEpisodes(fetchEpisodes(seriesNode.get("id").asLong()));
                tvSeries.setGenres(fetchGenres(seriesNode.get("id").asLong(), "tv"));
                tvSeries.setCredits(fetchCredits(seriesNode.get("id").asLong(), "tv"));

                tvSeriesList.add(tvSeries);
            }
            tvSerieRepository.saveAll(tvSeriesList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Genre> fetchGenres(Long id, String type) {
        List<Genre> genres = new ArrayList<>();
        try {
            String url = apiUrl + "/" + type + "/" + id + "?api_key=" + apiKey + "&language=vi-VN";
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response).get("genres");

            for (JsonNode genreNode : jsonNode) {
                Long genreId = genreNode.get("id").asLong();
                String genreName = genreNode.get("name").asText();

                Optional<Genre> existingGenre = genreRepository.findById(genreId);
                Genre genre = existingGenre.orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setId(genreId);
                    newGenre.setName(genreName);
                    return genreRepository.save(newGenre);
                });

                genres.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genres;
    }

    private List<Credit> fetchCredits(Long id, String type) {
        List<Credit> credits = new ArrayList<>();
        try {
            String url = apiUrl + "/" + type + "/" + id + "/credits?api_key=" + apiKey + "&language=vi-VN";
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response).get("cast");

            for (JsonNode creditNode : jsonNode) {
                Long creditId = creditNode.get("id").asLong();
                String name = creditNode.get("name").asText();
                String character = creditNode.get("known_for_department").asText();
                String profilePath = creditNode.get("profile_path").asText();

                Optional<Credit> existingCredit = creditRepository.findById(creditId);
                Credit credit = existingCredit.orElseGet(() -> {
                    Credit newCredit = new Credit();
                    newCredit.setId(creditId);
                    newCredit.setName(name);
                    newCredit.setKnownForDepartment(character);
                    newCredit.setProfilePath(profilePath);
                    return creditRepository.save(newCredit);
                });

                credits.add(credit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return credits;
    }

    private List<Episode> fetchEpisodes(Long seriesId) {
        List<Episode> episodes = new ArrayList<>();
        try {
            for (int season = 1; season <= 5; season++) {
                try {
                    String url = apiUrl + "/tv/" + seriesId + "/season/" + season + "?api_key=" + apiKey + "&language=vi-VN";
                    String response = restTemplate.getForObject(url, String.class);

                    JsonNode root = objectMapper.readTree(response);
                    JsonNode episodeNodes = root.get("episodes");

                    if (episodeNodes == null || !episodeNodes.isArray()) {
                        System.err.println("Không tìm thấy tập phim nào cho Season " + season + " của TV Series ID " + seriesId);
                        continue; // Bỏ qua season này nếu không có dữ liệu
                    }

                    for (JsonNode episodeNode : episodeNodes) {
                        Episode episode = new Episode();
                        episode.setId(episodeNode.get("id").asLong());
                        episode.setSeasonNumber(season);
                        episode.setEpisodeNumber(episodeNode.get("episode_number").asInt());
                        episode.setName(episodeNode.get("name").asText());
                        episode.setOverview(episodeNode.get("overview").asText());
                        episode.setAirDate(episodeNode.get("air_date").asText());
                        episode.setRuntime(episodeNode.get("runtime").asInt());
                        episode.setTvSerie(tvSerieRepository.findById(seriesId)
                                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy TV Series ID " + seriesId)));

                        // Kiểm tra still_path có null không trước khi lấy giá trị
                        episode.setStillPath(episodeNode.has("still_path") && !episodeNode.get("still_path").isNull()
                                ? episodeNode.get("still_path").asText() 
                                : null);

                        episodes.add(episode);
                    }
                } catch (HttpClientErrorException.NotFound e) {
                    // Nếu season không tồn tại (404), chỉ log lỗi và tiếp tục với season tiếp theo
                    System.err.println("Không tìm thấy dữ liệu cho Season " + season + " của TV Series ID " + seriesId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return episodes;
    }

    // public MovieDto getMovieDetails(int movieId) {
    //     String url = UriComponentsBuilder.fromUriString(apiUrl + "/movie/" + movieId)
    //             .queryParam("api_key", apiKey)
    //             .toUriString();

    //     return restTemplate.getForObject(url, MovieDto.class);
    // }

    // public List<MovieDto> searchMovies(String query) {
    //     String url = UriComponentsBuilder.fromUriString(apiUrl + "/search/movie")
    //             .queryParam("api_key", apiKey)
    //             .queryParam("query", query)
    //             .toUriString();

    //     TMDBResponse response = restTemplate.getForObject(url, TMDBResponse.class);
    //     return response != null ? response.getResults() : List.of();
    // }

    // public List<MovieDto> getPopularMovies() {
    //     String url = UriComponentsBuilder.fromUriString(apiUrl + "/movie/popular")
    //             .queryParam("api_key", apiKey)
    //             .toUriString();

    //     TMDBResponse response = restTemplate.getForObject(url, TMDBResponse.class);
    //     return response != null ? response.getResults() : List.of();
    // }

}
