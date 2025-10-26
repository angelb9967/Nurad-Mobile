# 🏨 Nurad: Mobile Hotel Booking System (Guest)

## ✨ Project Overview

Nurad is an Android application engineered to facilitate guest interaction with hotel services. 🛌🛎 

It streamlines room browsing, reservation processes, and booking management. Developed using Android Studio and Java, and leveraging Firebase for backend services, Nurad provides real-time room availability and secure transaction functionalities to end-users. It serves as the primary interface for guests to engage with the hotel's reservation system.

## 🌟 Key Features

Explore the core functionalities of Nurad:

*   **🔍 Room Catalog:** Enables users to view available hotel rooms, including specifications such as room type, amenities, and pricing.
*   **🔒 Reservation Process:** Supports the creation of new room reservations with robust data handling mechanisms.
*   **🗓️ Booking Management:** Provides functionality for users to access current and past bookings, monitor reservation statuses, and execute modification or cancellation requests (subject to established hotel policies).
*   **🔑 User Authentication:** Implements Firebase Authentication for secure user registration, login, and session management protocols.
*   **⚡ Real-time Availability:** Displays dynamic room availability information, synchronized instantaneously with the central hotel management system.
*   **📱 User Interface (UI):** Engineered for intuitive navigation and a clear, efficient booking experience for guests.

## 🛠️ Technologies Utilized

Nurad is constructed upon the following core technologies:

*   **Android Studio:** The official Integrated Development Environment (IDE) for native Android application development.
*   **Java:** The primary programming language for developing application logic and UI components.
*   **Firebase:** Employed as the Backend as a Service (BaaS) platform, integrating:
    *   **Firebase Realtime Database:** For efficient, synchronous, and scalable data storage and retrieval of room availability and booking records.
    *   **Firebase Authentication:** For secure user identity verification, registration, and session management.

## 🚀 Getting Started

These instructions detail the process of setting up and running the Nurad User application on a local development environment.

### 📋 Prerequisites

*   Android Studio (Latest Stable Version Recommended)
*   Java Development Kit (JDK) 8 or higher
*   A configured Google Firebase project with:
    *   Firebase Realtime Database enabled.
    *   Firebase Authentication (Email/Password provider) enabled.

### ⬇️ Installation

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/angelb9967/NUrad.git
    ```
2.  **Open Project in Android Studio:**
    *   Launch Android Studio.
    *   Select `Open an existing Android Studio project` and navigate to the root directory of the cloned `NUrad` repository.
3.  **Verify Firebase Configuration:**
    *   Ensure the `google-services.json` file is correctly placed within the `app/` directory of the Android project.
    *   Confirm that necessary Firebase SDK dependencies are accurately declared in both the project-level `build.gradle` and app-level `build.gradle` files.
4.  **Synchronize Gradle:**
    *   Allow Android Studio to perform a Gradle synchronization. Resolve any dependency resolution issues that may arise.

## ▶️ Usage

Post-installation, the Nurad application can be operated as follows:

1.  **Application Execution:**
    *   Connect an Android physical device with USB debugging enabled or launch an Android Virtual Device (AVD) via the Android Studio AVD Manager.
    *   Initiate the application build and run process by selecting the `Run 'app'` command within Android Studio.
2.  **User Registration/Login:**
    *   New users are prompted to register an account using email and password credentials.
    *   Existing users can authenticate via their registered login credentials.
3.  **Booking and Management:**
    *   Access the room catalog to view available options.
    *   Proceed with room reservation by submitting required guest and stay information.
    *   Utilize the dedicated 'My Bookings' section to view and manage personal reservation details.

## 📧 Contact

For any inquiries or feedback regarding this project, please reach out to the team:

| Name               | Email                                    |
| :----------------- | :--------------------------------------- |
| Allysandrei Aparicio | [***REMOVED***](mailto:***REMOVED***) |
| Angeline Bedis     | [***REMOVED***](mailto:***REMOVED***) |
| Angie Suson        | [***REMOVED***](mailto:***REMOVED***) |
| Jersey Usman       | [***REMOVED***](mailto:***REMOVED***) |

Project Repository: [https://github.com/angelb9967/Nurad-Mobile.git](https://github.com/angelb9967/Nurad-Mobile.git)
