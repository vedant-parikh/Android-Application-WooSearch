# Android Application WooSearch

WooSearch is an application which displays the reviews and other details of various categories such as Movies, Food etc. and display the details using the API’s of different websites such as Wikinik, Yummly etc.

The data fetched from different websites in in form of JSON and parsed using JSONParser.

#Functionalities

Login:
An Existing user can login using their Username and Password in the application. The application will check the username and password using the parse API, and if it matches, it will connect the user to the application.

Search and Store Movie Rating:
Here, User can search any movie from IMDB and it will fetch the reviews of the user, that is, the rating given by the IMDB users and display along with name, picture and ratings of the movie. Apart from that, it also displays the ratings given by the application users. The users can also give their ratings which will be stored on cloud. So, whenever user searches for the same movie, it will display the IMDB ratings, ratings given by the Application Users and the ratings given by the particular user.

Search Best Meal:
On this activity, I fetch the values of different food from the yummly.com and display them. The values contain name of the dish, the image of the dish, the ratings given by yummly.com and the ingredients of that particular dish.

Dictionary:
On this activity, a user can search a particular word, and application will display the name, meaning and example of that word used in a particular sentence. I fetch the data from wordnik.com


#Classes

DisplayActivity.java -
In this application, I have added functionality to displayed the application image for 1 second when the application starts.

SignUpActivity.java - 
In this Activity, I have used parse API to create the Signup Activity

LoginActivity.java - 
In this Activity, I have Used Parse API to Login Activity

ParseApplication.java - 
In this file, I have assigned the application and client keys which authenticates my cloud database.

MainActivity,java - 
In this file, I have set by calling the FragmentStatePagerAdapter to swipe between different fragments having different categories.

PagerAdapter.java - 
In this File, I have assigned all the 3 fragments of 3 categories that I am using as menu of FragmentStatePagerAdapter.

Movie_fragment.java - 
In this file, I have used JSONParser to fetch and parse json data which contains name, image and ratings of movies. I have also fetch the in application user ratings and set the user ratings using Parse API.

RecipeFragment.java - 
In this file, I have used JSONParser to fetch and parse json data which contains name, image and ingredients of different dishes that we search.

DictionaryFragment.java - 
In this file, I have used JSONParser to fetch and parse json data which contains name, meaning and usage in sentence of different English words that we search.
