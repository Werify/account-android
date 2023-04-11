package net.werify.id

interface RequestCallback<Result> {
    fun onError(throwable: Throwable)
    fun onSuccess(result: Result)
}