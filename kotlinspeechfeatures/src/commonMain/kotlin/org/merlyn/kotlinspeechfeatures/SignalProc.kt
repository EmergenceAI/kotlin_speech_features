package org.merlyn.kotlinspeechfeatures

import kotlinx.coroutines.runBlocking
import org.merlyn.kotlinspeechfeatures.fft.FFT
import org.merlyn.kotlinspeechfeatures.fft.KotlinFFT
import org.merlyn.kotlinspeechfeatures.internal.asyncForEachIndexed
import org.merlyn.kotlinspeechfeatures.internal.tile
import org.merlyn.kotlinspeechfeatures.internal.transpose
import kotlin.math.*

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
    fun framesig(
        signal: FloatArray,
        frameLen: Int,
        frameStep: Int,
        winFunc: FloatArray? = null,
        strideTrick: Boolean = true
    ): Array<FloatArray> {
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
     * Does overlap-add procedure to undo the action of framesig.
     * @param frames the array of frames.
     * @param sigLen the length of the desired signal, use 0 if unknown. Output will be truncated to siglen samples.
     * @param frameLen length of each frame measured in samples.
     * @param frameStep number of samples after the start of the previous frame that the next frame should begin.
     * @param winFunc the analysis window to apply to each frame. By default no window is applied.
     * @return a 1-D signal.
     */
    fun deframesig(
        frames: Array<FloatArray>,
        sigLen: Int, frameLen: Int,
        frameStep: Int,
        winFunc: (Int) -> IntArray = { Array(it){ 1 }.toIntArray() }
    ): FloatArray {
        val numFrames = frames.size
        if (frames[0].size != frameLen) {
            throw IllegalArgumentException("frames matrix is wrong size, 2nd dim is not equal to frameLen")
        }
        val arrange= (0..frameLen).map { it.toFloat() }.toFloatArray()
        val arrange2 = (0..numFrames).map { it * frameStep.toFloat() }.toFloatArray()
        val indices = (tile(arrange, numFrames, 1) + transpose(tile(arrange2, frameLen, 1))).map {
            it.map { it.roundToInt() }.toIntArray()
        }
        val padLen = (numFrames - 1) * frameStep + frameLen
        val sigLen2 = if (sigLen <= 0) {
            padLen
        }
        else {
            sigLen
        }
        val windowCorrection = (0..padLen).map { 0f }.toMutableList()
        val recSignal = (0..padLen).map { 0f }.toMutableList()
        val win = winFunc(frameLen)
        (0..numFrames).forEach { i ->
            val updatedWinCorrection = indices[i].map { windowCorrection[it] } + win.map { it + 1f.pow(-15) }
            val updatedRecSignal = indices[i].map { recSignal[it] } + frames[i].toTypedArray()
            indices[i].forEachIndexed { index, value ->
                windowCorrection[value] = updatedWinCorrection[index]
                recSignal[value] = updatedRecSignal[index]
            }
        }

        return recSignal.mapIndexed { index, value ->
            value / windowCorrection[index]
        }.subList(0, sigLen2).toFloatArray()
    }

    /**
     * Compute the magnitude spectrum of each frame in frames. If frames is an NxD matrix, output will be Nx(NFFT/2+1).
     * @param frames the array of frames. Each row is a frame.
     * @param nfft the FFT length to use. If NFFT > frame_len, the frames are zero-padded.
     * @return If frames is an NxD matrix, output will be Nx(NFFT/2+1). Each row will be the magnitude spectrum of the corresponding frame.
     */
    fun magspec(frames : Array<FloatArray>, nfft : Int): Array<FloatArray> {
        val frameWidth = frames[0].size
        val mspec = Array(frames.size) { FloatArray(frameWidth) }
        runBlocking {
            frames.asyncForEachIndexed { index, frame ->
                val absOutput = ArrayList<Float>(frameWidth+2)
                val input = frame + FloatArray(nfft-frameWidth) // pad tail with zeros to return accurate dimensions (dims: 512,)
                val result = fft.rfft(input, nfft).toList()
                absOutput.addAll(result.subList(0, result.size/2-1).map {
                    modul(it.re().toFloat(), it.im().toFloat())
                })
                mspec[index] = absOutput.toFloatArray()
            }
        }
        return mspec
    }

    /**
     * Compute the power spectrum of each frame in frames. If frames is an NxD matrix, output will be Nx(NFFT/2+1).
     * @param frames the array of frames. Each row is a frame.
     * @param nfft the FFT length to use. If NFFT > frame_len, the frames are zero-padded.
     * @return If frames is an NxD matrix, output will be Nx(NFFT/2+1). Each row will be the power spectrum of the corresponding frame.
     */
    fun powspec(frames: Array<FloatArray>, nfft: Int): Array<FloatArray> {
        val fftOut = nfft / 2 + 1
        val mspec = magspec(frames, nfft)
        val pspec = Array(mspec.size) { FloatArray(fftOut)}

        // Compute the power spectrum
        runBlocking {
            frames.asyncForEachIndexed { frameIndex, _ ->
                for ((index, element) in mspec[frameIndex].withIndex()){
                    pspec[frameIndex][index] = (1.0f/nfft * element.toDouble().pow(2.0)).toFloat()
                }
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

    /**
     * Compute the log power spectrum of each frame in frames. If frames is an NxD matrix, output
     * will be Nx(NFFT/2+1).
     * @param frames the array of frames. Each row is a frame.
     * @param nfft the FFT length to use. If NFFT > frame_len, the frames are zero-padded.
     * @param norm If norm=true, the log power spectrum is normalised so that the max value (across
     * all frames) is 0.
     * @return If frames is an NxD matrix, output will be Nx(NFFT/2+1). Each row will be the log
     * power spectrum of the corresponding frame.
     */
    fun logpowspec(
        frames: Array<FloatArray>,
        nfft: Int,
        norm: Boolean = true
    ): Array<FloatArray> {
        val ps = powspec(frames, nfft)
        val lps = ps.map { arr ->
            return@map arr.map x@ {
                val x = if (it <= 10f.pow(-30)) 10f.pow(-30) else it
                return@x 10 * log10(x)
            }.toFloatArray()
        }.toTypedArray()
        return if (norm) {
            val max = lps.maxBy {
                it.max()
            }.max()
            lps.map { arr ->
                arr.map {
                    it - max
                }.toFloatArray()
            }.toTypedArray()
        } else {
            lps
        }
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
