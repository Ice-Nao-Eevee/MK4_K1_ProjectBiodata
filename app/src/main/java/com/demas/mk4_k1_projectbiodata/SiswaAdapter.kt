package com.demas.mk4_k1_projectbiodata

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

// SiswaAdapter sekarang mengimplementasikan Filterable
class SiswaAdapter(private val listSiswa: List<Siswa>) :
    RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder>(), Filterable {

    // 1. Deklarasi daftar data yang ditampilkan (filteredList)
    private var filteredList: List<Siswa> = listSiswa
    // 2. Deklarasi daftar data asli (originalList)
    private val originalList: List<Siswa> = listSiswa.toList()

    class SiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvAbsen: TextView = itemView.findViewById(R.id.tvAbsen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa, parent, false)
        return SiswaViewHolder(view)
    }

    // Menggunakan filteredList
    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        val siswa = filteredList[position]

        holder.tvNama.text = siswa.nama
        holder.tvAbsen.text = "Absen: ${siswa.absen}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, DetailSiswa::class.java).apply {
                putExtra(DetailSiswa.EXTRA_NAMA, siswa.nama)
                putExtra(DetailSiswa.EXTRA_ABSEN, siswa.absen)
                putExtra(DetailSiswa.EXTRA_NIS, siswa.nis)
                putExtra(DetailSiswa.EXTRA_GENDER, siswa.gender)
                putExtra(DetailSiswa.EXTRA_HOBY, siswa.hobi)
                putExtra(DetailSiswa.EXTRA_KELAS, siswa.kelas)
            }

            context.startActivity(intent)
        }
    }

    // Menggunakan filteredList
    override fun getItemCount(): Int = filteredList.size

    // 3. Implementasi Filterable
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString().toLowerCase(Locale.getDefault())

                // Jika input kosong, tampilkan daftar asli (originalList)
                filteredList = if (charSearch.isEmpty()) {
                    originalList
                } else {
                    // Lakukan filter pada daftar asli
                    originalList.filter {
                        it.nama.toLowerCase(Locale.getDefault()).contains(charSearch)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // Perbarui daftar dan refresh RecyclerView
                filteredList = results?.values as List<Siswa>
                notifyDataSetChanged()
            }
        }
    }
}