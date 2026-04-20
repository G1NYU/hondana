## Shelf Navigation Wiring Guide

Once you are ready to wire the Shelf tab into Mihon's navigation, follow these steps:

### 1. Add a new destination

In `app/src/main/java/eu/kanade/tachiyomi/ui/main/MainActivity.kt` or the navigation graph (depending on the Mihon version), add a new tab/route for Shelf.

Mihon uses a bottom navigation bar defined in:
`app/src/main/java/eu/kanade/tachiyomi/ui/main/`

Add a new entry to the `Tab` sealed class or enum:
```kotlin
object Shelf : Tab()
```

### 2. Register the composable route

In the NavHost setup, add:
```kotlin
composable(route = "shelf") {
    ShelfScreen()
}
```

### 3. Add a bottom navigation icon

In the BottomNav composable, add a new `NavigationBarItem` for the Shelf tab with an icon such as `Icons.Outlined.CollectionsBookmark`.

### 4. Add strings

In `i18n/src/commonMain/moko-resources/base/strings.xml`, add:
```xml
<string name="label_shelf">Shelf</string>
```

### 5. ViewModel factory

If Mihon uses Hilt/DI, register `ShelfViewModel` as a Hilt ViewModel:
```kotlin
@HiltViewModel
class ShelfViewModel @Inject constructor(
    private val repository: ShelfRepository,
) : ViewModel() { ... }
```

And annotate `ShelfRepository` with `@Singleton`.
