package org.d3if1008.fundamentals222.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.BuildConfig
import cz.msebera.android.httpclient.Header
import org.d3if1008.fundamentals222.Model.UserItems
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUser = MutableLiveData<ArrayList<UserItems>>()
    val navigateToDetails = MutableLiveData<Event<String>>()

    fun setUsers(username:String){
        val listItems = ArrayList<UserItems>()

        val urlSearch =  "https://api.github.com/search/users?q=$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", 0.toString())
        client.addHeader("User-Agent","reqsuest")

        client.get(urlSearch, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try{
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for(i in 0 until items.length()){
                        val item = items.getJSONObject(i)
                        val user = UserItems()
                        user.username = item.getString("login")
                        user.html_url = item.getString("html_url")
                        user.avatar_url = item.getString("avatar_url")
                        listItems.add(user)
                    }
                    listUser.postValue(listItems)

                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                navigateToDetails.value = Event(errorMessage)
            }
        })
    }

    fun getUser() : LiveData<ArrayList<UserItems>> {
        return listUser
    }


}