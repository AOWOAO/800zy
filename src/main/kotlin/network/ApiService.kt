package network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ApiService {

    @GET("users/list")
    fun getUserList(): Observable<ResponseBody>

}