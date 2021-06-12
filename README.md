# Lybre-Application

<h2>Machine Learning Documentation</h2>

<h3>Library and Requirements</h3>

<h3>Preprocessing Data</h3>

<h3>Recommendation System Model</h3>

<h3>Photo Detection (Unused)</h3>

<h2>Mobile Development Documentation</h2>

<h3>Feature:</h3>

1. <b>Splashcreen</b>, this is the beginning app before move to login activity.
2. <b>Login</b>, Login activity for user.
3. <b>Register</b>, Register activity for user.
4. <b>Borrow the book</b>, User is able to borrowed the book with capture the book what will be borrowed.
5. <b>Return the book</b>, User is able to return the book with GPS.
6. <b>Bookmark the book</b>, User is able to save/marked the book anytime for added to list borrowed.
7. <b>History of borrowed book</b>, User is able to see history of borrowed.
8. <b>Change Language</b>, User can change language ID/ENG.
9. <b>Search the book</b>, User can search book.
10. <b>Recommendation book for user</b>, User can see recommendation book after they borrowed the book.

<h3>Library/Depedencies:</h3>

1. <b>Kotlin Coroutines</b>, https://developer.android.com/kotlin/coroutines?hl=id
2. <b>Materials</b>, https://material.io/
3. <b>Live Data</b>, https://developer.android.com/reference/android/arch/lifecycle/LiveData
4. <b>View Model</b>, https://developer.android.com/reference/android/arch/lifecycle/ViewModel
5. <b>Circle image view (hdodenhof)</b>, https://github.com/hdodenhof/CircleImageView 
6. <b>Preferences</b>, https://developer.android.com/jetpack/androidx/releases/preference
7. <b>TensorFlow Lite</b>, https://www.tensorflow.org/lite/guide/android
8. <b>Glide</b>, https://github.com/bumptech/glide
9. <b>Room Database</b>, https://developer.android.com/jetpack/androidx/releases/room
10. <b>Paging</b>, https://developer.android.com/jetpack/androidx/releases/paging
11. <b>CardView</b>, https://developer.android.com/jetpack/androidx/releases/cardview?hl=id
12. <b>ML Kit Recognition</b>, https://developers.google.com/ml-kit/vision/text-recognition
13. <b>Fusedlocationprovider (GPS)</b>, https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi
14. <b>Chaquopy (Integrated python code)</b>, https://chaquo.com/chaquopy/

<h3>Acknowledgements:</h3> 

* <b>Memulai Pemrograman Dengan Kotlin</b>, https://www.dicoding.com/academies/80
* <b>Belajar Prinsip Pemrograman SOLID</b>, https://www.dicoding.com/academies/169
* <b>Belajar Membuat Aplikasi Android untuk Pemula</b>, https://www.dicoding.com/academies/51
* <b>Belajar Fundamental Aplikasi Android</b>, https://www.dicoding.com/academies/14/tutorials
* <b>Belajar Android Jetpack Pro</b>, https://www.dicoding.com/academies/129

<h2>Project Detail</h2>

<h3>Documentation & Installation:</h3>

1. Install and Import libraries needed
2. Download the dataset from Kaggle
3. Preprocessing the dataset
4. Create the model
5. Training the model with 5 epoch
6. Predict the dataset
7. Use the converter to convert the model into the tflite model

Clone repository from https://github.com/ramdhanjr11/SmartLibraryApp
Change python location in build.gradle[App] at python function to your python directory. 

buildPython "C:/Users/your_profile_directory/AppData/Local/Programs/Python/Python38/python.exe"

<h3>Usage:</h3>

1. For login you can use this account : username (Loremipsum1), password (Loremipsum123).
2. Wait 2 - 5 minutes for load data from local.
3. In home you still not see the recommendation book, waiting until 2 - 5 minutes, because recommendation running in the background, if it's done recommendation be appears.
4. Actived your connection before use the application.
5. Allow the camera, files, and gps permission to application.
6. Actived your gps location if you want to return back the book *if you are not actived you can't access feature from return book in home.
7. You can borrowed the book with capture book, example cover book : http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg, http://images.amazon.com/images/P/0002005018.01.LZZZZZZZ.jpg

<h3>Notes:</h3>

1. Our application is not final, we still have more function that we must develop and improve.
2. We not use cloud service for the application, therefore our application only use local data in android.
3. Our application still have more bugs.
4. Our application didn't had license from chaquopy because we didn't pay for this, thus you just given 10 minutes for usage the application in home 

<h3>Contact:</h3>

* Android Developer
  * <b>M Ramdhan Syahputra</b> - https://www.linkedin.com/in/m-ramdhan-syahputra-4a10731bb/
  * <b>Yossy Syahida</b> - https://www.linkedin.com/in/yossy-s-960b6b10b/
  
* Machine Learning Developer
  * <b>Afgani Fajar Rizky</b> - https://www.linkedin.com/in/afganifajar/
  * <b>Ergina Kamilia Putri</b> - https://www.linkedin.com/in/ergina-kamilia-putri-74665a1b6/

(Not usage cloud because inactive/responsive/contribute members)
* Cloud Computing
  * <b>Orel Revo Sackhi Usdelivian</b> - https://www.linkedin.com/in/orelrevo/
  * <b>Arif Wicaksono</b> - https://www.linkedin.com/in/arifwicaksono501/
