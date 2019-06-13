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

package movasync.model;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @author Miguel Gamboa
 *         created on 02-03-2017
 */
public class SearchItem {
    private final int id;
    private final String title;
    private final String release_date;
    private final double vote_average;
    private final CompletableFuture<Movie> details;

    public SearchItem(
            int id,
            String title,
            String release_date,
            double vote_average,
            CompletableFuture<Movie> details) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public CompletableFuture<Movie> getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "SearchItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", release_date='" + release_date + '\'' +
                ", vote_average=" + vote_average +
                ", details=" + details +
                '}';
    }
}
