package testcars.web;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import testcars.data.dataobjects.BaseObject;
import testcars.data.service.Service;

public class BaseController<T extends BaseObject> {

	protected Object processListRequest(String search, String sort, String filter) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalArgumentException, NoSuchFieldException, SecurityException, InstantiationException {
		List<T> result = service.find(search);
		result = this.processFilter(filter, result);
		this.processSort(sort, result);
		return result;
	}
	protected void processSort(String sort, List<T> list) {
		if (sort != null) {
			String[] sortArr = sort.split(":");
			service.sort(sortArr[1], sortArr[0], list);
		}
	}
	protected List<T> processFilter(String filter, List<T> list) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, SecurityException, InstantiationException {
		if (filter != null) {
			return service.filterSubObjects(list, filter);
		}
		return list;
	}
	@Autowired
	private Service<T> service;
}
