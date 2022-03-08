public class Documentary extends Film{
    protected String releaseDate;

    Documentary(String id,String filmTitle,String language,String[] directors,String runtime,String country,String[] cast,
                String releaseDate){
        super(id,filmTitle,language,directors,runtime,country,cast);
        this.releaseDate=releaseDate;

    }
}
