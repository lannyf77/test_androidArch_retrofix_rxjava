package com.example.linma9.mytechcruncharticlelistapplication.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by linma9 on 1/23/18.
 */


@Database(entities = arrayOf(DbPost::class, DbAuthor::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun authModel(): DbAuthorDao
    abstract fun postModel(): DbPostDao

    //
    //abstract fun noteModel(): NoteDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInMemoryDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "wctcdatabase")

                //INSTANCE = Room.inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java)
                        // To simplify the codelab, allow queries on the main thread.
                        // Don't do this on a real app! See PersistenceBasicSample for an example.

                        //.allowMainThreadQueries()  //in ListViewModel::subscribeToDbPostChanges() do in different thread

                        .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
