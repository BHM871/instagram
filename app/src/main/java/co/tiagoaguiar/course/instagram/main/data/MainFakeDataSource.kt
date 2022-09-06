package co.tiagoaguiar.course.instagram.main.data

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database

class MainFakeDataSource : MainDataSource {

    override fun logout(callback: RequestCallback<Boolean>) {
        Handler(Looper.getMainLooper()).postDelayed({
            Database.sessionAuth = null

            callback.onSuccess(true)
            callback.onComplete()
        }, 500)
    }
}