package com.android254.droidconke19.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android254.droidconke19.database.converters.Converter
import com.android254.droidconke19.database.dao.SessionsDao
import com.android254.droidconke19.database.dao.StarredSessionDao
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.models.StarredSessionModel


@Database(entities = [StarredSessionModel::class, SessionsModel::class], version = 9, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    //dao
    abstract fun sessionsDao(): SessionsDao

    abstract fun starredSessionDao(): StarredSessionDao

    companion object {
        //Singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            when (INSTANCE) {
                null -> INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "droidconKE_db")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}
