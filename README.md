# K2 Utilities - v0.2.0
The K2 Utilities project contains various utility classes and packages.

The following utilities are included in this project

| Utility     | Description |
|-------------|-------------|
| Version     | The Version interface defines a simple version implementation comprising a major, minor and point version number.|
| Identity    | The Identity package includes interfaces for identifying objects and a utility to extract id values from objects. |
| BooleanUtil | The BoooleanUtil provides static methods for handling Boolean values and converting them to other base objects. |
| ClassUtil   | The ClassUtil provides static methods for dealing with and finding classes. |
| DateUtil    | The DateUtil provides static methods for handling Date values and converting them to other base objects. |
| DoubleUtil  | The DoubleUtil provides static methods for handling Double values and converting them to other base objects. |
| FileUtil    | The FileUtil provides static methods for finding, deleting and locking files. |
| FloatUtil   | The FloatUtil provides static methods for handling Float values and converting them to other base objects. |
| IntegerUtil | The IntegerUtil provides static methods for handling Integer values and converting them to other base objects. |
| LongUtil    | The LongUtil provides static methods for handling Long values and converting them to other base objects. |
| StringUtil  | The StringUtil provides static methods for handling Double values and converting them to other base objects. |
| UtilsLogger | The UtilsLogger provides static methods for dealing with logging from the utility classes. |

Javadoc documentation of this project can be found [here](https://simonemmott.github.io/Util/index.html)

In most cases the utility classes log exceptional conditions to the utilities logger and carry on. Where error conditions are identified the unchecked `UtilityError` or one of its subclasses is thrown to limit development of unnecessarily defensive code.

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






## Identity Package
The identity package provides 2 interfaces for identifying objects.

| Interface    | Description |
|--------------|-------------|
| `Id`         | The Id interface defines methods to get and set object primary key field, typically named id. |
| `Identified` | The Identified interface defines a method that will return a human readable string to identify an object. The returned value is not guaranteed to uniquely identify the object |

The identify package includes the IdentityUtil provides a static method to extract the value of the Serializable primary key field of an object

### Id Interface

The Id interface defines the following methods. It is defined with two generic types `<T,K>` where T is the type of the class implementing the interface and K is the `Serializable` type of the primary key value.

| Method     | Description |
|------------|-------------|
| K getId()  | This method gets the Serializable value of the objects primary key. |
| T setId(K) | This method sets the Serializable value of the object primary key and returns the object implementing the interface for method chaining. |

The code below shows a very simple class implementing the Id interface with a String for it's primary key.
```java
public class NoSnap implements Id<NoSnap, String> {

	public String aw;

	@Override
	public String getId() { return aw; }

	@Override
	public NoSnap setId(String key) {
		aw = key;
		return this;
	}

}
```

### Identified Interface

The identified interface defines a single method to return a String value that identifies an object in a human readable format.

| Method                  | Description |
|-------------------------|-------------|
| `String getIdentity() ` | This method returns a human readable identity for an object. This method is not guaranteed to provide a unique value for the object. |

### IdentityUtil

The IdentityUtil provides a static method to extract the Serializable value of an objects primary key.

| Method                       | Description |
|------------------------------|-------------|
| `Serializable getId(Object)` | This method gets the serializable id value from an object. |

#### getId(...)

The `getId(...)` method extracts the primary key value from an object. The method performs checks in the following order.

1. If the object implements the `Id` interface then the `get()` method of the interface is used to extract and return the primary key value.
1. If the object is annotated with `javax.persistence.Id` then the value of the `@Id` annotated field is extracted and returned.
1. If the object contains a field named `id` with a `Serializable` type the value of that field is extracted and returned.
1. The objects simple class name is returned

## BooleanUtil

















