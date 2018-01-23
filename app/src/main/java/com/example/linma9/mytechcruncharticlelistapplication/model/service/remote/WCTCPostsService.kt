package com.example.linma9.mytechcruncharticlelistapplication.model.service.remote

import com.example.linma9.mytechcruncharticlelistapplication.model.data.Posts
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by linma9 on 1/23/18.
 */

interface WCTCPostsService {

    @GET("posts")
    fun getPosts(@Query("number") num: Int,
                   @Query("offset") offset: Int): Observable<Posts>

}