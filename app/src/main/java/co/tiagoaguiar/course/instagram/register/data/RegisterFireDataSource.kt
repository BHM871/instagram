package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RegisterFireDataSource : RegisterDataSource {

    private val servError: String = "Serv error"

    override fun createUser(email: String, callback: RequestCallback<Boolean>) {
        FirebaseFirestore.getInstance()
            .collection("/users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->

                if (documents.isEmpty) {
                    callback.onSuccess(true)
                } else {
                    callback.onFailure("User already exists")
                }

            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: servError)
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }

    override fun createUser(
        email: String,
        name: String,
        username: String,
        password: String,
        callback: RequestCallback<Pair<User, Boolean?>>
    ) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->

                val uid = result.user?.uid

                if (uid == null) {
                    callback.onFailure(servError)
                } else {
                    FirebaseFirestore.getInstance()
                        .collection("/users")
                        .document(uid)
                        .set(
                            hashMapOf(
                                "uuid" to uid,
                                "name" to name,
                                "username" to username,
                                "email" to email,
                                "photoUrl" to null,
                                "postCount" to 0,
                                "following" to 0,
                                "followers" to 0
                            )
                        )
                        .addOnSuccessListener {
                            val newUser = User(uid, name, username, email, password)
                            callback.onSuccess(Pair(newUser, null))
                        }
                        .addOnFailureListener { exception ->
                            callback.onFailure(exception.message ?: servError)
                        }
                        .addOnCompleteListener {
                            callback.onComplete()
                        }
                }

            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: servError)
            }

    }

    override fun updateUser(userUUID: String, photoUri: Uri, callback: RequestCallback<Uri?>) {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null || photoUri.lastPathSegment == null) {
            callback.onFailure("User not found")
            return
        }

        val storageRef = FirebaseStorage.getInstance().reference

        val imageRef = storageRef.child("images/")
            .child(uid)
            .child(photoUri.lastPathSegment!!)

        imageRef.putFile(photoUri)
            .addOnSuccessListener { result ->

                imageRef.downloadUrl
                    .addOnSuccessListener { res ->

                        val userRef =
                            FirebaseFirestore.getInstance().collection("/users").document(uid)

                        userRef.get()
                            .addOnSuccessListener { document ->
                                val user = document.toObject(User::class.java)
                                val newUser = user?.copy(photoUrl = res.toString())

                                if (newUser != null) {
                                    userRef.set(newUser)
                                        .addOnSuccessListener {
                                            callback.onSuccess(photoUri)
                                        }
                                        .addOnFailureListener { exception ->
                                            callback.onFailure(
                                                exception.message ?: "Error importing image"
                                            )
                                        }
                                        .addOnCompleteListener {
                                            callback.onComplete()
                                        }
                                }
                            }
                    }
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Error importing image")
                callback.onComplete()
            }
    }

}