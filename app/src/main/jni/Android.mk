LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := bzlib

LOCAL_MODULE := bspatch

LOCAL_SRC_FILES := bspatch_native.cpp bzlib/blocksort.c bzlib/huffman.c bzlib/crctable.c bzlib/randtable.c bzlib/compress.c bzlib/decompress.c bzlib/bzlib.c bzlib/bzip2.c

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)





