package co.tiagoaguiar.course.instagram.splash

import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView

interface SplashScreen {

    interface Presenter : BasePresenter{
        fun authenticate()
    }

    interface View : BaseView<Presenter>{
        fun goToMainScreen()
        fun goToLoginScreen()
    }

}