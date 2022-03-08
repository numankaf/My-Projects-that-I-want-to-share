public class Human extends Calliance{
    Human(String name){
        super(name);
        hp=Constants.humanHP;
        ap=Constants.humanAP;
        maxMove=Constants.humanMaxMove;

    }
    @Override
    public String printCurrent(){
        return name+"\t"+hp+"\t"+"("+Constants.humanHP+")";
    }
}
