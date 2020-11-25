
/*
//Programmer：Sisi Kang
//11/24/2020
 */

import java.util.ArrayList;
import java.util.Collections;

public class Node<T> extends ProbabilityGenerator<T>{
    ArrayList<T> tokenSequence; //record if it is the end of the word
    ArrayList<Node<T>> children;
    int count=1;
    boolean hasSeqAtEndOfDataset;
    double this_r;
    T nextToken;

    public Node(){
      tokenSequence = new ArrayList<>();
      children = new ArrayList<>();
    }

    public Node(ArrayList<T> input, boolean end, T next){//
    tokenSequence = new ArrayList<>(input);
    children = new ArrayList<>();
    hasSeqAtEndOfDataset = end;
    nextToken = next;
    }

    boolean addNode(Node<T> node) {
    boolean found = false; // whether the node has been added or not yet

    if (tokenSequence.equals(node.tokenSequence))
    {
      found = true;
      count++;
      if (!node.hasSeqAtEndOfDataset) {
          trainViaProbGen(node);
      }
    }
    else if( amIaSuffix(node) || (tokenSequence.size()==0)) //note that the empty sequence is always a suffix!
    {
      for (Node<T> c: children) {
        if (c.addNode(node)) {
          found = true;

        }
      }
      if (!found && tokenSequence.size() == node.tokenSequence.size()-1) {
        found = true;
        children.add(node);
        if (!node.hasSeqAtEndOfDataset) {
          node.trainViaProbGen(node);
        }
      }
    // 1.	try to add the node to all the children nodes.
    // 2.	Did one your child nodes add the node? **keep track of this via the found variable**
    // If NOT found and the length of node’s tokenSequence is one less than this tokenSequence Add the node to our children array. Thus- found=true.
    // children.add(node);
    }
//
//        System.out.println("sequence="+tokenSequence);
//        System.out.println("alphabet="+alphabet);
//        System.out.println("alphabet_counts="+alphabet_counts);
//        System.out.println("======");

    return found;

    }

    void print() {
    System.out.println(tokenSequence);
    for (Node<T> node: children) {
      node.print(1);
    }
    }

    void print(int numSpacesBefore) {
    for (int i=1; i<=numSpacesBefore; i++) {
      System.out.print(" ");
    }

    System.out.print("-->"); //if you like, it will be clearer print the token
    System.out.println(tokenSequence);
//    System.out.print(" ");
//    System.out.println(this_r);
    for (Node<T> node: children)
    {
      node.print(numSpacesBefore + 1);
    //each time you call this from the next child the number of spaces will increase by 1 node.print(numSpacesBefore + 1);
    }

    }


    boolean amIaSuffix(Node<T> node) {
    int len1 = tokenSequence.size();
    int len2 = node.tokenSequence.size();
    return tokenSequence.equals(node.tokenSequence.subList(len2 - len1, len2));
    // determines whether the tokenSequence of this node is a suffix of the tokenSequence of the input node Hint #1: using the sublist() method makes this much easier.
    // Hint #2: note the difference between .equals() and ==.

    // Hint #3: You MUST test this separately to make sure it works. That means calling it temporarily from the main class to make sure it works.

    // Nothing in your Node adding will work if this is incorrect and you cannot simply assume it is correct if you haven’t tested it.
    }

    boolean pMinElimination( int totalTokens, double pMin ) {
    //          System.out.println("sequenct="+tokenSequence);
    //          System.out.println("count="+count);
    //          System.out.println("total="+totalTokens);
    //          System.out.println("===========");
      double p = (double) count / totalTokens;
      boolean shouldRemove = p < pMin && tokenSequence.size() != 0;
      if (!shouldRemove) {
          for (int i=0; i<children.size(); i++) {
              Node<T> c = children.get(i);
              boolean flag = c.pMinElimination(totalTokens, pMin);
              if (flag) {
                  children.remove(c);
                  i--;
              }
          }
      }
      return shouldRemove;
    }

    boolean rElimination(double r, Node<T> myRoot) {
        boolean shouldRemove = tokenSequence.size() > 1;

        if (shouldRemove) {
            //Find the r of this node
            int max = Collections.max(alphabet_counts);
            double myRatio = (double) max / count;
//
//            System.out.println("sequence="+tokenSequence);
//            System.out.println("alphabet="+alphabet);
//            System.out.println("alphabet_counts="+alphabet_counts);
//            System.out.println("======");

            //Find the conditional probabilities for the root. You can decide to either check for the case for when there are multiple maximums or not

            //NOT checking –Option 1
            int tmp = 0;
            for (int i=0; i<alphabet_counts.size(); i++) {
                if (alphabet_counts.get(i) == max) {
                    tmp = Math.max(tmp, myRoot.getCountsAtToken(alphabet.get(i)));
                }
            }
            double rootRatio = (double) tmp / myRoot.count;

            //Note this is not optimized algorithm – I believe you could take time and exploit the ArrayList methods more than I did, but this works.

            this_r = myRatio / rootRatio;

            shouldRemove = this_r < r;
        }
        if (!shouldRemove) {
            for (int i=0; i<children.size(); i++) {
                Node<T> c = children.get(i);
                boolean flag = c.rElimination(r, this);
                if (flag) {
                    children.remove(c);
                    i--;
                }
            }
        }
        return shouldRemove;
    }

    T generate(ArrayList<T> initSeq) {
        T newToken = null; //the new token to return

        if (tokenSequence.equals(initSeq)) {
            return generate();
        } else if (tokenSequence.equals(initSeq.subList(initSeq.size()-tokenSequence.size(), initSeq.size()))) {
            for (Node<T> n : children) {
                newToken = n.generate(initSeq);
                if (newToken != null) {
                  return newToken;
                }
            }
        }

        return generate();
    }

    ArrayList<T> generate(ArrayList<T> initSeq, int length) {
        ArrayList<T> newSequence = new ArrayList<>();
        for(int i=0; i<length; i++)
        {
          newSequence.add(generate(initSeq));
        }
        return newSequence;
    }


    void trainViaProbGen(Node<T> node) {
        int index=alphabet.indexOf(node.nextToken);
        if(index<0){
            alphabet.add(node.nextToken);
            alphabet_counts.add(0);
            index=alphabet.size()-1;
        }
        alphabet_counts.set(index,alphabet_counts.get(index)+1);
    }

    int getCountsAtToken(T token) {
        int idx = alphabet.indexOf(token);
        return alphabet_counts.get(idx);
    }
}
