package org.merlyn.kotlinspeechfeatures

import org.merlyn.kotlinspeechfeatures.internal.stdev

class MathUtils {
    companion object {
        fun normalize(sig: IntArray): FloatArray {
            val std = stdev(sig.map { it.toDouble() }.toDoubleArray())
            val mean = sig.sum() / sig.size
            val normalizedResponse = FloatArray(sig.size)
            if (std != 0.0) {
                for ((index,element) in sig.withIndex()){
                    normalizedResponse[index] = ((element - mean) / std).toFloat()
                }
            }
            return normalizedResponse
        }
    }
}
