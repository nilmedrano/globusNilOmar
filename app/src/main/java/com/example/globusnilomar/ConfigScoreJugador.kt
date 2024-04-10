package com.example.globusnilomar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ConfigScoreJugador(private val jugadorList: ArrayList<Jugador>) : RecyclerView.Adapter<ConfigScoreJugador.JugadorsViewHolder>() {

    private lateinit var jListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        jListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_jugador, parent, false)
        return JugadorsViewHolder(layoutInflater,jListener)
    }

    override fun getItemCount(): Int {
        return jugadorList.size
    }

    override fun onBindViewHolder(holder: JugadorsViewHolder, position: Int) {
        val jugador = jugadorList[position]
        holder.bind(jugador)
    }

    inner class JugadorsViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        private val context: Context = itemView.context
        private val nom: TextView = itemView.findViewById(R.id.tvNom_Jugador)
        private val puntuacio: TextView = itemView.findViewById(R.id.tvPuntuacio_Jugador)
        private val imatge: ImageView = itemView.findViewById(R.id.ivJugador)

        init {

            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)
            }

        }

        fun bind(jugador: Jugador) {
            nom.text = jugador.Nom
            puntuacio.text = jugador.Puntuacio

            if (jugador.Imatge.isNotEmpty()) {
                Picasso.get().load(jugador.Imatge).resize(150, 150).into(imatge)
            } else {
                Picasso.get().load(R.drawable.fperfil).resize(150, 150).into(imatge)
            }
        }
    }
}