package com.example.globusnilomar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

lateinit var button1: Button
lateinit var button2: Button
lateinit var button3: Button
lateinit var button4: Button
lateinit var button5: Button
lateinit var button6: Button
lateinit var button7: Button
lateinit var button8: Button
lateinit var button9: Button
lateinit var PVP: Button
lateinit var PVC: Button
lateinit var menuBtn: Button

var puntuacionAumentada = false;
var user: FirebaseUser? = null;
var PVPChoose: Boolean = true

class JocActivity : AppCompatActivity() {

    var partidaEnCurso = false
    var Player1 = ArrayList<Int>()
    var Player2 = ArrayList<Int>()
    var ActivePlayer = 1
    var setPlayer = 1
    var primerMovimientoHecho = false

    // Nombre de las preferencias compartidas
    private val PREFS_NAME = "com.example.globusnilomar"
    private val PREF_PVP_CHOICE = "pvp_choice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joc)

        // Inicializar preferencias compartidas
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        PVPChoose = prefs.getBoolean(PREF_PVP_CHOICE, true)

        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)
        button6 = findViewById(R.id.button6)
        button7 = findViewById(R.id.button7)
        button8 = findViewById(R.id.button8)
        button9 = findViewById(R.id.button9)
        PVP = findViewById(R.id.PVP)
        PVC = findViewById(R.id.PVC)

        if (PVPChoose) {
            setButtonActive(PVP)
            setButtonInactive(PVC)
        } else {
            setButtonActive(PVC)
            setButtonInactive(PVP)
        }

        menuBtn = findViewById<Button>(R.id.buttonJoc)
        menuBtn.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setButtonActive(button: Button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.bluesoft))
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.blueberry))
    }

    private fun setButtonInactive(button: Button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.blueberry))
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.bluesoft))
    }

    fun restartGame(view: View) {
        puntuacionAumentada = false;
        // Habilitar los botones de selección de modo
        PVP.isEnabled = true
        PVC.isEnabled = true

        partidaEnCurso = false
        primerMovimientoHecho = false

        // Guardar la elección del modo de juego en las preferencias compartidas
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(PREF_PVP_CHOICE, PVPChoose)
        editor.apply()

        val intent = intent
        finish()
        startActivity(intent)
    }

    fun buttonClick(view: View) {
        if (!primerMovimientoHecho) {
            // Bloquear los botones de selección de modo
            PVP.isEnabled = false
            PVC.isEnabled = false
            primerMovimientoHecho = true
        }
        val buSelected: Button = view as Button
        var cellId = 0
        when (buSelected.id) {
            R.id.button1 -> cellId = 1
            R.id.button2 -> cellId = 2
            R.id.button3 -> cellId = 3
            R.id.button4 -> cellId = 4
            R.id.button5 -> cellId = 5
            R.id.button6 -> cellId = 6
            R.id.button7 -> cellId = 7
            R.id.button8 -> cellId = 8
            R.id.button9 -> cellId = 9
        }
        PlayGame(cellId, buSelected)
    }

    fun PlayerChoose(view: View) {
        val ps: Button = view as Button
        when (ps.id) {
            R.id.PVP -> {
                setPlayer = 1
                setButtonActive(PVP)
                setButtonInactive(PVC)
                PVPChoose = true
            }
            R.id.PVC -> {
                setPlayer = 2
                setButtonActive(PVC)
                setButtonInactive(PVP)
                PVPChoose = false
            }
        }
    }

    fun PlayGame(cellId: Int, buSelected: Button) {
        if (ActivePlayer == 1) {
            buSelected.text = "X"
            buSelected.setTextColor(ContextCompat.getColor(this, R.color.bluesoft))
            buSelected.setBackgroundColor(ContextCompat.getColor(this, R.color.blueberry))
            Player1.add(cellId)
            ActivePlayer = 2
            if (setPlayer == 1) {
            } else {
                try {
                    AutoPlay()
                } catch (ex: Exception) {
                    Toast.makeText(this, getString(R.string.GameOver) , Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            buSelected.text = "O"
            buSelected.setTextColor(ContextCompat.getColor(this, R.color.blueberry))
            buSelected.setBackgroundColor(ContextCompat.getColor(this, R.color.bluesoft))
            Player2.add(cellId)
            ActivePlayer = 1
        }
        buSelected.isEnabled = false
        CheckWinner()
    }

    fun CheckWinner() {
        var winner = -1

        //row1
        if (Player1.contains(1) && Player1.contains(2) && Player1.contains(3)) {
            winner = 1
        }
        if (Player2.contains(1) && Player2.contains(2) && Player2.contains(3)) {
            winner = 2
        }

        //row2
        if (Player1.contains(4) && Player1.contains(5) && Player1.contains(6)) {
            winner = 1
        }
        if (Player2.contains(4) && Player2.contains(5) && Player2.contains(6)) {
            winner = 2
        }

        //row3
        if (Player1.contains(7) && Player1.contains(8) && Player1.contains(9)) {
            winner = 1
        }
        if (Player2.contains(7) && Player2.contains(8) && Player2.contains(9)) {
            winner = 2
        }

        //col1
        if (Player1.contains(1) && Player1.contains(4) && Player1.contains(7)) {
            winner = 1
        }
        if (Player2.contains(1) && Player2.contains(4) && Player2.contains(7)) {
            winner = 2
        }

        //col2
        if (Player1.contains(2) && Player1.contains(5) && Player1.contains(8)) {
            winner = 1
        }
        if (Player2.contains(2) && Player2.contains(5) && Player2.contains(8)) {
            winner = 2
        }

        //col3
        if (Player1.contains(3) && Player1.contains(6) && Player1.contains(9)) {
            winner = 1
        }
        if (Player2.contains(3) && Player2.contains(6) && Player2.contains(9)) {
            winner = 2
        }

        //cross1
        if (Player1.contains(1) && Player1.contains(5) && Player1.contains(9)) {
            winner = 1
        }
        if (Player2.contains(1) && Player2.contains(5) && Player2.contains(9)) {
            winner = 2
        }

        //cross2
        if (Player1.contains(3) && Player1.contains(5) && Player1.contains(7)) {
            winner = 1
        }
        if (Player2.contains(3) && Player2.contains(5) && Player2.contains(7)) {
            winner = 2
        }

        if (winner != -1) {
            if (winner == 1) {
                if (setPlayer == 1) {
                    Toast.makeText(this, getString(R.string.P1Wins), Toast.LENGTH_SHORT).show()
                    stopTouch()
                } else {
                    Toast.makeText(this, getString(R.string.YouWon), Toast.LENGTH_SHORT).show()
                    augmentaPuntuacio()
                    val database = FirebaseDatabase.getInstance("https://globusnilomar-default-rtdb.firebaseio.com/")
                    val reference = database.getReference("DATA BASE JUGADORS")
                    stopTouch()
                }
            } else {
                if (setPlayer == 1) {
                    Toast.makeText(this, getString(R.string.P2Wins), Toast.LENGTH_SHORT).show()
                    stopTouch()
                } else {
                    Toast.makeText(this, getString(R.string.CPUWins), Toast.LENGTH_SHORT).show()
                    stopTouch()
                }
            }
        }
        if (winner == -1 && Player1.size + Player2.size == 9) {
            Toast.makeText(this, getString(R.string.Tie), Toast.LENGTH_SHORT).show()
            stopTouch()
        }
        if (winner != -1 || (winner == -1 && Player1.size + Player2.size == 9)) {
            val soundPool = SoundPool.Builder().setMaxStreams(1).build()
            val soundId = soundPool.load(this, R.raw.sonido, 1)
            soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
                if (status == 0) {
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                }
            }
        }
    }

    fun augmentaPuntuacio() {
        user = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()
        val bdreference = database.getReference("DATA BASE JUGADORS")

        bdreference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.child("Email").getValue(String::class.java) == user?.email) {
                        val puntuacioString = ds.child("Puntuacio").getValue(String::class.java) ?: "0"
                        val puntuacioInt = puntuacioString.toIntOrNull() ?: 0

                        // Verificar si la puntuación ya ha sido aumentada
                        // val puntuacionAumentada = ds.child("PuntuacionAumentada").getValue(Boolean::class.java) ?: false

                        if (!puntuacionAumentada) {
                            puntuacionAumentada = true;
                            // Aumentar la puntuación solo si no se ha aumentado antes
                            val nuevaPuntuacioInt = puntuacioInt + 10
                            val nuevaPuntuacioString = nuevaPuntuacioInt.toString()

                            bdreference.child(ds.key!!).child("Puntuacio").setValue(nuevaPuntuacioString)
                            bdreference.child(ds.key!!).child("PuntuacionAumentada").setValue(true)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    fun stopTouch() {
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false
        button5.isEnabled = false
        button6.isEnabled = false
        button7.isEnabled = false
        button8.isEnabled = false
        button9.isEnabled = false
    }

    fun AutoPlay() {
        val emptyCells = ArrayList<Int>()
        for (cellId in 1..9) {
            if (Player1.contains(cellId) || Player2.contains(cellId)) {
            } else {
                emptyCells.add(cellId)
            }
        }

        val r = Random()
        val randomIndex = r.nextInt(emptyCells.size - 0) + 0
        val cellId = emptyCells[randomIndex]

        val buSelect: Button?
        buSelect = when (cellId) {
            1 -> button1
            2 -> button2
            3 -> button3
            4 -> button4
            5 -> button5
            6 -> button6
            7 -> button7
            8 -> button8
            9 -> button9
            else -> button1
        }

        PlayGame(cellId, buSelect)
    }
}
