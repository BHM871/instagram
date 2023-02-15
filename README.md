<h1 align="center">Instagram</h1>
<h3 align="center">This app is a remake of Instagram, being able to register, be able to enter with email and password, take photos and publish, like and post description when posting a photo. The user can also follow other users who are registered in the database.</h3>

<div align="center">
  <img src="https://img.shields.io/static/v1?label=liscence&message=MIT&color=blue&style=flat">
</div>

## Description
<p>This project was developed with the ARCHITECTURE MVP + Clean Architecture, I used in this project libraries of Google Firebase, Glide, Image Cropped and Circle Image View. I also used Fragment and Camera Lifecycle. The app also saves images taken on it.</p>

## Features

- Themes dark e light
- Publishing photos with description
- Take pictures
- Follow or unfollow

## Index

<!--ts-->
  * [Common](#common)
  * [Activitys Folders](#activitys-folders)
<!--te-->

<article id="common">
  <h2 align="center">Common</h2>
  
  - ### [`base`](https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/common/base): 
      This folder contains all the generic classes and interfaces used in the application, such as Callback, my Dependency Injector, Cache with default functions, the basis for Fragment, etc.
  
  - ### [`extension`](https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/common/extension):
      This folder contains a file that contains common functions for the application, which can be accessed from anywhere.
  
  - ### [`model`](https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/common/model):
      This folder contains all the classes of data used in the application and the object that functions as a databasetemporary local data.
  
  - ### [`util`](https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/common/util):
      This folder contains classes with utilities for the entire application.
  
  - ### [`view`](https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/common/view):
      This folder contains preview screens that can be called from anywhere in the application and custom visualization element classes.
      
</article>

<article id="activitys-folders">
  <h2 align="center">Activitys Folders</h2>
  <p>All folders except the Common folder are in accordance with the <a href="https://www.actionlabs.com.br/insights/entenda-o-que-e-mvp-e-para-que-serve-essa-estrategia/">MVP</a> + the interfaces used during folder flow.</p>
  
  <section id="login-folder">
  
- ### [Login Folder](https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login)
    
    <hr>
    
    <section id="login-interface">
      <p>
        - The <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login/Login.kt">Login</a> interface aims to defines other two interfaces, one for layer of View and other for layer of Presentation.
      </p>
    </section>
    <section id="login-mvp">
      <div id="login-model">
        <h3><a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login/data">data</a></h3>
        <p>
          - <b>This layer in the MVP architecture contains two classes and an interface.</b>
        </p>
        <p>
          - <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login/data/LoginDataSource.kt">LoginDataSource</a> is the interface that defines rules for communication with the database, which is executed by the LoginFireDataSource class.
        </p>
        <p>
          - The <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login/data/LoginFireDataSource.kt">LoginFireDataSource</a> class is responsible for verifying that the email and password exist in the database and using Callback to return the response. Utilize a Google Firebase librarie.
        </p>
        <p>
          - The Repository classes used in all folders in the project, aim to define the class that will perform the called function. In this case there is only one option the <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login/data/LoginRepository.kt">LoginRepository</a>.
        </p>
      </section>
      <section id="login-presenter">
        <h3><a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login/presentation">presentation</a></h3>
        <p>
          - This layer contains a <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login/presentation/LoginPresenter.kt">LoginPresenter</a>, which is responsible for validating email and password arriving from the View, calls the Model layer and defines the function that will call in the View.
        </p>
      </div>
      <div id="login-view">
        <h3><a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login/view">view</a></h3>
        <p>
          - This layer is responsible for defining and executing all visualization business rules in <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/login/view/LoginActivity.kt">LoginActivity</a>, such as showing error messages and defining how they will be shown, showing or hiding the ProgressBar, etc.
        </p>
      </div>
    </section>
  </section>
  <section id="register-folder">
  
- ### [Register Folder](https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register)
    
    <hr>
    
    <section id="register-interface">
      <p>
        - The Register folder contains three interfaces, because RegisterActivity manages four fragments and three of these fragments need interfaces. Interfaces aims to defines other two interfaces, one for layer of View and other for layer of Presentation.
      </p>
    </section>
    <section id="register-mvp">
      <div id="register-model">
        <h3><a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register/data">data</a></h3>
        <p>
          - <b>This layer in the MVP architecture contains three classes and one interface.</b>
        </p>
        <p>
          - <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register/data/RegisterDataSource.kt">RegisterDataSource</a> is the interface that defines rules for communication with the database, which is executed by the RegisterFireDataSource class and/or by the RegisterLocalDataSource.
        </p>
        <p>
          - The <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register/data/RegisterFireDataSource.kt">RegisterFireDataSource</a> class uses two Google Firebase libraries to register new users and verifies that the email you enter already exists.
        </p>
        <p>
          - The <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register/data/RegisterLocalDataSource.kt">RegisterLocalDataSource</a> class aims to verify the session user and add the new user to the application cache.
        </p>
        <p id="repository">
          - Just like the others Repositorys the <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register/data/RegisterRepository.kt">RegisterRepository</a> aims to defining the class that will perform the function called.
        </p>
      </div>
      <div id="register-presenter">
        <h3><a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register/presenter">presentation</a></h3>
        <p>
          - This layer contains three presenter, one for each fragment that needs something that is not the responsibility of the view.
        <p>
          - The <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register/presenter/RegisterEmailPresenter.kt">RegisterEmailPresenter</a> which is responsible for validating email arriving from the View, calls the Model layer and defines the function that will call in the View. 
        </p>
        <p>
          - The <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register/presenter/RegisterNamePasswordPresenter.kt">RegisterNamePasswordPresenter</a> which is responsible for validating password and username arriving from the View, calls the Model layer for create user and defines the function that will call in the View. 
        </p>
        <p>
          - The <a href="https://github.com/BHM871/Instagram/tree/master/app/src/main/java/co/tiagoaguiar/course/instagram/register/presenter/RegisterPhotoPresenter.kt">RegisterPhotoPresenter</a> is responsible for calling the Model layer to update the user to add a profile picture and defines the function that will call the View.
        </p>
      </div>
      <div id="register-view">
      </div>
    </section>
  </section>
</article>

<h3 align="center">:warning: TODO: README em processo. :warning:</h3>
