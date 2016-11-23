package testCars;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

import testcars.data.dataobjects.Car;
import testcars.data.dataobjects.User;

public class ObjectCopyTest {

	@Test
	public void test1() {
		User u1 = new User();
		u1.setName("name1");
		Car c1 = new Car();
		Car c2 = new Car();
		u1.getCars().add(c1);
		u1.getCars().add(c2);
		User u2 = new User();
		BeanUtils.copyProperties(u1, u2);
		assertThat(u1, not(equalTo(u2)));
		assertThat(u1.getCars(), equalTo(u2.getCars()));
		u2.setCars(new HashSet<>(u1.getCars()));
		assertThat(u1.getCars(), equalTo(u2.getCars()));
		u2.getCars().remove(c1);
		assertThat(u1.getCars(), not(equalTo(u2.getCars())));
	}
}
