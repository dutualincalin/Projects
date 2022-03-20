import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clasa task-urilor Map
 */
public class Map implements Runnable{
    private final ExecutorService tpe;
    private final AtomicInteger inQueue;
    private final String docName;
    private final int fragDim;
    private final int offset;

    public Map(ExecutorService tpe, AtomicInteger inQueue,
               String docName, int fragDim, int offset) {
        this.tpe = tpe;
        this.fragDim = fragDim;
        this.offset = offset;
        this.docName = docName;
        this.inQueue = inQueue;
    }

    @Override
    public void run() {
        int left, newOffset = offset;
        char letter;
        int empty = 0;
        StringBuilder word;

        // Declararea dicționarului
        MapDictionary dictionary = new MapDictionary(docName.replace("tests/files/", ""));


        // Declararea pattern-ului pentru a detecta
        // semne de punctuație, spatii, newline etc.
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]+");
        Matcher match;

        // Declararea, citirea fișierului de intrare și
        // setarea offset-ului
        RandomAccessFile file = null;

        try {
            file = new RandomAccessFile(new File(docName), "r");
            file.seek(offset);
        } catch (IOException e ) {
            e.printStackTrace();
        }

        // Declarație
        word = new StringBuilder();

        do{
            try {
                // Citirea unui char din fișier
                letter = (char) Objects.requireNonNull(file).readByte();
            }
            catch (EOFException e){
                // In caz de EOF, daca avem cuvânt
                // in word, il trecem in dicționar
                if(word.length() != 0) {
                   dictionary.addWord(word);
                }

                empty = 1;
                break;
            }
            catch (IOException e) {
                break;
            }

            // Se crește noul offset și se verifică tip-ul char-ului
            newOffset++;
            match = pattern.matcher(String.valueOf(letter));

            // Dacă char-ul nu e literă și dacă word are un cuvânt,
            // il bagă in dicționar, altfel dacă char-ul e o literă,
            // o adaugă în cuvânt
            if(match.find()) {
                if(word.length() != 0) {
                    dictionary.addWord(word);
                    word = new StringBuilder();
                }
            } else word.append(letter);

            match = pattern.matcher(String.valueOf(letter));
        }
        while(newOffset < (offset/fragDim + 1) * fragDim || !match.find());
        // While-ul se va efectua până când se va ajunge la dimensiunea maximă a fragmentului,
        // dar dacă fragmentul se oprește in mijlocul unui cuvânt, while-ul își va continua
        // execuția până când va da de ceva ce nu e literă.

        // Închiderea fișierului de intrare
        try {
            Objects.requireNonNull(file).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reținerea dicționarului nou format
        Tema2.mapDictionaries.add(dictionary);

        // Adăugarea unui nou task cu noul offset, numai
        // in cazul in care nu s-a ajuns la finalul fișierului.
        if(empty != 1) {
            inQueue.incrementAndGet();
            tpe.submit(new Map(tpe, inQueue, docName, fragDim, newOffset));
        }

        // Terminarea task-ului actual
        left  = inQueue.decrementAndGet();
        if(left == 0){
            tpe.shutdown();
        }
    }
}
