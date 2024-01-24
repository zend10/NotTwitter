<h1>NotTwitter</h1>
<p>Completely testable Android app built with Compose and MVVM.</p>

## Tools
- Compose
- Voyager
- Koin
- Realm
- Coil
- kotlinx-datetime
- Turbine
- Firebase Authentication
- Firebase Firestore
- Firebase Storage

## How to run
1. Add your own `google-services.json` to `app` folder.
2. Set up Firebase Authentication and use Email/Password authentication.
3. Set up Firebase Firestore and set these rules and indexes:

    ```
    rules_version = '2';

    service cloud.firestore {
        match /databases/{database}/documents {
            match /users/{uid}/{document=**} {
                allow read, write: if request.auth != null && request.auth.uid == uid;
            }
            match /posts/{uid}/{document=**} {
                allow read: if request.auth != null
                allow write: if request.auth != null && request.auth.uid == request.resource.data.userUid;
            }
        }
    }
    ```
    
    ```
    Collection ID : posts
    Field indexed : userUid Ascending createdOn Descending __name__ Descending
    Query scope   : Collection
    ```
4. Set up Firebase Storage.
    ```
    rules_version = '2';

    service firebase.storage {
        match /b/{bucket}/o {
            match /images/{uid}/{allPaths=**} {
                allow read, write: if request.auth != null && request.auth.uid == uid;
            }
        }
    }
    ```