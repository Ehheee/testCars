package testcars.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import testcars.data.dataobjects.Car;
import testcars.data.dataobjects.User;
import testcars.data.service.UserService;
import testcars.data.service.CarService;

@Component
public class Initializer {
	
	private ObjectMapper mapper = new ObjectMapper();
	protected Log logger = LogFactory.getLog(getClass());
	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		logger.info("Initalizing data");
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
			logger.error("Cannot initialize data", e);
		}
	}
	@Autowired
	private CarService carService;
	@Autowired
	private UserService userService;
}
