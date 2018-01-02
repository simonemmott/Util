package com.k2.Util;
import com.k2.Util.BooleanUtil;
import com.k2.Util.TypeConverter;

public class BooleanUtilExample {
	
	public static class Foo {
		Foo(int a, int b) {
			this.a = a;
			this.b = b;
		}
		int a;
		int b;
//		@Override
		public int getA() { return a; }
//		@Override
		public int getB() { return b; }
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
	
	public static interface FooBar {
		public int getA();
		public int getB();
	}

	public static void main(String[] args) {
		
		BooleanUtil.registerTypeConverter(new TypeConverter<Foo,Boolean>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public Boolean convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					return (foo.a > foo.b);
				}
				return false;
			}});
		
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
		
		Foo trueFoo = new Foo(1,0);
		Foo falseFoo = new Foo(0,1);
		Bar trueBar = new Bar(1,0);
		Bar falseBar = new Bar(0,1);
		
		if (BooleanUtil.toBoolean(trueFoo)) System.out.println("trueFoo");
		if (BooleanUtil.toBoolean(falseFoo)) System.out.println("falseFoo");
		if (BooleanUtil.nvl(BooleanUtil.toBoolean(trueBar), false)) System.out.println("trueBar");
		if (BooleanUtil.nvl(BooleanUtil.toBoolean(falseBar), false)) System.out.println("falseBar");
		
	}

}
