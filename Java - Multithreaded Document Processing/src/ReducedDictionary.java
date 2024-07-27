import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clasa dicționarelor pentru task-urile Reduce
 */
public class ReducedDictionary {
    private final ArrayList<HashMap<Integer, Integer>> statistics;
    private final ArrayList<String> maxWords;
    private final String docName;

    public ReducedDictionary(String docName) {
        statistics = new ArrayList<>();
        maxWords = new ArrayList<>();
        this.docName = docName;
    }

    public ArrayList<HashMap<Integer, Integer>> getStatistics() {
        return statistics;
    }

    public ArrayList<String> getMaxWords() {
        return maxWords;
    }

    public String getDocName() {
        return docName;
    }

    /**
     * Adaugă Hash-ul si cuvintele maxime din dicționarul
     * unui fragment în dicționarul mare
     * @param dictionary Dicționarul unui fragment
     */
    public void addDictionary(MapDictionary dictionary){
        statistics.add(dictionary.getWordMap());
        maxWords.addAll(dictionary.getMaxStr());
    }
}
