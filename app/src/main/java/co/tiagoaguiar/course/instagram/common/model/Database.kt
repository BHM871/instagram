package co.tiagoaguiar.course.instagram.common.model

import co.tiagoaguiar.course.instagram.R
import java.util.*

object Database {

    val usersAuth = hashSetOf<UserAuth>()
    val photo = hashSetOf<Photo>()
    val posts = hashMapOf<String, Set<Post>>()
    val feed = hashMapOf<String, Set<Post>>()

    var sessionAuth: UserAuth? = null

    init {
        usersAuth.add(UserAuth(UUID.randomUUID().toString(), "UserA", "user_a", "userA@gmail.com", "12345678"))
        usersAuth.add(UserAuth(UUID.randomUUID().toString(), "UserB", "user_b", "userB@gmail.com", "87654321"))

        sessionAuth = usersAuth.first()
    }

}