// Generated from "com/google/j2cl/benchmarks/octane/navierstokes/FluidField.java"
package com.google.j2cl.benchmarks.octane.navierstokes

import javaemul.lang.*
import kotlin.jvm.*

open class FluidField {
 @kotlin.jvm.JvmField var iterations: kotlin.Int = 10

 @kotlin.jvm.JvmField var dens: kotlin.DoubleArray? = null

 @kotlin.jvm.JvmField var densPrev: kotlin.DoubleArray? = null

 @kotlin.jvm.JvmField var u: kotlin.DoubleArray? = null

 @kotlin.jvm.JvmField var uPrev: kotlin.DoubleArray? = null

 @kotlin.jvm.JvmField var v: kotlin.DoubleArray? = null

 @kotlin.jvm.JvmField var vPrev: kotlin.DoubleArray? = null

 @kotlin.jvm.JvmField var width: kotlin.Int = 0

 @kotlin.jvm.JvmField var height: kotlin.Int = 0

 @kotlin.jvm.JvmField var rowSize: kotlin.Int = 0

 @kotlin.jvm.JvmField var size: kotlin.Int = 0

 @kotlin.jvm.JvmField var uiCallback: com.google.j2cl.benchmarks.octane.navierstokes.FluidField.UICallback? = null

 @kotlin.jvm.JvmField var displayFunc: com.google.j2cl.benchmarks.octane.navierstokes.FluidField.DisplayFunction? = null

 constructor() {
  this.setResolution(64, 64)
 }

 fun addFields(x: kotlin.DoubleArray?, s: kotlin.DoubleArray?, dt: kotlin.Double) {
  var i: kotlin.Int = 0
  LOOP@ while (i < this.size) {
   LOOP_CONTINUE@ do {
    x!![i] = x!![i] + dt * s!![i]
   } while (false)
   i = i + 1
  }
 }

 fun setBnd(b: kotlin.Int, x: kotlin.DoubleArray?) {
  if (b === 1) {
   var i: kotlin.Int = 1
   LOOP@ while (i <= this.width) {
    LOOP_CONTINUE@ do {
     x!![i] = x!![i + this.rowSize]
     x!![i + (this.height + 1) * this.rowSize] = x!![i + this.height * this.rowSize]
    } while (false)
    i = i + 1
   }
   var j: kotlin.Int = 1
   LOOP_1@ while (j <= this.height) {
    LOOP_CONTINUE_1@ do {
     x!![j * this.rowSize] = - x!![1 + j * this.rowSize]
     x!![this.width + 1 + j * this.rowSize] = - x!![this.width + j * this.rowSize]
    } while (false)
    j = j + 1
   }
  } else if (b === 2) {
   var i_1: kotlin.Int = 1
   LOOP_2@ while (i_1 <= this.width) {
    LOOP_CONTINUE_2@ do {
     x!![i_1] = - x!![i_1 + this.rowSize]
     x!![i_1 + (this.height + 1) * this.rowSize] = - x!![i_1 + this.height * this.rowSize]
    } while (false)
    i_1 = i_1 + 1
   }
   var j_1: kotlin.Int = 1
   LOOP_3@ while (j_1 <= this.height) {
    LOOP_CONTINUE_3@ do {
     x!![j_1 * this.rowSize] = x!![1 + j_1 * this.rowSize]
     x!![this.width + 1 + j_1 * this.rowSize] = x!![this.width + j_1 * this.rowSize]
    } while (false)
    j_1 = j_1 + 1
   }
  } else {
   var i_2: kotlin.Int = 1
   LOOP_4@ while (i_2 <= this.width) {
    LOOP_CONTINUE_4@ do {
     x!![i_2] = x!![i_2 + this.rowSize]
     x!![i_2 + (this.height + 1) * this.rowSize] = x!![i_2 + this.height * this.rowSize]
    } while (false)
    i_2 = i_2 + 1
   }
   var j_2: kotlin.Int = 1
   LOOP_5@ while (j_2 <= this.height) {
    LOOP_CONTINUE_5@ do {
     x!![j_2 * this.rowSize] = x!![1 + j_2 * this.rowSize]
     x!![this.width + 1 + j_2 * this.rowSize] = x!![this.width + j_2 * this.rowSize]
    } while (false)
    j_2 = j_2 + 1
   }
  }
  val maxEdge: kotlin.Int = (this.height + 1) * this.rowSize
  x!![0] = 0.5 * (x!![1] + x!![this.rowSize])
  x!![maxEdge] = 0.5 * (x!![1 + maxEdge] + x!![this.height * this.rowSize])
  x!![this.width + 1] = 0.5 * (x!![this.width] + x!![this.width + 1 + this.rowSize])
  x!![this.width + 1 + maxEdge] = 0.5 * (x!![this.width + maxEdge] + x!![this.width + 1 + this.height * this.rowSize])
 }

 fun linSolve(b: kotlin.Int, x: kotlin.DoubleArray?, x0: kotlin.DoubleArray?, a: kotlin.Int, c: kotlin.Int) {
  if (a === 0 && c === 1) {
   var j: kotlin.Int = 1
   LOOP@ while (j <= this.height) {
    LOOP_CONTINUE@ do {
     var currentRow: kotlin.Int = j * this.rowSize
     currentRow = currentRow + 1
     var i: kotlin.Int = 0
     LOOP_1@ while (i < this.width) {
      LOOP_CONTINUE_1@ do {
       x!![currentRow] = x0!![currentRow]
       currentRow = currentRow + 1
      } while (false)
      i = i + 1
     }
    } while (false)
    j = j + 1
   }
   this.setBnd(b, x)
  } else {
   val invC: kotlin.Double = 1.0 / c.toDouble()
   var k: kotlin.Int = 0
   LOOP_2@ while (k < this.iterations) {
    LOOP_CONTINUE_2@ do {
     var j_1: kotlin.Int = 1
     LOOP_3@ while (j_1 <= this.height) {
      LOOP_CONTINUE_3@ do {
       var lastRow: kotlin.Int = (j_1 - 1) * this.rowSize
       var currentRow_1: kotlin.Int = j_1 * this.rowSize
       var nextRow: kotlin.Int = (j_1 + 1) * this.rowSize
       var lastX: kotlin.Double = x!![currentRow_1]
       currentRow_1 = currentRow_1 + 1
       var i_1: kotlin.Int = 1
       LOOP_4@ while (i_1 <= this.width) {
        LOOP_CONTINUE_4@ do {
         lastX = kotlin.run {
          val `$array`: kotlin.DoubleArray? = x
          val `$index`: kotlin.Int = currentRow_1
          val `$value`: kotlin.Double = (x0!![currentRow_1] + a.toDouble() * (lastX + x!![kotlin.run {
           currentRow_1 = currentRow_1 + 1
           currentRow_1
          }] + x!![kotlin.run {
           lastRow = lastRow + 1
           lastRow
          }] + x!![kotlin.run {
           nextRow = nextRow + 1
           nextRow
          }])) * invC
          `$array`!![`$index`] = `$value`
          `$value`
         }
        } while (false)
        i_1 = i_1 + 1
       }
      } while (false)
      j_1 = j_1 + 1
     }
     this.setBnd(b, x)
    } while (false)
    k = k + 1
   }
  }
 }

 fun diffuse(b: kotlin.Int, x: kotlin.DoubleArray?, x0: kotlin.DoubleArray?, dt: kotlin.Double) {
  val a: kotlin.Int = 0
  this.linSolve(b, x, x0, a, 1 + 4 * a)
 }

 fun linSolve2(x: kotlin.DoubleArray?, x0: kotlin.DoubleArray?, y: kotlin.DoubleArray?, y0: kotlin.DoubleArray?, a: kotlin.Int, c: kotlin.Int) {
  if (a === 0 && c === 1) {
   var j: kotlin.Int = 1
   LOOP@ while (j <= this.height) {
    LOOP_CONTINUE@ do {
     var currentRow: kotlin.Int = j * this.rowSize
     currentRow = currentRow + 1
     var i: kotlin.Int = 0
     LOOP_1@ while (i < this.width) {
      LOOP_CONTINUE_1@ do {
       x!![currentRow] = x0!![currentRow]
       y!![currentRow] = y0!![currentRow]
       currentRow = currentRow + 1
      } while (false)
      i = i + 1
     }
    } while (false)
    j = j + 1
   }
   this.setBnd(1, x)
   this.setBnd(2, y)
  } else {
   val invC: kotlin.Double = 1.0 / c.toDouble()
   var k: kotlin.Int = 0
   LOOP_2@ while (k < this.iterations) {
    LOOP_CONTINUE_2@ do {
     var j_1: kotlin.Int = 1
     LOOP_3@ while (j_1 <= this.height) {
      LOOP_CONTINUE_3@ do {
       var lastRow: kotlin.Int = (j_1 - 1) * this.rowSize
       var currentRow_1: kotlin.Int = j_1 * this.rowSize
       var nextRow: kotlin.Int = (j_1 + 1) * this.rowSize
       var lastX: kotlin.Double = x!![currentRow_1]
       var lastY: kotlin.Double = y!![currentRow_1]
       currentRow_1 = currentRow_1 + 1
       var i_1: kotlin.Int = 1
       LOOP_4@ while (i_1 <= this.width) {
        LOOP_CONTINUE_4@ do {
         lastX = kotlin.run {
          val `$array`: kotlin.DoubleArray? = x
          val `$index`: kotlin.Int = currentRow_1
          val `$value`: kotlin.Double = (x0!![currentRow_1] + a.toDouble() * (lastX + x!![currentRow_1] + x!![lastRow] + x!![nextRow])) * invC
          `$array`!![`$index`] = `$value`
          `$value`
         }
         lastY = kotlin.run {
          val `$array_1`: kotlin.DoubleArray? = y
          val `$index_1`: kotlin.Int = currentRow_1
          val `$value_1`: kotlin.Double = (y0!![currentRow_1] + a.toDouble() * (lastY + y!![kotlin.run {
           currentRow_1 = currentRow_1 + 1
           currentRow_1
          }] + y!![kotlin.run {
           lastRow = lastRow + 1
           lastRow
          }] + y!![kotlin.run {
           nextRow = nextRow + 1
           nextRow
          }])) * invC
          `$array_1`!![`$index_1`] = `$value_1`
          `$value_1`
         }
        } while (false)
        i_1 = i_1 + 1
       }
      } while (false)
      j_1 = j_1 + 1
     }
     this.setBnd(1, x)
     this.setBnd(2, y)
    } while (false)
    k = k + 1
   }
  }
 }

 fun diffuse2(x: kotlin.DoubleArray?, x0: kotlin.DoubleArray?, y: kotlin.DoubleArray?, y0: kotlin.DoubleArray?, dt: kotlin.Double) {
  val a: kotlin.Int = 0
  this.linSolve2(x, x0, y, y0, a, 1 + 4 * a)
 }

 fun advect(b: kotlin.Int, d: kotlin.DoubleArray?, d0: kotlin.DoubleArray?, u: kotlin.DoubleArray?, v: kotlin.DoubleArray?, dt: kotlin.Double) {
  val wdt0: kotlin.Double = dt * this.width.toDouble()
  val hdt0: kotlin.Double = dt * this.height.toDouble()
  val wp5: kotlin.Double = this.width.toDouble() + 0.5
  val hp5: kotlin.Double = this.height.toDouble() + 0.5
  var j: kotlin.Int = 1
  LOOP@ while (j <= this.height) {
   LOOP_CONTINUE@ do {
    var pos: kotlin.Int = j * this.rowSize
    var i: kotlin.Int = 1
    LOOP_1@ while (i <= this.width) {
     LOOP_CONTINUE_1@ do {
      var x: kotlin.Double = i.toDouble() - wdt0 * u!![kotlin.run {
       pos = pos + 1
       pos
      }]
      var y: kotlin.Double = j.toDouble() - hdt0 * v!![pos]
      if (x < 0.5) {
       x = 0.5
      } else if (x > wp5) {
       x = wp5
      }
      val i0: kotlin.Int = x.toInt()
      val i1: kotlin.Int = i0 + 1
      if (y < 0.5) {
       y = 0.5
      } else if (y > hp5) {
       y = hp5
      }
      val j0: kotlin.Int = y.toInt()
      val j1: kotlin.Int = j0 + 1
      val s1: kotlin.Double = x - i0.toDouble()
      val s0: kotlin.Double = 1.0 - s1
      val t1: kotlin.Double = y - j0.toDouble()
      val t0: kotlin.Double = 1.0 - t1
      val row1: kotlin.Int = j0 * this.rowSize
      val row2: kotlin.Int = j1 * this.rowSize
      d!![pos] = s0 * (t0 * d0!![i0 + row1] + t1 * d0!![i0 + row2]) + s1 * (t0 * d0!![i1 + row1] + t1 * d0!![i1 + row2])
     } while (false)
     i = i + 1
    }
   } while (false)
   j = j + 1
  }
  this.setBnd(b, d)
 }

 fun project(u: kotlin.DoubleArray?, v: kotlin.DoubleArray?, p: kotlin.DoubleArray?, div: kotlin.DoubleArray?) {
  val h: kotlin.Double = - 0.5 / java.lang.Math.sqrt((this.width * this.height).toDouble())
  var j: kotlin.Int = 1
  LOOP@ while (j <= this.height) {
   LOOP_CONTINUE@ do {
    val row: kotlin.Int = j * this.rowSize
    var previousRow: kotlin.Int = (j - 1) * this.rowSize
    var prevValue: kotlin.Int = row - 1
    var currentRow: kotlin.Int = row
    var nextValue: kotlin.Int = row + 1
    var nextRow: kotlin.Int = (j + 1) * this.rowSize
    var i: kotlin.Int = 1
    LOOP_1@ while (i <= this.width) {
     LOOP_CONTINUE_1@ do {
      div!![kotlin.run {
       currentRow = currentRow + 1
       currentRow
      }] = h * (u!![kotlin.run {
       nextValue = nextValue + 1
       nextValue
      }] - u!![kotlin.run {
       prevValue = prevValue + 1
       prevValue
      }] + v!![kotlin.run {
       nextRow = nextRow + 1
       nextRow
      }] - v!![kotlin.run {
       previousRow = previousRow + 1
       previousRow
      }])
      p!![currentRow] = 0.0
     } while (false)
     i = i + 1
    }
   } while (false)
   j = j + 1
  }
  this.setBnd(0, div)
  this.setBnd(0, p)
  this.linSolve(0, p, div, 1, 4)
  val wScale: kotlin.Double = 0.5 * this.width.toDouble()
  val hScale: kotlin.Double = 0.5 * this.height.toDouble()
  var j_1: kotlin.Int = 1
  LOOP_2@ while (j_1 <= this.height) {
   LOOP_CONTINUE_2@ do {
    var prevPos: kotlin.Int = j_1 * this.rowSize - 1
    var currentPos: kotlin.Int = j_1 * this.rowSize
    var nextPos: kotlin.Int = j_1 * this.rowSize + 1
    var prevRow: kotlin.Int = (j_1 - 1) * this.rowSize
    val currentRow_1: kotlin.Int = j_1 * this.rowSize
    var nextRow_1: kotlin.Int = (j_1 + 1) * this.rowSize
    var i_1: kotlin.Int = 1
    LOOP_3@ while (i_1 <= this.width) {
     LOOP_CONTINUE_3@ do {
      kotlin.run {
       val `$array`: kotlin.DoubleArray? = u
       val `$index`: kotlin.Int = kotlin.run {
        currentPos = currentPos + 1
        currentPos
       }
       kotlin.run {
        val `$array_1`: kotlin.DoubleArray? = `$array`
        val `$index_1`: kotlin.Int = `$index`
        val `$value`: kotlin.Double = `$array`!![`$index`] - wScale * (p!![kotlin.run {
         nextPos = nextPos + 1
         nextPos
        }] - p!![kotlin.run {
         prevPos = prevPos + 1
         prevPos
        }])
        `$array_1`!![`$index_1`] = `$value`
        `$value`
       }
      }
      v!![currentPos] = v!![currentPos] - hScale * (p!![kotlin.run {
       nextRow_1 = nextRow_1 + 1
       nextRow_1
      }] - p!![kotlin.run {
       prevRow = prevRow + 1
       prevRow
      }])
     } while (false)
     i_1 = i_1 + 1
    }
   } while (false)
   j_1 = j_1 + 1
  }
  this.setBnd(1, u)
  this.setBnd(2, v)
 }

 fun densStep(x: kotlin.DoubleArray?, x0: kotlin.DoubleArray?, u: kotlin.DoubleArray?, v: kotlin.DoubleArray?, dt: kotlin.Double) {
  this.addFields(x, x0, dt)
  this.diffuse(0, x0, x, dt)
  this.advect(0, x, x0, u, v, dt)
 }

 fun velStep(u: kotlin.DoubleArray?, v: kotlin.DoubleArray?, u0: kotlin.DoubleArray?, v0: kotlin.DoubleArray?, dt: kotlin.Double) {
  var u_1: kotlin.DoubleArray? = u
  var v_1: kotlin.DoubleArray? = v
  var u0_1: kotlin.DoubleArray? = u0
  var v0_1: kotlin.DoubleArray? = v0
  this.addFields(u_1, u0_1, dt)
  this.addFields(v_1, v0_1, dt)
  var temp: kotlin.DoubleArray? = u0_1
  u0_1 = u_1
  u_1 = temp
  temp = v0_1
  v0_1 = v_1
  v_1 = temp
  this.diffuse2(u_1, u0_1, v_1, v0_1, dt)
  this.project(u_1, v_1, u0_1, v0_1)
  temp = u0_1
  u0_1 = u_1
  u_1 = temp
  temp = v0_1
  v0_1 = v_1
  v_1 = temp
  this.advect(1, u_1, u0_1, u0_1, v0_1, dt)
  this.advect(2, v_1, v0_1, u0_1, v0_1, dt)
  this.project(u_1, v_1, u0_1, v0_1)
 }

 fun queryUI(d: kotlin.DoubleArray?, u: kotlin.DoubleArray?, v: kotlin.DoubleArray?) {
  var i: kotlin.Int = 0
  LOOP@ while (i < this.size) {
   LOOP_CONTINUE@ do {
    u!![i] = kotlin.run {
     val `$array`: kotlin.DoubleArray? = v
     val `$index`: kotlin.Int = i
     val `$value`: kotlin.Double = kotlin.run {
      val `$array_1`: kotlin.DoubleArray? = d
      val `$index_1`: kotlin.Int = i
      val `$value_1`: kotlin.Double = 0.0
      `$array_1`!![`$index_1`] = `$value_1`
      `$value_1`
     }
     `$array`!![`$index`] = `$value`
     `$value`
    }
   } while (false)
   i = i + 1
  }
  this.uiCallback!!.prepareFrame(com.google.j2cl.benchmarks.octane.navierstokes.Field(d, u, v, this.rowSize))
 }

 open fun update() {
  this.queryUI(this.densPrev, this.uPrev, this.vPrev)
  this.velStep(this.u, this.v, this.uPrev, this.vPrev, com.google.j2cl.benchmarks.octane.navierstokes.FluidField.DT)
  this.densStep(this.dens, this.densPrev, this.u, this.v, com.google.j2cl.benchmarks.octane.navierstokes.FluidField.DT)
  this.displayFunc!!.call(com.google.j2cl.benchmarks.octane.navierstokes.Field(this.dens, this.u, this.v, this.rowSize))
 }

 open fun setDisplayFunction(func: com.google.j2cl.benchmarks.octane.navierstokes.FluidField.DisplayFunction?) {
  this.displayFunc = func
 }

 open fun setIterations(iters: kotlin.Int) {
  if (iters > 0 && iters <= 100) {
   this.iterations = iters
  }
 }

 open fun setUICallback(callback: com.google.j2cl.benchmarks.octane.navierstokes.FluidField.UICallback?) {
  this.uiCallback = callback
 }

 open fun reset() {
  this.rowSize = this.width + 2
  this.size = (this.width + 2) * (this.height + 2)
  this.dens = kotlin.DoubleArray(this.size)
  this.densPrev = kotlin.DoubleArray(this.size)
  this.u = kotlin.DoubleArray(this.size)
  this.uPrev = kotlin.DoubleArray(this.size)
  this.v = kotlin.DoubleArray(this.size)
  this.vPrev = kotlin.DoubleArray(this.size)
 }

 open fun getDens(): kotlin.DoubleArray? {
  return this.dens
 }

 open fun setResolution(hRes: kotlin.Int, wRes: kotlin.Int): kotlin.Boolean {
  val res: kotlin.Int = wRes * hRes
  if (res > 0 && res < 1000000 && (wRes !== this.width || hRes !== this.height)) {
   this.width = wRes
   this.height = hRes
   this.reset()
   return true
  }
  return false
 }

 companion object {
  @kotlin.jvm.JvmField val DT: kotlin.Double = 0.1
 }

 fun interface DisplayFunction {
  fun call(f: com.google.j2cl.benchmarks.octane.navierstokes.Field?)
 }

 fun interface UICallback {
  fun prepareFrame(field: com.google.j2cl.benchmarks.octane.navierstokes.Field?)
 }
}
