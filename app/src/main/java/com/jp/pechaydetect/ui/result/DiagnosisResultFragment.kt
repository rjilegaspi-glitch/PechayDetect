package com.jp.pechaydetect.ui.result

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jp.pechaydetect.R
import com.jp.pechaydetect.data.fertilizer.FertilizerData
import com.jp.pechaydetect.databinding.FragmentDiagnosisResultBinding
import java.util.Locale

class DiagnosisResultFragment : Fragment() {

    private var _binding: FragmentDiagnosisResultBinding? = null
    private val binding get() = _binding!!
    private val args: DiagnosisResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiagnosisResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(Uri.parse(args.imageUri))
            .centerCrop()
            .into(binding.imgLeaf)

        val confidencePct = (args.confidence * 100).toInt()

        if (!args.isConfident) {
            binding.tvDeficiencyLabel.text = getString(R.string.uncertain_result)
            binding.tvConfidence.text = getString(R.string.confidence_format, confidencePct)
            binding.tvQuickAction.text = getString(R.string.ood_message)
            binding.cardDeficiencyColor.setCardBackgroundColor(
                resources.getColor(R.color.status_unknown, null)
            )
            binding.btnViewFertilizer.visibility = View.GONE
        } else {
            binding.tvDeficiencyLabel.text = args.predictedClass
            binding.tvConfidence.text = getString(R.string.confidence_format, confidencePct)
            binding.tvQuickAction.text = FertilizerData.getQuickAction(args.predictedClass)

            val colorRes = when (args.predictedClass) {
                "Healthy"    -> R.color.status_healthy
                "Nitrogen"   -> R.color.status_nitrogen
                "Phosphorus" -> R.color.status_phosphorus
                "Potassium"  -> R.color.status_potassium
                else         -> R.color.status_unknown
            }
            binding.cardDeficiencyColor.setCardBackgroundColor(
                resources.getColor(colorRes, null)
            )
            binding.btnViewFertilizer.visibility = View.VISIBLE
        }

        binding.btnViewFertilizer.setOnClickListener {
            val action = DiagnosisResultFragmentDirections
                .actionDiagnosisResultFragmentToFertilizerFragment(args.predictedClass)
            findNavController().navigate(action)
        }

        binding.btnScanAgain.setOnClickListener {
            findNavController().navigate(R.id.action_diagnosisResultFragment_to_cameraFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
