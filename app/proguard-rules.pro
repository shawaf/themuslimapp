# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ---- Keep stack traces readable in release crash reports ----
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature,EnclosingMethod

# ---- Gson: models are (de)serialized by reflection ----
-keep class me.shawaf.themuslimapp.data.local.model.** { *; }
-keep class me.shawaf.themuslimapp.data.local.prefers.** { *; }
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# ---- adhan: prayer-time enums are resolved by name (Enum.valueOf) ----
-keep class com.batoulapps.adhan.** { *; }
-keepclassmembers enum com.batoulapps.adhan.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}