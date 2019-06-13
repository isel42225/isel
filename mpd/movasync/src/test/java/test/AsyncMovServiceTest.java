package test;


import movasync.MovService;
import movasync.MovWebApi;
import movasync.dto.SearchItemDto;
import movasync.model.Credit;
import movasync.model.Movie;
import movasync.model.Person;
import movasync.model.SearchItem;
import org.junit.jupiter.api.Test;
import util.FileRequest;
import util.HttpRequest;
import util.IRequest;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AsyncMovServiceTest {

    @Test
    public void testSearchMovieInSinglePage() {
        IRequest req = new HttpRequest()
                .compose(System.out::println);
        MovService movapi = new MovService(new MovWebApi(req));
        Object [] movs = movapi.search("War Games").join().toArray();   //allow multiple iterations
        SearchItem m = (SearchItem) movs[0];
        assertEquals("War Games: The Dead Code", m.getTitle());
        assertEquals(6, movs.length);// number of returned movies
    }

    @Test
    public void testSearchMovieManyPages() {
        int[] count = {0};
        IRequest req = new HttpRequest()
                .compose(System.out::println)
                .compose(__ -> count[0]++);

        MovService movapi = new MovService(new MovWebApi(req));
        CompletableFuture<Stream<SearchItem>> movs = movapi.search("galo");
        assertEquals(1, count[0]);
        SearchItem galo = movs.join()
                .filter(m -> m.getTitle().equals("Hoje o Galo Sou Eu!"))
                .findFirst()
                .get();
        System.out.println(galo.getTitle());
        assertEquals(9, count[0]); // Much more requests made in comparision with lazy version

    }

    @Test
    public void testMovieDbApiGetActor() {
        int[] count = {0};
        IRequest req = new HttpRequest()
                //.compose(System.out::println)
                .compose(__ -> count[0]++);

        MovWebApi movWebApi = new MovWebApi(req);
        CompletableFuture<SearchItemDto[]> actorMovs = movWebApi.getPersonCreditsCast(4756);
        assertNotNull(actorMovs);
        assertEquals("Ladyhawke", actorMovs.join()[1].getTitle());
        assertEquals(1, count[0]); // 1 request
    }

    @Test
    public void testSearchMovieThenActorsThenMoviesAgain() {
        final int[] count = {0};
        IRequest req = new HttpRequest()
                //.compose(System.out::println)
                .compose(__ -> count[0]++);


        MovService movapi = new MovService(new MovWebApi(req));

        SearchItem [] vs = movapi.search("War Games").join().toArray(SearchItem[]::new);
        assertEquals(6, Arrays.stream(vs).count());// number of returned movies
        assertEquals(8, count[0]);
        /**
         * Iterable<SearchItem> is Lazy and without cache.
         */
        SearchItem warGames =  Arrays.stream(vs).
                 filter( m -> m.getTitle().equals("WarGames"))
                .findFirst()
                .get();

        assertEquals(860, warGames .getId());
        assertEquals("WarGames", warGames.getTitle());


        Movie mov = warGames.getDetails().join();

        assertEquals("WarGames", mov.getOriginalTitle());

        assertEquals("Is it a game, or is it real?", mov.getTagline());


        Credit [] warGamesCredits = mov.getCredits().join().toArray(Credit[]::new);

        assertEquals("Matthew Broderick", warGamesCredits[0].getName());

        Stream<Credit> iter = Arrays.stream(warGamesCredits).skip(2);

        assertEquals("Ally Sheedy", iter.findFirst().get().getName());

        Person broderick = warGamesCredits[0].getActor().join();

        assertEquals("New York City, New York, USA",
                broderick.getPlaceOfBirth());

        assertEquals("New York City, New York, USA",
                broderick.getPlaceOfBirth());

        SearchItem [] broderickMovs = broderick.getMovies().join().toArray(SearchItem[]::new);

        assertEquals("Inspector Gadget", broderickMovs[0].getTitle());

        assertEquals("Inspector Gadget", broderickMovs[0].getTitle());


        /**
         * Check Cache from the beginning
         */
        assertEquals("New York City, New York, USA",
                movapi.getMovie(860).join().getCredits().join().findFirst().get().getActor().join().getPlaceOfBirth());

        /*
         * Now get a new Film
         */
        assertEquals("Predator",
                movapi.getMovie(861).join().getCredits().join().findFirst().get().getActor().join().getMovies().join().findFirst().get().getTitle());

    }
}
