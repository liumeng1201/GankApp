# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\work\adt-bundle-windows\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes Signature

# for Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# for bmob
-keep class cn.bmob.** {*;}
-keep class com.bmob.** {*;}

# for okhttp、okio
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** {*;}
-keep interface com.squareup.okhttp.** {*;}
-dontwarn okio.**
# okhttputils
-keep class com.zhy.http.okhttp.** {*;}

# for gson
-keep class com.google.gson.** {*;}

# for logger
-keep class com.orhanobut.logger.** {*;}

# for LeakCanary
-keep class org.eclipse.mat.** {*;}
-keep class com.squareup.leakcanary.** {*;}

# for icepick icepick-processor
-dontwarn icepick.**
-keep class **$$Icepick {*;}
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}