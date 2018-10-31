cmake_minimum_required(VERSION 3.4.1)

#set(distribution_DIR ${CMAKE_SOURCE_DIR}/libs)            # 文件夹为libs的情况下
set(distribution_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs) # 文件夹为jniLibs的情况下
message("distribution_DIR:" ${distribution_DIR})

add_library( libavcodec
        SHARED
        IMPORTED )

add_library( libavfilter
        SHARED
        IMPORTED )

add_library( libavformat
        SHARED
        IMPORTED )

add_library( libavutil
        SHARED
        IMPORTED )


add_library( libswresample
        SHARED
        IMPORTED )

add_library( libswscale
        SHARED
        IMPORTED )

set_target_properties( libavcodec
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavcodec.so)

set_target_properties( libavfilter
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavfilter.so)

set_target_properties( libavformat
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavformat.so)

set_target_properties( libavutil
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavutil.so)


set_target_properties( libswresample
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libswresample.so)

set_target_properties( libswscale
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libswscale.so)

add_library(
        native-lib
        SHARED
        ${CMAKE_SOURCE_DIR}/src/main/cpp/native-lib.cpp) #如果是C++则为native-lib.cpp

find_library(
        log-lib
        log)

include_directories(src/main/cpp/include)

target_link_libraries( # Specifies the target library.
        native-lib
        libavcodec
        libavfilter
        libavformat
        libavutil
        libswresample
        libswscale
        ${log-lib} )