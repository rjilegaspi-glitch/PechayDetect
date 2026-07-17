package com.jp.pechaydetect.ui.historydetail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jp.pechaydetect.PechayDetectApp
import com.jp.pechaydetect.R
import com.jp.pechaydetect.data.fertilizer.FertilizerData
import com.jp.pechaydetect.databinding.FragmentScanDetailBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScanDetailFragment : Fragment() {

    private var _binding: FragmentScanDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ScanDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = (requireActivity().application as PechayDetectApp).repository

        lifecycleScope.launch {
            val scan = repository.getScanById(args.scanId) ?: return@launch

            Glide.with(requireView())
                .load(Uri.parse(scan.imagePath))
                .centerCrop()
                .into(binding.imgLeaf)

            val dateFormat = SimpleDateFormat("MMMM d, yyyy  h:mm a", Locale.getDefault())
            binding.tvScanDate.text = dateFormat.format(Date(scan.timestamp))
            binding.tvPrediction.text = scan.predictedClass
            binding.tvConfidence.text = getString(
                R.string.confidence_format, (scan.confidence * 100).toInt()
            )
            binding.tvQuickAction.text = FertilizerData.getQuickAction(scan.predictedClass)

            val colorRes = when (scan.predictedClass) {
                "Healthy"    -> R.color.status_healthy
                "Nitrogen"   -> R.color.status_nitrogen
                "Phosphorus" -> R.color.status_phosphorus
                "Potassium"  -> R.color.status_potassium
                else         -> R.color.status_unknown
            }
            binding.cardStatus.setCardBackgroundColor(
                resources.getColor(colorRes, null)
            )

            binding.btnViewFertilizer.setOnClickListener {
                val action = ScanDetailFragmentDirections
                    .actionScanDetailFragmentToFertilizerFragment(scan.predictedClass)
                findNavController().navigate(action)
            }

            binding.btnDeleteScan.setOnClickListener {
                lifecycleScope.launch {
                    repository.deleteById(scan.id)
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
