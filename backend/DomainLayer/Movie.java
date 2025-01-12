package DomainLayer;

import java.util.ArrayList;
import java.util.Date;

public class Movie {
    private String title;
    private String posterFileName;
    private ArrayList<ArrayList<Showtime>> showtimes; // list of showtimes by date, then by time

    public Movie(String title, String posterFileName) {
        this.title = title;
        this.posterFileName = posterFileName;
        this.showtimes = new ArrayList<>();
    }

    public ArrayList<ArrayList<Showtime>> getShowtimes() {
        return showtimes;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterFileName() {
        return posterFileName;
    }

    public void addShowtime(Showtime newShowtime) {
        Date newShowtimeDate = newShowtime.getShowingDateTime();
        String newShowtimeDateString = newShowtime.getDateString();

        // Find the correct position for the new date in the outer list
        int outerIndex = 0;
        while (outerIndex < showtimes.size() &&
                showtimes.get(outerIndex).get(0).getDateString().compareTo(newShowtimeDateString) < 0) {
            outerIndex++;
        }

        // Check if the date already exists
        if (outerIndex < showtimes.size() &&
                showtimes.get(outerIndex).get(0).getDateString().equals(newShowtimeDateString)) {
            // Insert into the existing inner list, sorted by time
            ArrayList<Showtime> innerList = showtimes.get(outerIndex);
            int innerIndex = 0;
            while (innerIndex < innerList.size() &&
                    innerList.get(innerIndex).getShowingDateTime().compareTo(newShowtimeDate) < 0) {
                innerIndex++;
            }
            innerList.add(innerIndex, newShowtime); // Insert at the correct position
        } else {
            // Create a new inner list for the date
            ArrayList<Showtime> newInnerList = new ArrayList<>();
            newInnerList.add(newShowtime);
            showtimes.add(outerIndex, newInnerList); // Insert at the correct position in the outer list
        }

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterFileName(String posterFileName) {
        this.posterFileName = posterFileName;
    }
}
