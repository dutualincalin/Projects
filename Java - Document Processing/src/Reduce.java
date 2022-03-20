import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clasa task-urilor Reduce
 */
public class Reduce implements Runnable{
    public final ExecutorService tpe;
    public final AtomicInteger inQueue;
    public final ReducedDictionary dictionary;

    public Reduce(ExecutorService tpe, AtomicInteger inQueue,
                  ReducedDictionary dictionary) {
        this.tpe = tpe;
        this.inQueue = inQueue;
        this.dictionary = dictionary;
    }

    /**
     * Calculează al n-lea element din șirul lui Fibonacci
     * @param nr - n-ul nostru
     * @return Numărul din șirul Fibonacci aflat pe poziția nr
     */
    float fibonacci(float nr){
        if(nr == 0 || nr == 1){
            return nr;
        }

        return fibonacci(nr - 1) + fibonacci(nr - 2);
    }

    @Override
    public void run() {
        /** Etapa de combinare **/

        // Declarații de variabile
        HashMap<Integer, Integer> wordStats = new HashMap<>();
        ArrayList<String> maxWords = new ArrayList<>();
        int max = 0, nrWords = 0;

        // Combină toate Hash-urile din dicționar într-unul singur
        for(HashMap<Integer, Integer> hash : dictionary.getStatistics()){
            for(int key : hash.keySet()) {
                if (!wordStats.containsKey(key)) {
                    wordStats.put(key, hash.get(key));
                } else {
                    wordStats.replace(key, wordStats.get(key) + hash.get(key));
                }

                nrWords += hash.get(key);
                if(max < key){
                    max = key;
                }
            }
        }

        // Face o listă a cuvintelor de lungime maxima,
        // lungimea fiind maximă pe întregul fișier
        for(String word : dictionary.getMaxWords()){
            if(word.length() == max){
                maxWords.add(word);
            }
        }

        /** Etapa de procesare **/

        // Declarație
        float value = 0;

        // Calcularea rang-ului fișierului
        for(Map.Entry<Integer, Integer> entry : wordStats.entrySet()){
            value += fibonacci(entry.getKey() + 1) * (float)entry.getValue() / (float)nrWords;
        }

        // Reținerea rezultatului
        Tema2.results.add(new ReduceResult(dictionary.getDocName(), value, max, maxWords.size()));

        // Eliminarea task-ului Reduce
        int left  = inQueue.decrementAndGet();
        if(left == 0){
            tpe.shutdown();
        }
    }
}
