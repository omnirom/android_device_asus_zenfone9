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
LOCAL_KERNEL := $(TARGET_KERNEL_DIR)/Image
PRODUCT_COPY_FILES += \
    $(LOCAL_KERNEL):kernel \

# Prebuilt Kernel Headers
PRODUCT_VENDOR_KERNEL_HEADERS ?= device/asus/zenfone9-kernel/kernel-headers

# Overlays
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay \
    vendor/omni/overlay/CarrierConfig

PRODUCT_PACKAGES += \
    aptxalsOverlay \
    FrameworksResOverlay \
    FrameworksResVendor \
    SettingsProviderOverlay \
    SystemUIOverlay \
    TeleServiceOverlay \
    TetheringConfigOverlay \
    WifiOverlay

# A/B
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

# ANT+
PRODUCT_PACKAGES += \
    AntHalService

# Api
BOARD_API_LEVEL := 31
BOARD_SHIPPING_API_LEVEL := $(BOARD_API_LEVEL)
PRODUCT_SHIPPING_API_LEVEL := $(BOARD_API_LEVEL)

# Atrace
PRODUCT_PACKAGES += \
    android.hardware.atrace@1.0-service

# Audio
PRODUCT_PACKAGES += \
    android.hardware.audio@7.0-impl \
    android.hardware.audio.effect@7.0-impl \
    android.hardware.audio.service

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/audio/audio_policy_configuration.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/audio_policy_configuration.xml \
    $(LOCAL_PATH)/audio/audio_policy_configuration.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/DAVINCI/audio_policy_configuration_AI2202.xml \
    $(LOCAL_PATH)/audio/audio_policy_volumes.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/audio_policy_volumes.xml \
    $(LOCAL_PATH)/audio/audio_policy_volumes.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/DAVINCI/audio_policy_volumes_AI2202.xml \
    $(LOCAL_PATH)/audio/audio_effects.conf:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/audio_effects.conf \
    $(LOCAL_PATH)/audio/audio_effects_AI2202.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/audio_effects.xml \
    $(LOCAL_PATH)/audio/default_volume_tables_AI2202.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/default_volume_tables.xml \
    $(LOCAL_PATH)/audio/resourcemanager_davinci.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/resourcemanager_davinci.xml \
    $(LOCAL_PATH)/audio/usecaseKvManager_davinci.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio/sku_cape/usecaseKvManager_davinci.xml \
    $(LOCAL_PATH)/audio/audio_effects_AI2202.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio_effects.xml \
    $(LOCAL_PATH)/audio/audio_policy_volumes.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio_policy_volumes.xml \
    $(LOCAL_PATH)/audio/bluetooth_qti_hearing_aid_audio_policy_configuration.xml:$(TARGET_COPY_OUT_VENDOR)/etc/bluetooth_qti_hearing_aid_audio_policy_configuration.xml

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
    android.hardware.graphics.common-V2-ndk_platform.vendor \
    libion \
    libtinyxml2 \
    lights.qcom

$(call inherit-product, vendor/qcom/opensource/display/config/display-product-vendor.mk)
$(call inherit-product, vendor/qcom/opensource/commonsys/display/config/display-product-commonsys.mk)
$(call inherit-product, vendor/qcom/opensource/commonsys-intf/display/config/display-interfaces-product.mk)
$(call inherit-product, vendor/qcom/opensource/commonsys-intf/display/config/display-product-system.mk)

PRODUCT_PACKAGES += \
    libtinyalsa

# DRM
PRODUCT_PACKAGES += \
    android.hardware.drm@1.4-service.clearkey

# fastbootd
PRODUCT_PACKAGES += \
    android.hardware.fastboot@1.1-impl-mock \
    fastbootd

# FM
PRODUCT_PACKAGES += \
    FM2 \
    libqcomfm_jni \
    qcom.fmradio

PRODUCT_BOOT_JARS += qcom.fmradio

# GPS
PRODUCT_PACKAGES += \
    android.hardware.gnss-V1-ndk_platform.vendor

# Health
PRODUCT_PACKAGES += \
    android.hardware.health@2.1-impl-qti \
    android.hardware.health@2.1-service

# HIDL
PRODUCT_PACKAGES += \
    android.hidl.base@1.0 \
    android.hidl.base@1.0.vendor \
    libhwbinder.vendor

# Identity
PRODUCT_PACKAGES += \
    android.hardware.identity-V3-ndk_platform.vendor

# Input
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/keylayout/fts_ts.idc:system/usr/idc/fts_ts.idc \
    $(LOCAL_PATH)/keylayout/fts_ts.kcm:system/usr/keychars/fts_ts.kcm \
    $(LOCAL_PATH)/keylayout/fts_ts.kl:system/usr/keylayout/fts_ts.kl \
    $(LOCAL_PATH)/keylayout/goodixfp.kl:system/usr/keylayout/goodixfp.kl \
    $(LOCAL_PATH)/keylayout/i-rocks_Bluetooth_Keyboard.kl:system/usr/keylayout/i-rocks_Bluetooth_Keyboard.kl

# Keymint
PRODUCT_PACKAGES += \
    android.hardware.security.keymint-V1-ndk_platform.vendor

# Lights
PRODUCT_PACKAGES += \
    android.hardware.lights-service.qti \
    android.hardware.light-V1-ndk_platform.vendor

# Live Wallpapers
PRODUCT_PACKAGES += \
    LiveWallpapers \
    LiveWallpapersPicker \
    VisualizationWallpapers \
    librs_jni

# Media
PRODUCT_PACKAGES += \
    libavservices_minijail \
    libavservices_minijail.vendor \
    libavservices_minijail_vendor \
    libOmxCore \
    libcodec2_hidl@1.0.vendor \
    libcodec2_vndk.vendor \
    libmm-omxcore \
    libstagefright_softomx.vendor \
    libstagefrighthw \
    libplatformconfig

$(call inherit-product, hardware/qcom-caf/sm8450/media/product.mk)

# NFC
PRODUCT_PACKAGES += \
    NfcNci \
    Tag \
    SecureElement \
    com.android.nfc_extras

# Perf
PRODUCT_PACKAGES += \
    vendor.qti.hardware.perf@2.3

# Platform
PRODUCT_AAPT_CONFIG := normal
PRODUCT_AAPT_PREF_CONFIG := xxhdpi
PRODUCT_BUILD_SUPER_PARTITION := false
PRODUCT_USE_DYNAMIC_PARTITIONS := true

# Power
PRODUCT_PACKAGES += \
    android.hardware.power@1.2.vendor \
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
    $(LOCAL_PATH) \
    vendor/qcom/opensource/display \
    vendor/qcom/opensource/commonsys/display \
    vendor/qcom/opensource/commonsys-intf/display

# Systemhelper
PRODUCT_PACKAGES += \
    vendor.qti.hardware.systemhelper@1.0

# Telephony
PRODUCT_PACKAGES += \
    ims-ext-common \
    ims_ext_common.xml \
    qti-telephony-hidl-wrapper \
    qti_telephony_hidl_wrapper.xml \
    qti-telephony-utils \
    qti_telephony_utils.xml \
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

# VNDK
PRODUCT_COPY_FILES += \
    prebuilts/vndk/v32/arm64/arch-arm64-armv8-a/shared/vndk-sp/libhidlbase.so:$(TARGET_COPY_OUT_VENDOR)/lib64/libhidlbase-v32.so

# Wifi
PRODUCT_PACKAGES += \
    android.hardware.wifi@1.0-service \
    hostapd \
    libwifi-hal-qcom \
    libwpa_client \
    wpa_supplicant \
    wpa_supplicant.conf

# Wifi Display
PRODUCT_PACKAGES += \
    libnl
