package com.example.appxmlrss

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {


        lateinit var rv: RecyclerView
        lateinit var topMovies : MutableList<Movie>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            rv = findViewById(R.id.rvMain)
            FetchTopMovies().execute()
        }

        private inner class FetchTopMovies : AsyncTask<Void, Void, MutableList<Movie>>() {
            val parser = MyXmlPullParser()
            val progressD = ProgressDialog(this@MainActivity)

            override fun onPreExecute() {
                super.onPreExecute()
                progressD.setMessage("Please Wait")
                progressD.show()
            }

            override fun doInBackground(vararg params: Void?): MutableList<Movie> {
                val url = URL("http://www.rediff.com/rss/moviesreviewsrss.xml")
                val urlConnection = url.openConnection() as HttpURLConnection
                topMovies =
                    urlConnection.getInputStream()?.let { parser.parse(it) } as MutableList<Movie>
                return topMovies
            }

            override fun onPostExecute(result: MutableList<Movie>?) {
                super.onPostExecute(result)
                rv.adapter = rvAdapter(result)
                rv.layoutManager = LinearLayoutManager(applicationContext)
            }

        }
}