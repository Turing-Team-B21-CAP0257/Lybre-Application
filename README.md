# Lybre-Application

<h2>Machine Learning Documentation</h2>

The code was originally developed using google colab notebook due to its feature and the support of GPU computation
here's the link for the notebook : https://colab.research.google.com/drive/1d1RmXX_G6G6Ltqx8D5t7Tw8reXcpVALG?usp=sharing

<h3>Library and Requirements</h3>

This project use following library to support the program:
1. Python os                 
2. Python re                 
3. Python csv              
4. Python string           
5. Numpy
6. Pandas
7. Tensorflow
8. Matplotlib Pyplot
9. Train test split from Sklearn

Make sure to fully download it with pip command (if not available as default in your IDE) and import the library before running the program 

<h3>Get Data</h3>

The dataset used for this application is the book recommendation dataset from Kaggle, collected by Cai – Nicolas Ziegler from Book – Crossing community. The dataset contains 278,858 users providing 1,149,780 ratings about 271,379 books. The dataset comprises of 3 files:

- Users : 
Contains the integer value of User-ID, and the location and the age of user if available.
- Books : 
Contains the valid respective ISBN and some content-based information of the book (Book-Title, Book-Author, Year-Of-Publication, Publisher). Moreover, it contains URLs linking to cover images, appearing in three different size for small, medium, and large that point to the Amazon web site.
- Ratings : 
Contains the book rating information based on User-ID and book ISBN. Ratings are expressed on a scale from 0-10.

Here's the link for the original dataset: https://www.kaggle.com/arashnic/book-recommendation-dataset

<h3>Preprocessing Data</h3>

Before we do the machine learning process, we need to do some preprocessing to the data to fit the data to machine learning process and to android database. We divided the preprocessing process to 4 section based on what thing and what data that we preprocess.

- Rating Section 1 :
In this section, we fix the rating column name by change the title to lowercase and remove the punctuation, and we create new table called 'newrate' to get the average rating based on book ISBN. 
- Book Section :
In this section, we fix the book column name, and joining the book data with 'newrate' data to add average rating to the data, and add zero if there is none of it. We also removing invalid ISBN after data merge, then we make new bookid based on ISBN to make it easier for android team, and then removing comma from the data which can corrupt the csv file. Lastly, we make new csv file for android team which contains book data that has been preprocessed
- User Section :
In this section, we fix the user column name, and add zero value to NaN data in the age column, and make new csv file which contains user data that has been preprocessed
- Rating Section 2 :
In this section, we create new table called 'isbn_id' to get book ID based on book ISBN, and then we joining the rating data with 'isbn_id' to add book ID to rating data. Then, we remove the invalid ISBN and convert the book ID data type to Integer. Lastly, we make new csv file which contains rating data that has been preprocessed

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
