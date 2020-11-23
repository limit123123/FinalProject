
//Description: Suffix Tree

import java.util.ArrayList;

public class Tree<T> {


    Node<T> root;//record the root
    int L;
    double Pmin;
    int totalInputTokens = 0;
    double r;
    /** Initialize your data structure here. */
    public Tree(int l, double Pmin, double r) {
        root = new Node<>();
        L = l;
        this.Pmin = Pmin;
        this.r = r;
    }

    void train(ArrayList<T> input) {
        for(int i = 1; i<=L ; i++) {
            for(int j = 0; j<input.size()-(i-1); j++) {
                ArrayList<T> curSequence = new ArrayList<>(input.subList(j, j+i));
                Node<T> theNewNode = new Node<>(curSequence, j+i == input.size(), j+i < input.size() ? input.get(j+i) : null);

                root.addNode(theNewNode);
                // curSequence = find the current sequence of size i
                // create a new node with the current sequence, theNewNode root.addNode(theNewNode)
            }
        }

        /*sum the total tokens in the input here(totalInputTokens)--
         * this is most easily done similarly to how we summed our total tokens in the ProbabilityGenerator.
         * root.pMinElimination( totalInputTokens, Pmin);
         */
        for (Node<T> c : root.children) {
            totalInputTokens += c.count;
        }
        root.pMinElimination( totalInputTokens, Pmin );
        root.rElimination( r, null );
    }


    void print() {
        root.print();
    }

    /* pMinElimination( int totalTokens, float pMin)
    *  {
    *  1.find the number of times that the sequence could have occurred( dependent on tokenSequence.size() )
    *   shouldRemove = empirical probability of the token sequence < pMin (note: handle the empty sequence/ root)
    *   if we should NOT remove this node
    *   {for each node (start from the end & go to the front of each array):
    *   call pMinElimination on all the children nodes
    *   if they return true (ie, we should remove the node)
    *   {
    *   then remove the entire node (which incl. its children)you may usethe ArrayList method.remove()
    *  		 }
    *  	 }
    *   4. return shouldRemove
    *   }
    */

    ArrayList<T> generate(int length) {
        for (Node<T> node : root.children) {
            root.alphabet.add(node.tokenSequence.get(0));
            root.alphabet_counts.add(node.count);
        }
        root.getPro();
        return root.generate(new ArrayList<>(), length);
    }

}
