package com.rahul.hope.di.modules

import android.content.Context
import com.rahul.hope.AppExecutors
import com.rahul.hope.data.DataRepository
import com.rahul.hope.data.database.SheSafeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [(ContextModule::class)])
object RoomModule {
    @Provides
    @Singleton
    fun provideAppExecutors() = AppExecutors.getInstance()

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context) = SheSafeDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideDataRepository(sheSafeDatabase: SheSafeDatabase, appExecutors: AppExecutors) =
            DataRepository.getInstance(sheSafeDatabase.emergencyContactsDao(),
                sheSafeDatabase.chatRoomDao(),
                appExecutors)
}