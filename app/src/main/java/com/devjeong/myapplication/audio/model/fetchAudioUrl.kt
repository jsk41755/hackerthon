package com.devjeong.myapplication.audio.model

import android.content.Context
import android.util.Log
import com.devjeong.myapplication.BuildConfig
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

fun fetchAudioUrl(context: Context, text: String, speaker: String) : String{
    val clientId = BuildConfig.NAVER_CLOVA_VOICE_ID
    val clientSecret = BuildConfig.NAVER_CLOVA_VOICE_API

    lateinit var outputFile : File

    try {
        val encodedText = URLEncoder.encode(text, "UTF-8") // 텍스트 인코딩
        val apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts"
        val url = URL(apiURL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId)
        connection.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret)
        // POST 요청 설정
        var postParams = "speaker=$speaker&volume=0&speed=4&pitch=0&format=wav&text=$encodedText"
        if(speaker == "vyuna" || speaker == "vara"){
            postParams = "$postParams&emotion=2"
        }

        Log.d("speaker", speaker)
        connection.doOutput = true
        val wr = DataOutputStream(connection.outputStream)
        wr.writeBytes(postParams)
        wr.flush()
        wr.close()
        val responseCode = connection.responseCode
        val br: InputStream

        if (responseCode == 200) { // 정상 호출
            br = connection.inputStream
            val tempName = Date().time.toString()
            outputFile = File("${context.cacheDir}/$tempName.wav")
            outputFile.createNewFile()
            val outputStream = FileOutputStream(outputFile)

            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (br.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            br.close()
            outputStream.close()

            return outputFile.absolutePath
        }
    } catch (e: Exception) {
        e.printStackTrace() // 예외 출력
    }
    return ""
}

