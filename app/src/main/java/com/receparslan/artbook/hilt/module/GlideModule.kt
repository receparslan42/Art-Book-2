package com.receparslan.artbook.hilt.module

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.receparslan.artbook.util.defaultImageOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GlideModule {

    @Provides
    fun provideGlide(@ApplicationContext context: Context): RequestManager =
        Glide.with(context.applicationContext)
            .setDefaultRequestOptions(defaultImageOptions(context.applicationContext))
}