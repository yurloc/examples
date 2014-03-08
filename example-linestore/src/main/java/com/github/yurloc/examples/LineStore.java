package com.github.yurloc.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This is an append-only store for string messages. Writers can add lines with {@link #addLine(java.lang.String)}.
 * Readers can get a random sublist of all stored lines that is safe for further usage (does not depend on the original
 * collections unlike the list you get with {@link List#subList(int, int)}).
 * <p>
 * It is intended to be thread-safe but {@link #getLines()} may throw {@link java.util.ConcurrentModificationException}.
 * Make a fix and explain why the current code allows for CME.
 */
public class LineStore {

    private final List<String> lines = Collections.synchronizedList(new LinkedList<String>());
    private final Random rnd = new Random(0);

    public void addLine(String line) {
        lines.add(line);
    }

    public List<String> getLines() {
        int size = lines.size();
        if (size == 0) {
            return Collections.emptyList();
        }
        int n1 = rnd.nextInt(size);
        int n2 = rnd.nextInt(size);
        List<String> sub, copy;
        // create random sublist of lines (synchronization handled by the list)
        sub = lines.subList(Math.min(n1, n2), Math.max(n1, n2));
        // deep copy sublist (must be synchronized on list, not this!)
        // http://docs.oracle.com/javase/6/docs/api/java/util/Collections.html#synchronizedList%28java.util.List%29
        synchronized (lines) {
            // this is the way to deep copy the sub-list
            copy = new ArrayList<String>(sub);
        }
        return copy;
    }
}
