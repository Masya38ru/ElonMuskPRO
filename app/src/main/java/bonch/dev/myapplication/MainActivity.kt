package bonch.dev.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var pictureAdapter: PictureAdapter
    private val responses = LinkedList<Response<ResponseBody>>()
    private var calls = LinkedList<Call<ResponseBody>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        pictureAdapter = PictureAdapter()
        val layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = pictureAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItem >= totalItemCount) {
                        Log.v(TAG, "Call Load More !")
                        if (calls.size == 0)
                            loadMore(CallBackImplementer())
                    }
                }
            }
        })
        loadMore(CallBackImplementer())
    }

    fun loadMore(callback: Callback<ResponseBody>) {
        val call = RestClient.instance().loadImage()
        for (i in 1..PictureAdapter.MIN_SIZE) {
            calls.add(call.clone())
            calls.last.enqueue(callback)
        }
    }

    inner class CallBackImplementer : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e(TAG, t.message)
        }

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            responses.add(response)
            if (responses.size == PictureAdapter.MIN_SIZE) {
                while (responses.size > 0) {
                    calls.poll()
                    val resp = responses.pollFirst()
                    val url = resp.raw().request().url().toString()
                    pictureAdapter.addItem(
                        BitmapFactory.decodeStream(resp.body()?.byteStream()),
                        url
                    )
                    text_url.text = url
                }
                pictureAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        for (call in calls)
            if (!call.isExecuted)
                call.cancel()
        calls = LinkedList()
    }
}

