import java.io.IOException;
import java.util.*;

public class SimpleMarkovModel extends BaseMarkovModel  {

    public SimpleMarkovModel(int size){
        super(size);
    }

    public SimpleMarkovModel(){
        this(3);
    }

    /* You must implement getFollows to find and return a List<String> that contains each individual string that getFollows
    the parameter List<String> context. See the examples at the beginning of this document for details. You'll find
    every order-k (where k is myOrder) sequence in the instance variable mySequence using the subList method and
    compare it for equality with context, building the returned ArrayList with Strings that follow
    the context. You can model the code you write on BaseMarkovModel.differentContexts which finds every order-k subsequence
    and stores them in a local HashSet variable. */

    public List<String> getFollows(List<String> context) {
        List<String> list = new ArrayList<>();
        int len = context.size();
        for(int i = 0; i <= myWordSequence.size() - len; i++) {
            if ((myWordSequence.subList(i, i + len)).equals(context)) {
                if (i + len < myWordSequence.size()) {
                    list.add(myWordSequence.get(i + len));
                }
            }
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        SimpleMarkovModel mm = new SimpleMarkovModel(2);
        String dirName = "data/shakespeare";

        int size = 500;
        mm.trainDirectory(dirName);
        System.out.printf("seq size: %d\n",mm.getSequence().size());
        String text = mm.generate(size);

        System.out.printf("size of generated text = %d\n",text.length());
        System.out.printf("# different contexts possible = %d\n",mm.differentContexts());
    }
}
