package co.tiagoaguiar.course.instagram.common.model

object Database {

    val usersAuth = mutableListOf<UserAuth>()
    val posts = hashMapOf<String, MutableSet<Post>>()
    val feed = hashMapOf<String, MutableSet<Post>>()
    val followers = hashMapOf<String, MutableSet<String>>()

    var sessionAuth: UserAuth? = null

    init {
        usersAuth.add(UserAuth("uisdsad", "UserA", "user_a", "a@a.com", "123123123"))
    }

}