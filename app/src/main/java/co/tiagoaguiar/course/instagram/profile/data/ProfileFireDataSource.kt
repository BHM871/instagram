package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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

                when (user) {
                    null -> callback.onFailure("Error in user converting")
                    else -> {
                        if (user.uuid == FirebaseAuth.getInstance().uid) {
                            callback.onSuccess(Pair(user, null))
                        } else {
                            FirebaseFirestore.getInstance()
                                .collection("/followers")
                                .document(userUUID)
                                .get()
                                .addOnSuccessListener { response ->
                                    if (!response!!.exists()) {
                                        callback.onSuccess(Pair(user, false))
                                    } else {
                                        val list = response.get("followers") as List<String>
                                        callback.onSuccess(
                                            Pair(
                                                user,
                                                list.contains(FirebaseAuth.getInstance().uid)
                                            )
                                        )
                                    }
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
        val userIdSession =
            FirebaseAuth.getInstance().uid ?: throw RuntimeException("User not found")

        if (userUUID == userIdSession) {
            callback.onComplete()
            return
        }

        FirebaseFirestore.getInstance()
            .collection("/followers")
            .document(userUUID)
            .update(
            "followers", if (isFollow) FieldValue.arrayUnion(userIdSession)
            else FieldValue.arrayRemove(userIdSession)
        )
            .addOnSuccessListener { resFollowers ->

                if (isFollow) {
                    followersIncrement(userUUID, 1)
                    addFeed(userIdSession, userUUID, callback)
                } else {
                    followersIncrement(userUUID, -1)
                    removeFeed(userIdSession, userUUID, callback)
                }

            }
            .addOnFailureListener { exception ->
                val err = exception as? FirebaseFirestoreException

                if (err?.code == FirebaseFirestoreException.Code.NOT_FOUND) {
                    FirebaseFirestore.getInstance()
                        .collection("/followers")
                        .document(userUUID)
                        .set(
                            hashMapOf(
                                "folloers" to listOf(userIdSession)
                            )
                        )
                        .addOnSuccessListener {
                            followersIncrement(userUUID, 1)
                            addFeed(userIdSession, userUUID, callback)
                        }
                        .addOnFailureListener { exceptionIner ->
                            callback.onFailure(exception.message ?: "Error in create follower")
                            callback.onComplete()
                        }
                }

                callback.onFailure(exception.message ?: "Error in follow user")
                callback.onComplete()
            }
    }

    private fun addFeed(idSession: String, idUser: String, callback: RequestCallback<Boolean>) {
        FirebaseFirestore.getInstance()
            .collection("/posts")
            .document(idUser)
            .collection("posts")
            .get()
            .addOnSuccessListener { resFeed ->
                val documents = resFeed.documents

                for (document in documents) {
                    val post = document.toObject(Post::class.java)
                        ?: throw RuntimeException("Converting error")

                    FirebaseFirestore.getInstance()
                        .collection("/feeds")
                        .document(idSession)
                        .collection("posts")
                        .document()
                        .set(post)
                }

                FirebaseFirestore.getInstance()
                    .collection("/following")
                    .document(idSession)
                    .collection("following")
                    .document()
                    .set(idUser)
                    .addOnSuccessListener { res ->

                        followingIncrement(idSession, 1)
                        callback.onSuccess(true)
                    }
                    .addOnFailureListener { exception ->
                        callback.onFailure(exception.message ?: "Error")
                    }
                    .addOnCompleteListener {
                        callback.onComplete()
                    }

            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Error")
                callback.onComplete()
            }
    }

    private fun removeFeed(idSession: String, idUser: String, callback: RequestCallback<Boolean>) {
        FirebaseFirestore.getInstance()
            .collection("/feeds")
            .document(idSession)
            .collection("posts")
            .whereEqualTo("publisher.uuid", idUser)
            .get()
            .addOnSuccessListener { resFeed ->
                val documents = resFeed.documents

                for (document in documents){
                    document.reference.delete()
                }

                FirebaseFirestore.getInstance()
                    .collection("/following")
                    .document(idSession)
                    .update("following", FieldValue.arrayRemove(idUser))
                    .addOnSuccessListener { res ->

                        followingIncrement(idSession, -1)
                        callback.onSuccess(true)

                    }
                    .addOnFailureListener { exception ->
                        callback.onFailure(exception.message ?: "Error")
                        callback.onComplete()
                    }

            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Error")
                callback.onComplete()
            }
    }

    private fun followersIncrement(user: String, l: Long) {
        FirebaseFirestore.getInstance()
            .collection("/users")
            .document(user)
            .update("followers", FieldValue.increment(l))
    }

    private fun followingIncrement(user: String, l: Long) {
        FirebaseFirestore.getInstance()
            .collection("/users")
            .document(user)
            .update("following", FieldValue.increment(l))
    }
}