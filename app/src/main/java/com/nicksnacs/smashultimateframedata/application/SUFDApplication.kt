package com.nicksnacs.smashultimateframedata.application

import android.app.Application

class SUFDApplication : Application() {
    private var mApplicationComponent: ApplicationComponent? = null
    val applicationComponent: ApplicationComponent
        get() {
            if (mApplicationComponent == null) {
                mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(this))
                    .build()
            }
            return mApplicationComponent!!
        }
}
