package co.tiagoaguiar.course.instagram.common.model

import android.net.Uri

data class UserAuth(
    val uuid: String,
    val name: String,
    var username: String,
    val email: String,
    var password: String,
    var photoUri: Uri? = null,
    var postCount: Int = 0,
    var followingCount: Int = 0,
    var followersCount: Int = 0
)
