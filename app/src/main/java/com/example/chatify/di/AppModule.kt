package com.example.chatify.di

import android.content.Context
import com.example.chatify.constants.API_KEY
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }


    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-pro",  // You can also use "gemini-pro-vision" for images
            apiKey = "AIzaSyA93NGP16DfGhHfrNesaZLSjeDT8mTyKYg" // 🔥 Replace with your actual Gemini API key
        )
    }
}