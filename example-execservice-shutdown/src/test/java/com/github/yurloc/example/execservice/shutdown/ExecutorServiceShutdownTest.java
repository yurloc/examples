package com.github.yurloc.example.execservice.shutdown;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

/**
 * This test demonstrates two mistakes in shutting down ExecutorService which is running threads that perform infinite
 * tasks. The correct way to stop such threads is to call {@link ExecutorService#shutdownNow()}, which interrupts
 * running tasks, and check thread's interrupted state in each cycle of the task using {@link Thread#isInterrupted()}.
 */
public class ExecutorServiceShutdownTest {

    @Test
    public void test() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService es = Executors.newCachedThreadPool();
        Future<?> f = es.submit(new Runnable() {

            @Override
            public void run() {
                long last = System.currentTimeMillis();
                System.out.println("Thread start.");
                // Mistake #2: thread ignores interrupted state
//                while (true) {
                while (!Thread.currentThread().isInterrupted()) {
                    if (System.currentTimeMillis() - last > 250) {
                        last = System.currentTimeMillis();
                        System.out.println(".");
                    }
                }
//                System.out.println("Interrupted.");
            }
        });
        Thread.sleep(2000); // let the thread run for a while
        System.out.println("Shutdown.");
        // Mistake #1: shutdown() only closes the service for new tasks; submitted tasks are not cancelled
        es.shutdown();
//        es.shutdownNow(); // important, interrupts running threads
        es.awaitTermination(2, TimeUnit.SECONDS); // is blocked by running tasks but *doesn't fail* after the timeout
        System.out.println("Terminated.");
        f.get(2, TimeUnit.SECONDS); // avoid infinite waiting; throws TimeoutException after the timeout
    }
}
