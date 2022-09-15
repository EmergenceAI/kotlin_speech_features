package org.merlyn.kotlinspeechfeatures.fft

import com.paramsen.noise.Noise

class NoiseFFT : FFT {

    override fun rfft(signal: FloatArray, nfft: Int): Array<Complex> {
        val real = Noise.real(nfft)
        val output = FloatArray(signal.size + 2)
        real.fft(signal, output)
        val result = ArrayList<Complex>(output.size/2)
        for (i in 0 until output.size / 2) {
            result[i] = Complex(output[i * 2].toDouble(), output[i * 2 + 1].toDouble())
        }
        return result.toTypedArray()
    }

}
