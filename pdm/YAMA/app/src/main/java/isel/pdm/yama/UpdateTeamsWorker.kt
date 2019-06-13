package isel.pdm.yama

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.VolleyError
import isel.pdm.yama.utils.SharedPrefAccess

class UpdateTeamsWorker(ctx : Context, params : WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        return try {
            val app = applicationContext as YamaApplication

            val prefAccess = SharedPrefAccess(app)

            val org = prefAccess.getOrg()
            val token = prefAccess.getToken()

            if(org != "" && token != "") {
                app.repo.syncUpdateTeams(token, org)
                Result.SUCCESS
            }
            else Result.RETRY
        }catch (err : VolleyError){
            if(canRecover(err)) Result.RETRY else Result.FAILURE

        }
    }

    private fun canRecover(error: VolleyError): Boolean {
        val statusCode =
            if (error.networkResponse != null) error.networkResponse.statusCode
            else 0
        return statusCode in 500..599
    }

}
