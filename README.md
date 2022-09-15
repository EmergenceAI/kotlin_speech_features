<div align='center'>

<h1 align="center">
Kotlin Speech Features
</h1>


<img src="https://img.shields.io/badge/version-0.1.0-FDD835?style=for-the-badge"></img>	&nbsp; <img src="https://img.shields.io/badge/Kotlin-1.7.10-blue?style=for-the-badge&logo=kotlin"></img> &nbsp; <img src="https://img.shields.io/badge/license-MIT-chlorine?style=for-the-badge"></img> &nbsp; <img src="https://img.shields.io/jitpack/version/com.github.MerlynMind/kotlin_speech_features?style=for-the-badge"></img>

<!-- <img style="background-color: white;" src="https://assets.website-files.com/627028e6193b2d840a066eab/627028e6193b2d86cd066ee0_MM%20Logo.svg" loading="lazy" > -->
</center>

---

<h3 align="left">Quick Links</h3>


<a href="https://merlyn.org"><img src="https://img.shields.io/badge/home-ff7300?style=for-the-badge"></a>	&nbsp;
<a href="https://merlynmind.github.io/kotlin_speech_features/"><img src="https://img.shields.io/badge/Docs-2196F3?style=for-the-badge"></a>

</div>



# 📒 Introduction
<p align="center">
This library is a complete port of <a href="https://github.com/jameslyons/python_speech_features"> python_speech_features</a> in pure Kotlin. </p>
<p align="center">
It provides common speech features for Automated speech recognition (ASR) including MFCCs and filterbank energies.
<br>To know more about MFCCs <a href="http://www.practicalcryptography.com/miscellaneous/machine-learning/guide-mel-frequency-cepstral-coefficients-mfccs/">read more</a>.

### Features

- Mel Frequency Cepstral Coefficients (mfcc) - [Docs](https://merlynmind.github.io/kotlin_speech_features/-kotlin%20-speech%20-features/org.merlyn.kotlinspeechfeatures/-speech-features/mfcc.html)
- Filterbank Energies (fbank) - [Docs](https://merlynmind.github.io/kotlin_speech_features/-kotlin%20-speech%20-features/org.merlyn.kotlinspeechfeatures/-speech-features/fbank.html)
- Log Filterbank Energies (logfbank) - [Docs](https://merlynmind.github.io/kotlin_speech_features/-kotlin%20-speech%20-features/org.merlyn.kotlinspeechfeatures/-speech-features/logfbank.html)
- Spectral Subband Centroids (ssc) - [Docs](https://merlynmind.github.io/kotlin_speech_features/-kotlin%20-speech%20-features/org.merlyn.kotlinspeechfeatures/-speech-features/ssc.html)

</p>


# 🙋 How to use

We support multiple platforms using Kotlin multiplatform.

<details>
<summary> Android </summary>

## Integration
Add jitpack.io to your project's repositories:

```gradle
allProjects {
  repositories {
    google()
    maven { url 'https://jitpack.io' }
  }
}
```

Add artifact to your project:

```gradle
dependencies {
    implementation "com.github.MerlynMind:kotlin_speech_features:${version}"
}
```


## Example implementation

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
4. The result will contain metrices with the expected features. Pass in these features for further processes (e.g. classification, speech recognition).

  ---
</details>

<details>
	<summary> iOS </summary>


## Integration

1. In XCode, go to `File > Add Packages...`
2. Paste in the URL of this repo in the search box
3. Select the package found
4. Click `Add Package` button


## Example implementation

A sample app is included in this repo to help understand the implementation.

1. Convert your audio signal in the form of an `KotlinIntArray` and normalize it.
   ```swift
   import KotlinSpeechFeatures

   let signal = [Int](1...1000) // Example signal
   let normalized = MathUtils.Companion.init().normalize(sig: toKotlinIntArray(arr: signal))

   func toKotlinIntArray(arr: [Int]) -> KotlinIntArray {
       let result = KotlinIntArray(size: Int32(arr.capacity))
       for i in 0...(arr.count-1) {
           result.set(index: Int32(i), value: Int32(arr[i]))
       }
       return result
   }
   ```
2. Initialize speech features
   ```swift
   let speechFeatures = SpeechFeatures()
   ```
3. Perform any of the 4 operations:
   ```swift
   let result = speechFeatures.mfcc(signal: normalized, sampleRate: 16000, winLen: 0.025, winStep: 0.01, numCep: 13, nFilt: 64, nfft: 512, lowFreq: 0, highFreq: ni;, preemph: 0.97, ceplifter: 22, appendEnergy: true, winFunc: nil)
   let result = speechFeatures.fbank(signal: normalized, sampleRate: 16000, winLen: 0.025, winStep: 0.01, nFilt: 64, nfft: 512, lowFreq: 0, highFreq: nil, preemph: 0.97, winFunc: nil)
   let result = speechFeatures.logfbank(signal: normalized, sampleRate: 16000, winLen: 0.025, winStep: 0.01, nFilt: 64, nfft: 512, lowFreq: 0, highFreq: nil, preemph: 0.97, winFunc: nil)
   let result = speechFeatures.ssc(signal: normalized, sampleRate: 16000, winLen: 0.025, winStep: 0.01, nFilt: 64, nfft: 512, lowFreq: 0, highFreq: nil, preemph: 0.97, winFunc: nil)
   ```
4. The result will contain metrices with the expected features. Pass in these features for further processes (e.g. classification, speech recognition).

</details>

<details>
	<summary> JavaScript </summary>

  ```
  Coming soon...
  ```

</details>




# ✍️ Contributing
Interested in contributing to the library? Thank you so much for your interest!
We are always looking for improvements to the project and contributions from open-source developers are greatly appreciated.

1. Clone repo and create a new branch:
```
git checkout https://github.com/merlynmind/kotlin_speech_features -b name_for_new_branch
```
2. Make changes and test
3. Submit Pull Request with comprehensive description of changes

# 🌟 Spread the word!
If you want to say thank you and/or support active development of this library:

- Add a GitHub Star to the project!
- Tweet about the project on your Twitter!
Tag @MerlynMind and/or #heyMerlnyn

Thank you so much for your interest in growing the reach of our library!


# 🧡 Credits
- [Arjun Sunil](https://github.com/arjun921) - Original Author of kotlin speech features
- [Raquib-Ul Alam](https://github.com/alamkanak) - For major refactoring and making the code presentable
- [Rob Smith](https://github.com/robmsmt) - For Mentoring and helping us to navigate through the task

# 📝 Reference

- Original library - Python Speech Features: https://github.com/jameslyons/python_speech_features
- Reference Library - Used as a reference for building parts of Kotlin Speech Features: https://github.com/Cwiiis/c_speech_features
- Sample english.wav was obtained from
	```
	wget http://voyager.jpl.nasa.gov/spacecraft/audio/english.au
	sox english.au -e signed-integer english.wav
	```
