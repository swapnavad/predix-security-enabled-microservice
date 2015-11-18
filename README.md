Predix Boot
==============

Welcome to Predix Boot, a Predix Backend Microservice Template.  Predix Boot contains 3 sub-projects

| Project |  Note | 
| ------------- | :----- |
| predix-boot-cf | a java microservice template |
| predix-boot-dsp-cf | will be deprecated in June 2016 |
| predix-rest-client | utility used to call other secure microservices in the predix cloud

The predix-boot-cf project is a cloud-ready microservice that demonstrates how to create either [CXF](https://cxf.apache.org/) or [Spring](https://spring.io/) RestTemplate based REST services.  You simply change the @Path url and begin adding your service implementation.  It has SpringBoot, Spring Profiles and Property file management configured and ready for local development vs. cloud deployment.  It is also set up for Test Driven Development with JUnit and Mockito.  To see how it works, start with the [Application.java](https://github.com/PredixDev/predix-boot/blob/master/predix-boot-cf/src/main/java/com/ge/predix/solsvc/boot/Application.java) in predix-boot-cf.

The predix-rest-client project provides standard GET, PUT, POST, DELETE calls with helpers to manage Predix OAuth Security in the cloud.  See the property files and [IOauthRestConfig.java](https://github.com/PredixDev/predix-boot/blob/master/predix-rest-client/src/main/java/com/ge/predix/solsvc/restclient/config/IOauthRestConfig.java) that allow a microservice to connect to Predix UAA (User Authentication and Authorization) servers in the cloud. All the reference app microservices use this utility to make REST calls in the cloud.

Once you are familiar with the project and ready to make your own microservice, use the predix-boot-cf project as your starting point for a Cloud Foundry Rest Microservice. Change 'predix-boot' to 'my-servicename' everywhere (using eclipse) and check in to your own repo.

>For Predix users migrating from older predix systems, the predix-boot-dsp-cf has all the features of predix-boot-cf, and it brings a backward compatible bundle from a non-cloud Predix implementation to the cloud simply by adding a dependency to your old rest service code.  If you use Spring, you simply add the @ImportResource annotation to list the Spring XML files from your old project.  In other words, it's adding a cloud-foundry wrapper around your old Rest service, which suddenly makes it deployable to the cloud. 

##To Download and Push Predix-Boot

1. Download a [DevBox](https://www.predix.io/catalog/other-resources/devbox.html) and launch it in Virtual Box or install the [Dependencies](#dependencies)

1. [Prepare your environment](#preparation) and follow the steps below to get up and running on Cloud Foundry.   

1. Download Predix-Boot  
  ```
  $ git clone https://github.com/PredixDev/predix-boot.git  
  
  $ cd predix-boot  
  
  $ mvn clean package  
  
    note: mvn clean install may run integration tests against services you may not have set up yet
  ```
1. To load in eclipse (you may skip to 'Push to Cloud' if desired)  
  
  Vanilla [Eclipse](https://www.eclipse.org/downloads) or [Eclipse STS - Springsource Tool Suite(https://spring.io/tools/sts/all) are both supported
  ```
  $ mvn eclipse:clean eclipse:eclipse  
  
  File/Import/General/Existing Projects/Browse to predix-boot dir  
  
  Check the box 'Search for nested projects'  
  ```
1. Try it out locally  
  ```
  in Eclipse - Right Click predix-boot-cf project / Run As / Application 
  in Eclise STS - Right Click predix-boot-cf project / Run As / Spring Boot Application 
  
  Visit service at http://localhost:9092 - a Spring RestTemplate  
  
  and http://localhost:9092/services/ping - a CXF Rest Endpoint  
  
  and http://localhost:9092/services?_wadl - a CXF Rest descriptor
  ```
1. Push to cloud  

    Take a look at the [predix-boot-cf manifest.yml](predix-boot-cf/manifest.yml) which provides properties and instructions for [pushing cloud foundry apps](https://docs.cloudfoundry.org/devguide/deploy-apps/manifest.html)
  ```
  $ cd predix-boot-cf
  
  $ cf push  
  
  visit http://(cloud-url-here)/services/ping - get the url from the output of cf push  
  ```

##Troubleshooting
If you encounter a github acct/password issue then contact predixgithubaccount@ge.com.  

If you encounter a [corporate proxy issue](https://github.com/PredixDev/predix-rmd-ref-app/blob/master/docs/proxy.md#proxy), you might need to set up your env vars as required by your IT department.

If you encounter a maven or artifactory account issue, add your predix.io username and encrypted password to a maven ~/.m2/[settings.xml](docs/settings.xml) file on your laptop.  It should be setup already if in a [DevBox](https://www.predix.io/catalog/other-resources/devbox.html).

##Preparation
Predix Boot accesses code repos at https://github.com/PredixDev.

The best experience is to use a [DevBox](https://www.predix.io/catalog/other-resources/devbox.html) which has all the tools and settings pre-installed.  
* In DevBox,  
	* to avoid entering user/pass several times, run this command to cache it
  ```
  git config --replace-all --global credential.helper 'cache --timeout=7200'
  ``` 
	* OSX: To enable Copy/Paste using Left Command key.  
  ```
    On Host: Please choose VirtualBox/Preferences/Input/Virtual Machine/Host Key Combination.  
    Set to Right ⌘.  
    On VM: Choose System/Preference/Keyboard/Layouts/Layout Options/Alt/Win Key Behavior/Ctrl is mapped to Win Keys(and the usual Ctrl keys)
  ```
  
  >We have a second DevBox for GE employees which helps with corporate proxy settings.  

  >In your own IT environment you may need to configure the [proxy](docs/proxy.md) settings Environment variables within the VM or your own laptop.

* For non-DevBox users,  
	For users wanting to install all the tools, please reference the DevBox settings, and also ensure you have the prerequisites installed from Predix.io [Getting Started](https://www.predix.io/docs/?b=#Uva9INX3) documentation and the [RMD Reference App Dependencies](https://github.com/PredixDev/predix-rmd-ref-app#dependencies).  

	* add your <a href="https://maven.apache.org/guides/mini/guide-encryption.html">encrypted</a> predix.io username and password to the ~/.m2/<a href="https://github.com/PredixDev/predix-rmd-ref-app/blob/master/docs/settings.xml">settings.xml</a>
by replacing predixuser@ge.com with your Predix.io user and pass.

  * to avoid entering user/pass several times, run this command to cache it
  ```
  git config --global credential.helper cache --timeout=7200
  ```

##Dependencies
If you are not using DevBox, ensure your development environment is configured with the tools listed here.

|Required | Version | Note |
| ------------- | :----- | :----- |
| Java | 8 | |
| GitHub Acct | n/a | logged in |
| Git | latest | |
| Maven | latest | https://artifactory.predix.io/artifactory/PREDIX-EXT |
| CloudFoundry ClI | 6.12.2 | https://github.com/cloudfoundry/cli/tree/v6.12.2#downloads.  There is bug on this page, so you have to manually get the URL and the add "&version=6.12.2".  For example for Windows32 it would look like this...https://cli.run.pivotal.io/stable?release=windows32&source=github&version=6.12.2 |
