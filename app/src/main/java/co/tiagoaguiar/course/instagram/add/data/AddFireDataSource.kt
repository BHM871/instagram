package co.tiagoaguiar.course.instagram.add.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddFireDataSource : AddDataSource {

    override fun createPost(
        userUUID: String,
        uri: Uri,
        caption: String,
        callback: RequestCallback<Boolean>
    ) {

        val imgLastPath = uri.lastPathSegment ?: throw RuntimeException("Image not found")

        val imgRef = FirebaseStorage.getInstance().reference
            .child("images/")
            .child(userUUID)
            .child(imgLastPath)

        imgRef.putFile(uri)
            .addOnSuccessListener { resPut ->

                imgRef.downloadUrl
                    .addOnSuccessListener { resDow ->

                        val meRef = FirebaseFirestore.getInstance()
                            .collection("/users")
                            .document(userUUID)

                            meRef.get()
                            .addOnSuccessListener { resUser ->

                                val me = resUser.toObject(User::class.java) ?: throw RuntimeException("User not found")

                                val postRef = FirebaseFirestore.getInstance()
                                    .collection("/posts")
                                    .document(userUUID)
                                    .collection("posts")
                                    .document()

                                val post = Post(
                                    uuid = postRef.id,
                                    photoUrl = resDow.toString(),
                                    description = caption,
                                    timestamp = System.currentTimeMillis(),
                                    publisher = me
                                )

                                postRef.set(post)
                                    .addOnSuccessListener { resPost ->

                                        meRef.update("postCount", FieldValue.increment(1))

                                        FirebaseFirestore.getInstance()
                                            .collection("/feeds")
                                            .document(userUUID)
                                            .collection("posts")
                                            .document(postRef.id)
                                            .set(post)
                                            .addOnSuccessListener { resMyFeed ->

                                                FirebaseFirestore.getInstance()
                                                    .collection("/followers")
                                                    .document(userUUID)
                                                    .get()
                                                    .addOnSuccessListener { resMyFollowers ->

                                                        if (resMyFollowers!!.exists()){

                                                            val followers = resMyFollowers.get("followers") as List<String>

                                                            for (follower in followers) {

                                                                FirebaseFirestore.getInstance()
                                                                    .collection("/feeds")
                                                                    .document(follower)
                                                                    .collection("posts")
                                                                    .document(postRef.path)
                                                                    .set(post)
                                                            }
                                                        }

                                                        callback.onSuccess(true)
                                                    }
                                                    .addOnFailureListener { exception ->
                                                        callback.onFailure(exception.message ?: "Failure add in feed")
                                                    }
                                                    .addOnCompleteListener {
                                                        callback.onComplete()
                                                    }

                                            }
                                            .addOnFailureListener { exception ->
                                                callback.onFailure(exception.message ?: "Failure in create post")
                                                callback.onComplete()
                                            }
                                    }
                                    .addOnFailureListener { exception ->
                                        callback.onFailure(exception.message ?: "Failure in create post")
                                        callback.onComplete()
                                    }
                            }
                            .addOnFailureListener { exception ->
                                callback.onFailure(exception.message ?: "User not found")
                                callback.onComplete()
                            }
                    }
                    .addOnFailureListener { exception ->
                        callback.onFailure(exception.message ?: "Failure in download image")
                        callback.onComplete()
                    }
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Failure in upload image")
                callback.onComplete()
            }
    }

}