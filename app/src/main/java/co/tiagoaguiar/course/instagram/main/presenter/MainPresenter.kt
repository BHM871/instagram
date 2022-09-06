package co.tiagoaguiar.course.instagram.main.presenter

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.main.Main
import co.tiagoaguiar.course.instagram.main.data.MainRepository

class MainPresenter(
    private var view: Main.View?,
    private val repository: MainRepository
) : Main.Presenter {

    override fun logout() {
        repository.logout(object : RequestCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                view?.isLogout(data)
            }

            override fun onFailure(message: String) { }

            override fun onComplete() { }
        })
    }

    override fun onDestroy() {
        view = null
    }
}