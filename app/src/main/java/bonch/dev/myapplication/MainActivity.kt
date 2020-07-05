package bonch.dev.myapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
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
    private var getImageUrlCalls = HashSet<Call<ResponseBody>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fresco.initialize(this)

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
                        if (getImageUrlCalls.size == 0) {
                            Log.v(TAG, "Call Load More !")
                            loadMore(GetImageUriCallback())
                        }
                    }
                }
            }
        })
        loadMore(GetImageUriCallback())
    }

    fun loadMore(callback: Callback<ResponseBody>) {
        for (call in getImageUrlCalls) {
            call.cancel()
        }
        getImageUrlCalls.removeAll(getImageUrlCalls)
        for (i in 1..PictureAdapter.MIN_SIZE){
            val getImageUrl = RestClient.instance().getRandomImageUri()
            getImageUrl.enqueue(callback)
            getImageUrlCalls.add(getImageUrl)
        }
    }

    inner class GetImageUriCallback : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e(TAG, t.message)
            getImageUrlCalls.remove(call)
            val getImageUrl = RestClient.instance().getRandomImageUri()
            getImageUrl.enqueue(GetImageUriCallback())
            getImageUrlCalls.add(getImageUrl)
        }

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            getImageUrlCalls.remove(call)
            responses.add(response)
            if (responses.size == PictureAdapter.MIN_SIZE) {
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

    override fun onStop() {
        super.onStop()
        for (call in getImageUrlCalls) {
            call.cancel()
        }
        getImageUrlCalls = HashSet()
    }

    override fun onDestroy() {
        super.onDestroy()
        Fresco.getImagePipeline().clearCaches()
    }
}

