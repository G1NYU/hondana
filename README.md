<div align="center">

# ほんだな — Hondana 本棚

### Neo-otaku media shelf for Android
A fork of [Mihon](https://github.com/mihonapp/mihon) — read manga & manhwa with all Keiyoushi extensions, plus a cinematic personal shelf for manga, manhwa, music, and books.

---

## Features

<div align="left">

* Everything from Mihon — full manga/manhwa reader, all Keiyoushi extensions, trackers, library, backups
* **Shelf tab** — a neo-otaku cinematic media archive powered by:
  * [AniList](https://anilist.co/) for manga & manhwa favorites
  * [Last.fm](https://www.last.fm/) for top albums & artists
  * [Open Library](https://openlibrary.org/) for books
* Cinematic blurred backdrop that changes on hover/tap
* Spotlight bottom sheet with descriptions, genres, and tags
* Pull-to-refresh on all shelf sections

</div>

---

## Setup

Clone the repo and open in Android Studio.

In `ShelfScreenModel` inside `ShelfTab.kt`, set:

```kotlin
private val aniListUser: String = "YOUR_ANILIST_USERNAME",
private val lastFmApiKey: String = "YOUR_LASTFM_API_KEY",
private val lastFmUser: String = "YOUR_LASTFM_USERNAME",
```

Build and run. The **Shelf tab** appears between Browse and More in the bottom nav.

---

## Branch

Active development: `feature/shelf`

---

## Credits

Built on top of [Mihon](https://github.com/mihonapp/mihon) by the Mihon Open Source Project.  
Shelf feature by [G1NYU](https://github.com/G1NYU).

### License

Apache 2.0 — see [LICENSE](/LICENSE)

</div>
