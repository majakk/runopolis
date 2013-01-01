package studio.coldstream.runopolis;

public class Player {

	private String myuser;
	private int myuserid;
	private String myemail;
	
	public Player(int userid, String user, String pass, String email){
		myuser = user;
		myuserid = userid;
		myemail = email;
	}
	
	public String getUser(){
		return myuser;
	}
	
	public int getUserId(){
		return myuserid;
	}
	
	public String getEmail(){
		return myemail;
	}
	
}
