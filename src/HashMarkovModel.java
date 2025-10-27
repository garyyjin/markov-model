import java.util.*;

public class HashMarkovModel extends BaseMarkovModel{
    private HashMap<List<String>, List<String>> myMap;

    public HashMarkovModel() {
        this(3);
    }

    public HashMarkovModel(int size) {
        super(size);
        myMap = new HashMap<>();
    }

    public void processTraining() {
        myMap.clear();
        for (int i = 0; i <= myWordSequence.size() - myModelSize; i++) {
            ArrayList<String> list = new ArrayList<>();
            myMap.putIfAbsent(myWordSequence.subList(i, i + myModelSize), list);
        }
        for (int i = 0; i <= myWordSequence.size() - myModelSize; i++) {
            if (i + myModelSize < myWordSequence.size()) {
                myMap.get(myWordSequence.subList(i, i + myModelSize)).add(myWordSequence.get(i + myModelSize));
            }
        }
    }

    public List<String> getFollows(List<String> context) {
        return myMap.get(context);
    }
}
