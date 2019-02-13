interface ResponseCallBack<RESULT> {

    fun onError(msg: String)

    fun onSuccess(result: RESULT)
}