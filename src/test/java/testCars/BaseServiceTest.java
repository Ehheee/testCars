package testCars;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import testcars.data.dataobjects.Car;
import testcars.data.dataobjects.User;
import testcars.data.service.CarService;
import testcars.data.service.UserService;

public class BaseServiceTest {

	private CarService carService;
	private UserService userService;
	private ObjectMapper mapper;
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		carService = new CarService();
		userService = new UserService();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("initData.json");
		TypeReference<List<User>> typeRef = new TypeReference<List<User>>(){};
		try {
			List<User> users = mapper.readValue(in, typeRef);
			for (int i = 0; i < users.size(); i++) {
				User user = users.get(i);
				Set<Car> userCars = user.getCars();
				Iterator<Car> it = userCars.iterator();
				while (it.hasNext()) {
					Car car = it.next();
					car.setUser(user);
					carService.add(car);
				}
				userService.add(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testFind() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalArgumentException, NoSuchFieldException, SecurityException {
		assertThat(1, equalTo(userService.find("Pille").size()));
		assertThat(0, equalTo(userService.find("Kia").size()));
		assertThat(4, equalTo(userService.find("k").size()));
	}
	@Test
	public void testCollectionFilter() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalArgumentException, NoSuchFieldException, SecurityException, InstantiationException {
		List<User> users = userService.find("Pille");
		assertThat(userService.get(users.get(0).getId()), equalTo(users.get(0)));
		assertThat(users.get(0).getCars().size(), equalTo(2));
		List<User> filteredUsers = userService.filterSubObjects(users, "Kia");
		assertThat(1, equalTo(filteredUsers.size()));
		assertThat(filteredUsers.get(0).getCars().size(), equalTo(1));
	}
}
