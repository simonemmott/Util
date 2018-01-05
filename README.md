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

| Method                                           | Description |
|--------------------------------------------------|-------------|
| `Serializable getId(Object)`                     | This method gets the serializable id value from an object. |
| `Serializable getId(Object, Serializable)`       | This method gets the serializable id value from an object. If a suitable id value cannot be found the given serializable value is returned |
| `String getIdentity(Object, String)` | This method gets the string identity of an object. If the object doesn't implement the Identified interface then a the value of a field annotated with the `@Identity` annotation is returned. If no suitable identity value can be obtained then the objects id as a string is returned. If no id value exists then the given default value is used |

#### getId(...)

The `getId(...)` method extracts the primary key value from an object. The method performs checks in the following order.

1. If the object implements the `Id` interface then the `get()` method of the interface is used to extract and return the primary key value.
1. If the object is annotated with `javax.persistence.Id` then the value of the `@Id` annotated field is extracted and returned.
1. If the object contains a field named `id` with a `Serializable` type the value of that field is extracted and returned.
1. The objects simple class name is returned

## BooleanUtil

The BooleanUtil provides static methods for converting Boolean values into other basic data types in a consistent manner. It also provides a static method to convert objects to boolean values. This method supports the basic data type objects of String, Date, Long etc and can be extended by registering TypeConverter instances with the BooleanUtil.

### Conversion defaults

The conversion of Boolean values to other data types is supported through the definition of several default values.

The table below shows the default values used by the BooleanUtil to convert boolean values to other data types.

| Alias        | Default | Description |
|--------------|---------|-------------|
| FALSE_STRING | false   | The string value to use to represent false boolean values |
| TRUE_STRING  | true    | The string value to use to represent true boolean values |
| FALSE_INT    | 0       | The integer value to use to represent false numerical values |
| TRUE_INT     | 1       | The integer value to use to represent true boolean values |

These defaults can be extracted from or set in the BooleanUtil using the following methods

| Method                       | Description |
|------------------------------|-------------|
| `void trueAsString(String)`  | Set the default String for `true` |
| `String trueAsString()`      | Get the default String for `true` |
| `void falseAsString(String)` | Set the default String for `false` |
| `String falseAsString()`     | Get the default String for `false` |
| `void trueAsString(int)`     | Set the default integer for `true` |
| `int trueAsString()`         | Get the default integer for `true` |
| `void falseAsString(int)`    | Set the default integer for `false` |
| `int falseAsString()`        | Get the default integer for `false` |

The initial values for these defaults can be set using the method `restoreDefaults()`

### Conversion Methods

The BooleanUtil offers the following static methods to convert Boolean values to other types.

| Method                       | Description |
|------------------------------|-------------|
| `Integer toInteger(Boolean)` | Converts the given Boolean value into an Integer using the current defaults |
| `Long toLong(Boolean)`       | Converts the given Boolean value into a long using the current defaults |
| `Float toFloat(Boolean)`     | Converts the given Boolean value into a Float using the current defaults |
| `Double toDouble(Boolean)`   | Converts the given Boolean value into a Double using the current defaults |
| `Date toDate(Boolean)`       | Converts the given Boolean value into a Date using the current defaults. The date that represents a false value is the Date created for the false value as integer and the date that represents a for a true value is the current date. This is consistent with the idea that 0 is false and any other number is true |
| `String toString(Boolean)`   | Converts the given Boolean value into a String using the current defaults |

The BooleanUtil offers the following static method to convert Objects to Boolean values.

| Method                       | Description |
|------------------------------|-------------|
| `Boolean toBoolean(Object)` | Converts the given Object into a Boolean using the corresponding *Util static classes where they exist. Other types are converted into boolean values through registering TypeConverter instances with the BooleanUtil with the method `registerTypeConverter(...)`. See below for details on creating and registering TypeConverters with static utilities. |

### Random Methods

The BooleanUtil offers the following static method to create random Boolean values

| Method             | Description |
|--------------------|-------------|
| `Boolean random()` | Creates a random boolean value |

### Handling Nulls

The BooleanUtil offers the following static method to replace a null Boolean value with an alternative Boolean value

| Method                          | Description |
|---------------------------------|-------------|
| `Boolean nvl(Boolean, Boolean)` | Returns the first Boolean value if it is not null otherwise the second Boolean value is returned |

## IntgerUtil

The IntegerUtil provides static methods for converting Integer values into other basic data types in a consistent manner. It also provides a static method to convert objects to Integer values. This method supports the basic data type objects of String, Date, Long etc and can be extended by registering TypeConverter instances with the IntegerUtil.

### Conversion Methods

The IntegerUtil offers the following static methods to convert Integer values to other types.

| Method                       | Description |
|------------------------------|-------------|
| `Boolean toBoolean(Integer)` | Converts the given Integer value into an Boolean using the current defaults of BooleanUtil|
| `Long toLong(Integer)`       | Converts the given Integer value into a long |
| `Float toFloat(Integer)`     | Converts the given Integer value into a Float |
| `Double toDouble(Integer)`   | Converts the given Integer value into a Double |
| `Date toDate(Integer)`       | Converts the given Integer value into a Date with the number of milliseconds since the epoch equal to the integer value |
| `String toString(Integer)`   | Converts the given Integer value into a String |

The IntegerUtil offers the following static method to convert Objects to Integer values.

| Method                       | Description |
|------------------------------|-------------|
| `Integer toInteger(Object)` | Converts the given Object into a Integer using the corresponding *Util static classes where they exist. Other types are converted into Integer values through registering TypeConverter instances with the IntegerUtil with the method `registerTypeConverter(...)`. See below for details on creating and registering TypeConverters with static utilities. |

### Random Methods

The IntegerUtil offers the following static methods to create random Integer values

| Method                             | Description |
|------------------------------------|-------------|
| `Integer random()`                 | Creates a random Integer value |
| `Integer random(Integer)`          | Creates a random Integer value between 0 and the given Integer value inclusive |
| `Integer random(Integer, Integer)` | Creates a random Integer value between the given integer values inclusive |

### Handling Nulls

The BooleanUtil offers the following static method to replace a null Boolean value with an alternative Boolean value

| Method                          | Description |
|---------------------------------|-------------|
| `Integer nvl(Integer, Integer)` | Returns the first Integer value if it is not null otherwise the second Integer value is returned |

## LongUtil

The LongUtil provides static methods for converting Long values into other basic data types in a consistent manner. It also provides a static method to convert objects to Long values. This method supports the basic data type objects of String, Date, Long etc and can be extended by registering TypeConverter instances with the LongUtil.

### Conversion Methods

The LongUtil offers the following static methods to convert Long values to other types.

| Method                       | Description |
|------------------------------|-------------|
| `Boolean toBoolean(Long)`    | Converts the given Long value into an Boolean using the current defaults of BooleanUtil|
| `Integer toInteger(Long)`    | Converts the given Long value into an Integer |
| `Float toFloat(Long)`        | Converts the given Long value into a Float |
| `Double toDouble(Long)`      | Converts the given Long value into a Double |
| `Date toDate(Long)`          | Converts the given Long value into a Date with the number of milliseconds since the epoch equal to the long value |
| `String toString(Long)`      | Converts the given Long value into a String |

The LongUtil offers the following static method to convert Objects to Long values.

| Method                       | Description |
|------------------------------|-------------|
| `Long toLong(Object)` | Converts the given Object into a Long using the corresponding *Util static classes where they exist. Other types are converted into Long values through registering TypeConverter instances with the LongUtil with the method `registerTypeConverter(...)`. See below for details on creating and registering TypeConverters with static utilities. |

### Random Methods

The LongUtil offers the following static methods to create random Long values

| Method                             | Description |
|------------------------------------|-------------|
| `Long random()`                    | Creates a random Long value |
| `Long random(Long)`                | Creates a random Long value between 0 and the given Long value inclusive |
| `Long random(Long, Long)`          | Creates a random Long value between the given Long values inclusive |

### Handling Nulls

The LongUtil offers the following static method to replace a null Long value with an alternative Long value

| Method                          | Description |
|---------------------------------|-------------|
| `Long nvl(Long, Long)` | Returns the first Long value if it is not null otherwise the second Long value is returned |

## FloatUtil

The FloatUtil provides static methods for converting Float values into other basic data types in a consistent manner. It also provides a static method to convert objects to Float values. This method supports the basic data type objects of String, Date, Long etc and can be extended by registering TypeConverter instances with the FloatUtil.

### Conversion Methods

The FloatUtil offers the following static methods to convert Float values to other types.

| Method                       | Description |
|------------------------------|-------------|
| `Boolean toBoolean(Float)`   | Converts the given Float value into a Boolean using the current defaults of BooleanUtil|
| `Long toLong(Float)`         | Converts the given Float value into a long |
| `Integer toInteger(Float)`   | Converts the given Float value into a Integer |
| `Double toDouble(Float)`     | Converts the given Float value into a Double |
| `Date toDate(Float)`         | Converts the given Float value into a Date with the number of milliseconds since the epoch equal to the integer value of the Float |
| `String toString(Float)`     | Converts the given Float value into a String |

The FloatUtil offers the following static method to convert Objects to Float values.

| Method                       | Description |
|------------------------------|-------------|
| `Float toFloat(Object)` | Converts the given Object into a Float using the corresponding *Util static classes where they exist. Other types are converted into Float values through registering TypeConverter instances with the FloatUtil with the method `registerTypeConverter(...)`. See below for details on creating and registering TypeConverters with static utilities. |

### Random Methods

The FloatUtil offers the following static methods to create random Float values

| Method                             | Description |
|------------------------------------|-------------|
| `Float random()`                   | Creates a random Float value |
| `Float random(Float, Float)`       | Creates a random Float value between the given Float values inclusive |

### Handling Nulls

The FloatUtil offers the following static method to replace a null Float value with an alternative Float value

| Method                          | Description |
|---------------------------------|-------------|
| `Float nvl(Float, Float)` | Returns the first Float value if it is not null otherwise the second Float value is returned |

## DoubleUtil

The DoubleUtil provides static methods for converting Double values into other basic data types in a consistent manner. It also provides a static method to convert objects to Double values. This method supports the basic data type objects of String, Date, Long etc and can be extended by registering TypeConverter instances with the DoubleUtil.

### Conversion Methods

The DoubleUtil offers the following static methods to convert Double values to other types.

| Method                       | Description |
|------------------------------|-------------|
| `Boolean toBoolean(Double)`  | Converts the given Double value into a Boolean using the current defaults of BooleanUtil|
| `Long toLong(Double)`        | Converts the given Double value into a long |
| `Integer toInteger(Double)`  | Converts the given Double value into a Integer |
| `Float toFloat(Double)`      | Converts the given Double value into a Float |
| `Date toDate(Double)`        | Converts the given Double value into a Date with the number of milliseconds since the epoch equal to the long value of the Double |
| `String toString(Double)`    | Converts the given Double value into a String |

The DoubleUtil offers the following static method to convert Objects to Double values.

| Method                       | Description |
|------------------------------|-------------|
| `Double toDouble(Object)`    | Converts the given Object into a Double using the corresponding *Util static classes where they exist. Other types are converted into Double values through registering TypeConverter instances with the FloatUtil with the method `registerTypeConverter(...)`. See below for details on creating and registering TypeConverters with static utilities. |

### Random Methods

The DoubleUtil offers the following static methods to create random Double values

| Method                             | Description |
|------------------------------------|-------------|
| `Double random()`                   | Creates a random Float value |
| `Double random(Double, Double)`       | Creates a random Float value between the given Double values inclusive |

### Handling Nulls

The DoubleUtil offers the following static method to replace a null Double value with an alternative Float value

| Method                          | Description |
|---------------------------------|-------------|
| `Double nvl(Double, Double)` | Returns the first Double value if it is not null otherwise the second Double value is returned |

## DateUtil

The DateUtil provides static methods for converting Date values into other basic data types in a consistent manner. It also provides a static method to convert objects to Date values. This method supports the basic data type objects of String, Date, Long etc and can be extended by registering TypeConverter instances with the BooleanUtil.

### Conversion defaults

The conversion of Date values to other data types is supported through the definition of several default instances of `SimpleDateFormat`.

The table below shows the default date formats used by the DateUtil to convert Date values to Strings.

| Alias        | Default Format      | Description |
|--------------|---------------------|-------------|
| DATE         | yyyy-MM-dd          | The date in a date only format |
| DATE_TIME    | yyyy-MM-dd HH:mm:SS | The date in a date and time format |

These values are encapsulated in the ennumeration `DateFormat`

The DateUtil selects which of these formats to use by default through setting the default format through the method `defaultDateFormat(DateFormat)`

These defaultFormatters can be extracted from or set in the DateUtil using the following methods

| Method                                       | Description |
|----------------------------------------------|-------------|
| `SimpleDateFormat dateFormatter()`           | Get the current default date formatter for formatting date only strings |
| `void dateFormatter(SimpleDateFormat)`       | Set the current default date formatter for formatting date only strings |
| `SimpleDateFormat dateTimeFormatter()`       | Get the current default date formatter for formatting date and time strings |
| `void dateTimeFormatter(SimpleDateFormat)`   | Set the current default date formatter for formatting date and time strings |
| `SimpleDateFormat defaultDateFormatter()`    | Get the current default date formatter |
| `SimpleDateFormat dateFormatter(DateFormat)` | Get the current default date formatter for the given DateFormat |

The initial values for these formatters can be set using the method `restoreDefaults()`

### Conversion Methods

The DateUtil offers the following static methods to convert Date values to other types.

| Method                                    | Description |
|-------------------------------------------|-------------|
| `Integer toInteger(Date)`                 | Converts the given Date value into an Integer using the integer value of the given date |
| `Long toLong(Date)`                       | Converts the given Date value into a long using the long value of the given date |
| `Float toFloat(Date)`                     | Converts the given Date value into a using the long value of the given date |
| `Double toDouble(Date)`                   | Converts the given Date value into a Double using the long value of the given date |
| `Boolean toBoolean(Date)`                 | Converts the given Date value into a Date using the long value of the given date and the current default from BooleanUtil |
| `String toString(Date)`                   | Converts the given Date value into a String using the current default formatter |
| `String toString(Date, DateFormat)`       | Converts the given Date value into a String using the current formatter identified by the given DateFormat |
| `String toString(Date, SimpleDateFormat)` | Converts the given Date value into a String using the given SimpleDateFormat |
| `String toString(Date, String)`           | Converts the given Date value into a String in the given date format |

The DateUtil offers the following static method to convert Objects to Date values.

| Method                       | Description |
|------------------------------|-------------|
| `Date toDate(Object)` | Converts the given Object into a Date using the corresponding *Util static classes where they exist. Other types are converted into Date values through registering TypeConverter instances with the DateUtil with the method `registerTypeConverter(...)`. See below for details on creating and registering TypeConverters with static utilities. |

### Random Methods

The DateUtil offers the following static method to create random Date values

| Method                    | Description |
|---------------------------|-------------|
| `Date random()`           | Creates a random Date value |
| `Date random(Date. Date)` | Creates a random Date value between the given dates inclusive |

### Handling Nulls

The DateUtil offers the following static method to replace a null Boolean value with an alternative Date value

| Method                 | Description |
|------------------------|-------------|
| `Date nvl(Date, Date)` | Returns the first Date value if it is not null otherwise the second Date value is returned |

## StringUtil

The StringUtil provides static methods for converting String values into other basic data types in a consistent manner. It also provides a static method to convert objects to String values. This method supports the basic data type objects of String, Date, Long etc and can be extended by registering TypeConverter instances with the StringUtil.

### Conversion Methods

The StringUtil offers the following static methods to convert String values to other types.

| Method                       | Description |
|------------------------------|-------------|
| `Boolean toBoolean(String)`  | Converts the given String value into a Boolean using the current defaults of BooleanUtil|
| `Long toLong(String)`        | Converts the given String value into a long |
| `Integer toInteger(String)`  | Converts the given String value into a Integer |
| `Float toFloat(String)`      | Converts the given String value into a Float |
| `Date toDate(String)`        | Converts the given String value into a Date using the dafault date formatter of DateUtil |
| `Double toDouble(String)`    | Converts the given String value into a Double |

The StringUtil offers the following static method to convert Objects to String values.

| Method                       | Description |
|------------------------------|-------------|
| `String toString(Object)`    | Converts the given Object into a String using the corresponding *Util static classes where they exist. Other types are converted into String values through registering TypeConverter instances with the StringUtil with the method `registerTypeConverter(...)`. See below for details on creating and registering TypeConverters with static utilities. |

### Random Methods

The StringUtil offers the following static methods to create random String values

| Method                                   | Description |
|------------------------------------------|-------------|
| `String random(int)`                     | Creates a random String of the given length |
| `String random(int, int)`                | Creates a random String between the given lengths inclusive |
| `String random(Intger, String)`          | Creates a random String of the given length from the set of characters in the given String |
| `String random(Intger, Integer, String)` | Creates a random String between the given lengths from the set of characters in the given String |

The `random(...)` methods are supported using a default set of salt characters. The default salt characters is the set of lower case latin characters and upper case characters.
The methods `saltChars()` returns the current set of salt characters as a String. The method `saltChars(String)` set the salt characters to the characters in the given string.

### Handling Nulls

The StringUtil offers the following static method to replace a null String value with an alternative String value

| Method                       | Description |
|------------------------------|-------------|
| `String nvl(String, String)` | Returns the first String value if it is not set otherwise the second String value is returned. **NOTE** This method uses the `isSet(String)` method below |

### Handling Strings

The StringUtil provides the following static methods for handling Strings

| Method                                           | Description |
|--------------------------------------------------|-------------|
| `String initialLowerCase(String)`                | Returns the given Sting with its first character in lower case |
| `String initialUpperCase(String)`                | Returns the given Sting with its first character in upper case |
| `boolean isSet(String)`                          | Returns true if the given String is not null and contains non white space characters |
| `String[] words(String)`                         | Splits the given string into words on any white space characters |
| `String safeWord(String)`                        | Returns the given String with all its unsafe characters removed. See below for more details |
| `String aliasCase(String)`                       | Returns the given String in alias case. See below |
| `String camelCase(String)`                       | Returns the given String in camel case. See below |
| `String classCase(String)`                       | Returns the given String in class case. See below |
| `String staticCase(String)`                      | Returns the given String in static case. See below |
| `String kebebCase(String)`                       | Returns the given String in kebab case. See below |
| `String replaceAll(String, String, Object ... )` | Returns first String replacing each occurrence of the second string with the string variants of the given objects |

### Alias Case

A String in alias case is all lower case except the first letter of each word apart from the first word and with all white space and unsafe characters removed. If the first character of the first word is a digit then the String is prefixed with an underscore. This provides a String suitable for use as variable alias.

e.g. `thisIsInAliasCase` or `_1thisIsInAliasCase`

### Camel Case

A String in camel case is all in lower case except for the first letter of each word which is in upper case. Duplicate white space characters within the body of the String (.i.e not at either end of the string) are replace with a single space character and leading and training white space is removed.

e.g. `This Is In Camel Case`

### Class Case

A String in class case is all in lower case except the first character of each word which is in upper case and with all white space and unsafe characters removed. If the first character of the first word is a digit then the String is prefixed with an underscore. This provides a String suitable for use as a Class name.

e.g. `ThisIsInClassCase` or `_1thisIsInClassCase`
 
### Static Case

A String in static case is all upper case with white space replaced by the underscore character and unsafe characters removed. If the first character of the first word is a digit then the String is prefixed with an underscore.

e.g. `THIS_IS_IN_STATIC_CASE` or `_1THIS_IS_IN_SATAIC_CASE`

### Kebab Case

A String in kebab case has all leading and trailing white space removed and white space within the body of the String replaced by the hyphen character and all in lower case.

e.g. `this-is-in-kebab-case`

### Safe Characters

The StringUtil by default defines safe characters as upper and lower case latin characters and the 10 digits.

The StringUtil provides methods to manage the set of safe characters.

| Method                   | Description |
|--------------------------|-------------|
| `String safeChars()`     | Returns the set of safe characters as a String |
| `void safeChars(String)` | Sets the safe characters to the characters in the given String |

## FileUtil

The FileUtil provides static methods for dealing with files. There are methods to find files in a directory, cascade delete files and handle locking of files.

### Finding Files

FileUtil defines the following methods for finding files.

| Method                                           | Description |
|--------------------------------------------------|-------------|
| `File fetch(File, String)`                       | Returns the file with a name matching the given String in the given directory. If the File is not a directory or cannot be read a null value is returned |
| `List<File> listForExtension(File, String ... )` | Returns as a `List` all the files in a the given directory with any of the given file extensions. The given file extensions may be prefixed with a period or not |

### Deleting Files

FileUtil defines the following methods for deleting files.

| Method | Description |
|--------|-------------|
| `deleteCascase(File)` | Deletes the file and all its contents if it is a directory |

### Locking Files

FileUtil provides the following methods for locking files. Locking files is achieved through the `FileLocked` java API.

| Method | Description |
|--------|-------------|
| `FileLock lock(File)` | This method locks the file if it exists and is not already locked and returns the FileLock holding the lock. If the file is already locked the checked `FileLockedException` is thrown |
| `boolean isLocked(File)` | returns true if the file is locked |

### Createing Files

FileUtil provides the following methods for creating files.

| Method           | Description |
|------------------|-------------|
| `buildTree(File, Path ...)` | This method builds a directory tree defined by the given paths as child directories of the given file. If the given file does not exist it is created. If it is a file a UtilityError is thrown |

## Registering Type Converters

Type converters can be registered with static util classes through the `registerTypeConverter(TypeConverter)` method of the static utility.

With the following classes defined:
```java
public static interface FooBar {
	public int getA();
	public int getB();
}

public static class Bar  implements FooBar{
	Bar(int a, int b) {
		this.a = a;
		this.b = b;
	}
	int a;
	int b;
	@Override
	public int getA() { return a; }
	@Override
	public int getB() { return b; }
}
```
The code below shows an example of registering a type converter to convert an instance of FooBar to a boolean value
```java
BooleanUtil.registerTypeConverter(new TypeConverter<FooBar,Boolean>() {

	@Override
	public Class<FooBar> convertClass() { return FooBar.class; }

	@Override
	public Boolean convert(Object value) {
		if (value instanceof FooBar) {
			FooBar fooBar = (FooBar)value;
			return (fooBar.getA() > fooBar.getB());
		}
		return false;
	}});
```
Having registerd the TypeConverter above the BooleanUtil class can convert an instance of FooBar into a Boolean value as below:
```java
Bar bar = new Bar(1,0);
	
Boolean b = BooleanUtil.toBoolean(bar);
```

## ClassUtil 

The ClassUtil provides static methods for dealing with Classes

The following methods are defined

| Method                                                             | Description |
|--------------------------------------------------------------------|-------------|
| `Class[] getClasses(String)`                                       | This method returns all the classes in the named package and sub packages |
| `Class[] getClasses(String, boolean)`                              | This method returns all the classes in the named package and sub packages and optionally throws an exception if the there is a class on the class path was unable to be found in the class loader | 
| `Class[] getClasses(String, boolean, AnnotationCheck, Class ... )` | This method returns all the classes in the named package and sub packages and optionally throws an exception if there is a class on the class path that was unable to found in the class loader where the class is annotated with the defined annotations |
| `Class[] getClasses(String, AnnotationCheck, Class ... )`          | This method returns all the classes in the named package and sub packages if they are annotated with the defined annotations |
| `Class[] getClasses(String, Class ... )`                           | This method returns all the classes in the named package and sub packages if they are annotated with all the given annotations |

The ClassUtil defines the following methods of checking multiple annotations against a class in the enumeration `AnnotationCheck`

| Alias | Description |
|-------|-------------|
| ALL   | The found class must be annotated with all the given annotations |
| ANY   | The found class must match any of the given annotations |









































