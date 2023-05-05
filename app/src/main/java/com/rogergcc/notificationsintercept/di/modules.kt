package com.rogergcc.notificationsintercept.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rogergcc.notificationsintercept.BuildConfig
import com.rogergcc.notificationsintercept.data.remote.NotificationsCloudDataSource
import com.rogergcc.notificationsintercept.data.remote.api.ApiService
import com.rogergcc.notificationsintercept.data.repositories.NotificationsRepositoryImpl
import com.rogergcc.notificationsintercept.domain.GetNotificationsUseCase
import com.rogergcc.notificationsintercept.domain.repositories.NotificationRepository
import com.rogergcc.notificationsintercept.domain.usecases.GetNotificationsCaseImp
import com.rogergcc.notificationsintercept.ui.NotificationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun injectModules() = appModules

val appModules by lazy {
    loadKoinModules(
        listOf(
            uiModule,
            domainModule,
            dataModule
        )
    )
}

val uiModule = module {
    // UI section
    viewModel { NotificationsViewModel(get()) }

}

val domainModule = module {
    single<GetNotificationsUseCase> { GetNotificationsCaseImp(get()) }

}

val dataModule = module {
    single<NotificationRepository> { NotificationsRepositoryImpl(get()) }
    single<NotificationsCloudDataSource> { get() }
//    factory<NotificationRepository> {
//        NotificationsRepositoryImpl(get())
//    }

    // Retrofit section
    single { provideRetrofit(get()) }
    factory { providesGson() }
    factory { provideAPI(get()) }
}


fun provideRetrofit(gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun providesGson(): Gson = GsonBuilder().create()

fun provideAPI(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)


