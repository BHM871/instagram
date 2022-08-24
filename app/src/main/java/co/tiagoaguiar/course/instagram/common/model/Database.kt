package co.tiagoaguiar.course.instagram.common.model

import android.content.SharedPreferences
import java.util.*

object Database {

    val usersAuth = hashSetOf<UserAuth>()
    val photo = hashMapOf<String, Photo>()
    val posts = hashMapOf<String, MutableSet<Post>>()
    val feed = hashMapOf<String, MutableSet<Post>>()
    val followers = hashMapOf<String, Set<String>>()

    var sessionAuth: UserAuth? = null

    init {
        val userA = UserAuth(UUID.randomUUID().toString(), "UserA", "user_a", "userA@gmail.com", "12345678")
        val userB = UserAuth(UUID.randomUUID().toString(), "UserB", "user_b", "userB@gmail.com", "87654321")

        usersAuth.add(userA)
        usersAuth.add(userB)

        posts[userA.uuid] = mutableSetOf()
        feed[userA.uuid] = mutableSetOf()
        followers[userA.uuid] = mutableSetOf()

        posts[userB.uuid] = mutableSetOf()
        feed[userB.uuid] = mutableSetOf()
        followers[userB.uuid] = mutableSetOf()


        sessionAuth = usersAuth.first()
    }

}