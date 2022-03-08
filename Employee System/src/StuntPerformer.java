public class StuntPerformer extends Performer{
    protected String[] realAuthorsId;
    protected String height;
    StuntPerformer(String id,String name,String surname,String country,String height,String[] realAuthorsId){
        super(id,name,surname,country);
        this.realAuthorsId=realAuthorsId;
        this.height=height;

    }
}

