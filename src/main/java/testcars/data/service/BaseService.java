package testcars.data.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import testcars.data.dataobjects.BaseObject;

public abstract class BaseService<T extends BaseObject> implements Service<T> {

	protected Map<Long, T> objects;
	protected Log logger = LogFactory.getLog(getClass());
	public List<T> find(String search) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalArgumentException, NoSuchFieldException, SecurityException {
		if (search == null) {
			return new ArrayList<T>(objects.values());
		}
		List<T> results = new ArrayList<>();
		if (objects.size() < 1) {
			return results;
		}
		for (Entry<Long, T> e: objects.entrySet()) {
			for (Entry<String, String> props: BeanUtils.describe(e.getValue()).entrySet()) {
				Field f = e.getValue().getClass().getField(props.getKey());
				if (f.get(e.getValue()) instanceof BaseObject || "class".equals(props.getKey())) {
					continue;
				}
				if (props.getValue().toLowerCase().contains((search.toLowerCase()))) {
					results.add(e.getValue());
				}
			}
		}
		return results;
	}
	public void sort(String type, String field, List<T> list) {
		list.sort(new BeanComparator<T>(field));
		if ("desc".equals(type)) {
			Collections.reverse(list);
		}
	}
	public void add(T o) {
		if (o.getId() == null) {
			o.setId(new Long(this.objects.size()));
		}
		this.objects.put(o.getId(), o);
	}
	public T get(Long id) {
		return this.objects.get(id);
	}
}
