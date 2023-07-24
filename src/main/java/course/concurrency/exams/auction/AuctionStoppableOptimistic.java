package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private final Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    /**
     * mark = true если аукцион остановлен
     */
    private final AtomicMarkableReference<Bid> latestBid = new AtomicMarkableReference<>(DEFAULT_BID, false);

    public boolean propose(Bid bid) {
        Bid lb;
        do {
            lb = latestBid.getReference();
            if (latestBid.isMarked() || lb.getPrice() >= bid.getPrice()) return false;
        } while (!latestBid.compareAndSet(lb, bid, false, false));
        notifier.sendOutdatedMessage(lb);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.getReference();
    }

    public Bid stopAuction() {
        Bid bid;
        do {
            bid = latestBid.getReference();
        } while (!latestBid.attemptMark(bid, true));
        return bid;
    }
}
