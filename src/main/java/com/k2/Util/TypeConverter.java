package com.k2.Util;

/**
 * This interface defines the methods of a TypeConverter implementation used by the static utility classes to convert
 * complex types into native data types of String, Integer, Long etc.
 * 
 * @author simon
 *
 * @param <T>	The type to be converted
 * @param <C>	The type being converted to
 */
public interface TypeConverter<T,C> {
	/**
	 * This method must return the class or interface of the type being converted
	 * 
	 * e.g.
	 * public Class&lt;Foo&gt; convertClass() { return Foo.class; }
	 * 
	 * @return	The class or interface of the type being converted
	 */
	public Class<T> convertClass();
	/**
	 * This method performs the conversion on the given object.
	 * 
	 * e.g.
	 * public Boolean convert(Object value) {
	 *     if (value instanceof Foo) {
	 *         Foo foo = (Foo)value;
	 *         return (foo.a &gt; foo.b);
	 *     }
	 *     return false;
	 *     }
	 *     
	 * @param value The object to convert
	 * @return	The converted value
	 */
	public C convert(Object value);

}
