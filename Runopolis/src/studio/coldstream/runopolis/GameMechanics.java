package studio.coldstream.runopolis;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameMechanics {
	
	public static int DEFAULT_CASH = 25000;
	//private static int INCREMENTAL_VALUE = 5;
	public static String DEFAULT_STREET = "Welcome to Runopoly!";
	private static String DEFAULT_AREA = "Own your Neighburhood";
	public static String DEFAULT_OWNER = "Unclaimed";
	public static String DEFAULT_USER = "Anonomous";

	
	private Player me;
	private int myCash;
	private int myAddedCash;
	private Card currentCard;
	private List<Card> myCards;
	
	private Random generator;
	
	public GameMechanics(){
		me = new Player(0, DEFAULT_USER, "" ,"");
		myCash = 0;
		myAddedCash = 0;
		myCards = new LinkedList<Card>();
		currentCard = new Card("0 A 000000 0000000", DEFAULT_STREET, DEFAULT_AREA, 0, DEFAULT_OWNER, 0, 0);
		
		generator = new Random(System.currentTimeMillis());
	}
	
	public void foundCash(){
		//myAddedCash = INCREMENTAL_VALUE;
		//myCash += INCREMENTAL_VALUE;
		myAddedCash = ((int) (Math.round(Math.pow(Math.abs(generator.nextDouble()), -0.5))) % 10000) + 4;
		myCash += myAddedCash;
		return;
	}
	
	public void addCash(int c){
		myCash += c;
		if(myCash < 0)
			myCash = 0;
		myAddedCash = c;
		return;
	}
	
	public void setCash(int c){
		myCash = c;
		if(myCash < 0)
			myCash = 0;
		return;
	}
	
	public int getCash(){
		return myCash;
	}
	
	public int getAddedCash(){
		return myAddedCash;
	}
	
	public boolean addCard(Card acard){
		try {
			myCards.add(acard);
		} catch (IllegalArgumentException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean removeCard(int l){
		try {
			myCards.remove(l);
		} catch (IndexOutOfBoundsException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void setPlayer(Player p){
		me = p;		
		return;
	}
	
	public Player getPlayer(){		
		return me;
	}
	
	public void setCurrentCard(Card ccard){
		currentCard = ccard;
		return;
	}
	
	public Card getCurrentCard(){
		return currentCard;
	}
	
	
}
