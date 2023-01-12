#
# Copyright (C) 2023 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

$(call inherit-product, device/xiaomi/shark/device.mk)

# Inherit some common Lineage stuff.
$(call inherit-product, vendor/lineage/config/common_full_phone.mk)

# Device identifier. This must come after all inclusions.
PRODUCT_NAME := lineage_shark
PRODUCT_DEVICE := shark
PRODUCT_BRAND := Xiaomi
PRODUCT_MODEL := Black Shark
PRODUCT_MANUFACTURER := Xiaomi

PRODUCT_SYSTEM_NAME := shark

BUILD_FINGERPRINT := "blackshark/SKR-H0/shark:10/G66X2010300OS00MQ1/V11.0.4.0.JOYUI:user/release-keys"

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRIVATE_BUILD_DESC="shark-user 10 G66X2010300OS00MQ1 V11.0.4.0.JOYUI release-keys" \
    TARGET_PRODUCT="shark"

PRODUCT_GMS_CLIENTID_BASE := android-xiaomi
