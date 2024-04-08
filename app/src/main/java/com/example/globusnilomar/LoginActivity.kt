package com.example.globusnilomar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//Despleguem les variables que farem servir
lateinit var correoLogin : EditText
lateinit var passLogin : EditText
lateinit var BtnLogin : Button
lateinit var auth2 : FirebaseAuth //FIREBASE AUTENTIFICACIO
lateinit var btnContra : Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        //btnContra = findViewById<Button>(R.id.BtnCanviContrasenya)

        // Busquem a R els elements als que apunten les variables
        correoLogin =findViewById<EditText>(R.id.correoEt2)
        passLogin =findViewById<EditText>(R.id.passEt2)
        BtnLogin =findViewById<Button>(R.id.BtnLogin)
        //Instanciem el firebaseAuth
        auth2 = FirebaseAuth.getInstance()
        BtnLogin.setOnClickListener(){
            //Abans de fer el registre validem les dades
            var email:String = correoLogin.getText().toString()
            var passw:String = passLogin.getText().toString()
            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                correoLogin.setError("Invalid Mail")
            }
            else if (passw.length<6) {
                passLogin.setError("Password less than 6 chars")
            }
            else
            {
                LogindeJugador(email, passw)
            }
        }
        btnContra.setOnClickListener(){
            changePaswd()
        }
    }

    private fun changePaswd() {
        Toast.makeText(this, "HOLA",
            Toast.LENGTH_LONG).show()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val email = user.email
            email?.let {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val dialog = AlertDialog.Builder(this)
                                .setTitle("CHANGE PASSWORD")
                                .setMessage("You have been sent an email to change your password.")
                                .setNegativeButton("DONE") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .setCancelable(false)
                                .create()
                            dialog.show()
                        } else {
                            Toast.makeText(this, "Error sending the email to reset the password.",
                                Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    private fun LogindeJugador(email: String, passw: String) {
        auth2.signInWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this)
            { task ->
                if (task.isSuccessful) {
                    val tx: String = "Benvingut "+ email
                    Toast.makeText(this, tx, Toast.LENGTH_LONG).show()
                    val user = auth2.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(this, "ERROR Autentificació",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    fun updateUI(user: FirebaseUser?)
    {
        val intent= Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

}
