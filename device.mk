#
# Copyright (C) 2023 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_o.mk)

# Get non-open-source specific aspects
$(call inherit-product, vendor/xiaomi/perseus/perseus-vendor.mk)

# Audio
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/audio/audio_platform_info.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio_platform_info.xml \
    $(LOCAL_PATH)/audio/audio_policy_volumes.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio_policy_volumes.xml \
    $(LOCAL_PATH)/audio/default_volume_tables.xml:$(TARGET_COPY_OUT_VENDOR)/etc/default_volume_tables.xml \
    $(LOCAL_PATH)/audio/mixer_paths_tavil_dvt2.xml:$(TARGET_COPY_OUT_VENDOR)/etc/mixer_paths_tavil_dvt2.xml \
    $(LOCAL_PATH)/audio/mixer_paths_tavil_factory.xml:$(TARGET_COPY_OUT_VENDOR)/etc/mixer_paths_tavil_factory.xml \
    $(LOCAL_PATH)/audio/sound_trigger_mixer_paths_wcd9340.xml:$(TARGET_COPY_OUT_VENDOR)/etc/sound_trigger_mixer_paths_wcd9340.xml \
    $(LOCAL_PATH)/audio/sound_trigger_platform_info.xml:$(TARGET_COPY_OUT_VENDOR)/etc/sound_trigger_platform_info.xml

# Boot animation
TARGET_SCREEN_HEIGHT := 2160
TARGET_SCREEN_WIDTH := 1080

# Overlays
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.sensor.barometer.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.sensor.barometer.xml \
    frameworks/native/data/etc/android.hardware.sensor.hifi_sensors.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.sensor.hifi_sensors.xml

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
