# Board specific SELinux policy variable definitions
SEPOLICY_PATH:= device/qcom/sepolicy
SYSTEM_EXT_PUBLIC_SEPOLICY_DIRS  := \
    $(SYSTEM_EXT_PUBLIC_SEPOLICY_DIRS ) \
    $(SEPOLICY_PATH)/generic/public \
    $(SEPOLICY_PATH)/generic/public/attribute

SYSTEM_EXT_PRIVATE_SEPOLICY_DIRS := \
    $(SYSTEM_EXT_PRIVATE_SEPOLICY_DIRS) \
    $(SEPOLICY_PATH)/generic/private

SYSTEM_EXT_PUBLIC_SEPOLICY_DIRS  := \
    $(SYSTEM_EXT_PUBLIC_SEPOLICY_DIRS ) \
    $(SEPOLICY_PATH)/qva/public \
    $(SEPOLICY_PATH)/qva/public/attribute

#once all the services are moved to Product /ODM above lines will be removed.
# sepolicy rules for product images
PRODUCT_PUBLIC_SEPOLICY_DIRS := \
    $(PRODUCT_PUBLIC_SEPOLICY_DIRS) \
    $(SEPOLICY_PATH)/generic/product/public \
    $(SEPOLICY_PATH)/qva/product/public 

PRODUCT_PRIVATE_SEPOLICY_DIRS := \
    $(PRODUCT_PRIVATE_SEPOLICY_DIRS) \
    $(SEPOLICY_PATH)/generic/product/private \
    $(SEPOLICY_PATH)/qva/product/private
