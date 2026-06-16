package com.example.readrecipe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.readrecipe.data.local.entity.SavedRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(recipe: SavedRecipeEntity)

    @Query("DELETE FROM saved_recipes WHERE meal_id = :mealId AND user_id = :userId")
    suspend fun unsave(mealId: String, userId: Long)

    @Query("SELECT * FROM saved_recipes WHERE user_id = :userId ORDER BY meal_id DESC")
    fun getSavedRecipes(userId: Long): Flow<List<SavedRecipeEntity>>

    @Query("SELECT COUNT(*) > 0 FROM saved_recipes WHERE meal_id = :mealId AND user_id = :userId")
    fun isRecipeSaved(mealId: String, userId: Long): Flow<Boolean>
}
