package com.zen.nottwitter.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DefaultFirebaseClient : FirebaseClient {

    override fun authClient(): FirebaseAuth {
        return Firebase.auth
    }

    override fun firestoreClient(): FirebaseFirestore {
        return Firebase.firestore
    }
}