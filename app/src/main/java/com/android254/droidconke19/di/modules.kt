package com.android254.droidconke19.di

import android.content.Context
import androidx.room.Room
import com.android254.droidconke19.database.AppDatabase
import com.android254.droidconke19.repository.*
import com.android254.droidconke19.utils.SharedPref
import com.android254.droidconke19.viewmodels.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase dependencies
    single { Firebase.firestore }
    single { FirebaseAuth.getInstance() }
    single { FirebaseRemoteConfig.getInstance() }
    single { FirebaseMessaging.getInstance() }
    single {
        val db = FirebaseDatabase.getInstance()
        db.setPersistenceEnabled(true)
        db
    }

    // ViewModels
    viewModel { AboutViewModel(get()) }
    viewModel { SessionsViewModel(get()) }
    viewModel { SessionDataViewModel(get(), get(), get(), get()) }
    viewModel { AgendaViewModel(get()) }
    viewModel { EventTypeViewModel(get()) }
    viewModel { FeedBackViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { AnnouncementViewModel(get()) }
    viewModel { SessionDetailsViewModel(get(), get(),get()) }

    factory { (context: Context) -> context.getSharedPreferences(SharedPref.PREF_NAME, Context.MODE_PRIVATE) }
}

val dataModule = module {
    // Repos
    single<AboutDetailsRepo> { AboutDetailsRepoImpl(get()) }
    single<SessionsRepo> { SessionsRepoImpl(get(), get()) }
    single<SessionDataRepo> { SessionDataRepoImpl(get(), get()) }
    single<SpeakersRepo> { SpeakersRepoImpl(get()) }
    single<RoomRepo> { RoomRepoImpl(get()) }
    single<SessionFeedbackRepo> { SessionFeedbackRepoImpl(get()) }
    single<UpdateTokenRepo> { UpdateTokenRepoImpl(get()) }
    single<EventFeedbackRepo> { EventFeedbackRepoImpl(get()) }
    single<EventTypeRepo> { EventTypeRepoImpl(get()) }
    single<AgendaRepo> { AgendaRepoImpl(get()) }
    single<AnnouncementRepo> { AnnouncementRepoImpl(get()) }
    single<ReserveSeatRepo> { ReserveSeatRepoImpl(get()) }

    // Database
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "droidconKE_db").fallbackToDestructiveMigration().build() }
}