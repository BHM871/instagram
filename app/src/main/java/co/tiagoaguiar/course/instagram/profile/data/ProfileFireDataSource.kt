package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProfileFireDataSource : ProfileDataSource {

    private val servError: String = "Serv error"

    override fun fetchUserProfile(
        userUUID: String,
        callback: RequestCallback<Pair<User, Boolean?>>
    ) {
        FirebaseFirestore.getInstance()
            .collection("/users")
            .document(userUUID)
            .get()
            .addOnSuccessListener { res ->
                val user = res.toObject(User::class.java)

                when(user) {
                    null -> callback.onFailure("Error in user converting")
                    else -> {
                        if (user.uuid ==  FirebaseAuth.getInstance().uid) {
                            callback.onSuccess(Pair(user, null))
                        } else {
                            FirebaseFirestore.getInstance()
                                .collection("/followers")
                                .document(FirebaseAuth.getInstance().uid!!)
                                .collection("followers")
                                .whereEqualTo("uuid", userUUID)
                                .get()
                                .addOnSuccessListener{ response ->
                                    callback.onSuccess(Pair(user, !response.isEmpty))
                                }
                                .addOnFailureListener { exception ->
                                    callback.onFailure(exception.message ?: servError)
                                }
                                .addOnCompleteListener {
                                    callback.onComplete()
                                }
                        }
                    }
                }

            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: servError)
            }
    }

    override fun fetchUserPost(userUUID: String, callback: RequestCallback<List<Post>>) {
        FirebaseFirestore.getInstance()
            .collection("/posts")
            .document(userUUID)
            .collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { res ->
                val posts = mutableListOf<Post>()

                val documents = res.documents

                for (document in documents) {

                   val post = document.toObject(Post::class.java)
                    post?.let {
                        posts.add(it)
                    }

                }

                callback.onSuccess(posts)
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: servError)
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }

    override fun followUser(
        userUUID: String,
        isFollow: Boolean,
        callback: RequestCallback<Boolean>
    ) {
        //TODO: fazer depois
    }
}