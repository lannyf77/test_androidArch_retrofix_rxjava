package com.example.linma9.mytechcruncharticlelistapplication.database.DatabaseUtils

import android.os.AsyncTask
import com.example.linma9.mytechcruncharticlelistapplication.database.AppDatabase
import android.util.Log

/**
 * Created by linma9 on 1/23/18.
 */

class DatabaseInitializer {

    companion object {
        val TAG = DatabaseInitializer::class.java.name
        internal val DELAY_MILLIS = 500

        private var databaseInitor: DatabaseInitializer? = null
        val instance: DatabaseInitializer
            @Synchronized get() {
                if (databaseInitor == null) {
                    databaseInitor = DatabaseInitializer()
                }
                return databaseInitor!!
            }
    }

    fun populateAsync(db: AppDatabase) {

        ////Log.d(DatabaseInitializer.TAG, "+++ ######### DatabaseInitializer:populateAsync(), db: ${db}, thread:"+Thread.currentThread().getId())

        val task = PopulateDbAsync(db)
        task.execute()
    }

    fun populateSync(db: AppDatabase) {

        ////Log.d(DatabaseInitializer.TAG, "+++ ######### DatabaseInitializer:populateSync(), db: ${db}, thread:"+Thread.currentThread().getId())

        populateWithTestData(db)
    }

    internal class PopulateDbAsync internal constructor(private val mDb2: AppDatabase) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {

            ////Log.e(DatabaseInitializer.TAG, "+++ ######### 222 PopulateDbAsync:doInBackground(), mDb2: ${mDb2}, call DatabaseInitializer.instance.populateWithTestData(mDb2) thread:"+Thread.currentThread().getId())

            DatabaseInitializer.instance.populateWithTestData(mDb2)
            return null
        }

    }

//    private fun addAuther(db: AppDatabase, author: DbAuthor): Long {
//        var rowId: Long = db.authModel().insertAuthor(author)
//        //Log.d("eee888", "+++ +++ {{{}}} addAuther(), rowId: " + rowId+", author :"+author.toString())
//        return rowId  // row Id
//    }
//
//    private fun addPost(db: AppDatabase, dbPost: DbPost): DbPost {
//        db.postModel().insertDbPost(dbPost)
//        return dbPost
//    }

    private fun populateWithTestData(db: AppDatabase) {

        ////Log.d("eee888", "+++ +++ {{{}}} enter populateWithTestData(), db: $db, thread:"+Thread.currentThread().getId())

        // for test always to clean first
//        db.authModel().deleteAll()
//        db.postModel().deleteAll()

        /*

        val author: Author? = post.author
                            val authorName = author!!.firstName + " " + author!!.lastName
                            val avatarUrl: String = author!!.avatarURL ?: ""
                            val title = post.title ?: " == no title == "
                            val dateStr: String? = post.date
                            var excerpt: String? = post.excerpt
                            val url: String? = post.url

                            convertedPosts.add(CTViewDataItem(
                                    authorName,
                                    title,
                                    longDate, //date, // time  1457207701L
                                    avatarUrl, // image url
                                    url,
                                    excerpt,
                                    categories
                            ))

        img_thumbnail.loadImg(item.thumbnail)
        description.text = Html.fromHtml(item.title)
        author.text = item.author
        time.text = item.created.getFriendlyTime()
        snippet.text = Html.fromHtml(item.snippet)
        categories.text = item.categories

         */
//        var author = DbAuthor()
//        author.authorid = 5956050
//        author.firstName = "firstName1"
//        author.lastName = "lastName1"
//        author.name = "test autor1"
//        author.avatarURL = "https://2.gravatar.com/avatar/e819990a43a5972b40593cdc6896eef9?s=96&d=identicon&r=G"
//
//        DataManager.instance!!.addAuther(author)
//
//        var author2 = DbAuthor()
//        author2.authorid = 1527966
//        author2.firstName = "firstName 2"
//        author2.lastName = "lastName 2"
//        author2.name = "test autor 2"
//        author2.avatarURL = "https://en.gravatar.com/avatar/5f5876ccfd72ee5c2620b86429e8caf5?s=96&d=identicon&r=G"
//        DataManager.instance!!.addAuther(author2)
//
//        //Log.i("eee888","+++ +++ ^^^^^^^^^^ author1: $author, author2: $author2");
//
//        var dbPost = DbPost()
//        dbPost.postId = 1528356
//        dbPost.authorId = author.authorid
//        dbPost.excerpt = "post 1 snippet"
//        dbPost.title = "post 1 tiltle"
//        dbPost.url = "http://tcrn.ch/2sdqsM1"
//        dbPost.date = "2017-06-17T13:10:54-07:00"
//        val dbPost1= DataManager.instance!!.addPost(dbPost)
//
//        dbPost.postId = 1528366
//        dbPost.authorId = author2.authorid
//        dbPost.excerpt = "post 2 snippet"
//        dbPost.title = "post 2 tiltle"
//        dbPost.url = "http://tcrn.ch/2sKsmoo"
//        dbPost.date = "2017-08-17T13:00:01-07:00"
//        val dbPost2 = DataManager.instance!!.addPost(dbPost)


        val authorList = db.authModel().loadAllAuthors()
        val postList = db.postModel().loadAllPosts()
        ////Log.d("eee888", "+++ +++ {{{}}} --- EXIT populateWithTestData(), post : " + postList.size+"\nauthor:"+authorList.size)

    }

}
