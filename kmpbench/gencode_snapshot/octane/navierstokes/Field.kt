// Generated from "com/google/j2cl/benchmarks/octane/navierstokes/Field.java"
package com.google.j2cl.benchmarks.octane.navierstokes

import javaemul.lang.*
import kotlin.jvm.*

open class Field {
 @kotlin.jvm.JvmField val dens: kotlin.DoubleArray?

 @kotlin.jvm.JvmField val u: kotlin.DoubleArray?

 @kotlin.jvm.JvmField val v: kotlin.DoubleArray?

 @kotlin.jvm.JvmField val rowSize: kotlin.Int

 constructor(dens: kotlin.DoubleArray?, u: kotlin.DoubleArray?, v: kotlin.DoubleArray?, rowSize: kotlin.Int) {
  this.dens = dens
  this.u = u
  this.v = v
  this.rowSize = rowSize
 }

 open fun setDensity(x: kotlin.Int, y: kotlin.Int, d: kotlin.Double) {
  this.dens!![x + 1 + (y + 1) * this.rowSize] = d
 }

 open fun getDensity(x: kotlin.Int, y: kotlin.Int): kotlin.Double {
  return this.dens!![x + 1 + (y + 1) * this.rowSize]
 }

 open fun setVelocity(x: kotlin.Int, y: kotlin.Int, xv: kotlin.Double, yv: kotlin.Double) {
  this.u!![x + 1 + (y + 1) * this.rowSize] = xv
  this.v!![x + 1 + (y + 1) * this.rowSize] = yv
 }

 open fun getXVelocity(x: kotlin.Int, y: kotlin.Int): kotlin.Double {
  return this.u!![x + 1 + (y + 1) * this.rowSize]
 }

 open fun getYVelocity(x: kotlin.Int, y: kotlin.Int): kotlin.Double {
  return this.v!![x + 1 + (y + 1) * this.rowSize]
 }
}
