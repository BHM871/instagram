package co.tiagoaguiar.course.instagram.common.model

data class UserAuth(
    val uuid: String,
    val name: String,
    var username: String,
    val email: String,
    var password: String,
    var postCount: Int = 0,
    var followingCount: Int = 0,
    var followersCount: Int = 0
)
