package co.tiagoaguiar.course.instagram.login.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import com.google.firebase.auth.FirebaseAuth
import co.tiagoaguiar.course.instagram.common.model.User

class LoginFireDataSource : LoginDataSource {

    override fun login(
        email: String,
        password: String,
        callback: RequestCallback<User>
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { res ->
                if (res != null) {
                    callback.onSuccess((res as User))
                } else {
                    callback.onFailure("Sing in error")
                }
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Serv error")
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }
}