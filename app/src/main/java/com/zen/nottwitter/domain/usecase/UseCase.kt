package com.zen.nottwitter.domain.usecase

interface UseCase<in R, T> {
    suspend fun execute(request: R): T
}