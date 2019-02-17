#
# Copyright (C) 2023 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_o.mk)

# Get non-open-source specific aspects
$(call inherit-product, vendor/xiaomi/perseus/perseus-vendor.mk)

# Boot animation
TARGET_SCREEN_HEIGHT := 2160
TARGET_SCREEN_WIDTH := 1080

# Overlays
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay

# Screen density
PRODUCT_AAPT_CONFIG := normal
PRODUCT_AAPT_PREF_CONFIG := xxhdpi

# Inherit from sdm845-common
$(call inherit-product, device/xiaomi/sdm845-common/sdm845.mk)

# Device fstab
PRODUCT_PACKAGES += \
    fstab.qcom

# Input
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/keylayout/NVTCapacitiveTouchScreen.kl:system/usr/keylayout/NVTCapacitiveTouchScreen.kl \
    $(LOCAL_PATH)/keylayout/fpcsensor.kl:system/usr/keylayout/fpcsensor.kl \
    $(LOCAL_PATH)/keylayout/gpio-keys.kl:system/usr/keylayout/gpio-keys.kl
