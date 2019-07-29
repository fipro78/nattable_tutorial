# NatTable Getting Started Tutorial

This repository contains the sources for the NatTable Getting Started Tutorial.
It is based on the [NatTable Getting Started Tutorial](https://www.vogella.com/tutorials/NatTable/article.html).

##Preparations

To get started with the tutorial fast
- clone this repository
- switch to the *start* branch
- open an existing Eclipse IDE in a new workspace
- import the projects **_com.vogella.nattable_** and **_com.vogella.nattable.target_**
- open the file _com.vogella.nattable.target.target_ and **Update** the nattable software site to get the latest SNAPSHOT version
- Select _Set as Active Target Platform_

Alternatively follow these steps to prepare the tutorial manually:

1. If not done yet, download and install an Eclipse IDE: https://www.eclipse.org/downloads/packages/
2. Open the IDE and create a new workspace
3. Create a target definition similar to the exercise in the [online tutorial](https://www.vogella.com/tutorials/NatTable/article.html#exercise_nattable_install_target_def)
   Use the following update site URLs to get the latest framework versions:
   
   - Eclipse release update site: http://download.eclipse.org/releases/2019-06 (Eclipse RCP SDK, Eclipse Platform Launcher Executables, Note: Remember to disable "Group by Category" to find the specified elements)
   - NatTable Latest SNAPSHOT: https://download.eclipse.org/nattable/snapshots/latest/repository (All)
   - vogella sample data model: https://dl.bintray.com/vogellacompany/codeexamples-javadatamodel/updatesite/1.4.0/ (All)
   
4. Create and prepare an Eclipse 4 RCP application
   This will serve as a skeleton to integrate and verify the exercises.
   
   - Create the Eclipse 4 RCP project
   - Configure dependencies in MANIFEST (com.vogella.model, com.vogella.model.service, org.eclipse.nebula.widgets.nattable.core)
   - Configure the product (set product ID to com.vogella.nattable.product, and add above plugins in the Contents section)
   - Locate the PartStack in the Application Model and remove the Sample Part. The NatTable exercises will be added to this PartStack.
   
   The more detailed steps can be followed in the [online tutorial](https://www.vogella.com/tutorials/NatTable/article.html#exercise_nattable_data)
   
## NatTable Examples Application

Download and install the [NatTable examples application](https://www.eclipse.org/nattable/documentation.php?page=examples_application).
This way you can get code examples that help to solve tutorial exercices and shows the capabilities of NatTable beyond the tutorial.
