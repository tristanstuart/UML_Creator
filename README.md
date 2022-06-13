# UML_Creator
## Summary
This project was created for use during in an Object Oriented Programming course. We were expected to create UML documentation for our homework assignments, so I created this generator to automate that process. This program parses basic .java files and generated simple UML diagrams for them. The rendering of each class is done custom with java's Graphics class.

## Sample
A sample of the output can be found in the ```src/UML_Output``` directory.

## Unsupported Features
As the usecase for this program was only intended for very basic Java programs, many of the feature of the language are unsupported. The list of unsupported feature includes (but is not limited to): 
* packages
* package level visibility for classes, methods, and variables
* class level relationships, including extending, implementing, and all of the UML relationships (association, dependency, etc.)
* enums and interfaces

Since this project is no longer something I use or maintain, these and any other features will remain unsupported.

## Running the project
Download the project and compile the code in the src directory. The project takes the directory of the files to be scanned as the only command line argument. 
