package uz.gita.androidexam.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Constants {
    var user = Firebase.auth.currentUser
}