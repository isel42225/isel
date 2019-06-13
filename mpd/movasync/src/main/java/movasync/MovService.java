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

package movasync;

import movasync.dto.*;
import movasync.model.*;
import util.Cache;
import util.iterator.CreditDtoJoinSpliterator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

/**
 * @author Miguel Gamboa
 *         created on 02-03-2017
 */
public class MovService {

    private final MovWebApi movWebApi;
    private final Map<Integer, CompletableFuture<Movie>> movies = new ConcurrentHashMap<>();
    private final Map<Integer, CompletableFuture<List<Credit>>> cast = new ConcurrentHashMap<>();
    private final Map<Integer, CompletableFuture<Person>> actors = new ConcurrentHashMap<>();

    public MovService(MovWebApi movWebApi) {
        this.movWebApi = movWebApi;
    }

    public CompletableFuture<Stream<SearchItem>> search(String name) {
        //first req to know total pages
        CompletableFuture<SearchDto> first = movWebApi.getSearchTotalPages(name);

        return first
                .thenApply(dto -> Stream.iterate(1, prev -> ++prev).limit(dto.getTotal_pages()))
                .thenApply(pages -> pages.map(page -> movWebApi.search(name, page)))
                .thenApply(strm -> strm.collect(toList()).stream()) //make all http req
                .thenApply(strm -> strm.map(CompletableFuture::join))   //wait for reqs (?)
                .thenApply(strm -> strm.flatMap(Stream::of))
                .thenApply(strm -> strm.map(this::parseSearchItemDto));

    }

    private SearchItem parseSearchItemDto(SearchItemDto dto) {
        return  new SearchItem(
                dto.getId(),
                dto.getTitle(),
                dto.getReleaseDate(),
                dto.getVoteAverage(),
                getMovie(dto.getId()));   //CF<Movie>
    }

    public CompletableFuture<Movie> getMovie(int movId) {
        return movies.computeIfAbsent(movId, id -> {
            CompletableFuture<MovieDto> dto = movWebApi.getMovie(id);
            return dto.thenApply(mov -> new Movie(
                    mov.getId(),
                    mov.getOriginalTitle(),
                    mov.getTagline(),
                    mov.getOverview(),
                    mov.getVoteAverage(),
                    mov.getReleaseDate(),
                    getMovieCredits(id)));

        });
    }

    public CompletableFuture<Stream<Credit>> getMovieCredits(int movId) {
        return cast.computeIfAbsent(movId, id ->
                getCredits(id)
                        .thenApply(strm -> Cache.of(()-> strm)) //add to cache
                        .thenApply(supp -> supp.get().collect(toList()))    //conv to list

        ).thenApply(Collection::stream);    //conv to stream

    }

    private CompletableFuture<Stream<Credit>> getCredits(int movId ){

        CompletableFuture<MovieCreditsDto> cf = movWebApi.getMovieCredits(movId);
        return cf
                .thenApply(dto -> joinCreditDto(of(dto.getCast()), of(dto.getCrew())))  //join cast and crew
                .thenApply(strm -> strm.map(this::parseCreditDto)); //final map to Credit
    }


    private Stream<CreditDto> joinCreditDto(Stream<CreditDto> cast , Stream<CreditDto> crew )
    {
        CreditDtoJoinSpliterator join =
                new CreditDtoJoinSpliterator(cast.spliterator(), crew.spliterator());
        //Join equal crew and cast members
        return StreamSupport.stream(join,false);
    }

    private Credit parseCreditDto(CreditDto dto ){
        return new Credit(dto.getId(),
                dto.getCharacter(),
                dto.getName(),
                dto.getDepartment(),
                dto.getJob() ,
                getActor(dto.getId())   //CF<Person>
        );
    }

    public CompletableFuture<Person> getActor(int actorId) {
        return actors.computeIfAbsent(actorId , this::parsePersonDto);

    }

    private CompletableFuture<Person> parsePersonDto(int id) {
        CompletableFuture<PersonDto> dto = movWebApi.getPerson(id);
        return dto.thenApply(person -> new Person(
                person.getId(),
                person.getName(),
                person.getPlaceOfBirth(),
                person.getBiography(),
                getActorCreditsCast(person.getId())));  //CF<Stream<SearchItem>>
    }

    //Actor credits
    public CompletableFuture<Stream<SearchItem>> getActorCreditsCast(int actorId) {
    CompletableFuture<SearchItemDto[]> arr = movWebApi.getPersonCreditsCast(actorId);
        return arr
                .thenApply(Stream::of)  //conv arr to Stream
                .thenApply(strm -> strm.map(this::parseSearchItemDto)); //map to Stream<SearchItem>

    }


}
