package com.example.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {
    private lateinit var retService: AlbumService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)
        getRequestWithQueryParameters()
       // getRequestWithPathParameters()

    }
    private fun getRequestWithQueryParameters(){
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response:Response<Albums> = retService.getSortedAlbums(3)
            emit(response)
        }
        responseLiveData.observe(this) {
            val albumsList: MutableListIterator<AlbumsItem>? = it.body()?.listIterator()
            if (albumsList != null) {
                while (albumsList.hasNext()){
                    val albumsItem:AlbumsItem = albumsList.next()
                    //Log.i("MYTAG", albumsItem.title)
                    val result:String = " "+"Album Title : ${albumsItem.title}"+"\n"+
                            " "+"Album id : ${albumsItem.id}"+"\n"+
                            " "+"User id : ${albumsItem.userId}"+"\n\n\n"
                    val text_view = findViewById<TextView>(R.id.text_view)
                    text_view.append(result)

                }
            }
        }
    }
    private fun getRequestWithPathParameters(){
        // path parameter example
        val pathResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response:Response<AlbumsItem> = retService.getAlbum(3)
            emit(response)
        }
        pathResponse.observe(this, Observer{
            val title:String? = it.body()?.title
            Toast.makeText(applicationContext, title, Toast.LENGTH_LONG).show()


        })
    }
}