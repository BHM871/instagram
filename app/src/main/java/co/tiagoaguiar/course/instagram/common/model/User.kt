package co.tiagoaguiar.course.instagram.common.model

data class User(
    val uuid: String? = null,
    val name: String? = null,
    var username: String? = null,
    val email: String? = null,
    var password: String,
    var photoUrl: String? = null,
    var postCount: Int = 0,
    var following: Int = 0,
    var followers: Int = 0
)
