package com.jp.pechaydetect.ui.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jp.pechaydetect.R
import com.jp.pechaydetect.data.model.ScanResult
import com.jp.pechaydetect.databinding.ItemScanHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScanHistoryAdapter(
    private val onItemClick: (ScanResult) -> Unit
) : ListAdapter<ScanResult, ScanHistoryAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ItemScanHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScanResult) {
            val dateFormat = SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(Date(item.timestamp))
            binding.tvPrediction.text = item.predictedClass
            binding.tvConfidence.text = itemView.context.getString(
                R.string.confidence_format, (item.confidence * 100).toInt()
            )

            val colorRes = when (item.predictedClass) {
                "Healthy"    -> R.color.status_healthy
                "Nitrogen"   -> R.color.status_nitrogen
                "Phosphorus" -> R.color.status_phosphorus
                "Potassium"  -> R.color.status_potassium
                else         -> R.color.status_unknown
            }
            binding.viewStatusDot.setBackgroundColor(
                itemView.context.getColor(colorRes)
            )

            Glide.with(itemView)
                .load(Uri.parse(item.imagePath))
                .centerCrop()
                .placeholder(R.drawable.ic_leaf_placeholder)
                .into(binding.imgThumbnail)

            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScanHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ScanResult>() {
            override fun areItemsTheSame(old: ScanResult, new: ScanResult) = old.id == new.id
            override fun areContentsTheSame(old: ScanResult, new: ScanResult) = old == new
        }
    }
}
