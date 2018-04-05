# Project-13 (ECSE 321, McGill University, Winter Semester 2018, Professor: Daniel Varro, Tree Management System)
This project was done on 6 different deliverables for the course of ECSE 321 on McGill Github.

![Image of Branches](https://image.ibb.co/dvMt1c/Introductory_Slide.png)



Table of Contents
=================

   * [Project-13 (ECSE 321, McGill University, Winter Semester 2018, Professor: Daniel Varro, Tree Management System)](#project-13-ecse-321-mcgill-university-winter-semester-2018-professor-daniel-varro-tree-management-system)
   * [Table of Contents](#table-of-contents)
      * [Introduction](#introduction)
      * [The Team](#the-team)
      * [Challenges Faced](#challenges-faced)
      * [Project Iterations:](#project-iterations)
         * [Iteration 1 (Design)](#iteration-1-design)
            * [choice of architecture](#choice-of-architecture)
            * [domain model](#domain-model)
            * [use cases](#use-cases)
         * [Iteration 2 (Prototype)](#iteration-2-prototype)
         * [Iteration 3 (Testing)](#iteration-3-testing)
         * [Iteration 4 (Release PipeLine)](#iteration-4-release-pipeline)
         * [** Iteration 5 and 6 are the presentation and the documentation phases **](#-iteration-5-and-6-are-the-presentation-and-the-documentation-phases-)
      * [List of Tools Used:](#list-of-tools-used)
      * [Feature List](#feature-list)
         * [Adding A tree](#adding-a-tree)
         * [Log in/ Register](#log-in-register)
         * [Modifying a tree](#modifying-a-tree)
         * [Listing all information stored on the server](#listing-all-information-stored-on-the-server)
         * [Downloading the APK from the website](#downloading-the-apk-from-the-website)
      * [Unique Features](#unique-features)
         * [Google-maps-vue2](#google-maps-vue2)
         * [Displaying statistical data](#displaying-statistical-data)
         * [Secure Login](#secure-login)
      * [To make it work](#to-make-it-work)
         * [War file](#war-file)
         * [build android](#build-android)
         * [build the frontend](#build-the-frontend)
      * [To DO (in future iterations)](#to-do-in-future-iterations)



## Introduction
The system is a Tree management system that displays trees on a map, which allows to modify/update trees. The purpose of this app is to display statistical data useful to the users who are considering a what-if scenario in case of building a new construction over some specific area. It provides bio-diversity index according to search criteria for example Municipality, Species, and Status. We have also decided to release the app to the public domain. It consists of:

1. A Java spring Application (BackEnd)

2. An Android Application (FrontEnd)

3. A front-end running Vue.js (FrontEnd)

![Image of Branches](https://image.ibb.co/gHprnH/trees.png)

![Image of Branches](https://image.ibb.co/nPmU7H/tree.png)

## The Team
1. Al Homsi, Elias
https://www.linkedin.com/in/ealhomsi/

2. Chuong, Kevin
3. Davis, Jeremy
4. Décéus, Oscar

## Challenges Faced
* Q: Which architectural styles apply best?
* Q: How to test effectively?
* Q: How to keep user authentication secure?
* Q: How to define areas for biodiversity index?

## Project Iterations:
### Iteration 1 (Design)
Iteration one was the designing phase which included things like
#### choice of architecture
For this project three main architectures were used:

1. The MVC: 
the global structure which specifies the backend part and the two different front-ends. This choice was given because the two front-ends share the same controller. Also, It helps to separate the concerns into three different parts.

![Image of Branches](https://preview.ibb.co/dhErnH/mvc.png)

2. The MMVM:
This structure was used on the website front-end in order to grab the contents of the backend while keeping them in javascript temporarily for display. The model-view objects are DTO (Data transfer objects) that were used to update the view.

![Image of Branches](https://preview.ibb.co/cgGRMc/mvmm.png)

3. The Layered Architecture:
The backend was further separated into two different parts: 

The first being the REST-Controller which contain the API calls and handle user requests to the backend.
, The second being the service layer which contains the business logic and operations.

![Image of Branches](https://preview.ibb.co/jsqmMc/layeredarch.png)


#### domain model
The domain model is implemented using Umple designing language. The model consists of the following classes: Tree, Resident, Location, Municipality, TreePLESystem, Transaction.

1. Tree: 
Is the Tree class which contains parameters for the tree such as the diameter, status, and species.

2. Municipality:
This class contained the names of the municipality in the system.

3. Resident:
This class contained the name, email and the securely stored hashed password of the user.

4. Location:
This is a helper class that contained the LatLng (Latitude and Longitude) of a point on a map.

#### use cases
The uses cases were generated to match the requirements:

1. A resident login and adding a tree

2. A resident Register and log in and modifying a tree

3. A resident downloading the apk to his/her phone from the website and then running the app

4. A resident asking for bio-index information through the specification of a polygon or a search Criteria

5. Clicking on a tree would show you directly the tree place.

Logic rules are also implemented to make sense for modifying a tree. For example, a cutdown tree cannot be updated to healthy. Another example, A resident cannot set a tree to diseased unless he/she introduced that tree into the system. Therefore, Login is required when modifying trees.

### Iteration 2 (Prototype)
In iteration 2 an initial prototype of the web front-end and the backend was implemented. Challenges in applying the google maps API and integrating it into the vuejs system. google-maps-vue2 library was used to solve this issue. Also, trees were retrieved from the backend to display on the front-end. The purpose of this iteration is testing the feasibility of the system while getting some information in depth about the technical requirements of the project.

### Iteration 3 (Testing)
An extensive amount of tests were written for the backend using JUnit Testing in Java. Tests are conducted before pushing to the release branch on GitHub. The testing suits were written for both the service part of the backend layer and the Controller part. A stub was created for the service part to test the Controller upon.

![Image of Branches](https://preview.ibb.co/n2LLgc/integration.png)

Integration Testing

### Iteration 4 (Release PipeLine)
The automated build plan was implemented using GitHub and Jenkins. Jenkins was set up on the server of http://ecse321-12.ece.mcgill.ca:8081/. Jenkins would automatically check the release branch of the project for new pushes. In this case, Jenkins would clone the changes and build the backend using the gradle script. The bash script would also execute to run both the gradle for Android to build the apk and make it available on the website. The website would be built using npm run build and deployed using Apache httpd server. The backend is implemented using the Tomcat server. Follow the notes on the slides for more details on the server configuration also, find attached the build scripts. Jenkins is deployed on 8081, Tomcat is deployed on 8080, and the Apache is deployed on 8087.



![Image of Branches](https://preview.ibb.co/h4VxSH/branches.png)

Development Branches


![Image of Branches](https://image.ibb.co/gLpP7H/release_Pipeline.png)

Build

![Image of Branches](https://preview.ibb.co/hMTTZx/deploy.png)

Deploy

### ** Iteration 5 and 6 are the presentation and the documentation phases **


## List of Tools Used:

* Eclipse
Eclipse was used to code the Java Spring backend

* Sublime
Sublime was used as the main text editor for the web development

* Gradle
Gradle was used to build and main the releases

* Vue.js
The website front-end was coded using Vue.js

* Spring Java
J2EE java to help to build the backend

* Jenkins
An automation build and deployment tool used to deploy releases

* LucidChart
Used to generate diagrams and data-models

* Ngrok
Ngrok is a port exposer that allows exposing a local port for the public internet. This allowed us to make the android app work without the restriction of using VPN to connect to McGill's network. Alternatives are localtunnel.

* AndroidStudio
Android studio was used to develop the Android application.

![Image of Branches](https://preview.ibb.co/j4AWnH/tools.png)


## Feature List

### Adding A tree
on the website right click on the map, on android it is a long press on the map. A pop up would show us asking for information regarding the tree.


### Log in/ Register
in order to be able to modify a tree you should first login/ register on the website. Those credentials would be used on the android application to login to the system.

### Modifying a tree
Once you click on a tree a bubble would show up to help you modify a tree. Also, in android a long press on that bubble would do the same effect. The modification would ask for the new status to be applied.

### Listing all information stored on the server
On the website menu tabs are provided so that it would be possible to list information regarding objects in the system. If you click on trees for example, you would see some data highlighted. Clicking on any of these would initiate a search criteria based on all trees of that criteria and display statistical information.

### Downloading the APK from the website
The website allows the user to download the apk.

## Unique Features

### Google-maps-vue2 
source: https://www.npmjs.com/package/vue2-google-maps
This is the native library of google maps which allowed easy interaction in Vue.js, The library was used heavily in developing markers and polygon feature for tree selection. Work includes Customized icons, customized notification windows, information bubbles and **custom polygons**.
To Add a tree right click on the map.

![Image of Branches](https://image.ibb.co/j46t1c/gmapspolygon.png)

### Displaying statistical data
Many different search criteria could be used to filter out a group of trees and display information about most frequent trees in that group. For example, trees could be filtered based on species, diameter, municipality and using a polygon. The data outputted in the format of percentages of the most frequent trees in that group.
![Image of Branches](https://image.ibb.co/fmOvEx/data.png)

### Secure Login
Secure standard practices in making and saving the password. Instead of sending the password in clear text, the website hash and salts the password. Only this version is checked against the Database. This ensures that the backend does not store users password.

![Image of Branches](https://image.ibb.co/iXm47H/secure_Login.png)
Secure Login


## To make it work
* modify the application.properties inside the spring application to match the server port you are intending to run it on

* modify the build/configuration.js to specify which port and host would be the backend and the front end

* modify the URL inside HTTP utils in the android studio application

* make sure your ports are open and if you are trying to run it in McGill make sure you install your VPN

Once the configuration is done. Use the build scripts to generate the:

### War file
*copy the war file to the /var/lib/tomcat/webapps/ROOT/

*give it the right access permissions

*restart the tomcat server using systemctl tomcat restart

### build android
* run the ./gradlew assembleRlease inside the root folder of the android

* using the gradle wrapper the app will generate the apk

* Please note that you may need to sign it with your own key (my key is protected on my machine)

### build the frontend
* npm run build -> from inside the root folder of the website

* this will generate a compiled website with css optimized (with other stuff)

* copy /dist to /var/www/html/

* restart the apache server and configure it to run on 8087

* systemctl restart apache / systemctl restart httpd

## Project Management


### Members
* Elias Al Homsi Software Lead
total hours spend on the project is 8 hours per week
Leadership roles: hold meetings and coordinates integration of new developed features.

* Kevin Chuong - Documentation Manager

* Jeremy Davis - Head of Quality Assurance

* Oscar Décéus - Head of Testing

### Efforts per deliverable
* The first deliverable took a total of 26 hours between all team members.

* The second deliverable took a total of 23 hours between all team members.

* The third deliverable took a total of 30 hours between all team members.

* The forth deliverable took a total of 40 hours between all team members.

* The fifth deliverable took a total of 31 hours between all team members.

* The sixth deliverable took a total of 36 hours between all team members

** Please find more information on iterations, meeting and project managment in the Documents folder **

## To DO (in future iterations)
* Color trees based on species

* Deploying on a secure SSL tunnel website (https)

* Adding redundancy to the system.

* Cluster trees based on the type and find out the distances between those clusters

* Providing more biodiversity indexes

* Testing the frontend using Mocha js

Thank you for Reading

Sincerely,
Team 13