package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFireDataSource : HomeDataSource {

    override fun fetchFeed(userUUID: String, callback: RequestCallback<List<Post>>) {
        val uid = FirebaseAuth.getInstance().uid ?: throw RuntimeException()
        FirebaseFirestore.getInstance()
            .collection("/feeds")
            .document(uid)
            .collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { res ->
                val feed = mutableListOf<Post>()

                val documents = res.documents
                for (document in documents) {
                    val post = document.toObject(Post::class.java)
                    post?.let { feed.add(it) }
                }

                callback.onSuccess(feed)
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "User not found")
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }

    override fun liked(post: Post, liked: Boolean) {
        //
    }
}