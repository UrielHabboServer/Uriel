# Uriel Server
Uriel is a Habbo Server library written in Kotlin, letting people easily create their own fully custom Habbo Hotels.

> [!NOTE]
> Uriel has switched goals from being a Habbo Emulator to a Habbo Server Library. This means that Uriel will no longer be a standalone server.

> [!IMPORTANT]
> Uriel is not yet finished and may contain bugs or lack features. Breaking changes may also occur at any time.

## Goals
* Hotel-wide tick loops and per-room tick loops.
* Total customisation of the hotel, which includes the ability to use your own components anywhere.
* A plugin system to allow for easy extension of the server via other people's creations.
* A powerful, yet easy to use permission system, like that of Bukkit.

## Goal Usage *(not yet implemented)*
```kotlin
val server = UrielServer {
    port = 3000
    ip = "127.0.0.1"
    
    authentication {
        handler = MyAuthenticationHandler()
    }
}

server.start()
```

## Initial Roadmap
* [x] Handshake & Authentication
* [x] Friends & Messenger
* [x] Default Navigator provider
* [ ] Rooms *(in progress)*
* * [x] Chat, Commands & Chat Bubbles
* [ ] Inventory *(in progress)*
* [ ] Furniture
* [ ] Catalogue
* [ ] Moderation

## License
[MIT](/LICENSE)