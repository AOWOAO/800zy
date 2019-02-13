package zy

import com.google.gson.Gson
import okhttp3.*
import org.dom4j.DocumentHelper
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy

fun main(args: Array<String>) {
    httpGet("http://cj.809zy.com/inc/zyapimac.php?ac=videolist&pg=3&t=1")
}

fun xml2json(xml: String?): ArrayList<DataBean> {

    xml?.let {
        // 得到document 对象
        val document = DocumentHelper.parseText(xml)
        // 获取rootElement (根节点)
        val rootElement = document.rootElement
        // 获取listElement (list节点)
        val listElement = rootElement.element("list")
        // 获取videoElements 列表 (video节点)
        val videoElements = listElement.elements("video")

        val dataList = ArrayList<DataBean>()

        videoElements.forEach {
            // 更新日期
            val last = it.element("last").text
            // 标题
            val name = it.element("name").text
            // 图片
            val pic = it.element("pic").text
            // 播放地址
            val dd = it.element("dl").element("dd").text.split("$")[1]
            // println("$last $name $pic $dd")
            dataList.add(DataBean(name, pic, dd, last))
        }

        return dataList
    }

    return ArrayList()

}

fun httpGet(url: String) {
    val httpClient by lazy {
        OkHttpClient.Builder()
        .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("10.187.132.23", 80)))
        .build()
    }

    var request = Request.Builder()
        .url(url)
        .get()
        .build()

    val call: Call = httpClient.newCall(request)
    call.enqueue(object: Callback {

        override fun onResponse(call: Call, response: Response) {

            var xml = response.body()?.string()

            val dataList = xml2json(xml)

            val gson = Gson()
            val json = gson.toJson(dataList)
            println(json)
        }

        override fun onFailure(call: Call, e: IOException) {
            println(e.toString())
        }
    })


}

