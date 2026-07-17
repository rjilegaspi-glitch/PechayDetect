# PechayDetect — Android App

## Overview
Android application for detecting nutrient deficiencies in pechay (*Brassica rapa* subsp. *chinensis*) leaves using on-device AI inference.

## App Configuration

| Parameter | Value | Rationale |
|---|---|---|
| **Package name** | `com.jp.pechaydetect` | Thesis application identifier |
| **Min SDK** | **API 24** (Android 7.0 Nougat) | TFLite 2.x requires API 21+; CameraX requires API 21+. API 24 adds headroom for NNAPI/GPU delegate and covers ~95% of active Android devices (as of 2024), ensuring no runtime conflicts between the TFLite model and platform APIs |
| **Target SDK** | API 34 (Android 14) | |
| **Compile SDK** | API 34 | |

## Deficiency Classes Detected

| Class | Symptoms | Status Color |
|---|---|---|
| **Healthy** | Uniform dark green leaves, vigorous growth | Green `#2E7D32` |
| **Nitrogen** | Uniform yellowing of older leaves, stunted growth | Orange `#F57C00` |
| **Phosphorus** | Purple/reddish underside discoloration, dark green upper surface | Purple `#7B1FA2` |
| **Potassium** | Marginal leaf scorch, brown leaf edges | Red `#C62828` |

OOD threshold: **0.70** — predictions below 70% confidence are flagged as uncertain.

## Fertilizer Recommendations

All fertilizer recommendations are sourced from the **Philippine Department of Agriculture (DA)**:
- Bureau of Plant Industry (BPI), *Techno-Guide on Pechay Production*
- DA Fertilizer and Pesticide Authority (FPA), *Philippine Fertilizer Guide*
- BARC-PCARRD, *Soil Fertility and Crop Nutrition Research Recommendations*

Recommendations cover both **inorganic** (Urea, MOP, TSP, 14-14-14) and **organic** (vermicompost, fish amino acid, wood ash) fertilizer options.

## App Screens

1. **Splash Screen** — Animated logo with tagline
2. **Onboarding** — 3-page introduction (first launch only)
3. **Home Dashboard** — Quick scan button, stats, quick links
4. **Camera Screen** — Live preview with leaf framing guide; supports front/back camera flip
5. **Image Preview** — Confirm captured or gallery image before analysis
6. **AI Analysis** — Loading screen while TFLite inference runs
7. **Diagnosis Result** — Predicted class, confidence score, quick action
8. **Fertilizer Recommender** — Full DA-backed fertilizer steps + cultural practices
9. **Nutrient Library** — Educational content on N, P, K deficiencies
10. **Scan History** — Chronological list of all past scans (stored in Room DB)
11. **Scan Details** — Detail view for a single past scan with delete option

## TFLite Model

Place your trained model file at:
```
app/src/main/assets/pechay_model.tflite
```

**Expected model format:**
- **Input:** `[1, 224, 224, 3]` float32 tensor
- **Preprocessing:** `(pixel / 127.5) − 1.0` (matches MobileNetV2 and MobileNetV3Large)
- **Output:** `[1, 4]` float32 softmax probabilities
- **Classes (order):** `Healthy`, `Nitrogen`, `Phosphorus`, `Potassium`
- **Recommended format:** Float16 quantized (best balance of size and accuracy)

The app automatically tries GPU delegate first; falls back to CPU if unsupported.

## Project Structure

```
app/src/main/
├── assets/
│   └── pechay_model.tflite          ← Place trained model here
├── java/com/jp/pechaydetect/
│   ├── PechayDetectApp.kt           ← Application class
│   ├── MainActivity.kt              ← Navigation host
│   ├── data/
│   │   ├── model/                   ← ScanResult, FertilizerRecommendation, NutrientInfo
│   │   ├── database/                ← Room database + DAO
│   │   ├── repository/              ← ScanRepository
│   │   └── fertilizer/              ← FertilizerData (DA recommendations)
│   ├── ml/
│   │   └── PechayClassifier.kt      ← TFLite inference wrapper
│   └── ui/
│       ├── splash/                  ← SplashActivity
│       ├── onboarding/              ← OnboardingActivity
│       ├── home/                    ← HomeFragment + ViewModel
│       ├── camera/                  ← CameraFragment (CameraX)
│       ├── preview/                 ← ImagePreviewFragment
│       ├── analysis/                ← AnalysisFragment + ViewModel
│       ├── result/                  ← DiagnosisResultFragment
│       ├── fertilizer/              ← FertilizerFragment
│       ├── library/                 ← NutrientLibraryFragment + Adapter
│       ├── history/                 ← ScanHistoryFragment + ViewModel + Adapter
│       └── historydetail/           ← ScanDetailFragment
└── res/
    ├── navigation/nav_graph.xml     ← Navigation component graph
    ├── layout/                      ← XML layouts for all screens
    ├── values/                      ← strings, colors, themes
    ├── drawable/                    ← Vector icons + drawables
    └── anim/                        ← Navigation transition animations
```

## Building the App

1. Open the `PechayDetect` root folder in Android Studio (Hedgehog or later)
2. Place `pechay_model.tflite` in `app/src/main/assets/`
3. Click **Build → Make Project** (or `./gradlew assembleDebug`)
4. Install on a device running Android 7.0+ (API 24+)

## Dependencies

| Library | Version | Purpose |
|---|---|---|
| TensorFlow Lite | 2.14.0 | On-device ML inference |
| TFLite GPU Delegate | 2.14.0 | GPU acceleration |
| CameraX | 1.3.4 | Camera preview and capture |
| Room | 2.6.1 | Local scan history database |
| Navigation Component | 2.7.7 | Fragment navigation |
| Glide | 4.16.0 | Image loading |
| Material 3 | 1.12.0 | UI components |
