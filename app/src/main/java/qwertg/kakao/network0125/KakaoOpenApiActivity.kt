package qwertg.kakao.network0125

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_kakao_open_api.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class KakaoOpenApiActivity : AppCompatActivity() {
    //스레드에서 넘겨준 데이터를 화면에 출력해주는 역할을 수행
    val handler = object:Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            //스레드가 전송해준 데이터를 가져오기
            val result = msg.obj as String
            //데이터 출력
            resultView.text = result
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_open_api)

        object:Thread(){
            override fun run() {
                //2개의 파라미터를 전송하는 Kakao OPEN API URL 생성
                //파라미터는 한글인 경우 인코딩을 해주어야 합니다.
                val addr = "https://dapi.kakao.com/v3/search/book?query=" + URLEncoder.encode("코틀린", "utf-8") + "&size=50"
                //다운로드 받을 URL 생성
                val url = URL(addr)

                //URLConnection 만들기
                val con = url.openConnection() as HttpURLConnection

                //옵션 설정
                con.connectTimeout = 30000
                con.useCaches = false
                con.requestMethod = "GET"
                con.setRequestProperty("Authorization", "KakaoAK 3c17fc3b27408ecf89118782ddbb2608")

                //문자열을 받아오기 위한 인스턴스 생성
                val sb = StringBuilder()
                val br = BufferedReader(InputStreamReader(con.inputStream))

                //문자열을 읽어서 sb에 저장
                while(true){
                    val line = br.readLine()
                    if(line == null){
                        break
                    }
                    sb.append(line)
                }

                //정리
                br.close()
                con.disconnect()

                //데이터 확인
                Log.e("문자열", sb.toString())

                //JSON Parsing
                if(TextUtils.isEmpty(sb.toString())){
                    //sb 내용이 없으면
                    Toast.makeText(this@KakaoOpenApiActivity, "쿼리의 횟수가 초과되었거나 네트워크에 문제가 발생했습니다.", Toast.LENGTH_LONG).show()
                    return
                }else{
                    //문자열을 JSONObject 로 변환
                    val data = JSONObject(sb.toString())

                    //documents 키의 데이터를 JSONArray로 가져오기
                    val documents = data.getJSONArray("documents")
                    var result : String = ""
                    //Log.e("documents", documents.toString())
                    //반복문 수행
                    for(i in 0 until documents.length()){
                        //배열 안의 요소를 JSONObject 로 가져오기
                        val document = documents.getJSONObject(i)
                        //title과 price 가져오기
                        val title = document.getString("title")
                        val price = document.getInt("price")
                        result += "제목:${title} 가격:${price}\n"
                    }

                    //핸들러에게 전송할 메시지를 생성
                    val msg = Message()
                    msg.obj = result
                    //핸들러에게 메시지 전송
                    handler.sendMessage(msg)
                }
            }
        }.start()
    }
}