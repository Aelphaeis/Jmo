# Jmo Library:
[![Build Status](https://travis-ci.org/Aelphaeis/Jmo.svg?branch=2.1)](https://travis-ci.org/Aelphaeis/Jmo)
 
This is a grouping of utilities created and used by Joseph Morain to make magic occur.

To use Jmo add the following to repositories element
```
<repositories>
    ...
    <repository>
         <id>maven-s3-release-repo</id>
        <name>aws release repository</name>
        <url>https://s3.amazonaws.com/repo.maven/release</url>
    </repository>
    <repository>
        <id>maven-s3-snapshot-repo</id>
        <name>aws snapshot repository</name>
        <url>https://s3.amazonaws.com/repo.maven/snapshots</url>
    </repository>
</repositories>
<dependencies>
    ...
    <dependency>
        <groupId>JmoUtilities</groupId>
        <artifactId>JmoUtilities</artifactId>
        <version>@version@</version>
    </dependency>
</dependencies>
```