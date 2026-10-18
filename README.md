# pluginPaper

A plugin under development for **Paper servers (Minecraft Java 1.21.11)**, built as a learning project to apply Java in practice through server-side plugin development.

## 📋 About

This project started with the goal of learning Java hands-on, through visible and testable results inside a real server, rather than studying theory alone. The plugin adds custom items and mechanics to Minecraft.

## ⚔️ Features

### Special Sword
Applies **Slowness II** to the enemy hit, turning it into a combat control weapon.

### Berserker Chestplate
Chestplate with **death-prevention logic**. When triggered, it grants a combination of effects for 30 seconds:
- Poison I
- Regeneration IV
- Nausea I
- Strength II
- Speed II

Has a **60-second cooldown**, controlled by elapsed-time logic (avoiding bugs caused by key-presence checks resetting incorrectly).

### Custom Fortune (Ancient Debris)
A custom fortune system for Ancient Debris ore, with **anti-farm logic** that prevents exploiting the effect through block replacement (using `BlockPlaceEvent` combined with location tracking).

## 🛠️ Tech Stack

- **Java** (JDK 21/25)
- **Paper API**
- **Maven**

## 🏗️ Architecture

All plugin `NamespacedKey` definitions are centralized in a single class (`ItemKeys.java`) and injected via constructor into listeners, keeping the code organized and avoiding duplicated keys scattered across the project.

## 🚧 Status

Actively under development. New features and balance adjustments are being added continuously.

## 📦 Usage

```bash
git clone https://github.com/ragaso62/pluginPaper.git
cd pluginPaper
mvn clean package
```

The generated `.jar` can be placed in the `plugins/` folder of a Paper server.

## 👤 Author

Developed by [ragaso62](https://github.com/ragaso62) as part of a Java learning path, which also went through Python, HTML/CSS and C#.
