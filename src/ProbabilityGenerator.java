
/*
//Programmer：Sisi Kang
//11/24/2020
 */


import java.util.ArrayList;


public class ProbabilityGenerator<T> {

	ArrayList<T> alphabet; //T is type of token coming into probability generator class
	ArrayList<Integer> alphabet_counts; //need to use numbers
	ArrayList<Double> pro;
	double[] proArr;
	
	
	ProbabilityGenerator()
	{
		alphabet = new ArrayList<T>();
		alphabet_counts = new ArrayList<Integer>();
		pro = new ArrayList<Double>();
	}
	
	//it is training probability generator with new data
	void train(ArrayList<T> newTokens) // new Token as a parameter
	{
	
	
		//you will code the training, find the probability of each note in the array

		for (int i=0; i < newTokens.size(); i++){


			int index=alphabet.indexOf(newTokens.get(i));
			if(index<0){
				alphabet.add(newTokens.get(i));
				alphabet_counts.add(0);
				index=alphabet.size()-1;
			}
			alphabet_counts.set(index,alphabet_counts.get(index)+1);
		}
		getPro();
		
	}

	void getPro() {
		pro.clear();
		int sum = 0;
		for (int v : alphabet_counts) {
			sum += v;  //sum of counts
		}
		for ( int v : alphabet_counts) {
			pro.add((double) v / sum); //probability
		}
		proArr = new double[pro.size()+1];
		for (int i=1; i<proArr.length; i++) {
			proArr[i] = proArr[i-1] + pro.get(i-1);
		}
	}

	public void printProbabilityDistribution() {
		System.out.println("-----------Probability Distribution----------------");
		for (int i=0; i<alphabet.size(); i++) {
			String s = "Token:" + alphabet.get(i) + "|Probability:" + pro.get(i);
			System.out.println(s);
		}
	}
	
	//generate one token
	T generate()
	{
		T newToken = null;
		//do something here
		double rand = Math.random();
		for (int i=0; i<pro.size(); i++) {
			if (rand < proArr[i+1]) {
				newToken = alphabet.get(i);
				break;
			}
		}

		return newToken;
	}
	
	//generate a lot of token
	ArrayList<T> generate( int length )
	{
		ArrayList<T> newSequence = new ArrayList<T>();
		for(int i=0; i<length; i++)
		{
			newSequence.add(generate());
			
		}
		return newSequence;
		
	}
	
}
