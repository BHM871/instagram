package co.tiagoaguiar.course.instagram.splash.data

import com.google.firebase.auth.FirebaseAuth

class SplashFireDataSource : SplashDataSource {

    override fun session(callback: SplashCallback) {
//        if (FirebaseAuth.getInstance().uid != null) {
//            callback.onSuccess()
//        } else {
//            callback.onFailure()
//        }
        callback.onFailure()
    }
}