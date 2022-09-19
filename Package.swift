// swift-tools-version:5.6
import PackageDescription

let package = Package(
    name: "KotlinSpeechFeatures",
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
