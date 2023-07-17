package course.concurrency.exams.auction;

public interface Auction {

    Bid DEFAULT_BID = new Bid(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);

    boolean propose(Bid bid);

    Bid getLatestBid();
}
