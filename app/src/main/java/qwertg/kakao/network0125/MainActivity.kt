package qwertg.kakao.network0125

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnKakaoOpenAPI.setOnClickListener{
            val intent = Intent(this, KakaoOpenApiActivity::class.java)
            startActivity(intent)
        }

        btnItemDetail.setOnClickListener{
            val intent = Intent(this, ItemDetailActivity::class.java)
            startActivity(intent)
        }

        btnItemInsert.setOnClickListener{
            val intent = Intent(this, ItemInsertActivity::class.java)
            startActivity(intent)
        }
    }

}