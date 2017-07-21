# Helios

Plugin API/Tweaks collection on top of an [Orion](https://github.com/OrionMinecraft/Orion) Mixin loader &
[Paper](https://github.com/PaperMC/Paper) Minecraft server software

## Features
- Player tweaks:
    - Built-in AFK events
    - Can disable player data saving - extremely useful for most lobby servers
    - Configurable keep-alive packet sending threshold (helps getting ping graph correct faster and/or lower network load (?))
        - Custom implementation is in works
    - Disable idle timeout kicking for opped players (Vanilla does not have that feature built-in for some reason!)
- World system tweaks:
    - More precise spawnpoint without plugins! Decimals and yaw/pitch are saved to world directly now¹
    - Disable UUID conversion on **every** goddamn server start. Saves start up time if worlds don't need to be converted
    from older server versions
- Command system tweaks:
    - Makes default permission denied message configurable (The famous red `I'm sorry, but you do not have permission to
    perform this command...` message)
    - Also can make override every² plugin permission message (Your server can now have consistent
    permission messages!)
- Server core tweaks:
    - Automatic [EULA](https://account.mojang.com/documents/minecraft_eula) agreement. As you should agree to EULA 
    anyway if you are going to run a Minecraft Server
- Various exploit precautionary tweaks:
    - Strict check for item name length on Creative gamemode
    - Disable using NBT received from Creative gamemode³
    - Disable copying container blocks
- (Optional) [Sentry](https://sentry.io) support:
    - Get instantly notified of server errors (in production, server shouldn't throw any exceptions, eh? :wink:)

### Notes
¹ - Not supported outside of Helios  
² - Depends how plugin was made.  
³ - Might cause confusion, option below is bit better choice

## Requirements
- [Orion](https://github.com/OrionMinecraft/Orion)
- [Paper](https://github.com/PaperMC/Paper) 1.12
- Java 8

## Installation
1) Build Paper server from source, install server (yes, server, not API) jar to local Maven repository
2) Clone this repository and invoke `./gradlew build`
3) Grab jar from `build/libs` and put it to `mods/` directory of your server
4) Start server once and configure the Helios
5) Enjoy :)

## License
MIT