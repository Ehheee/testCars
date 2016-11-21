package testcars.data.service;

import java.util.HashMap;
import org.springframework.stereotype.Component;

import testcars.data.dataobjects.Car;

@Component
public class CarService extends BaseService<Car> implements Service<Car> {

	public CarService() {
		this.objects = new HashMap<>();
	}
}
