<div align="center">

![Hilt](https://i.imgur.com/34pQrYN.png)

# Hilt

### Shared foundations. Modern mods.

**A shared library and compatibility layer for Modern Mods and other NeoForge projects.**

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62B47A?style=flat-square)
![NeoForge](https://img.shields.io/badge/Loader-NeoForge-EF9D31?style=flat-square)
![Java](https://img.shields.io/badge/Java-21-E76F00?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-7289DA?style=flat-square)

**Maintained and published by [Modern Mods](https://github.com/Modern-Mods).**

</div>

---

## About Hilt

**Hilt** is a shared Minecraft modding library maintained by **Modern Mods** for **Minecraft 1.21.1 and NeoForge**.

It provides common code, APIs, utilities, data systems, registration infrastructure, and other shared foundations used across projects that would otherwise need to duplicate the same functionality.

Hilt is primarily a **developer dependency** rather than a standalone gameplay mod.

Its purpose is simple:

> **Put the shared machinery in one place so the mods built on top of it can focus on their own systems.**

Hilt also carries forward portions of the architecture and implementation lineage of **Mantle** and related Slime Knights projects while adapting that foundation for the modern NeoForge ecosystem.

---

## What Hilt Is For

Hilt provides reusable infrastructure for systems commonly shared between larger Minecraft mods.

Depending on the consuming project, this can include foundations for:

* Registries and content registration
* Data-driven content
* Materials and material-related systems
* Recipes and serialization
* Networking
* Menus and screens
* Fluid-related systems
* Tags and resource handling
* Datagen
* Common utilities
* Mod interoperability
* Client and server helpers
* Shared APIs
* Extensible systems for additional Modern Mods projects

Not every Hilt system is necessarily used by every dependent mod.

The goal is to provide a stable shared foundation without forcing unrelated gameplay systems into the library itself.

---

## Used by Modern Mods

Hilt is developed as part of the broader **Modern Mods** ecosystem.

Modern Mods builds substantial NeoForge projects centered around crafting, automation, progression, exploration, interoperability, and other large gameplay systems.

Hilt exists underneath those projects as shared infrastructure rather than as another gameplay-focused mod.

### Modern Mods

**Modern Minecraft. Bigger systems.**

[View the Modern Mods organization](https://github.com/Modern-Mods)

---

## Platform

Current primary development target:

|               |            |
| ------------- | ---------- |
| **Minecraft** | `1.21.1`   |
| **Loader**    | `NeoForge` |
| **Java**      | `21`       |
| **Language**  | `Java`     |

Individual releases may require a particular NeoForge build or dependency version.

Always check the metadata included with the release or the requirements of the mod depending on Hilt.

---

## For Players

You generally only need to install Hilt when another mod lists it as a dependency.

Hilt is not intended to provide a major standalone gameplay experience.

If a mod requires Hilt:

1. Make sure your **Minecraft version** matches.
2. Make sure you are using **NeoForge**.
3. Install the required version of **Hilt**.
4. Install the mod that depends on it.
5. Place the required `.jar` files in your `mods` directory.

Dependent mods may require a specific Hilt version.

---

## For Developers

Hilt is intended to provide common infrastructure that can be shared between multiple NeoForge projects.

When developing against Hilt, keep in mind that APIs may evolve alongside the projects using them, especially while active ports and migrations are still underway.

Where practical, shared systems should remain:

* Data-driven
* Server-aware
* Multiplayer-safe
* Extensible
* Compatible with modern NeoForge APIs
* Independent from unnecessary client-only code
* Suitable for reuse across multiple projects

---

# Compiling From Source

## Requirements

Before building Hilt, make sure you have:

* **Java 21**
* **Git**
* A compatible development environment
* The repository cloned locally

Git must be installed and available in your system `PATH` when required by repository scripts or build tooling.

---

## Clone

```bash
git clone https://github.com/Modern-Mods/Hilt.git
cd Hilt
```

---

## IDE Setup

Import the repository as a **Gradle project** into your preferred IDE.

The included Gradle wrapper should be used rather than relying on a separately installed Gradle version.

### Windows

```text
.\gradlew.bat build
```

### Linux / macOS

```text
./gradlew build
```

---

## Clean Build

If you encounter unusual Gradle or generated-resource problems, try a clean build.

### Windows

```text
.\gradlew.bat clean build
```

### Linux / macOS

```text
./gradlew clean build
```

If the repository provides additional development, datagen, client, or server run configurations, use the tasks defined by the current branch.

---

# Issue Reporting

If you encounter a problem involving Hilt, please include enough information for the issue to be reproduced.

Include:

* Minecraft version
* Hilt version
* NeoForge version/build
* Versions of mods depending on Hilt
* Versions of other mods potentially related to the problem
* Whether the issue occurs in singleplayer or multiplayer
* Whether the issue occurs on a dedicated server
* Steps to reproduce the issue
* Relevant screenshots when useful

For crashes, also include:

* The relevant crash report
* `latest.log`
* Any other relevant NeoForge logs
* The actions performed immediately before the crash

Please avoid submitting only a screenshot of the final crash screen when a crash report or log is available.

---

# Attribution

Hilt includes and adapts work originally created and maintained by **Slime Knights**.

The inherited project credits include:

* mDiyo
* fuj1n
* Sunstrike
* progwml6
* pillbox
* alexbegt
* KnightMiner

Hilt preserves applicable upstream copyright notices, licensing requirements, source attribution, and acknowledgements.

The **Hilt** name and its maintenance under **Modern Mods** do not replace those obligations.

---

# License

Hilt is distributed under the **MIT License**, except where individual files, components, assets, or inherited material explicitly state otherwise.

Original upstream copyright:

```text
Copyright (c) 2013-2022 Slime Knights
(mDiyo, fuj1n, Sunstrike, progwml6, pillbox, alexbegt, KnightMiner)
```

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED **"AS IS"**, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

**Alternate licenses and third-party notices are documented where applicable.**

---

<div align="center">

## Hilt

**Shared foundations. Modern mods.**

`LIBRARY` · `API` · `DATA` · `NEOFORGE` · `MODERN MODS`

[Modern Mods](https://github.com/Modern-Mods)

</div>
