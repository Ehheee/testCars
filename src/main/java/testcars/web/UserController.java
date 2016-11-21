package testcars.web;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import testcars.data.dataobjects.User;
import testcars.data.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController<User> {

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseBody
	public User getUserById(@PathVariable("id") Long id) {
		return userService.get(id);
	}
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object getUsers(@RequestParam(value = "find", required = false) String search,
						   @RequestParam(value = "sort", required = false) String sort,
						   @RequestParam(value = "filter", required = false) String filter) 
						   throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalArgumentException, NoSuchFieldException, SecurityException {
		return this.processListRequest(search, sort, filter);
	}
	@Autowired
	private UserService userService;
}
