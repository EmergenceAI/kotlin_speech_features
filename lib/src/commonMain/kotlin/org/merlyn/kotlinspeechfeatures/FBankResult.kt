package org.merlyn.kotlinspeechfeatures

data class FBankResult(
    val features: Array<FloatArray>,
    val energy: FloatArray
)
