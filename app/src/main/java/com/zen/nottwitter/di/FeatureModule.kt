package com.zen.nottwitter.di

import com.zen.nottwitter.presentation.ui.landing.LandingViewModel
import com.zen.nottwitter.presentation.ui.login.LoginViewModel
import com.zen.nottwitter.presentation.ui.main.MainViewModel
import com.zen.nottwitter.presentation.ui.register.RegisterViewModel
import org.koin.dsl.module

val featureModule = module {
    factory { LandingViewModel() }
    factory { LoginViewModel(get()) }
    factory { RegisterViewModel(get()) }
    factory { MainViewModel() }
}