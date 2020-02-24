package com.nicksnacs.smashultimateframedata.application

import com.nicksnacs.smashultimateframedata.home.CharactersModelComponent
import com.nicksnacs.smashultimateframedata.home.DetailedCharacterInformationModelComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelProviderFactoryModule::class])
interface ApplicationComponent {
    fun getCharactersViewModelComponentBuilder() : CharactersModelComponent.Builder
    fun getDetailedCharacterInformationModelComponentBuilder() : DetailedCharacterInformationModelComponent.Builder
}