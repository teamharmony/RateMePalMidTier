package prj.resources.mgmt.domain;

public class User {
	
	private String name;
	private String contact;
	private String username;
	private String email;
	private String password;
	private byte[] profilePic;
	private int visible;
	private String status;
	private String designation;
	private String description;
	

	public String getDesignation() {
		return designation;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getStatus() {
		return status;
	}
	
	public int getVisible() {
		return visible;
	}

	public String getName() {
		return name;
	}

	public String getContact() {
		return contact;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public byte[] getProfilePic() {
		return profilePic;
	}

	
	
	public void setName(String name) {
		this.name = name;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User(){
		//do nothing
	}
	
	public User(String name, String contact,
			String username, String email, String password,
			byte[] profilePic, Location location, int visible, String status, String designation, String description) {
		super();
		this.name = name;
		this.contact = contact;
		this.username = username;
		this.email = email;
		this.password = password;
		this.profilePic = profilePic;
		this.visible = visible;
		this.status = status;
		this.designation = designation;
		this.description = description;
	}
	
	private User(UserBuilder builder) {
		this.name = builder.name;
		this.contact = builder.contact;
		this.username = builder.username;
		this.email = builder.email;
		this.password = builder.password;
		this.profilePic = builder.profilePic;
		this.visible = builder.visible;
		this.status = builder.status;
		this.designation = builder.designation;
		this.description = builder.description;
		
	}
	
	public static class UserBuilder {
		private String name;
		private String contact;
		private String username;
		private String email;
		private String password;
		private byte[] profilePic;
		private int visible;
		private String status;
		private String designation;
		private String description;
		
		public UserBuilder designation(String designation) {
			this.designation = designation;
			return this;
		}
				
		public UserBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		public UserBuilder status(String status) {
			this.status = status;
			return this;
		}
				
		public UserBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public UserBuilder contact(String contact) {
			this.contact = contact;
			return this;
		}

		public UserBuilder userName(String userName) {
			this.username = userName;
			return this;
		}
		
		public UserBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserBuilder password(String password) {
			this.password = password;
			return this;
		}
		
		public UserBuilder profilePic(byte[] profilePic) {
			this.profilePic = profilePic;
			return this;
		}
		
		public UserBuilder visible(int visible) {
			this.visible = visible;
			return this;
		}
		
		public User build() {
			User u = new User(this);
			return u;
		}
		
	}
		

}
