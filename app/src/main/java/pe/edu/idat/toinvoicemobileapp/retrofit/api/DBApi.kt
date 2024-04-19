package pe.edu.idat.toinvoicemobileapp.retrofit.api

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class DBApi {
    suspend fun postafirmativo(json: String, autorizacion: String? = null): Any? {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url("https://api.pse.pe/api/v1/dc55239ea28b4e0aa755c3894a681811f8c19619044b40cfbb725ece2d5a612f")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.IjIwYmUyMjIyYzAwMTQyMTc5MDQxZDA2OTNiMDU2NmUyMTI1M2ZjNzFjNGE4NDBkMGE3MjNlOTRjZjE1MmEzM2Mi.mXBrOrQ8bykTFePv8Kx6RQdmjsHl4X6fKm0EhzOSZjU")
                .apply {
                    autorizacion?.let { addHeader("Authorization", "Bearer $it") }
                }
                .build()

            try {
                val response = client.newCall(request).execute()
                val datos = Gson().fromJson(response.body?.string(), Any::class.java)
                response.close()
                datos
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
}

