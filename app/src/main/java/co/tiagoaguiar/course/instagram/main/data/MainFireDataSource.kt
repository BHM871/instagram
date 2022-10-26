package co.tiagoaguiar.course.instagram.main.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import com.google.firebase.auth.FirebaseAuth

class MainFireDataSource : MainDataSource {

    override fun logout(callback: RequestCallback<Boolean>) {
        FirebaseAuth.getInstance()
            .signOut()

        callback.onSuccess(true)
        callback.onComplete()
    }
}