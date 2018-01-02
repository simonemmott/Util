package com.k2.Util;
import com.k2.Util.StringUtil;

public class StringUtilExample {
	
	public static void main(String[] args) {
		
		System.out.println(StringUtil.random(20));
		System.out.println(StringUtil.random(10,20));
		String test = "this is my phrase";
		System.out.println("Alias case: "+StringUtil.aliasCase(test));
		System.out.println("Camel case: "+StringUtil.camelCase(test));
		System.out.println("Class case: "+StringUtil.classCase(test));
		System.out.println("Static case: "+StringUtil.staticCase(test));
		System.out.println("Kebab case: "+StringUtil.kebabCase(test));
		test = "123hello world! @#a$%";
		System.out.println("Alias case: "+StringUtil.aliasCase(test));
		System.out.println("Camel case: "+StringUtil.camelCase(test));
		System.out.println("Class case: "+StringUtil.classCase(test));
		System.out.println("Static case: "+StringUtil.staticCase(test));
		System.out.println("Kebab case: "+StringUtil.kebabCase(test));
	}

}
