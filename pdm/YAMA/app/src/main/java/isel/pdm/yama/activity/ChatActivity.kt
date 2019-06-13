package isel.pdm.yama.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import isel.pdm.yama.Message
import isel.pdm.yama.MessageBoard
import isel.pdm.yama.R
import isel.pdm.yama.YamaApplication
import isel.pdm.yama.adapter.ChatAdapter
import isel.pdm.yama.utils.SharedPrefAccess
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_chat.*

class ChatActivity : AppCompatActivity() {

    private fun getViewModelFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MessageBoard(this@ChatActivity.application as YamaApplication) as T
        }
    }

    lateinit var user : String
    lateinit var team : String
    lateinit var board : MessageBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatView.setHasFixedSize(true)
        chatView.layoutManager = LinearLayoutManager(this)

        val prefAccess = SharedPrefAccess(this)

        user = prefAccess.getLogin()

        team = intent.getStringExtra("team")

        board = ViewModelProviders
                    .of(this, getViewModelFactory())
                    .get(MessageBoard::class.java)

        board.registerChannel(team)

        board.messages.observe(this, Observer {
            chatView.adapter = ChatAdapter(board)
        })


        sendButton.setOnClickListener{ sendMessage()}
    }

    private fun sendMessage(){
        val txt = sendText.text.toString()
        sendText.text.clear()
        closeKeyboard()
        if(txt.isNotBlank()) {
            val msg = Message(user, txt)
            board.post(team, msg)
        }
    }

    private fun closeKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        sendText.clearFocus()
    }

}
