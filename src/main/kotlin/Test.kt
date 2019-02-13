import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import network.ApiRequest
import okhttp3.ResponseBody

fun main(args: Array<String>) {

    ApiRequest.gClient.getUserList()
        //.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(object : Observer<ResponseBody> {
            override fun onSubscribe(d: Disposable) {
                println("start")
            }

            override fun onNext(t: ResponseBody) {
                println(t.string())
            }

            override fun onError(e: Throwable) {
                println(e.toString())
            }

            override fun onComplete() {
                println("done")
            }
        }
        )


}