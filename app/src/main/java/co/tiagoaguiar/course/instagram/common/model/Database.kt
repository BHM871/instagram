package co.tiagoaguiar.course.instagram.common.model

import java.util.*

object Database {

    val usersAuth = mutableListOf<UserAuth>()
    val posts = hashMapOf<String, MutableSet<Post>>()
    val feed = hashMapOf<String, MutableSet<Post>>()
    val followers = hashMapOf<String, MutableSet<String>>()

    var sessionAuth: UserAuth? = null

    init {
        for (i in 0..26) {
            usersAuth.add(
                UserAuth(
                    UUID.randomUUID().toString(),
                    "User${(i + 65).toChar()}",
                    "user_${(i + 97).toChar()}",
                    "user${(i + 65).toChar()}@gmail.com",
                    "$i$i$i$i$i$i$i$i"
                )
            )

            posts[usersAuth[i].uuid] = mutableSetOf()
            feed[usersAuth[i].uuid] = mutableSetOf()
            followers[usersAuth[i].uuid] = mutableSetOf()

        }
    }

}