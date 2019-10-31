#include <jni.h>

extern int main(int argc,char * argv[]);

extern "C"
JNIEXPORT void JNICALL
Java_com_hxj_bsdiffdemo_MainActivity_generateNewApkByPatch(JNIEnv *env, jobject jobj,
        jstring old_apk_file_, jstring new_apk_file_, jstring patch_file_);



void Java_com_hxj_bsdiffdemo_MainActivity_generateNewApkByPatch(JNIEnv *env, jobject jobj,
        jstring old_apk_file_, jstring new_apk_file_, jstring patch_file_) {
    const char *old_apk_file = env->GetStringUTFChars(old_apk_file_, NULL);
    const char *new_apk_file = env->GetStringUTFChars(new_apk_file_, NULL);
    const char *patch_file = env->GetStringUTFChars(patch_file_, NULL);

    char *args[4];

    args[0] = (char *)"bspatch";
    args[1] = (char *) old_apk_file;
    args[2] = (char *) new_apk_file;
    args[3] = (char *) patch_file;

    main(4, args);

    env->ReleaseStringUTFChars(old_apk_file_, old_apk_file);
    env->ReleaseStringUTFChars(new_apk_file_, new_apk_file);
    env->ReleaseStringUTFChars(patch_file_, patch_file);
}