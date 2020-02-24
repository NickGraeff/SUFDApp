package com.nicksnacs.smashultimateframedata.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nicksnacs.smashultimateframedata.application.CustomViewModelProviderFactory
import com.nicksnacs.smashultimateframedata.application.ViewModelKey
import com.nicksnacs.smashultimateframedata.character_page.DetailedCharacterInformationModel
import com.nicksnacs.smashultimateframedata.character_page.DetailedCharacterScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DetailedCharacterInformationModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DetailedCharacterInformationModel::class)
    @DetailedCharacterScope
    abstract fun detailedCharacterInformationModel(viewModel: DetailedCharacterInformationModel): ViewModel
}
