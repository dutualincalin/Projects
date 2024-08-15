package main;

import action.Action;
import actor.ActorList;
import checker.Checker;
import checker.Checkstyle;
import common.Constants;
import entertainment.MovieList;
import entertainment.ShowList;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.UserList;
import utils.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        ActorList actors = new ActorList(input.getActors());
        UserList users = new UserList(input.getUsers());
        MovieList movies = new MovieList(input.getMovies());
        ShowList serials = new ShowList(input.getSerials());
        List<ActionInputData> actions = input.getCommands();
        org.json.simple.JSONObject messenger = null;

        int i;
        Action action = new Action();
        ArrayList<Result> results = new ArrayList<>();
        Result result;

        for (i = 0; i < actions.size(); i++) {
            result = action.execute(actions.get(i), users.getUsers(), actors.getActors(),
                    movies.getMovies(), serials.getSerials());
            result.setId(i + 1);
            results.add(result);
        }

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        for (i = 0; i < results.size(); i++) {
            messenger = fileWriter.writeFile(results.get(i).getId(), results.get(i).getMessage(),
                    results.get(i).getMessage());

            arrayResult.add(messenger);
        }


        //TODO add here the entry point to your implementation

        fileWriter.closeJSON(arrayResult);
    }
}
