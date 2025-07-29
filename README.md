 📸 Instagram Clone

## Description

This project is an Android-based clone of Instagram, developed using Java and Kotlin. It serves as a portfolio piece and a practical application of Android development principles. The app mimics core Instagram features, including user authentication, photo sharing, a content feed, commenting, and user profiles.

---

## Project Objectives

-   To build a functional social media application with key features inspired by Instagram.
-   To implement user registration and login using Firebase Authentication.
-   To enable users to upload, filter, and share photos.
-   To create an interactive feed where users can view and comment on posts.
-   To apply modern Android development practices, including the use of Fragments and RecyclerView.
-   To integrate Firebase for backend services like database and storage.

---

## Technologies Used

-   ![Android](https://img.icons8.com/color/28/000000/android-os.png) **Android (Java & Kotlin)**
-   ![Firebase](https://img.icons8.com/color/28/000000/firebase.png) **Firebase**
-    <img width="55" height="24" alt="image" src="https://github.com/user-attachments/assets/bf8be994-6cad-45f5-8ca3-08a8b52153b1" /> **Gradle**
-   ![Camera](https://img.icons8.com/color/28/000000/camera--v1.png) **Android Camera API**
-   ![Material Design](https://img.icons8.com/color/28/000000/material-ui.png) **Material Design**

---

## Project Structure

````
instagram-clone/
├── build.gradle
├── settings.gradle
└── app/
    ├── build.gradle
    ├── google-services.json
    └── src/
        └── main/
            ├── AndroidManifest.xml
            ├── java/
            │   └── com/
            │       └── example/
            │           └── instagramclone/
            │               ├── activity/
            │               │   ├── CommentsActivity.java
            │               │   ├── EditProfileActivity.java
            │               │   ├── FilterActivity.java
            │               │   ├── FriendsProfileActivity.java
            │               │   ├── ImageFullViewActivity.java
            │               │   ├── LoginActivity.java
            │               │   ├── MainActivity.java
            │               │   └── RegisterActivity.java
            │               ├── adapter/
            │               │   ├── CommentsAdapter.java
            │               │   ├── FeedAdapter.java
            │               │   ├── GridAdapter.java
            │               │   ├── SearchAdapter.java
            │               │   └── ThumbnailsAdapter.java
            │               ├── config/
            │               │   ├── ConfigurationFirebase.java
            │               │   └── UserFirebase.java
            │               ├── fragment/
            │               │   ├── AccountFragment.java
            │               │   ├── FeedFragment.java
            │               │   ├── SearchFragment.java
            │               │   └── ShareFragment.java
            │               ├── helper/
            │               │   ├── Base64Custom.java
            │               │   ├── Constants.java
            │               │   ├── CustomAlertDialog.java
            │               │   ├── GeneralUtils.java
            │               │   ├── Permissions.java
            │               │   ├── RecyclerItemClickListener.java
            │               │   ├── SquareImageView.java
            │               │   └── ThumbnailsManager.java
            │               ├── listener/
            │               │   └── ThumbnailListener.java
            │               └── model/
            │                   ├── Comments.java
            │                   ├── Feed.java
            │                   ├── Posts.java
            │                   ├── PostsLike.java
            │                   ├── ThumbnailItem.java
            │                   └── User.java
            └── res/
                ├── layout/
                ├── drawable/
                ├── values/
                └── …
---
````

## Functionality by Class

### Activities

-   **`LoginActivity`**: The entry point of the app. Handles user login via email and password.
-   **`RegisterActivity`**: Allows new users to create an account.
-   **`MainActivity`**: The main screen after login, which hosts the primary navigation and fragments.
-   **`EditProfileActivity`**: Allows the logged-in user to update their profile information, such as name and profile picture.
-   **`FriendsProfileActivity`**: Displays the profile of other users, including their posts.
-   **`FilterActivity`**: Provides image filtering options before a user posts a photo.
-   **`CommentsActivity`**: A dedicated screen to view and add comments to a specific post.
-   **`ImageFullViewActivity`**: Displays a selected image in full-screen mode.

### Core Components

-   **Fragments**: The UI is primarily fragment-based, separating concerns for the Feed, Search, Post creation, and Profile views.
-   **Adapters**: Custom adapters (`FeedAdapter`, `CommentsAdapter`, etc.) are used to bind data sets to `RecyclerView` instances, efficiently displaying lists of posts, comments, and users.
-   **Models**: Plain Old Java Objects (POJOs) like `User`, `Post`, and `Comment` define the data structure for the application.
-   **Helpers**: Utility classes in the `helper` package manage tasks like Firebase configuration, user session management, and runtime permissions.

---

## Libraries and Frameworks

-   **Firebase Authentication**: Manages the entire authentication process, providing a secure way to handle user sign-up and sign-in.
-   **Firebase Realtime Database**: A NoSQL cloud database used to store and sync app data (like user info, posts, and comments) in real-time across clients.
-   **Firebase Storage**: Used for storing and serving user-generated content, specifically the photos uploaded by users.
-   **Material Components for Android**: A library of UI components that implement Material Design, ensuring a modern and consistent look and feel.
-   **Android Camera API**: Integrated to allow users to capture photos directly from their device's camera within the app.

---

## How to Execute and Configure

### Prerequisites

-   ![Android Studio](https://img.icons8.com/color/28/000000/android-studio--v2.png) **Android Studio** (latest version recommended)
-   ![Java](https://img.icons8.com/color/28/000000/java-coffee-cup-logo--v1.png) **JDK 8** or higher
-   A ![Firebase](https://img.icons8.com/color/28/000000/firebase.png) account

### GitHub and Android Studio Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/OL-sergio/Instagram-clone.git
    ```
2.  **Open in Android Studio:**
    -   Launch Android Studio.
    -   Select `File` > `Open` and navigate to the cloned `Instagram-clone` directory.

### Firebase Configuration

1.  **Create a Firebase Project:**
    -   Go to the [Firebase Console](https://console.firebase.google.com/).
    -   Click "Add project" and follow the on-screen instructions.
2.  **Add an Android App to your Project:**
    -   Inside your new project, click the Android icon to add a new Android app.
    -   Use `com.example.instagramclone` as the package name.
    -   Download the generated `google-services.json` file.
3.  **Add Configuration File:**
    -   Place the downloaded `google-services.json` file into the `app/` directory of your project in Android Studio.
4.  **Enable Firebase Services:**
    -   In the Firebase Console, navigate to the **Authentication** section and enable the "Email/Password" sign-in method.
    -   Navigate to the **Realtime Database** section and create a new database. Start in test mode for initial setup.
    -   Navigate to the **Storage** section and set up a new storage bucket.

### Running the Application

1.  Connect an Android device or start an Android Virtual Device (AVD).
2.  Click the `Run 'app'` button (▶️) in Android Studio.

---

## App Permissions

The `AndroidManifest.xml` file declares the following necessary permissions:

-   `CAMERA`: To capture photos for posts.
-   `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE`: To select photos from the gallery and cache images.
-   `INTERNET`: To connect to Firebase services.

---

## Contribution

Feel free to fork the repository, make improvements, and submit a pull request. For major changes, please open an issue first to discuss what you would like to change.
