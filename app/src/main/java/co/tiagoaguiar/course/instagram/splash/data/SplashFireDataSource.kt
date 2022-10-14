package co.tiagoaguiar.course.instagram.splash.data

import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashFireDataSource : SplashDataSource {

    override fun session(callback: SplashCallback) {
        if (FirebaseAuth.getInstance().uid != null) {
            FirebaseFirestore.getInstance()
                .collection("/users")
                .document(FirebaseAuth.getInstance().uid!!)
                .get()
                .addOnSuccessListener { res ->
                    val user = res.toObject(User::class.java)
                    if (user != null) callback.onSuccess(Pair(user, null))
                    else callback.onFailure()
                }
                .addOnFailureListener {
                    callback.onFailure()
                }
        } else {
            callback.onFailure()
        }
    }
}