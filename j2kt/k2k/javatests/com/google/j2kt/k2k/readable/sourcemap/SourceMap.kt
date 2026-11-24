@file:Suppress("UNNECESSARY_NOT_NULL_ASSERTION", "USELESS_CAST", "UNUSED_ANONYMOUS_PARAMETER")

package sourcemap

import kotlin.Suppress

open class SourceMap {
  fun interface Consumer<T> {
    fun accept(value: T)
  }

  open fun runConsumer(x: Int, consumer: Consumer<String>) {}

  open fun test(s: String?, limit: Int): String {
    val nonNull: String = (s as String?)!!
    var i: Int = 0
    var count: Int = 0
    while (i < limit) {
      count = count + 1
      i = i + 1
    }
    this.runConsumer(count, Consumer { unused: String -> println(nonNull) })
    return "" + nonNull + count
  }
}
