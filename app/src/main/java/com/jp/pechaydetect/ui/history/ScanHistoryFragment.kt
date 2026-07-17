package com.jp.pechaydetect.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jp.pechaydetect.databinding.FragmentScanHistoryBinding

class ScanHistoryFragment : Fragment() {

    private var _binding: FragmentScanHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScanHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ScanHistoryAdapter { scan ->
            val action = ScanHistoryFragmentDirections
                .actionScanHistoryFragmentToScanDetailFragment(scan.id)
            findNavController().navigate(action)
        }

        binding.recyclerHistory.adapter = adapter

        viewModel.allScans.observe(viewLifecycleOwner) { scans ->
            adapter.submitList(scans)
            binding.tvEmptyState.visibility = if (scans.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerHistory.visibility = if (scans.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
