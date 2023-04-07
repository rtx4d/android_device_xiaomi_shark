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

# HIDL
DEVICE_MANIFEST_FILE += $(DEVICE_PATH)/manifest.xml

# Kernel
BOARD_DTBOIMG_PARTITION_SIZE := 8388608
BOARD_KERNEL_SEPARATED_DTBO := true
TARGET_KERNEL_CONFIG := sdm845-perf_defconfig
TARGET_KERNEL_SOURCE := kernel/msm-4.9

# Partitions
BOARD_BUILD_SYSTEM_ROOT_IMAGE := true

# Recovery
BOARD_USES_RECOVERY_AS_BOOT := true
TARGET_RECOVERY_FSTAB := $(DEVICE_PATH)/rootdir/etc/fstab.qcom

# Inherit from the proprietary version
include vendor/xiaomi/shark/BoardConfigVendor.mk
