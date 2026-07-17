package com.jp.pechaydetect.ui.fertilizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jp.pechaydetect.R
import com.jp.pechaydetect.data.fertilizer.FertilizerData
import com.jp.pechaydetect.data.model.FertilizerStep
import com.jp.pechaydetect.databinding.FragmentFertilizerBinding

class FertilizerFragment : Fragment() {

    private var _binding: FragmentFertilizerBinding? = null
    private val binding get() = _binding!!
    private val args: FertilizerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFertilizerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recommendation = FertilizerData.getRecommendation(args.deficiencyClass)

        if (recommendation == null) {
            binding.tvFertilizerTitle.text = getString(R.string.no_recommendation)
            return
        }

        binding.tvFertilizerTitle.text = recommendation.title
        binding.tvSeverity.text = recommendation.severity
        binding.tvDaReference.text = recommendation.daReference
        binding.tvNotes.text = recommendation.notes

        // Build fertilizer steps list
        val stepsText = buildStepsText(recommendation.recommendations)
        binding.tvFertilizerSteps.text = stepsText

        // Build cultural practices list
        val practicesText = recommendation.culturalPractices
            .mapIndexed { i, practice -> "${i + 1}. $practice" }
            .joinToString("\n\n")
        binding.tvCulturalPractices.text = practicesText

        // Color header by deficiency type
        val colorRes = when (args.deficiencyClass) {
            "Healthy"    -> R.color.status_healthy
            "Nitrogen"   -> R.color.status_nitrogen
            "Phosphorus" -> R.color.status_phosphorus
            "Potassium"  -> R.color.status_potassium
            else         -> R.color.primary_green
        }
        binding.headerCard.setCardBackgroundColor(resources.getColor(colorRes, null))
    }

    private fun buildStepsText(steps: List<FertilizerStep>): String {
        return steps.mapIndexed { i, step ->
            val organic = if (step.isOrganic) " [Organic]" else ""
            """Step ${i + 1}: ${step.product}$organic
Rate: ${step.rate}
Method: ${step.method}
Timing: ${step.timing}""".trimIndent()
        }.joinToString("\n\n──────────────────────────\n\n")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
