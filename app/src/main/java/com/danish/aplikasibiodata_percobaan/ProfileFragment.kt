package com.danish.aplikasibiodata_percobaan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import java.util.Locale

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvNis: TextView
    private lateinit var tvAbsenGender: TextView
    private lateinit var tvBirthDate: TextView
    private lateinit var tvContact: TextView
    private lateinit var tvBio: TextView
    private lateinit var tvProjects: TextView
    private lateinit var tvCommits: TextView
    private lateinit var tvActivityRole: TextView
    private lateinit var tvQuote: TextView
    private lateinit var btnLogout: TextView
    private lateinit var btnEdit: TextView
    private lateinit var profileAvatar: ImageView

    private var currentStudentId: Int = 0
    private var currentNis: String = "-"
    private var currentName: String = ""
    private var currentAbsen: Int = 0
    private var currentGender: String = "-"
    private var currentBirthDate: String = "Belum diisi"
    private var currentWhatsapp: String = "08xxxxxxxxxx"
    private var currentInstagram: String = "@instagram"
    private var currentEmail: String = "-"
    private var currentRole: String = "Siswa"
    private var currentBio: String = "Belum ada biodata."
    private var currentProjects: Int = 0
    private var currentCommits: Int = 0
    private var currentActivityRole: String = "Siswa"
    private var currentQuote: String = ""
    private var currentFotoUrl: String = ""
    private var currentStudentClassroomId: Int = 0
    private var currentClassName: String = ""
    private var currentNamaAyah: String = ""
    private var currentNamaIbu: String = ""
    private var currentSensitiveHidden: Boolean = false
    private var currentPrivacyNotice: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.tvProfileName)
        tvRole = view.findViewById(R.id.tvProfileRole)
        tvNis = view.findViewById(R.id.tvNis)
        tvAbsenGender = view.findViewById(R.id.tvAbsenGender)
        tvBirthDate = view.findViewById(R.id.tvBirthDate)
        tvContact = view.findViewById(R.id.tvContact)
        tvBio = view.findViewById(R.id.tvBio)
        tvProjects = view.findViewById(R.id.tvProjects)
        tvCommits = view.findViewById(R.id.tvCommits)
        tvActivityRole = view.findViewById(R.id.tvActivityRole)
        tvQuote = view.findViewById(R.id.tvQuote)
        btnLogout = view.findViewById(R.id.btnLogout)
        btnEdit = view.findViewById(R.id.btnEditStudent)
        profileAvatar = view.findViewById(R.id.profileAvatar)

        // ── 🔴 FIX UTAMA: Cek bundle secara ketat & pastikan bukan navigasi tab bawah menu profil ──
        val hasStudentData = arguments != null && requireArguments().containsKey("studentName") && !arguments?.getString("studentName").isNullOrBlank()

        if (hasStudentData) {
            val actualStudentId = arguments?.getInt("studentId", 0) ?: 0
            val backupStudentId = arguments?.getInt("id", 0) ?: 0
            currentStudentId = if (actualStudentId != 0) actualStudentId else backupStudentId

            currentNis = arguments?.getString("studentNis") ?: "-"
            currentName = arguments?.getString("studentName") ?: "Nama Siswa"
            currentAbsen = arguments?.getInt("studentAbsen", 0) ?: 0
            currentGender = arguments?.getString("studentGender") ?: "-"
            currentBirthDate = arguments?.getString("studentBirthDate") ?: "Belum diisi"
            currentWhatsapp = arguments?.getString("studentWhatsapp") ?: "08xxxxxxxxxx"
            currentInstagram = arguments?.getString("studentInstagram") ?: "@instagram"
            currentEmail = arguments?.getString("studentEmail") ?: "-"
            currentRole = arguments?.getString("studentRole") ?: "Siswa"
            currentBio = arguments?.getString("studentBio") ?: "Belum ada biodata."
            currentProjects = arguments?.getInt("studentProjects", 0) ?: 0
            currentCommits = arguments?.getInt("studentCommits", 0) ?: 0
            currentActivityRole = arguments?.getString("studentActivityRole") ?: "Siswa"
            currentQuote = arguments?.getString("studentQuote") ?: "\"Belajar pelan-pelan, yang penting konsisten.\""
            currentFotoUrl = arguments?.getString("studentFoto") ?: ""
            currentStudentClassroomId = arguments?.getInt("studentClassroomId", 0) ?: 0
            currentClassName = arguments?.getString("studentClassName") ?: ""
            currentNamaAyah = arguments?.getString("studentNamaAyah") ?: ""
            currentNamaIbu = arguments?.getString("studentNamaIbu") ?: ""
            currentSensitiveHidden = arguments?.getBoolean("studentSensitiveHidden", false) ?: false
            currentPrivacyNotice = arguments?.getString("studentPrivacyNotice") ?: "Data kontak disembunyikan untuk menjaga privasi siswa."

            showClickedStudentProfile()

            // Hapus isi argument setelah dibaca agar saat tab menu Profil ditekan lagi, dia balik nampilin User Login!
            arguments?.clear()
        } else {
            showLoggedInUserProfile()
        }

        setupLogout()
        setupEditAction()
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = requireActivity().getSharedPreferences("edit_temp_sync", Context.MODE_PRIVATE)
        val updatedId = sharedPref.getInt("sync_id", 0)

        if (updatedId == currentStudentId && updatedId != 0) {
            currentName = sharedPref.getString("sync_name", currentName) ?: currentName
            currentNis = sharedPref.getString("sync_nis", currentNis) ?: currentNis
            currentAbsen = sharedPref.getInt("sync_absen", currentAbsen)
            currentGender = sharedPref.getString("sync_gender", currentGender) ?: currentGender
            currentBirthDate = sharedPref.getString("sync_birthdate", currentBirthDate) ?: currentBirthDate
            currentWhatsapp = sharedPref.getString("sync_whatsapp", currentWhatsapp) ?: currentWhatsapp
            currentInstagram = sharedPref.getString("sync_instagram", currentInstagram) ?: currentInstagram
            currentBio = sharedPref.getString("sync_bio", currentBio) ?: currentBio
            currentQuote = sharedPref.getString("sync_quote", currentQuote) ?: currentQuote
            currentFotoUrl = sharedPref.getString("sync_foto", currentFotoUrl) ?: currentFotoUrl

            showClickedStudentProfile()
            sharedPref.edit().clear().apply()
        }
    }

    private fun showClickedStudentProfile() {
        val prefs = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        val loginRole = prefs.getString("role", "siswa") ?: "siswa"
        val userClassroomId = prefs.getInt("classroom_id", -1)
        val roleLower = loginRole.lowercase()
        val canSeeSensitive = roleLower == "siswa" || roleLower == "wali_kelas" || roleLower == "admin_sekolah"
        val canSeeParents = roleLower == "wali_kelas" || roleLower == "admin_sekolah"

        tvName.text = currentName
        tvRole.text = currentRole
        tvNis.text = if (canSeeSensitive) "NIS: $currentNis" else "NIS: Disembunyikan"
        tvAbsenGender.text = "Absen: $currentAbsen • Gender: $currentGender"
        tvBirthDate.text = if (canSeeSensitive) "Tanggal lahir: $currentBirthDate" else "Tanggal lahir: Disembunyikan"
        tvContact.text = if (canSeeSensitive && !currentSensitiveHidden) {
            "Kontak: $currentWhatsapp • $currentInstagram • $currentEmail"
        } else {
            "Kontak: Disembunyikan untuk menjaga privasi siswa"
        }

        val baseBio = if (currentSensitiveHidden && !canSeeSensitive) {
            currentPrivacyNotice.ifBlank { "Data pribadi siswa disembunyikan untuk akun umum." }
        } else {
            currentBio
        }

        tvBio.text = if (canSeeParents) {
            val ayah = currentNamaAyah.ifBlank { "Belum diisi" }
            val ibu = currentNamaIbu.ifBlank { "Belum diisi" }
            "$baseBio\n\nData orang tua:\nAyah: $ayah\nIbu: $ibu"
        } else {
            baseBio
        }

        tvProjects.text = if (currentClassName.isNotBlank()) currentClassName.take(3) else currentProjects.toString()
        tvCommits.text = currentAbsen.toString()
        tvActivityRole.text = if (currentGender == "P") "Siswi" else "Siswa"
        tvQuote.text = currentQuote

        if (currentFotoUrl.isNotEmpty() && currentFotoUrl != "default.jpg") {
            Glide.with(this)
                .load(currentFotoUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(profileAvatar)
        } else {
            profileAvatar.setImageResource(R.drawable.ic_person)
        }

        if (roleLower == "wali_kelas" && userClassroomId == currentStudentClassroomId && userClassroomId != -1) {
            btnEdit.visibility = View.VISIBLE
        } else if (roleLower == "admin_sekolah") {
            btnEdit.visibility = View.VISIBLE
        } else {
            btnEdit.visibility = View.GONE
        }

        btnLogout.visibility = View.GONE
    }

    private fun showLoggedInUserProfile() {
        val prefs = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)

        val name = prefs.getString("name", "Pengguna") ?: "Pengguna"
        val email = prefs.getString("email", "-") ?: "-"
        val role = prefs.getString("role", "umum") ?: "umum"
        val className = prefs.getString("class_name", "") ?: ""
        val nis = prefs.getString("nis", "") ?: ""
        val absen = prefs.getInt("student_absen", 0)
        val gender = prefs.getString("student_gender", "") ?: ""
        val birthDate = prefs.getString("student_birth_date", "") ?: ""
        val whatsapp = prefs.getString("student_whatsapp", "") ?: ""
        val instagram = prefs.getString("student_instagram", "") ?: ""
        val bio = prefs.getString("student_bio", "") ?: ""
        val quote = prefs.getString("student_quote", "") ?: ""
        val foto = prefs.getString("student_foto", "") ?: ""

        val prettyRole = when (role.lowercase()) {
            "wali_kelas" -> if (className.isNotEmpty()) "Wali Kelas $className" else "Wali Kelas"
            "admin_sekolah" -> "Admin Sekolah"
            "siswa" -> if (className.isNotEmpty()) "Siswa $className" else "Siswa"
            "umum" -> "Pengguna Umum"
            else -> role.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        }

        tvName.text = name
        tvRole.text = prettyRole

        if (role.lowercase() == "siswa") {
            tvNis.text = "NIS: ${nis.ifBlank { "-" }}"
            tvAbsenGender.text = "Absen: ${if (absen == 0) "-" else absen} • Gender: ${gender.ifBlank { "-" }}"
            tvBirthDate.text = "Tanggal lahir: ${birthDate.ifBlank { "Belum diisi" }}"
            tvContact.text = "Kontak: ${whatsapp.ifBlank { "Belum diisi" }} • ${instagram.ifBlank { "Belum diisi" }} • ${email.ifBlank { "-" }}"
            tvBio.text = bio.ifBlank { "Ini adalah profil siswa yang sedang login." }
            tvProjects.text = className.ifBlank { "-" }
            tvCommits.text = if (absen == 0) "-" else absen.toString()
            tvActivityRole.text = if (gender == "P") "Siswi" else "Siswa"
            tvQuote.text = quote.ifBlank { "\"Selamat datang kembali, $name.\"" }
        } else if (role.lowercase() == "umum") {
            tvNis.text = "Akses: Umum"
            tvAbsenGender.text = "Level Akses: Data publik"
            tvBirthDate.text = "Tanggal lahir: -"
            tvContact.text = "Kontak siswa disembunyikan untuk akun umum"
            tvBio.text = "Akun umum dapat melihat data dasar sekolah. Email, WhatsApp, Instagram, tanggal lahir, dan data keluarga siswa disembunyikan untuk menjaga privasi."
            tvProjects.text = "U"
            tvCommits.text = "M"
            tvActivityRole.text = "Pengguna Umum"
            tvQuote.text = "\"Privasi siswa tetap dijaga.\""
        } else {
            tvNis.text = "Email: $email"
            tvAbsenGender.text = "Level Akses: $prettyRole"
            tvBirthDate.text = "Tanggal lahir: -"
            tvContact.text = "Kontak: $email"
            tvBio.text = "Ini adalah profil utama akun $prettyRole. Data orang tua siswa hanya tampil saat membuka detail siswa."
            tvProjects.text = if (role.lowercase() == "wali_kelas") "W" else "A"
            tvCommits.text = if (role.lowercase() == "wali_kelas") "L" else "D"
            tvActivityRole.text = prettyRole
            tvQuote.text = "\"Selamat datang kembali, $name.\""
        }

        if (foto.isNotBlank() && foto != "default.jpg") {
            Glide.with(this)
                .load(foto)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(profileAvatar)
        } else {
            profileAvatar.setImageResource(R.drawable.ic_person)
        }

        btnLogout.visibility = View.VISIBLE
        btnEdit.visibility = View.GONE
    }

    private fun setupLogout() {
        btnLogout.setOnClickListener {
            val prefs = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupEditAction() {
        btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), EditSiswaActivity::class.java).apply {
                putExtra("studentId", currentStudentId)
                putExtra("studentNis", currentNis)
                putExtra("studentName", currentName)
                putExtra("studentAbsen", currentAbsen)
                putExtra("studentGender", currentGender)
                putExtra("studentBirthDate", currentBirthDate)
                putExtra("studentWhatsapp", currentWhatsapp)
                putExtra("studentInstagram", currentInstagram)
                putExtra("studentEmail", currentEmail)
                putExtra("studentRole", currentRole)
                putExtra("studentBio", currentBio)
                putExtra("studentProjects", currentProjects)
                putExtra("studentCommits", currentCommits)
                putExtra("studentActivityRole", currentActivityRole)
                putExtra("studentQuote", currentQuote)
                putExtra("studentFoto", currentFotoUrl)
                putExtra("studentClassroomId", currentStudentClassroomId)
            }
            startActivity(intent)
        }
    }
}