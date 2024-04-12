package pe.edu.idat.toinvoicemobileapp.retrofit.api

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class DBApi {
    fun postafirmativo(context: Context, url: String, json: String, autorizacion: String? = null): Any? {
        try {
            val client = OkHttpClient()
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body = json.toRequestBody(mediaType)
            val request = Request.Builder()
                .url("https://api.pse.pe/api/v1/dc55239ea28b4e0aa755c3894a681811f8c19619044b40cfbb725ece2d5a612f")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.IjIwYmUyMjIyYzAwMTQyMTc5MDQxZDA2OTNiMDU2NmUyMTI1M2ZjNzFjNGE4NDBkMGE3MjNlOTRjZjE1MmEzM2Mi.mXBrOrQ8bykTFePv8Kx6RQdmjsHl4X6fKm0EhzOSZjU")
                .apply {
                    autorizacion?.let {
                        addHeader("Authorization", "Bearer $it")
                    }
                }
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body?.string()
            val gson = Gson()
            return gson.fromJson(responseData, Any::class.java)
        } catch (ex: Exception) {
            ex.message?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            return null
        }
    }
}
