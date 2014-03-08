package com.github.yurloc.examples;

import java.util.UUID;

import org.junit.Test;

/**
 * Helps to check if {@link LineStore} is safe for access from multiple threads.
 */
public class LineStoreTest {

    @Test(timeout = 10000)
    public void testConcurrentAccess() {
        final LineStore store = new LineStore();
        Thread writer = new Thread(new Runnable() {

            public void run() {
                while (!Thread.interrupted()) {
                    store.addLine(UUID.randomUUID().toString());
                }
            }
        }, "writer");
        writer.start();

        int size = 0;
        while (size < 100000) {
            size = store.getLines().size();
            System.out.println("size: " + size);
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                System.err.println("interrupted");
            }
        }
    }
}
