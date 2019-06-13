package isel.pdm.yama.model

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.RequestFuture
import isel.pdm.yama.R
import isel.pdm.yama.YamaApplication
import isel.pdm.yama.net.GetRequest
import isel.pdm.yama.net.ProfileDto
import isel.pdm.yama.net.TeamDto

class ApiService(private val app : YamaApplication) {

    private val queue = app.queue

    fun getMembers(url: String, success: (Array<ProfileDto>) -> Unit) {
        val queue = queue
        val req = GetRequest(
            Array<ProfileDto>::class.java, url,
            Response.Listener { success(it) },
            Response.ErrorListener { errorHandler(it) })
        queue.add(req)
    }

    fun verifyOrg(url: String, success: (Array<TeamDto>) -> Unit) {
        val queue = queue
        val req = GetRequest(Array<TeamDto>::class.java, url,
            Response.Listener { success(it) },
            Response.ErrorListener { errorHandler(it) })
        queue.add(req)
    }

    fun attemptLogin(url: String, success: (dto: ProfileDto) -> Unit) {
        val queue = queue
        val req = GetRequest(ProfileDto::class.java, url,
            Response.Listener { success(it) },
            Response.ErrorListener {
                errorHandler(it)
            })
        queue.add(req)
    }

    fun getImage(url: String, success: (Bitmap) -> Unit) {
        val request = queue
        val imageReq = ImageRequest(url, Response.Listener<Bitmap> {
            success(it)
        }, 200, 200, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
            Response.ErrorListener {
                Toast.makeText(app, "Error", Toast.LENGTH_LONG).show()
            })
        request.add(imageReq)
    }

    private fun errorHandler(it: VolleyError) {
        if (it.networkResponse == null) Toast.makeText(app, it.message, Toast.LENGTH_LONG).show()
        else {
            when (it.networkResponse.statusCode) {
                401 -> Toast.makeText(app, R.string.invalid_access_token, Toast.LENGTH_LONG).show()
                404 -> Toast.makeText(app, R.string.resource_not_found, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun syncFetchTeams(token: String, org: String) : Array<TeamDto> {
        val url = "https://api.github.com/orgs/$org/teams?access_token=$token"
        val future  : RequestFuture<Array<TeamDto>> = RequestFuture.newFuture()
        queue.add(GetRequest(Array<TeamDto>::class.java, url, future, future))
        return try{
            future.get()
        }catch (e : Exception ){
            arrayOf()
        }
    }

    fun syncFetchMembers(token : String, team: Int): Array<ProfileDto>{
        val url = "https://api.github.com/teams/$team/members?access_token=$token"
        val future  : RequestFuture<Array<ProfileDto>> = RequestFuture.newFuture()
        queue.add(GetRequest(Array<ProfileDto>::class.java, url, future, future))
        return try{
            future.get()
        }catch (e : Exception ){
            arrayOf()
        }
    }

}
