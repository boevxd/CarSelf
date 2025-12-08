# ========== General Android / Kotlin ==========
# Keep Kotlin metadata (required for reflection and serialization)
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**
-dontwarn org.jetbrains.annotations.**
-keepattributes *Annotation*

# Keep your app entry points
-keep class dm.com.carlog.CarLogApp { *; }
-keep class dm.com.carlog.MainActivity { *; }

# ========== Jetpack Compose ==========
# Keep Composables and related runtime
-keep @androidx.compose.runtime.Composable class * { *; }
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ========== Hilt / Dagger ==========
# Keep Hilt/Dagger generated components
-keep class dagger.hilt.** { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-dontwarn dagger.hilt.internal.**
-dontwarn javax.inject.**
-dontwarn dagger.**

# ========== Room ==========
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# Keep entities, DAOs, and repositories
-keep class dm.com.carlog.data.** { *; }

# ========== Navigation Compose ==========
-keep class androidx.navigation.** { *; }
-dontwarn androidx.navigation.**

# ========== Coil (Image loading) ==========
-keep class coil.** { *; }
-dontwarn coil.**

# ========== Other utilities / helpers ==========
-keep class dm.com.carlog.util.** { *; }
-dontwarn dm.com.carlog.util.**

# Keep ViewModels (Hilt + Compose rely on reflection here)
-keep class dm.com.carlog.model.**ViewModel { *; }

# ========== Optional but useful ==========
# Keep your custom Application and DI module
-keep class dm.com.carlog.di.AppModule { *; }

# Keep line numbers for crash reports (optional)
-keepattributes SourceFile,LineNumberTable
