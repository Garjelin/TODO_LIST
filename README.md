# To-Do List Application

Welcome to the **To-Do List Application**, a modern Android app designed for task management. This project showcases my journey from a QA Automation Engineer (SDET) to an Android Developer, highlighting skills in Kotlin, Jetpack Compose, and software development best practices. Built as a personal project, it serves as a portfolio piece to demonstrate my ability to create functional, testable, and maintainable Android applications.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Technologies](#technologies)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Features
- **Task Management**: Add, edit, delete, and filter tasks (completed/uncompleted) with a clean UI.
- **Navigation**: Seamless screen transitions between Task List, Task Detail, Search, and Archive using Navigation Component.
- **Adaptive UI**: Modern interface built with Jetpack Compose, supporting tab-based filtering.
- **Data Persistence**: Store tasks with Room and manage filter state with SharedPreferences.
- **Lifecycle Logging**: Monitor app lifecycle for debugging and performance optimization.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Garjelin/TODO_LIST.git
2. Open the project in Android Studio.
3. Sync the project with Gradle to download dependencies.
4. Build and run the app on an emulator or physical device with Android API 21+.

## Usage
- Home Screen: View and manage your task list with options to add new tasks or filter them.
- Task Detail: Edit task title and completion status, then save changes.
- Search: Search tasks by title.
- Archive: View archived tasks (placeholder functionality).
- Navigate between screens using the bottom NavigationBar.

## Technologies
- Languages: Kotlin
- UI Framework: Jetpack Compose
- Navigation: Navigation Component
- Data Storage: Room, SharedPreferences
- Architecture: ViewModel
- Build Tools: Gradle
- Testing: Kaspresso, Espresso
- Debugging: Custom Logger with Logcat integration
- IDE: Android Studio

## Testing
The project includes UI tests to ensure reliability and ease of maintenance:

- Kaspresso and Espresso: Cover scenarios like adding tasks, filtering, and editing.
- Test Infrastructure: Utilizes test tags and a custom Logger for automated testing.
- To run tests, use Android Studio's test runner or the following command:
`./gradlew connectedAndroidTest`

## Contributing
This is a personal project, but contributions are welcome! Please follow these steps:

- Fork the repository.
- Create a new branch (git checkout -b feature-branch).
- Commit your changes (git commit -m "Add new feature").
- Push to the branch (git push origin feature-branch).
- Open a Pull Request with a clear description.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Contact
- Author: Sergey Yakimov
- Email: sergeyyakimov89@gmail.com
- LinkedIn: linkedin.com/in/sergey-yakimov
- GitHub: github.com/Garjelin

---
