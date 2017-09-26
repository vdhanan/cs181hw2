package cs181;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
 
public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	/* Implement the reduce function. 
	 * 
	 * 
	 * Input :    Adjacency Matrix Format       ->	( j   ,   M  \t  i	\t value 
	 * 			  Vector Format					->	( j   ,   V  \t   value )
	 * 
	 * Output :   Key-Value Pairs               
	 * 			  Key ->   	i
	 * 			  Value -> 	M_ij * V_j  
	 * 						
	 */

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			
		
		double vVal = 0;
		ArrayList<String> mList = new ArrayList<String> ();
					
		// Loop through values, to add m_ij term to mList and save v_j to variable v_j
		// Then Iterate through the terms in mList, to multiply each term by variable v_j.
		// Each output is a key-value pair  ( i  ,   m_ij * v_j)
		
		for (Text val : values) {
			String strVal  = val.toString();
			String[] value = strVal.split("\t");
			
			if (value[0].equals("V")) {  // vector tuple ("V", value)
				vVal += Double.parseDouble(value[1]); // add value to vVal
			}
			else { // matrix tuple ("M", i, value)
				mList.add(strVal); // add to mList
			}
		}
		
		for (String val : mList) {
			String[] value = val.split("\t");
			String x = value[1];
			String y =  value[2];
			double vMult = vVal*Double.parseDouble(y);
			context.write( new Text(x) , new Text(Double.toString(vMult)));
		}
		
	}

}
