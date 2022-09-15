package org.merlyn.kotlinspeechfeatures.internal
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.*

internal suspend fun <T> Array<T>.asyncForEachIndexed(function: (Int, T) -> Unit): List<Unit> {
    return mapIndexed { index, element ->
        GlobalScope.async(dispatcherDefault()) {
            function(index, element)
        }
    }.awaitAll()
}

private fun dispatcherDefault() : CoroutineContext {
    val crashHandler = CoroutineExceptionHandler {
            _, exception -> println("CoroutineCrash: ${exception.message}\n${exception.stackTraceToString()}")
    }
    return Dispatchers.Default + crashHandler
}

internal fun stdev(numArray: DoubleArray): Double {
    var sum = 0.0
    var standardDeviation = 0.0

    for (num in numArray) {
        sum += num
    }

    val mean = sum / numArray.size

    for (num in numArray) {
        standardDeviation += (num - mean).pow(2.0)
    }

    return sqrt(standardDeviation / numArray.size)
}
internal suspend fun floatArrayLog(feat:Array<FloatArray>): Array<FloatArray> {
    var logfeat = Array(feat.size) { FloatArray (feat[0].size)}
    feat.asyncForEachIndexed { i, row ->
        for ((j,element) in row.withIndex()){
            logfeat[i][j] = ln(element)
        }
    }
    return logfeat
}

internal suspend fun dct2withLifter(feat: Array<FloatArray>, aNCep: Int, aNFilters: Int, ceplifter: Int): Array<FloatArray>{
    val response : Array<FloatArray> = Array(feat.size) { FloatArray( feat[0].size) }
    feat.asyncForEachIndexed { index, row ->
        val tempResponse = oneDdct2(flatfeat = row, aNCep=aNCep, aNFilters=aNFilters)
        response[index] = cepstralLifter(tempResponse,1,aNCep=aNCep, aCepLifter = ceplifter)
    }
    return response
}

internal fun cepstralLifter(aCepstra: FloatArray, aNFrames: Int, aNCep: Int, aCepLifter: Int): FloatArray {
    val lifter = (aCepLifter / 2.0).toFloat()
    val factors = FloatArray(aNCep)
    for (i in 0 until aNCep) {
        factors[i] = (1 + lifter * sin(PI.toFloat() * i / aCepLifter.toFloat()))
    }

    run {
        var i = 0
        var idx = 0
        while (i < aNFrames) {
            var j = 0
            while (j < aNCep) {
                aCepstra[idx] = aCepstra[idx] * factors[j]
                j++
                idx++
            }
            i++
        }
    }
    return aCepstra
}

private fun oneDdct2(flatfeat: FloatArray, aNCep:Int, aNFilters:Int ): FloatArray {
    var nFrames: Int = 1

    val dct2f = FloatArray(aNFilters * aNCep)

    // Perform DCT-II
    val sqrVal1 = 1/(4*aNFilters).toFloat()
    val sf1 = sqrt(sqrVal1)
    val sqrVal2 = 1/(2*aNFilters).toDouble()
    val sf2 = sqrt(sqrVal2)
    val mfcc = FloatArray(nFrames * aNCep)
    run {
        var i = 0
        var idx = 0
        var fidx = 0
        while (i < nFrames) {
            var j = 0
            var didx = 0
            while (j < aNCep) {
                var sum = 0.0
                var k = 0
                while (aNFilters > k) {
                    if (i == 0) {
                        dct2f[didx] = cos(PI.toFloat() * j * (2 * k + 1) / (2 * aNFilters.toFloat()))
                    }
                    sum += flatfeat.get(fidx + k) * dct2f[didx]
                    k++
                    didx++
                }
                if (i == 0 && j == 0){
                    mfcc[idx + j] = (sum * 2.0 * sf1).toFloat()
                }
                else{
                    mfcc[idx + j] = (sum * 2.0 * sf2).toFloat()
                }
                j++
            }
            i++
            idx += aNCep
            fidx += aNFilters.toInt()
        }
    }
    return mfcc
}

internal suspend fun calcEnergy(pspec: Array<FloatArray>): FloatArray {
    val energy = sumAcrossRows(pspec) // equivalent to numpy.sum(arr,1)
    for ((index, element) in energy.withIndex()){ // equivalent to numpy.where(...)
        //if energy is zero, we get problems with log
        if (element == 0.0f) {
            energy[index] = 2.220446049250313E-16f
        }
    }
    return energy
}

private suspend fun sumAcrossRows(arr:Array<FloatArray>): FloatArray {
    val response = FloatArray(arr.size)
    arr.asyncForEachIndexed { index, _ ->
        response[index] = arr[index].sum()
    }
    return response
}

private fun multipliedSum(v1:FloatArray, v2: FloatArray): Float {
    var result = 0.0f
    for (i in v1.indices){
        val product = v1[i]*v2[i]
        result += product
    }
    return result
}

internal fun linspace(start: Double, stop: Double, num: Double) = Array(num.toInt()) { start + it * ((stop - start) / (num - 1)) }

internal suspend fun dot2d(v1:Array<FloatArray>, v2: Array<FloatArray>): Array<FloatArray> {
    val v2T = transpose(v2)
    val response : Array<FloatArray> = Array(v1.size) { FloatArray(v2[0].size)}
    for (i in v1.indices) {
        val x1 = v1[i]
        for (j in v2T[0].indices) {
            val x2 = v2T.map { it[j] }.toFloatArray()
            response[i][j] = multipliedSum(x1, x2)
        }
    }
    return response
}

internal suspend fun calcFeat(pspec: Array<FloatArray>, fb: Array<FloatArray>): Array<FloatArray> {
    val response = dot2d(pspec, fb)
    response.asyncForEachIndexed { i, row ->
        for ((j, element) in row.withIndex()){
            if (element == 0.0f) {
                response[i][j] = 2.220446049250313E-16f
            }
        }
    }
    return response
}

internal fun mel2hz(mel:Double): Double {
    return 700.0*(10.0.pow(((mel / 2595.0))) -1)
}

internal fun tile(x: FloatArray, repsY: Int, repsX: Int): Array<FloatArray> {
    val newArray = mutableListOf<FloatArray>()
    val x2 = FloatArray(x.size * repsX)
    for (i in 0 until repsX) {
        for (j in x.indices) {
            x2[i+j] = x[j]
        }
    }
    for (i in 1..repsY) {
        newArray.add(x2)
    }
    return newArray.toTypedArray()
}

internal fun transpose(arr: Array<FloatArray>): Array<FloatArray>{
    val x = arr.size
    val y = arr[0].size
    val response = Array(y) {FloatArray(x)}
    for (i in 0 until x){
        for (j in 0 until y){
            response[j][i] = arr[i][j]
        }
    }
    return response
}
