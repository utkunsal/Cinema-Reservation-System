import java.util.HashMap;

public class Hall {
    private String hallName,filmName,date;
    private int row,column,pricePerSeat;
    private HashMap<Integer,Seat> seats = new HashMap<>(); // contains seats of a hall

    public Hall(String[] hallInfo){
        this.filmName = hallInfo[1];
        this.hallName = hallInfo[2];
        this.pricePerSeat = Integer.parseInt(hallInfo[3]);
        this.row = Integer.parseInt(hallInfo[4]);
        this.column = Integer.parseInt(hallInfo[5]);
        this.date = hallInfo[6];
    }

    // to add seats
    public void addSeat(String[] seatInfo){
        Seat seat = new Seat(seatInfo);
        seats.put(seat.getSeatId(),seat);
    }

    public String getFilmName() {
        return filmName;
    }

    public String getHallName() {
        return hallName;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPricePerSeat() {
        return pricePerSeat;
    }

    public HashMap<Integer, Seat> getSeats() {
        return seats;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return hallName + "  -  " + date;
    }


}
