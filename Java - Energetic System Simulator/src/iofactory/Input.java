package iofactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import input.LoaderIn;

import java.io.File;

/**
 * Subclasa a clasei abstracte IO care defineste actiunile output-ului.
 * Pentru crearea clasei s-a folosit Factory Pattern.
 * @author Dutu Alin Calin
 */
public final class Input extends IO {
    private final String path;

    public Input(final String path) {
        this.path = path;
    }

    /**
     * Preia datele de input si le pune intr-o variabila de tip LoaderIn.
     * @throws Exception Arunca exceptie in caz de esec;
     */
    @Override
    public void execute() throws Exception {
        ObjectMapper map = new ObjectMapper();
        LoaderIn.setLoader(map.readValue(new File(path), LoaderIn.class));
    }
}
