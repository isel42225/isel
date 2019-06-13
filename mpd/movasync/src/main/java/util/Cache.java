package util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Cache {

        public static <T> Supplier<Stream<T>> of(Supplier<Stream<T>> dataSrc) {
            //final Spliterator<T> src = dataSrc.get().spliterator(); // !!!maybe it should be lazy and memorized!!!
            final Recorder<T> rec = new Recorder<>(dataSrc);
            return () -> {
                // CacheIterator starts on index 0 and reads data from src or
                // from an internal cache of Recorder.
                Spliterator<T> iter = rec.cacheIterator();
                return StreamSupport.stream(iter, false);
            };
        }

        static class Recorder<T> {
            final Supplier<Stream<T>> dataSrc;
            final List<T> cache = new ArrayList<>();
            long estimateSize;
            boolean hasNext = true;
            private Spliterator<T> iter ;

            public Recorder(Supplier<Stream<T>> dataSrc) {
                this.dataSrc = dataSrc;
                //this.estimateSize = src.estimateSize();
            }

            public synchronized boolean getOrAdvance(
                    final int index,
                    Consumer<? super T> cons) {
                if (index < cache.size()) {
                    // If it is in cache then just get if from the corresponding index.
                    cons.accept(cache.get(index));
                    return true;
                } else if (hasNext)
                    // If not in cache then advance the src iterator

                    hasNext = iter.tryAdvance(item -> {
                        cache.add(item);
                        cons.accept(item);
                    });
                return hasNext;
            }

            public Spliterator<T> cacheIterator() {
                if(iter == null) {
                    //first time
                    iter = dataSrc.get().spliterator();
                    estimateSize = iter.estimateSize();
                }
                return new Spliterators.AbstractSpliterator<T>(
                        estimateSize, iter.characteristics()
                ) {
                    int index = 0;
                    public boolean tryAdvance(Consumer<? super T> cons) {
                        return getOrAdvance(index++, cons);
                    }
                    public Comparator<? super T> getComparator() {
                        return iter.getComparator();
                    }
                };
            }
        }
    }

