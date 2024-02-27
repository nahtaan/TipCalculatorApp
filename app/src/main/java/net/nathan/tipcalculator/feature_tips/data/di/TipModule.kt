package net.nathan.tipcalculator.feature_tips.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import net.nathan.tipcalculator.feature_tips.data.local.EmployeeDatabase
import net.nathan.tipcalculator.feature_tips.data.local.EmployeeEntryDao
import net.nathan.tipcalculator.feature_tips.data.repo.EmployeeRepoImpl
import net.nathan.tipcalculator.feature_tips.domain.repo.EmployeeRepo
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TipModule {
    @Provides
    fun providesRoomDao(database: EmployeeDatabase): EmployeeEntryDao{
        return database.dao
    }

    @Singleton
    @Provides
    fun providesRoomDb(
        @ApplicationContext appContext: Context,
    ): EmployeeDatabase{
        return Room.databaseBuilder(appContext.applicationContext,
            EmployeeDatabase::class.java,
            "employee_db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesEmployeeEntryRepo(
        db: EmployeeDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): EmployeeRepo{
        return EmployeeRepoImpl(dao = db.dao, dispatcher = dispatcher)
    }

}