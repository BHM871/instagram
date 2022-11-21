package co.tiagoaguiar.course.instagram.main

import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView

interface Main {

    interface Presenter : BasePresenter{
        fun logout()
    }

    interface View : BaseView<Presenter> {
        fun success()
    }
}