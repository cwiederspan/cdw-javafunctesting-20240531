# cdw-javafunctesting-20240531
An Azure Function for testing performance of Java-based applications.

```bash

npm i -g azure-functions-core-tools@4 --unsafe-perm true

// Follow instructions from this URL:
// https://learn.microsoft.com/en-us/azure/azure-functions/create-first-function-cli-java?tabs=linux%2Cbash%2Cazure-cli%2Cbrowser

mvn archetype:generate -DarchetypeGroupId=com.microsoft.azure -DarchetypeArtifactId=azure-functions-archetype -DjavaVersion=11

// groupId: com.chwieder
// artifact: chwieder-functions

cd chwieder-functions

mvn clean package

mvn azure-functions:run

mvn azure-functions:deploy

```