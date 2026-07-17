package com.jp.pechaydetect.ui.preview

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jp.pechaydetect.databinding.FragmentImagePreviewBinding

class ImagePreviewFragment : Fragment() {

    private var _binding: FragmentImagePreviewBinding? = null
    private val binding get() = _binding!!
    private val args: ImagePreviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUri = Uri.parse(args.imageUri)

        Glide.with(this)
            .load(imageUri)
            .centerCrop()
            .into(binding.imgPreview)

        binding.btnAnalyze.setOnClickListener {
            val action = ImagePreviewFragmentDirections
                .actionImagePreviewFragmentToAnalysisFragment(args.imageUri)
            findNavController().navigate(action)
        }

        binding.btnRetake.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
