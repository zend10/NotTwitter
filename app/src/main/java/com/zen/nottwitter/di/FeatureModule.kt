package com.zen.nottwitter.di

import com.zen.nottwitter.presentation.ui.base.DefaultDispatchers
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import com.zen.nottwitter.presentation.ui.editor.EditorViewModel
import com.zen.nottwitter.presentation.ui.home.HomeViewModel
import com.zen.nottwitter.presentation.ui.landing.LandingViewModel
import com.zen.nottwitter.presentation.ui.login.LoginViewModel
import com.zen.nottwitter.presentation.ui.main.MainViewModel
import com.zen.nottwitter.presentation.ui.profile.ProfileViewModel
import com.zen.nottwitter.presentation.ui.register.RegisterViewModel
import org.koin.dsl.module

val featureModule = module {
    factory<DispatcherProvider> { DefaultDispatchers() }
    factory { LandingViewModel(get(), get()) }
    factory { LoginViewModel(get(), get(), get()) }
    factory { RegisterViewModel(get(), get(), get()) }
    factory { MainViewModel(get()) }
    factory { HomeViewModel(get()) }
    factory { ProfileViewModel(get(), get()) }
    factory { EditorViewModel(get()) }
}