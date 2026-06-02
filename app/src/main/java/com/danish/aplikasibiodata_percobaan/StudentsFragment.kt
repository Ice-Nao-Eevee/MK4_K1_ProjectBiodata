package com.danish.aplikasibiodata_percobaan

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView

class StudentsFragment : Fragment() {

    private lateinit var containerClassSections: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_students, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        containerClassSections = view.findViewById(R.id.containerClassSections)
        buildClassList()
    }

    private fun buildClassList() {
        containerClassSections.removeAllViews()

        SchoolClassRepository.levels.forEach { level ->
            val title = TextView(requireContext()).apply {
                text = "Kelas $level"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_red))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                typeface = Typeface.DEFAULT_BOLD
                setPadding(0, dp(18), 0, dp(10))
            }
            containerClassSections.addView(title)

            SchoolClassRepository.majors.forEach { major ->
                val majorTitle = TextView(requireContext()).apply {
                    text = major
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                    typeface = Typeface.DEFAULT_BOLD
                    setPadding(0, dp(4), 0, dp(8))
                }
                containerClassSections.addView(majorTitle)

                SchoolClassRepository.generateClassList(level, major).forEach { className ->
                    containerClassSections.addView(createClassRow(className))
                }
            }
        }
    }

    private fun createClassRow(className: String): View {
        val card = MaterialCardView(requireContext()).apply {
            radius = dp(18).toFloat()
            cardElevation = 1f
            strokeWidth = dp(1)
            strokeColor = ContextCompat.getColor(requireContext(), R.color.card_border)
            setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = dp(10) }
            isClickable = true
            isFocusable = true
        }

        val row = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(16), dp(16), dp(16))
        }

        val classText = TextView(requireContext()).apply {
            text = className
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val hint = TextView(requireContext()).apply {
            text = "Lihat siswa"
            setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_red))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            typeface = Typeface.DEFAULT_BOLD
        }

        row.addView(classText)
        row.addView(hint)
        card.addView(row)

        card.setOnClickListener {
            val bundle = Bundle().apply { putString("className", className) }
            findNavController().navigate(R.id.classStudentsFragment, bundle)
        }

        return card
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
