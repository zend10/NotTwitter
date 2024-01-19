package com.zen.nottwitter.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface FirebaseClient {
    fun authClient(): FirebaseAuth
    fun firestoreClient(): FirebaseFirestore
}