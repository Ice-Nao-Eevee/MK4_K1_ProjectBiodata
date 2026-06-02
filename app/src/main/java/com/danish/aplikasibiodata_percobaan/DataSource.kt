package com.danish.aplikasibiodata_percobaan

/**
 * Data dummy lokal sudah tidak dipakai.
 * Data siswa sekarang diambil dari API Laravel: GET /api/siswa.
 * File ini sengaja dibiarkan supaya kalau ada referensi lama tidak langsung bikin compile error.
 */
object DataSource {
    fun getAllStudents(): List<Student> = emptyList()

    fun registerStudent(student: Student): Boolean = true

    fun buildRegisteredStudent(name: String, email: String): Student {
        val nis = email.substringBefore("@").ifBlank { "-" }
        return Student(
            id = 0, // 👈 MODIF: Tambahkan ID default di sini supaya tidak eror compile!
            nis = nis,
            name = name,
            absen = 0,
            gender = "-",
            birthDate = "Belum diisi",
            whatsapp = "08xxxxxxxxxx",
            instagram = "@instagram",
            email = email,
            role = "Siswa",
            category = "All",
            tags = emptyList(),
            bio = "",
            projects = 0,
            commits = 0,
            activityRole = "Siswa",
            quote = ""
        )
    }
}