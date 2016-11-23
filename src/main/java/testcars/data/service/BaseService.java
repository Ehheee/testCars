package testcars.data.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
			T o = e.getValue();
			if (this.searchObject(o, search)) {
				results.add(o);
			}
		}
		return results;
	}
	private boolean searchObject(Object o, String search) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException {
		for (Entry<String, String> prop: BeanUtils.describe(o).entrySet()) {
			if (!this.isSearchField(prop, o)) {
				continue;
			}
			if (prop.getValue().toLowerCase().contains((search.toLowerCase()))) {
				return true;
			}
		}
		return false;
	}
	private boolean isSearchField(Entry<String, String> prop, Object o) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if ("id".equals(prop.getKey()) || "class".equals(prop.getKey())) {
			return false;
		} else {
			Field f = o.getClass().getDeclaredField(prop.getKey());
			f.setAccessible(true);
			if (f.get(o) instanceof BaseObject || f.get(o) instanceof Collection || f.get(o) instanceof Map) {
				return false;
			} else {
				return true;
			}
		}
	}
	public List<T> filterSubObjects(List<T> list, String filter) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, SecurityException, InstantiationException {
		List<T> results = new ArrayList<>();
		for (T oOriginal: list) {
			T o = (T) oOriginal.getClass().newInstance();
			BeanUtils.copyProperties(o, oOriginal);
			for (Entry<String, String> prop: BeanUtils.describe(o).entrySet()) {
				if ("id".equals(prop.getKey()) || "class".equals(prop.getKey())) {
					continue;
				}
				Field f = o.getClass().getDeclaredField(prop.getKey());
				f.setAccessible(true);
				if (f.get(o) instanceof BaseObject) {
					if (!this.searchObject(o, filter)) {
						f.set(o, null);
					}
				} else if (f.get(o) instanceof Map<?, ?>) {
					Map<?, ?> map = new HashMap<>((Map<?, ?>)f.get(o));
					f.set(o,map);
					this.filterMapObjects(map, filter);
				} else if (f.get(o) instanceof Collection) {
					Set set = (Set) f.get(o);
					Set set2 = new HashSet();
					set2.addAll(set);
					f.set(o, set2);
					this.filterCollectionObjects(set2, filter);
				}
			}
			results.add(o);
		}
		return results;
	}
	private void filterMapObjects(Map<?, ?> map, String filter) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException {
		for (Object key: map.keySet()) {
			if (!(map.get(key) instanceof BaseObject)) {
				return;
			} else {
				if (!this.searchObject(map.get(key), filter)) {
					map.remove(key);
				}
			}
		}
	}
	private void filterCollectionObjects(Collection<?> c, String filter) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException {
		Iterator<?> it = c.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (!(o instanceof BaseObject)) {
				return;
			} else {
				if (!this.searchObject(o, filter)) {
					it.remove();
				}
			}
		}
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
