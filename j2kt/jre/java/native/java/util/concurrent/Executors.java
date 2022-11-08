// CHECKSTYLE_OFF: Copyrighted to members of JCP JSR-166 Expert Group.
/*
 * This file is a modified version of
 * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/main/java/util/concurrent/Executors.java?revision=1.90
 * which contained the following notice:
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
// CHECKSTYLE_ON

package java.util.concurrent;

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/** Emulation of executors. */
@NullMarked
public class Executors {

  public static <T extends @Nullable Object> Callable<T> callable(Runnable task, T result) {
    if (task == null) {
      throw new NullPointerException();
    }
    return new RunnableAdapter<T>(task, result);
  }

  public static Callable<@Nullable Object> callable(Runnable task) {
    if (task == null) {
      throw new NullPointerException();
    }
    return new RunnableAdapter<@Nullable Object>(task, null);
  }

  private static final class RunnableAdapter<T extends @Nullable Object> implements Callable<T> {

    final Runnable task;
    final T result;

    RunnableAdapter(Runnable task, T result) {
      this.task = task;
      this.result = result;
    }

    @Override
    public T call() {
      task.run();
      return result;
    }
  }

  private Executors() {
  }
}
