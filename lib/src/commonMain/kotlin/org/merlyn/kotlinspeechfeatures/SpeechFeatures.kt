package org.merlyn.kotlinspeechfeatures

import kotlinx.coroutines.runBlocking
import org.merlyn.kotlinspeechfeatures.fft.FFT
import org.merlyn.kotlinspeechfeatures.fft.KotlinFFT
import org.merlyn.kotlinspeechfeatures.internal.*
import kotlin.math.*

class SpeechFeatures(private val fft: FFT = KotlinFFT()) {

    private val signalProc = SignalProc(fft)

    /**
     * Compute MFCC features from an audio signal.
     * @param signal the audio signal from which to compute features. Should be an N*1 array
     * @param sampleRate the samplerate of the signal we are working with.
     * @param winLen the length of the analysis window in seconds. Default is 0.025s (25 milliseconds)
     * @param winStep the step between successive windows in seconds. Default is 0.01s (10 milliseconds)
     * @param numCep the number of cepstrum to return, default 13
     * @param nFilt the number of filters in the filterbank, default 26.
     * @param nfft the FFT size. Default is 512.
     * @param lowFreq lowest band edge of mel filters. In Hz, default is 0.
     * @param highFreq highest band edge of mel filters. In Hz, default is samplerate/2
     * @param preemph apply preemphasis filter with preemph as coefficient. 0 is no filter. Default is 0.97.
     * @param ceplifter apply a lifter to final cepstral coefficients. 0 is no lifter. Default is 22.
     * @param appendEnergy if this is true, the zeroth cepstral coefficient is replaced with the log of the total frame energy.
     * @param winFunc the analysis window to apply to each frame. By default no window is applied.
     * @return An array of size (NUMFRAMES by numcep) containing features. Each row holds 1 feature vector.
     */
    fun mfcc(
        signal: FloatArray,
        sampleRate: Int = 16000,
        winLen: Float = 0.025f,
        winStep: Float = 0.01f,
        numCep: Int = 13,
        nFilt: Int = 26,
        nfft: Int? = 512,
        lowFreq: Int = 0,
        highFreq: Int? = null,
        preemph: Float = 0.97f,
        ceplifter: Int = 22,
        appendEnergy: Boolean = true,
        winFunc: FloatArray? = null
    ): Array<FloatArray> {
        val mfccNfft = nfft ?: calculateNfft(sampleRate,winLen)
        val (feat, energy) = fbank(
            signal,
            sampleRate,
            winLen,
            winStep,
            nFilt,
            mfccNfft,
            lowFreq,
            highFreq,
            preemph,
            winFunc
        )
        val logFeat = runBlocking { floatArrayLog(feat) }
        val lifterDctFeat = runBlocking {
            dct2withLifter(
                feat=logFeat,
                aNCep = numCep,
                aNFilters = nFilt,
                ceplifter=ceplifter
            )
        }
        if (appendEnergy){ // replace first cepstral coefficient with log of frame energy
            for ((index, eng) in energy.withIndex()){
                lifterDctFeat[index][0] = ln(eng)
            }
        }
        return lifterDctFeat
    }

    /**
     * Compute Mel-filterbank energy features from an audio signal.
     * @param signal the audio signal from which to compute features. Should be an N*1 array
     * @param sampleRate the samplerate of the signal we are working with.
     * @param winLen the length of the analysis window in seconds. Default is 0.025s (25 milliseconds)
     * @param winStep the step between successive windows in seconds. Default is 0.01s (10 milliseconds)
     * @param nFilt the number of filters in the filterbank, default 26.
     * @param nfft the FFT size. Default is 512.
     * @param lowFreq lowest band edge of mel filters. In Hz, default is 0.
     * @param highFreq highest band edge of mel filters. In Hz, default is samplerate/2
     * @param preemph apply preemphasis filter with preemph as coefficient. 0 is no filter. Default is 0.97.
     * @param winFunc the analysis window to apply to each frame. By default no window is applied.
     * @return 2 values. The first is an array of size (NUMFRAMES by nfilt) containing features. Each row holds 1 feature vector. The second return value is the energy in each frame (total energy, unwindowed)
     */
    fun fbank(
        signal: FloatArray,
        sampleRate: Int=16000,
        winLen: Float=0.025f,
        winStep: Float=0.01f,
        nFilt: Int=26,
        nfft: Int=512,
        lowFreq: Int=0,
        highFreq: Int? = null,
        preemph: Float = 0.97f,
        winFunc: FloatArray? = null
    ): Pair<Array<FloatArray>, FloatArray> {
        val premphSignal = signalProc.preemphasis(signal, preemph)
        val frameLen: Int = (winLen * sampleRate).toInt()
        val frameStep: Int = (winStep * sampleRate).toInt()
        val frames = signalProc.framesig(
            signal = premphSignal,
            frameLen = frameLen,
            frameStep = frameStep,
            winFunc = winFunc
        )
        val (feat, energy) = runBlocking {
            val pspec = signalProc.powspec(frames, nfft)
            val energy = calcEnergy(pspec) // stores the total energy in each frame.
            val fb = getFilterBanks(nFilt, nfft, sampleRate, lowFreq, highFreq)
            val feat = calcFeat(pspec, fb)
            return@runBlocking feat to energy
        }
        return Pair(feat, energy)
    }

    /**
     * Compute log Mel-filterbank energy features from an audio signal.
     * @param signal the audio signal from which to compute features. Should be an N*1 array.
     * @param sampleRate the sampling rate of the signal we are working with.
     * @param winLen the length of the analysis window in seconds. Default is 0.025s (25 milliseconds).
     * @param winStep the step between successive windows in seconds. Default is 0.01s (10 milliseconds).
     * @param nFilt the number of filters in the filterbank, default 26.
     * @param nfft the FFT size. Default is 512.
     * @param lowFreq lowest band edge of mel filters. In Hz, default is 0.
     * @param highFreq highest band edge of mel filters. In Hz, default is samplerate/2.
     * @param preemph apply preemphasis filter with preemph as coefficient. 0 is no filter. Default is 0.97.
     * @param winFunc the analysis window to apply to each frame. By default no window is applied.
     * @return An array of size (NUMFRAMES by nFilt) containing features. Each row holds 1 feature vector.
     */
    fun logfbank(
        signal: FloatArray,
        sampleRate: Int = 16000,
        winLen: Float = 0.025f,
        winStep: Float = 0.01f,
        nFilt: Int = 26,
        nfft: Int = 512,
        lowFreq: Int = 0,
        highFreq: Int? = null,
        preemph: Float = 0.97f,
        winFunc: FloatArray? = null
    ): Array<FloatArray> {
        val (feat, _) = fbank(
            signal,
            sampleRate,
            winLen,
            winStep,
            nFilt,
            nfft,
            lowFreq,
            highFreq,
            preemph,
            winFunc
        )
        return runBlocking { floatArrayLog(feat) }
    }

    /**
     * Convert a value in Hertz to Mels
     * @param hz a value in Hz. This can also be a numpy array, conversion proceeds element-wise.
     * @return a value in Mels. If an array was passed in, an identical sized array is returned.
     */
    fun hz2mel(hz: Double): Double {
        return 2595.0 * log10((1.0+hz/700.0))
    }

    /**
     * Convert a value in Hertz to Mels
     * @param hz an array in Hz. Conversion proceeds element-wise.
     * @return an array in Mels. An identical sized array is returned.
     */
    fun hz2mel(hz: DoubleArray): DoubleArray {
        return hz.map { hz2mel(it) }.toDoubleArray()
    }

    /**
     * Convert a value in Mels to Hertz
     * @param mel – a value in Mels. This can also be a numpy array, conversion proceeds element-wise.
     * @return a value in Hertz.
     */
    fun mel2hz(mel: Double): Double {
        return 700.0*(10.0.pow(((mel/2595.0)))-1)
    }

    /**
     * Convert a value in Mels to Hertz
     * @param mel – a value in Mels. This can also be a numpy array, conversion proceeds element-wise.
     * @return an array in Hertz. An identical sized array is returned.
     */
    fun mel2hz(mel: DoubleArray): DoubleArray {
        return mel.map { mel2hz(it) }.toDoubleArray()
    }

    /**
     * Compute a Mel-filterbank. The filters are stored in the rows, the columns correspond to fft bins. The filters are returned as an array of size nfilt * (nfft/2 + 1)
     * @param nFilt the number of filters in the filterbank, default 20.
     * @param nfft the FFT size. Default is 512.
     * @param sampleRate the samplerate of the signal we are working with. Affects mel spacing.
     * @param lowFreq lowest band edge of mel filters, default 0 Hz
     * @param highFreqIn highest band edge of mel filters, default samplerate/2
     * @return An array of size nfilt * (nfft/2 + 1) containing filterbank. Each row holds 1 filter.
     */
    fun getFilterBanks(
        nFilt: Int = 20,
        nfft: Int = 512,
        sampleRate: Int = 16000,
        lowFreq: Int = 0,
        highFreqIn: Int? = null
    ): Array<FloatArray> {
        val highFreq: Int = highFreqIn ?: (sampleRate / 2)
        val lowMel = hz2mel(lowFreq.toDouble())
        val highMel =  hz2mel(highFreq.toDouble())
        val melPoints =  linspace(lowMel, highMel, (nFilt+2).toDouble())
        val bin = DoubleArray(melPoints.size)
        for ((index, element) in melPoints.withIndex()){
            bin[index] = floor((nfft + 1.0) * mel2hz(element) / sampleRate)
        }
        val fBank: Array<FloatArray> = Array(nFilt) { FloatArray((nfft/2+1)) }
        for (j in 0 until nFilt){
            for (i in bin[j].toInt() until bin[j+1].toInt()){
                fBank[j][i] = ((i - bin[j]) / (bin[j+1]-bin[j])).toFloat()
            }
            for (i in bin[j+1].toInt() until bin[j+2].toInt()){
                fBank[j][i] = ((bin[j+2]-i) / (bin[j+2]-bin[j+1])).toFloat()
            }
        }
        return fBank
    }

    /**
     * Compute Spectral Subband Centroid features from an audio signal.
     * @param signal the audio signal from which to compute features. Should be an N*1 array
     * @param sampleRate the sample rate of the signal we are working with, in Hz.
     * @param winLen the length of the analysis window in seconds. Default is 0.025s (25 milliseconds)
     * @param winStep the step between successive windows in seconds. Default is 0.01s (10 milliseconds)
     * @param nFilt the number of filters in the filterbank, default 26.
     * @param nfft the FFT size. Default is 512.
     * @param lowFreq lowest band edge of mel filters. In Hz, default is 0.
     * @param highFreq highest band edge of mel filters. In Hz, default is samplerate/2
     * @param preemph apply preemphasis filter with preemph as coefficient. 0 is no filter. Default is 0.97.
     * @param winFunc the analysis window to apply to each frame. By default no window is applied. You can use numpy window functions here e.g. winfunc=numpy.hamming
     * @return An array of size (NUMFRAMES by nfilt) containing features. Each row holds 1 feature vector.
     */
    fun ssc(
        signal: FloatArray,
        sampleRate: Int = 16000,
        winLen: Float = 0.025f,
        winStep: Float = 0.01f,
        nFilt: Int = 26,
        nfft: Int = 512,
        lowFreq: Int = 0,
        highFreq: Int? = null,
        preemph: Float = 0.97f,
        winFunc: FloatArray? = null
    ): Array<FloatArray> {
        val highFreq2: Int = highFreq ?: (sampleRate / 2)
        val signal2 = signalProc.preemphasis(signal, preemph)
        val frames = signalProc.framesig(signal2, (winLen * sampleRate).roundToInt(), (winStep * sampleRate).roundToInt(), winFunc)
        val pspec = runBlocking { signalProc.powspec(frames, nfft) }
        val pspec2 = pspec.map { row ->
            row.map { element ->
                if (element == 0.0f) {
                    2.220446049250313E-16f
                }
                else element
            }.toFloatArray()
        }.toTypedArray()
        val fb = getFilterBanks(nFilt, nfft, sampleRate, lowFreq, highFreq2)
        val feat = runBlocking { calcFeat(pspec2, fb) }
        val linSpace = linspace(1.0, sampleRate/2.0, pspec2[0].size.toDouble())
        val tiled = tile(linSpace.map { it.toFloat() }.toFloatArray(), pspec2.size, 1)
        return runBlocking { dot2d(pspec2 * tiled, fb) / feat }.map { it.filter { it != 0f }.toFloatArray() }.toTypedArray()
    }

    /**
     * Apply a cepstral lifter the the matrix of cepstra. This has the effect of increasing the
     * magnitude of the high frequency DCT coeffs.
     * @param cepstra the matrix of mel-cepstra, will be numframes * numcep in size.
     * @param l the liftering coefficient to use. Default is 22. L <= 0 disables lifter.
     */
    fun lifter(cepstra: FloatArray, l: Int = 22): FloatArray {
        return cepstralLifter(cepstra, 1, aNCep = 13, aCepLifter = l)
    }

    /**
     * Compute delta features from a feature vector sequence.
     * @param feat An array of size (NUMFRAMES by number of features) containing features. Each row holds 1 feature vector.
     * @param n For each frame, calculate delta features based on preceding and following N frames
     * @returns An array of size (NUMFRAMES by number of features) containing delta features. Each row holds 1 delta feature vector.
     */
    fun delta(feat: FloatArray, n: Int): Array<FloatArray> {
        if (n < 1) {
            throw IllegalArgumentException("N must be an integer >= 1")
        }
        val numFrames = feat.size
        val denominator = (1..n+1).map { it * it }.sum() * 2
        val deltaFeat = mutableListOf<FloatArray>()
        val padded = Array(n+n+1) {
            feat
        }
        val x = (-n..n+1).map { it.toFloat() }.toFloatArray()
        for (t in 0 until numFrames) {
            val y = runBlocking { dot2d(arrayOf(x), padded.toList().subList(t, t+2*n+1).toTypedArray())[0] / denominator }
            deltaFeat.add(y)
        }
        return deltaFeat.toTypedArray()
    }

    private fun calculateNfft(sampleRate: Int, winLen: Float): Int {
        val windowLengthSamples = winLen * sampleRate
        var nfft = 1
        while (nfft < windowLengthSamples){
            nfft *=2
        }
        return nfft
    }

    private operator fun Array<FloatArray>.times(r: Array<FloatArray>): Array<FloatArray> {
        if (size != r.size && get(0).size != r[0].size) {
            throw IllegalArgumentException("Matrix sizes do not match")
        }
        val result = Array(size) {
            FloatArray(r[0].size)
        }
        for (i in 0 until size) {
            for (j in 0 until get(0).size) {
                result[i][j] = get(i)[j] * r[i][j]
            }
        }
        return result
    }

    private operator fun Array<FloatArray>.div(r: Array<FloatArray>): Array<FloatArray> {
        if (size != r.size && get(0).size != r[0].size) {
            throw IllegalArgumentException("Matrix sizes do not match")
        }
        val result = Array(size) {
            FloatArray(r[0].size)
        }
        for (i in 0 until size) {
            for (j in 0 until get(0).size) {
                result[i][j] = get(i)[j] / r[i][j]
            }
        }
        return result
    }

    private operator fun FloatArray.div(denominator: Int): FloatArray {
        return map {
            it / denominator
        }.toFloatArray()
    }
}

