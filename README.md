# 🎶 Music Library Manager

Welcome to the **Music Library Manager** project! This application lets you seamlessly organize, play, and manage your music library with style. Dive into the code and explore how each component comes together to create a fully functional music library experience.

---

## 🎧 Features

- **User Management**:
  	- Log in and out with ease using the `LoginManager`.
  	- User profiles are stored securely for a personalized experience.

- **Music Organization**:
  	- Manage albums, artists, and genres with classes like `Album.java`, `Artist.java`, and `Genre.java`.
  	- Create and edit playlists using the `Playlist` class.

- **Audio Playback**:
  	- Play your favorite tracks with the `AudioPlayer`.

- **GUI Integration**:
  	- User-friendly graphical interface for effortless interaction (find it under the `gui` folder).

---

## 🔄 Project Structure

The project is structured for clarity and ease of navigation:

```
MusicLibraryManager/
├── backend/        # Core application logic and data handling
├── gui/            # Graphical User Interface implementation
├── playlists/      # Storage for user playlists
├── resources/      # App resources such as icons and audio files
├── songs/          # Audio files and song data
├── .github/        # CI/CD workflows (e.g., `ant.yml`)
├── src/            # Main source files
├── target/classes/ # Compiled classes
```

---

## 🚀 Quick Start

1. **Clone the repository**:
   ```bash
   git clone https://github.com/nahomice/MusicLibraryManager.git
   ```

2. **Navigate to the directory**:
   ```bash
   cd MusicLibraryManager
   ```

3. **Run the application**:
   
   	For GUI:
   ```bash
   java -cp target/classes Main
   ```

   	For CLI:
   ```bash
   java -cp target/classes backend/CLIHandler
   ```

4. **Enjoy your music!** 🎶

---

## 🎨 Contributing

We welcome contributions! Here’s how you can get involved:

1. Fork the repo and create your branch:
   ```bash
   git checkout -b feature/amazing-feature
   ```

2. Commit your changes:
   ```bash
   git commit -m "Add amazing feature"
   ```

3. Push to your branch:
   ```bash
   git push origin feature/amazing-feature
   ```

4. Submit a pull request. ✨

---

## 📖 Documentation

Check out the `HELP.md` file for more details on:

- Setting up the development environment
- Project dependencies
- API usage

---

## ⚡ Tech Stack

- **Programming Language**: Java ☕
- **Build Tool**: Maven
- **Version Control**: Git
- **CI/CD**: GitHub Actions

---

## 🌟 Acknowledgments

- Special thanks to the contributors who brought this project to life. ❤‍🔧
- Shoutout to our testers for helping us refine the user experience.

---

## 🔒 License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

---

## 📃 Commit History

Here’s a glimpse of recent changes:

| File                     | Last Commit Message | Last Commit Date |
|--------------------------|---------------------|------------------|
| `Album.java`             | Add files via upload | 2 hours ago      |
| `Artist.java`            | Add files via upload | 2 hours ago      |
| `AudioPlayer.java`       | Add files via upload | 2 hours ago      |
| `Genre.java`             | Add files via upload | 2 hours ago      |
| `LoginManager.class`     | Add files via upload | 2 hours ago      |
| `Main.java`              | Add files via upload | 2 hours ago      |
| `.github/workflows/ant.yml` | Create ant.yml    | 4 days ago       |
| `README.md`              | Initial commit      | 4 days ago       |

---

**Let’s bring music to life!** ✨🎶

