import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clasa dicționarelor pentru Map
 */
public class MapDictionary {
    private final HashMap<Integer, Integer> WordMap;
    private final ArrayList<String> MaxStr;
    private int max;
    private final String docName;

    public MapDictionary(String docName){
        this.docName = docName;
        WordMap = new HashMap<>();
        MaxStr = new ArrayList<>();
        max = 0;
    }

    public HashMap<Integer, Integer> getWordMap() {
        return WordMap;
    }

    public ArrayList<String> getMaxStr() {
        return MaxStr;
    }

    public String getDocName() {
        return docName;
    }

    /**
     * Adaugă un cuvânt in dicționar prin
     * adăugarea sau incrementarea unei intrări din
     * HashMap, urmată de adăugarea in lista de cuvinte
     * de lungime maximă, dacă e cazul.
     * @param word Cuvântul de adăugat.
     */
    public void addWord(StringBuilder word){
        int length = word.length();

        if(!WordMap.containsKey(length)){
            WordMap.put(length, 1);

            if(length > max){
                MaxStr.clear();
                MaxStr.add(word.toString());
                max = length;
            }
        }

        else{
            WordMap.replace(length, WordMap.get(length) + 1);

            if(length == max){
                MaxStr.add(word.toString());
            }
        }
    }
}
