package isel.pdm.yama

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

data class Message(val sender : String,
                   val content : String)

class MessageBoard(app : YamaApplication) : AndroidViewModel(app){

    val store = FirebaseFirestore.getInstance()
    val messages =  MutableLiveData <ArrayList<Message>>()

    fun registerChannel(name : String){
        val docRef = store.collection(name)
        docRef.addSnapshotListener{ snapshot, exc ->

            if(exc != null){
                return@addSnapshotListener
            }

            if(snapshot != null ){
                val docs = ArrayList<Message>()
                snapshot
                    .documents
                    .map{it -> it.data?.values?.toList()}
                    .map{list -> parseDocSnapshot(list) }
                    .forEach { msg -> docs.add(msg)}

                messages.value = docs
            }
        }
    }

    fun post(name: String , msg : Message){
        val now = Calendar.getInstance().time
        val formater = SimpleDateFormat("yyyy.MM.dd E 'at' hh:mm:ss a", Locale.getDefault())
        val doc = formater.format(now)


        val docRef = store.collection(name).document(doc)
        docRef.set(msg)
    }
}

private fun parseDocSnapshot(l : List<Any>?) : Message{
    val sender = l?.get(0).toString()
    val content = l?.get(1).toString()
    return Message(sender,  content)
}