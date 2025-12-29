package org.example.clothing;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Gathers elements from an infinite stream into a fixed-size list,
 * skipping the first N elements that match a city value.
*/

public final class Gatherer {
    public static List<Cloth> gather(Stream<Cloth> stream, String cityToSkip, int nToSkip, int count) {
        AtomicInteger skipped = new AtomicInteger(0);
        return stream.filter(c -> {
            if (c.getCity().equals(cityToSkip) && skipped.get() < nToSkip) {
                skipped.incrementAndGet();
                return false;
            }
            return true;
        }).limit(count).collect(Collectors.toList());
    }
}
