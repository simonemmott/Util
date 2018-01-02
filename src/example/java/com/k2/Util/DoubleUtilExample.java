package com.k2.Util;
import java.util.Date;

import com.k2.Util.TypeConverter;
import com.k2.Util.Sample.Foo;

public class DoubleUtilExample {
	

	public static void main(String[] args) {
		
		DoubleUtil.registerTypeConverter(new TypeConverter<Foo,Double>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public Double convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					return foo.fooDouble;
				}
				return null;
			}});
		
		
		Foo foo1 = new Foo();
		foo1.fooDouble = new Double(123.456);
		Foo foo2 = new Foo();
		foo2.fooDouble = new Double(987.654);
		
		
		System.out.println(DoubleUtil.toDate(new Double(987.654)).getTime());
		
	}

}
