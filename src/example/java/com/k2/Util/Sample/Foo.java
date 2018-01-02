package com.k2.Util.Sample;

import java.util.Date;

@TestAnnotation1
@TestAnnotation2
public class Foo {

	@javax.persistence.Id
	public Long id;
	public String name;
	public String description;
	public Date fooDate;
	public Double fooDouble;
	public Float fooFloat;
	public Integer fooInteger;
	public Long fooLong;

}
