package org.merlyn.kotlinspeechfeatures.fft

import kotlin.math.atan2
import kotlin.math.cosh
import kotlin.math.hypot
import kotlin.math.sinh

class Complex(private val real: Double, private val imag: Double) {

    // return a string representation of the invoking Complex object
    override fun toString(): String {
        if (imag == 0.0) return real.toString() + ""
        if (real == 0.0) return imag.toString() + "i"
        return if (imag < 0) real.toString() + " - " + -imag + "i" else real.toString() + " + " + imag + "i"
    }

    // return abs/modulus/magnitude
    fun abs(): Double {
        return hypot(real, imag)
    }

    // return angle/phase/argument, normalized to be between -pi and pi
    fun phase(): Double {
        return atan2(imag, real)
    }

    // return a new Complex object whose value is (this + b)
    operator fun plus(b: Complex): Complex {
        val a = this // invoking object
        val real = a.real + b.real
        val imag = a.imag + b.imag
        return Complex(real, imag)
    }

    // return a new Complex object whose value is (this - b)
    operator fun minus(b: Complex): Complex {
        val a = this
        val real = a.real - b.real
        val imag = a.imag - b.imag
        return Complex(real, imag)
    }

    // return a new Complex object whose value is (this * b)
    operator fun times(b: Complex): Complex {
        val a = this
        val real = a.real * b.real - a.imag * b.imag
        val imag = a.real * b.imag + a.imag * b.real
        return Complex(real, imag)
    }

    // return a new object whose value is (this * alpha)
    fun scale(alpha: Double): Complex {
        return Complex(alpha * real, alpha * imag)
    }

    // return a new Complex object whose value is the conjugate of this
    fun conjugate(): Complex {
        return Complex(real, -imag)
    }

    // return a new Complex object whose value is the reciprocal of this
    fun reciprocal(): Complex {
        val scale = real * real + imag * imag
        return Complex(real / scale, -imag / scale)
    }

    // return the real or imaginary part
    fun re(): Double {
        return real
    }

    fun im(): Double {
        return imag
    }

    // return a / b
    fun divides(b: Complex): Complex? {
        val a = this
        return a.times(b.reciprocal())
    }

    // return a new Complex object whose value is the complex exponential of this
    fun exp(): Complex? {
        return Complex(
            kotlin.math.exp(real) * kotlin.math.cos(imag),
            kotlin.math.exp(real) * kotlin.math.sin(imag)
        )
    }

    // return a new Complex object whose value is the complex sine of this
    fun sin(): Complex {
        return Complex(
            kotlin.math.sin(real) * cosh(imag),
            kotlin.math.cos(real) * sinh(imag)
        )
    }

    // return a new Complex object whose value is the complex cosine of this
    fun cos(): Complex {
        return Complex(
            kotlin.math.cos(real) * cosh(imag),
            -kotlin.math.sin(real) * sinh(imag)
        )
    }

    // return a new Complex object whose value is the complex tangent of this
    fun tan(): Complex? {
        return sin().divides(cos())
    }

    // a static version of plus
    fun plus(a: Complex, b: Complex): Complex? {
        val real = a.real + b.real
        val imag = a.imag + b.imag
        return Complex(real, imag)
    }

    override fun equals(x: Any?): Boolean {
        if (x == null) return false
        val that = x as Complex
        return real == that.real && imag == that.imag
    }

    override fun hashCode(): Int {
        var result = real.hashCode()
        result = 31 * result + imag.hashCode()
        return result
    }

}
