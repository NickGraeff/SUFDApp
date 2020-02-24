package com.nicksnacs.smashultimateframedata.application

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelProviderFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(customViewModelProviderFactory: CustomViewModelProviderFactory): ViewModelProvider.Factory
}