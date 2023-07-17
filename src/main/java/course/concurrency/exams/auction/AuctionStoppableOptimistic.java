package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private final Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final AtomicReference<Bid> latestBid = new AtomicReference<>(DEFAULT_BID);
    private volatile boolean stopped = false;

    public boolean propose(Bid bid) {
        Bid lb;
        do {
            lb = latestBid.get();
            if (stopped || lb.getPrice() >= bid.getPrice()) return false;
        } while (!latestBid.compareAndSet(lb, bid));
        notifier.sendOutdatedMessage(lb);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }

    public Bid stopAuction() {
        stopped = true;
        return latestBid.get();
    }
}
