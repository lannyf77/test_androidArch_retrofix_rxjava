package com.example.linma9.mytechcruncharticlelistapplication.database

import android.arch.persistence.room.*

/**
 * Created by linma9 on 1/23/18.
 */

@Dao
interface DbPostDao {
    @Query("select * from post")
    fun loadAllPosts(): List<DbPost>

    @Query("select * from post where postID = :arg0")
    fun loadPostByPostId(id: Int): DbPost

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDbPost(post: DbPost)

    @Delete
    fun deleteDbPost(post: DbPost)

    @Query("DELETE FROM post")
    fun deleteAll()
}
