package studio.coldstream.runopolis;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListCardsActivity extends Activity{

	private static final String TAG = "ListCardsActivity";

	ListView list_main;
	
	List<String> listan1;
	List<String> listan2;
	List<String> listan3;
	List<String> listan4;
	List<String> listan5;
	
	List<int[]> color1;
	
	String userid;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        Log.v(TAG, "LIST CARD ACTIVITY STARTED");
        
        Bundle extras = this.getIntent().getExtras();
        userid = Integer.toString(extras.getInt("USERID"));
        Log.d(TAG, userid);
        
        setContentView(R.layout.listcards);
        
        listan1 = new LinkedList<String>();
		listan2 = new LinkedList<String>();
		listan3 = new LinkedList<String>();
		listan4 = new LinkedList<String>();
		listan5 = new LinkedList<String>();
		
		color1 = new LinkedList<int[]>();		
		
        //A function that Loads the lists with data from the database. Help class to retrieve/return data or? 
		if(extras.getInt("USERID") != 0)
			loadListData();
        	
		list_main = (ListView)findViewById(R.id.ListView01);
        list_main.setAdapter(new CardsAdapter(this, listan1, listan2, listan3, listan4, listan5));
        list_main.invalidate();	
	}
	
	public void loadListData(){
		DataBaseComm dbc = new DataBaseComm();
		List<Card> cd = new LinkedList<Card>();
		
		//Retrieve list from db using the player userid
		cd = dbc.listCards(userid);	
		int s = 0;
		try{
			s = cd.size();
		}catch(Exception e){
			e.printStackTrace();
		}
		listan1.add("Street, Area   (Total: " + Integer.toString(s) + ")");
		listan2.add("Houses");
		listan3.add("Level");
		listan4.add("Value");
		listan5.add("Tax Reve");
		
		color1.add(new int[] {15, 18, 78});
		
		if(cd != null){
			for(int i = 0; i < cd.size(); i++){
				listan1.add(cd.get(i).getStreet().toUpperCase() + ", " + cd.get(i).getArea());
				listan2.add(Integer.toString(cd.get(i).getHouses()));
				listan3.add(Integer.toString(cd.get(i).getTransactions()));
				listan4.add(Integer.toString(cd.get(i).getValue()));
				listan5.add("");
				
				color1.add(cd.get(i).getColor());
			}
		}		
	}	
	
	public class CardsViewHolder{
		TextView text1;
		TextView text2;
		TextView text3;
		TextView text4;
		TextView text5;
	}
	
	public class CardsAdapter extends BaseAdapter{
        Context mContext;
        List<String> listan1;
        List<String> listan2;
        List<String> listan3;
        List<String> listan4;
        List<String> listan5;
        public CardsAdapter(Context k, List<String> a, List<String> b, List<String> c, List<String> d, List<String> e){
            mContext = k;
            listan1 = new LinkedList<String>();
            listan1 = a;
            listan2 = new LinkedList<String>();
            listan2 = b;
            listan3 = new LinkedList<String>();
            listan3 = c;
            listan4 = new LinkedList<String>();
            listan4 = d;
            listan5 = new LinkedList<String>();
            listan5 = e;
            
        }
        @Override
        public int getCount() {
            return listan1.size();
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CardsViewHolder v;
            if(convertView==null){
                LayoutInflater li = getLayoutInflater();
                convertView = li.inflate(R.layout.cardrow, null);
             
                v = new CardsViewHolder();
                v.text1 = (TextView)convertView.findViewById(R.id.item_text);
                v.text2 = (TextView)convertView.findViewById(R.id.quantity_text);
                v.text3 = (TextView)convertView.findViewById(R.id.value_text);
                v.text4 = (TextView)convertView.findViewById(R.id.bid_text);
                v.text5 = (TextView)convertView.findViewById(R.id.bids_text);
                convertView.setTag(v);
            }
            else
            {
                v = (CardsViewHolder) convertView.getTag();
            }
            v.text1.setText(listan1.get(position).toString());
            v.text2.setText(listan2.get(position).toString());
            v.text3.setText(listan3.get(position).toString());
            v.text4.setText(listan4.get(position).toString());
            v.text5.setText(listan5.get(position).toString());
            if(position % 2 == 1){
            	v.text1.setBackgroundColor(Color.rgb(color1.get(position)[0], color1.get(position)[1], color1.get(position)[2]));
            	//v.text1.setBackgroundColor(Color.rgb(20, 24, 110));
            	v.text2.setBackgroundColor(Color.rgb(20, 24, 110));
            	v.text3.setBackgroundColor(Color.rgb(20, 24, 110));
            	v.text4.setBackgroundColor(Color.rgb(20, 24, 110));
            	v.text5.setBackgroundColor(Color.rgb(20, 24, 110));
            }
            else{
            	v.text1.setBackgroundColor(Color.rgb(color1.get(position)[0], color1.get(position)[1], color1.get(position)[2]));
            	//v.text1.setBackgroundColor(Color.rgb(15, 18, 78));
            	v.text2.setBackgroundColor(Color.rgb(15, 18, 78));
            	v.text3.setBackgroundColor(Color.rgb(15, 18, 78));
            	v.text4.setBackgroundColor(Color.rgb(15, 18, 78));
            	v.text5.setBackgroundColor(Color.rgb(15, 18, 78));
            }
            return convertView;
        }
              
		@Override
		public Object getItem(int position) {		
			return null;
		}
		@Override
		public long getItemId(int position) {	
			return 0;
		}
	};
	
}
