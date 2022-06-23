// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Shape.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

abstract class Shape {
 abstract fun intersect(ray: com.google.j2cl.benchmarks.octane.raytrace.Ray?): com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo?

 abstract fun getMaterial(): com.google.j2cl.benchmarks.octane.raytrace.BaseMaterial?

 abstract fun getPosition(): com.google.j2cl.benchmarks.octane.raytrace.Vector?
}
