public class ShortFilm extends Film{
    protected String releaseDate;
    protected String[] writers;
    protected String[] genre;
    ShortFilm(String id,String filmTitle,String language,String[] directors,String runtime,String country,String[] cast,
                String[] genre,String releaseDate,String[] writers){
        super(id,filmTitle,language,directors,runtime,country,cast);
        this.releaseDate=releaseDate;
        this.genre=genre;
        this.writers=writers;

    }
}
