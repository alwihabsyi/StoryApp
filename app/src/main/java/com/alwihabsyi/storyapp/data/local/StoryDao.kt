package com.alwihabsyi.storyapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alwihabsyi.storyapp.data.remote.ListStory

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStory>)

    @Query("SELECT * FROM list_story")
    fun getAllStory(): PagingSource<Int, ListStory>

    @Query("DELETE FROM list_story")
    suspend fun deleteAll()
}