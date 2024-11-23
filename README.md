# Simple Notes

## Members
Gabini, Brian Pitallo
Verano, Carl Matthew

## Description

This application is a local note-taking app designed to help users efficiently manage their notes. It provides the following functionalities:

- **Create, Read, Update, Delete (CRUD)**: Users can create new notes, edit existing ones, and delete notes. Deleted notes are moved to a recycle bin, where they can either be restored or permanently removed.
- **Sorting and Filtering**: Notes can be sorted and filtered by date, title, or color, making it easier to find specific information.
- **Search**: Users can search for notes by keywords for quick access to relevant entries.
- **Built-in Camera with OCR**: The app includes a camera feature that allows users to capture images and extract text using Optical Character Recognition (OCR). The extracted text can be added to notes.
- **Image Support**: Users can attach images to their notes, enhancing the app's usability for tasks like organizing visual information or creating image-based reminders.

## Additional Features
- **Recycle Bin Management**: Deleted notes are stored temporarily in a recycle bin, giving users the option to recover or permanently delete them.
- **Simple Interface**: The app focuses on providing a clean, user-friendly experience.

## Architecture
The application is built using **Clean Architecture**, which organizes the code into layers for better maintainability and scalability. This approach ensures:
- Clear separation of responsibilities between components.
- Easier updates and the addition of new features.
- Improved testability for a more reliable application.

## Requirements
- Android Studio
- Required dependencies (listed in the `build.gradle` file)

## Installation
1. Clone the repository.
   ```bash
   git clone [repository link]
   ```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or a physical device.

## Usage
1. Create, edit, and delete notes as needed.
2. Use the sorting, filtering, and search features to organize and find notes.
3. Utilize the camera and OCR feature to extract text from images and add it to notes.
4. Attach images to notes for better organization or visual reminders.

## Future Improvements
- Add support for cloud backups to synchronize notes across devices.
- Enhance search functionality with tag-based and content-based filters.
- Include collaborative features for sharing notes.

## Screenshots
Below are placeholders for the application's key features. Replace these placeholders with actual screenshots from the app.

### Home Screen
![1_Homepage](https://github.com/user-attachments/assets/474185db-3c24-4422-8b87-6771f9aefbcb)

### Swipe-Delete Notes
![2_SwipeDeleteHomepage](https://github.com/user-attachments/assets/1d8cdaaa-e2b4-4496-b469-2dd8f6696df4)

### Undo Delete Notes
![3_UndoDelete](https://github.com/user-attachments/assets/2fae2aaa-6fff-4244-a3a5-a665c2a6e29c)

### Sorting Notes
![4_SortNotes](https://github.com/user-attachments/assets/9601b795-43e1-4c7b-a9e3-1a5a85085e76)

### Searching Notes
![5_SearchNotes](https://github.com/user-attachments/assets/cb7a2af1-c6b9-446f-b3d1-4d313f08141d)

### Restore Notes
![6_RestoreNote](https://github.com/user-attachments/assets/327389a1-7466-4186-b387-cc4d4948fb2b)

### Camera with OCR
![7_OCR_Active_Camera](https://github.com/user-attachments/assets/24ad99ce-45b1-41f6-a91e-f80e9e9e47ff)
![8_OCR_Results](https://github.com/user-attachments/assets/6d7b8f3f-764d-4083-9da2-86e855ec519c)
![9_OCR_Copied](https://github.com/user-attachments/assets/553532de-5b29-4c69-88e2-6f1e8a9f1b3f)
![10_OCR_Results_Pasted](https://github.com/user-attachments/assets/2ab44104-ffee-4c8e-9406-7f60baeb229a)


### Night Mode
![11_NightMode](https://github.com/user-attachments/assets/17a15c6f-ba9d-4adc-996c-1ecb1cfe1499)
![12_Home_NightMode](https://github.com/user-attachments/assets/db7fb622-e9d7-4c11-8fd0-5e8f79fa06c1)


