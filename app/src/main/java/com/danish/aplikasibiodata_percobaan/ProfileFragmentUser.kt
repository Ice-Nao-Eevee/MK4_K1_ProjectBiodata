package com.danish.aplikasibiodata_percobaan

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class ProfileFragmentUser : Fragment() {

    private lateinit var viewAccentBar: View
    private lateinit var ivProfilePic: ShapeableImageView
    private lateinit var tvInitials: TextView
    private lateinit var tvName: TextView
    private lateinit var tvRoleBadge: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvStat1Val: TextView
    private lateinit var tvStat1Lbl: TextView
    private lateinit var tvStat2Val: TextView
    private lateinit var tvStat2Lbl: TextView
    private lateinit var tvStat3Val: TextView
    private lateinit var tvStat3Lbl: TextView
    private lateinit var tvInfoSecLabel: TextView
    private lateinit var tvInfo2Label: TextView
    private lateinit var tvInfo2Value: TextView
    private lateinit var tvInfo2Badge: TextView
    private lateinit var cardAcademic: CardView
    private lateinit var cardGuestNote: CardView
    private lateinit var btnLogout: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_profile_user, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewAccentBar = view.findViewById(R.id.viewAccentBar)
        ivProfilePic = view.findViewById(R.id.ivMyProfilePic)
        tvInitials = view.findViewById(R.id.tvAvatarInitials)
        tvName = view.findViewById(R.id.tvMyName)
        tvRoleBadge = view.findViewById(R.id.tvMyRoleBadge)
        tvEmail = view.findViewById(R.id.tvMyEmail)
        tvStat1Val = view.findViewById(R.id.tvStat1Value)
        tvStat1Lbl = view.findViewById(R.id.tvStat1Label)
        tvStat2Val = view.findViewById(R.id.tvStat2Value)
        tvStat2Lbl = view.findViewById(R.id.tvStat2Label)
        tvStat3Val = view.findViewById(R.id.tvStat3Value)
        tvStat3Lbl = view.findViewById(R.id.tvStat3Label)
        tvInfoSecLabel = view.findViewById(R.id.tvInfoSectionLabel)
        tvInfo2Label = view.findViewById(R.id.tvInfo2Label)
        tvInfo2Value = view.findViewById(R.id.tvInfo2Value)
        tvInfo2Badge = view.findViewById(R.id.tvInfo2Badge)
        cardAcademic = view.findViewById(R.id.cardAcademic)
        cardGuestNote = view.findViewById(R.id.cardGuestNote)
        btnLogout = view.findViewById(R.id.btnLogout)

        renderFromSession()
        refreshProfileFromApi()

        btnLogout.setOnClickListener {
            val prefs = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun refreshProfileFromApi() {
        val prefs = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "").orEmpty()
        if (token.isBlank()) return

        RetrofitClient.instance.me("Bearer $token")
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (!isAdded || !response.isSuccessful) return
                    val user = response.body()?.user ?: return
                    val classroom = user.classroom
                    prefs.edit()
                        .putString("name", user.name)
                        .putString("email", user.email)
                        .putString("nis", user.nis)
                        .putString("role", user.role)
                        .putInt("classroom_id", classroom?.id ?: prefs.getInt("classroom_id", 0))
                        .putString("class_name", classroom?.namaKelas ?: prefs.getString("class_name", "Umum"))
                        .apply()
                    renderFromSession()
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    // Profil tetap memakai session lokal supaya aplikasi tidak crash saat server mati.
                }
            })
    }

    private fun renderFromSession() {
        val prefs = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        var name = prefs.getString("name", "Pengguna") ?: "Pengguna"
        val email = prefs.getString("email", "-") ?: "-"
        val nis = prefs.getString("nis", "") ?: ""
        val role = prefs.getString("role", "umum") ?: "umum"
        var className = prefs.getString("class_name", "Umum") ?: "Umum"

        // Fallback khusus agar login NIS milik kamu tetap tampil sebagai biodata Danish Adelio
        // walaupun response backend belum mengirim classroom lengkap.
        if (nis == "541241042") {
            if (name.equals("Pengguna", true) || name.equals("Siswa", true)) name = "DANISH ADELIO"
            if (className == "Umum" || className.isBlank()) className = "XI PPLG 4"
        }

        val parts = className.split(" ")
        val tingkat = parts.getOrNull(0) ?: "-"
        val jurusan = parts.getOrNull(1) ?: "-"
        val nomorKelas = parts.getOrNull(2) ?: "-"

        val prettyRole = when (role.lowercase(Locale.getDefault())) {
            "wali_kelas" -> if (className != "Umum") "Wali Kelas $className" else "Wali Kelas"
            "siswa" -> "Siswa $className"
            "umum" -> "Pengguna Umum"
            else -> role.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }

        val primary = ContextCompat.getColor(requireContext(), R.color.primary_red)
        viewAccentBar.setBackgroundColor(primary)
        ivProfilePic.strokeColor = ColorStateList.valueOf(primary)
        tvInitials.text = getInitials(name)
        tvInitials.setTextColor(primary)
        tvInitials.visibility = View.VISIBLE

        tvName.text = name
        tvRoleBadge.text = prettyRole
        tvRoleBadge.setTextColor(primary)

        tvEmail.text = when {
            nis.isNotBlank() -> "NIS: $nis"
            email.isNotBlank() && email != "null" -> email
            else -> "Belum ada kontak"
        }

        tvStat1Val.text = if (nis.isNotBlank()) nis.takeLast(3) else nomorKelas
        tvStat1Lbl.text = if (nis.isNotBlank()) "NIS Akhir" else "Kelas"
        tvStat2Val.text = tingkat
        tvStat2Lbl.text = "Tingkat"
        tvStat3Val.text = jurusan
        tvStat3Lbl.text = "Jurusan"

        tvInfoSecLabel.text = "BIODATA AKUN"
        tvInfo2Label.text = "Kelas"
        tvInfo2Value.text = if (className == "Umum") "Belum terikat kelas" else className
        tvInfo2Badge.text = prettyRole
        tvInfo2Badge.visibility = View.VISIBLE

        if (role.lowercase(Locale.getDefault()) == "siswa") {
            cardAcademic.visibility = View.VISIBLE
            cardGuestNote.visibility = View.GONE
        } else if (role.lowercase(Locale.getDefault()) == "wali_kelas") {
            cardAcademic.visibility = View.GONE
            cardGuestNote.visibility = View.GONE
        } else {
            cardAcademic.visibility = View.GONE
            cardGuestNote.visibility = View.VISIBLE
        }

        btnLogout.text = "Keluar dari Akun"
    }

    private fun getInitials(fullName: String): String {
        val parts = fullName.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
        return when {
            parts.isEmpty() -> "U"
            parts.size == 1 -> parts[0].take(2).uppercase(Locale.getDefault())
            else -> "${parts[0].first()}${parts[1].first()}".uppercase(Locale.getDefault())
        }
    }
}
