public class Troll extends Zorde{
    Troll(String name){
        super(name);
        hp=Constants.trollHP;
        ap=Constants.trollAP;
        maxMove=Constants.trollMaxMove;
        maxHp=Constants.trollHP;
    }
    @Override
    public String printCurrent(){
        return name+"\t"+hp+"\t"+"("+Constants.trollHP+")";
    }
}
