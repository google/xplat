/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An optionally-bounded {@linkplain BlockingQueue blocking queue} based on linked nodes. This queue
 * orders elements FIFO (first-in-first-out). The <em>head</em> of the queue is that element that
 * has been on the queue the longest time. The <em>tail</em> of the queue is that element that has
 * been on the queue the shortest time. New elements are inserted at the tail of the queue, and the
 * queue retrieval operations obtain elements at the head of the queue. Linked queues typically have
 * higher throughput than array-based queues but less predictable performance in most concurrent
 * applications.
 *
 * <p>The optional capacity bound constructor argument serves as a way to prevent excessive queue
 * expansion. The capacity, if unspecified, is equal to {@link Integer#MAX_VALUE}. Linked nodes are
 * dynamically created upon each insertion unless this would bring the queue above capacity.
 *
 * <p>This class and its iterator implement all of the <em>optional</em> methods of the {@link
 * Collection} and {@link Iterator} interfaces.
 *
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 */
@NullMarked
public class LinkedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E> {

  /*
   * A variant of the "two lock queue" algorithm.  The putLock gates
   * entry to put (and offer), and has an associated condition for
   * waiting puts.  Similarly for the takeLock.  The "count" field
   * that they both rely on is maintained as an atomic to avoid
   * needing to get both locks in most cases. Also, to minimize need
   * for puts to get takeLock and vice-versa, cascading notifies are
   * used. When a put notices that it has enabled at least one take,
   * it signals taker. That taker in turn signals others if more
   * items have been entered since the signal. And symmetrically for
   * takes signalling puts. Operations such as remove(Object) and
   * iterators acquire both locks.
   *
   * Visibility between writers and readers is provided as follows:
   *
   * Whenever an element is enqueued, the putLock is acquired and
   * count updated.  A subsequent reader guarantees visibility to the
   * enqueued Node by either acquiring the putLock (via fullyLock)
   * or by acquiring the takeLock, and then reading n = count.get();
   * this gives visibility to the first n items.
   *
   * To implement weakly consistent iterators, it appears we need to
   * keep all Nodes GC-reachable from a predecessor dequeued Node.
   * That would cause two problems:
   * - allow a rogue Iterator to cause unbounded memory retention
   * - cause cross-generational linking of old Nodes to new Nodes if
   *   a Node was tenured while live, which generational GCs have a
   *   hard time dealing with, causing repeated major collections.
   * However, only non-deleted Nodes need to be reachable from
   * dequeued Nodes, and reachability does not necessarily have to
   * be of the kind understood by the GC.  We use the trick of
   * linking a Node that has just been dequeued to itself.  Such a
   * self-link implicitly means to advance to head.next.
   */

  /** Linked list node class */
  static class Node<E> {
    E item;

    /**
     * One of: - the real successor Node - this Node, meaning the successor is head.next - null,
     * meaning there is no successor (this is the last node)
     */
    @Nullable Node<E> next;

    Node(E x) {
      item = x;
    }
  }

  /** Creates a {@code LinkedBlockingQueue} with a capacity of {@link Integer#MAX_VALUE}. */
  public LinkedBlockingQueue() {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a {@code LinkedBlockingQueue} with the given (fixed) capacity.
   *
   * @param capacity the capacity of this queue
   * @throws IllegalArgumentException if {@code capacity} is not greater than zero
   */
  public LinkedBlockingQueue(int capacity) {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a {@code LinkedBlockingQueue} with a capacity of {@link Integer#MAX_VALUE}, initially
   * containing the elements of the given collection, added in traversal order of the collection's
   * iterator.
   *
   * @param c the collection of elements to initially contain
   * @throws NullPointerException if the specified collection or any of its elements are null
   */
  public LinkedBlockingQueue(Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  // this doc comment is overridden to remove the reference to collections
  // greater in size than Integer.MAX_VALUE
  /**
   * Returns the number of elements in this queue.
   *
   * @return the number of elements in this queue
   */
  public int size() {
    throw new UnsupportedOperationException();
  }

  // this doc comment is a modified copy of the inherited doc comment,
  // without the reference to unlimited queues.
  /**
   * Returns the number of additional elements that this queue can ideally (in the absence of memory
   * or resource constraints) accept without blocking. This is always equal to the initial capacity
   * of this queue less the current {@code size} of this queue.
   *
   * <p>Note that you <em>cannot</em> always tell if an attempt to insert an element will succeed by
   * inspecting {@code remainingCapacity} because it may be the case that another thread is about to
   * insert or remove an element.
   */
  public int remainingCapacity() {
    throw new UnsupportedOperationException();
  }

  /**
   * Inserts the specified element at the tail of this queue, waiting if necessary for space to
   * become available.
   *
   * @throws InterruptedException {@inheritDoc}
   * @throws NullPointerException {@inheritDoc}
   */
  public void put(E e) throws InterruptedException {
    throw new UnsupportedOperationException();
  }

  /**
   * Inserts the specified element at the tail of this queue, waiting if necessary up to the
   * specified wait time for space to become available.
   *
   * @return {@code true} if successful, or {@code false} if the specified waiting time elapses
   *     before space is available
   * @throws InterruptedException {@inheritDoc}
   * @throws NullPointerException {@inheritDoc}
   */
  public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
    throw new UnsupportedOperationException();
  }

  /**
   * Inserts the specified element at the tail of this queue if it is possible to do so immediately
   * without exceeding the queue's capacity, returning {@code true} upon success and {@code false}
   * if this queue is full. When using a capacity-restricted queue, this method is generally
   * preferable to method {@link BlockingQueue#add add}, which can fail to insert an element only by
   * throwing an exception.
   *
   * @throws NullPointerException if the specified element is null
   */
  public boolean offer(E e) {
    throw new UnsupportedOperationException();
  }

  public E take() throws InterruptedException {
    throw new UnsupportedOperationException();
  }

  public @Nullable E poll(long timeout, TimeUnit unit) throws InterruptedException {
    throw new UnsupportedOperationException();
  }

  public @Nullable E poll() {
    throw new UnsupportedOperationException();
  }

  public @Nullable E peek() {
    throw new UnsupportedOperationException();
  }

  /**
   * Removes a single instance of the specified element from this queue, if it is present. More
   * formally, removes an element {@code e} such that {@code o.equals(e)}, if this queue contains
   * one or more such elements. Returns {@code true} if this queue contained the specified element
   * (or equivalently, if this queue changed as a result of the call).
   *
   * @param o element to be removed from this queue, if present
   * @return {@code true} if this queue changed as a result of the call
   */
  public boolean remove(@Nullable Object o) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns {@code true} if this queue contains the specified element. More formally, returns
   * {@code true} if and only if this queue contains at least one element {@code e} such that {@code
   * o.equals(e)}.
   *
   * @param o object to be checked for containment in this queue
   * @return {@code true} if this queue contains the specified element
   */
  public boolean contains(@Nullable Object o) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns an array containing all the elements in this queue, in proper sequence.
   *
   * <p>The returned array will be "safe" in that no references to it are maintained by this queue.
   * (In other words, this method must allocate a new array). The caller is thus free to modify the
   * returned array.
   *
   * <p>This method acts as bridge between array-based and collection-based APIs.
   *
   * @return an array containing all the elements in this queue
   */
  public @Nullable Object[] toArray() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns an array containing all the elements in this queue, in proper sequence; the runtime
   * type of the returned array is that of the specified array. If the queue fits in the specified
   * array, it is returned therein. Otherwise, a new array is allocated with the runtime type of the
   * specified array and the size of this queue.
   *
   * <p>If this queue fits in the specified array with room to spare (i.e., the array has more
   * elements than this queue), the element in the array immediately following the end of the queue
   * is set to {@code null}.
   *
   * <p>Like the {@link #toArray()} method, this method acts as bridge between array-based and
   * collection-based APIs. Further, this method allows precise control over the runtime type of the
   * output array, and may, under certain circumstances, be used to save allocation costs.
   *
   * <p>Suppose {@code x} is a queue known to contain only strings. The following code can be used
   * to dump the queue into a newly allocated array of {@code String}:
   *
   * <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
   *
   * Note that {@code toArray(new Object[0])} is identical in function to {@code toArray()}.
   *
   * @param a the array into which the elements of the queue are to be stored, if it is big enough;
   *     otherwise, a new array of the same runtime type is allocated for this purpose
   * @return an array containing all the elements in this queue
   * @throws ArrayStoreException if the runtime type of the specified array is not a supertype of
   *     the runtime type of every element in this queue
   * @throws NullPointerException if the specified array is null
   */
  @SuppressWarnings("unchecked")
  public <T extends @Nullable Object> T[] toArray(T[] a) {
    throw new UnsupportedOperationException();
  }

  public String toString() {
    throw new UnsupportedOperationException();
  }

  /**
   * Atomically removes all of the elements from this queue. The queue will be empty after this call
   * returns.
   */
  public void clear() {
    throw new UnsupportedOperationException();
  }

  /**
   * @throws UnsupportedOperationException {@inheritDoc}
   * @throws ClassCastException {@inheritDoc}
   * @throws NullPointerException {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public int drainTo(Collection<? super E> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * @throws UnsupportedOperationException {@inheritDoc}
   * @throws ClassCastException {@inheritDoc}
   * @throws NullPointerException {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public int drainTo(Collection<? super E> c, int maxElements) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns an iterator over the elements in this queue in proper sequence. The elements will be
   * returned in order from first (head) to last (tail).
   *
   * <p>The returned iterator is a "weakly consistent" iterator that will never throw {@link
   * java.util.ConcurrentModificationException ConcurrentModificationException}, and guarantees to
   * traverse elements as they existed upon construction of the iterator, and may (but is not
   * guaranteed to) reflect any modifications subsequent to construction.
   *
   * @return an iterator over the elements in this queue in proper sequence
   */
  public Iterator<E> iterator() {
    throw new UnsupportedOperationException();
  }
}
