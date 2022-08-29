package org.merlyn.kotlinspeechfeatures.fft

interface FFT {
    fun rfft(signal: FloatArray, nfft: Int): Array<Complex>
}