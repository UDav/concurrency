package course.concurrency.m2_async.cf;

import java.util.stream.IntStream;

public class LoadGenerator {

    public void work() {
//        sleep();
        compute();
    }

    private void sleep() {
        try {
            Thread.sleep(1_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int compute() {
        return IntStream.range(0, 5_000_000).boxed().filter(i -> i % 2 == 0).reduce((a, b) -> b).get();
    }
}