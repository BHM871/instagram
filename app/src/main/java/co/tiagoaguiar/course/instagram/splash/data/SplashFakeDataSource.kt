package co.tiagoaguiar.course.instagram.splash.data

import co.tiagoaguiar.course.instagram.common.model.Database

class SplashFakeDataSource : SplashDataSource {

    override fun session(callback: SplashCallback) {
        when{
//            Database.sessionAuth != null -> callback.onSuccess()
//            else -> callback.onFailure()
        }
    }

}