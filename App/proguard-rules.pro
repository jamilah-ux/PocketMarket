# --- Domain Models & Data Entities ---
-keep class com.example.pocketmarket.domain.model.** { *; }
-keep class com.example.pocketmarket.data.local.entity.** { *; }
-keep class com.example.pocketmarket.data.remote.dto.** { *; }

# --- Room Database ---
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# --- Retrofit & Kotlinx Serialization ---
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.retrofit2.http.* <methods>;
}

# --- Coroutines & Flow ---
-keepclassmembers class kotlinx.coroutines.** { *; }