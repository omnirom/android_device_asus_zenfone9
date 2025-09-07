# Copyright (C) 2016 The CyanogenMod Project
# Copyright (C) 2019 The OmniRom Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#
# This file is the build configuration for a full Android
# build for grouper hardware. This cleanly combines a set of
# device-specific aspects (drivers) with a device-agnostic
# product configuration (apps).
#
$(call inherit-product, vendor/asus/zenfone9/zenfone9-vendor.mk)

# fusefs / disable sdcardfs usage
$(call inherit-product, $(SRC_TARGET_DIR)/product/emulated_storage.mk)

# Enable updating of APEXes
$(call inherit-product, $(SRC_TARGET_DIR)/product/updatable_apex.mk)

# Prebuilt Kernel Binary
TARGET_KERNEL_VERSION := 5.10
TARGET_KERNEL_DIR ?= device/asus/zenfone9-kernel
TARGET_PREBUILT_KERNEL := device/asus/zenfone9-kernel/Image
LOCAL_KERNEL := $(TARGET_KERNEL_DIR)/Image
PRODUCT_COPY_FILES += \
    $(LOCAL_KERNEL):kernel

# Prebuilt Kernel Headers
TARGET_BOARD_KERNEL_HEADERS ?= device/asus/zenfone9-kernel/kernel-headers

# Overlays
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay \
    vendor/omni/overlay/CarrierConfig

PRODUCT_PACKAGES += \
    FrameworksResOverlay \
    FrameworksResVendorOverlay \
    OmniRomResInternalOverlay \
    SettingsOverlay \
    SettingsProviderOverlay \
    SystemUIOverlay \
    TeleServiceOverlay \
    TetheringConfigOverlay \
    WifiOverlay

# A/B
AB_OTA_UPDATER := true
ENABLE_VIRTUAL_AB := true
$(call inherit-product, $(SRC_TARGET_DIR)/product/virtual_ab_ota/launch_with_vendor_ramdisk.mk)

AB_OTA_POSTINSTALL_CONFIG += \
    RUN_POSTINSTALL_system=true \
    POSTINSTALL_PATH_system=system/bin/omnipreopt_script \
    FILESYSTEM_TYPE_system=ext4 \
    POSTINSTALL_OPTIONAL_system=true

AB_OTA_POSTINSTALL_CONFIG += \
    RUN_POSTINSTALL_vendor=true \
    POSTINSTALL_PATH_vendor=bin/checkpoint_gc \
    FILESYSTEM_TYPE_vendor=ext4 \
    POSTINSTALL_OPTIONAL_vendor=true

PRODUCT_PACKAGES += \
    checkpoint_gc \
    omnipreopt_script

# Adreno
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.vulkan.version-1_1.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.vulkan.version-1_1.xml

# Api
BOARD_SHIPPING_API_LEVEL := 31
PRODUCT_SHIPPING_API_LEVEL := $(BOARD_SHIPPING_API_LEVEL)

# Atrace
PRODUCT_PACKAGES += \
    android.hardware.atrace@1.0-service

# Audio
$(call soong_config_set,android_hardware_audio,run_64bit,true)

PRODUCT_PACKAGES += \
    android.hardware.audio@7.0-impl \
    android.hardware.audio.effect@7.0-impl \
    android.hardware.audio.service \
    android.media.audio.common.types-V1-cpp \
    libhapticgenerator

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/audio/audio_policy_configuration.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/audio_policy_configuration.xml \
    $(LOCAL_PATH)/audio/audio_policy_volumes.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/DAVINCI/audio_policy_volumes_AI2202.xml \
    $(LOCAL_PATH)/audio/audio_effects.conf:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/audio_effects.conf \
    $(LOCAL_PATH)/audio/audio_effects.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/audio_effects.xml \
    $(LOCAL_PATH)/audio/bluetooth_hearing_aid_audio_policy_configuration.xml:$(TARGET_COPY_OUT_VENDOR)/etc/bluetooth_hearing_aid_audio_policy_configuration.xml

# Biometric
PRODUCT_PACKAGES += \
    android.hardware.biometrics.fingerprint@2.1-service

# Boot control
PRODUCT_PACKAGES += \
    android.hardware.boot@1.2-impl-qti \
    android.hardware.boot@1.2-impl-qti.recovery \
    android.hardware.boot@1.2-service

PRODUCT_PACKAGES_DEBUG += \
    bootctl

# Charger images
PRODUCT_PACKAGES += \
    omni_charger_res_images \
    animation.txt \
    font_charger.png

# Dalvik
$(call inherit-product, frameworks/native/build/phone-xhdpi-6144-dalvik-heap.mk)

# DeviceParts
PRODUCT_PACKAGES += \
    DeviceParts

# Display
PRODUCT_PACKAGES += \
    libion \
    libtinyxml2 \
    vendor.qti.hardware.memtrack-service

-include hardware/qcom-caf/sm8450/display/config/display-board.mk
-include hardware/qcom-caf/sm8450/display/config/display-product.mk

# DRM
PRODUCT_PACKAGES += \
    android.hardware.drm-service.clearkey

# fastbootd
PRODUCT_PACKAGES += \
    android.hardware.fastboot@1.1-impl-mock \
    fastbootd

# Health
PRODUCT_PACKAGES += \
    android.hardware.health@2.1-impl-qti \
    android.hardware.health@2.1-service

# Health for charing control
PRODUCT_PACKAGES += \
    vendor.lineage.health-service.default

$(call soong_config_set,lineage_health,charging_control_charging_path,/sys/class/asuslib/charging_suspend_en)
$(call soong_config_set,lineage_health,charging_control_charging_enabled,0)
$(call soong_config_set,lineage_health,charging_control_charging_disabled,1)

# Init
PRODUCT_PACKAGES += \
    libinit_zenfone9

# Input
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/keylayout/fts_ts.idc:system/usr/idc/fts_ts.idc \
    $(LOCAL_PATH)/keylayout/fts_ts.kl:system/usr/keylayout/fts_ts.kl \
    $(LOCAL_PATH)/keylayout/goodixfp.kl:system/usr/keylayout/goodixfp.kl \
    $(LOCAL_PATH)/keylayout/i-rocks_Bluetooth_Keyboard.kl:system/usr/keylayout/i-rocks_Bluetooth_Keyboard.kl

# Lights
PRODUCT_PACKAGES += \
    vendor.qti.hardware.lights.service

# Live Wallpapers
PRODUCT_PACKAGES += \
    LiveWallpapers \
    LiveWallpapersPicker \
    VisualizationWallpapers \
    librs_jni

# Mount Point symlink
PRODUCT_PACKAGES += \
    mnt_point_factory_symlink \
    vendor_asusfw_mountpoint \
    vendor_bt_firmware_mountpoint \
    vendor_asus_cam_almalence \
    vendor_asus_cam_ditscidgen \
    vendor_xrom_mountpoint \
    vendor_firmware_mnt_mountpoint

# NFC
PRODUCT_PACKAGES += \
    Tag \
    SecureElement \
    com.android.nfc_extras

# Perf
PRODUCT_PACKAGES += \
    vendor.qti.hardware.perf@2.3

# Permisions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/handheld_core_hardware.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/handheld_core_hardware.xml

# Platform
PRODUCT_BUILD_SUPER_PARTITION := false
PRODUCT_USE_DYNAMIC_PARTITIONS := true

# Power
PRODUCT_PACKAGES += \
    android.hardware.power-service-qti

# Prebuilt
PRODUCT_COPY_FILES += \
    $(call find-copy-subdir-files,*,device/asus/zenfone9/prebuilt/product,product) \
    $(call find-copy-subdir-files,*,device/asus/zenfone9/prebuilt/root,recovery/root) \
    $(call find-copy-subdir-files,*,device/asus/zenfone9/prebuilt/system,system) \
    $(call find-copy-subdir-files,*,device/asus/zenfone9/prebuilt/system_ext,system_ext) \
    $(call find-copy-subdir-files,*,device/asus/zenfone9/prebuilt/vendor,vendor)

# Properties
BOARD_PROPERTY_OVERRIDES_SPLIT_ENABLED := true

# QMI
PRODUCT_PACKAGES += \
    libqti_vndfwk_detect_vendor \
    libvndfwk_detect_jni.qti_vendor

# Ramdisk
$(call inherit-product, $(SRC_TARGET_DIR)/product/generic_ramdisk.mk)

PRODUCT_PACKAGES += \
    tune2fs.vendor_ramdisk \
    resize2fs.vendor_ramdisk

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/ramdisk/fstab.qcom:$(TARGET_COPY_OUT_VENDOR_RAMDISK)/first_stage_ramdisk/fstab.qcom \
    $(LOCAL_PATH)/ramdisk/fstab.qcom:$(TARGET_COPY_OUT_VENDOR)/etc/fstab.qcom

# Soong namespaces
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH)

# Sensors
PRODUCT_PACKAGES += \
    android.hardware.sensors-service.multihal

# Systemhelper
PRODUCT_PACKAGES += \
    vendor.qti.hardware.systemhelper@1.0

# Telephony
PRODUCT_PACKAGES += \
    extphonelib \
    extphonelib.xml \
    extphonelib-product \
    extphonelib_product.xml \
    ims-ext-common \
    ims_ext_common.xml \
    qti-telephony-hidl-wrapper \
    qti_telephony_hidl_wrapper.xml \
    qti-telephony-hidl-wrapper-prd \
    qti_telephony_hidl_wrapper_prd.xml \
    qti-telephony-utils \
    qti_telephony_utils.xml \
    qti-telephony-utils-prd \
    qti_telephony_utils_prd.xml \
    tcmiface

# Update engine
PRODUCT_PACKAGES += \
    otapreopt_script \
    update_engine \
    update_engine_sideload \
    update_verifier

PRODUCT_HOST_PACKAGES += \
    brillo_update_payload

PRODUCT_PACKAGES_DEBUG += \
    update_engine_client

# USB
TARGET_HAS_DIAG_ROUTER := true
$(call inherit-product, vendor/qcom/opensource/usb/vendor_product.mk)

# Vendor service manager
PRODUCT_PACKAGES += \
    vndservicemanager

# Vibrator
PRODUCT_PACKAGES += \
    vendor.qti.hardware.vibrator.service

# Wifi
PRODUCT_PACKAGES += \
    android.hardware.wifi-service \
    hostapd \
    libwifi-hal-qcom \
    wpa_supplicant \
    wpa_supplicant.conf

# WiFi firmware symlinks
PRODUCT_PACKAGES += \
    firmware_wlan_mac.bin_symlink \
    firmware_WCNSS_qcom_cfg.ini_symlink
