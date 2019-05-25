package fr.dornacraft.servertools.utils;

public class Home {

	private String name;
	private double x;
	private double y;
	private double z;
	private String user;

	public Home() {}

	public Home(String name, double x, double y, double z, String user) {
		super();
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
