[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/tkint/MiniDAO/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/tkint/MiniDAO.svg?branch=master)](https://travis-ci.org/tkint/MiniDAO)

# MiniDAO 

Java library for quick connection between java model and database tables.

# Summary


* [Quick Start](#quick-start)
  * [Maven dependency](#maven-dependency)
  * [Simple use case](#simple-use-case)
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
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

# Simple use case

Start by creating an MDConnectionConfig object with login informations to your database:
```java
MDConnectionConfig mdConnectionConfig = new MDConnectionConfig(MDDriver.MYSQL, "{url}", "{port}", "{username}", "{password}", "{database}");
```

For example:
```java
MDConnectionConfig mdConnectionConfig = new MDConnectionConfig(MDDriver.MYSQL, "127.0.0.1", "3306", "minidao", "password", "minidao");
```

Then, set the library config:
```java
MiniDAO.config(mdConnectionConfig);
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

# Annotations

_In progress_

# Configuration

_In progress_

# Development

_In progress_

# License

_Not yet_
