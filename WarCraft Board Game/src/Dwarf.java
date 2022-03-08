public class Dwarf extends Calliance{
    Dwarf(String name){
        super(name);
        ap=Constants.dwarfAP;
        hp=Constants.dwarfHP;
        maxMove=Constants.dwarfMaxMove;
    }
    @Override
    public String printCurrent(){
        return name+"\t"+hp+"\t"+"("+Constants.dwarfHP+")";
    }
}