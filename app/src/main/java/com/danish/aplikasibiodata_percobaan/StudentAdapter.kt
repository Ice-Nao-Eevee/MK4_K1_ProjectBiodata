package com.danish.aplikasibiodata_percobaan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StudentAdapter(
    private var students: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvRole: TextView = view.findViewById(R.id.tvRole)
        val tvNis: TextView = view.findViewById(R.id.tvNis)
        val tvClassName: TextView = view.findViewById(R.id.tvClassName)
        val tvAbsenGender: TextView = view.findViewById(R.id.tvAbsenGender)
        val tagsContainer: LinearLayout = view.findViewById(R.id.tagsContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]

        holder.tvName.text = student.name.ifBlank { "Nama siswa belum diisi" }
        holder.tvRole.text = student.role.ifBlank { "Siswa" }
        holder.tvNis.text = "NIS: ${student.nis.ifBlank { "-" }}"
        holder.tvClassName.text = "Kelas: ${student.className.ifBlank { "-" }}"
        holder.tvAbsenGender.text = "Absen: ${if (student.absen > 0) student.absen else "-"} • L/P: ${student.gender.ifBlank { "-" }}"

        if (student.fotoUrl.startsWith("http")) {
            Glide.with(holder.itemView.context)
                .load(student.fotoUrl)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(holder.ivAvatar)
        } else {
            holder.ivAvatar.setImageResource(student.avatarRes.takeIf { it != 0 } ?: R.drawable.ic_avatar_placeholder)
        }

        holder.itemView.setOnClickListener { onItemClick(student) }
    }

    override fun getItemCount() = students.size

    fun updateList(newList: List<Student>) {
        students = newList
        notifyDataSetChanged()
    }
}
