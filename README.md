# Kotlin Speech Features

This library provides common speech features for Automated speech recognition (ASR) including MFCCs and filterbank energies. If you are not sure what MFCCs are, and would like to know more have a look at this MFCC tutorial

This library is a complete port of [python_speech_features](https://github.com/jameslyons/python_speech_features) in pure Kotlin. It can be used both in Android/java projects and iOS projects.

## Installation

### Kotlin/Android/JVM
Add jitpack.io to your project's repositories:

```gradle
allProjects {
  repositories {
    google() 
    maven { url "https://www.jitpack.io" }
  }
}
```

Add artifact to your project:

```gradle
dependencies {
    implementation "org.merlyn.kotlinspeechfeatures:${version}"
}
```

## Usage
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