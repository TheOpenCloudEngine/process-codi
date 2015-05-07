package org.uengine.codi.mw3.widget;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

@Face(ejsPath="genericfaces/Window.ejs")
public class Person {

	String name;
	int age;
	
	public Person() {}
	
	public Person(String name,int age) {
		this.name = name;
		this.age = age;
	}
	
	@Id
	@Face(displayName="성명")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	@ServiceMethod(callByContent=true)
	public void saveToAddressbook() throws InterruptedException
	{
		Thread.sleep(3000);
		System.out.println("Hello, " + getName() + " and your age is " + getAge());
	}
	
}
