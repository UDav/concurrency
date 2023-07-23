package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private final Notifier notifier;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private volatile Bid latestBid = DEFAULT_BID;
    private final Object lock = new Object();

    private volatile boolean stopped;

    public boolean propose(Bid bid) {
        if (!stopped && bid.getPrice() > latestBid.getPrice()) {
            synchronized (lock) {
                if (!stopped && bid.getPrice() > latestBid.getPrice()) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;
                    return true;
                }
            }
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public Bid stopAuction() {
        stopped = true;
        return latestBid;
    }
}
