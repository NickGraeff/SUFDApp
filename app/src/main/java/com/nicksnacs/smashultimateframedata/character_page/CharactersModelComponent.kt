package com.nicksnacs.smashultimateframedata.home

import com.nicksnacs.smashultimateframedata.character_page.CharacterActivity
import com.nicksnacs.smashultimateframedata.character_page.DetailedCharacterScope
import dagger.Subcomponent

@DetailedCharacterScope
@Subcomponent(modules = [DetailedCharacterInformationModelModule::class])
interface DetailedCharacterInformationModelComponent {
    fun inject(characterActivity: CharacterActivity)

    @Subcomponent.Builder
    interface Builder {
        fun build() : DetailedCharacterInformationModelComponent
    }
}