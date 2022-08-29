Pod::Spec.new do |spec|
    spec.name                     = 'lib'
    spec.version                  = '1.0'
    spec.homepage                 = 'https://github.com/XioResearchInterGalactic/kotlin_speech_features'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'This library provides common speech features for ASR including MFCCs and filterbank energies.'
    spec.vendored_frameworks      = 'build/cocoapods/framework/KotlinSpeechFeatures.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '14.1'
                
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':lib',
        'PRODUCT_MODULE_NAME' => 'KotlinSpeechFeatures',
    }
                
    spec.script_phases = [
        {
            :name => 'Build lib',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$COCOAPODS_SKIP_KOTLIN_BUILD" ]; then
                  echo "Skipping Gradle build task invocation due to COCOAPODS_SKIP_KOTLIN_BUILD environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end