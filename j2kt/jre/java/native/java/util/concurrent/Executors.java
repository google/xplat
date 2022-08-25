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

import org.jspecify.nullness.Nullable;

/**
 * Emulation of executors.
 */
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

  private static final class RunnableAdapter<T> implements Callable<@Nullable T> {

    final Runnable task;
    final T result;

    RunnableAdapter(Runnable task, @Nullable T result) {
      this.task = task;
      this.result = result;
    }

    public @Nullable T call() {
      task.run();
      return result;
    }
  }

  private Executors() {
  }
}
