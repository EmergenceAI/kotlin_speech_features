package org.merlyn.kotlinspeechfeatures.fft

interface FFT {
    /**
     * Compute the one-dimensional discrete Fourier Transform for real input.
     * This function computes the one-dimensional n-point discrete Fourier Transform (DFT) of a
     * real-valued array by means of an efficient algorithm called the Fast Fourier Transform (FFT).
     * @param signal Input array
     * @param nfft Number of points along transformation axis in the input to use. If n is smaller
     * than the length of the input, the input is cropped. If it is larger, the input is padded
     * with zeros. If n is not given, the length of the input along the axis specified by axis is
     * used.
     * @return The truncated or zero-padded input, transformed along the axis indicated by axis, or
     * the last one if axis is not specified. If n is even, the length of the transformed axis is
     * (n/2)+1. If n is odd, the length is (n+1)/2.
     */
    fun rfft(signal: FloatArray, nfft: Int): Array<Complex>
}