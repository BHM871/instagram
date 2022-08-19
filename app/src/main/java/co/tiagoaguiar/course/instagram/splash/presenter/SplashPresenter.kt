package co.tiagoaguiar.course.instagram.splash.presenter

import co.tiagoaguiar.course.instagram.splash.SplashScreen
import co.tiagoaguiar.course.instagram.splash.data.SplashCallback
import co.tiagoaguiar.course.instagram.splash.data.SplashRepository

class SplashPresenter(
    private var view: SplashScreen.View?,
    private val repository: SplashRepository
) : SplashScreen.Presenter {

    override fun authenticate() {
        repository.session(object : SplashCallback{
            override fun onSuccess() {
                view?.goToMainScreen()
            }

            override fun onFailure() {
                view?.goToLoginScreen()
            }

        })
    }

    override fun onDestroy() {
        view = null
    }

}