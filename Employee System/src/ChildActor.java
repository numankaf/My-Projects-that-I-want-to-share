public class ChildActor extends Performer{
    protected String age;
    ChildActor(String id,String name,String surname,String country,String age){
        super(id,name,surname,country);
        this.age=age;
    }
}
