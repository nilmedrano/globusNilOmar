package com.example.globusnilomar

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var correoEt: EditText
    private lateinit var passEt: EditText
    private lateinit var nombreEt: EditText
    private lateinit var fechaText: TextView
    private lateinit var Registrar: Button
    private lateinit var EditarFoto: Button
    private lateinit var EliminarFoto: Button
    private lateinit var edatEt: EditText
    private lateinit var poblacioEt: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var storageFotos: StorageReference
    private lateinit var image_url: Uri
    private lateinit var progressDialog: ProgressDialog
    private var COD_SEL_IMAGE = 300
    private lateinit var reference: DatabaseReference
    private lateinit var idd: String
    private lateinit var uidFoto: String
    private lateinit var foto: AppCompatImageView
    private lateinit var downloadUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        uidFoto = "0"
        downloadUri = "https://www.barakaldotiendaveterinaria.es/blog/wp-content/uploads/2018/12/blobfish.jpg"
        auth = FirebaseAuth.getInstance()
        correoEt = findViewById(R.id.correoEt)
        passEt = findViewById(R.id.passEt)
        nombreEt = findViewById(R.id.nombreEt)
        fechaText = findViewById(R.id.fechaEt)
        Registrar = findViewById(R.id.Registrar)
        edatEt = findViewById(R.id.edatEt)
        poblacioEt = findViewById(R.id.poblacioEt)
        EditarFoto = findViewById(R.id.EditarFoto)
        foto = findViewById(R.id.jugador)

        progressDialog = ProgressDialog(this)
        storageFotos = FirebaseStorage.getInstance().reference

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        val formatedDate = formatter.format(date)
        fechaText.text = formatedDate

        Registrar.setOnClickListener {
            val email: String = correoEt.text.toString()
            val pass: String = passEt.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                correoEt.error = "Invalid Mail"
            } else if (pass.length < 6) {
                passEt.error = "Password less than 6 chars"
            } else {
                RegistrarJugador(email, pass)
            }
        }

        EditarFoto.setOnClickListener {
            uploadPhoto()
        }
    }

    private fun uploadPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, COD_SEL_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uidFoto = UUID.randomUUID().toString()
        if (resultCode == RESULT_OK) {
            if (requestCode == COD_SEL_IMAGE) {
                val imageUrl = data?.data
                imageUrl?.let {
                    image_url = it
                    subirPhoto(uidFoto)
                }
            }
        }
    }

    private fun subirPhoto(uid: String) {
        progressDialog.setMessage("Updating photo")
        progressDialog.show()

        val routeStoragePhoto = "GlobusNilOmarFotos/$uid/photo"
        val reference = storageFotos.child(routeStoragePhoto)

        reference.putFile(image_url)
            .addOnSuccessListener { taskSnapshot ->
                reference.downloadUrl.addOnSuccessListener { uri ->
                    downloadUri = uri.toString()
                    /*val map = hashMapOf<String, Any>(
                        "photo" to downloadUri
                    )*/
                    Toast.makeText(this, getString(R.string.ImageUpdated), Toast.LENGTH_SHORT).show()

                    // Mostrar la imagen en el AppCompatImageView 'foto'
                    Glide.with(this)
                        .load(downloadUri)
                        .into(foto)

                    progressDialog.dismiss()
                    /*val database = FirebaseDatabase.getInstance("https://globusnilomar-default-rtdb.firebaseio.com/")
                    val dbReference = database.getReference("DATA BASE JUGADORS/$uid")
                    dbReference.updateChildren(map)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Update photo success", Toast.LENGTH_SHORT).show()

                            // Mostrar la imagen en el AppCompatImageView 'foto'
                            Glide.with(this)
                                .load(downloadUri)
                                .into(foto)

                            progressDialog.dismiss()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Failed to update photo: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressDialog.dismiss()
                        }*/
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, getString(R.string.ErrorImageUpdate) + " ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                progressDialog.dismiss()
            }
    }

    private fun RegistrarJugador(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.uid?.let {
                        RegistrarJugadorEnDB(it)
                    }
                } else {
                    Toast.makeText(baseContext, getString(R.string.AuthFail), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun RegistrarJugadorEnDB(uid: String) {
        val puntuacio = 0
        val pjugades = 0
        val pguanyades = 0
        val correoString = correoEt.text.toString()
        val passString = passEt.text.toString()
        val nombreString = nombreEt.text.toString()
        val fechaString = fechaText.text.toString()
        val edatString = edatEt.text.toString()
        val poblacioString = poblacioEt.text.toString()
        val dadesJugador = hashMapOf(
            "Uid" to uid,
            "Email" to correoString,
            "Password" to passString,
            "Nom" to nombreString,
            "Data" to fechaString,
            "Edat" to edatString,
            "Poblacio" to poblacioString,
            "Imatge" to downloadUri,
            "Puntuacio" to puntuacio.toString(),
            "PartidesJugades" to pjugades.toString(),
            "PartidesGuanyades" to pguanyades.toString()
        )

        val database = FirebaseDatabase.getInstance("https://globusnilomar-default-rtdb.firebaseio.com/")
        val reference = database.getReference("DATA BASE JUGADORS")

        reference.child(uid).setValue(dadesJugador)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.RegisterSucces), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, getString(R.string.RegisterFail)+ " ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
