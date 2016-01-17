# Building this project
Generally there are two ways for building Android projects. Either with Eclipse
and the AVD plugin or independently from an IDE with Ant-scripts form the command line.

# Building this project with Ant
1. Download and install the Android SDK according the official documentation:
   http://developer.android.com/sdk/index.html

2. Add the "tools" and "platform-tools" directories from the SDK to the executable path of your system.

3. Run the following commands inside the root directory of this (AAT) project.
     android list targets
     android update project --name AAT --path . --target X
   Where X is an ID listed by the first command
   This will create the android ant scripts.

4. build the project with the commands:
     ant clean
     ant debug
   If you are lucky this will create "bin/aat-debug.apk".


# Building this project with Eclipse
1. Install [Eclipse](http://eclipse.org)

2. Download and install the Android SDK according the official documentation:
   http://developer.android.com/sdk/index.html
   Also install the AVD plugin as described there.


3. In 'Window->Preferences->Compiler' set JDK Compliance to 1.6

4. Import AAT:
   - In Eclipse select 'File->import->existing project'
     - Provide the path to the projects root directory.
     - Select 'copy projects into workspace' and then select 'finish'
    - if there are errors:
      select 'Android Tools' -> 'Fix project properties' in the project's context menu
    - if there still are errors:
      consult stack overflow or send me an email.
