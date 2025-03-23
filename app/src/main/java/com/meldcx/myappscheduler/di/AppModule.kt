package com.meldcx.myappscheduler.di

import android.app.AlarmManager
import android.content.Context
import com.meldcx.myappscheduler.data.repository.AppAlarmRepositoryImpl
import com.meldcx.myappscheduler.data.repository.AppSchedulerRepositoryImpl
import com.meldcx.myappscheduler.datamodel.repository.AppAlarmRepository
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

    @Provides
    fun provideAlarmManager(@ApplicationContext application: Context): AlarmManager {
        return application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Singleton
    @Provides
    fun provideAppScheduleRepository(appSchedulerRepository: AppSchedulerRepositoryImpl): AppSchedulerRepository =
        appSchedulerRepository

    @Singleton
    @Provides
    fun provideAlarmRepository(appAlarmRepository: AppAlarmRepositoryImpl): AppAlarmRepository =
        appAlarmRepository

}