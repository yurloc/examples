package io.github.yurloc.example.runlistener;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class MyListener extends RunListener {

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);
        dumpChildren(description, 1);
    }

    private void dumpChildren(Description description, int level) {
        String levelString = String.format("%" + level + "c", ' ').replaceAll(" ", "~ ");
        System.out.printf("Description: %s%s (%d)%n", levelString, description, description.testCount());

        int next = level + 1;
        for (Description child : description.getChildren()) {
            dumpChildren(child, next);
        }
    }
}
