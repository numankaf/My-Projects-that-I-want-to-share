public class Hero {
    public String name;
    public int hp;
    public int ap;
    public int maxMove;
    public int maxHp;
    Hero(String name){
        this.name=name;
    }
    public String printCurrent(){
        return name+"\t"+"("+hp+")";
    }
    @Override
    public String toString(){
        return name;
    }
    public void getDamage(int damage){
        if (damage>0){
            hp-=damage;
        }

    }
    public void getHeal(int heal,int maxHp){
        if (maxHp>=hp+heal){
            hp+=heal;
        }
    }
}
