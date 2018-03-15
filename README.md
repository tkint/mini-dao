<table>
 <tr style="text-align: center">
  <td>License</td>
  <td>Master</td>
  <td>Develop</td>
 </tr>
 <tr>
  <td>
   <a href="https://github.com/tkint/MiniDAO/blob/master/LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg"/>
   </a>
  </td>
  <td>
   <a href="https://travis-ci.org/tkint/MiniDAO">
    <img src="https://travis-ci.org/tkint/MiniDAO.svg?branch=master"/>
   </a>
  </td>
  <td>
   <a href="https://travis-ci.org/tkint/MiniDAO">
    <img src="https://travis-ci.org/tkint/MiniDAO.svg?branch=develop"/>
   </a>
  </td>
 </tr>
</table>

# MiniDAO 

Java library for quick connection between java model and database tables.

# Summary

* [Quick Start](#quick-start)
  * [Maven dependency](#maven-dependency)
  * [Simple use case](#simple-use-case)
* [Installation](#installation)
  * [Through git](#through-git)
  * [Download and install ojdbc8](#download-and-install-ojdbc8)
  * [Install ojdbc8 during maven build](#install-ojdbc8-during-maven-build)
* [Annotations](#annotations)
* [Configuration](#configuration)
* [Development](#development)
* [License](#license)

# Quick Start

## Maven dependency

Add those lines in your pom.xml:
```xml
<dependencies>
    <!-- Other dependencies -->
    <dependency>
        <groupId>com.thomaskint</groupId>
        <artifactId>minidao</artifactId>
        <version>{version}</version>
    </dependency>
</dependencies>
```

## Simple use case

Start by creating an MDConnectionConfig object with login informations to your database:
```java
MDConnectionConfig connectionConfig = new MDConnectionConfig(MDDriver.MYSQL, "{url}", "{port}", "{username}", "{password}", "{database}");
```

For example:
```java
MDConnectionConfig connectionConfig = new MDConnectionConfig(MDDriver.MYSQL, "127.0.0.1", "3306", "minidao", "password", "minidao");
```

Then, set the library config:
```java
MiniDAO.config(connectionConfig);
```

Create an object reflecting one of your database table:
```java
@MDEntity(name = "user")
public class User { 
    
    @MDId
    @MDField(name = "id_user", params = SELECT)
    public BigDecimal id_user;
    
    @MDField(name = "email")
    public String email;
    
    @MDField(name = "login")
    public String login;
    
    @MDField(name = "password")
    public String password;
}
```

Now, you can retrieve data from your database:
```java
public List<User> getUsers() {
    List<User> users = null;
    try {
        users = MiniDAO.getEntities(User.class);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return users;
}
```

# Installation

## Through git

Clone project:
```
git clone https://github.com/tkint/MiniDAO.git
```

If ojdbc8 (12.2.0.1) is not already installed in your local maven repository will need to [create an oracle account](https://profile.oracle.com/myprofile/account/create-account.jspx).
Then you can either [download and install ojdbc8](#download-and-install-ojdbc8)
or [install ojdbc8 during maven build](#install-ojdbc8-during-maven-build)

Now, install MiniDAO in your local maven repository:
```
mvn clean install
```

## Download and install ojdbc8

First of all, you should [create an oracle account]((https://profile.oracle.com/myprofile/account/create-account.jspx))

Go to [oracle website](http://www.oracle.com/technetwork/database/features/jdbc/jdbc-ucp-122-3110062.html),
accept OTN License Agreement and download ojdbc8.jar.

Now, go to your download directory, open a bash and run the following command:
```
mvn install:install-file -Dfile=ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=12.2.0.1 -Dpackaging=jar
```

It will install ojdbc8 on your local maven repository, making it available to you and MiniDAO. 

## Install ojdbc8 during maven build

First of all, you should [create an oracle account]((https://profile.oracle.com/myprofile/account/create-account.jspx))

Next, you have to [accept the license agreement](https://www.oracle.com/webapps/maven/register/license.html)

In settings.xml, there are fields for credentials:
```xml
<username>{oracle_username}</username>
<password>{oracle_password}</password>
```

Change {oracle_username} and {oracle_password} respectively by your oracle username and password.

Now, next time you want to install MiniDAO in your local maven repository, you will have to run the following command:
```
mvn clean install -s settings.xml
```

It will automatically install MiniDAO and ojdbc8 on your local maven repository based on the oracle credentials you have given.

# Annotations

_In progress_

# Configuration

_In progress_

# Development

_In progress_

# License

MiniDAO is developed and distributed under the terms of the [MIT License](https://github.com/tkint/MiniDAO/blob/master/LICENSE)
