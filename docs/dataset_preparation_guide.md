# PechayDetect — Dataset Preparation Guide

## Overview

This guide explains how to prepare your image dataset for training the MobileNetV2 and MobileNetV3Large models. Follow every step carefully to ensure the training notebooks run without errors.

---

## Required Folder Structure

Your dataset must follow this exact structure:

```
Dataset/
├── train/
│   ├── Healthy/
│   │   ├── image_001.jpg
│   │   ├── image_002.jpg
│   │   └── ...
│   ├── Nitrogen/
│   │   └── ...
│   ├── Phosphorus/
│   │   └── ...
│   ├── Potassium/
│   │   └── ...
│   └── Unknown/          ← Optional (recommended for OOD rejection)
│       └── ...
├── validation/
│   ├── Healthy/
│   ├── Nitrogen/
│   ├── Phosphorus/
│   ├── Potassium/
│   └── Unknown/          ← Optional
└── test/
    ├── Healthy/
    ├── Nitrogen/
    ├── Phosphorus/
    ├── Potassium/
    └── Unknown/          ← Optional
```

**Folder names are case-sensitive.** They must exactly match the `CLASS_NAMES` list in the training notebooks:

```python
CLASS_NAMES = ['Healthy', 'Nitrogen', 'Phosphorus', 'Potassium']
```

If you add the `Unknown` class:

```python
CLASS_NAMES = ['Healthy', 'Nitrogen', 'Phosphorus', 'Potassium', 'Unknown']
```

---

## Accepted Image Formats

The training pipeline accepts the following formats:

- `.jpg` / `.jpeg`
- `.png`
- `.bmp`
- `.tiff`
- `.webp`

**Recommended format:** JPEG (`.jpg`) for smallest file size. PNG is acceptable for lossless quality.

---

## Recommended Dataset Split

| Split | Purpose | Recommended proportion |
|---|---|---|
| `train/` | Model training | 80% of total images |
| `validation/` | Hyperparameter tuning, early stopping | 10–20% of total images |
| `test/` | Final evaluation only (never used during training) | 10–20% of total images |

**The test set must never be used during training or validation.** It is used only in the final evaluation section of each notebook.

### Splitting an Existing Dataset Manually

If you have a single folder per class, use the following Python script to split it:

```python
import os
import shutil
import random

SOURCE_DIR  = '/path/to/your/raw/images'   # Each subfolder = one class
OUTPUT_DIR  = '/path/to/Dataset'
TRAIN_RATIO = 0.80
VAL_RATIO   = 0.10
TEST_RATIO  = 0.10
SEED        = 42

random.seed(SEED)

for class_name in os.listdir(SOURCE_DIR):
    class_path = os.path.join(SOURCE_DIR, class_name)
    if not os.path.isdir(class_path):
        continue

    images = [f for f in os.listdir(class_path)
              if f.lower().endswith(('.jpg', '.jpeg', '.png', '.bmp'))]
    random.shuffle(images)

    n_total = len(images)
    n_train = int(n_total * TRAIN_RATIO)
    n_val   = int(n_total * VAL_RATIO)

    splits = {
        'train':      images[:n_train],
        'validation': images[n_train:n_train + n_val],
        'test':       images[n_train + n_val:],
    }

    for split_name, split_images in splits.items():
        dest = os.path.join(OUTPUT_DIR, split_name, class_name)
        os.makedirs(dest, exist_ok=True)
        for img_file in split_images:
            shutil.copy2(
                os.path.join(class_path, img_file),
                os.path.join(dest, img_file)
            )
        print(f'{class_name}/{split_name}: {len(split_images)} images')

print('Dataset split complete.')
```

---

## Image Quality Guidelines

### What to Capture

- Photograph individual Pechay leaves, or leaves still attached to the plant.
- Fill most of the frame with the leaf.
- Use consistent, natural lighting when possible (outdoor shade or diffused sunlight).
- Take photos from multiple angles: top-down, slight angle.

### What to Avoid

- Blurry or heavily shadowed images.
- Heavily wet leaves where water droplets obscure symptoms.
- Images where the leaf occupies less than 30% of the frame.
- Duplicate or near-identical images (reduces dataset diversity).

### Minimum Recommended Images per Class

| Scenario | Min images per class |
|---|---|
| Proof-of-concept | 50–100 |
| Thesis-quality results | 200–500 |
| Publication-quality results | 500+ |

---

## Class Definitions

The notebooks currently support four deficiency classes. Ensure your images are correctly labeled:

| Folder Name | Description |
|---|---|
| `Healthy` | Pechay leaves with no visible nutrient deficiency symptoms |
| `Nitrogen` | Leaves showing nitrogen deficiency symptoms |
| `Phosphorus` | Leaves showing phosphorus deficiency symptoms |
| `Potassium` | Leaves showing potassium deficiency symptoms |

> **Important:** Only use images you are confident are correctly diagnosed. Mislabeled images directly reduce model accuracy.

---

## Optional: Unknown / Not-Pechay Class

Adding an `Unknown` class significantly improves the model's ability to reject non-Pechay images (out-of-distribution detection).

**What to put in the `Unknown` folder:**

- Other leaf types (mango, banana, tomato, etc.)
- Non-leaf objects (soil, hands, tools)
- Low-quality or irrelevant images

**Recommended:** 100–300 images in the `Unknown` class. Class weighting in the training notebooks handles imbalance automatically.

To enable 5-class training, update this line in both notebooks:

```python
CLASS_NAMES = ['Healthy', 'Nitrogen', 'Phosphorus', 'Potassium', 'Unknown']
```

---

## Uploading to Google Colab

### Option 1: Upload to Colab VM (temporary)

```python
from google.colab import files
uploaded = files.upload()   # Upload Dataset.zip

import zipfile
with zipfile.ZipFile('Dataset.zip', 'r') as z:
    z.extractall('/content/')
```

### Option 2: Google Drive (persistent — recommended)

1. Upload your `Dataset/` folder to Google Drive.
2. Uncomment these lines in the Configuration section of each notebook:

```python
from google.colab import drive
drive.mount('/content/drive')
DATASET_ROOT = '/content/drive/MyDrive/PechayDetect/Dataset'
```

---

## Verifying Your Dataset

The training notebooks include a `verify_dataset_structure()` function that automatically checks:

- All required directories exist.
- All class folders exist inside each split.
- Image counts per class are reported.
- An error is raised if any folder is missing or empty.

Run the **Section 3** cell in either notebook to verify before training begins.

---

## Adding More Deficiency Classes

The notebooks are designed to support additional classes. To add a new class (e.g., `Calcium`):

1. Create `Dataset/train/Calcium/`, `Dataset/validation/Calcium/`, `Dataset/test/Calcium/`.
2. Add images to each split folder.
3. Update `CLASS_NAMES` in both notebooks:

```python
CLASS_NAMES = ['Healthy', 'Nitrogen', 'Phosphorus', 'Potassium', 'Calcium']
```

4. Retrain both models from scratch for valid comparison.

No other code changes are required — the architecture, loss function, and evaluation code all adapt automatically to `NUM_CLASSES = len(CLASS_NAMES)`.
