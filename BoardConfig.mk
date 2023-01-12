#
# Copyright (C) 2023 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from sdm845-common
include device/xiaomi/sdm845-common/BoardConfigCommon.mk

BUILD_BROKEN_DUP_RULES := true

DEVICE_PATH := device/xiaomi/shark

# Assert
TARGET_OTA_ASSERT_DEVICE := shark

# Kernel
BOARD_KERNEL_SEPARATED_DTBO := true
TARGET_KERNEL_CONFIG += vendor/xiaomi/shark.config

# Inherit from the proprietary version
include vendor/xiaomi/shark/BoardConfigVendor.mk
