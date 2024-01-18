package com.zen.nottwitter.di

import com.zen.nottwitter.presentation.ui.landing.LandingViewModel
import org.koin.dsl.module

val featureModule = module {
    factory { LandingViewModel() }
}