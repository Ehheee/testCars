package testcars.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import testcars.data.dataobjects.Car;
import testcars.data.service.CarService;

@Controller
@RequestMapping("/cars")
public class CarController extends BaseController<Car> {
	
	@RequestMapping("/{id}")
	@ResponseBody
	public Car getCarById(@PathVariable("id") Long id) {
		return carService.get(id);
	}
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object getCars(@RequestParam(value = "find", required = false) String search,
						  @RequestParam(value = "sort", required = false) String sort,
						  @RequestParam(value = "filter", required = false) String filter) 
						  throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalArgumentException, NoSuchFieldException, SecurityException {
		
		return this.processListRequest(search, sort, filter);
	}
	@Autowired
	private CarService carService;
}
