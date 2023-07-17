package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {

    private final Notifier notifier;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final AtomicReference<Bid> latestBid = new AtomicReference<>(DEFAULT_BID);

    public boolean propose(Bid bid) {
        Bid lb;
        do {
            lb = latestBid.get();
            if (lb.getPrice() >= bid.getPrice()) return false;
        } while (!latestBid.compareAndSet(lb, bid));
        notifier.sendOutdatedMessage(lb);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
