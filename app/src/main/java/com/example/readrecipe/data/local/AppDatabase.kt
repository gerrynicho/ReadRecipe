package com.example.readrecipe.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.readrecipe.data.local.dao.SavedRecipeDao
import com.example.readrecipe.data.local.dao.UserDao
import com.example.readrecipe.data.local.entity.SavedRecipeEntity
import com.example.readrecipe.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, SavedRecipeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun savedRecipeDao(): SavedRecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "readrecipe_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
