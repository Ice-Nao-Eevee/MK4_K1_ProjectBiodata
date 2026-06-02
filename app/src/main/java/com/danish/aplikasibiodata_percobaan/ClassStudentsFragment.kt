package com.danish.aplikasibiodata_percobaan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class ClassStudentsFragment : Fragment() {

    private lateinit var adapter: StudentAdapter
    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var tvEmptyState: TextView
    private lateinit var rvStudents: RecyclerView
    private lateinit var etSearch: EditText

    private var selectedClass = SchoolClassRepository.DEFAULT_CLASS
    private var selectedClassroomId = 1
    private var currentSearch = ""
    private var studentsFromApi: List<Student> = emptyList()
    private var classroomIdByName: MutableMap<String, Int> = fallbackClassroomMap().toMutableMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_class_students, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedClass = arguments?.getString("className") ?: SchoolClassRepository.DEFAULT_CLASS
        selectedClassroomId = classroomIdByName[selectedClass] ?: 1

        tvTitle = view.findViewById(R.id.tvClassStudentTitle)
        tvSubtitle = view.findViewById(R.id.tvClassStudentSubtitle)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)
        rvStudents = view.findViewById(R.id.rvStudents)
        etSearch = view.findViewById(R.id.etSearch)
        val btnBack = view.findViewById<TextView>(R.id.btnBackToClasses)

        tvTitle.text = selectedClass
        tvSubtitle.text = "Memuat data siswa kelas $selectedClass"

        adapter = StudentAdapter(emptyList()) { student ->
            val bundle = Bundle().apply {
                putInt("studentId", student.id)
                putInt("studentClassroomId", student.classroomId)
                putString("studentNis", student.nis)
                putString("studentName", student.name)
                putInt("studentAbsen", student.absen)
                putString("studentGender", student.gender)
                putString("studentBirthDate", student.birthDate)
                putString("studentWhatsapp", student.whatsapp)
                putString("studentInstagram", student.instagram)
                putString("studentEmail", student.email)
                putString("studentRole", student.role)
                putString("studentBio", student.bio)
                putInt("studentProjects", student.projects)
                putInt("studentCommits", student.commits)
                putString("studentActivityRole", student.activityRole)
                putString("studentQuote", student.quote)
                putString("studentFoto", student.fotoUrl)
                putString("studentClassName", student.className)
                putString("studentNamaAyah", student.namaAyah)
                putString("studentNamaIbu", student.namaIbu)
                putBoolean("studentSensitiveHidden", student.isSensitiveHidden)
                putString("studentPrivacyNotice", student.privacyNotice)
            }
            findNavController().navigate(R.id.detailSiswaFragment, bundle)
        }

        rvStudents.layoutManager = LinearLayoutManager(requireContext())
        rvStudents.adapter = adapter

        btnBack.setOnClickListener { findNavController().popBackStack() }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                currentSearch = s.toString().trim().lowercase()
                applySearch()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        fetchClassroomsThenStudents()
    }

    private fun fetchClassroomsThenStudents() {
        RetrofitClient.instance.getClassrooms()
            .enqueue(object : Callback<ClassroomsMasterResponse> {
                override fun onResponse(call: Call<ClassroomsMasterResponse>, response: Response<ClassroomsMasterResponse>) {
                    if (!isAdded) return
                    if (response.isSuccessful) {
                        val rooms = response.body()?.data.orEmpty()
                        if (rooms.isNotEmpty()) {
                            classroomIdByName.clear()
                            rooms.forEach { room ->
                                val name = room.namaKelas.orEmpty()
                                val id = room.id ?: return@forEach
                                if (name.isNotBlank()) classroomIdByName[name] = id
                            }
                            selectedClassroomId = classroomIdByName[selectedClass] ?: selectedClassroomId
                        }
                    }
                    fetchStudents()
                }

                override fun onFailure(call: Call<ClassroomsMasterResponse>, t: Throwable) {
                    if (!isAdded) return
                    fetchStudents()
                }
            })
    }

    private fun fetchStudents() {
        val prefs = requireContext().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)
        if (token.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Silakan login dulu", Toast.LENGTH_SHORT).show()
            goToLogin()
            return
        }

        tvEmptyState.visibility = View.VISIBLE
        rvStudents.visibility = View.GONE
        tvEmptyState.text = "Memuat data siswa $selectedClass..."

        RetrofitClient.instance.getStudents("Bearer $token", selectedClassroomId)
            .enqueue(object : Callback<SiswaResponse> {
                override fun onResponse(call: Call<SiswaResponse>, response: Response<SiswaResponse>) {
                    if (!isAdded) return
                    if (response.isSuccessful) {
                        val siswaList = response.body()?.data.orEmpty()
                        studentsFromApi = if (siswaList.isNotEmpty()) {
                            siswaList.map { it.toStudent() }.sortedBy { it.absen }
                        } else {
                            listOf(createDemoStudent(selectedClass, selectedClassroomId))
                        }
                    } else {
                        studentsFromApi = listOf(createDemoStudent(selectedClass, selectedClassroomId))
                        if (response.code() == 401) {
                            prefs.edit().clear().apply()
                            goToLogin()
                            return
                        }
                    }
                    applySearch()
                }

                override fun onFailure(call: Call<SiswaResponse>, t: Throwable) {
                    if (!isAdded) return
                    studentsFromApi = listOf(createDemoStudent(selectedClass, selectedClassroomId))
                    tvSubtitle.text = "Mode demo lokal • Laravel belum terhubung"
                    applySearch()
                }
            })
    }

    private fun SiswaApiItem.toStudent(): Student {
        val nisValue = nis.orEmpty()
        val genderValue = jenisKelamin.orEmpty()
        val classNameFromApi = classroom?.namaKelas ?: selectedClass
        val classroomIdValue = classroomId ?: classroom?.id ?: selectedClassroomId
        val hidden = isSensitiveHidden == true
        val notice = privacyNotice ?: "Data kontak disembunyikan untuk menjaga privasi siswa."

        return Student(
            id = id ?: 0,
            nis = nisValue,
            name = nama.orEmpty(),
            absen = noAbsen ?: 0,
            gender = genderValue,
            classroomId = classroomIdValue,
            birthDate = if (hidden) "Disembunyikan" else (tanggalLahir ?: "Belum diisi"),
            whatsapp = if (hidden) "Disembunyikan" else (whatsapp ?: "Belum diisi"),
            instagram = if (hidden) "Disembunyikan" else (instagram ?: "Belum diisi"),
            email = if (hidden) "Disembunyikan" else (email ?: if (nisValue.isBlank()) "-" else "$nisValue@smktelkom-pwt.sch.id"),
            className = classNameFromApi,
            role = jabatanDev ?: "Siswa",
            category = "Siswa",
            bio = bio ?: if (hidden) notice else "Siswa SMK Telkom Purwokerto.",
            projects = 0,
            commits = 0,
            activityRole = if (genderValue == "P") "Siswi" else "Siswa",
            quote = quote ?: "\"Belajar pelan-pelan, yang penting konsisten.\"",
            avatarRes = R.drawable.ic_avatar_placeholder,
            fotoUrl = foto.orEmpty(),
            namaAyah = namaAyah.orEmpty(),
            namaIbu = namaIbu.orEmpty(),
            isSensitiveHidden = hidden,
            privacyNotice = notice
        )
    }

    private fun applySearch() {
        val filtered = studentsFromApi.filter { student ->
            currentSearch.isEmpty() ||
                student.name.lowercase().contains(currentSearch) ||
                student.nis.lowercase().contains(currentSearch) ||
                student.className.lowercase().contains(currentSearch)
        }

        if (filtered.isEmpty()) {
            rvStudents.visibility = View.GONE
            tvEmptyState.visibility = View.VISIBLE
            tvEmptyState.text = "Belum ada data siswa di kelas $selectedClass."
            adapter.updateList(emptyList())
        } else {
            tvEmptyState.visibility = View.GONE
            rvStudents.visibility = View.VISIBLE
            adapter.updateList(filtered)
            tvSubtitle.text = "${filtered.size} siswa ditemukan di $selectedClass"
        }
    }

    private fun createDemoStudent(className: String, classroomId: Int): Student {
        val names = listOf("Alya Prameswari", "Raka Mahendra", "Nadia Putri", "Bagas Pratama", "Dimas Arya", "Salsa Nabila", "Fajar Ramadhan")
        val index = abs(className.hashCode()) % names.size
        return Student(
            id = classroomId * 1000 + 1,
            nis = "DUMMY%03d".format(classroomId),
            name = names[index],
            absen = 1,
            gender = if (index % 2 == 0) "P" else "L",
            classroomId = classroomId,
            birthDate = "Belum diisi",
            whatsapp = "Belum diisi",
            instagram = "@dummy",
            email = "dummy$classroomId@smktelkom-pwt.sch.id",
            className = className,
            role = "Siswa",
            category = "Siswa",
            bio = "Data dummy untuk demo kelas $className.",
            projects = 0,
            commits = 0,
            activityRole = "Siswa",
            quote = "\"Data demo untuk kebutuhan presentasi.\"",
            avatarRes = R.drawable.ic_avatar_placeholder,
            fotoUrl = "",
            namaAyah = "Bapak Demo",
            namaIbu = "Ibu Demo",
            isSensitiveHidden = false,
            privacyNotice = ""
        )
    }

    private fun fallbackClassroomMap(): Map<String, Int> {
        val map = linkedMapOf<String, Int>()
        SchoolClassRepository.allClasses().forEachIndexed { index, className ->
            map[className] = index + 1
        }
        return map
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
