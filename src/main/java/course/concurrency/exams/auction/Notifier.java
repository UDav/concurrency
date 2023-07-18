package course.concurrency.exams.auction;

import java.util.concurrent.*;

public class Notifier {

    private final ExecutorService executor = new ForkJoinPool(32);

    public void sendOutdatedMessage(Bid bid) {
        executor.submit(this::imitateSending);
    }

    private void imitateSending() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}
