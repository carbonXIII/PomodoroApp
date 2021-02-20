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

##
  This rough sequence diagram shows the flow of messages during the use case for user story 1 (basic timer implementation). It is broken down into the same "Model-View-Presenter" containers, but also considers the user as an actor. <TODO>

# Data Design

![ER Diagram](/artifacts/images/Simple_ERD_and_explanation.PNG)

# Business Rules

You should list the assumptions, rules, and guidelines from external sources that are impacting your program design. 

See Code Complete, Chapter 3

# User Interface Design

You should have one or more user interface screens in this section. Each screen should be accompanied by an explaination of the screens purpose and how the user will interact with it. You should relate each screen to one another as the user transitions through the states of your application. You should also have a table that relates each window or component to the support using stories. 

See Code Complete, Chapter 3

# Resource Management

See Code Complete, Chapter 3

# Security

See Code Complete, Chapter 3

# Performance

See Code Complete, Chapter 3

# Scalability

See Code Complete, Chapter 3

# Interoperability

See Code Complete, Chapter 3

# Internationalization/Localization

See Code Complete, Chapter 3

# Input/Output

See Code Complete, Chapter 3

# Error Processing

See Code Complete, Chapter 3

# Fault Tolerance

See Code Complete, Chapter 3

# Architectural Feasibility

See Code Complete, Chapter 3

# Overengineering

See Code Complete, Chapter 3

# Build-vs-Buy Decisions

This section should list the third party libraries your system is using and describe what those libraries are being used for.

See Code Complete, Chapter 3

# Reuse

See Code Complete, Chapter 3

# Change Strategy

See Code Complete, Chapter 3
