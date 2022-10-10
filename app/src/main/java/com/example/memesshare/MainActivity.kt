package com.example.memesshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memesshare.MySingleton.MySingleton.Companion.getInstance

class MainActivity : AppCompatActivity() {
    var currentImageUrl: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    private fun loadMeme(){
        val pB = findViewById<ProgressBar>(R.id.progressbar)
        pB.visibility=View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"

    // Request a json response from the provided URL.
            val jasonObjectRequest= JsonObjectRequest{Request.Method.GET,
                url,
                null,

            Response.Listener{ response ->
                currentImageUrl=response.getString("url")
                val mi=findViewById<ImageView>(R.id.memeImage)
                Glide.with(this).load(currentImageUrl).listener(object:RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pB.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pB.visibility=View.GONE
                        return false
                    }
                }).into(mi)

            },
            Response.ErrorListener {
                Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()

            })

    // Add the request to the RequestQueue.
        MySingleton.MySingleton.Companion.getInstance(this).addToRequestQueue(jasonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey!!,Check out this cool meme I got from reddit $currentImageUrl")
        val chooser=Intent.createChooser(intent,"Share this meme using.......")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}