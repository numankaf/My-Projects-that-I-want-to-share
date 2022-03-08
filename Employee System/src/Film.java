import java.util.ArrayList;

public class Film {
    protected String id;
    protected String filmTitle;
    protected String language;
    protected String runtime;
    protected String country;
    protected String[] directors;
    protected String[] cast;
    Film(String id,String filmTitle,String language,String[] directors,String runtime,String country,String[] cast){
        this.id=id;
        this.country=country;
        this.cast=cast;
        this.filmTitle=filmTitle;
        this.language=language;
        this.runtime=runtime;
        this.directors=directors;

    }
}
