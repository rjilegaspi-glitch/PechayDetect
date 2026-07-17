package com.jp.pechaydetect.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jp.pechaydetect.databinding.FragmentAnalysisBinding

class AnalysisFragment : Fragment() {

    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!
    private val args: AnalysisFragmentArgs by navArgs()
    private val viewModel: AnalysisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AnalysisViewModel.AnalysisState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvAnalysisStatus.text = "Analyzing leaf…"
                }
                is AnalysisViewModel.AnalysisState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val action = AnalysisFragmentDirections
                        .actionAnalysisFragmentToDiagnosisResultFragment(
                            scanId = state.scanId,
                            imageUri = args.imageUri,
                            predictedClass = state.result.predictedClass,
                            confidence = state.result.confidence,
                            isConfident = state.result.isConfident
                        )
                    findNavController().navigate(action)
                }
                is AnalysisViewModel.AnalysisState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvAnalysisStatus.text = "Analysis failed"
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
            }
        }

        // Trigger analysis only once
        if (viewModel.state.value is AnalysisViewModel.AnalysisState.Loading) {
            viewModel.analyze(args.imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
