# ReadRecipe

Aplikasi Android untuk menjelajahi dan menyimpan resep makanan dari seluruh dunia, dibangun dengan Jetpack Compose dan arsitektur modern Android.

## Fitur

- **Autentikasi** — Registrasi dan login pengguna dengan penyimpanan lokal
- **Home** — Carousel resep unggulan acak dan filter berdasarkan kategori
- **Search** — Pencarian resep secara real-time dengan debounce
- **Saved** — Daftar resep favorit yang disimpan per pengguna
- **Detail Resep** — Bahan-bahan, instruksi memasak, dan tautan YouTube
- **Profile** — Informasi akun dan logout

## Arsitektur

Aplikasi ini menggunakan **Clean Architecture** dengan pola **MVVM (Model-View-ViewModel)**.

```
UI (Compose Screens)
       ↕
  ViewModels
       ↕
  Repositories
    ↙     ↘
Remote     Local
(Retrofit) (Room DB)
```

### Struktur Direktori

```
com.example.readrecipe/
├── data/
│   ├── local/            # Room Database
│   │   ├── dao/          # UserDao, SavedRecipeDao
│   │   ├── entity/       # UserEntity, SavedRecipeEntity
│   │   └── AppDatabase   # Instance database
│   ├── remote/           # Retrofit API
│   │   ├── model/        # DTO: MealDetailDto, MealSummaryDto, CategoryDto
│   │   ├── MealDbApi     # Interface endpoint Retrofit
│   │   └── RetrofitInstance
│   └── repository/       # AuthRepository, RecipeRepository
├── domain/model/         # Model domain: Meal, MealDetail, Ingredient
├── session/              # SessionManager (SharedPreferences)
├── ui/
│   ├── screens/          # Login, SignUp, Home, Search, Saved, Detail, Profile
│   ├── navigation/       # NavGraph + BottomNavBar
│   ├── components/       # Reusable Composable
│   └── theme/            # Warna, tipografi Material3
├── MainActivity
└── ReadRecipeApplication
```

## Layer

### Data Layer

**Remote — TheMealDB API** (`https://www.themealdb.com/api/json/v1/1/`)

| Endpoint | Fungsi |
|---|---|
| `/categories.php` | Semua kategori makanan |
| `/filter.php?c={category}` | Makanan berdasarkan kategori |
| `/search.php?s={query}` | Pencarian resep |
| `/lookup.php?i={mealId}` | Detail resep |
| `/random.php` | Resep acak |

**Local — Room Database**

| Tabel | Kolom Utama |
|---|---|
| `users` | id, name, email (unique), passwordHash (SHA-256), avatarEmoji |
| `saved_recipes` | mealId + userId (composite PK), title, thumbnail, category |

### Repository Layer

- **`AuthRepository`** — Registrasi, login, logout, dan manajemen sesi user
- **`RecipeRepository`** — Fetch resep dari API, simpan/hapus favorit, search

Semua method API mengembalikan `Result<T>` untuk penanganan error yang konsisten.

### ViewModel Layer

| ViewModel | Screen | Tanggung Jawab |
|---|---|---|
| `AuthViewModel` | Login / SignUp | Validasi input dan autentikasi |
| `HomeViewModel` | Home | Load kategori, featured meals, filter kategori |
| `SearchViewModel` | Search | Query real-time dengan debounce 500ms |
| `DetailViewModel` | Detail | Load detail resep, toggle save/unsave |
| `SavedViewModel` | Saved | Daftar resep tersimpan milik user |
| `ProfileViewModel` | Profile | Info user dan logout |

### UI Layer

**Alur Navigasi:**

```
App Start
    ↓
SessionManager.isLoggedIn()?
  ├── Ya   → Home Screen
  └── Tidak → Login Screen

Login / SignUp ──────────→ Home
                              ↕ (Bottom Navigation)
               Home ↔ Search ↔ Saved ↔ Profile
                ↓
            Detail Screen  (parameter: mealId)
                ↓
            Kembali ke screen sebelumnya
```

## Implementasi Fitur Utama

| Fitur | Implementasi |
|---|---|
| **Autentikasi** | Room DB lokal, password di-hash SHA-256, sesi disimpan di SharedPreferences |
| **Featured Meals** | 3 resep acak dari `/random.php` dengan pengecekan duplikat |
| **Filter Kategori** | Chip selector, data dimuat sekali saat Home diinisialisasi |
| **Search** | Debounce 500ms dengan cancellable Coroutine Job |
| **Save Resep** | Room DB, status dipantau secara reaktif via `Flow<Boolean>` |
| **Image Loading** | Coil async image loader dengan `ContentScale` |

## Alur Data

1. App dibuka → `SessionManager` mengecek status login
2. Jika sudah login → Home memuat kategori dan featured meals dari API
3. User pilih kategori → `RecipeRepository.getMealsByCategory()` → Retrofit → UI diperbarui
4. User buka detail → `getMealById()` + `isRecipeSaved()` dari Room DB
5. User simpan resep → `saveMeal()` → Room DB → Flow update otomatis di Saved screen
6. User search → debounce 500ms → `searchMeals()` → Retrofit → hasil ditampilkan

## Tech Stack

| Kategori | Library | Versi |
|---|---|---|
| UI Framework | Jetpack Compose + Material3 | BOM 2024.09 |
| Navigation | Navigation Compose | 2.8.9 |
| State & Async | ViewModel + Coroutines + Flow | 2.10.0 |
| Database | Room | 2.7.1 |
| Networking | Retrofit + OkHttp + GSON | 2.11.0 / 4.12.0 |
| Image Loading | Coil | 2.7.0 |
| Build | KSP | 2.3.5 |
| Min SDK | Android 7.0 (API 24) | — |
| Target SDK | API 37 | — |

## Data Source

Resep diambil dari [TheMealDB](https://www.themealdb.com/) — API publik gratis untuk data makanan dari seluruh dunia.
