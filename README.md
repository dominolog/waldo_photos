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
In order to configure the app, edit build.gradle file and set below values:

* buildConfigField **WEB_SERVICE_URL** - url to GraphQL service. By default set to https://core-graphql.staging.waldo.photos/
* buildConfigField **AUTH_COOKIE** - Authentication cookie obtained from login operation. A value generated is already set.