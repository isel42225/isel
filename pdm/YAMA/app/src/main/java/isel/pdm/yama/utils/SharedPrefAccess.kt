package isel.pdm.yama.utils

import android.R
import android.content.Context
import android.preference.PreferenceManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class SharedPrefAccess (ctx : Context) {
    private val ORG = "organization"
    private val LOGIN = "login"
    private val TOKEN = "token"
    private val PREF_ORG = "prefOrg"
    private val PREF_LOGIN = "prefLogin"
    private val SharedPref = PreferenceManager.getDefaultSharedPreferences(ctx)

    fun deleteSession(){
        SharedPref.edit().remove(ORG).remove(LOGIN).remove(TOKEN).apply()
    }

    fun getToken():String{
        return SharedPref.getString(TOKEN, "")!!
    }
    fun getLogin():String{
        return SharedPref.getString(LOGIN, "")!!
    }
    fun getOrg():String{
        return SharedPref.getString(ORG, "")!!
    }

    fun getPrefLogin(ctx: Context, loginView: AutoCompleteTextView): List<String>{
        val suggestions = SharedPref.getString(PREF_LOGIN, "")!!.split(",")
        return putSugestionInView(ctx, loginView, suggestions)
    }

    fun getPrefOrg(ctx: Context, loginView: AutoCompleteTextView):List<String>{
        val suggestions = SharedPref.getString(PREF_ORG, "")!!.split(",")
        return putSugestionInView(ctx, loginView, suggestions)
    }

    fun putToken(data :String){
        SharedPref.edit().putString(TOKEN, data).apply()
    }
    fun putLogin(data :String){
        SharedPref.edit().putString(LOGIN, data).apply()
    }
    fun putOrg(data :String){
        SharedPref.edit().putString(ORG, data).apply()
    }

    fun putPrefLogin(suggestions : List<String>, item: String ) {
        storePref(suggestions, item, PREF_LOGIN)
    }

    fun putPrefOrg(suggestions : List<String>, item: String){
        storePref(suggestions, item, PREF_ORG)
    }

    private fun putSugestionInView(ctx: Context, loginView: AutoCompleteTextView, suggestions: List<String>) :List<String> {
        val adapter = ArrayAdapter<String>(ctx, R.layout.select_dialog_item, suggestions)
        loginView.threshold = 1
        loginView.setAdapter(adapter)
        return suggestions
    }

    private fun storePref(suggestions : List<String>, item: String, key: String){
        if(!suggestions.contains(item)){
            val strBuilder = StringBuilder()
            suggestions.forEach{
                strBuilder.append(it).append(",")
            }
            strBuilder.append(item)
            SharedPref.edit().putString(key, strBuilder.toString()).apply()
        }
    }

}