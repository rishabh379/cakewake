# 🎂 Cakewake

Cakewake is a modern Android application that makes cake ordering delightful, fast, and fully customizable. Whether you’re choosing a ready-made cake or designing one from scratch, Cakewake brings your dream cake to life with just a few taps.

---

## 📚 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Modules](#modules)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## 🧁 Overview

Cakewake is built using modern Android development practices like **Jetpack Compose**, **MVVM**, **Clean Architecture**, and **Modularization**. The app supports real-time interactions and secure authentication to provide a seamless and personalized cake-ordering experience.

---

## 🚀 Features

- 🔐 **User Authentication**: Secure sign-up and login flows.
- 📜 **Onboarding**: Smooth onboarding for new users.
- 🍰 **Cake Feed**: Browse curated cake listings with rich media and prices.
- 🎨 **Cake Customization**: Build your own cake by choosing layers, flavors, toppings, and decorations.
- 📦 **Order Management**: Place and track orders for both custom and ready-made cakes.
- 🧱 **Modular Architecture**: Scalable, cleanly separated codebase for features.

---

## 🏗️ Project Structure

```bash
Cakewake/
├── app/                        # Main application module
├── core/                       # Shared resources, utilities, and base classes
└── features/                   # Modularized feature implementations
    ├── auth/                   # Authentication logic and UI
    ├── cake_customization/     # Cake customization flow
    ├── feed/                   # Cake feed and browsing
    ├── home/                   # Main screen and navigation
    └── onboarding/             # First-time user onboarding
````

---

## 🛠️ Getting Started

### ✅ Prerequisites

* Android Studio (latest stable version)
* JDK 17+
* Gradle (wrapper is included)

### 🧩 Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/rishabh379/Cakewake.git
   cd Cakewake
   ```

2. **Open in Android Studio**

   * Launch Android Studio
   * Choose **Open an existing project**
   * Navigate to the `Cakewake` directory

3. **Build the Project**

   * Gradle wrapper will automatically resolve dependencies

4. **Run the App**

   * Use an emulator or a connected device
   * Click ▶️ Run

---

## 📦 Modules

### `app/`

Main entry point for the application, handling setup for DI, navigation, and theming.

### `core/`

Includes shared components such as:

* UI theme and design system
* Network utilities
* Base classes and extensions
* Data storage handlers

### `features/`

Each module encapsulates specific functionality:

* `auth/`: Login, sign-up, and session management
* `cake_customization/`: UI and business logic for designing cakes
* `feed/`: Displays browseable list of cakes
* `home/`: Hosts the main app navigation
* `onboarding/`: First-time user introduction and walkthrough

---

## 🤝 Contributing

We welcome contributions from developers of all levels! 🚀

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature-name`)
3. Make your changes
4. Commit with a clear message (`git commit -m "Add: new cake decoration feature"`)
5. Push to your fork
6. Open a Pull Request and describe your changes

**Code Style & Guidelines**

* Follow the existing modular architecture
* Use meaningful names and maintain consistency
* Write unit/UI tests where applicable

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 📬 Contact

For questions, feedback, or feature requests:

* Open an [issue](https://github.com/rishabh379/Cakewake/issues)
* Or reach out to the maintainer via email or LinkedIn (add if needed)

---

> **Cakewake** – *Bringing your cake dreams to life, one order at a time!* 🍰

```