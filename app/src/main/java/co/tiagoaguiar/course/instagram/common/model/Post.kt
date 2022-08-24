package co.tiagoaguiar.course.instagram.common.model

import android.net.Uri

data class Post(
    val uuid: String,
    val uri: Uri,
    val description: String,
    val timestamp: Long,
    val publisher: UserAuth,
    var countClick: Int = 0
)