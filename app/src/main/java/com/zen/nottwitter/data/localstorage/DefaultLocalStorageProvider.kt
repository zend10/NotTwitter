package com.zen.nottwitter.data.localstorage

import com.zen.nottwitter.data.mapper.PostEntityMapper
import com.zen.nottwitter.data.mapper.UserEntityMapper
import com.zen.nottwitter.data.mapper.UserPostEntityMapper
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.model.entity.PostEntity
import com.zen.nottwitter.data.model.entity.UserEntity
import com.zen.nottwitter.data.model.entity.UserPostEntity
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query

class DefaultLocalStorageProvider(private val realmClient: RealmClient) : LocalStorageProvider {

    override suspend fun getUser(): User {
        try {
            return realmClient.realmClient().query<UserEntity>().find().first().run {
                UserEntityMapper().mapTo(this)
            }
        } catch (exception: NoSuchElementException) {
            throw exception
        }
    }

    override suspend fun saveUser(user: User): User {
        return realmClient.realmClient()
            .write { copyToRealm(UserEntityMapper().mapFrom(user), UpdatePolicy.ALL) }.run { user }
    }

    override suspend fun deleteUser() {
        realmClient.realmClient().write {
            val query = this.query<UserEntity>().find()
            delete(query)
        }
    }

    override suspend fun getPosts(): List<Post> {
        return realmClient.realmClient().query<PostEntity>().find().map {
            PostEntityMapper().mapTo(it)
        }
    }

    override suspend fun savePosts(posts: List<Post>): List<Post> {
        return realmClient.realmClient().write {
            posts.forEach { copyToRealm(PostEntityMapper().mapFrom(it), UpdatePolicy.ALL) }.run { posts }
        }
    }

    override suspend fun deletePosts() {
        realmClient.realmClient().write {
            val query = this.query<PostEntity>().find()
            delete(query)
        }
    }

    override suspend fun getUserPosts(): List<Post> {
        return realmClient.realmClient().query<UserPostEntity>().find().map {
            UserPostEntityMapper().mapTo(it)
        }
    }

    override suspend fun saveUserPosts(posts: List<Post>): List<Post> {
        return realmClient.realmClient().write {
            posts.forEach { copyToRealm(UserPostEntityMapper().mapFrom(it), UpdatePolicy.ALL) }.run { posts }
        }
    }

    override suspend fun deleteUserPosts() {
        realmClient.realmClient().write {
            val query = this.query<UserPostEntity>().find()
            delete(query)
        }
    }
}