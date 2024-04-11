package com.example.globusnilomar

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private val DURACION: Long = 4850

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val logo = findViewById<ImageView>(R.id.gifLogo)
        Glide.with(this).load(R.drawable.dispositivos).into(logo)
        canviarActivity()

        mediaPlayer = MediaPlayer.create(this, R.raw.sonido)
        mediaPlayer?.start()

        // Detener la reproducción después de 3 segundos
        Handler().postDelayed({
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }, DURACION)
    }

    private fun canviarActivity() {
        Handler().postDelayed({
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish() // Terminar MainActivity después de iniciar JocActivity
        }, DURACION)
    }
}