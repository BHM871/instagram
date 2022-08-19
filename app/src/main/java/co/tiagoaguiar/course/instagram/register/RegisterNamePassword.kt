package co.tiagoaguiar.course.instagram.register

import androidx.annotation.StringRes
import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView

interface RegisterNamePassword {

    interface Presenter : BasePresenter{
        fun create(email: String, name: String, username: String, password: String, confirm: String)
    }

    interface View : BaseView<Presenter>{
        fun showProgress(enabled: Boolean)
        fun displayNameFailure(@StringRes message: Int?)
        fun displayUsernameFailure(@StringRes message: Int?)
        fun displayPasswordFailure(@StringRes message: Int?)
        fun displayConfirmFailure(@StringRes message: Int?)
        fun displayPasswordNotEquals(@StringRes message: Int?)
        fun onCreateFailure(message: String)
        fun onCreateSuccess(name: String)
    }

}