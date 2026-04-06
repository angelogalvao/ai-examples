package io.github.angelogalvao.example.ai.resource;

import io.github.angelogalvao.example.ai.embedding.EmbeddingCalculator;
import io.github.angelogalvao.example.ai.helper.MoviesParser;
import io.github.angelogalvao.example.ai.model.Movie;
import io.github.angelogalvao.example.ai.model.MovieDto;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Random;

@Path("/movies/api")
public class MoviesResource {

    public record MovieApiDto(String poster, String name, String plot, String director, double rating){}

    @Inject
    MoviesParser moviesParser;

    @Inject
    EmbeddingCalculator embeddingCalculator;

    @ConfigProperty(name = "embedding.calculate")
    boolean embeddingCalculate;

    @Inject
    Logger logger;

//    @RestClient
//    TmdbService tmdbService;

    // @ConfigProperty(name = "tmdb.api.key")
    String tmdbAPiKey;

    @ConfigProperty(name = "poster.download")
    boolean posterDownload;

    List<String> highRatings = List.of("Clint Eastwood", "Steven Spielberg", "Christopher Nolan", "Hayao Miyazaki");

    Random random = new Random();

    @Startup
    @Transactional
    @TransactionConfiguration(timeout = 500)
    public void startup() {

        // Clint Eastwood, Steven Spielberg, Christopher Nolan

        if (embeddingCalculate) {

            final List<MovieDto> movieDtos = moviesParser
                    .loadMoviesGreaterThanReleaseDate(2007);

            try (ProgressBar pb = new ProgressBarBuilder()
                    .setTaskName("Importing Movies")
                    .setInitialMax(movieDtos.size())
                    .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
                    .build()) {
                movieDtos.stream()
                        .map(m -> {
                            float[] vector = embeddingCalculator.calculateVector(m);
                            return new Movie(m.title(), m.director(), m.plot(), calculateRating(m), vector);
                        })
                        .forEach(m -> {
                            m.persist();
                            pb.setExtraMessage(m.title);
                            pb.step();
                        });
            }
        } else {
            logger.info("Importing Data from tmpimport.sql file");
        }
    }

    @GET
    @Path("/search")
    public List<MovieApiDto> recommendMovies(@QueryParam("q") String description) {

        float[] plotVector = embeddingCalculator.calculateVector(description);

        List<Movie> movies = Movie.suggestProducts(plotVector, highRatings);
        return movies.stream()
                //.map(m -> new MovieApiDto(findPoster(m.title), m.title, cutPlot(m.plot), m.director, m.rating))
                .map(m -> new MovieApiDto(m.title, m.title, m.plot, m.director, m.rating))
                .toList();

    }

    private String findPoster(String title) {
//        if (posterDownload) {
//            TmdbService.TmdbMovies tmdbMovies = tmdbService.searchMovie(tmdbAPiKey, title);
//            return "http://image.tmdb.org/t/p/w500/" + tmdbMovies.results().getFirst().poster_path();
//        }

        return "";
    }

    private String cutPlot(String plot) {
        return plot.substring(0, Math.min(plot.length(), 400));
    }

    private double calculateRating(MovieDto movieDto) {
        double rating = 0;

        if (highRatings.contains(movieDto.director())) {
            rating = random.nextDouble(4,5);
        }
        else {
            rating = random.nextDouble(0, 5);
        }

        return  Math.round(rating * 10) / 10.0;
    }

}
