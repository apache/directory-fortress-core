package us.jts.fortress.util;

import org.apache.tools.ant.Task;

/**
 * Interface is extended by custom Ant tasks that require JUnit validation.
 *
 * @author Shawn McKinney
 */
public interface Testable
{
    public void execute( Task task );
}
