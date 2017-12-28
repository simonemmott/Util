# K2 Utilities
The K2 Utilities project contains various utility classes and packages.

The following utilities are included in this project

| Utility | Description |
|---------|-------------|
|Version  | The Version interface defines a simple version implementation comprising a major, minor and point version number.

Javadoc documentation of this project can be found [here](https://simonemmott.github.io/Util/index.html)

### License

[GNU GENERAL PUBLIC LICENSE v3](http://fsf.org/)

## Version Interface

The version interface defines a simple 3 part version number comprising a major, minor and point version number.

The interface provides:

1. `create(...)` methods to instantiate a concrete implementation of the interface. 
1. `increment(...)` methods to increment the major, minor or point version number.
1. `major()`, `minor()` and `point()` methods to retrieve the respective version numbers

The standard implementation of the Version interface overrides the `toString()` method to provide a sensible representation of the version e.g. "v1.0.0". It also override the `hashCode()` and `equals(Object)` methods for equality based on the major, minor and point version numbers.

### Version Examples

The following code produces a default version with the number 0.0.0.
```java
Version example = Version.create();

System.out.println("The default version is: "+example);
```
Which produces the following output
```text
The default version is: v0.0.0
```

The following code produces a version with the specified version numbering.
```java
Version example = Version.create(1,2,3);

System.out.println("This is a specific version: "+example);
```
Which produces the following output
```text
This is a specific version: v1.2.3
```

The following code increments a versions point, minor then major version numbers.
```java
Version example = Version.create(1,2,3);

System.out.println("This is the initial version: "+example);

example.increment(Increment.POINT);

System.out.println("The point version has been incremented: "+example);

example.increment(Increment.MINOR);

System.out.println("The minor version has been incremented: "+example);

example.increment(Increment.MAJOR);

System.out.println("The major version has been incremented: "+example);
```
Which produces the following output
```text
This is the initial version: v1.2.3
The point version has been incremented: v1.2.4
The minor version has been incremented: v1.3.0
The major version has been incremented: v2.0.0
```
The following code checks whether one version is before or after another version.
```java
Version example = Version.create(1,2,3);
Version v114 = Version.create(1,1,4);
Version v131 = Version.create(1,3,1);

if (example.includes(v114)) System.out.println("Version "+example+" contains version "+v114);

if (!example.includes(v131)) System.out.println("Version "+example+" does not contain version "+v131);
```
Which produces the following output.
```text
Version v1.2.3 contains version v1.1.4
Version v1.2.3 does not contain version v1.3.1
```





