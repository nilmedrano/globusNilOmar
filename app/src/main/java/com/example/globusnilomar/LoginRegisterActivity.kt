package com.example.globusnilomar

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.lang.Class as Class1

class LoginRegisterActivity : AppCompatActivity() {
    /**override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
    }*/

    val DURACIO: Long = 500;
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        var BTMLOGIN = findViewById<Button>(R.id.BTMLOGIN);
        var BTMREGISTRO = findViewById<Button>(R.id.BTMREGISTRO);
        BTMLOGIN.setOnClickListener() {
            canviarActivity(LoginActivity::class.java)
        }
        BTMREGISTRO.setOnClickListener() {
            canviarActivity(RegisterActivity::class.java)
        }
    }
    private fun canviarActivity(activityClass: Class1<out Activity>) {
        Handler().postDelayed({
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }, DURACIO)
    }

    // Aquest mètode s'executarà quan s'obri el menu
    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }
    private fun usuariLogejat() {
        if (user !=null)
        {
            val intent= Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}


