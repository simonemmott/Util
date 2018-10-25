package com.k2.Util.classes;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

public class K2ComponentScanner extends ClassPathScanningCandidateComponentProvider {

	public K2ComponentScanner() {
		super(false);
	}
	
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return true;
	}

}
