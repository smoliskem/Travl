package com.example.travl.items

data class Friend(
    val friendUserID: String = "",
    val friendUsername: String = ""
)

fun Friend.toMap(): Map<String, Any> {
    return mapOf(
        "friendUserID" to friendUserID,
        "friendUsername" to friendUsername
    )
}