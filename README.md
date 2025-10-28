# QuickBite - A Restaurant Food Ordering System

**QuickBite** — a small **JavaFX desktop food-ordering application** with user authentication, a structured menu, cart & quantity controls, and a local SQLite backend.  
Designed for teaching, demos, and academic project submissions — clean, fully local, and easy to run.

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Tech Stack](#tech-stack)
4. [Prerequisites](#prerequisites)
5. [Project Structure](#project-structure)
6. [Database (quickbite.db)](#database-quickbitedb)
7. [Default Accounts / Credentials](#default-accounts--credentials)
8. [Build a Distributable JAR](#build-a-distributable-jar)
9. [Contributing](#contributing)

---

## Project Overview
QuickBite is a **desktop application** that simulates a simple restaurant ordering system.  
It demonstrates key Java and database integration concepts such as:

- JavaFX UI (FXML + Controllers)
- Local SQLite database for users, menu items, and seeded data
- Secure password hashing using BCrypt
- A shopping cart system (add/remove/quantity) with runtime persistence

This project is **student-friendly** and designed for extensions such as:
- Admin dashboard and order management  
- Persistent order history  
- Client-server migration  

---

## Features
- Login / Register (with password hashing)  
- Personalized Dashboard (per-user)  
- Structured Menu with categories and search  
- Add items to cart (with quantity controls + / −)  
- Per-item Remove, Clear Cart, and Checkout functionality  
- Local SQLite database auto-created (`db/quickbite.db`)  
- Modular, extendable architecture  

---

## Images
<div align="center" >
   
![login](https://github.com/user-attachments/assets/717c4d92-f96c-45d5-b922-8c0aa57f186d) <br>
![menu](https://github.com/user-attachments/assets/e32593d5-7873-4f92-9e5e-011c61807dbb) <br>
![cart](https://github.com/user-attachments/assets/17f4af4f-ce1f-45e6-8dbb-24572cc07c48)
</div>

---

## Tech Stack
- Java 21 (OpenJDK / Oracle JDK)
- JavaFX 21 (FXML)
- Maven build system
- SQLite (via `sqlite-jdbc`)
- BCrypt (`org.mindrot:jbcrypt`) for password hashing

---

## Prerequisites
Install the following on your development machine:

1. **JDK 21** — check using `java -version`
2. **Maven** — check using `mvn -v`
3. *(Optional)* **VS Code** with Java extensions or IntelliJ IDEA
4. *(Optional)* **DB Browser for SQLite** (to inspect `quickbite.db`)

---

## Project Structure
```bash
QuickBiteApp/
├─ pom.xml
├─ README.md
├─ src/
│  └─ main/
│     ├─ java/com/quickbite/
│     │  ├─ MainApp.java
│     │  ├─ controllers/
│     │  │  ├─ LoginController.java
│     │  │  ├─ DashboardController.java
│     │  │  ├─ MenuController.java
│     │  │  ├─ CartController.java
│     │  │  └─ MenuItem.java
│     │  └─ util/
│     │     ├─ Database.java
│     │     └─ CartManager.java
│     └─ resources/fxml/
│        ├─ login.fxml
│        ├─ dashboard.fxml
│        ├─ menu.fxml
│        └─ cart.fxml
└─ db/
   └─ quickbite.db (created at runtime, ignored by git)
```

---

## Install & Run
Open a terminal at the project root (folder that contains `pom.xml`):

1. Build and run with Maven (recommended)
   ```bash
    mvn clean javafx:run
   ```
   
2. If you prefer to compile only
   ```bash
     mvn clean compile
   ```

3. If `mvn` not found — install Maven OR use your IDE's Maven runner (VS Code: Maven extension → run `javafx:run`).

4. Stopping the app — focus the terminal and press `Ctrl + C`.

---

## Database (`quickbite.db`)
- The DB is automatically created in `db/quickbite.db` when you run the app for the first time.
- It is a SQLite file (binary) — do NOT open it with a text editor. Use DB Browser for SQLite or the VS Code SQLite extension to browse.
- Tables created and seeded: `users`, `menu` (sample items).
- If you want a fresh DB: delete `db/quickbite.db` and re-run the app (`mvn clean javafx:run`) — it will recreate and reseed.

  Quick SQL (view inside sqlite3 shell or DB Browser)
  ```bash
  -- show tables
  .tables
  
  -- view menu
  SELECT id, name, price, description FROM menu;
  ```

---

## Default accounts / credentials
- Administrator:
  username: `admin`
  password: `admin123`

- Register new users using the Create Account / Register screen.

---

## Build a distributable JAR
You can build a JAR with dependencies using Maven.<br>
(note: JavaFX runtime modules may still need to be present at runtime or you can package a native bundle; the plugin used here is JavaFX Maven plugin).

1. Package:
   ```bash
     mvn clean package
   ```

2. To run the packaged app with JavaFX modules:
   ```bash
     mvn javafx:run
    ```

---

## Contributing
If you want to make changes or extend the project:
1. Fork the repo (on GitHub).
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Make commits with clear messages.
4. Open a Pull Request describing the change.

---
