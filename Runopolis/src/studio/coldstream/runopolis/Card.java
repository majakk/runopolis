package studio.coldstream.runopolis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class Card {
	
	private static int DEFAULT_VALUE = 1000;
	private static double TAX_MULTIPLIER = 0.02;
	private static double HOUSE_MULTIPLIER = 0.1;
	private static int TRANSACTIONS_MULTIPLIER = 100;	
	
	private String position;
	private String street;
	private String area;
	private String id;
	private int transactions;
	private int color[] = {150, 150, 150};
	private int value;
	private int tax;
	private String owner;
	private int ownerid;
	private int housePrise;
	private int houses;
	
	public Card(String pos, String str, String are, int tra, String own, int uid, int hou){
		transactions = tra;
		position = pos;
		street = str;
		area = are;
		
		setColor(pos);
		
		setId(pos);
				
		if(own == "" || own == null){
			owner = GameMechanics.DEFAULT_OWNER;
		}
		else{
			owner = own;
		}
		ownerid = uid;
		
		if(hou < 0)
			houses = 0;
		else if(hou > 5)
			houses = 5;
		else
			houses = hou;		
		setValue();		
		
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(){
		value = (transactions * TRANSACTIONS_MULTIPLIER) + DEFAULT_VALUE;
		tax = (int) (value * TAX_MULTIPLIER + DEFAULT_VALUE * houses * HOUSE_MULTIPLIER);
		if(houses <= 5)
			housePrise = (int)((houses + 2) * value * 0.5);
		else
			housePrise = 0;
	}
	
	public int getHousePrise(){
		return housePrise;
	}
	
	public int getTax(){
		return tax;
	}
		
	public void setLocationData(Context c, String posstring){
		CoordinateConversion bertil = new CoordinateConversion();
		String[] david = posstring.split(" ");
		double[] esteban = bertil.utm2LatLon(david[0] + " " + david[1] + " " + david[2].substring(0, 3) + "500 " + david[3].substring(0, 4) + "500");
		
		position = david[0] + " " + david[1] + " " + david[2].substring(0, 3) + "500 " + david[3].substring(0, 4) + "500";
		
		//Set Address and Area
		street = position; //default
		area = "(UTM)";
		List<Address> listan = new LinkedList<Address>();
		Geocoder feodor = new Geocoder(c);
		try {
			listan.addAll(feodor.getFromLocation(esteban[0], esteban[1], 9));
			//Select the first non-null values from list
			for(int i = 0; i < listan.size(); i++){				
				if(listan.get(i).getThoroughfare() != null)
					street = listan.get(i).getThoroughfare();
				if(listan.get(i).getLocality() != null)
					area = listan.get(i).getLocality();			
			}
			
		} catch (IOException e) {			
			e.printStackTrace();
		}				
		
		setColor(posstring);
		
		setId(posstring);
	}
	
	public void setColor(String posstring){		
		String[] david = posstring.split(" ");		
		position = david[0] + " " + david[1] + " " + david[2].substring(0, 3) + "500 " + david[3].substring(0, 4) + "500";
		
		//setColor
		String[] pos = david;
		Random rand = new Random();
		rand.setSeed(Long.parseLong(pos[2].substring(0, 3) + pos[3].substring(1, 4)) + 1);
		color[0] = 127 + Math.round((rand.nextInt() % 255) / 2);
		color[1] = 127 + Math.round((rand.nextInt() % 255) / 2);
		color[2] = 127 + Math.round((rand.nextInt() % 255) / 2);
	}
	
	public void setId(String posstring){
		String[] david = posstring.split(" ");
		String[] pos = david;
		id =  pos[0] + Character.getNumericValue(pos[1].toCharArray()[0]) + pos[2].substring(0, 3) + pos[3].substring(0, 4);
	}
	
	public String getPosition(){
		return position;
	}
	
	public String getStreet(){
		return street;
	}
	
	public String getArea(){
		return area;
	}		
	
	public int[] getColor(){
		return color;
	}
	
	public String getId(){
		return id;
	}
	
	public void setOwner(Player p){
		owner = p.getUser();
		ownerid = p.getUserId();
	}
	
	public void setOwner(String own){
		owner = own;
	}
	
	public String getOwner(){
		return owner;
	}
	
	public int getOwnerId(){
		return ownerid;
	}
	
	public int getHouses(){
		return houses;
	}
	
	public void setHouses(int hou){
		if(hou < 0)
			houses = 0;
		else if(hou > 5)
			houses = 5;
		else
			houses = hou;
		setValue();
	}
	
	public void setTransactions(int t){
		transactions = t;
		setValue();
	}
	
	public int getTransactions(){
		return transactions;
	}
	
	
}
