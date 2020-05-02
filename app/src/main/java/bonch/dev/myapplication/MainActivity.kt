package bonch.dev.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Callback<ResponseBody> {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pictureAdapter: PictureAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var firstVisibleItem: Int = 0

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        pictureAdapter = PictureAdapter()
        layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = pictureAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItem >= totalItemCount) {
                        Log.v(TAG, "Call Load More !")
                        onLoadMore()
                    }
                }
            }
        })
        onLoadMore()
        onLoadMore()
        onLoadMore()
    }

    fun onLoadMore() {
        RestClient.instance().loadImage().enqueue(this)
    }

    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        Log.e(TAG, t.message)
    }

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        pictureAdapter.addItem(BitmapFactory.decodeStream(response.body()?.byteStream()))
        pictureAdapter.notifyDataSetChanged()
    }
}
