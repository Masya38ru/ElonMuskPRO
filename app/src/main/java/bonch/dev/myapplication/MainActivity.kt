package bonch.dev.myapplication

import android.net.Uri
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
import kotlin.collections.HashSet

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var pictureAdapter: PictureAdapter
    private val responses = ArrayList<Response<ResponseBody>>()
    private var loadImageUrlCalls = HashSet<Call<ResponseBody>>()

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
                        if (loadImageUrlCalls.isEmpty()) {
                            Log.v(TAG, "Call Load More !")
                            loadMore(GetImageUriCallback())
                        }
                    }
                }
            }
        })
    }

    private fun addLoadImageUrlCall (callback: Callback<ResponseBody>){
        val loadImageUrl = RestClient.instance().getRandomImageUri()
        loadImageUrlCalls.add(loadImageUrl)
        loadImageUrl.enqueue(callback)
    }

    private fun loadMore(callback: Callback<ResponseBody>) {
        for (call in loadImageUrlCalls) {
            call.cancel()
        }
        loadImageUrlCalls.removeAll(loadImageUrlCalls)
        for (i in 1..PictureAdapter.MIN_SIZE){
            addLoadImageUrlCall(callback)
        }
    }

    inner class GetImageUriCallback : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e(TAG, t.message)
            loadImageUrlCalls.remove(call)
            addLoadImageUrlCall(this)
        }

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            loadImageUrlCalls.remove(call)
            responses.add(response)
            if (loadImageUrlCalls.isEmpty()) {
                while (responses.size > 0) {
                    val resp = responses.last()
                    responses.removeAt(responses.size - 1)
                    val uri = Uri.parse(resp.message())
                    pictureAdapter.addItem(uri)
                    text_url.text = uri.toString()
                }
                pictureAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadMore(GetImageUriCallback())
    }
    override fun onStop() {
        super.onStop()
        for (call in loadImageUrlCalls) {
            call.cancel()
        }
        loadImageUrlCalls = HashSet()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

