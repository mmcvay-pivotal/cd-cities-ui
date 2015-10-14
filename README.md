# cd-cities-ui
Cities-ui Microservice part of the continuous delivery demo for the NEMEA FE team.

# Introduction

This demo shows how Pivotal CF supports Continuous Delivery as the platform to run and execute the artifacts produced by a build pipeline on the different environments required (Test, QA, Prod, DR, etc).

The demo does not use continuous pipeline tools, or artifact repositories, which should be used in any CD pipeline. This is to reduce the amount of infrastructure required for the demo. In order to build and manage the pipeline, the demo uses Jenkins.

## What does the demo show?

The demo shows that a developer can commit and push an application to a code repository (Github in our case) which will trigger the build pipeline. The pipeline, or actually the pipeline tool, then liases with Pivotal CF to deploy and execute the application in two different CF spaces (QA and Prod).

What features does it show: 
1. Jenkins integration
This is through cli that is installed in the PCF Jenkins tile, rather than the jenkins CF plugin.
2. Blue-green deployments
Both when deploying to QA and Prod. No downtime of the service.
3. Automated promotion of same artifact through the different environments.

# Setup the demo

The demo is already setup so, no need to do these steps, unless you want to re-create the demo in a different environment.

1. Create a user, org and 3 spaces (dev, qa and prod) in your PCF instance.
2. Create a "citiesDB" MySQL service on all space.
3. Deploy the cd-cities-service app on all space
Its not a requirement for this demo to install on all space, just one space would suffice. However, for future demos, we may require one in each space.
4. Create a User-Defined service called `citiesService` in each space. Something like this:
cf cups citiesService -p '{"tag":"cities","uri":"http://<<url of cd-cities-service>>"}' 
5. Install the Jenkins tile in OpsManager and configure it as below:

- Login to your Jenkins instance deployed by Ops Manager.  This is located at https://pivotal-cloudbees.<YOUR SYSTEM DOMAIN>. Use the `Admin` user under `UAA`.

- Navigate to `Manage Jenkins` > `Configure System`

- Add the JDK 1.8 installation under the `JDK` section
+
* `Add Installer` > `Install from java.sun.com`
* Agree to the license
* Provide your Oracle login information - no longer required to put in credentials that work!
* Ensure `Install automatically` is selected
+

- Add the latest stable Gradle under the `Gradle section`
+
* Ensure `Install automatically` is selected
* `Install from Gradle.org`

- Click `Apply` then `Save`

- Navigate to `Manage Jenkins` > `Manage Plugins` > `Available`

- Ensure the following plugins are installed:
+
* Gradle (should be installed)
* Mask Passwords
* Extensible Choice Parameter Plugin
* Parameterized Remote Trigger Plugin
* Github Plugin
* build-name-setter
* Build Pipeline plugin
+

- Tell Jenkins to restart after plugin install

6. Create the build jobs.
Rather than creating the jobs from scratch, we will be importing the jobs, and then modifying them to suit your needs.

**NEED COMPLETE THIS SECTION** In the meantime, ask me for the backup of the job definitions and how to import them. 

# Running the demo

## Preparation for the demo:
1. Clone repository
Clone the project repository from `https://github.com/Pivotal-Field-Engineering/cd-cities-ui.git`

If you have already cloned the repository, do a pull or a checkout of the index.html: `git pull`.

Obviously, you can fork the repo, and use your repo as the source of code, but if you do that, then you have to ensure to point the jenkins build job at your repo.

2. Check the apps are running on the QA and prod spaces.

3. If app is not running, then check above to see what steps are required to get app running
Maybe you need to deploy the ui app or the service app as well?

4. Ensure correct version of app deployed.
The correct version of the app, should **not** have a form to search for a particular city. If the search form is visible, go to the `src/main/resources/templates/index.html` and comment "include form" line, ie:
```
<!--
	<div th:include="cities :: form"></div>
-->
```
Do a `git commit -m "before demo"` and then a `git push`. Ensure that your modified version of index.html gets pushed.
After a couple of minutes, if you try the apps again, the search form should not be visible.
 
## Showing the demo
1. Explain the environment and pipeline
- Spaces:
	- dev space for dev to play with.
	- QA space for integration testing
	- Prod space for production
- jobs:
	1. cd-cities-ui:
	Triggered by a change in github. Builds the source code, packages in jar file and stores that artifact in jenkins. Could put it in Artifactory or other service if available. Could potentially run unit tests too.
	2. cd-cities-deploy-qa:
	Triggered by a successful finish of previous job. Pushes to QA space doing a blue-green deployment and tests the app is running with a curl command - expects HTTP 200 result. Could run more elaborate integration tests.
	3. cd-cities-deploy-prod:
	Triggered by a successful finish of previous job. Pushes to Prod space doing a blue-green deployment and tests the app is running with a curl command - expects HTTP 200 result.

Split over 3 build jobs, to maybe integrate with a CD tool like go.cd in the future.

2. Show app running on Prod space.
- Mention that it lists all cities in US and their post codes 
(Due to space and time, we did not load all cities in US!)

- Mention that a developer has added that functionality to search the cities.

3. Modify the the `index.html` to uncomment the form.

4. Git commit and push.

5. login to jenkins UI and watch the jobs running.

6. Show new version of the app.
Show app in prod and QA spaces now with form for searching the city.


## after the demo:

**MAKE SURE TO COMMENT THE FORM IN INDEX.HTML AND PUSH THE CHANGES TO GITHUB** 