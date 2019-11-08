# DEVELOPER GUIDE

## What Is The Project?

Bast is a lock security system that is built to allow owners of small businesses or homes to
safeguard their space and belongings by giving members specified access to different rooms
throughout the space. This Android Studio component accompanies the physical lock and middleman
hardware of the project. It's purpose is to run the operations that connect to the middleman,
access the databases of users and locks, and run the app component.

Along with this app, the middleman is run by a controller that manages the settings, the
databases, and the connection between the app and the locks.

## What Are The Features?

- *Add a user:* If this feature is called to a system that has no other users, this user will
become the admin of the system.
- *User Levels/Roles:* Controls the features and accesses the user has
- *Timezones:* Grants access to users only at certain times of the day for a particular room
- *Anti-Passback:* A security protocol that will help prevent improper access. Ensures that an
order must be followed for particular doors
- *Temporary Users:* The admin(s) can give temporary access to keyholders
- *Factors:* Configuration with key cards or key codes
- *Temporary Keycodes* - the admin(s) can give temporary access keycodes
- *Multi-User Authentication:* Multiple users would be needed to gain access through a
particular lock
- *Multi-factor authentication:* The user would need multiple levels of authentication to gain
access.
- *Log Files:* A history of the people and the doors access would be recoded and logged
- *Air-Lock Mode:* For hallways, the first lock would have to be closed after entry for the
second lock at the end of the hallway to be opened
- *Alarm:* If too many failed attempts, and alert sound will ring
- *Toggle mode:* Can change to a standard lock

## How To Install

1. Install Android from the [Android Developer website](https://developer.android.com/studio/). Follow the steps to download using all the default settings.
2. Clone the Android-App source code
3. Open and build the project from Android Studio
4. Clone the Controller source code to your broadcom controller
5. Navigate to the controller source code and run ``mix run --no-halt``. Alternatively, the app can be run as a Nerves app.

## What Are The Components?

There are 3 components to this project, only 1 of which is controlled by the Android Studio app:

**1. Android App**
The app is necessary for users and admins to be added to a system or to control the settings in
the system. It provides the data such as the user's name, public and private key, administrative
features, and is the main communicator between the user and the middleman. This is all managed
within Android Studio.

**2. Middleman**
The middleman has the role of communicating the user's data to the locks in the system. It will
contain the databases and settings of the system.

**3. Hardware**
The hardware in this system are the locks, keypads, and key cards that are used. These will all be
managed by the middleman, but controlled by the Admin(s) using the Android App.

## How To Contribute

The source code can be found on the GitHub oraganization:
[Github organization](https://github.com/Bast-Security)
Within this organization, the different repositories used for this project can be found.
[Branding](https://github.com/Bast-Security/Branding)
[Android App](https://github.com/Bast-Security/Android-App)
[Controller](https://github.com/Bast-Security/Controller)
[Lock-Firmware](https://github.com/Bast-Security/Lock-Firmware)

## Supported Systems

The Android App requires Android 4.3 (Jellybean) or above.

The Controller firmware targets the Broadcom SoC's, and officially supports the Broadcom BCM2837.
The firmware is written in Elixir, and requires Elixir 1.8 or newer with Erlang/OTP 22 or newer.

The Lock firmware is compatible with megaAVR compliant microcontroller.

