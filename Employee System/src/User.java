import java.util.ArrayList;

public class User extends Person{
    protected  ArrayList<String> ratedMovies=new ArrayList<String>();
    protected ArrayList<Integer> ratedMoviesPoints=new ArrayList<Integer>();
    User(String id,String name,String surname,String country){
        super(id,name,surname,country);
    }
    public void rateTheMovie(String id,int rate){
        //simple method ,adding the id to ratedMovies, rate to ratedMoviesPoints
        this.ratedMovies.add(id);
        this.ratedMoviesPoints.add(rate);

    }
    public void editTheRate(int j,int newpoint){
        //changing the ratedpoint
        this.ratedMoviesPoints.set(j,newpoint);

    }

}
