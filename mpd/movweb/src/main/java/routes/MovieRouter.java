package routes;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import movasync.MovService;
import movasync.MovWebApi;
import movasync.model.Credit;
import movasync.model.SearchItem;
import util.HttpRequest;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MovieRouter {

    private static final String PERSON_TEMPLATE = "/person.hbs";
    private static final String PERSON_CREDITS_TEMPLATE = "/personCredits.hbs";
    private static final String MOVIE_TEMPLATE = "/movie.hbs";
    private static final String MOV_SEARCH_TEMPLATE = "/movSearch.hbs";
    private static final String MOV_CREDITS_TEMPLATE = "/movCredits.hbs";
    private static final String TEMPLATES_DIR = "templates";
    public static final String HTML_RESP_HEADER = "text/html";

    private final MovService service;
    private final TemplateEngine engine;

    public MovieRouter(MovService service) {
        this.service = service;
        engine = HandlebarsTemplateEngine.create();
    }

    public static Router router(Vertx vertx) {
        return router(vertx, new MovService(new MovWebApi(new HttpRequest())));
    }

    public static Router router(Vertx vertx, MovService service) {
        Router router = Router.router(vertx);

        MovieRouter handlers = new MovieRouter(service);
        router.route("/movies").handler(handlers::movSearchHandler);
        router.route("/movies/:id").handler(handlers::movSearchByIdHandler);
        router.route("/movies/:id/credits").handler(handlers::movCreditsHandler);
        router.route("/person/:id").handler(handlers::personHandler);
        router.route("/person/:id/movies").handler(handlers::personCreditsHandler);
        return router;
    }

    private void personCreditsHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", HTML_RESP_HEADER);

        int id = Integer.parseInt(req.getParam("id"));

        service
                .getActorCreditsCast(id)
                .thenAccept(movs -> {
                    ctx.put("movies", movs.toArray(SearchItem[]::new));
                    engine.render(ctx, TEMPLATES_DIR, PERSON_CREDITS_TEMPLATE,
                            view -> {
                                if(view.succeeded()) {
                                    resp.setChunked(true);
                                    resp.end(view.result());
                                }
                                else{
                                    ctx.fail(view.cause());
                                }
                            });
                });
    }

    private void personHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", HTML_RESP_HEADER);

        int id = Integer.parseInt(req.getParam("id"));

        service
                .getActor(id)
                .thenAccept(person -> {
                    ctx.put("id", person.getId());
                    ctx.put("name", person.getName());
                    ctx.put("placeOfBirth", person.getPlaceOfBirth());

                    engine.render(ctx, TEMPLATES_DIR, PERSON_TEMPLATE,
                            view -> {
                                if(view.succeeded()) {
                                    resp.setChunked(true);
                                    resp.end(view.result());
                                }
                                else{
                                    ctx.fail(view.cause());
                                }
                });

                            });
    }

    private void movCreditsHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", HTML_RESP_HEADER);

        int movId = Integer.parseInt(req.getParam("id"));

        service
                .getMovieCredits(movId)
                .thenAccept( strm -> {
                    ctx.put("credits", strm.toArray(Credit[]::new));
                    engine.render(ctx, TEMPLATES_DIR, MOV_CREDITS_TEMPLATE,
                            view -> {
                                if(view.succeeded()) {
                                    resp.setChunked(true);
                                    resp.end(view.result());
                                }
                                else{
                                    ctx.fail(view.cause());
                                }
                            }
                    )  ;

                });



    }


    private void movSearchHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", HTML_RESP_HEADER);

        String movie = req.getParam("name");

        service
                .search(movie)
                .thenAccept( strm -> {

                            ctx.put("movies", strm.toArray(SearchItem[]::new)); // give information of every movie in search
                                                                                    // for handlebar to fill in template

                            engine.render(ctx, TEMPLATES_DIR, MOV_SEARCH_TEMPLATE,
                                    view -> {
                                            if(view.succeeded()) {
                                                resp.setChunked(true);
                                                resp.end(view.result());
                                            }
                                        else{
                                            ctx.fail(view.cause());
                                        }
                                    }
                            )  ;
                        }
                );
    }

    private void movSearchByIdHandler(RoutingContext ctx )
    {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", HTML_RESP_HEADER);

        int movId = Integer.parseInt(req.getParam("id"));

        service
                .getMovie(movId)
                .thenAccept(mov -> {
                    ctx.put("original_title", mov.getOriginalTitle());
                    ctx.put("id", mov.getId());
                    ctx.put("release_date", mov.getReleaseDate());
                    engine.render(ctx, TEMPLATES_DIR, MOVIE_TEMPLATE,
                            view -> {
                                if(view.succeeded()) {
                                    resp.setChunked(true);
                                    resp.end(view.result());
                                }
                                else{
                                    ctx.fail(view.cause());
                                }
                            });
                });
    }

}
