package testcars.data.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import testcars.data.dataobjects.BaseObject;

public interface Service<T extends BaseObject> {

	public void add(T o);
	public List<T> find(String search)  throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalArgumentException, NoSuchFieldException, SecurityException;
	public T get(Long id);
	public void sort(String type, String field, List<T> list);
}
