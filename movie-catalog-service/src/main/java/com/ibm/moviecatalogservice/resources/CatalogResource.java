package com.ibm.moviecatalogservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.ibm.moviecatalogservice.models.CatalogItem;
import com.ibm.moviecatalogservice.models.Movie;
import com.ibm.moviecatalogservice.models.Rating;
import com.ibm.moviecatalogservice.models.UserRating;
import com.ibm.moviecatalogservice.services.MovieInfo;
import com.ibm.moviecatalogservice.services.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;
    
    @Autowired
    MovieInfo movieInfo;
    
    @Autowired
    UserRatingInfo userRatingInfo;
    
    
    
  //LEAVE EVERYTHING DEFAULT AND CONFIGURE FALLBACK 

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = userRatingInfo.getUserRating(userId);

        return userRating.getRatings().stream()
                .map(rating -> movieInfo.getCatalogItem(rating))
                .collect(Collectors.toList());

    }

   

  
    
    
}

//KILL INFO SERVICE TO TEST THE SERVICE.
//localhost:8081/catalog/bvr

/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/