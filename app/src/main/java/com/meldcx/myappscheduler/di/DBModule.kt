package com.meldcx.myappscheduler.di

import android.content.Context
import androidx.room.Room
import com.meldcx.myappscheduler.data.dao.AppEventDao
import com.meldcx.myappscheduler.data.dao.AppScheduleDao
import com.meldcx.myappscheduler.data.db.AppDatabase
import com.meldcx.myappscheduler.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

    @Singleton
    @Provides
    fun provideDB(
        @ApplicationContext application: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            Constants.DB_NAME
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun providesAppScheduleDao(parameter: AppDatabase): AppScheduleDao {
        return parameter.getAppScheduleDao()
    }

    @Provides
    fun providesAppEventDao(parameter: AppDatabase): AppEventDao {
        return parameter.getAppEventDao()
    }

}
