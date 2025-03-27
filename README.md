# YouTube Translator

This project contains two components:
1. An Android app that plays YouTube videos and translates English speech to Russian subtitles
2. A web-based simulator demonstrating the functionality of the Android app

## Android App

The Android application is designed to:
- Extract video content from YouTube URLs
- Play videos using ExoPlayer
- Detect English speech using ML Kit
- Translate detected speech to Russian in real-time
- Display translated subtitles overlaid on the video

### Technologies Used (Android)
- Kotlin
- ExoPlayer for video playback
- ML Kit for translation
- Android Speech Recognition APIs

### Requirements
- Android Studio 4.2+
- Android SDK 21+
- Internet connection for YouTube video access and translation services

## Web Simulator

A web-based simulator demonstrating the functionality of the YouTube Translator Android app. This application shows how the YouTube Translator would work, displaying videos from YouTube and providing simulated real-time English to Russian subtitle translations.

### Features (Web Simulator)

- YouTube video player integration
- URL input for YouTube videos
- Simulated real-time English to Russian translation of speech
- Android-like UI/UX

### Technologies Used (Web Simulator)

- HTML/CSS/JavaScript
- Node.js and Express for serving the application

### How to Use the Web Simulator

1. Enter a YouTube URL in the input field
2. Click "Play Video" to load the YouTube video
3. Click "Generate Translation" to see simulated English to Russian translations

## Development Plan

Future development plan includes:
1. Improving video stream extraction for better integration with YouTube
2. Implementing actual speech-to-text capabilities with improved accuracy
3. Adding additional translation language options beyond Russian
4. Enhancing UI controls for playback (speed, rewind, etc.)
5. Supporting videos without built-in subtitles
6. Offline translation capabilities

## License

ISC