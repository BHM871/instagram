package co.tiagoaguiar.course.instagram.common.model

data class Post(
    val uuid: String,
    val uri: Int,
    val description: String,
    val timestamp: Long,
    val publisher: UserAuth,
    var countClick: Int = 0
)