package com.nicksnacs.smashultimateframedata.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nicksnacs.smashultimateframedata.application.CustomViewModelProviderFactory
import com.nicksnacs.smashultimateframedata.application.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CharactersModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CharactersViewModel::class)
    @MainActivityScope
    abstract fun charactersViewModel(viewModel: CharactersViewModel): ViewModel
}
