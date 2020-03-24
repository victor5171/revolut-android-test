# Currency Converter
Currency Converter app for Android

## Technology Stack
The project was written in **Kotlin**, targeting *JVM 1.8* and using **Gradle** as the build system.

For HTTP requests **Retrofit** is being used

For JSON deserialization **Moshi** is being used.

For unit testing, **JUnit 4**, **Mockk** and **Robolectric** were chosen.

It supports also **offline** mode, the currency rates are stored using **Room**, which is also the single source of truth point.

The used architecture is **MVVM** with **DataBinding** and the project uses **Dagger2** as the DI framework.

**Ktlint** is also used to format the code.

## Code design
The app uses a **MVVM** layered architecture with the use of modules and separation of concerns. Below is the description of each module:

 - **app**: The main library, composed by the Application and MainActivity classes. It also contains the AppComponent (DI component);
 
- **feature-rateconversion**: Contains the Fragment responsible for converting the rates. This app is designed to use a Fragment only approach. Here we have also the ViewModel responsible to connect our View with the Repository. This library also contains a Dagger subcomponent and some modules;

- **repository**: Connects the ViewModel with the **api** and **repository** module. It's important to mention that repository doesn't know about the specifications of retrofit and room. It only knows about the raw definitions and interfaces. The specifications are injected in the AppComponent, present in the app library;

- **retrofit-api**: Conforms to the interfaces present in the api module, it contains the retrofit API description and a Module to be injected into the AppComponent;

- **api**: Contains the raw definition of the API and the response object, it doesn't know about any HTTP library but knows about **Moshi**, because the project uses KAPT to generate the adapter code;

- **room-persistence**: Conforms to the interfaces present in the persistence module, it contains the DAO, DTO and the Database definition. It uses Room. It's important to mention also that this library contains a Module to be injected into the AppComponent;

- **persistence**: Contains the raw definition of the persistence layer, it doesn't know about any persistence library;

- **viewmodel-extensions**: Contains a ViewModel factory to be used along with Dagger. It also contains a **MapKey**, used to declare the ViewModel inside the Feature Module.

## Side notes
- The API base URL is configured on the **BuildConfig** from the app. You can change it by editing **app/build.gradle** file;
- The versions are declared in a single gradle file, you can check them on **versions.gradle**;
- JAXB has to be included optionally because it's removed from the Java framework starting from Java9.