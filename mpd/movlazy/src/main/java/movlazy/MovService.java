/*
 * Copyright (c) 2017, Miguel Gamboa
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

import movlazy.dto.*;
import movlazy.model.Person;
import movlazy.model.Credit;
import movlazy.model.Movie;
import movlazy.model.SearchItem;
import util.Cache;
import util.iterator.CreditDtoJoinSpliterator;
import util.iterator.TakeWhileSpliterator;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Miguel Gamboa
 *         created on 02-03-2017
 */
public class MovService {

    private final MovWebApi movWebApi;
    private final Map<Integer, Movie> movies = new HashMap<>();
    private final Map<Integer, Supplier<Stream<Credit>>> cast = new HashMap<>();
    private final Map<Integer, Person> actors = new HashMap<>();

    public MovService(MovWebApi movWebApi) {
        this.movWebApi = movWebApi;
    }

    public Supplier<Stream<SearchItem>> search(String name) {
       return () ->
               StreamSupport.stream(
                       new TakeWhileSpliterator<>(
                        Stream.iterate(1, prev -> ++prev)
                                .map(page -> movWebApi.search(name,page)).spliterator(), movs -> movs.length !=0), false)
                .flatMap(Stream::of)
                .map(this::parseSearchItemDto);

       /*//<==> Versão JDK 10
        return () -> Stream.iterate(1,prev -> ++prev)   //Generate page numbers
                .map(page -> movWebApi.search(name, page))  //Map page value into a SearchItemDto[]
                .takeWhile(movs -> movs.length != 0)    // ???
                .flatMap(Stream::of)                    //Map SearchItemDto[] into a stream
                .map(this::parseSearchItemDto);        //Map each SearchItemDto to a SearchItem
    */}

    private SearchItem parseSearchItemDto(SearchItemDto dto) {
        return new SearchItem(
                dto.getId(),
                dto.getTitle(),
                dto.getReleaseDate(),
                dto.getVoteAverage(),
                () -> getMovie(dto.getId()));   //Supplier Movie
    }

    public Movie getMovie(int movId) {
        return movies.computeIfAbsent(movId, id -> {
            MovieDto mov = movWebApi.getMovie(id);
            return new Movie(
                    mov.getId(),
                    mov.getOriginalTitle(),
                    mov.getTagline(),
                    mov.getOverview(),
                    mov.getVoteAverage(),
                    mov.getReleaseDate(),
                    this.getMovieCredits(id));
        });
    }

    public Supplier<Stream<Credit>> getMovieCredits(int movId) {

        return  cast.computeIfAbsent(movId, id ->
                Cache.of(getCredits(id))
        );
    }



    private Supplier<Stream<Credit>> getCredits(int movId ){

        return () -> {
            MovieCreditsDto dto = movWebApi.getMovieCredits(movId);
            return StreamSupport.stream(
                    //Join equal crew and cast members
                    new CreditDtoJoinSpliterator(
                            Stream.of(dto.getCast()).spliterator(),
                            Stream.of(dto.getCrew()).spliterator()
                    ),
                    false
            ).map(this::parseCreditDto);
        };

    }

    private Credit parseCreditDto(CreditDto dto ){
        return new Credit(dto.getId(),
                dto.getCharacter(),
                dto.getName(),
                dto.getDepartment(),
                dto.getJob() ,
                () -> this.getActor(dto.getId() , dto.getName())
        );
    }

    public Person getActor(int actorId , String name) {
        return actors.computeIfAbsent(actorId , id ->
                parsePersonDto(movWebApi.getPerson(id)));

    }

    private Person parsePersonDto(PersonDto person) {
        return new Person(
                person.getId(),
                person.getName(),
                person.getPlaceOfBirth(),
                person.getBiography(),
                getActorCreditsCast(person.getId()));
    }

    //Actor credits
    public Supplier<Stream<SearchItem>> getActorCreditsCast(int actorId) {
        return () -> Stream.of(movWebApi.getPersonCreditsCast(actorId)). //MovWebApi provides SearchItemDto []
                map(this::parseSearchItemDto);   //Final mapping to SearchItem

    }


}
