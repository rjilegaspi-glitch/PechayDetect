# PechayDetect — Model Comparison Report Template

**Instructions:** After completing both notebooks, copy your results into this table.

---

## Experimental Setup

| Parameter | Value |
|---|---|
| Dataset | PechayDetect (Pechay nutrient deficiency) |
| Classes | Healthy, Nitrogen, Phosphorus, Potassium |
| Image size | 224 × 224 × 3 |
| Random seed | 42 |
| Training split | 80% |
| Validation split | 20% |
| Test set | Held-out (never used during training) |
| Training environment | Google Colab (GPU) |
| Python version | 3.10 |
| TensorFlow version | 2.13.0 |
| Augmentation | Horizontal+Vertical Flip, Rotation ±15°, Zoom ±10%, Brightness ±20%, Contrast ±10% |
| Label smoothing | 0.1 |
| Dropout | 0.3 |
| L2 regularization | 1e-4 |
| OOD threshold | 0.70 |

---

## Training Configuration

| Parameter | MobileNetV2 | MobileNetV3Large |
|---|---|---|
| Pretrained weights | ImageNet | ImageNet |
| Width multiplier (alpha) | 1.0 | 1.0 |
| Minimalistic | N/A | False |
| Preprocessing | (pixel / 127.5) − 1.0 | (pixel / 127.5) − 1.0 |
| Phase 1 epochs (max) | 20 | 20 |
| Phase 2 epochs (max) | 20 | 20 |
| Phase 3 epochs (max) | 15 | 15 |
| Phase 1 LR | 1e-3 | 1e-3 |
| Phase 2 LR | 1e-4 | 1e-4 |
| Phase 3 LR | 1e-5 | 1e-5 |
| LR schedule | Cosine decay + warmup | Cosine decay + warmup |
| Fine-tune from (Phase 2) | Layer 100 of 154 | Layer 200 of ~264 |
| Fine-tune from (Phase 3) | Layer 0 (all) | Layer 0 (all) |
| Batch size | 32 | 32 |

---

## Test Set Performance

| Metric | MobileNetV2 | MobileNetV3Large |
|---|---|---|
| **Accuracy** | ___ | ___ |
| **Accuracy (with TTA)** | ___ | ___ |
| **Precision (weighted)** | ___ | ___ |
| **Recall (weighted)** | ___ | ___ |
| **F1 Score (weighted)** | ___ | ___ |
| **Macro-average AUC** | ___ | ___ |
| **Calibration (ECE)** | ___ | ___ |

---

## Per-Class F1 Score

| Class | MobileNetV2 F1 | MobileNetV3Large F1 |
|---|---|---|
| Healthy | ___ | ___ |
| Nitrogen | ___ | ___ |
| Phosphorus | ___ | ___ |
| Potassium | ___ | ___ |
| **Macro avg** | ___ | ___ |
| **Weighted avg** | ___ | ___ |

---

## TFLite Model Sizes and Accuracy

| Format | MobileNetV2 KB | MobileNetV2 Acc | MobileNetV3 KB | MobileNetV3 Acc |
|---|---|---|---|---|
| Float32 | ___ | ___ | ___ | ___ |
| Float16 | ___ | ___ | ___ | ___ |
| Int8 | ___ | ___ | ___ | ___ |

---

## OOD Rejection (Confidence Threshold = 0.70)

| Metric | MobileNetV2 | MobileNetV3Large |
|---|---|---|
| Accepted predictions | ___ % | ___ % |
| Rejected as unknown | ___ % | ___ % |
| Accuracy on accepted | ___ | ___ |
| Mean entropy (correct) | ___ | ___ |
| Mean entropy (incorrect) | ___ | ___ |

---

## Architecture Comparison

| Aspect | MobileNetV2 | MobileNetV3Large |
|---|---|---|
| Paper | Sandler et al. (2018), arXiv:1801.04381 | Howard et al. (2019), arXiv:1905.02244 |
| Design method | Manual | Neural Architecture Search (NAS) + NetAdapt |
| Activation | ReLU6 | Hard-Swish + Hard-Sigmoid |
| Attention | None | Squeeze-and-Excitation (SE) |
| ImageNet top-1 (reported) | 71.8% | 75.2% |
| Parameters | ~3.4M | ~5.4M |
| Total layers (approx.) | 154 | 264 |

---

## Recommended Model for Android Deployment

Based on your results, record your recommendation here:

**Recommended model:** ___________________

**Justification:** _______________________

**TFLite format to deploy:** ___________________

---

## Notes and Observations

_Fill in after training completes._

---

## References

- Sandler, M., Howard, A., Zhu, M., Zhmoginov, A., & Chen, L. C. (2018). MobileNetV2: Inverted Residuals and Linear Bottlenecks. *CVPR 2018*. arXiv:1801.04381
- Howard, A., Sandler, M., Chu, G., Chen, L. C., Chen, B., Tan, M., ... & Adam, H. (2019). Searching for MobileNetV3. *ICCV 2019*. arXiv:1905.02244
- Guo, C., Pleiss, G., Sun, Y., & Weinberger, K. Q. (2017). On Calibration of Modern Neural Networks. *ICML 2017*.
- Selvaraju, R. R., Cogswell, M., Das, A., Vedantam, R., Parikh, D., & Batra, D. (2017). Grad-CAM: Visual Explanations from Deep Networks via Gradient-based Localization. *ICCV 2017*.
- Loshchilov, I., & Hutter, F. (2017). SGDR: Stochastic Gradient Descent with Warm Restarts. *ICLR 2017*. arXiv:1608.03983
