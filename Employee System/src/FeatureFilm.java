import java.util.ArrayList;

public class FeatureFilm extends Film{
    protected String releaseDate;
    protected String budget;
    protected String[] writers;
    protected String[] genre;
    FeatureFilm(String id, String filmTitle, String language, String[] directors, String runtime, String country,String[] cast,
                String[] genre, String releaseDate, String[] writers, String budget){
        super(id,filmTitle,language,directors,runtime,country,cast);
        this.releaseDate=releaseDate;
        this.genre=genre;
        this.writers=writers;
        this.budget=budget;
    }
}
