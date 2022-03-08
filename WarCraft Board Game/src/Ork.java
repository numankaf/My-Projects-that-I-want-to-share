public class Ork extends Zorde{
    public int orkHealPoint=Constants.orkHealPoints;
    Ork(String name){
        super(name);
        hp=Constants.orkHP;
        ap=Constants.orkAP;
        maxMove=Constants.orkMaxMove;
        maxHp=Constants.orkHP;
    }
    @Override
    public String printCurrent(){
        return name+"\t"+hp+"\t"+"("+Constants.orkHP+")";
    }

}
