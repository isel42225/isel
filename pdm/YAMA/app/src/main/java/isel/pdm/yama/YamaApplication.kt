package isel.pdm.yama

import android.app.Application
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.work.*
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import isel.pdm.yama.model.ApiService
import isel.pdm.yama.model.RoomService
import isel.pdm.yama.model.YamaDB
import isel.pdm.yama.model.YamaRepository
import java.util.concurrent.TimeUnit

class YamaApplication : Application()  {

    val DB_UPDATE_JOB_ID : String = "DB_UPDATE_JOB_ID"

    lateinit var queue: RequestQueue
    lateinit var workManager : WorkManager
    lateinit var repo : YamaRepository
    lateinit var database : YamaDB


    override fun onCreate() {
        super.onCreate()
        queue = Volley.newRequestQueue(this)
        workManager = WorkManager.getInstance()
        database = Room.databaseBuilder(this, YamaDB::class.java, "yama-db").build()
        repo = YamaRepository(this, ApiService(this), RoomService(database))

        scheduleTeamsUpdate()
    }


    private fun scheduleTeamsUpdate() {

        val request = PeriodicWorkRequestBuilder<UpdateTeamsWorker>(
            1, TimeUnit.DAYS)
                .setConstraints(Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .setRequiresCharging(true)
                .build())
            .build()

        workManager.enqueueUniquePeriodicWork(DB_UPDATE_JOB_ID, ExistingPeriodicWorkPolicy.KEEP, request)
    }
}