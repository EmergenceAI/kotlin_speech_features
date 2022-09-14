Pod::Spec.new do |spec|
    spec.name                     = 'lib'
    spec.version                  = '0.1.0'
    spec.homepage                 = 'https://github.com/MerlynMind/kotlin_speech_features'
    spec.source                   = { :http=> ''}
    spec.authors                  = 'Raquib-ul Alam, Arjun Sunil, Rob Smith'
    spec.license                  = 'MIT License'
    spec.summary                  = 'This library provides common speech features for ASR including MFCCs and filterbank energies.'
    spec.vendored_frameworks      = 'KotlinSpeechFeatures.xcframework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '14.1'




end
