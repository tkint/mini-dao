# MiniDAO [![Build Status](https://travis-ci.org/tkint/MiniDAO.svg?branch=master)](https://travis-ci.org/tkint/MiniDAO)

Librairie qui permet de lier un modèle avec une base de données de type MySQL

# Summary

* Quick Start
  * Maven dependency
  * Simple use case
* Annotations
* Configuration
* Development
* License

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

# Configuration

# Development

# License
