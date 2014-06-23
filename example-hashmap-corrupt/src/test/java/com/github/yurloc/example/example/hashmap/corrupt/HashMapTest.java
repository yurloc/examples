package com.github.yurloc.example.example.hashmap.corrupt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

public class HashMapTest {

    @Test
    public void test() throws InterruptedException, ExecutionException {
//        final Set<String> set = Collections.synchronizedSet(new HashSet<String>());
//        final Set<String> set = Collections.newSetFromMap( new ConcurrentHashMap<String, Boolean>() );
        final Set<String> set = new HashSet<>();
        ExecutorService es = Executors.newCachedThreadPool();

        for (int o = 0; o < 10000; o++) {
            if (o % 100 == 0) {
                System.out.println("Starting batch #" + o);
            }

            int batchSize = 100;
            List<Runnable> tasks = new ArrayList<>(batchSize);
            for (int i = 0; i < batchSize; i++) {
                final String item = String.valueOf(o * batchSize + i);
                tasks.add(new Runnable() {

                    @Override
                    public void run() {
                        set.add(item);
                    }
                });
            }

            List<Future<?>> results = new ArrayList<>(batchSize);
            for (int i = 0; i < batchSize; i++) {
                results.add(es.submit(tasks.get(i)));
            }

            for (int i = 0; i < batchSize; i++) {
                results.get(i).get();
            }

            for (int i = 0; i < batchSize; i++) {
                String item = String.valueOf(o * batchSize + i);
                if (!set.contains(item)) {
                    System.out.printf("Batch #%s missing item: %s%n", o, item);
                }
            }

            tasks.clear();
            results.clear();
        }
    }
}
