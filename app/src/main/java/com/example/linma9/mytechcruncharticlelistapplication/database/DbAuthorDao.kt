package com.example.linma9.mytechcruncharticlelistapplication.database

import android.arch.persistence.room.*

@Dao
interface DbAuthorDao {
    @Query("select * from author")
    fun loadAllAuthors(): List<DbAuthor>

    @Query("select * from author where authorid = :arg0")
    fun loadUserById(id: Int): DbAuthor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAuthor(author: DbAuthor) : Long

    @Delete
    fun deleteAuthor(author: DbAuthor) : Int

    @Query("delete from author where firstName like :arg0 OR lastName like :arg0")
    fun deleteAuthorByName(badName: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceUsers(vararg author: DbAuthor)

    @Delete
    fun deleteAuthors(vararg authors: DbAuthor) : Int

    @Query("DELETE FROM Author")
    fun deleteAll()

    @Insert
    fun insertAll(vararg authors: DbAuthor)
}