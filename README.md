<table>
 <tr style="text-align: center">
  <td>License</td>
  <td>Master</td>
  <td>Develop</td>
  <td>Version</td>
 </tr>
 <tr>
  <td>
   <a href="https://gitlab.com/tkint/MiniDAO/blob/master/LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg"/>
   </a>
  </td>
  <td>
   <a href="https://gitlab.com/tkint/MiniDAO/commits/master">
    <img alt="master status" src="https://gitlab.com/tkint/MiniDAO/badges/master/pipeline.svg"/>
   </a>
  </td>
  <td>
   <a href="https://gitlab.com/tkint/MiniDAO/commits/develop">
    <img alt="develop status" src="https://gitlab.com/tkint/MiniDAO/badges/develop/pipeline.svg"/>
   </a>
  </td>
  <td>
   <a href='https://bintray.com/tkint/maven/MiniDAO/_latestVersion'>
    <img src='https://api.bintray.com/packages/tkint/maven/MiniDAO/images/download.svg'>
   </a>
  </td>
 </tr>
</table>

# MiniDAO 

MiniDAO is a java library created to form a quick connection between java model and database.

Its purpose is to simplify requests, but it is not made for creating a database automatically.
I will maybe add a feature to execute scripts on initialization, or to expose a maven job,
a bit like Artisan and Eloquent in PHP.

If you want to participate, I'm fully open to it.
If you want a new feature incorporated or encounter a bug, don't hesitate to fill an issue.

# Summary

* [Quick Start](#quick-start)
  * [Maven dependency](#maven-dependency)
  * [Simple use case](#simple-use-case)
* [Installation](#installation)
  * [Through git](#through-git)
  * [Download and install ojdbc8](#download-and-install-ojdbc8)
  * [Install ojdbc8 during maven build](#install-ojdbc8-during-maven-build)
* [Annotations](#annotations)
  * [@MDEntity](#mdentity)
  * [@MDField](#mdfield)
  * [@MDId](#mdid)
  * [@MDInheritLink](#mdinheritlink)
  * [@MDManyToOne](#mdmanytoone)
  * [@MDOneToMany](#mdonetomany)
* [Configuration](#configuration)
* [Main class : MiniDAO](#main-class--minidao)
  * [read() - MDRead](#read---mdread)
  * [create() - MDCreate](#create---mdcreate)
  * [update() - MDUpdate](#update---mdupdate)
  * [delete() - MDDelete](#delete---mddelete)
* [Query Builders](#query-builders)
  * [MDSelectBuilder](#mdselectbuilder)
  * [MDInsertBuilder](#mdinsertbuilder)
  * [MDUpdateBuilder](#mdupdatebuilder)
  * [MDDeleteBuilder](#mddeletebuilder)
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
MiniDAO miniDAO = new MiniDAO(connectionConfig);
```

Create an object reflecting one of your database table:
```java
@MDEntity(tableName = "user")
public class User {
    
    @MDId
    @MDField(fieldName = "id", allowedSQLActions = SELECT)
    public BigDecimal id;
    
    @MDField(fieldName = "email")
    public String email;
    
    @MDField(fieldName = "login")
    public String login;
    
    @MDField(fieldName = "password")
    public String password;
}
```

Now, you can retrieve data from your database:
```java
public List<User> getUsers() {
    List<User> users = null;
    try {
        users = miniDAO.read().getEntities(User.class);
    } catch (MDException e) {
        e.printStackTrace();
    }
    return users;
}
```

# Installation

## Through git

Clone project:
```
git clone https://gitlab.com/tkint/MiniDAO.git
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

#### @MDEntity

Define class as an object of database
- constraints:
  - class must be public
  - must have an empty constructor if custom ones are defined
- properties:
    - tableName: table name in database
    - allowedSQLActions: authorized verbs on this entity
      - default: { SELECT, INSERT, UPDATE, DELETE }
- examples:
```java
@MDEntity(tableName = "user")
public class User {
}

@MDEntity(tableName = "user_token", allowedSQLActions = { SELECT, INSERT })
public class UserToken {
}
```

#### @MDField

Define field as a column of the entity table
- constraints:
  - field must be public
- properties:
    - fieldName: field name in database
    - allowedSQLActions: authorized verbs on this field
      - default: { SELECT, INSERT, UPDATE, DELETE }
- examples:
```java
@MDEntity(tableName = "user")
public class User {

    @MDField(fieldName = "id", allowedSQLActions = SELECT)
    public BigDecimal id;
    
    @MDField(fieldName = "email")
    public String email;
}
```

#### @MDId

Define field as the primary key of the entity table
- constraints:
  - must be on a field annotated with MDField
- examples:
```java
@MDEntity(tableName = "user")
public class User {
    
    @MDId
    @MDField(fieldName = "id", allowedSQLActions = SELECT)
    public BigDecimal id;
    
    @MDField(fieldName = "email")
    public String email;
}
```

#### @MDInheritLink

Define field as a link of the entity table to the parent class table
- constraints:
  - must be on a field annotated with MDField
  - MDField must be a foreign key referencing the parent entity primary key
- examples:
```java
@MDEntity(tableName = "user")
public class User {
    
    @MDId
    @MDField(fieldName = "id", allowedSQLActions = SELECT)
    public BigDecimal id;
    
    @MDField(fieldName = "email")
    public String email;
}

@MDEntity(tableName = "player")
public class Player extends User {
    
	@MDId
	@MDField(fieldName = "id_player")
	public BigDecimal idPlayer;
    
	@MDInheritLink
	@MDField(fieldName = "id_user")
	public BigDecimal idUser;

	@MDField(fieldName = "pseudo")
	public String pseudo;
}
```

NB: This feature is not really useful, I made it as a Proof of Concept for linking entities between them before making @MDManyToOne and @MDOneToMany

#### @MDManyToOne

Define field as a foreign key link to the referenced entity
- constraints:
  - field must be public
  - field must be an MDEntity object reference
  - field must <b>NOT</b> be annotated with MDField
- properties:
    - fieldName: field name of the link key in database
    - targetFieldName: field name of the target key
    - target: entity to link
    - loadPolicy:
      - LAZY: avoid loading of linked entity when current one is retrieved
      - HEAVY: force loading of linked entity when current one is retrieved
- examples:
```java
@MDEntity(tableName = "user")
public class User {
    
    @MDId
    @MDField(fieldName = "id", allowedSQLActions = SELECT)
    public BigDecimal id;
    
    @MDField(fieldName = "email")
    public String email;
}

@MDEntity(tableName = "user_token")
public class UserToken {
    
    @MDManyToOne(fieldName = "id_user", targetFieldName = "id", target = User.class, loadPolicy = HEAVY)
    public User user;
    
    @MDField(fieldName = "token")
    public String token;
}
```


#### @MDOneToMany

Define field as a foreign key link to the referenced entity
- constraints:
  - field must be public
  - field must be a collection of MDEntity object
  - field must <b>NOT</b> be annotated with MDField
- properties:
    - fieldName: field name of the link key in database
    - targetFieldName: field name of the target foreign key
    - target: entity to link
    - loadPolicy:
      - LAZY: avoid loading of linked entity when current one is retrieved
      - HEAVY: force loading of linked entity when current one is retrieved
- examples:
```java
@MDEntity(tableName = "user")
public class User {
    
    @MDId
    @MDField(fieldName = "id", allowedSQLActions = SELECT)
    public BigDecimal id;
    
    @MDField(fieldName = "email")
    public String email;

    @MDOneToMany(fieldName = "id", targetFieldName = "id_user", target = UserToken.class, loadPolicy = HEAVY)
    public List<UserToken> userTokens;
}

@MDEntity(tableName = "user_token")
public class UserToken {
    
    @MDManyToOne(fieldName = "id_user", targetFieldName = "id", target = User.class, loadPolicy = HEAVY)
    public User user;
    
    @MDField(fieldName = "token")
    public String token;
}
```

# Configuration

The MDConnectionConfig object allow you to setup a connection with a database. You can 
instantiate multiple MiniDAO connections and MDConnectionConfig.

This object has the following properties:
 - MDDriver driver:<br/>
    The driver is used by MiniDAO to build the uri and instantiate Connection.<br/>
    For now, supported drivers are  MYSQL, ORACLE and H2 (file only).
    In the future, MiniDAO will be able to do specific requests based on this value.
 - String url:<br/>
    Ip address or domain name of the database server
 - String port:<br/>
    Port used to access database server instance
 - String login:<br/>
    Login of the user
 - String password:<br/>
    Password of the user
 - String instance:<br/>
    Name of database

# Main class : MiniDAO

#### read() - MDRead

_In progress_

#### create() - MDCreate

_In progress_

#### update() - MDUpdate

_In progress_

#### delete() - MDDelete

_In progress_

# Query Builders

When the default methods are not enough to get what you want, you
can use Query Builders to make your own queries and execute them.

#### MDSelectBuilder

```java
public class UserRetriever {
    	
    private MiniDAO miniDAO;
    
    public UserRetriever(MiniDAO miniDAO) {
        this.miniDAO = miniDAO;
    }
	
    public List<User> getUsersByFirstName(String firstName) throws MDException {
        MDSelectBuilder selectBuilder = new MDSelectBuilder();
        selectBuilder.select().from(User.class).where("first_name", EQUAL, firstName);
        String query = selectBuilder.build();
        
        ResultSet resultSet;
        List<User> users;
        try {
        	resultSet = miniDAO.executeQuery(query);
        	users = miniDAO.mapResultSetToEntities(resultSet, User.class);
        } catch (MDException exception) {
        	throw exception;
        }
        
        return users;
    }
}
```

#### MDInsertBuilder

_In progress_

#### MDUpdateBuilder

_In progress_

#### MDDeleteBuilder

_In progress_

# License

MiniDAO is developed and distributed under the terms of the [MIT License](https://gitlab.com/tkint/MiniDAO/blob/master/LICENSE)
