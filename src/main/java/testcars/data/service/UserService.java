package testcars.data.service;

import java.util.HashMap;
import org.springframework.stereotype.Component;

import testcars.data.dataobjects.User;
@Component
public class UserService extends BaseService<User> implements Service<User> {

	public UserService() {
		this.objects = new HashMap<>();
	}
}
