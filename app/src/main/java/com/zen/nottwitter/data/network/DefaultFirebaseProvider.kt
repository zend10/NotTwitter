package com.zen.nottwitter.data.network

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.zen.nottwitter.data.exception.LoginFailedException
import com.zen.nottwitter.data.exception.PostFailedException
import com.zen.nottwitter.data.exception.RegisterFailedException
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID

class DefaultFirebaseProvider(
    private val context: Context,
    private val firebaseClient: FirebaseClient
) : FirebaseProvider {

    override suspend fun authenticate(): User? {
        try {
            val currentUser = firebaseClient.authClient().currentUser
            return if (currentUser != null) {
                val uid = currentUser.uid
                val storedUser =
                    firebaseClient.firestoreClient().collection(DB_USERS).document(uid).get()
                        .await()
                val nickname =
                    storedUser.data?.get(KEY_NICKNAME) as? String ?: throw LoginFailedException()
                val email =
                    storedUser.data?.get(KEY_EMAIL) as? String ?: throw LoginFailedException()
                User(uid, nickname, email)
            } else {
                null
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun register(nickname: String, email: String, password: String): User {
        try {
            val createdUser =
                firebaseClient.authClient().createUserWithEmailAndPassword(email, password).await()

            val uid = createdUser?.user?.uid ?: throw RegisterFailedException()
            val createdOn = FieldValue.serverTimestamp()
            val user = mapOf(
                KEY_NICKNAME to nickname,
                KEY_EMAIL to email,
                KEY_CREATED_ON to createdOn
            )
            firebaseClient.firestoreClient().collection(DB_USERS).document(uid).set(user).await()

            return User(uid, nickname, email)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun login(email: String, password: String): User {
        try {
            val loggedInUser =
                firebaseClient.authClient().signInWithEmailAndPassword(email, password).await()

            val uid = loggedInUser?.user?.uid ?: throw LoginFailedException()
            val storedUser =
                firebaseClient.firestoreClient().collection(DB_USERS).document(uid).get().await()

            val nickname =
                storedUser.data?.get(KEY_NICKNAME) as? String ?: throw LoginFailedException()
            return User(uid, nickname, email)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun logout() {
        firebaseClient.authClient().signOut()
    }

    override suspend fun createPost(user: User, message: String, imageUriString: String): Post {
        try {
            var imageUrl = ""
            if (imageUriString.isNotBlank()) {
                val imageUri = Uri.parse(imageUriString)
                val inputStream = context.contentResolver.openInputStream(imageUri)!!
                val imageName = "${UUID.randomUUID()}.jpg"
                val storageRef = firebaseClient.storageClient().reference
                val imagesRef = storageRef.child("$STORAGE_IMAGES/${user.uid}/$imageName")
                val uploadedImage = imagesRef.putStream(inputStream).await()

                if (uploadedImage != null && uploadedImage.task.isSuccessful) {
                    imageUrl = imagesRef.downloadUrl.await().toString()
                } else {
                    throw PostFailedException()
                }
            }

            val postUid = UUID.randomUUID().toString()
            val createdOn = FieldValue.serverTimestamp()
            val post = mapOf(
                KEY_USER_UID to user.uid,
                KEY_NICKNAME to user.nickname,
                KEY_MESSAGE to message,
                KEY_IMAGE_URL to imageUrl,
                KEY_CREATED_ON to createdOn
            )
            firebaseClient.firestoreClient().collection(DB_POSTS).document(postUid).set(post)
                .await()
            return Post(
                postUid,
                user.uid,
                user.nickname,
                message,
                imageUrl
            )
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getPosts() {

    }

    override suspend fun getPost() {

    }

    override suspend fun getUserPosts() {

    }

    companion object {
        private const val DB_USERS = "users"
        private const val KEY_NICKNAME = "nickname"
        private const val KEY_EMAIL = "email"
        private const val KEY_CREATED_ON = "createdOn"

        private const val DB_POSTS = "posts"
        private const val KEY_USER_UID = "userUid"
        private const val KEY_MESSAGE = "message"
        private const val KEY_IMAGE_URL = "imageUrl"

        private const val STORAGE_IMAGES = "images"
    }
}