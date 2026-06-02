package com.danish.aplikasibiodata_percobaan

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleDetailFragment : Fragment() {

    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvMainTitle: TextView
    private lateinit var tvMainSubtitle: TextView
    private lateinit var tvMainMeta: TextView
    private lateinit var tvNextTitle: TextView
    private lateinit var tvNextSubtitle: TextView
    private lateinit var cardNext: CardView
    private lateinit var llTodaySchedule: LinearLayout
    private lateinit var tvTodayEmpty: TextView

    private var selectedClass = SchoolClassRepository.DEFAULT_CLASS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_schedule_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedClass = arguments?.getString("className") ?: SchoolClassRepository.DEFAULT_CLASS

        tvTitle = view.findViewById(R.id.tvScheduleDetailTitle)
        tvSubtitle = view.findViewById(R.id.tvScheduleDetailSubtitle)
        tvStatus = view.findViewById(R.id.tvStatus)
        tvMainTitle = view.findViewById(R.id.tvMainTitle)
        tvMainSubtitle = view.findViewById(R.id.tvMainSubtitle)
        tvMainMeta = view.findViewById(R.id.tvMainMeta)
        tvNextTitle = view.findViewById(R.id.tvNextTitle)
        tvNextSubtitle = view.findViewById(R.id.tvNextSubtitle)
        cardNext = view.findViewById(R.id.cardNext)
        llTodaySchedule = view.findViewById(R.id.llTodaySchedule)
        tvTodayEmpty = view.findViewById(R.id.tvTodayEmpty)
        val btnBack = view.findViewById<TextView>(R.id.btnBackToScheduleClasses)

        tvTitle.text = selectedClass
        tvSubtitle.text = "Memuat jadwal berjalan kelas $selectedClass"
        btnBack.setOnClickListener { findNavController().popBackStack() }

        fetchSchedule(selectedClass)
    }

    private fun fetchSchedule(className: String) {
        renderLoading(className)
        RetrofitClient.instance.getCurrentSchedule(className)
            .enqueue(object : Callback<CurrentScheduleResponse> {
                override fun onResponse(call: Call<CurrentScheduleResponse>, response: Response<CurrentScheduleResponse>) {
                    if (!isAdded) return
                    val body = response.body()
                    if (response.isSuccessful && body != null && body.success) {
                        renderFromResponse(body)
                    } else {
                        renderSchedule(SchoolScheduleRepository.getCurrentScheduleStatus(className), "Mode demo lokal • Laravel belum memberi data")
                    }
                }

                override fun onFailure(call: Call<CurrentScheduleResponse>, t: Throwable) {
                    if (!isAdded) return
                    renderSchedule(SchoolScheduleRepository.getCurrentScheduleStatus(className), "Mode demo lokal • Laravel belum terhubung")
                }
            })
    }

    private fun renderLoading(className: String) {
        tvStatus.text = "Memuat"
        applyStatusColor(ScheduleStatus.UNAVAILABLE)
        tvMainTitle.text = "Mengambil jadwal..."
        tvMainSubtitle.text = className
        tvMainMeta.text = "Mohon tunggu sebentar."
        cardNext.visibility = View.GONE
        llTodaySchedule.removeAllViews()
        tvTodayEmpty.visibility = View.VISIBLE
        tvTodayEmpty.text = "Memuat jadwal hari ini..."
    }

    private fun renderFromResponse(response: CurrentScheduleResponse) {
        val status = when (response.status) {
            "berlangsung" -> ScheduleStatus.ONGOING
            "istirahat" -> ScheduleStatus.BREAK
            "selesai" -> ScheduleStatus.FINISHED
            "libur" -> ScheduleStatus.HOLIDAY
            else -> ScheduleStatus.UNAVAILABLE
        }

        val state = ScheduleDisplayState(
            status = status,
            title = response.current?.subject ?: response.holiday?.title ?: response.message,
            currentSchedule = response.current,
            nextSchedule = response.next,
            holiday = response.holiday,
            message = response.message,
            todaySchedules = response.todaySchedules.orEmpty()
        )
        renderSchedule(state, "Live dari Laravel • $selectedClass")
    }

    private fun renderSchedule(state: ScheduleDisplayState, infoText: String? = null) {
        tvStatus.text = state.status
        applyStatusColor(state.status)
        tvSubtitle.text = infoText ?: "Jadwal berjalan $selectedClass"

        when (state.status) {
            ScheduleStatus.ONGOING -> renderOngoing(state)
            ScheduleStatus.BREAK -> renderBreak(state)
            ScheduleStatus.FINISHED -> renderFinished(state)
            ScheduleStatus.HOLIDAY -> renderHoliday(state)
            else -> renderUnavailable(state)
        }
        renderTodaySchedule(state.todaySchedules)
    }

    private fun renderOngoing(state: ScheduleDisplayState) {
        val current = state.currentSchedule
        tvMainTitle.text = current?.subject ?: state.title ?: "Sedang Berlangsung"
        tvMainSubtitle.text = selectedClass
        tvMainMeta.text = current?.let { "${it.teacher} • ${it.room} • ${cleanTime(it.startTime)} - ${cleanTime(it.endTime)}" }
            ?: state.message.orEmpty()
        renderNext(state.nextSchedule)
    }

    private fun renderBreak(state: ScheduleDisplayState) {
        tvMainTitle.text = "Sedang Istirahat"
        tvMainSubtitle.text = selectedClass
        tvMainMeta.text = state.message ?: "Persiapkan diri untuk pelajaran berikutnya."
        renderNext(state.nextSchedule)
    }

    private fun renderFinished(state: ScheduleDisplayState) {
        tvMainTitle.text = "Jadwal Hari Ini Selesai"
        tvMainSubtitle.text = selectedClass
        tvMainMeta.text = state.message ?: "Tidak ada pelajaran lagi hari ini."
        cardNext.visibility = View.GONE
    }

    private fun renderHoliday(state: ScheduleDisplayState) {
        tvMainTitle.text = state.holiday?.title ?: state.title ?: "Hari Ini Libur"
        tvMainSubtitle.text = selectedClass
        tvMainMeta.text = state.holiday?.description ?: state.message ?: "Tidak ada kegiatan belajar mengajar."
        cardNext.visibility = View.GONE
    }

    private fun renderUnavailable(state: ScheduleDisplayState) {
        tvMainTitle.text = state.title ?: "Jadwal Belum Tersedia"
        tvMainSubtitle.text = selectedClass
        tvMainMeta.text = state.message ?: "Jadwal kelas ini belum tersedia."
        cardNext.visibility = View.GONE
    }

    private fun renderNext(next: ScheduleItem?) {
        if (next == null) {
            cardNext.visibility = View.GONE
            return
        }
        cardNext.visibility = View.VISIBLE
        tvNextTitle.text = next.subject
        tvNextSubtitle.text = "${next.teacher} • ${next.room} • ${cleanTime(next.startTime)} - ${cleanTime(next.endTime)}"
    }

    private fun renderTodaySchedule(todaySchedules: List<ScheduleItem>) {
        llTodaySchedule.removeAllViews()
        if (todaySchedules.isEmpty()) {
            tvTodayEmpty.visibility = View.VISIBLE
            return
        }

        tvTodayEmpty.visibility = View.GONE
        todaySchedules.forEach { item ->
            val row = TextView(requireContext()).apply {
                text = "${cleanTime(item.startTime)} - ${cleanTime(item.endTime)}  •  ${item.subject}\n${item.teacher} • ${item.room}"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                setPadding(dp(18), dp(14), dp(18), dp(14))
                background = ContextCompat.getDrawable(requireContext(), R.drawable.login_card_bg)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = dp(10) }
            }
            llTodaySchedule.addView(row)
        }
    }

    private fun applyStatusColor(status: String) {
        val colorRes = when (status) {
            ScheduleStatus.ONGOING -> R.color.green_primary
            ScheduleStatus.BREAK -> R.color.tag_python_text
            ScheduleStatus.HOLIDAY -> R.color.primary_red
            ScheduleStatus.FINISHED -> R.color.gray_text
            else -> R.color.gray_text
        }
        tvStatus.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes))
        tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun cleanTime(value: String): String = value.take(5).ifBlank { "-" }
    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
