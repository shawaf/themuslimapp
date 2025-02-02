package me.shawaf.themuslimapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.app.TheMuslimApplication
import me.shawaf.themuslimapp.data.local.dp.AppDataBase
import me.shawaf.themuslimapp.data.local.dp.DB_NAME
import me.shawaf.themuslimapp.data.local.prefers.SharedPreferencesManager
import me.shawaf.themuslimapp.data.local.prefers.SharedPreferencesManagerImp
import me.shawaf.themuslimapp.utils.AzkarUtils
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): TheMuslimApplication =
        context as TheMuslimApplication

    @Provides
    @Singleton
    fun provideAppContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideSharePrefers(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideSharePrefersManager(
        sharedPreferences: SharedPreferences, gson: Gson
    ): SharedPreferencesManager = SharedPreferencesManagerImp(sharedPreferences, gson)

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideAppDatabase(applicationContext: Context): AppDataBase {
        val roomDB = Room.databaseBuilder(applicationContext, AppDataBase::class.java, DB_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()  // Deletes OLD DB When new scheme provided, disable it if you want to preserve data.
            .build()

        return roomDB
    }

    @Singleton
    @Provides
    fun provideZekrDao(appDataBase: AppDataBase) = appDataBase.zekrDao()

    @Singleton
    @Provides
    fun provideAzkarUtils(applicationContext: Context) = AzkarUtils(applicationContext)
}