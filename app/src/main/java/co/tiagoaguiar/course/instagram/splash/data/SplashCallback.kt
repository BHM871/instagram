package co.tiagoaguiar.course.instagram.splash.data

import co.tiagoaguiar.course.instagram.common.model.User

interface SplashCallback {
    fun onSuccess(data: Pair<User, Boolean?>)
    fun onFailure()
}