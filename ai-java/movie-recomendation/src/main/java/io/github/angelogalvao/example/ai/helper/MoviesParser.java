package io.github.angelogalvao.example.ai.helper;

import io.github.angelogalvao.example.ai.model.MovieDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.*;
import java.util.List;

import static java.lang.Integer.parseInt;

@ApplicationScoped
public class MoviesParser {

    @Inject
    Logger logger;

    @ConfigProperty(name = "movies.file.location.csv")
    File location;

    public List<MovieDto> loadMoviesGreaterThanReleaseDate(int releaseYear) {
        try(Reader reader = new FileReader(location);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            logger.info("Start Parsing Movies");

            List<MovieDto> movieDtos = csvParser
                    .stream()
                    .filter(r -> parseInt(r.get("Release Year")) > releaseYear)
                    .map(r -> new MovieDto(r.get("Title"),
                                                      r.get("Director"),
                                                      r.get("Plot")))
                    .toList();

            logger.info("End Parsing Movies");

            return movieDtos;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
