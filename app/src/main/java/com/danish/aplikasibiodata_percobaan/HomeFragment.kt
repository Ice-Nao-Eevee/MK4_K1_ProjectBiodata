package com.danish.aplikasibiodata_percobaan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var selectedClass = SchoolClassRepository.DEFAULT_CLASS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        val name = prefs.getString("name", "Pengguna") ?: "Pengguna"
        val userClass = prefs.getString("class_name", SchoolClassRepository.DEFAULT_CLASS) ?: SchoolClassRepository.DEFAULT_CLASS
        selectedClass = if (userClass == "Umum" || !SchoolClassRepository.allClasses().contains(userClass)) {
            SchoolClassRepository.DEFAULT_CLASS
        } else {
            userClass
        }

        view.findViewById<TextView>(R.id.tvHomeGreeting).text = "Halo, $name"
        renderScheduleCard(view, SchoolScheduleRepository.getCurrentScheduleStatus(selectedClass))
        fetchScheduleCardFromLaravel(view)

        view.findViewById<View>(R.id.cardJadwal).setOnClickListener {
            findNavController().navigate(R.id.scheduleFragment)
        }
        view.findViewById<View>(R.id.cardDataSiswa).setOnClickListener {
            findNavController().navigate(R.id.studentsFragment)
        }
        view.findViewById<View>(R.id.cardGallery).setOnClickListener {
            findNavController().navigate(R.id.galleryFragment)
        }
        view.findViewById<View>(R.id.cardProfil).setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun fetchScheduleCardFromLaravel(view: View) {
        RetrofitClient.instance.getCurrentSchedule(selectedClass)
            .enqueue(object : Callback<CurrentScheduleResponse> {
                override fun onResponse(call: Call<CurrentScheduleResponse>, response: Response<CurrentScheduleResponse>) {
                    if (!isAdded) return
                    val body = response.body()
                    if (response.isSuccessful && body != null && body.success) {
                        renderScheduleCard(view, body.toDisplayState())
                    }
                }

                override fun onFailure(call: Call<CurrentScheduleResponse>, t: Throwable) {
                    // Tetap pakai fallback lokal, jangan ganggu user.
                }
            })
    }

    private fun CurrentScheduleResponse.toDisplayState(): ScheduleDisplayState {
        val status = when (this.status) {
            "berlangsung" -> ScheduleStatus.ONGOING
            "istirahat" -> ScheduleStatus.BREAK
            "selesai" -> ScheduleStatus.FINISHED
            "libur" -> ScheduleStatus.HOLIDAY
            else -> ScheduleStatus.UNAVAILABLE
        }
        return ScheduleDisplayState(
            status = status,
            title = current?.subject ?: holiday?.title,
            currentSchedule = current,
            nextSchedule = next,
            holiday = holiday,
            message = message ?: holiday?.description,
            todaySchedules = todaySchedules.orEmpty()
        )
    }

    private fun renderScheduleCard(view: View, state: ScheduleDisplayState) {
        val tvStatus = view.findViewById<TextView>(R.id.tvCurrentStatus)
        val tvClass = view.findViewById<TextView>(R.id.tvCurrentClass)
        val tvSubject = view.findViewById<TextView>(R.id.tvCurrentSubject)
        val tvTeacher = view.findViewById<TextView>(R.id.tvCurrentTeacher)
        val tvRoom = view.findViewById<TextView>(R.id.tvCurrentRoom)
        val tvTime = view.findViewById<TextView>(R.id.tvCurrentTime)
        val tvNextSubject = view.findViewById<TextView>(R.id.tvNextSubject)
        val tvNextTime = view.findViewById<TextView>(R.id.tvNextTime)

        tvStatus.text = state.status
        tvStatus.setBackgroundColor(ContextCompat.getColor(requireContext(), statusColor(state.status)))
        tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        tvClass.text = selectedClass

        when (state.status) {
            ScheduleStatus.ONGOING -> {
                val current = state.currentSchedule
                tvSubject.text = current?.subject ?: "Jadwal berlangsung"
                tvTeacher.text = "Guru: ${current?.teacher ?: "-"}"
                tvRoom.text = "Ruangan: ${current?.room ?: "-"}"
                tvTime.text = "${current?.startTime ?: "--:--"} - ${current?.endTime ?: "--:--"}"
            }
            ScheduleStatus.BREAK -> {
                tvSubject.text = "Sedang Istirahat"
                tvTeacher.text = state.message ?: "Persiapkan diri untuk jadwal berikutnya."
                tvRoom.text = "Ruangan: -"
                tvTime.text = "-"
            }
            ScheduleStatus.HOLIDAY -> {
                tvSubject.text = state.holiday?.title ?: state.title ?: "Hari Ini Libur"
                tvTeacher.text = state.holiday?.description ?: state.message ?: "Tidak ada kegiatan belajar mengajar."
                tvRoom.text = "Ruangan: -"
                tvTime.text = "-"
            }
            ScheduleStatus.FINISHED -> {
                tvSubject.text = "Jadwal Selesai"
                tvTeacher.text = state.message ?: "Tidak ada pelajaran lagi hari ini."
                tvRoom.text = "Ruangan: -"
                tvTime.text = "-"
            }
            else -> {
                tvSubject.text = "Jadwal Belum Tersedia"
                tvTeacher.text = state.message ?: "Jadwal kelas ini belum tersedia."
                tvRoom.text = "Ruangan: -"
                tvTime.text = "-"
            }
        }

        val next = state.nextSchedule
        tvNextSubject.text = next?.subject ?: "Tidak ada jadwal berikutnya"
        tvNextTime.text = next?.let { "${it.startTime} - ${it.endTime}" } ?: "-"
    }

    private fun statusColor(status: String): Int = when (status) {
        ScheduleStatus.ONGOING -> R.color.green_primary
        ScheduleStatus.BREAK -> R.color.tag_python_text
        ScheduleStatus.HOLIDAY -> R.color.primary_red
        ScheduleStatus.FINISHED -> R.color.gray_text
        else -> R.color.gray_text
    }
}
