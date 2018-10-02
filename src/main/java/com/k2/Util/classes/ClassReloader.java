package com.k2.Util.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;

import com.k2.Util.ObjectUtil;

public class ClassReloader extends ClassLoader {

	public static ClassReloader forParentClassLoader(ClassLoader parent) {
		return new ClassReloader(parent);
	}
	
    private ClassReloader(ClassLoader parent) {
        super(parent);
    }
/*
	@SuppressWarnings("unchecked")
	public Class<?> loadClass(String packageName, String className, String source) {

            
		String fullClassName = packageName.replace('.', '/')+"/"+className;
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		SimpleJavaFileObject simpleJavaFileObject = new SimpleJavaFileObject(URI.create(fullClassName + ".java"), Kind.SOURCE) {

			@Override
			public CharSequence getCharContent(boolean ignoreEncodingErrors) {
				return source;
			}
			
			@Override
			public OutputStream openOutputStream() throws IOException{
				return byteArrayOutputStream;
			}
		};
		
		@SuppressWarnings({ "rawtypes" })
		JavaFileManager javaFileManager = new ForwardingJavaFileManager(
				ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null)) {
						@Override
						public JavaFileObject getJavaFileForOutput(
								Location location,String className,
								JavaFileObject.Kind kind,
								FileObject sibling)  {
							return simpleJavaFileObject;
						}
		};
		
		ToolProvider.getSystemJavaCompiler().getTask(null, javaFileManager, null, null, null, ObjectUtil.singletonList(simpleJavaFileObject)).call();
		
		byte[] classData = byteArrayOutputStream.toByteArray();

		
//        byte[] classData = source.getBytes();

        return defineClass(packageName+"."+className,
                classData, 0, classData.length);


    }
*/
}