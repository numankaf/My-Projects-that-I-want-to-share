public class Elf extends Calliance{
    public int elfRangedAP=Constants.elfRangedAP;
    Elf(String name){
        super(name);
        hp=Constants.elfHP;
        ap=Constants.elfAP;
        maxMove=Constants.elfMaxMove;
    }
    @Override
    public String printCurrent(){
        return name+"\t"+hp+"\t"+"("+Constants.elfHP+")";
    }

}
