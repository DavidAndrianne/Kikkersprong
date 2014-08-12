package mobile.apps.kikkersprong.model;

import java.util.Date;

public class Child {
	//Cumpolsory
	private long id;
	private String firstname;
	private String name;
	
	//Optional
	private String nickname;
	private Date birthday;
	private String emailAddress;
	
	public Child(){}
	public Child(long id, String firstname, String name){
		setId(id);
		setFirstname(firstname);
		setName(name);
	}
	public Child(long id, String firstname, String name, String nickname, Date birthday, String emailAddress){
		setId(id);
		setFirstname(firstname);
		setName(name);
		setNickname(nickname);
		setBirthday(birthday);
		setEmailAddress(emailAddress);
	}
	
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public String getFirstname() { return firstname; }
	public void setFirstname(String firstname) { this.firstname = firstname; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getNickname() { return nickname; }
	public void setNickname(String nickname) { this.nickname = nickname; }

	public Date getBirthday() { return birthday; }
	public void setBirthday(Date birthday) { this.birthday = birthday; }

	public String getEmailAddress() { return emailAddress; }
	public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
}