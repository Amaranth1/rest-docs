# vap-rest-docs

VAP Rest Docs

## Features
* convert Swagger yaml to HTML and PDF document
* see apis-docs via browser and download pdf document from browser
* if you want add a yaml file,you should put the file into directory 'resources' and 'dist' both


## Usage
To add another new project api please add another sub folder with project name under
```
swagger/
```
Generated output will under
```
build/install/generated/  # PDF Documents
```


## Build

```
$ gradlew clean installDist run -P generatePdf=true
```


