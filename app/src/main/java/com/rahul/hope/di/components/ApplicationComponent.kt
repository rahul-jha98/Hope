package com.rahul.hope.di.components

import com.rahul.hope.data.DataRepository
import com.rahul.hope.data.network.ApiService
import com.rahul.hope.di.modules.NetworkModule
import com.rahul.hope.di.modules.ViewModelModule
import com.rahul.hope.viewmodels.RoomViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ViewModelModule::class), (NetworkModule::class)])
interface ApplicationComponent {
    fun getViewModelFactory() : RoomViewModelFactory
    fun getRepository() : DataRepository
    fun getApiService() : ApiService
}