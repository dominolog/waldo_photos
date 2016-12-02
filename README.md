# Android project for Waldo Photos 

This is demo project for recruitment process as Android Engineer for Waldo Photos.

The author of this project is CUBESOFT Dominik Tomczak

## General information

The application is written in Android Studio. It uses below libraries:

* OkHttp for HTTP stack
* Retrofit 2 for REST GraphQL webservice access
* RetroLamba for Lambda support for Android
* Picasso for robust image loading
* RxJava for all background operations
* ButterKnife 2.0 for view injections

## Configuration
In order to configure the app, edit **build.gradle** file and set below values:

* buildConfigField **WEB_SERVICE_URL** - url to GraphQL service. By default set to https://core-graphql.staging.waldo.photos/
* buildConfigField **AUTH_COOKIE** - Authentication cookie obtained from login operation. A value generated is already set.

## Problem assessment
The load more function is implemented by detecting of reaching the last element of RecyclerView in SwipeRefreshLayout. If this happens, the application asks its Model loader to load more data and the newly added "page" is added to the 
esisting adapter. The Model.java implements both API access and in memory caching of data. If for instance the screen is rotated, the data is taken from cache, not reloaded. In the production implementation
there should be of course some kind of disk caching using Sqlite or ORM based mechanism.

##Screen Shots
![alt tag](https://github.com/dominolog/waldo_photos/blob/master/device-2016-12-02-181620.jpg?raw=true) ![alt tag](https://github.com/dominolog/waldo_photos/blob/master/device-2016-12-02-181649.jpg?raw=true)

![alt tag](https://github.com/dominolog/waldo_photos/blob/master/video.gif)