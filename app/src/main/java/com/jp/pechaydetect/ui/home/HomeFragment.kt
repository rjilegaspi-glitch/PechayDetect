package com.jp.pechaydetect.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jp.pechaydetect.R
import com.jp.pechaydetect.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.totalScans.observe(viewLifecycleOwner) { count ->
            binding.tvTotalScans.text = count.toString()
        }

        viewModel.recentScans.observe(viewLifecycleOwner) { scans ->
            binding.tvRecentActivity.text = if (scans.isEmpty()) {
                getString(R.string.no_scans_yet)
            } else {
                val last = scans.first()
                getString(R.string.last_scan_result, last.predictedClass)
            }
        }

        binding.btnScanLeaf.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_cameraFragment)
        }

        binding.btnViewHistory.setOnClickListener {
            findNavController().navigate(R.id.scanHistoryFragment)
        }

        binding.btnNutrientLibrary.setOnClickListener {
            findNavController().navigate(R.id.nutrientLibraryFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
