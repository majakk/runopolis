package studio.coldstream.runopolis;

public class Statistics {

	private int accdistance;
	/*private int fixes;
	
	private int turnover;
	private int netvalue;
	
	private int findingsperfix;
	private int biggestfind;*/
	
	
	public Statistics(){
		accdistance = 0;
	}
	
	public void addAccDistance(int dist){
		accdistance += dist;
	}
	
	public int getAccDistance(){
		return accdistance;
	}
	
}
