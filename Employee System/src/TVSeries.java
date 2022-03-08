public class TVSeries extends Film{
    protected String startDate;
    protected String endDate;
    protected String[] genre;
    protected String[] writers;
    protected String numberOfSeasons;
    protected String numberOfEpisodes;
    TVSeries(String id,String filmTitle,String language,String[] directors,String runtime,String country,String[] cast,
             String[] genre,String[] writers,String startDate,String endDate,String numberOfSeasons,String numberOfEpisodes){
        super(id,filmTitle,language,directors,runtime,country,cast);
        this.startDate=startDate;
        this.endDate=endDate;
        this.writers=writers;
        this.genre=genre;
        this.numberOfEpisodes=numberOfEpisodes;
        this.numberOfSeasons=numberOfSeasons;

    }
}
