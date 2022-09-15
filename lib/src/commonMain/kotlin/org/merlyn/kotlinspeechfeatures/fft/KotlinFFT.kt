package org.merlyn.kotlinspeechfeatures.fft

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class KotlinFFT : FFT {

    /**
     * Compute the FFT of x[], assuming its length n is a power of 2
     */
    override fun rfft(signal: FloatArray, nfft: Int): Array<Complex> {
        val complexSignal = signal.map {
            Complex(it.toDouble(), 0.0)
        }
        return rfftComplex(complexSignal.toTypedArray()).filterNotNull().toTypedArray()
    }

    private fun rfftComplex(complexSignal: Array<Complex?>): Array<Complex?> {
        val n = complexSignal.size

        // base case
        if (n == 1) return arrayOf(complexSignal[0]!!)

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw IllegalArgumentException("n is not a power of 2")
        }

        // compute FFT of even terms
        val even: Array<Complex?> = arrayOfNulls(n / 2)
        for (k in 0 until n / 2) {
            even[k] = complexSignal[2 * k]
        }
        val evenFFT = rfftComplex(even)

        // compute FFT of odd terms
        val odd = even // reuse the array (to avoid n log n space)
        for (k in 0 until n / 2) {
            odd[k] = complexSignal[2 * k + 1]
        }
        val oddFFT = rfftComplex(odd)

        // combine
        val y: Array<Complex?> = arrayOfNulls(n)
        for (k in 0 until n / 2) {
            val kth: Double = -2 * k * PI / n
            val wk = Complex(cos(kth), sin(kth))
            y[k] = evenFFT[k]!!.plus(wk.times(oddFFT[k]!!))
            y[k + n / 2] = evenFFT[k]!!.minus(wk.times(oddFFT[k]!!))
        }
        return y
    }

}