package DomainLayer;

import javax.lang.model.type.NullType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Showtime {
    private Date showingDateTime;
    private ArrayList<ArrayList<Seat>> seatMap;

    private static Date convertDate(String dateTimeString) {
        // Define the date-time format
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeFormat.setLenient(false); // Ensure strict parsing

        try {
            // Parse the string into a Date object
            return dateTimeFormat.parse(dateTimeString);
        } catch (ParseException e) {
            // Handle the exception if the string is not a valid date-time
            System.err.println("Invalid date-time format: " + dateTimeString);
            e.printStackTrace();
            return null; // Return null or handle as needed
        }
    }

    public Showtime(String dateTime, int numRows, int maxSeatsPerRow) {
        showingDateTime = convertDate(dateTime);
        seatMap = new ArrayList<ArrayList<Seat>>(numRows);
        for (ArrayList<Seat> row: seatMap) {
            row = new ArrayList<>(maxSeatsPerRow);
        }
    }

    public Date getShowingDateTime() {
        return showingDateTime;
    }

    public String getShowingDateTimeString() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Format the Date object into the specified string format
        return dateTimeFormat.format(showingDateTime);
    }

    public String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(showingDateTime);
    }


    public String getTimeString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(showingDateTime);
    }

    public void setShowingDateTime(Date showingDateTime) {
        this.showingDateTime = showingDateTime;
    }

    public void addSeat(Seat newSeat) {
        int rowNumber = ((int)newSeat.getRow())-(int)'A';
        seatMap.get(rowNumber).set(newSeat.getSeatNumber(), newSeat);
    }

    public Seat getSeat(char row, int seatNumber) {
        int rowNumber = ((int)row)-(int)'A';
        try {
            return seatMap.get(rowNumber).get(seatNumber);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Attempted to access out of bounds");
            return null;
        }
    }

}
