package com.nicksnacs.smashultimateframedata.home

import dagger.Subcomponent

@MainActivityScope
@Subcomponent(modules = [CharactersModelModule::class])
interface CharactersModelComponent {
    fun inject(mainActivity: MainActivity)

    @Subcomponent.Builder
    interface Builder {
        fun build() : CharactersModelComponent
    }
}