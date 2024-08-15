package utils;

/**
 * Clasa folosita pentru a scrie rezultatul unei actiuni.
 * @author Dutu Alin Calin
 */
public final class Result {
    private String message = "";
    private int id;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }
}
