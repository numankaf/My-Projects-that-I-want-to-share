public class Director extends Artist{
    protected String agent;
    Director(String id,String name,String surname,String country,String agent){
        super(id,name,surname,country);
        this.agent=agent;
    }
}
