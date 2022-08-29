package org.merlyn.kotlinspeechfeatures

import org.merlyn.kotlinspeechfeatures.fft.FFT
import org.merlyn.kotlinspeechfeatures.fft.KotlinFFT
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

class SignalProc(private val fft: FFT = KotlinFFT()) {

    /**
     * Frame a signal into overlapping frames.
     * @param signal the audio signal to frame.
     * @param frameLen length of each frame measured in samples.
     * @param frameStep number of samples after the start of the previous frame that the next frame should begin.
     * @param winFunc the analysis window to apply to each frame. By default no window is applied.
     * @param strideTrick use stride trick to compute the rolling window and window multiplication faster
     * @return an array of frames. Size is NUMFRAMES by frame_len.
     */
    fun framesig(signal: FloatArray, frameLen: Int, frameStep: Int, winFunc: FloatArray? = null, strideTrick: Boolean = true): Array<FloatArray> {
        val nFrames = if (signal.size > frameLen) {
            1 + (ceil((signal.size - frameLen) / frameStep.toFloat())).toInt()
        } else {
            1
        }

        val indices = IntArray(nFrames * frameLen)
        run {
            var i = 0
            var idx = 0
            while (i < nFrames) {
                val base: Int = i * frameStep
                run {
                    var j = 0
                    while (j < frameLen) {
                        indices[idx] = base + j
                        j++
                        idx++
                    }
                }
                i++
            }
        }

        val frames = FloatArray(nFrames * frameLen)
        run {
            var i = 0
            var idx = 0
            var iidx = 0
            while (i < nFrames) {
                run {
                    var j = 0
                    while (j < frameLen) {
                        val index = indices[iidx]
                        frames[idx] = (if (compareUnsigned(
                                index,
                                signal.size
                            ) < 0
                        ) signal[index] else 0.0).toFloat()
                        if (winFunc != null) {
                            frames[idx] = frames[idx] * winFunc[j]
                        }
                        j++
                        idx++
                        iidx++
                    }
                }
                run {
                    var j: Int = frameLen
                    while (j < frameLen) {
                        frames[idx] = 0.0f
                        j++
                        idx++
                    }
                }
                i++
            }
        }

        // reshape frames to return array of arrays
        val response = Array(nFrames) { FloatArray(frameLen) }
        var frameStart = 0
        var frameEnd = frameLen - 1
        for (frameIndex in 0 until nFrames) {
            val frameSlice = frames.slice(frameStart..frameEnd)
            response[frameIndex] = frameSlice.toFloatArray()
            frameStart += frameLen
            frameEnd += frameLen
        }
        return response
    }

    /**
     * Compute the magnitude spectrum of each frame in frames. If frames is an NxD matrix, output will be Nx(NFFT/2+1).
     * @param frames the array of frames. Each row is a frame.
     * @param nfft the FFT length to use. If NFFT > frame_len, the frames are zero-padded.
     * @return If frames is an NxD matrix, output will be Nx(NFFT/2+1). Each row will be the magnitude spectrum of the corresponding frame.
     */
    suspend fun magspec(frames : Array<FloatArray>, nfft : Int): Array<FloatArray> {
        val frameWidth = frames[0].size
        val mspec = Array(frames.size) { FloatArray(frameWidth) }
        frames.asyncForEachIndexed { index, frame ->
            val absOutput = ArrayList<Float>(frameWidth+2)
            val input = frame + FloatArray(nfft-frameWidth) // pad tail with zeros to return accurate dimensions (dims: 512,)
            val result = fft.rfft(input, nfft).toList()
            absOutput.addAll(result.subList(0, result.size/2-1).map {
                modul(it.re().toFloat(), it.im().toFloat())
            })
            mspec[index] = absOutput.toFloatArray()
        }
        return mspec
    }

    /**
     * Compute the power spectrum of each frame in frames. If frames is an NxD matrix, output will be Nx(NFFT/2+1).
     * @param frames the array of frames. Each row is a frame.
     * @param nfft the FFT length to use. If NFFT > frame_len, the frames are zero-padded.
     * @return If frames is an NxD matrix, output will be Nx(NFFT/2+1). Each row will be the power spectrum of the corresponding frame.
     */
    suspend fun powspec(frames: Array<FloatArray>, nfft: Int): Array<FloatArray> {
        val fftOut = nfft / 2 + 1
        val mspec = magspec(frames, nfft)
        val pspec = Array(mspec.size) { FloatArray(fftOut)}

        // Compute the power spectrum
        frames.asyncForEachIndexed { frameIndex, _ ->
            for ((index, element) in mspec[frameIndex].withIndex()){
                pspec[frameIndex][index] = (1.0f/nfft * element.toDouble().pow(2.0)).toFloat()
            }
        }
        return pspec
    }

    /**
     * Perform preemphasis on the input signal.
     * @param signal The signal to filter.
     * @param coeff The preemphasis coefficient. 0 is no filter, default is 0.95.
     * @return the filtered signal.
     */
    fun preemphasis(signal: FloatArray, coeff: Float=0.95f): FloatArray {
        val preemph = FloatArray(signal.size)
        val loopEnd = signal.size-1
        for (i in 1..loopEnd) {
            preemph[i] = signal[i] - signal[i-1] * coeff
        }
        preemph[0] = signal[0]
        return preemph
    }

    fun deframesig() {
        // TODO
    }

    fun logpowspec() {
        // TODO
    }


    private fun modul(r: Float, i: Float): Float {
        return if (r != 0.0f || i != 0.0f) {
            sqrt(r * r + i * i)
        }
        else {
            0.0f
        }
    }

    private fun compare(x: Int, y: Int): Int {
        return if (x < y) -1 else if (x == y) 0 else 1
    }

    private fun compareUnsigned(x: Int, y: Int): Int {
        return compare(x + Int.MIN_VALUE, y + Int.MIN_VALUE)
    }
}