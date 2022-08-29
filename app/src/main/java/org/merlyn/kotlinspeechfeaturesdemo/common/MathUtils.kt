package org.merlyn.kotlinspeechfeaturesdemo.common

object MathUtils {

    fun stdev(numArray: DoubleArray): Double {
        var sum = 0.0
        var standardDeviation = 0.0

        for (num in numArray) {
            sum += num
        }

        val mean = sum / numArray.size

        for (num in numArray) {
            standardDeviation += Math.pow(num - mean, 2.0)
        }

        return Math.sqrt(standardDeviation / numArray.size)
    }

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