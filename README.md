Predix Boot
==============

Welcome to Predix Boot, a Predix Backend Microservice Template.  

The predix-boot-cf project is a cloud ready microservice that explains how to make either [CXF](https://cxf.apache.org/) or [Spring](https://spring.io/) RestTemplate based REST services.  You simply change the @Path url and start writing up your service implementation.  It also has SpringBoot, Spring Profiles and Property file management all set up and ready for local development vs. cloud deployment.  It also is set up for Test Driven Development with JUnit and Mockito.  To see how it starts up, see the [Application.java](https://github.com/PredixDev/predix-boot/blob/master/predix-boot-cf/src/main/java/com/ge/predix/solsvc/boot/Application.java) in predix-boot-cf.

The predix-rest-client project provides standard GET, PUT, POST, DELETE with helpers to manage Predix OAuth Security in the cloud.  See the property files and [IOauthRestConfig.java](https://github.com/PredixDev/predix-boot/blob/master/predix-rest-client/src/main/java/com/ge/predix/solsvc/restclient/config/IOauthRestConfig.java) which allow a microservice to connect to Predix UAA (User Authentication and Authorization) servers in the cloud. All the reference app microservices use this utility to make Rest calls in the cloud.

Once you are familiar with the project and need to make your own microservice, take the predix-boot-cf project as your starting point for a Cloud Foundry Rest Microservice.  Univerally change 'predix-boot' to 'my-servicename' (using eclipse) and check in to your own repo.

>For GE Predix users, the predix-boot-dsp-cf has all the features of predix-boot-cf plus it helps bring a backwards compatible bundle from non-cloud Predix DSP implementations to the cloud simply by adding a dependency to your old rest service code to your project.  If you use Spring, you just use the @ImportResource annotation to list the Spring XML files from your old project.  In other words, it's a cloud-foundry cabable wrapper around your old Rest service that suddenly makes it deployable to the cloud. 

##To Download and Push Predix-Boot

[Prepare your environment](#preparation) and follow the steps below to get up and running on Cloud Foundry.   

1. Download a [DevBox](https://www.predix.io/catalog/other-resources/devbox.html) and launch it in Virtual Box or install the [Dependencies](#dependencies)
1. Download Predix-Boot  
  ```
  $ git clone https://github.com/PredixDev/predix-boot.git  
  
  $ cd predix-boot  
  
  $mvn clean package  
  
    note: mvn clean install may run integration tests against services you may not have set up yet
  ```
1. To load in eclipse (please use [SpringSource Tool Suite STS](https://spring.io/tools/sts/all))  
  ```
  $ mvn eclipse:clean eclipse:eclipse  
  
  File/Import/General/Existing Projects/Browse to predix-boot dir  
  
  Check the box 'Search for nested projects'  
  ```
1. Try it out locally  
  ```
  Right Click predix-boot-cf project / Run As / Spring Boot App  
  
  Visit service at http://localhost:9092 - a Spring RestTemplate  
  
  and http://localhost:9092/services/ping - a CXF Rest Endpoint  
  
  and http://localhost:9092/services?_wadl - a CXF Rest descriptor
  ```
1. Push to cloud  

    Take a look at the predix-boot-cf manifest.yml which provides properties and instructions for [pushing cloud foundry apps](https://docs.cloudfoundry.org/devguide/deploy-apps/manifest.html)
  ```
  $ cd predix-boot-cf
  
  $ cf push  
  
  visit http://(cloud-url-here)/services/ping - get the url from the output of cf push  
  ```

If you encounter a github acct/password issue then contact predixgithubaccess@ge.com.  

If you encounter a [corporate proxy issue](https://github.com/PredixDev/predix-rmd-ref-app/blob/master/docs/proxy.md#proxy), you might need to set up your env vars as required by your IT department.

If you encounter a maven or artifactory account issue, add your predix.io username and encrypted password to a maven ~/.m2/[settings.xml](docs/settings.xml) file on your laptop.  It should be setup already if in a [DevBox](https://www.predix.io/catalog/other-resources/devbox.html).

##Preparation
Reference App accesses code repos at https://github.com/PredixDev and a maven repository at https://artifactory.predix.io.

The best experience is to use a [DevBox](https://www.predix.io/catalog/other-resources/devbox.html) which has all the tools and settings pre-installed.  
* In DevBox,  
<code>
add your <a href="https://maven.apache.org/guides/mini/guide-encryption.html">encrypted</a> predix.io username and password to the /predix/.m2/<a href="https://github.com/PredixDev/predix-rmd-ref-app/blob/master/docs/settings.xml">settings.xml</a>
by replacing predixuser@ge.com with your Predix.io user and pass.
Also search for https://artifactory.predix.io/artifactory/PREDIX-EXT and set the snapshots to true</code>
</code>
  * to avoid entering user/pass several times, run this command to cache it
  ```
    git config --global credential.helper cache --timeout=7200
  ``` 
    
  >We have a second DevBox for GE employees which helps with corporate proxy settings.  

  >In your own IT environment you may need to configure the [proxy](docs/proxy.md) settings Environment variables within the VM or your own laptop.

For users wanting to install all the tools, please reference the DevBox settings, and also ensure you have the prerequisites installed from Predix.io [Getting Started](https://www.predix.io/docs/?b=#Uva9INX3) documentation and the [RMD Reference App Dependencies](https://github.com/PredixDev/predix-rmd-ref-app#dependencies).  

* For non-DevBox users,  
<code>
add your <a href="https://maven.apache.org/guides/mini/guide-encryption.html">encrypted</a> predix.io username and password to the ~/.m2/<a href="https://github.com/PredixDev/predix-rmd-ref-app/blob/master/docs/settings.xml">settings.xml</a>
by replacing predixuser@ge.com with your Predix.io user and pass.
Also create a repository for https://artifactory.predix.io/artifactory/PREDIX-EXT and set the snapshots to true</code>

  * to avoid entering user/pass several times, run this command to cache it
  ```
  git config --global credential.helper cache --timeout=7200
  ```
  
##Dependencies
|Required - latest unless specified | Note |
| ------------- | :----- |
| Java 8 | |
| GitHub Acct | logged in |
| Git | |
| Maven | https://artifactory.predix.io/artifactory/PREDIX-EXT |
| CloudFoundry ClI 6.12.2 |  https://github.com/cloudfoundry/cli/tree/v6.12.2#downloads.  There is bug on this page, so you have to manually get the URL and the add "&version=6.12.2".  For example for Windows32 it would look like this...https://cli.run.pivotal.io/stable?release=windows32&source=github&version=6.12.2 |
