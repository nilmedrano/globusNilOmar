package com.example.globusnilomar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.globusnilomar.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    val DURACIO: Long = 3000;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val logo = findViewById<ImageView>(R.id.gifLogo)
        Glide.with(this).load(R.drawable.dispositivos).into(logo)
        canviarActivity()
    }

    private fun canviarActivity(){
        Handler().postDelayed(Runnable {
          val intent = Intent(this, LoginActivity::class.java)
          startActivity(intent)
        }, DURACIO)
    }

}
