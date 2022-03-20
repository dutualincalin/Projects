import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * mapDictionaries - rezultatul task-urilor de Map
 * result- rezultatul task-urilor de Reduce
 */
public class Tema2 {
    public static List<MapDictionary> mapDictionaries = Collections.synchronizedList(new ArrayList<>());
    public static List<ReduceResult> results = Collections.synchronizedList(new ArrayList<>());

    /**
     * Funcția principala care gestionează toate task-urile de Map urmate de cele de Reduce si
     * afișează rezultatul la final
     * @param args Argumentele funcției
     * @throws IOException Tratează majoritatea excepțiilor
     * @throws InterruptedException - Tratează excepțiile generate de funcțiile await
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int i, workers, fragDim, nrDocs;
        String input, output, docFile;

        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }

        // Preluarea argumentelor funcției
        workers = Integer.parseInt(args[0]);
        input = args[1];
        output = args[2];

        // Inițializare ExecutorService pentru Map
        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpeMap = Executors.newFixedThreadPool(workers);

        // Deschiderea fișierului de intrare
        Scanner file = null;
        try {
            file = new Scanner(new File(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Citirea parametrilor
        fragDim = Objects.requireNonNull(file).nextInt();
        nrDocs = file.nextInt();


        /** Etapa de Map **/

        // Pornirea task-urilor
        for(i = 0; i < nrDocs; i++) {
            inQueue.incrementAndGet();
            docFile = file.next();
            tpeMap.submit(new Map(tpeMap, inQueue, docFile, fragDim, 0));
        }

        // Așteaptă workerii să termine toate task-urile de Map
        tpeMap.awaitTermination(20, TimeUnit.SECONDS);


        /** Etapa de pregătire pentru Reduce **/

        // Declarații variabile
        ArrayList<ReducedDictionary> reduceTasks = new ArrayList<>();
        boolean isRD;

        // Adăugarea la grămadă dicționarelor fragmentelor in
        // dicționare pentru fișiere
        for(MapDictionary d : mapDictionaries){
            isRD = false;

            // Adăugarea dicționarului fragment in
            // dicționarul pentru fișier
            for(ReducedDictionary rd : reduceTasks){
                if (rd.getDocName().equals(d.getDocName())){
                    rd.addDictionary(d);
                    isRD = true;
                    break;
                }
            }

            // ...sau adăugarea într-un nou dicționar
            // pentru un fișier care nu avea dicționar
            if(!isRD){
                ReducedDictionary newRd = new ReducedDictionary(d.getDocName());
                newRd.addDictionary(d);
                reduceTasks.add(newRd);
            }
        }


        /** Etapa de Reduce **/

        // Inițializare ExecutorService pentru Reduce
        ExecutorService tpeReduce = Executors.newFixedThreadPool(workers);

        // Rularea task-urilor de Reduce
        for(ReducedDictionary rd : reduceTasks){
            inQueue.incrementAndGet();
            tpeReduce.submit(new Reduce(tpeReduce, inQueue, rd));
        }

        // Așteaptă workerii să termine toate task-urile de Reduce
        tpeReduce.awaitTermination(20, TimeUnit.SECONDS);


        /** Etapa de Output **/

        // Sortarea rezultatelor descrescător în funcție de rang
        results.sort(ReduceResult :: sortByRang);


        // Deschiderea fișierului de ieșire
        BufferedWriter out = new BufferedWriter(new FileWriter(output));
        StringBuilder string = new StringBuilder();

        // Scrierea în fișierul de ieșire a rezultatelor
        for(ReduceResult result : results) {
            string.append(result.getDocName())
                    .append(",")
                    .append(String.format(Locale.US, "%.2f", result.getRang()))
                    .append(",")
                    .append(result.getMaxLen())
                    .append(",")
                    .append(result.getMaxWordsNum());

            out.write(string.toString());
            out.newLine();
            string = new StringBuilder();
        }

        // închiderea fișierelor
        file.close();
        out.close();
    }
}
