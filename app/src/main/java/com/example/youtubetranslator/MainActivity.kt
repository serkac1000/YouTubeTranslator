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
import java.net.URLDecoder

class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var translator: Translator

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
        translator.downloadModelIfNeeded()

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

    private fun playVideo(link: String, subtitlesView: TextView) {
        // Simplified video URL extraction (not full-proof, for demo purposes)
        val videoId = extractVideoId(link)
        val streamUrl = "https://www.youtube.com/watch?v=$videoId" // Placeholder; real extraction needs third-party libs

        val mediaItem = MediaItem.fromUri(streamUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        // Simulate subtitle generation (replace with real audio-to-text if needed)
        simulateSubtitles(subtitlesView)
    }

    private fun extractVideoId(url: String): String {
        val pattern = "(?<=v=|/)([a-zA-Z0-9_-]{11})".toRegex()
        return pattern.find(url)?.value ?: ""
    }

    private fun simulateSubtitles(subtitlesView: TextView) {
        // For demo, use hardcoded English text; in practice, integrate speech-to-text
        val sampleText = "Hello, this is a test video"
        translator.translate(sampleText)
            .addOnSuccessListener { translatedText ->
                subtitlesView.text = translatedText
            }
            .addOnFailureListener { subtitlesView.text = "Translation failed" }

        // Update subtitles every 5 seconds (simulated)
        subtitlesView.postDelayed({ simulateSubtitles(subtitlesView) }, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        translator.close()
    }
}