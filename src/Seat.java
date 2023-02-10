public class Seat {

    private User owner;
    private int rowIndex, columnIndex, price, seatId;
    private String filmName, hallName;

    public Seat(String[] seatInfo){
        rowIndex = Integer.parseInt(seatInfo[3]);
        columnIndex = Integer.parseInt(seatInfo[4]);
        owner = (seatInfo[5].equals("null")) ? null:User.getUsers().get(seatInfo[5]);
        seatId = Integer.parseInt(rowIndex + String.valueOf(columnIndex));
        filmName = seatInfo[1];
        hallName = seatInfo[2];
        price = Integer.parseInt(seatInfo[6]);
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSeatId() {
        return seatId;
    }

    public String getOwnerName() {
        if (owner==null)
            return null;
        return owner.getName();
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getPrice() {
        return price;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getHallName() {
        return hallName;
    }


}
