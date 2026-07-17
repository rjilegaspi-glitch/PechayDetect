package com.jp.pechaydetect.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jp.pechaydetect.R
import com.jp.pechaydetect.data.model.NutrientInfo
import com.jp.pechaydetect.databinding.FragmentNutrientLibraryBinding

class NutrientLibraryFragment : Fragment() {

    private var _binding: FragmentNutrientLibraryBinding? = null
    private val binding get() = _binding!!

    private val nutrients = listOf(
        NutrientInfo(
            nutrientName = "Nitrogen",
            symbol = "N",
            role = "Essential for leaf and stem growth. Major component of chlorophyll (the pigment " +
                    "that gives leaves their green colour) and all amino acids in plant tissue.",
            deficiencySymptoms = "Uniform yellowing (chlorosis) of older/lower leaves first, " +
                    "progressing upward. Stunted growth, thin stems, and small leaves. " +
                    "In severe cases, leaves turn pale yellow to white.",
            affectedParts = "Older (lower) leaves first; progresses to younger leaves in severe deficiency",
            colorIconRes = R.drawable.ic_nutrient_nitrogen
        ),
        NutrientInfo(
            nutrientName = "Phosphorus",
            symbol = "P",
            role = "Critical for energy transfer (ATP), root development, and flowering. " +
                    "Activates enzymes involved in photosynthesis and respiration.",
            deficiencySymptoms = "Purple or reddish discoloration on the underside of older leaves " +
                    "due to anthocyanin accumulation. Dark green upper surface. " +
                    "Delayed maturity and poor root development.",
            affectedParts = "Underside of older leaves; roots show poor development",
            colorIconRes = R.drawable.ic_nutrient_phosphorus
        ),
        NutrientInfo(
            nutrientName = "Potassium",
            symbol = "K",
            role = "Regulates water balance (stomata), enzyme activation, and carbohydrate " +
                    "transport. Improves disease resistance and fruit quality.",
            deficiencySymptoms = "Marginal leaf scorch (browning or necrosis along leaf edges) " +
                    "on older leaves, progressing inward. Interveinal yellowing may also occur. " +
                    "Weak stems; plants are more susceptible to stress.",
            affectedParts = "Leaf margins of older (lower) leaves first",
            colorIconRes = R.drawable.ic_nutrient_potassium
        ),
        NutrientInfo(
            nutrientName = "Healthy",
            symbol = "✓",
            role = "A healthy pechay plant has balanced NPK nutrition. Leaves are uniformly " +
                    "dark green, firm, and free from discoloration or necrosis.",
            deficiencySymptoms = "No deficiency symptoms. Leaves are deep green, turgid, and growing vigorously.",
            affectedParts = "No affected parts",
            colorIconRes = R.drawable.ic_nutrient_healthy
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutrientLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NutrientLibraryAdapter()
        binding.recyclerNutrients.adapter = adapter
        adapter.submitList(nutrients)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
