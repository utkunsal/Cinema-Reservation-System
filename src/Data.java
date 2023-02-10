import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Data {
    private static int maxErrorWithoutGettingBlocked, discountPercentage, blockTime;
    private static String title;


    // to initialize variables, users, films, halls, seats by reading properties and backup data
    public static void initialize(String backupFile, String propertiesFile){
        try {
            // creating an admin user
            User admin = new User("admin", User.hashPassword("password"), "true", "true");
            User.getUsers().put("admin",admin);

            // reading backup
            Scanner backup = new Scanner(new File(backupFile));
            while (backup.hasNextLine()){
                String line = backup.nextLine();
                String[] splittedLine = line.split("\t");
                if (line.startsWith("user")){
                    // to add users
                    new User(splittedLine[1],splittedLine[2],splittedLine[3],splittedLine[4]);
                } else if (line.startsWith("film")){
                    // to add films
                    new Film(splittedLine[1], splittedLine[2], splittedLine[3]);
                } else if (line.startsWith("hall")){
                    // to add halls
                    Film.getFilms().get(splittedLine[1]).addHall(splittedLine);
                } else if (line.startsWith("seat")){
                    // to add seats
                    Film.getFilms().get(splittedLine[1]).getHalls().get(splittedLine[2]).addSeat(splittedLine);
                }
            }
        backup.close();
        } catch (FileNotFoundException e) {
            // catchs the exception if there is no backup file, program runs without any errors
        }
        try {
            // reading properties
            Scanner properties = new Scanner(new File(propertiesFile));
            while (properties.hasNextLine()){
                String line = properties.nextLine();
                if (line.startsWith("maximum")){
                    maxErrorWithoutGettingBlocked = Integer.parseInt(line.split("=")[1]);
                } else if (line.startsWith("title")){
                    title = line.split("=")[1];
                } else if (line.startsWith("discount")){
                    discountPercentage = Integer.parseInt(line.split("=")[1]);
                } else if (line.startsWith("block")){
                    blockTime = Integer.parseInt(line.split("=")[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // to write the data to the backup file
    public static void backup(String fileName){
        try {
            FileWriter writer = new FileWriter(fileName);
            for (User user: User.getUsers().values()){
                writer.write("user\t"+user.getName()+"\t"+user.getPassword()+"\t"+
                        user.isClubMember() +"\t"+ user.isAdmin() +"\n");
            }
            for (Film film : Film.getFilms().values()){
                writer.write("film\t"+film.getName()+"\t"+film.getTrailerPath()+"\t"+film.getDuration()+"\n");
                for (Hall hall : film.getHalls().values()) {
                    writer.write("hall\t" + hall.getFilmName() + "\t" + hall.getHallName() + "\t" +
                            hall.getPricePerSeat() + "\t" + hall.getRow() + "\t" + hall.getColumn() + "\t" + hall.getDate() + "\n");
                    for (Seat seat : hall.getSeats().values()){
                        writer.write("seat\t"+seat.getFilmName()+"\t"+seat.getHallName()+"\t"+seat.getRowIndex()+
                                "\t"+seat.getColumnIndex()+ "\t"+seat.getOwnerName()+"\t"+seat.getPrice()+"\n");
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getMaxErrorWithoutGettingBlocked() {
        return maxErrorWithoutGettingBlocked;
    }

    public static int getDiscountPercentage() {
        return discountPercentage;
    }

    public static int getBlockTime() {
        return blockTime;
    }

    public static String getTitle() {
        return title;
    }
}
