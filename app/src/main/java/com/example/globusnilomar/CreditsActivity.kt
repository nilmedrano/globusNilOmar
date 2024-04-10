package com.example.globusnilomar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.Timer
import java.util.TimerTask

class CreditsActivity : AppCompatActivity() {
    var timer= Timer()
    lateinit var menuBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)
        timer.scheduleAtFixedRate(TimeTask(),0L,3000L)
        menuBtn = findViewById<Button>(R.id.buttonCredits)
        menuBtn.setOnClickListener(){
            val intent = Intent (this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
    private inner class TimeTask: TimerTask(){
        private var numeroFragment:Int=1;
        override fun run() {
            numeroFragment++
            if (numeroFragment>2) numeroFragment=1
            if (numeroFragment == 1) {
                if (!supportFragmentManager.isStateSaved) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentcontainer, InstitutCastelletFragment())
                        .addToBackStack("replacement")
                        .commit()
                }
            } else {
                if (!supportFragmentManager.isStateSaved) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentcontainer, OmarINilFragment())
                        .addToBackStack("replacement")
                        .commit()
                }
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }



}