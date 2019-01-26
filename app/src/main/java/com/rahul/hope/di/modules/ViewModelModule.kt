package com.rahul.hope.di.modules

import com.rahul.hope.data.DataRepository
import com.rahul.hope.viewmodels.RoomViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [(RoomModule::class)])
object ViewModelModule {
    @Provides
    @Singleton
    fun provideViewModelFactory(repository : DataRepository) = RoomViewModelFactory(repository)
}