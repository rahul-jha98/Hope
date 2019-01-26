package com.rahul.hope

import android.app.Application
import com.rahul.hope.di.components.ApplicationComponent
import com.rahul.hope.di.components.DaggerApplicationComponent
import com.rahul.hope.di.modules.ContextModule
import com.rahul.hope.di.modules.RoomModule
import com.rahul.hope.di.modules.ViewModelModule

class HopeApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .roomModule(RoomModule)
            .viewModelModule(ViewModelModule)
            .build()
    }
}