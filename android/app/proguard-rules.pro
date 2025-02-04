-dontwarn com.atom.sdk.**
-keep class com.atom.sdk.** { *; }
-keep interface com.atom.sdk.** { *; }

-dontwarn com.atom.core.**
-keep class com.atom.core.models.** { *; }
-keep interface com.atom.core.** { *; }

-dontwarn com.atom.proxy.**
-keep class com.atom.proxy.** { *; }
-keep interface com.atom.proxy.** { *; }
-keep interface com.purevpn.proxy.core.** { *; }

-keep class com.atom.sdk.android.** { *; }

-keep class com.pingchecker.** { *; }

-keep class de.blinkt.openvpn.** { *; }
-keep class org.spongycastle.util.** { *; }
-keep class org.strongswan.android.** { *; }

-keep class org.codehaus.jettison.** { *; }
-keep class com.thoughtworks.xstream.** { *; }
-keep class com.thoughtworks.xstream.converters.** { *; }

-keep class com.thoughtworks.xstream.annotations.** { *; }
-keep class com.thoughtworks.xstream.*
-keep class com.thoughtworks.xstream.* {
    public protected <methods>;
    public protected <fields>;
}

-dontwarn org.jetbrains.annotations.**
-keep class com.jakewharton.timber.** { *; }

#Crash cause: /lib/x86/libgojni.so (Java_lantern_Lantern__1init+190)
#Resolution: adding below rules
-keep class org.lantern.mobilesdk.**
-keep class lantern.** {*;}
-keep class go.** {*;}