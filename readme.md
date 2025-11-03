# ByteSessionRestore

<p align="center">
  <a href="https://www.java.com/">
    <img src="https://img.shields.io/badge/Java-21+-blue" alt="Java"/>
  </a>
  <a href="https://papermc.io/">
    <img src="https://img.shields.io/badge/PaperMC-1.20.2%2B-green" alt="PaperMC"/>
  </a>
  <a href="license">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License"/>
  </a>
  <a href="https://discord.com/invite/3K9yrZQRmS">
    <img src="https://img.shields.io/discord/1350369915521204276?label=Discord&color=7289DA&logo=discord&logoColor=white" alt="Discord"/>
  </a>
</p>

**ByteSessionRestore** is a modular and high-performance plugin for **PaperMC** servers that automatically saves and restores player session data.  
It ensures that player inventories, locations, and other critical data are preserved across disconnections, deaths, or world transitions.

---

## Overview

When a player leaves, dies, or changes worlds, ByteSessionRestore captures a snapshot of their state and stores it safely.  
Administrators can later restore these sessions manually or automatically, allowing for full recovery of items, experience, and locations.

The system is designed to be fast, reliable, and extensible — supporting different storage engines and compression strategies to adapt to any server environment.

---

<p align="center">
  <a href="https://discord.com/invite/3K9yrZQRmS">
    <img src="https://imgur.com/DvyC4jL.png" width="600" alt="ByteSessionRestore preview">
  </a>
  <br/>
  <i>If you need help, join the Discord server.</i>
</p>

---

## Features

- Automatically saves player sessions on death, logout, or world change.
- Allows manual saving and restoration of player states.
- Ensures reliable data recovery with lightweight compression and storage systems.
- Fully asynchronous to prevent main-thread performance impact.
- Compatible with **PaperMC 1.20.2+** and **Java 21+**.
- Clean modular architecture, built for scalability and integration.

---

## Commands

| Command                     | Description                                      | Permission                                   |
|------------------------------|--------------------------------------------------|-----------------------------------------------|
| `/bsr view <player>`         | Opens a menu to view saved sessions for a player | `bytesessionrestore.command.view`             |
| `/bsr save <player>`         | Saves a session for the specified player         | `bytesessionrestore.command.save`             |
| `/bsr save all`              | Saves sessions for all online players            | `bytesessionrestore.command.save_all`         |

---

## Installation

1. Download the latest **ByteSessionRestore** release.
2. Place the JAR inside your server’s `plugins/` folder.
3. Start or reload your Paper server.
4. The plugin will generate its configuration files automatically.

---

## License

This project is released under the [MIT License](LICENSE).  
You are free to use, modify, and distribute it with attribution.
