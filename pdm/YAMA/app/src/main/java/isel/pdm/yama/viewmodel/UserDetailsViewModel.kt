package isel.pdm.yama.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import isel.pdm.yama.YamaApplication

class UserDetailsViewModel (private val app : YamaApplication) : AndroidViewModel(app){

    val image : MutableLiveData<Bitmap> = MutableLiveData()

    fun getAvatar(avatar: String){
        app.repo.fetchImage(avatar, {it -> image.value = it})
    }
}