package com.jp.pechaydetect.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jp.pechaydetect.data.model.NutrientInfo
import com.jp.pechaydetect.databinding.ItemNutrientCardBinding

class NutrientLibraryAdapter :
    ListAdapter<NutrientInfo, NutrientLibraryAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ItemNutrientCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NutrientInfo) {
            binding.tvNutrientName.text = item.nutrientName
            binding.tvSymbol.text = item.symbol
            binding.tvRole.text = item.role
            binding.tvSymptoms.text = item.deficiencySymptoms
            binding.tvAffectedParts.text = item.affectedParts
            binding.ivNutrientIcon.setImageResource(item.colorIconRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNutrientCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<NutrientInfo>() {
            override fun areItemsTheSame(old: NutrientInfo, new: NutrientInfo) =
                old.nutrientName == new.nutrientName
            override fun areContentsTheSame(old: NutrientInfo, new: NutrientInfo) =
                old == new
        }
    }
}
