package com.danish.aplikasibiodata_percobaan

import com.google.gson.annotations.SerializedName

object ScheduleStatus {
    const val ONGOING = "Sedang Berlangsung"
    const val BREAK = "Sedang Istirahat"
    const val FINISHED = "Jadwal Selesai"
    const val HOLIDAY = "Hari Ini Libur"
    const val UNAVAILABLE = "Belum Tersedia"
}

data class ScheduleItem(
    @SerializedName("class_name")
    val className: String = "",

    @SerializedName("hari")
    val day: String = "",

    @SerializedName("mapel")
    val subject: String = "",

    @SerializedName("guru")
    val teacher: String = "",

    @SerializedName("ruangan")
    val room: String = "",

    @SerializedName("jam_mulai")
    val startTime: String = "",

    @SerializedName("jam_selesai")
    val endTime: String = ""
)

data class HolidayItem(
    @SerializedName("tanggal")
    val date: String = "",

    @SerializedName("judul")
    val title: String = "",

    @SerializedName("keterangan")
    val description: String = ""
)

data class ScheduleDisplayState(
    val status: String,
    val title: String? = null,
    val currentSchedule: ScheduleItem? = null,
    val nextSchedule: ScheduleItem? = null,
    val holiday: HolidayItem? = null,
    val message: String? = null,
    val todaySchedules: List<ScheduleItem> = emptyList()
)

data class CurrentScheduleResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("kelas")
    val kelas: String? = null,

    @SerializedName("current")
    val current: ScheduleItem? = null,

    @SerializedName("next")
    val next: ScheduleItem? = null,

    @SerializedName("holiday")
    val holiday: HolidayItem? = null,

    @SerializedName("today_schedules")
    val todaySchedules: List<ScheduleItem>? = null
)
