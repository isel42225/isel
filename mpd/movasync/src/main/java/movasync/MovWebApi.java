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

package movasync;

import com.google.gson.Gson;
import movasync.dto.*;
import util.IRequest;

import java.io.*;
import java.net.URL;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miguel Gamboa
 *         created on 16-02-2017
 */

public class MovWebApi {
    /**
     * Constants
     *
     * To format messages URLs use {@link MessageFormat#format(String, Object...)} method.
     */
    private static final String MOVIE_DB_HOST = "https://api.themoviedb.org/3/";
    private static final String MOVIE_DB_SEARCH = "search/movie?api_key={0}&query={1}&page={2}";
    private static final String MOVIE_DB_FIRST_SEARCH = "search/movie?api_key={0}&query={1}";
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

    public MovWebApi(IRequest req) {
        this.req = req;

    }

    /**
     * E.g. https://api.themoviedb.org/3/search/movie?api_key=***************&query=war+games
     */
    public CompletableFuture<SearchItemDto[]> search(String title, int page) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_SEARCH ,
                /*0*/ API_KEY , /*1*/ title.replaceAll(" ","+") ,/*2*/page);

        return req
                .getBody(url)
                .thenApply(str -> gson.fromJson(str , SearchDto.class))
                .thenApply(SearchDto::getResults);
    }

    public CompletableFuture<SearchDto> getSearchTotalPages(String title)
    {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_FIRST_SEARCH ,
                /*0*/ API_KEY , /*1*/ title.replaceAll(" ","+"));

        return req
                .getBody(url)
                .thenApply(str -> gson.fromJson(str , SearchDto.class));
    }


    /**
     * E.g. https://api.themoviedb.org/3/movie/860?api_key=***************
     */
    public CompletableFuture<MovieDto> getMovie(int id) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_MOVIE ,
                /*0*/ API_KEY , /*1*/ Integer.toString(id));

       return req
                .getBody(url)
                .thenApply(str -> gson.fromJson(str , MovieDto.class));
    }

    /**
     * E.g. https://api.themoviedb.org/3/movie/860/credits?api_key=***************
     */
    public CompletableFuture<MovieCreditsDto> getMovieCredits(int movieId) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_MOVIE_CREDITS,
                                            API_KEY , Integer.toString(movieId));

        return req
                .getBody(url)
                .thenApply(str -> gson.fromJson(str , MovieCreditsDto.class));

    }

    /**
     * E.g. https://api.themoviedb.org/3/person/4756?api_key=***************
     */
    public CompletableFuture<PersonDto> getPerson(int personId) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_PERSON ,
                /*0*/ API_KEY , /*1*/ Integer.toString(personId));

        return req
                .getBody(url)
                .thenApply(str -> gson.fromJson(str , PersonDto.class));
    }

    /**
     * E.g. https://api.themoviedb.org/3/person/4756/movie_credits?api_key=***************
     */
    public CompletableFuture<SearchItemDto[]> getPersonCreditsCast(int personId) {
        String url = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_PERSON_CREDITS ,
                /*0*/ API_KEY , /*1*/ personId);

        return req
                .getBody(url)
                .thenApply(str -> gson.fromJson(str , PersonCreditsDto.class))
                .thenApply(PersonCreditsDto::getCast);
    }
}