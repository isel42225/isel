package movasync.model;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Miguel Gamboa
 *         created on 04-08-2017
 */
public class Movie {
    private final int id;
    private final String original_title;
    private final String tagline;
    private final String overview;
    private final double vote_average;
    private final String release_date;
    private final CompletableFuture<Stream<Credit>> credits;

    public Movie(
            int id,
            String original_title,
            String tagline,
            String overview,
            double vote_average,
            String release_date,
            CompletableFuture<Stream<Credit>> credits)
    {
        this.id = id;
        this.original_title = original_title;
        this.tagline = tagline;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.credits = credits;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public CompletableFuture<Stream<Credit>> getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", original_title='" + original_title + '\'' +
                ", tagline='" + tagline + '\'' +
                ", overview='" + overview + '\'' +
                ", vote_average=" + vote_average +
                ", release_date='" + release_date + '\'' +
                '}';
    }
}
