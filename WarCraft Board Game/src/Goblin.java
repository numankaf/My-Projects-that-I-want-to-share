public class Goblin extends  Zorde{
    Goblin(String name){
        super(name);
        hp=Constants.goblinHP;
        ap=Constants.goblinAP;
        maxMove=Constants.goblinMaxMove;
        maxHp=Constants.goblinHP;
    }
    public String printCurrent(){
        return name+"\t"+hp+"\t"+"("+Constants.goblinHP+")";
    }
}
