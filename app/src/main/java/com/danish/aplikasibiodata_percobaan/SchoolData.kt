package com.danish.aplikasibiodata_percobaan

import java.util.Calendar
import kotlin.math.abs

object SchoolClassRepository {
    val levels = listOf("X", "XI", "XII")
    val majors = listOf("PPLG", "TJKT")
    const val DEFAULT_CLASS = "XI PPLG 4"

    fun generateClassList(level: String, major: String): List<String> {
        val maxClass = when {
            level == "X" && major == "PPLG" -> 6
            level == "X" && major == "TJKT" -> 5
            level == "XI" && major == "PPLG" -> 7
            level == "XI" && major == "TJKT" -> 5
            level == "XII" && major == "PPLG" -> 7
            level == "XII" && major == "TJKT" -> 5
            else -> 1
        }
        return (1..maxClass).map { "$level $major $it" }
    }

    fun allClasses(): List<String> = levels.flatMap { level ->
        majors.flatMap { major -> generateClassList(level, major) }
    }
}

object SchoolScheduleRepository {
    private val holidays = listOf(
        HolidayItem("2026-06-01", "Hari Pancasila", "Libur nasional. Tidak ada kegiatan belajar mengajar."),
        HolidayItem("2026-06-15", "Libur Semester", "Tidak ada kegiatan belajar mengajar."),
        HolidayItem("2026-08-17", "Hari Kemerdekaan Indonesia", "Libur nasional memperingati HUT RI.")
    )

    private val pplgSubjects = listOf(
        "Pemrograman Web", "Basis Data", "Pemrograman Mobile", "Projek Kreatif", "Bahasa Indonesia", "Bahasa Inggris", "Matematika"
    )
    private val tjktSubjects = listOf(
        "Administrasi Jaringan", "Teknologi Layanan Jaringan", "Keamanan Jaringan", "Sistem Komputer", "Bahasa Indonesia", "Bahasa Inggris", "Matematika"
    )
    private val teachers = listOf("Pak Fendi", "Bu Rina", "Pak Arif", "Bu Sari", "Pak Dimas", "Bu Wulan", "Pak Agus")
    private val rooms = listOf("A.1.2", "A.2.3", "B.1.4", "B.2.1", "Lab RPL 1", "Lab TKJ 1", "Ruang 204")
    private val timeSlots = listOf(
        "07:00" to "07:40",
        "07:40" to "08:20",
        "08:50" to "09:30",
        "09:30" to "10:10",
        "10:10" to "10:50",
        "10:50" to "11:30",
        "13:10" to "13:50",
        "13:50" to "14:30"
    )

    fun getCurrentScheduleStatus(selectedClass: String, calendar: Calendar = Calendar.getInstance()): ScheduleDisplayState {
        val todayIso = formatDate(calendar)
        holidays.firstOrNull { it.date == todayIso }?.let { holiday ->
            return ScheduleDisplayState(
                status = ScheduleStatus.HOLIDAY,
                title = holiday.title,
                holiday = holiday,
                message = holiday.description
            )
        }

        val currentDay = dayName(calendar)
        if (currentDay == "Minggu") {
            return ScheduleDisplayState(
                status = ScheduleStatus.HOLIDAY,
                title = "Akhir Pekan",
                message = "Hari Minggu tidak ada jadwal pelajaran."
            )
        }

        val todaySchedules = buildDemoSchedule(selectedClass, currentDay)
        if (todaySchedules.isEmpty()) {
            return ScheduleDisplayState(
                status = ScheduleStatus.UNAVAILABLE,
                title = "Tidak Ada Jadwal",
                message = "Jadwal untuk hari ini belum tersedia."
            )
        }

        val nowMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        val current = todaySchedules.firstOrNull {
            nowMinutes >= timeToMinutes(it.startTime) && nowMinutes < timeToMinutes(it.endTime)
        }
        val next = todaySchedules.firstOrNull { timeToMinutes(it.startTime) > nowMinutes }

        return when {
            current != null -> ScheduleDisplayState(
                status = ScheduleStatus.ONGOING,
                title = current.subject,
                currentSchedule = current,
                nextSchedule = next,
                todaySchedules = todaySchedules,
                message = "${current.teacher} • ${current.room}"
            )
            next != null -> ScheduleDisplayState(
                status = ScheduleStatus.BREAK,
                title = "Sedang Istirahat",
                nextSchedule = next,
                todaySchedules = todaySchedules,
                message = "Pelajaran berikutnya ${next.subject} pukul ${next.startTime}."
            )
            else -> ScheduleDisplayState(
                status = ScheduleStatus.FINISHED,
                title = "Jadwal Selesai",
                todaySchedules = todaySchedules,
                message = "Tidak ada pelajaran lagi hari ini."
            )
        }
    }

    private fun buildDemoSchedule(className: String, day: String): List<ScheduleItem> {
        if (day == "Minggu") return emptyList()
        val isPplg = className.contains("PPLG", ignoreCase = true)
        val subjects = if (isPplg) pplgSubjects else tjktSubjects
        val seed = abs((className + day).hashCode())
        val count = if (day == "Jumat") 5 else 6
        return timeSlots.take(count).mapIndexed { index, slot ->
            val subject = if (day == "Sabtu" && index < 2) "Kegiatan 5R" else subjects[(seed + index) % subjects.size]
            ScheduleItem(
                className = className,
                day = day,
                subject = subject,
                teacher = teachers[(seed + index) % teachers.size],
                room = rooms[(seed + index) % rooms.size],
                startTime = slot.first,
                endTime = slot.second
            )
        }
    }

    private fun timeToMinutes(time: String): Int {
        val parts = time.split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
        return hour * 60 + minute
    }

    private fun formatDate(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "%04d-%02d-%02d".format(year, month, day)
    }

    fun dayName(calendar: Calendar = Calendar.getInstance()): String {
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Senin"
            Calendar.TUESDAY -> "Selasa"
            Calendar.WEDNESDAY -> "Rabu"
            Calendar.THURSDAY -> "Kamis"
            Calendar.FRIDAY -> "Jumat"
            Calendar.SATURDAY -> "Sabtu"
            Calendar.SUNDAY -> "Minggu"
            else -> "-"
        }
    }
}
