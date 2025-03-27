package com.example.youtubetranslator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import android.widget.EditText
import android.widget.TextView
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.Translator
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var translator: Translator
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build()
        val playerView = findViewById<PlayerView>(R.id.videoPlayer)
        playerView.player = player

        // Initialize ML Kit Translator (English to Russian)
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.RUSSIAN)
            .build()
        translator = com.google.mlkit.nl.translate.Translation.getClient(options)
        
        // Download translation model if needed
        downloadTranslationModel()

        // Handle YouTube link input
        val youtubeLinkInput = findViewById<EditText>(R.id.youtubeLinkInput)
        val subtitlesView = findViewById<TextView>(R.id.subtitlesView)

        youtubeLinkInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val link = s.toString()
                if (link.contains("youtube.com") || link.contains("youtu.be")) {
                    playVideo(link, subtitlesView)
                }
            }
        })
    }

    private fun downloadTranslationModel() {
        // Download the translation model if it's not already downloaded
        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                Log.d(TAG, "Translation model downloaded successfully")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error downloading translation model: ${exception.message}")
            }
    }

    private fun playVideo(link: String, subtitlesView: TextView) {
        // Extract video ID from the YouTube link
        val videoId = extractVideoId(link)
        if (videoId.isEmpty()) {
            subtitlesView.text = "Invalid YouTube URL"
            return
        }
        
        // For demonstration purposes - use a direct stream URL
        // Note: In a production app, you would need a more sophisticated method
        // to extract the actual streaming URL from YouTube
        val streamUrl = "https://www.youtube.com/watch?v=$videoId"
        
        val mediaItem = MediaItem.fromUri(streamUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        // Start simulating subtitles
        simulateSubtitles(subtitlesView)
    }

    private fun extractVideoId(url: String): String {
        var videoId = ""
        
        // Pattern for standard YouTube URLs (youtube.com/watch?v=VIDEO_ID)
        val pattern1 = Pattern.compile("v=([a-zA-Z0-9_-]{11})")
        val matcher1 = pattern1.matcher(url)
        if (matcher1.find()) {
            videoId = matcher1.group(1)
        }
        
        // Pattern for shortened YouTube URLs (youtu.be/VIDEO_ID)
        if (videoId.isEmpty()) {
            val pattern2 = Pattern.compile("youtu\\.be/([a-zA-Z0-9_-]{11})")
            val matcher2 = pattern2.matcher(url)
            if (matcher2.find()) {
                videoId = matcher2.group(1)
            }
        }
        
        return videoId
    }

    private fun simulateSubtitles(subtitlesView: TextView) {
        // This is a simplified simulation for demo purposes
        // In a real app, you would use speech recognition to capture audio
        // and then translate that text
        
        // Sample English phrases that would normally come from speech recognition
        val englishPhrases = listOf(
            "Hello, welcome to this video",
            "Today we're going to learn something new",
            "This is a demonstration of real-time translation",
            "Thank you for watching this video",
            "Please subscribe for more content"
        )
        
        // Select a random phrase
        val randomIndex = (0 until englishPhrases.size).random()
        val englishText = englishPhrases[randomIndex]
        
        // Translate the English text to Russian
        translator.translate(englishText)
            .addOnSuccessListener { translatedText ->
                // Display both English and Russian text
                subtitlesView.text = "$englishText\n$translatedText"
            }
            .addOnFailureListener { exception ->
                subtitlesView.text = "Translation failed: ${exception.message}"
                Log.e(TAG, "Translation error: ${exception.message}")
            }

        // Update subtitles every 5 seconds
        subtitlesView.postDelayed({ simulateSubtitles(subtitlesView) }, 5000)
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onResume() {
        super.onResume()
        if (player.playWhenReady) {
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        translator.close()
    }
}
