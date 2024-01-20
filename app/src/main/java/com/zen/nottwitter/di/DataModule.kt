package com.zen.nottwitter.di

import com.zen.nottwitter.data.repository.ConfigRepository
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.DefaultConfigRepository
import com.zen.nottwitter.data.repository.DefaultContentRepository
import com.zen.nottwitter.data.repository.DefaultUserRepository
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.domain.usecase.CreatePostUseCase
import org.koin.dsl.module

val dataModule = module {
    single<UserRepository> { DefaultUserRepository(get(), get()) }
    single<ConfigRepository> { DefaultConfigRepository() }
    single<ContentRepository> { DefaultContentRepository(get()) }

    factory { CreatePostUseCase(get(), get()) }
}