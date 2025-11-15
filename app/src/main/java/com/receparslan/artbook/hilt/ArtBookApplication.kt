package com.receparslan.artbook.hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArtBookApplication : Application() {
    // This class is used to initialize Hilt in the application context.
    // No additional code is needed here as Hilt will automatically generate the necessary components.
}