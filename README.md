# Pokemon App  
_Image UI App for exploring Pokémon_

## ✨ Features

### Authentication
- Login & Register using **local Room database**

### Core Features
- List Pokémon with Paging 3
- Search Pokémon
- User Profile
- Offline support (cache using Room)

---

## Architecture

This app is built using **Clean Architecture** principles:

- **Presentation Layer**  
  Uses `ViewModel`, `StateFlow`, and `LiveData` for state management. UI communicates only with `UseCase`.

- **Domain Layer**  
  Contains `UseCase` and `Repository` interfaces

- **Data Layer**  
  Handles data source (local via Room, remote via Retrofit) and maps models to domain entities.

##  Tech Stack & Dependencies

### AndroidX & UI
- `androidx.core:core-ktx`
- `androidx.appcompat:appcompat`
- `androidx.activity:activity-ktx`
- `androidx.constraintlayout:constraintlayout`
- `androidx.recyclerview:recyclerview`
- `com.google.android.material:material`
- `androidx.cardview:cardview`
- `com.facebook.shimmer:shimmer`

### Navigation
- `androidx.navigation:navigation-fragment-ktx`
- `androidx.navigation:navigation-ui-ktx`

### Paging
- `androidx.paging:paging-runtime-ktx`
- `androidx.paging:paging-common`

### Local Database (Room)
- `androidx.room:room-runtime`
- `androidx.room:room-ktx`
- `androidx.room:room-compiler` (kapt)

### Networking (Retrofit)
- `com.squareup.retrofit2:retrofit`
- `com.squareup.retrofit2:converter-gson`
- `com.squareup.okhttp3:logging-interceptor`

###  Coroutines & Flow
- `org.jetbrains.kotlinx:kotlinx-coroutines-core`
- `org.jetbrains.kotlinx:kotlinx-coroutines-android`

### Lifecycle
- `androidx.lifecycle:lifecycle-livedata-ktx`

### Dependency Injection (Hilt)
- `com.google.dagger:hilt-android`
- `com.google.dagger:hilt-compiler` (kapt)

### RxJava (optional, for event bindings)
- `io.reactivex.rxjava3:rxjava`
- `com.jakewharton.rxbinding4:rxbinding`

### Preferences
- `androidx.datastore:datastore-preferences:1.1.4`

### Testing
- `junit:junit`
- `androidx.test.ext:junit`
- `androidx.test.espresso:espresso-core`

---

## App Interface
![InterfaceApp](https://github.com/MuhammadRafliGimnastiar/Poke-Mon/blob/master/images/UI_pokemon_app.png)
