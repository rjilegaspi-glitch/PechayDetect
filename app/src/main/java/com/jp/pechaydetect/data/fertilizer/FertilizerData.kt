package com.jp.pechaydetect.data.fertilizer

import com.jp.pechaydetect.data.model.FertilizerRecommendation
import com.jp.pechaydetect.data.model.FertilizerStep

/**
 * Fertilizer recommendations for pechay (Brassica rapa subsp. chinensis) sourced from:
 * Philippine Department of Agriculture (DA) — Bureau of Plant Industry (BPI),
 * "Techno-Guide on Pechay Production" and DA Fertilizer and Pesticide Authority (FPA)
 * guidelines for vegetable crop nutrition.
 *
 * Source: Bureau of Plant Industry, Department of Agriculture,
 * Visayas Avenue, Diliman, Quezon City, Philippines 1101
 * Website: https://bpi.da.gov.ph
 */
object FertilizerData {

    /** OOD confidence threshold — mirrors the model's OOD threshold setting. */
    const val CONFIDENCE_THRESHOLD = 0.70f

    val recommendations: Map<String, FertilizerRecommendation> = mapOf(

        // ─── HEALTHY ─────────────────────────────────────────────────────────
        "Healthy" to FertilizerRecommendation(
            deficiencyClass = "Healthy",
            title = "Standard Fertilization Program",
            severity = "No deficiency detected",
            recommendations = listOf(
                FertilizerStep(
                    product = "Complete Fertilizer (14-14-14)",
                    rate = "10–15 g/sqm",
                    method = "Basal application — broadcast and incorporate into soil before transplanting",
                    timing = "At transplanting (Day 0)"
                ),
                FertilizerStep(
                    product = "Urea (46-0-0)",
                    rate = "5–7 g/sqm",
                    method = "Side dress — apply 3–5 cm from the base of plants, cover lightly with soil",
                    timing = "10–14 days after transplanting (1st top dress)"
                ),
                FertilizerStep(
                    product = "Urea (46-0-0)",
                    rate = "5–7 g/sqm",
                    method = "Side dress — apply 3–5 cm from the base of plants, cover lightly with soil",
                    timing = "21–28 days after transplanting (2nd top dress)"
                ),
                FertilizerStep(
                    product = "Vermicompost or Compost",
                    rate = "1–2 kg/sqm",
                    method = "Basal — incorporate thoroughly into soil 1–2 weeks before planting",
                    timing = "Before transplanting",
                    isOrganic = true
                )
            ),
            culturalPractices = listOf(
                "Maintain soil moisture through regular irrigation (avoid water stress)",
                "Apply mulch (rice straw, dried grass) at 5 cm thickness to conserve moisture",
                "Monitor for pest and disease weekly (aphids, flea beetles, downy mildew)",
                "Practice crop rotation — do not plant brassicas consecutively on the same plot",
                "Test soil pH every season; optimal range for pechay is 6.0–7.0",
                "Harvest 25–35 days after transplanting for best quality and yield"
            ),
            notes = "Healthy pechay requires balanced NPK nutrition. Avoid over-fertilization " +
                    "with nitrogen, as this promotes lush growth that is more susceptible to " +
                    "insect pests. Organic matter application improves soil structure and " +
                    "water-holding capacity.",
            daReference = "DA-BPI Techno-Guide on Pechay Production; FPA Fertilizer Use " +
                    "Recommendation for Leafy Vegetables"
        ),

        // ─── NITROGEN DEFICIENCY ─────────────────────────────────────────────
        "Nitrogen" to FertilizerRecommendation(
            deficiencyClass = "Nitrogen",
            title = "Nitrogen Deficiency Correction",
            severity = "Moderate to severe — act within 3–5 days",
            recommendations = listOf(
                FertilizerStep(
                    product = "Urea (46-0-0)",
                    rate = "5–10 g/sqm",
                    method = "Side dress — apply in a band 3–5 cm from the base of the plant; " +
                            "cover with soil and water immediately to prevent volatilization",
                    timing = "Apply immediately upon confirming deficiency; repeat after 7–10 days if symptoms persist"
                ),
                FertilizerStep(
                    product = "Ammonium Sulfate (21-0-0)",
                    rate = "10–20 g/sqm",
                    method = "Side dress — dissolve in water (1 tbsp per liter) and drench around root zone",
                    timing = "Apply immediately; preferred in alkaline soils (pH > 7.0) as it acidifies the root zone"
                ),
                FertilizerStep(
                    product = "Complete Fertilizer (14-14-14)",
                    rate = "15–20 g/sqm",
                    method = "Broadcast and incorporate into soil between plant rows",
                    timing = "If deficiency is detected at transplanting or before first top dress"
                ),
                FertilizerStep(
                    product = "Foliar Urea Solution (2%)",
                    rate = "20 g Urea dissolved in 1 liter water",
                    method = "Spray directly on leaves (abaxial and adaxial surface); apply in early morning or late afternoon",
                    timing = "Quick correction — apply every 5–7 days until leaf color normalizes",
                    isOrganic = false
                ),
                FertilizerStep(
                    product = "Fish Amino Acid (FAA) — Fermented Fish Extract",
                    rate = "2–3 ml per liter of water",
                    method = "Foliar spray — apply to leaves in early morning",
                    timing = "Weekly; as organic nitrogen supplement",
                    isOrganic = true
                )
            ),
            culturalPractices = listOf(
                "Identify root cause: waterlogging, soil compaction, or very sandy soil accelerate N loss",
                "Improve soil drainage if waterlogging is present — N leaches rapidly in saturated soils",
                "Add compost or vermicompost (2 kg/sqm) to improve N-holding capacity of sandy soils",
                "Avoid over-irrigation after nitrogen application to prevent leaching",
                "Remove severely affected (completely yellow) leaves to reduce disease entry points",
                "Monitor re-greening of leaves within 5–7 days after corrective application"
            ),
            notes = "Nitrogen deficiency in pechay appears as uniform yellowing (chlorosis) of " +
                    "older/lower leaves first, progressing upward. Stunted growth is also common. " +
                    "Urea is the most economical nitrogen source in the Philippines. Ammonium " +
                    "sulfate is recommended in alkaline soils. Always water after soil application " +
                    "to prevent urea burn and nitrogen loss through volatilization. " +
                    "Do not apply nitrogen within 2 weeks of harvest to avoid nitrate accumulation " +
                    "in edible leaves.",
            daReference = "DA-BPI Techno-Guide on Pechay Production; DA-FPA Fertilizer Use " +
                    "Recommendation for Leafy Vegetables; DA Regional Field Office Guidelines on " +
                    "Soil Fertility Management"
        ),

        // ─── PHOSPHORUS DEFICIENCY ───────────────────────────────────────────
        "Phosphorus" to FertilizerRecommendation(
            deficiencyClass = "Phosphorus",
            title = "Phosphorus Deficiency Correction",
            severity = "Moderate — address within 1 week",
            recommendations = listOf(
                FertilizerStep(
                    product = "Triple Superphosphate — TSP (0-46-0)",
                    rate = "5–8 g/sqm",
                    method = "Incorporate into soil 5–10 cm deep near the root zone; " +
                            "broadcast and mix thoroughly for even distribution",
                    timing = "Basal application before transplanting, or as corrective side dress"
                ),
                FertilizerStep(
                    product = "Complete Fertilizer (14-14-14)",
                    rate = "15–20 g/sqm",
                    method = "Broadcast and incorporate into soil; water after application",
                    timing = "Apply at transplanting or immediately when deficiency is detected"
                ),
                FertilizerStep(
                    product = "Monoammonium Phosphate — MAP (11-52-0)",
                    rate = "4–6 g/sqm",
                    method = "Dissolve in water and apply as root drench around plant base",
                    timing = "Immediate corrective application"
                ),
                FertilizerStep(
                    product = "Rock Phosphate",
                    rate = "20–30 g/sqm",
                    method = "Broadcast and incorporate deeply into soil; effective in acidic soils (pH < 6.5)",
                    timing = "Basal application 1–2 weeks before transplanting",
                    isOrganic = true
                ),
                FertilizerStep(
                    product = "Compost (well-decomposed)",
                    rate = "2–3 kg/sqm",
                    method = "Mix thoroughly into top 10–15 cm of soil",
                    timing = "Before planting; improves phosphorus availability through microbial activity",
                    isOrganic = true
                )
            ),
            culturalPractices = listOf(
                "Check soil pH — phosphorus availability is greatly reduced below pH 5.0 and above pH 7.5",
                "Apply agricultural lime (dolomite) at 100–200 g/sqm if soil pH is below 6.0",
                "Avoid applying phosphorus fertilizer near irrigation water to prevent runoff",
                "Inoculate with mycorrhizal fungi (available at DA-BPI offices) to enhance P uptake",
                "Reduce soil compaction through deep tillage (25–30 cm) to improve root exploration",
                "Rotate with legume crops (mungbean, cowpea) to improve soil phosphorus cycling"
            ),
            notes = "Phosphorus deficiency in pechay typically shows as purple or reddish " +
                    "discoloration on the underside of older leaves, with dark green upper surfaces. " +
                    "This is caused by accumulation of anthocyanin pigments when P is limited. " +
                    "Phosphorus is relatively immobile in soil — basal application is most effective. " +
                    "Cold, wet soils significantly reduce phosphorus uptake even when soil levels " +
                    "are adequate. Soil testing through DA-RFO laboratories is recommended " +
                    "before applying corrective fertilizers.",
            daReference = "DA-BPI Techno-Guide on Pechay Production; DA Soil Testing Laboratory " +
                    "Recommendations; Philippine Fertilizer Guide (2nd Ed.), BARC-PCARRD"
        ),

        // ─── POTASSIUM DEFICIENCY ────────────────────────────────────────────
        "Potassium" to FertilizerRecommendation(
            deficiencyClass = "Potassium",
            title = "Potassium Deficiency Correction",
            severity = "Moderate — address within 5–7 days",
            recommendations = listOf(
                FertilizerStep(
                    product = "Muriate of Potash — MOP (0-0-60)",
                    rate = "5–8 g/sqm",
                    method = "Side dress — apply in a band 5 cm from plant base; cover with soil and irrigate",
                    timing = "Apply when deficiency symptoms appear; may repeat after 10–14 days"
                ),
                FertilizerStep(
                    product = "Potassium Sulfate — SOP (0-0-50)",
                    rate = "6–10 g/sqm",
                    method = "Side dress or dissolve in water (fertigation)",
                    timing = "Preferred over MOP in high-chloride soils or where sulfur is also deficient"
                ),
                FertilizerStep(
                    product = "Complete Fertilizer (14-14-14)",
                    rate = "15–20 g/sqm",
                    method = "Broadcast and incorporate; provides balanced NPK correction",
                    timing = "Use when multiple deficiencies are suspected"
                ),
                FertilizerStep(
                    product = "Potassium Nitrate (13-0-46)",
                    rate = "5–7 g/sqm",
                    method = "Dissolve in water and apply as foliar spray or root drench",
                    timing = "Rapid correction — provides both K and N; apply within 48 hours of confirming deficiency"
                ),
                FertilizerStep(
                    product = "Wood Ash",
                    rate = "50–100 g/sqm",
                    method = "Broadcast on soil surface and water in; raises soil pH slightly",
                    timing = "Organic option; avoid in alkaline soils (pH > 7.0)",
                    isOrganic = true
                ),
                FertilizerStep(
                    product = "Banana Peel Extract (Fermented)",
                    rate = "5 ml per liter of water",
                    method = "Foliar spray — apply to undersides of leaves in early morning",
                    timing = "Weekly as organic potassium supplement",
                    isOrganic = true
                )
            ),
            culturalPractices = listOf(
                "Identify cause: sandy soils, high rainfall, and excessive irrigation leach K rapidly",
                "Improve soil organic matter content to increase potassium retention capacity",
                "Avoid excessive nitrogen application — high N:K ratio worsens K deficiency symptoms",
                "Ensure adequate irrigation — water-stressed plants cannot absorb K efficiently",
                "Mulch with banana leaves or rice straw to return potassium to the soil as it decomposes",
                "Test soil K levels at DA-RFO laboratories for precise corrective rates"
            ),
            notes = "Potassium deficiency in pechay appears as marginal leaf scorch (browning " +
                    "or necrosis along leaf edges) on older/lower leaves, which progresses inward " +
                    "and upward. Affected leaves may also show interveinal yellowing. " +
                    "Potassium is highly mobile in the plant — deficiency symptoms appear on older " +
                    "leaves first. MOP (Muriate of Potash) is the most economical potassium source " +
                    "in the Philippines. Use Potassium Sulfate (SOP) if the crop is sensitive to " +
                    "chloride or if sulfur deficiency co-occurs. Do not apply K within 1 week of " +
                    "harvest. For organic production, wood ash and compost are DA-approved sources.",
            daReference = "DA-BPI Techno-Guide on Pechay Production; DA Fertilizer and Pesticide " +
                    "Authority (FPA) Philippine Fertilizer Guide; BARC Soil Fertility and Crop " +
                    "Nutrition Research Recommendations"
        )
    )

    /**
     * Retrieve the recommendation for a given predicted class.
     * Returns null if the class is not recognized (OOD/Unknown).
     */
    fun getRecommendation(predictedClass: String): FertilizerRecommendation? =
        recommendations[predictedClass]

    /**
     * Returns a brief one-line action summary for use in notifications or list items.
     */
    fun getQuickAction(predictedClass: String): String = when (predictedClass) {
        "Healthy"    -> "Continue standard fertilization program"
        "Nitrogen"   -> "Apply Urea (46-0-0) at 5–10 g/sqm immediately"
        "Phosphorus" -> "Apply TSP (0-46-0) at 5–8 g/sqm as basal or corrective treatment"
        "Potassium"  -> "Apply MOP (0-0-60) at 5–8 g/sqm as side dressing"
        else         -> "Image is not clearly recognizable — retake with better lighting"
    }
}
