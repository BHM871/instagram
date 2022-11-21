package co.tiagoaguiar.course.instagram.main.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback

class MainRepository(
    private val remoteDataSource: MainDataSource,
) {

    fun logout(callback: RequestCallback<Boolean>) {
        remoteDataSource.logout(object : RequestCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                callback.onSuccess(data)
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
            }

            override fun onComplete() {
                callback.onComplete()
            }
        })
    }

}