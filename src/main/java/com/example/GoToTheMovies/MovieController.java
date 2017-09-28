package com.example.GoToTheMovies;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MovieController {

    private static String base_url = "https://image.tmdb.org/t/p/w300";


    @RequestMapping(path="/", method = RequestMethod.GET)
    public String home() {
        return "home";
    }


    @RequestMapping(path="/now-playing", method = RequestMethod.GET)
    public String nowPlaying(Model model) {

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=be2a38521a7859c95e2d73c48786e4bb";

        model.addAttribute("movies", getMovies(url));

        return "now-playing";
    }


    @RequestMapping(path="/medium-popular-long-name", method = RequestMethod.GET)
    public String mediumPopular(Model model) {

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=be2a38521a7859c95e2d73c48786e4bb";

        List<Movie> notAsPopular = new ArrayList<>();

        notAsPopular = getMovies(url).stream()
            .filter(e -> e.getPopularity() >= 30 && e.getPopularity() <= 80)
            .filter(e -> e.getTitle().length() >= 10)
            .collect(Collectors.toList());

        model.addAttribute("movies", notAsPopular);

        return "medium-popular-long-name";
    }



    public static List<Movie> getMovies(String route) {

        RestTemplate restTemplate = new RestTemplate();
        ResultsPage movies = restTemplate.getForObject(route, ResultsPage.class);

        for (Movie movie : movies.getResults()) {
            movie.setPosterPath(base_url + movie.getPosterPath());
        }

        return movies.getResults();

    }
}
