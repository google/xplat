/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.j2cl.benchmarking.framework

// This is a Kotlin port of the corresponding J2cl Java class in
// google3/third_party/java_src/j2cl/benchmarking/java/com/google/j2cl/benchmarking/framework/

/** Ensures multiple versions of the collections are alive to prevent special case optimizations. */
object CollectionUtilizer {
  fun dependOnAllCollections() {
    utilizeMap(mutableMapOf<String, String>())
    utilizeList(mutableListOf<String>())

    // TODO(b/230841155): Some of the more complex utilization code that doesn't translate 1:1 to
    //   native Kotlin was removed here. Before spending effort, clarify with J2cl benchmark owners
    //   why it was considered necessary

  }

  private fun utilizeMap(map: MutableMap<String, String>) {
    if (addGet(map) && iterate(map) && remove(map)) {
      return
    }
    throw AssertionError()
  }

  private fun addGet(map: MutableMap<String, String>): Boolean {
    map["input"] = "output"
    return "output" == map.get("input") && map.size == 1
  }

  private fun iterate(map: Map<String, String>): Boolean {
    var result = ""
    for (entry in map.entries) {
      result += entry.key + entry.value
    }
    return result.length > 0
  }

  private fun remove(map: MutableMap<String, String>) = "output" == map.remove("input")

  private fun utilizeList(list: MutableList<String>) {
    if (addGet(list) && iterate(list) && remove(list)) {
      return
    }
    throw AssertionError()
  }

  private fun addGet(list: MutableList<String>): Boolean {
    list.add("input")
    return "input" == list[0] && list.size == 1
  }

  private fun remove(list: MutableList<String>) = "input" == list.removeAt(0)

  private fun iterate(list: List<String>): Boolean {
    var result = ""
    for (entry in list) {
      result += entry
    }
    return result.length > 0
  }
}
