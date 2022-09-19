// swift-tools-version:5.6
import PackageDescription

let package = Package(
    name: "KotlinSpeechFeatures",
    platforms: [
        .iOS(.v14.1)
    ],
    products: [
        .library(
            name: "KotlinSpeechFeatures",
            targets: ["KotlinSpeechFeatures"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "KotlinSpeechFeatures",
            path: "./KotlinSpeechFeatures.xcframework"
        ),
    ]
)
