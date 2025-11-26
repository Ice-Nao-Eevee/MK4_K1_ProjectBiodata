package com.demas.mk4_k1_projectbiodata

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SiswaAdapter(private val listSiswa: List<Siswa>) :
    RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder>() {

    class SiswaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgFoto: ImageView = view.findViewById(R.id.imgFoto)  // TAMBAHAN
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvAbsen: TextView = view.findViewById(R.id.tvAbsen)
        val tvNis: TextView = view.findViewById(R.id.tvNis)
        val tvKelas: TextView = view.findViewById(R.id.tvKelas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_siswa, parent, false)
        return SiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        val siswa = listSiswa[position]

        holder.imgFoto.setImageResource(siswa.foto)  // TAMBAHAN: Set foto
        holder.tvNama.text = siswa.nama
        holder.tvAbsen.text = siswa.absen
        holder.tvNis.text = siswa.nis
        holder.tvKelas.text = siswa.kelas

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailSiswa::class.java)

            intent.putExtra("NAMA", siswa.nama)
            intent.putExtra("ABSEN", siswa.absen)
            intent.putExtra("NIS", siswa.nis)
            intent.putExtra("KELAS", siswa.kelas)
            intent.putExtra("GENDER", siswa.gender)
            intent.putExtra("HOBI", siswa.hobi)
            intent.putExtra("FOTO", siswa.foto)  // TAMBAHAN: Kirim foto

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listSiswa.size
}