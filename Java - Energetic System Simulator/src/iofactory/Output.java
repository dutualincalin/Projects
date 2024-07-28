package iofactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import output.LoaderOut;

import java.io.File;

/**
 * Subclasa a clasei abstracte IO care defineste actiunile input-ului.
 * Pentru crearea clasei s-a folosit Factory Pattern.
 * @author Dutu Alin Calin
 */
public final class Output extends IO {
    private final String path;

    public Output(final String path) {
        this.path = path;
    }

    /**
     * Preia datele de output si le parseaza in output.
     * @throws Exception Arunca exceptie in caz de esec.
     */
    @Override
    public void execute() throws Exception {
        LoaderOut loader = LoaderOut.getLoader();
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(path), loader);
    }
}
