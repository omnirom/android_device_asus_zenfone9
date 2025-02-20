#!/usr/bin/env -S PYTHONPATH=../../../tools/extract-utils python3
#
# SPDX-FileCopyrightText: 2024 The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#

from extract_utils.fixups_blob import (
    blob_fixup,
    blob_fixups_user_type,
)
from extract_utils.fixups_lib import (
    lib_fixup_remove,
    lib_fixups,
    lib_fixups_user_type,
)
from extract_utils.main import (
    ExtractUtils,
    ExtractUtilsModule,
)

def lib_fixup_vendor_suffix(lib: str, partition: str, *args, **kwargs):
    return f'{lib}_{partition}' if partition == 'vendor' else None

lib_fixups: lib_fixups_user_type = {
    **lib_fixups,
    (
    ): lib_fixup_vendor_suffix,
    (
    ): lib_fixup_remove,
}

blob_fixups: blob_fixups_user_type = {
    ('system_ext/etc/permissions/vendor.qti.hardware.data.connection-V1.0-java.xml',
     'system_ext/etc/permissions/vendor.qti.hardware.data.connection-V1.1-java.xml'): blob_fixup()
        .regex_replace('system/product', 'system_ext')
        .regex_replace('xml version="2.0"', 'xml version="1.0"'),
     'vendor/bin/vendor.dpmd': blob_fixup()
         .add_needed('libhidlbase_shim.so'),
    ('vendor/bin/hw/android.hardware.security.keymint-service-qti',
     'vendor/lib64/libqtikeymint.so'): blob_fixup()
        .add_needed('android.hardware.security.rkp-V1-ndk.so'),
    ('vendor/etc/media_codecs_cape.xml', 'vendor/etc/media_codecs_cape_vendor.xml'): blob_fixup()
        .regex_replace('.*media_codecs_(google_audio|google_c2|google_telephony|google_video|vendor_audio).*\n', ''),
    ('vendor/etc/seccomp_policy/atfwd@2.0.policy',
     'vendor/etc/seccomp_policy/modemManager.policy',
     'vendor/etc/seccomp_policy/sensors-qesdk.policy'): blob_fixup()
        .add_line_if_missing('gettid: 1'),
    'vendor/lib64/libQnnGpu.so': blob_fixup()
        .strip_debug_sections(),
    ('vendor/lib/libcamximageformatutils.so',
     'vendor/lib64/libcamximageformatutils.so'): blob_fixup()
        .replace_needed('vendor.qti.hardware.display.config-V2-ndk_platform.so', 'vendor.qti.hardware.display.config-V2-ndk.so'),
    'vendor/lib64/libvendor.goodix.hardware.biometrics.fingerprint@2.1.so': blob_fixup()
        .remove_needed('libhidltransport.so')
        .replace_needed('libhidlbase.so', 'libhidlbase-v32.so'),
}  # fmt: skip

module = ExtractUtilsModule(
    'zenfone9',
    'asus',
    blob_fixups=blob_fixups,
    lib_fixups=lib_fixups,
    check_elf=False,
)

module.add_proprietary_file('proprietary-files-product.txt')
module.add_proprietary_file('proprietary-files-vendor.txt')

if __name__ == '__main__':
    utils = ExtractUtils.device(module)
    utils.run()
