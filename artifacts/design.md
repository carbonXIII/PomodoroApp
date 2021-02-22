Populate each section with information as it applies to your project. If a section does not apply, explain why. Include diagrams (or links to diagrams) in each section, as appropriate.  For example, sketches of the user interfaces along with an explanation of how the interface components will work; ERD diagrams of the database; rough class diagrams; context diagrams showing the system boundary; etc. Do _not_ link to your diagrams, embed them directly in this document by uploading the images to your GitHub and linking to them. Do _not_ leave any section blank.

# Program Organization

User story 1 focuses on basic app functions, and makes use of mock layouts that we can swap out with
the finalized UI layouts that will be generated as part of user story 5.

## System Context Diagram
For this user story there is no need for any backend information, there is only basic timer functionality, contained entirely within the app.

![System Context Diagram](/artifacts/images/001-system-context-diagram.png)

## Container Diagram
To make testing the different components of the app simpler, the application is divided into 2 layers the UI and API layers.
In the future, if we wanted to implement a web or other user interface we could simply exchange the android UI layer with this new component.

![Container Diagram](/artifacts/images/001-container-diagram.png)

## Component View

- The UI is composed of an activity (extended from the Activity class from the AndroidSDK) as well as a group of fragments.

    a. The Main Activity is the only piece of the UI that depends on components from the API, since it manages the lifetime of those objects.
    
    b. Fragments/Views implement all the UI code (manipulating UI elements and submitting forms).

- The API layer is broken down into 2 parts as well, a model component and a presenter component.

    a. The presenter handles form validation and acts as a liason between the UI layer and the model.
    
    b. The model serves as a wrapper around a source of data.
        In this case, there is only one Task, and information about that Task is not store in a DB.
        In the future, we can swap out this layer with a different backend implementation without worrying about breaking API compatibility with the rest of the app.
        
![Component Diagram](/artifacts/images/001-component-diagram.png)


# Code Design

### Primary View Classes

![Fragment View Collaboration Diagram](/artifacts/images/doxygen_generated/classorg_1_1team_1_1app_1_1view_1_1FragmentView__coll__graph.png)

This collaboration diagram shows the base class for all fragments, `FragmentView`, derived from the Android SDK class `Fragment`.
Its lifetime is managed by the main activity class. The `ActivityListener` provides an interface the view can use to trigger screen transitions.
    
### Primary Model Classes

![Task Store Collaboration Diagram](/artifacts/images/doxygen_generated/classorg_1_1team_1_1app_1_1model_1_1TaskStore__coll__graph.png)

`TaskStore` and `Task` are the major classes that make up the interface to the model. Since there is no DB interaction, the classes are fairly simple, though the interface should not change very much if a more complicated backend is added.
    
### UI-API Contracts

![Base View Collaboration Diagram](/artifacts/images/doxygen_generated/interfaceorg_1_1team_1_1app_1_1contract_1_1BaseView__coll__graph.png)

![Base Presenter Collaboration Diagram](/artifacts/images/doxygen_generated/interfaceorg_1_1team_1_1app_1_1contract_1_1BasePresenter__coll__graph.png)

- `BaseView` allows the main activity to set a presenter to handle events for a view.
- `BasePresenter` defines the interface for the presenter side of the contract, providing a method to call on start.

Additonal messages between the view and presenter are represented by functions, as show in this interface for the timer screen contract. Take the timer contract for example:

![Timer Presenter Collaboration Diagram](/artifacts/images/doxygen_generated/classorg_1_1team_1_1app_1_1presenter_1_1TimerPresenter__coll__graph.png)

![Timer View Collaboration Diagram](/artifacts/images/doxygen_generated/classorg_1_1team_1_1app_1_1view_1_1TimerView__coll__graph.png)

- The interface `TimerContract` provides to sub-interfaces `TimerContract.View` and `TimerContract.Presenter`.
- The presenter implements a method `onTimerComplete()` that is not present in the `BasePresenter` class, acting as a callback for the View. The presenter also maintains a handle to the `TaskStore` to retrieve task information that it will pass to the view for display.
- The view implements a method `setTitle()` to allow the presenter to set the timer title and task name

## Sequence Diagram
  This rough sequence diagram shows the flow of messages during the use case for user story 1 (basic timer implementation). It is broken down into the same "Model-View-Presenter" containers, but also considers the user as an actor. TODO
  
## Class Overview
| Class             | User Story | Description                                                                                                 |
|-------------------|------------|-------------------------------------------------------------------------------------------------------------|
| FragmentView      | 001, 005   | Handles fragment creation.                                                                                  |
| ActivityListener  | 001, 005   | Main interface to the main activity for the fragments.                                                      |
| TaskStore         | 001, 003   | Currently just an in-memory task store, but will provide the interface for interacting with the DB in U003. |
| Task              | 001, 003   | Represents a single task. Will store cached results from the DB in U003.                                    |
| BaseView          | 001        | Part of the View-Controller interface.                                                                      |
| BasePresenter     | 001        | Part of the View-Controller interface.                                                                      |
| TimerContract     | 001, 005   | The additional methods needed for the timer screen.                                                         |
| TaskSetupContract | 001, 005   | The additional methods needed for the task setup screen.                                                    |
|                   |            |                                                                                                             |


# Data Design

![ER Diagram](/artifacts/images/Entity_Relationship_Diagram.PNG)
## Entity Overview
We have tried some databases primarily micrsoft access and the cloud firebase database.
We will most likely use Firebase and a single databse. Multiple Databases will add uneeded complexity this early in development and we just want to be able to store data in a database. If in the future we require mulitple databases we will add them.
Each User will make an account with a user and password and other optional information. This account will be able to create tasks with a title and duration for breaks and focus time, the date and time of the task will be recorded and also if the task was completed or not, meaning both timers run out. Each Task may have a Reminder 0 or 1 or many. The Reminder records the day and time of a reminder to be sent to the user. The Detailed Statistics gets updated when a new Task is completed and holds data for Total Time in focus and break. That is all that we have so far for Detailed Statistics but in the future we will implement a weekly and daily sections for more data. We will implement a deeper Detail Statistics Entity later when each of us are more familiar with firebase and it's connection to our android sdk. For now we will just save data for 1 user in a file.

| Entity              | User Story | Description                                                  |
| :------------------ | :--------- | ------------------------------------------------------------ |
| Account             | 003 ,  012 | security of data for a user and also in order for the data for detailed Statistics to be recorded |
| Detailed Statistics | 003        | Updates from data given from completed tasks and stores data for daily, weekly, and, monthly progress. Used to track progress User process to a goal. (Goal not yet implemented) |
| Task                | 001        | Allows for the start of a Pomodoro session split into focus and break time to best employ the Pomodoro Method |
| Reminder            | 001 , 008  | Allows for a time and date for a notification to be sent to the user about a upcoming or passed Pomodoro Session |

    


# Business Rules

At this time, our design does not have any business rules. In the future, we
might need to meet some standard to publish the app to the Android Store, but
currently our app does not use any special permissions, so no additional
restrictions are in place.

# User Interface Design

You should have one or more user interface screens in this section. Each screen should be accompanied by an explaination of the screens purpose and how the user will interact with it. You should relate each screen to one another as the user transitions through the states of your application. You should also have a table that relates each window or component to the support using stories. 



# Resource Management

The Android SDK provides information about the lifetime of Fragments and
Activities, the base classes that handle the UI code. Any objects linked to the
lifetime of one of these UI classes can not be guaranteed to survive the app
shutting down. To handle this, information about the current task needs to be
saved in a bundle, a structure that is serialized and saved when a
Fragment/Activity is torn down. Information about the current task that cannot
be loaded from a database (as is the case for the current task in the U001)
should be saved to a bundle to prevent data loss. For the database side since our application is not yet in the android marketplace we will have very low traffic. This Traffic can be easily supported by our firebase database. Nosql databases have been proven to be more effectice thatn relation databases for mobiler apllication-based workloads. (we read and assumed this is true, none of us have experience with databases.)

# Security

Our app will use a login in system to protect a users data. The user will have a password and username to login and after they do so they will have access and the ability to upgrade their tasks and other information. This is the only security measure we have in place. Firebase our most likely option for a Database has it's own security rules and matching patterns to pick best database paths and allow us to have custom conditions to access those paths. Firebase also allows Email and Password authentication for google accounts.

# Performance

Database queries are in general too expensive to make everytime information
about the task needs to be displayed for the user. Some form of caching
mechansism will be need to store in-memory copies of the task. This can build on
the existing in memory Task object that will be used in U001.
Firebase offers realtime data and static file hosting while the storage is supported by google cloud.
We want the ability to create and update tasks and also navigatiing through the Application to be fast and responsive. The only area for concern would be the detailed Statsitics page/screen which will need to be updated pretty frequently from the database. However Firestore says it updates in real time so this most likely a non-issue.

# Scalability

Cloud Firestore is not only quick to be able to setup but it also allows for high scalability. In the details of the service it says it eliminated the middle sychronixation layer betweeen an application and backend database. This allows real-time updates via the Firebase SDK. However we do not expect this application grow to get so big that we will require our own custom-made database, for security and optimization reasons, or anything like that. Hopefully, The Android marketplace, Android SDK and Firebase will have all the inhereint scalability we need. we will most likely never need any load balancing components in our code.


# Interoperability

In U004 we want to allow users to import tasks from Google Calendar. The Google
Calendar's representation of a Task will need to be wrapped. Firebase also allows for a Google Analytics Integration that we might implement in the future.

# Internationalization/Localization

When providing task statistics, the locale of the user might effect the way in
which we organize tasks into days/weeks.
We may make test unicode for multilanguage support in the future.

# Input/Output

The Android SDK provides a set of widgets to handle button/text input, so
there's not much additional code to write here. We are currently using a file to hold data so if any errors are detected they should show up on the file.

# Error Processing

In general, we would want to display non-fatal errors to the user without
shutting down, as well as logging the error information so that we can debug
common issues remotely. This follows the corrective approach to error processing. The main issue is passing exceptions across the
View-Controller boundary. Ideally, any exceptions caught in the Controller
section should be wrapped in a general exception to hide the complexity from the
View section. Then, in the UI code when an exception from the controller is
encountered it can be logged there and the UI can choose whether or not to
display a warning to the user. For now our Error detection is passive and we will change the ui as we approach a more active level. Our error messages will be very similar to the ones we see on other pomodoro apps. It will begin with "an error has occured" and then give some detail about the error under that large bolded text. THe detail will be similar to "You have tried making a task too many times" or "This date and time is invalid for a reminder".

# Fault Tolerance

For now depending on the error our system may just freeze or shutdown. In the future we would want the errors to come up with an error message and the app to continue runnning after acknowledgement of the error. If we need to disable certain parts of the app while an error is occuring, such that the app can still run, we are willing to look into that aswell.

# Architectural Feasibility

The system as described uses a well established framework, so there is no risk
of the implementation becoming too complicated as there are many examples of
similar apps throughout the app. There are many apps in the Android marketplace that use the combination of Firebase and the android SDK we are using. There are many proof-of-concept apps currently released for free and for a small price.

# Overengineering

We are using an Agile approach so for each sprint we are using the simplest thing per sprint that completely fills a user story or requirement. The only overengineering that may take place is if our requirements and acceptance tests are too lofty or if we decide to create another requirement or userstory to expand a feature that is already adequate.

# Build-vs-Buy Decisions

For building an Android app, there is a lot of FOSS tools and resources that we
can use to develop the app for free, including the Android SDK, free Figma
licensing. Firebase, while has it's paid premium options, is as a base product free and we will not be needing any of the purchasable features. This also more benefitable for us that creating our own database by scratch as none of us now how to do that.

# Reuse

There are no plans to reuse any pre-existing software. None of us have ever created anything like this system before and have no preexisting software, test cases, or other materials relating to the development of this project.

# Change Strategy

We're using Agile, so we will maintain an Agile maintain. We want changes to big features to occur as early as possible such that we can better acqauint 
ourselves with the system or software. For smaller features or changes we want to implement the other agile idea of changing them the latest we can that is still responsible to the overall project. We want to anticipate if changes are necessary in advance and depending how necessary they are implement them in a responsible fashion.
