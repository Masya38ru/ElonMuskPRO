package bonch.dev.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)

        val adapter = PictureAdapter(
            listOf(
                "https://img0.liveinternet.ru/images/attach/c/1/58/895/58895836_0_2a883_8933efd9_XL.jpg",
                "https://cs.pikabu.ru/post_img/2013/08/08/5/1375944655_592971946.jpg",
                "https://i.pinimg.com/736x/17/a3/a8/17a3a8381771953e5df17a6ef90cdeb3--middle-fingers-funny-pets.jpg",
                "https://img-hw.xvideos.com/videos/profiles/profthumb/84/2d/26/brian808/profile_1_big.jpg",
                "https://www.proza.ru/pics/2013/07/11/1100.jpg",
                "https://i.mycdn.me/i?r=AzEPZsRbOZEKgBhR0XGMT1RkXIkzoSU89xL0yGqn8o8poaaKTM5SRkZCeTgDn6uOyic",
                "https://www.caption.me/images/flickr/263331.jpg",
                "https://pics.me.me/fuck-you-cat-7834808.png",
                "https://i02.fotocdn.net/s108/2811661b2a784b71/public_pin_m/2395182812.jpg",
                "https://i.pinimg.com/736x/2b/9a/1a/2b9a1a8becf3bfeaee73bf87ddef736a.jpg"
            )
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
