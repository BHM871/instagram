package co.tiagoaguiar.course.instagram.search.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.firestore.FirebaseFirestore

class SearchFireDataSource : SearchDataSource {

    private val servError: String = "Serv error"

    override fun fetchUsers(username: String, callback: RequestCallback<List<User>>) {
        FirebaseFirestore.getInstance()
            .collection("/users")
            .whereGreaterThanOrEqualTo("username", username)
            .whereLessThanOrEqualTo("username", username + "\uf8ff")
            .get()
            .addOnSuccessListener { res ->
                val users = mutableListOf<User>()

                val documents = res.documents

                for (document in documents) {

                    val user = document.toObject(User::class.java)
                    user?.let {
                        users.add(it)
                    }

                }

                callback.onSuccess(users)
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: servError)
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }
}