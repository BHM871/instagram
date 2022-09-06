package co.tiagoaguiar.course.instagram.main.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback

interface MainDataSource {

    fun logout(callback: RequestCallback<Boolean>)

}