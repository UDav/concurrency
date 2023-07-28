package course.concurrency.m3_shared.collections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class RestaurantService {

    private final ConcurrentHashMap<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

//    private final Map<String, Integer> stat = new ConcurrentHashMap<>(); // 1
//    private final Map<String, AtomicInteger> stat = new ConcurrentHashMap<>(); // 2
    private final ConcurrentHashMap<String, LongAdder> stat = new ConcurrentHashMap<>(); // 3

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
//        stat.merge(restaurantName, 1, Integer::sum); // 1
//        stat.computeIfAbsent(restaurantName, (k) -> new AtomicInteger()).incrementAndGet(); // 2
        stat.computeIfAbsent(restaurantName, (k) -> new LongAdder()).increment(); // 3
    }

    public Set<String> printStat() {
        return stat.entrySet().stream()
            .map(e -> e.getKey() + " - " + e.getValue())
            .collect(Collectors.toSet());
    }
}
