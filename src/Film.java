import java.util.HashMap;

public class Film {
    private String name,trailerPath,duration;
    private HashMap<String, Hall> halls = new HashMap<>(); // contains halls of a film
    private static HashMap<String,Film> films = new HashMap<>(); // contains all the films

    public Film(String name,String trailerPath,String duration){
        this.name = name;
        this.trailerPath = trailerPath;
        this.duration = duration;
        films.put(name,this); // adds the film to the hashmap of films
    }

    // to add a hall
    public void addHall(String[] hallInfo) {
        Hall hall = new Hall(hallInfo);
        halls.put(hall.getHallName(),hall); // adds the hall to the hashmap of halls
    }

    public static HashMap<String, Film> getFilms() {
        return films;
    }

    public HashMap<String, Hall> getHalls() {
        return halls;
    }

    public String getName() {
        return name;
    }

    public String getTrailerPath() {
        return trailerPath;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return name;
    }
}
