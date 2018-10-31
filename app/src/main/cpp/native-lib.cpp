#include <jni.h>
#include <android/log.h>
//编码
#ifdef __cplusplus
extern "C"
{
#endif
#include "include/libavcodec/avcodec.h"
//封装格式处理
#include "include/libavformat/avformat.h"
//像素处理
#include "include/libswscale/swscale.h"


#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"heiko",FORMAT,__VA_ARGS__);
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"heiko",FORMAT,__VA_ARGS__);

JNIEXPORT jstring JNICALL
Java_stan_androiddemo_tool_ffmpeg_FFmpegBridge_ffmpegInfo(JNIEnv *env, jclass type) {
    char info[10000] = { 0 };
    sprintf(info, "%s\n", avcodec_configuration());
    return (*env).NewStringUTF(info);
}
#ifdef __cplusplus
};
#endif