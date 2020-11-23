
//Description: Suffix Tree

import java.util.ArrayList;


public class MarkovGenerator<T> extends ProbabilityGenerator<T> {

	ArrayList<ArrayList<Integer>> transitionTable = new ArrayList<>();
	ArrayList<ArrayList<T>> uniqueAlphabetSequences = new ArrayList<>();
	ProbabilityGenerator<T> e;
	ArrayList<T> initSeq = null;
	int orderM;

	MarkovGenerator(int M){
		super(); //inheritance
		orderM = M; //variable, count the numbers
}

	
	T generate(ArrayList<T> initSeq) {
		int curSeqIndex = uniqueAlphabetSequences.indexOf(initSeq);
		int[] pro = new int[transitionTable.get(0).size()+1];
		int sum = 0;
		if (curSeqIndex < 0) {
			return e.generate();
		} else {
			ArrayList<Integer> correct_row = transitionTable.get(curSeqIndex);
			for (int i=0; i<correct_row.size(); i++) {
				sum += correct_row.get(i);
				pro[i+1] = pro[i] + correct_row.get(i);
			}
			if ( sum == 0) {
				return e.generate();
			} else {
				int rand = (int) (sum * Math.random());
				T newToken = null;
				for (int i=0; i<correct_row.size(); i++) {
					if (rand < pro[i+1]) {
						newToken = alphabet.get(i);
						break;
					}
				}
				return newToken;
			}
		}

	}
	
	void train(ArrayList<T> newTokens) {

		int rowIndex = -1;
		int tokenIndex = -1;
		for (int i = orderM - 1; i < newTokens.size() - 1; i++) {


			//sequence will be the container
			//You may do this in a for-loop or use .subList()
			ArrayList<T> curSequence = new ArrayList<T>(newTokens.subList(i + 1 - orderM, i + 1));
			//Find  curSequence in uniqueAlphabetSequences
			rowIndex = uniqueAlphabetSequences.indexOf(curSequence);
			if (rowIndex < 0) {
				rowIndex = uniqueAlphabetSequences.size();
				uniqueAlphabetSequences.add(curSequence);
				ArrayList<Integer> list = new ArrayList<>();
				for (int j = 0; j < alphabet.size(); j++) {
					list.add(0);
				}
				transitionTable.add(list);
			}

			//Find the current next token (tokenIndex)
			tokenIndex = alphabet.indexOf(newTokens.get(i + 1));
			if (tokenIndex < 0) {
				tokenIndex = alphabet.size();
				alphabet.add(newTokens.get(i + 1));
				for (ArrayList<Integer> arr : transitionTable) {
					arr.add(0);
				}
			}
			ArrayList<Integer> correct_row = transitionTable.get(rowIndex);
			correct_row.set(tokenIndex, correct_row.get(tokenIndex) + 1);
		}
	}
	

	
		
		/*
		int lastIndex = -1;
		for (int i=0; i < newTokens.size(); i++)
		{
			int tokenIndex = alphabet.indexOf(newTokens.get(i));
			if (tokenIndex<0)
			{
				tokenIndex = alphabet.size();
				ArrayList<Integer> row = new ArrayList<>();
				int j=0;
				while (j++<tokenIndex) {
					row.add(0);
				}
				transitionTable.add(row);
				for (ArrayList<Integer> r : transitionTable) {
					r.add(0);
				}
				alphabet.add(newTokens.get(i));
				
			}

			//ok, now add the counts to the transition table
			if(lastIndex > -1) //that is, we have a previous token so its not the 1st time thru
			{
				ArrayList<Integer> correct_row = transitionTable.get(lastIndex);
				correct_row.set(tokenIndex, correct_row.get(tokenIndex) + 1);
			}
			lastIndex = tokenIndex; //setting current to previous for next round
		}
		*/

	//when key pressed, generator, normalize to help print
	void norm() {
		System.out.println("                 "+alphabet);
		for (int i=0; i<transitionTable.size(); i++) {
			System.out.print(uniqueAlphabetSequences.get(i) + " ");
			int sum = 0;
			for (int v : transitionTable.get(i)) {
				sum += v;
			}
			for (int v : transitionTable.get(i)) {
				if (sum == 0) {
					System.out.print("0.0 ");
				} else {
					System.out.print((double) v / sum + " ");
				}
				
			}
			System.out.println( "\n");
		}
	}

	ArrayList<T> generate (int length)
	{
		int rand = (int) (uniqueAlphabetSequences.size() * Math.random());
		ArrayList<T> initSeq = new ArrayList<T>(uniqueAlphabetSequences.get(rand));
		return generate(length, initSeq);
	}

	ArrayList<T> generate (int length, ArrayList<T> initSeq)
	{
		ArrayList<T> newSequence = new ArrayList<T>();
		this.initSeq = initSeq;
		for (int i=0; i<length; i++)
		{
			T newToken = generate(this.initSeq);
			this.initSeq.remove(0);
			this.initSeq.add(newToken);
			newSequence.add(newToken);
		}
		return newSequence;
	}
	
}
