package com.meldcx.myappscheduler.di

import android.content.Context
import com.meldcx.myappscheduler.data.repository.AppSchedulerRepositoryImpl
import com.meldcx.myappscheduler.datamodel.repository.AppSchedulerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideContext(@ApplicationContext application: Context): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideAppScheduleRepository(appSchedulerRepository: AppSchedulerRepositoryImpl): AppSchedulerRepository =
        appSchedulerRepository

}