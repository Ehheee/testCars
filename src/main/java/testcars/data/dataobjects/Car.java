package testcars.data.dataobjects;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
public class Car extends BaseObject {

	private String make;
	private String model;
	private String numberplate;
	@JsonIdentityReference(alwaysAsId = true)
	private User user;
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getNumberplate() {
		return numberplate;
	}
	public void setNumberplate(String numberplate) {
		this.numberplate = numberplate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
