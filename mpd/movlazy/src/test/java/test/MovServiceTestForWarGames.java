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

package test;

import com.google.common.util.concurrent.RateLimiter;
import movlazy.MovService;
import movlazy.MovWebApi;
import movlazy.dto.SearchItemDto;
import movlazy.model.Credit;
import movlazy.model.SearchItem;
import org.junit.jupiter.api.Test;
import util.FileRequest;
import util.HttpRequest;
import util.IRequest;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MovServiceTestForWarGames {

    @Test
    public void testSearchMovieInSinglePage() {
        MovService movapi = new MovService(new MovWebApi(new FileRequest()));
        Supplier<Stream<SearchItem>> movs = movapi.search("War Games");
        SearchItem m = movs.get().findFirst().get();
        assertEquals("War Games: The Dead Code", m.getTitle());
        assertEquals(6, movs.get().count());// number of returned movies
    }

    @Test
    public void testSearchMovieManyPages() {
        int[] count = {0};
        IRequest req = new FileRequest()
                //.compose(System.out::println)
                .compose(__ -> count[0]++);

        MovService movapi = new MovService(new MovWebApi(req));
        Supplier<Stream<SearchItem>> movs = movapi.search("candle");
        assertEquals(0, count[0]);
        SearchItem candleshoe = movs.get()
                .filter(m -> m.getTitle().equals("Candleshoe"))
                .findFirst()
                .get();
        assertEquals(2, count[0]); // Found on 2nd page
        assertEquals(60, movs.get().count());// Number of returned movies
        assertEquals(6, count[0]); // 4 requests more to consume all pages
    }

    @Test
    public void testMovieDbApiGetActor() {
        int[] count = {0};
        IRequest req = new FileRequest()
                //.compose(System.out::println)
                .compose(__ -> count[0]++);

        MovWebApi movWebApi = new MovWebApi(req);
        SearchItemDto[] actorMovs = movWebApi.getPersonCreditsCast(4756);
        assertNotNull(actorMovs);
        assertEquals("Ladyhawke", actorMovs[1].getTitle());
        assertEquals(1, count[0]); // 1 request
    }

    @Test
    public void testSearchMovieThenActorsThenMoviesAgain() {
        final int[] count = {0};
        IRequest req = new FileRequest()
                //.compose(System.out::println)
                .compose(__ -> count[0]++);


        MovService movapi = new MovService(new MovWebApi(req));

        Supplier<Stream<SearchItem>> vs = movapi.search("War Games");
        assertEquals(6, vs.get().count());// number of returned movies
        assertEquals(2, count[0]);         // 2 requests to consume all pages
        /**
         * Iterable<SearchItem> is Lazy and without cache.
         */
        SearchItem warGames = vs.get()
                .filter(m -> m.getTitle().equals("WarGames"))
                .findFirst()
                .get();
        assertEquals(3, count[0]); // 1 more request for 1st page
        assertEquals(860, warGames .getId());
        assertEquals("WarGames", warGames.getTitle());
        assertEquals(3, count[0]); // Keep the same number of requests
        /**
         * getDetails() relation SearchItem ---> Movie is Lazy and supported on Supplier<Movie> with Cache
         */
        assertEquals("WarGames", warGames.getDetails().getOriginalTitle());
        assertEquals(4, count[0]); // 1 more request to get the Movie
        assertEquals("Is it a game, or is it real?", warGames.getDetails().getTagline());
        assertEquals(4, count[0]); // NO more request. It is already in cache
        /**
         * getCast() relation Movie --->* Credit is Lazy and
         * supported on Supplier<List<Credit>> with Cache
         */
        Supplier<Stream<Credit>> warGamesCredits = warGames.getDetails().getCredits();
        assertEquals(4, count[0]); // No requests to get the Movie Cast => It is Lazy
        assertEquals("Matthew Broderick",
                warGamesCredits.get().findFirst().get().getName());
        assertEquals(5, count[0]); // 1 more request for warGamesCast.get().

        Stream<Credit> iter = warGamesCredits.get().skip(2);
        assertEquals("Ally Sheedy", iter.findFirst().get().getName());
        assertEquals(5, count[0]); // NO more request. It is already in cache
        /**
         * Credit ---> Person is Lazy and with Cache for Person but No cache for actor credits
         */
        Credit broderick = warGames.getDetails().getCredits().get().findFirst().get();
        assertEquals(5, count[0]); // NO more request. It is already in cache
        assertEquals("New York City, New York, USA",
                broderick.getActor().getPlaceOfBirth());
        assertEquals(6, count[0]); // 1 more request for Person Person
        assertEquals("New York City, New York, USA",
                broderick.getActor().getPlaceOfBirth());
        assertEquals(6, count[0]); // NO more request. It is already in cache
        assertEquals("Inspector Gadget",
                broderick.getActor().getMovies().get().findFirst().get().getTitle());
        assertEquals(7, count[0]); // 1 more request for Person Credits
        assertEquals("Inspector Gadget",
                broderick.getActor().getMovies().get().findFirst().get().getTitle());
        assertEquals(8, count[0]); // 1 more request. Person Cast is not in cache

        /**
         * Check Cache from the beginning
         */
        assertEquals("New York City, New York, USA",
                movapi.getMovie(860).getCredits().get().findFirst().get().getActor().getPlaceOfBirth());
        assertEquals(8, count[0]); // No more requests for the same getMovie.
        /*
         * Now get a new Film
         */
        assertEquals("Predator",
                movapi.getMovie(861).getCredits().get().findFirst().get().getActor().getMovies().get().findFirst().get().getTitle());
        assertEquals(12, count[0]); // 1 request for Movie + 1 for CastItems + 1 Person + 1 Person Credits
    }


//    @Test
//    public void testSearchMovieWithManyPages() {
//        final RateLimiter rateLimiter = RateLimiter.create(3.0);
//        final int[] count = {0};
//        IRequest req = new HttpRequest()
//                .compose(__ -> count[0]++)
//                .compose(__ -> rateLimiter.acquire());
//
//        MovService movapi = new MovService(new MovWebApi(req));
//
//        Stream<SearchItem> vs = movapi.search("fire").get();
//        assertEquals(1173, vs.count());// number of returned movies
//        assertEquals(60, count[0]);    // 2 requests to consume all pages
//    }

    @Test
    public void shouldJoinSameCreditDtoInSingleCredit()
    {
        IRequest req = new FileRequest();

        MovService service = new MovService(new MovWebApi(req));

        // Get stream of credit that is both cast and crew
        Stream<Credit> joined = service.getMovieCredits(489).get()
                .filter(item -> item.getName().equals("Matt Damon"));

        Object [] actual = joined.toArray();    //dump stream

        Credit damon = (Credit)actual[0];   //get credit

        assertEquals(1, actual.length);
        assertNotNull(damon.getCharacter());    //
        assertNotNull(damon.getDepartment());   //  Credit must have all characteristics
        assertNotNull(damon.getJob());          //

    }

    @Test
    public void shouldReturnCorrectMovieCastCount(){
        IRequest req = new FileRequest();

        MovService movapi = new MovService(new MovWebApi(req));

        Stream<Credit> cast = movapi.getMovieCredits(860).get().
                filter(actor -> actor.getCharacter() != null);  //Only cast members have characters

        assertEquals(33 , cast.count());

    }

    @Test
    public void shouldReturnCorrectMovieCreditsCount(){
        IRequest req = new FileRequest();

        MovService movapi = new MovService(new MovWebApi(req));

        Stream<Credit> credits = movapi.getMovieCredits(860).get(); //get cast + crew
        assertEquals(48 , credits.count());

    }
}
