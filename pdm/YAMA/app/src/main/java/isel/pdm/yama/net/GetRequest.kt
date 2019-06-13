package isel.pdm.yama.net

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
* The GetRequest is responsible for the GET Requests in the Github API
* @param clazz This is the class of the T
* @param url  This is the url of the Request
* @param success  This is the Response Listener in case of success
* @param error  This is the Response Listener in case of Error
*/

class GetRequest<T>( private val clazz: Class<T>, url: String , success: Response.Listener<T>, error: Response.ErrorListener)
    : JsonRequest<T>(Request.Method.GET, url, "", success, error) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<T> {
        val data = String(response.data)
        val parser = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val dto = parser.readValue(data, clazz)
        return Response.success(dto, null)
    }
}
