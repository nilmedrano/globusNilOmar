package com.example.globusnilomar

import JocActivity
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class MenuActivity : AppCompatActivity() {
    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;
    lateinit var tancarSessio: Button
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    lateinit var miPuntuaciotxt: TextView
    lateinit var puntuacio: TextView
    lateinit var uid: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView
    lateinit var editarBtn: Button
    lateinit var edat: TextView
    lateinit var poblacio: TextView
    lateinit var imatgePerfil: ImageView
    lateinit var database: FirebaseDatabase
    lateinit var bdreference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consulta()
        setContentView(R.layout.activity_menu)
        tancarSessio =findViewById<Button>(R.id.tancarSessio)
        CreditsBtn =findViewById<Button>(R.id.CreditsBtn)
        PuntuacionsBtn =findViewById<Button>(R.id.PuntuacionsBtn)
        jugarBtn =findViewById<Button>(R.id.jugarBtn)
        auth= FirebaseAuth.getInstance()
        user =auth.currentUser
        tancarSessio.setOnClickListener(){
            tancalaSessio()
        }
        PuntuacionsBtn.setOnClickListener(){
            Toast.makeText(this,"PUNTUACIONS", Toast.LENGTH_SHORT).show()
            val intent= Intent(this, ScoreActivity::class.java)
            startActivity(intent)
            finish()        }
        CreditsBtn.setOnClickListener(){
            Toast.makeText(this,"Credits", Toast.LENGTH_SHORT).show()
        }
        PuntuacionsBtn.setOnClickListener(){
            Toast.makeText(this,"Puntuacions", Toast.LENGTH_SHORT).show()
        }
        jugarBtn.setOnClickListener(){
            Toast.makeText(this,"JUGAR", Toast.LENGTH_SHORT).show()
            val intent= Intent(this, JocActivity::class.java)
            startActivity(intent)
            finish()
        }
        puntuacio=findViewById(R.id.puntuacio)
        uid=findViewById(R.id.uid)
        correo=findViewById(R.id.correo)
        nom=findViewById(R.id.nom)
        //els hi assignem el tipus de lletra
        val tf1 = Typeface.createFromAsset(assets,"fonts/Ancient-Medium.ttf")
        val tf2 = Typeface.createFromAsset(assets,"fonts/Roboto-Regular.ttf")
        puntuacio.setTypeface(tf2)
        uid.setTypeface(tf2)
        correo.setTypeface(tf2)
        nom.setTypeface(tf2)
        tancarSessio.setTypeface(tf2)
        CreditsBtn.setTypeface(tf2)
        PuntuacionsBtn.setTypeface(tf2)
        jugarBtn.setTypeface(tf2)
        editarBtn = findViewById<Button>(R.id.editarBtn)
        edat=findViewById(R.id.edat)
        poblacio=findViewById(R.id.poblacio)
        imatgePerfil=findViewById(R.id.imatgePerfil)
        //Assignem tipus de lletra al botó
        editarBtn.setTypeface(tf2)
        editarBtn.setOnClickListener(){
            Toast.makeText(this,"EDITAR", Toast.LENGTH_SHORT).show()
        }

    }
    private fun Usuarilogejat()
    {
        if (user !=null)
        {
            Toast.makeText(this,"Jugador logejat",
                Toast.LENGTH_SHORT).show()
        }
        else
        {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Aquest mètode s'executarà quan s'obri el minijoc
    override fun onStart() {
        Usuarilogejat()
        super.onStart()
    }

    private fun tancalaSessio() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun consulta(){
        // Elimina la URL como argumento aquí
        database = FirebaseDatabase.getInstance()
        bdreference = database.getReference("DATA BASE JUGADORS")
        bdreference.addValueEventListener (object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i ("DEBUG","arrel value"+ snapshot.getValue().toString())
                Log.i ("DEBUG","arrel key"+ snapshot.key.toString())
                // ara capturem tots els fills
                var trobat: Boolean = false
                for (ds in snapshot.getChildren()) {
                    Log.i ("DEBUG","DS key:"+ds.child("Uid").key.toString())
                    Log.i ("DEBUG","DS value:"+ds.child("Uid").getValue().toString())
                    Log.i ("DEBUG","DS data:"+ds.child("Data").getValue().toString())
                    Log.i ("DEBUG","DS mail:"+ds.child("Email").getValue().toString())
                    //mirem si el mail és el mateix que el del jugador
                    //si és així, mostrem les dades als textview  corresponents
                    if (ds.child("Email").getValue().toString().equals(user?.email)){
                        trobat=true
                        //carrega els textview
                        puntuacio.setText(ds.child("Puntuacio").getValue().toString())
                        uid.setText(ds.child("Uid").getValue().toString())
                        correo.setText(ds.child("Email").getValue().toString())
                        nom.setText(ds.child("Nom").getValue().toString())
                        poblacio.setText( ds.child("Poblacio").getValue().toString())
                        edat.setText( ds.child("Edat").getValue().toString())
                        var imatge: String = ds.child("Imatge").getValue().toString()
                        if (imatge != "-1") {
                            // Mostrar la imagen en el ImageView 'foto'
                            Glide.with(this@MenuActivity) // Cambiar 'this' por 'this@MenuActivity'
                                .load(imatge)
                                .into(imatgePerfil)
                        }

                        /*try {
                            Picasso.get().load(imatge).into(imatgePerfil)
                        } catch (e:Exception){
                            Picasso.get().load(R.drawable.fperfil).into(imatgePerfil)
                        }*/
                    }
                    if (!trobat)
                    {
                        Log.e ("ERROR","ERROR NO TROBAT MAIL")
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e ("ERROR","ERROR DATABASE CANCEL")
            }
        })
    }

}