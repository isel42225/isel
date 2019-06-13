/*
 * Copyright (c) 2018 Miguel Gamboa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package movlazy;

import com.google.gson.Gson;
import movlazy.dto.*;
import util.IRequest;
import util.ThrowableReaderFunction;

import java.io.*;
import java.text.MessageFormat;
import java.net.URL;
import java.util.stream.Stream;

/**
 * @author Miguel Gamboa
 *         created on 16-02-2017
 */

public class MovWebApi {
    /**
     * Constants
     *
     * To format messages URLs use {@link java.text.MessageFormat#format(String, Object...)} method.
     */
    private static final String MOVIE_DB_HOST = "https://api.themoviedb.org/3/";
        private static final String MOVIE_DB_SEARCH = "search/movie?api_key={0}&query={1}&page={2}";
    private static final String MOVIE_DB_MOVIE = "movie/{1}?api_key={0}";
    private static final String MOVIE_DB_MOVIE_CREDITS = "movie/{1}/credits?api_key={0}";
    private static final String MOVIE_DB_PERSON = "person/{1}?api_key={0}";
    private static final String MOVIE_DB_PERSON_CREDITS = "person/{1,number,#}/movie_credits?api_key={0}";
    private static final String API_KEY;
    private static final String KEY_FILE = "api-key.txt";


    static {
        URL keyFile = ClassLoader.getSystemResource(KEY_FILE);
        if (keyFile == null) {
            throw new IllegalStateException(
                    "YOU MUST GET a KEY in www.themoviedb.org and place it in src/main/resources/api-key.txt");
        } else {
            try {
                InputStream keyStream = keyFile.openStream();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(keyStream))) {
                    API_KEY = reader.readLine();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    /*
     * Constructors
     */
    private IRequest req;
    private final Gson gson = new Gson();
    private final ThrowableReaderFunction readFunc = new ThrowableReaderFunction();

    public MovWebApi(IRequest req) {
        this.req = req;

    }


    /**
     * E.g. https://api.themoviedb.org/3/search/movie?api_key=***************&query=war+games
     */
    public SearchItemDto[] search(String title, int page) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_SEARCH ,
                /*0*/ API_KEY , /*1*/ title.replaceAll(" ","+") ,/*2*/page);

        Stream<String> content =
                Stream.of(req.getBody(url))
                .map(in -> new BufferedReader(new InputStreamReader(in)))    //InputStream -> InputStreamReader
                .map(readFunc); //BufferedReader -> String

        String json = content.reduce("", (prev, curr) -> prev + curr);
        SearchDto dto = gson.fromJson(json, SearchDto.class);
        return dto.getResults();
    }

    /*private String m1(String s1 , String s2){
        return s1 + s2;
    }*/


    /**
     * E.g. https://api.themoviedb.org/3/movie/860?api_key=***************
     */
    public MovieDto getMovie(int id) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_MOVIE ,
                /*0*/ API_KEY , /*1*/ Integer.toString(id));

        Stream<String> content =
                Stream.of(req.getBody(url))
                        .map(in -> new BufferedReader(new InputStreamReader(in)))    //InputStream -> InputStreamReader
                        .map(readFunc); //BufferedReader -> String
        String json = content.reduce("", (prev, curr) -> prev + curr);
        MovieDto dto = gson.fromJson(json , MovieDto.class);
        return dto;
    }

    /**
     * E.g. https://api.themoviedb.org/3/movie/860/credits?api_key=***************
     */
    public MovieCreditsDto getMovieCredits(int movieId) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_MOVIE_CREDITS,
                                            API_KEY , Integer.toString(movieId));

        Stream<String> content =
                Stream.of(req.getBody(url))
                        .map(in -> new BufferedReader(new InputStreamReader(in)))    //InputStream -> InputStreamReader
                        .map(readFunc); //BufferedReader -> String

        String json = content.reduce("", (prev, curr) -> prev + curr);
        MovieCreditsDto movieCredits = gson.fromJson(json , MovieCreditsDto.class);

        return movieCredits;
    }

    /**
     * E.g. https://api.themoviedb.org/3/person/4756?api_key=***************
     */
    public PersonDto getPerson(int personId) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_PERSON ,
                /*0*/ API_KEY , /*1*/ Integer.toString(personId));

        Stream<String> content =
                Stream.of(req.getBody(url))
                        .map(in -> new BufferedReader(new InputStreamReader(in)))    //InputStream -> InputStreamReader
                        .map(readFunc); //BufferedReader -> String
        String json = content.reduce("", (prev, curr) -> prev + curr);

        PersonDto person = gson.fromJson(json , PersonDto.class);
        return person;
    }

    /**
     * E.g. https://api.themoviedb.org/3/person/4756/movie_credits?api_key=***************
     */
    public SearchItemDto[] getPersonCreditsCast(int personId) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_PERSON_CREDITS ,
                /*0*/ API_KEY , /*1*/ personId);

        Stream<String> content =
                Stream.of(req.getBody(url))
                        .map(in -> new BufferedReader(new InputStreamReader(in)))    //InputStream -> InputStreamReader
                        .map(readFunc); //BufferedReader -> String
        String json = content.reduce("", (prev, curr) -> prev + curr);

        return gson.fromJson(json , PersonCreditsDto.class).getCast();
    }
}