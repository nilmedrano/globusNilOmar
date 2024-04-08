package com.example.globusnilomar

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class PlayerDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_details)

        val ImatgeDetall: ImageView = findViewById(R.id.imatgeD)
        // Obtén la referencia a los TextViews y Labels
        val NomDetall: TextView = findViewById(R.id.nomD)
        val PuntuacioDetall: TextView = findViewById(R.id.puntuacioD)
        val DataDetall: TextView = findViewById(R.id.dataD)
        val EdatDetall: TextView = findViewById(R.id.edatD)
        val EmailDetall: TextView = findViewById(R.id.emailD)
        val PoblacioDetall: TextView = findViewById(R.id.poblacioD)

        val PuntuacioLabel: TextView = findViewById(R.id.puntuacioLabel)
        val DataLabel: TextView = findViewById(R.id.dataLabel)
        val EdatLabel: TextView = findViewById(R.id.edatLabel)
        val EmailLabel: TextView = findViewById(R.id.emailLabel)
        val PoblacioLabel: TextView = findViewById(R.id.poblacioLabel)


        val bundle: Bundle? = intent.extras
        val Nom = bundle?.getString("Nom")
        val Puntuacio = bundle?.getString("Puntuacio")
        val Imatge = bundle?.getString("Imatge")
        val Data = bundle?.getString("Data")
        val Edat = bundle?.getString("Edat")
        val Email = bundle?.getString("Email")
        val Poblacio = bundle?.getString("Poblacio")

        NomDetall.text = Nom
        PuntuacioDetall.text = Puntuacio
        // Asigna el ImagenView adecuadamente con la librería Picasso o Glide según corresponda
        if (Imatge != null && Imatge.isNotEmpty()) {
            Picasso.get().load(Imatge).into(ImatgeDetall)
        } else {
            // Si la URL de la imagen es nula o vacía, carga la imagen predeterminada desde drawable
            Picasso.get().load(R.drawable.fperfil).into(ImatgeDetall)
        }
        DataDetall.text = Data
        EdatDetall.text = Edat
        EmailDetall.text = Email
        PoblacioDetall.text = Poblacio



    }
}