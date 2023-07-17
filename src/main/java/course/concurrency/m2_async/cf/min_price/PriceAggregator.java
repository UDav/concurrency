package course.concurrency.m2_async.cf.min_price;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.*;

public class PriceAggregator {

    private final ExecutorService executor = new ForkJoinPool(50);
    private PriceRetriever priceRetriever = new PriceRetriever();

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public double getMinPrice(long itemId) {
        final ArrayList<CompletableFuture<Double>> tasks = new ArrayList<>();
        shopIds.forEach((Long shopId) -> tasks.add(
            CompletableFuture.supplyAsync(() -> priceRetriever.getPrice(itemId, shopId), executor)
                .exceptionally((th) -> Double.NaN)
                .completeOnTimeout(Double.NaN, 2980, TimeUnit.MILLISECONDS)
        ));

        return tasks.stream()
            .map(CompletableFuture::join)
            .filter((a) -> !Double.isNaN(a))
            .min(Double::compareTo)
            .orElse(Double.NaN);
    }
}
