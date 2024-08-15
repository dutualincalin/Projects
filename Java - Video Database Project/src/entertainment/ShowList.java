package entertainment;

import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.List;

public final class ShowList {
    private ArrayList<Show> shows;

    public ShowList() {
        shows = new ArrayList<>();
    }

    public ArrayList<Show> getSerials() {
        return shows;
    }

    public ShowList(final List<SerialInputData> serialsData) {
        this();
        int i;

        for (i = 0; i < serialsData.size(); i++) {
            Show show = new Show(serialsData.get(i).getTitle(), serialsData.get(i).getYear(),
                    serialsData.get(i).getCast(), serialsData.get(i).getGenres(),
                    serialsData.get(i).getNumberSeason(), serialsData.get(i).getSeasons());
            shows.add(show);
        }
    }
}
