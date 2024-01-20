package com.zen.nottwitter.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

interface FirebaseClient {
    fun authClient(): FirebaseAuth
    fun firestoreClient(): FirebaseFirestore
    fun storageClient(): FirebaseStorage
}