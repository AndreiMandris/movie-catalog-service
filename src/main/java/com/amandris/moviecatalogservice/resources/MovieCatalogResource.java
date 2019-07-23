package com.amandris.moviecatalogservice.resources;

import com.amandris.moviecatalogservice.model.CatalogItem;
import com.amandris.moviecatalogservice.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.amandris.moviecatalogservice.model.Movie;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId){

        List<Rating> ratings = Arrays.asList(
                new Rating("1", 4),
                new Rating("2", 4)
        );

        return ratings.stream().map(this::createItem)
                .collect(Collectors.toList());
    }

    private CatalogItem createItem(Rating rating){
//        Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

        Movie movie = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/movies/")
                .retrieve()
                .bodyToMono(Movie.class)
                .block();

        return new CatalogItem(movie.getName(), "test", rating.getRating());
    }


