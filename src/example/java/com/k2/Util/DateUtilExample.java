package com.k2.Util;
import java.util.Date;

import com.k2.Util.TypeConverter;
import com.k2.Util.Sample.Foo;

public class DateUtilExample {
	

	public static void main(String[] args) {
		
		DateUtil.registerTypeConverter(new TypeConverter<Foo,Date>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public Date convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					return foo.fooDate;
				}
				return null;
			}});
		
		
		Foo foo1 = new Foo();
		foo1.fooDate = new Date(9999999999999L);
		Foo foo2 = new Foo();
		foo2.fooDate = new Date(111111111111111L);
		
		
		System.out.println(DateUtil.toString(new Date(12345678)));
		System.out.println(DateUtil.toString(new Date(987654321)));
		
		System.out.println(DateUtil.toDate("2016-08-30 23:25:59").getTime());
		
		System.out.println(StringUtil.toDate("1970-01-01 04:25:45").getTime());
		System.out.println(StringUtil.toDate("1970-01-12 11:20:54").getTime());
		System.out.println(StringUtil.toString(new Date(1472595959000L)));
	}

}
