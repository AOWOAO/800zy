package network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy

object ApiRequest {

    const val BASE_URL = "https://api.github.com/"
    const val DEBUG = true

    /**
     * by lazy 懒加载（延迟加载）
     */
    private val mRetrofit by lazy { createRetrofit() }
    /**
     * 默认接口实现类的实例
     */
    val gClient by lazy { createService(ApiService::class.java) }

    /**
     * 生成接口实现类的实例
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return mRetrofit.create(serviceClass)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 设置OkHttpclient
            .client(initOkhttpClient())
            // RxJava2
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            // 字符串
            .addConverterFactory(ScalarsConverterFactory.create())
            // Gson
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 每次请求都会走拦截器
     *
     * 只需要修改Constants.TOKEN就可以
     */
    private fun initOkhttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (DEBUG) {
            // OkHttp日志拦截器
            builder.addInterceptor(HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("10.187.132.23", 80)))
        }

       /* builder.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                // 设置请求头，从Debug中看到修改Constants.TOKEN请求header头也会修改
                .header("Authorization", Constants.TOKEN)
                .method(original.method(), original.body())
                .build()
            return@addInterceptor chain.proceed(request)
        }*/
        return builder.build()
    }
}
