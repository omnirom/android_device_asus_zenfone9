/*
   Copyright (c) 2015, The Linux Foundation. All rights reserved.
   Copyright (C) 2020 The OmniRom Project.
   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are
   met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of The Linux Foundation nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.
   THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
   WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
   ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
   BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
   CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
   SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
   BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
   WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
   OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
   IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <android-base/file.h>
#include <android-base/properties.h>
#include <android-base/logging.h>
#include "property_service.h"
#include <sys/resource.h>
#define _REALLY_INCLUDE_SYS__SYSTEM_PROPERTIES_H_
#include <sys/_system_properties.h>

#define CUSTOMER_FILE "/mnt/vendor/persist/CUSTOMER"
#define IDCODE_FILE "/mnt/vendor/persist/COLOR"
#define VARIANT_FILE "/mnt/vendor/persist/COUNTRY"

namespace android {
namespace init {

using android::base::GetProperty;
using android::base::ReadFileToString;

void property_override(const std::string& name, const std::string& value)
{
    size_t valuelen = value.size();

    prop_info* pi = (prop_info*) __system_property_find(name.c_str());
    if (pi != nullptr) {
        __system_property_update(pi, value.c_str(), valuelen);
    }
    else {
        int rc = __system_property_add(name.c_str(), name.size(), value.c_str(), valuelen);
        if (rc < 0) {
            LOG(ERROR) << "property_set(\"" << name << "\", \"" << value << "\") failed: "
                       << "__system_property_add failed";
        }
    }
}

void property_override_dual(char const prop[], char const system_prop[],
    const std::string& value)
{
    property_override(prop, value);
    property_override(system_prop, value);
}

void ro_prop_override(char const source[], char const prop[],
    const std::string& value, bool product)
{
    std::string prop_name = "ro.";

    if (product) prop_name += "product.";
    if (source != nullptr) prop_name += source;
    if (!product) prop_name += "build.";
    prop_name += prop;

    property_override(prop_name.c_str(), value);
};

static const char* RO_PROP_SOURCES[] = {
    nullptr,
    "bootimage.",
    "odm.",
    "odm_dlkm.",
    "product.",
    "system.",
    "system_dlkm.",
    "system_ext.",
    "vendor.",
    "vendor_dlkm.",
};

/* From Magisk@jni/magiskhide/hide_utils.c */
static const char *snet_prop_key[] = {
    "ro.boot.vbmeta.device_state",
    "ro.boot.verifiedbootstate",
    "ro.boot.flash.locked",
    "ro.boot.veritymode",
    "ro.boot.warranty_bit",
    "ro.warranty_bit",
    NULL
};

 static const char *snet_prop_value[] = {
    "locked",
    "green",
    "1",
    "enforcing",
    "0",
    "0",
    NULL
};

 static void workaround_snet_properties() {

     // Hide all sensitive props
    for (int i = 0; snet_prop_key[i]; ++i) {
        property_override(snet_prop_key[i], snet_prop_value[i]);
    }
}

static void set_configs()
{
    std::string variantValue;

    if (ReadFileToString(CUSTOMER_FILE, &variantValue)) {
        property_override("ro.vendor.config.CID", variantValue.c_str());
    }
    if (ReadFileToString(IDCODE_FILE, &variantValue)) {
        property_override("ro.vendor.config.idcode", variantValue.c_str());
    }
    if (ReadFileToString(VARIANT_FILE, &variantValue)) {
        property_override("ro.vendor.config.versatility", variantValue.c_str());
    }
}

static void set_fingerprint()
{
    std::string build_asus;
    std::string build_id;
    std::string build_number;
    std::ostringstream fp;
    std::string fingerprint;
    std::string name;
    std::string variant;
    std::ostringstream desc;
    std::string description;
    std::ostringstream disp;
    std::string display_id;

    LOG(INFO) << "Starting custom init";

    variant = GetProperty("ro.vendor.config.versatility", "");

    // Set the default "name" string and below properties based on variant name
    if (variant == "EU") {
        property_override("ro.product.vendor.name", "EU_I006D");
    } else if (variant == "RU") {
        property_override("ro.product.vendor.model", "AI2202");
        property_override("ro.product.vendor.name", "RU_AI2202");
    } else {
        property_override("ro.product.vendor.name", "WW_AI2202");
    }

    name = GetProperty("ro.product.vendor.name", "");

    LOG(INFO) << name;

    // These should be the only things to change for OTA updates
    build_asus = "34.0304.2004.87";
    build_id = "UKQ1.230924.001";
    build_number = "34.0304.2004.87";

    // Create the correct stock props based on the above values
    desc << name << "-user 14 " << build_id << " " << build_number << " release-keys";
    description = desc.str();

    fp << "asus/" << name << "/ASUS_AI2202:14/" << build_id << "/" << build_number << ":user/release-keys";
    fingerprint = fp.str();

    // These properties are the same for all variants
    property_override("ro.vendor.build.asus.number", build_number);
    property_override("ro.vendor.build.asus.sku", "WW");
    property_override("ro.vendor.build.asus.version", build_asus);
    property_override("ro.vendor.build.software.version", build_asus);
    property_override("ro.build.description", description);

    for (const auto& source : RO_PROP_SOURCES) {
        ro_prop_override(source, "brand", "asus", true);
        ro_prop_override(source, "device", "ASUS_AI2202", true);
        ro_prop_override(source, "manufacturer", "asus", true);
        ro_prop_override(source, "model", "ASUS_AI2202", true);
        ro_prop_override(source, "name", name, true);

        ro_prop_override(source, "fingerprint", fingerprint, false);
    }
}

void vendor_load_properties()
{
    // SafetyNet workaround
    property_override("ro.boot.verifiedbootstate", "green");
    workaround_snet_properties();
    set_configs();
    set_fingerprint();
}

} //init
} //android
