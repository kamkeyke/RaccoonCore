# 🦝 RaccoonCore

**RaccoonCore** is a base library I created to unify and optimize the infrastructure of my Minecraft (Forge) mods. The main goal is to eliminate boilerplate code and combine code that can be useful in various contexts and different mods.

## ✨ Features

* **Server Scheduler:** Schedules tasks by tick to be executed later on the server.
* **PlayerList Argument:** Command argument type that accepts a comma-separated list of players, e.g., `{nick1,nick2}`.
* **Time Utils:** Tick-to-duration converter (e.g., `72000` >> `1h`).

## 📦 Installation (via JitPack)

Add JitPack to your `build.gradle`:

```gradle
repositories {
    maven { url = "https://jitpack.io"  }
}
```
Then add the dependency:
```gradle
dependencies {
    implementation fg.deobf("com.github.kamkeyke:RaccoonCore:{raccooncore_version}")
}
```
Replace {raccooncore_version} with the desired version.

## 📜 License

This project is licensed under the **GNU GPL v3**.

If you use this library in your project, your project must also comply with GPLv3 terms.