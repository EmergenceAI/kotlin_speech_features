# Module lib

This library provides common speech features for ASR including MFCCs and filterbank energies. If you are not sure what MFCCs are, and would like to know more have a look at this MFCC tutorial: http://www.practicalcryptography.com/miscellaneous/machine-learning/guide-mel-frequency-cepstral-coefficients-mfccs/.

Supported features:

- Mel Frequency Cepstral Coefficients (mfcc)
- Filterbank Energies (fbank)
- Log Filterbank Energies (logfbank)
- Spectral Subband Centroids (ssc)

Example implementation:

A sample app is included in this repo to help understand the implementation.

1. Convert your audio signal in the form of a float array. (A demo provided in the sample app)
2. Initialize speech features
   ```kotlin
   private val speechFeatures = SpeechFeatures()
   ```
3. Perform any of the 4 operations:
   ```kotlin
   val result = speechFeatures.mfcc(MathUtils.normalize(wav), nFilt = 64)
   val result = speechFeatures.fbank(MathUtils.normalize(wav), nFilt = 64)
   val result = speechFeatures.logfbank(MathUtils.normalize(wav), nFilt = 64)
   val result = speechFeatures.ssc(MathUtils.normalize(wav), nFilt = 64)
   ```
4. The result will contain a 2 dimensional matrix with the expected values.

## Reference

- Original library - Python Speech Features: https://github.com/jameslyons/python_speech_features
- Reference Library - Used as a reference for building parts of Kotlin Speech Features: https://github.com/Cwiiis/c_speech_features
- Sample english.wav was obtained from
  ```
  wget http://voyager.jpl.nasa.gov/spacecraft/audio/english.au
  sox english.au -e signed-integer english.wav
  ```
