package com.jp.pechaydetect.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jp.pechaydetect.MainActivity
import com.jp.pechaydetect.R
import com.jp.pechaydetect.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    companion object {
        private const val PREF_NAME = "pechay_prefs"
        private const val KEY_ONBOARDING_DONE = "onboarding_done"
    }

    private lateinit var binding: ActivityOnboardingBinding

    private val pages = listOf(
        OnboardingPage(
            R.drawable.ic_onboarding_scan,
            "Detect Nutrient Deficiencies",
            "Point your camera at a pechay leaf. Our AI instantly identifies Nitrogen, " +
                    "Phosphorus, and Potassium deficiencies."
        ),
        OnboardingPage(
            R.drawable.ic_onboarding_fertilizer,
            "Get DA-Backed Recommendations",
            "Receive fertilizer recommendations aligned with Philippine Department of " +
                    "Agriculture (DA) guidelines — tailored to your detected deficiency."
        ),
        OnboardingPage(
            R.drawable.ic_onboarding_history,
            "Track Your Scan History",
            "Every scan is saved locally. Monitor your crop's nutrition over time and " +
                    "take timely corrective action."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnboardingAdapter(pages)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabIndicator, binding.viewPager) { _, _ -> }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val isLastPage = position == pages.lastIndex
                binding.btnNext.text = if (isLastPage) "Get Started" else "Next"
                binding.btnSkip.visibility = if (isLastPage) View.GONE else View.VISIBLE
            }
        })

        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < pages.lastIndex) {
                binding.viewPager.currentItem = current + 1
            } else {
                finishOnboarding()
            }
        }

        binding.btnSkip.setOnClickListener { finishOnboarding() }
    }

    private fun finishOnboarding() {
        getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ONBOARDING_DONE, true)
            .apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

data class OnboardingPage(
    val illustrationRes: Int,
    val title: String,
    val description: String
)

class OnboardingAdapter(private val pages: List<OnboardingPage>) :
    RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img_onboarding)
        val title: TextView = itemView.findViewById(R.id.tv_onboarding_title)
        val description: TextView = itemView.findViewById(R.id.tv_onboarding_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_page, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = pages[position]
        holder.image.setImageResource(page.illustrationRes)
        holder.title.text = page.title
        holder.description.text = page.description
    }

    override fun getItemCount(): Int = pages.size
}
