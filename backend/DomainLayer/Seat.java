package DomainLayer;

public class Seat implements Cloneable{
    private Boolean occupied;
    private char row;
    private int seatNumber;
    public Seat(Boolean o, char r, int s) {
        occupied = o;
        row = r;
        seatNumber = s;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public char getRow() {
        return row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public void setRow(char row) {
        this.row = row;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
