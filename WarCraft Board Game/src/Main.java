import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
//imported necessary liblaries

public class Main {
    //this is a method to open the file
    public  static String[] readFile(String arg){
        try{
            int i=0;
            int lenght= Files.readAllLines(Paths.get(arg)).size();
            String[] mylist=new String[lenght];
            for (String line:Files.readAllLines(Paths.get(arg))){
                mylist[i]=line;

                i++;
            }
            return mylist;

        }catch (IOException e){
            System.out.println("File not found "+arg);

            return null;
        }
    }
    //method for creating the board as Hero tpye
    public static Hero[][] createBoard(int x){
        Hero[][] myBoard=new Hero[x][x]; //board created
        for (Hero[] row: myBoard)
            Arrays.fill(row, null); //filling the board with null values
        return myBoard;
    }
    //this is the method for write the board's current station into the output file
    public static void printBoard(Object[][] board,ArrayList<Hero> heroes,FileWriter myWriter) throws IOException{
        int x=2*(board.length+1);
        String str="";
        for (int i=0;i<x;i++){
            str+="*";
        }
        //created str to make upper and lower line
        myWriter.write(str+"\n");
        //nested for loop to write the every element of board
        for(int i=0;i< board.length;i++){
            myWriter.write("*");
            for (int j=0;j< board.length;j++){
                if (board[i][j]==null){
                    myWriter.write("  ");
                }else{
                    myWriter.write(board[i][j].toString());
                }
            }
            myWriter.write("*");
            myWriter.write("\n");
        }

        myWriter.write(str+"\n");
        myWriter.write("\n");
        ArrayList<String> alliens=new ArrayList<>(); // new arraylist named alliens to sort the heroes
        for(Hero h:heroes){
            if (h.hp>0){
                alliens.add(h.printCurrent()); //adding to alliens by calling printCurrent method which is overrriden
                                                // in every child class of Hero, this is an polymorphism example.
            }
        }
        Collections.sort(alliens); //sorted alliens in ascending order
        for (String s:alliens){
            myWriter.write(s+"\n");
        }
        //writed the alliens elemens
        myWriter.write("\n");

    }
    //method to check deaths in every step
    public static void checkDeaths(ArrayList<Hero> heroes,Hero[][] board){
        //from heroes List  and  Hero [][] board,we are deleting the objects if hp value <=0
        for (int i=0;i< heroes.size();i++){
            if (heroes.get(i).hp<=0){
                heroes.remove(i);
            }
        }
        for (int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if(board[i][j]!=null&&board[i][j].hp<=0){
                    board[i][j]=null;
                }
            }
        }

    }

    public static void  dwarfMoves(Hero[][] board,Dwarf d,String[] moves,ArrayList<Hero> heroes)
    throws ArrayIndexOutOfBoundsException ,MoveCountException{
        //controlling the MoveCountException first
        if (moves.length!=Constants.dwarfMaxMove*2){
            throw new MoveCountException("Error : Move sequence contains wrong number of move steps. Input line ignored.");
        }
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if (board[i][j]!=null&&board[i][j].equals(d)){ //finding the desired dwarf
                    int locationX=j;
                    int locationY=i;
                    int x=0;
                    //now moving the dwarf
                    while (x<moves.length){
                        locationX+=Integer.parseInt(moves[x]);
                        locationY+=Integer.parseInt(moves[x+1]);
                        // if dwarf tries to go the cell occupied with its teemmate, then method is over
                        if(board[locationY][locationX] instanceof Calliance){
                            return;
                        }
                        //if dwarf tries to go the cell occupied with its enemy, method goes fight to death and then over.
                        if(board[locationY][locationX] instanceof Zorde){
                            fightToDeath(board,d,board[locationY][locationX],locationY,locationX,heroes);
                            return;
                        }
                        //changing the place of dwarf
                        board[locationY][locationX]=d;
                        board[locationY-Integer.parseInt(moves[x+1])][locationX-Integer.parseInt(moves[x])]=null;
                        //after movement, hits around the cells
                        hitAroundTheCells(board,d,locationY,locationX);
                        x+=2;

                    }
                    return;
                }
            }
        }
    }

    public static void orkMoves(Hero[][] board,Ork o,String[] moves,ArrayList<Hero> heroes)
            throws ArrayIndexOutOfBoundsException ,MoveCountException{
        //controlling the MoveCountException statement
        if (moves.length!=Constants.orkMaxMove*2){
            throw new MoveCountException("Error : Move sequence contains wrong number of move steps. Input line ignored.");
        }
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if (board[i][j]!=null&&board[i][j].equals(o)){
                    int locationX=j;
                    int locationY=i;
                    int x=0;
                    //ork founded
                    locationX+=Integer.parseInt(moves[x]);
                    locationY+=Integer.parseInt(moves[x+1]);
                    //this if statement is for ork heal, if ork want to go outside the board, then orkHeals will not executed,
                    //but if ork movement is avaliable in board,then it will executed.
                    if ((board[locationY][locationX] instanceof Hero)||(board[locationY][locationX] ==null)){
                        orkHeals(board,i,j,o);
                    }

                    if(board[locationY][locationX] instanceof Calliance){
                        fightToDeath(board,o,board[locationY][locationX],locationY,locationX,heroes);
                        return;
                    }
                    if(board[locationY][locationX] instanceof Zorde){
                        return;
                    }
                    board[locationY][locationX]=o;
                    board[locationY-Integer.parseInt(moves[x+1])][locationX-Integer.parseInt(moves[x])]=null;
                    hitAroundTheCells(board,o,locationY,locationX);
                    return;
                }
            }
        }
    }
    //all the other hero moves methods has the same algorithm
    public static void orkHeals(Hero[][] board,int i,int j,Ork o){
        //ork heals itself and all the 8 cell around itself if they are its teemmates
        o.getHeal(Constants.orkHealPoints,o.maxHp);
        try {
            if(board[i][j+1] instanceof Zorde){
                ((Zorde) board[i][j+1]).getHeal(Constants.orkHealPoints,board[i][j+1].maxHp);
            }
        }catch (IndexOutOfBoundsException e){

        }
        try{
            if(board[i+1][j+1] instanceof Zorde){
                ((Zorde) board[i+1][j+1]).getHeal(Constants.orkHealPoints,board[i+1][j+1].maxHp);
            }
        }catch (IndexOutOfBoundsException e){

        }
        try {
            if(board[i+1][j] instanceof Zorde){
                ((Zorde) board[i+1][j]).getHeal(Constants.orkHealPoints,board[i+1][j].maxHp);
            }
        }catch (IndexOutOfBoundsException e){

        }
        try {
            if(board[i-1][j+1] instanceof Zorde){
                ((Zorde) board[i-1][j+1]).getHeal(Constants.orkHealPoints,board[i-1][j+1].maxHp);
            }
        }catch (IndexOutOfBoundsException e){

        }
        try {
            if(board[i-1][j-1] instanceof Zorde){
                ((Zorde) board[i-1][j-1]).getHeal(Constants.orkHealPoints,board[i-1][j-1].maxHp);
            }
        }catch (IndexOutOfBoundsException e){

        }
        try {
            if(board[i-1][j] instanceof Zorde){
                ((Zorde) board[i-1][j]).getHeal(Constants.orkHealPoints,board[i-1][j].maxHp);
            }
        }catch (IndexOutOfBoundsException e){

        }
        try {
            if(board[i][j-1] instanceof Zorde){
                ((Zorde) board[i][j-1]).getHeal(Constants.orkHealPoints,board[i][j-1].maxHp);
            }
        }catch (IndexOutOfBoundsException e){

        }
        try {
            if(board[i+1][j-1] instanceof Zorde){
                ((Zorde) board[i+1][j-1]).getHeal(Constants.orkHealPoints,board[i+1][j-1].maxHp);
            }
        }catch (IndexOutOfBoundsException e){

        }

    }

    public static void humanMoves(Hero[][] board,Human h,String[] moves,ArrayList<Hero> heroes)
            throws ArrayIndexOutOfBoundsException,MoveCountException{
        if (moves.length!=Constants.humanMaxMove*2){
            throw new MoveCountException("Error : Move sequence contains wrong number of move steps. Input line ignored.");
        }
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if (board[i][j]!=null&&board[i][j].equals(h)){
                    int locationX=j;
                    int locationY=i;
                    int x=0;

                    while (x<moves.length){
                        locationX+=Integer.parseInt(moves[x]);
                        locationY+=Integer.parseInt(moves[x+1]);
                        if(board[locationY][locationX] instanceof Calliance){
                            return;
                        }
                        if(board[locationY][locationX] instanceof Zorde){
                            fightToDeath(board,h,board[locationY][locationX],locationY,locationX,heroes);
                            return;
                        }

                        board[locationY][locationX]=h;
                        board[locationY-Integer.parseInt(moves[x+1])][locationX-Integer.parseInt(moves[x])]=null;
                        x+=2;
                    }

                    hitAroundTheCells(board,h,locationY,locationX);
                    return;


                }
            }
        }
    }

    public static void  goblinMoves(Hero[][] board,Goblin g,String[] moves,ArrayList<Hero> heroes)
            throws ArrayIndexOutOfBoundsException ,MoveCountException{
        if (moves.length!=Constants.goblinMaxMove*2){
            throw new MoveCountException("Error : Move sequence contains wrong number of move steps. Input line ignored.");
        }
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if (board[i][j]!=null&&board[i][j].equals(g)){
                    int locationX=j;
                    int locationY=i;
                    int x=0;
                    System.out.println();
                    while (x<moves.length){
                        locationX+=Integer.parseInt(moves[x]);
                        locationY+=Integer.parseInt(moves[x+1]);
                        if(board[locationY][locationX] instanceof Calliance){
                            fightToDeath(board,g, board[locationY][locationX],locationY,locationX,heroes);

                            return;
                        }
                        if(board[locationY][locationX] instanceof Zorde){
                            return;
                        }
                        board[locationY][locationX]=g;
                        board[locationY-Integer.parseInt(moves[x+1])][locationX-Integer.parseInt(moves[x])]=null;
                        x+=2;
                        hitAroundTheCells(board,g,locationY,locationX);


                    }
                    return;
                }
            }
        }
    }

    public static void  elfMoves(Hero[][] board,Elf e,String[] moves,ArrayList<Hero> heroes)
            throws ArrayIndexOutOfBoundsException ,MoveCountException{
        if (moves.length!=Constants.elfMaxMove*2){
            throw new MoveCountException("Error : Move sequence contains wrong number of move steps. Input line ignored.");
        }
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if (board[i][j]!=null&&board[i][j].equals(e)){
                    int locationX=j;
                    int locationY=i;
                    int x=0;
                    while (x<moves.length){
                        locationX+=Integer.parseInt(moves[x]);
                        locationY+=Integer.parseInt(moves[x+1]);
                        if(board[locationY][locationX] instanceof Calliance){
                            return;
                        }
                        if(board[locationY][locationX] instanceof Zorde){
                            fightToDeath(board,e,board[locationY][locationX],locationY,locationX,heroes);
                            return;

                        }
                        board[locationY][locationX]=e;
                        board[locationY-Integer.parseInt(moves[x+1])][locationX-Integer.parseInt(moves[x])]=null;
                        x+=2;
                        if (x==moves.length){
                            elfmakesRangeAttack(board,e,locationY,locationX);
                        }else{
                            hitAroundTheCells(board,e,locationY,locationX);
                        }

                    }
                    return;
                }
            }
        }
    }

    public static void trollMoves (Hero[][] board, Troll t, String[] moves, ArrayList<Hero> heroes)
            throws ArrayIndexOutOfBoundsException ,MoveCountException{
        if (moves.length!=Constants.trollMaxMove*2){
            throw new MoveCountException("Error : Move sequence contains wrong number of move steps. Input line ignored.");
        }
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if (board[i][j]!=null&&board[i][j].equals(t)){
                    int locationX=j;
                    int locationY=i;
                    int x=0;

                    locationX+=Integer.parseInt(moves[x]);
                    locationY+=Integer.parseInt(moves[x+1]);
                    if(board[locationY][locationX] instanceof Calliance){
                        fightToDeath(board,t,board[locationY][locationX],locationY,locationX,heroes);
                        return;
                    }
                    if(board[locationY][locationX] instanceof Zorde){
                        return;
                    }
                    board[locationY][locationX]=t;
                    board[locationY-Integer.parseInt(moves[x+1])][locationX-Integer.parseInt(moves[x])]=null;

                    hitAroundTheCells(board,t,locationY,locationX);
                    return;


                }
            }
        }
    }
    //methods for figth to death
    public static void removeTheHero(Hero h,Hero[][] board){
        for (int i=0;i<board.length;i++){
            for (int j=0;j<board.length;j++){
                if (board[i][j]!=null&&board[i][j].equals(h)){
                    board[i][j]=null;
                }
            }
        }
    }

    public static void removeTheHeroFromList(Hero h,ArrayList<Hero> heroes){
        for (int i=0;i<heroes.size();i++){
            if(heroes.get(i).equals(h)){
                heroes.remove(i);
            }
        }
    }

    public static void fightToDeath(Hero[][] board,Hero attacker,
            Hero defender,int defenderY,int defenderX,ArrayList<Hero> heroes){
        defender.getDamage(attacker.ap); //first decrease defender hp,then compare their hp
        if(defender.hp>attacker.hp){
            defender.getDamage(attacker.hp);
            removeTheHero(defender,board);
            board[defenderY][defenderX]=defender;
            removeTheHero(attacker,board);
            removeTheHeroFromList(attacker,heroes);
            return;
        }
        if(defender.hp<attacker.hp){
            attacker.getDamage(defender.hp);
            removeTheHero(attacker,board);
            board[defenderY][defenderX]=attacker;
            removeTheHero(defender,board);
            removeTheHeroFromList(defender,heroes);
            return;

        }
        if(defender.hp==attacker.hp){
            removeTheHero(defender,board);
            removeTheHero(attacker,board);
            removeTheHeroFromList(defender,heroes);
            removeTheHeroFromList(attacker,heroes);
            return;

        }

    }
    // method to hit all the 8 cell around the hero
    public static   void hitAroundTheCells(Hero[][] board,Hero h,
    int locationY,int locationX){

        if (h instanceof Calliance){
            try {
                if(board[locationY][locationX + 1] instanceof Zorde){
                    ((Zorde) board[locationY][locationX+1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }

            try{
                if(board[locationY+1][locationX+1] instanceof Zorde){
                    ((Zorde) board[locationY+1][locationX+1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try {
                if(board[locationY+1][locationX] instanceof Zorde){
                    ((Zorde) board[locationY+1][locationX]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try {
                if(board[locationY-1][locationX+1] instanceof Zorde){
                    ((Zorde) board[locationY-1][locationX+1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try{
                if(board[locationY-1][locationX-1] instanceof Zorde){
                    ((Zorde) board[locationY-1][locationX-1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try{
                if(board[locationY-1][locationX] instanceof Zorde){
                    ((Zorde) board[locationY-1][locationX]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try{
                if(board[locationY][locationX-1] instanceof Zorde){
                    ((Zorde) board[locationY][locationX-1]).getDamage(h.ap);
                }

            }catch (ArrayIndexOutOfBoundsException e){

            }
            try{
                if(board[locationY+1][locationX-1] instanceof Zorde){
                    ((Zorde) board[locationY+1][locationX-1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
        }
        if (h instanceof Zorde){
            try {
                if(board[locationY][locationX + 1] instanceof Calliance){
                    ((Calliance) board[locationY][locationX+1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }

            try{
                if(board[locationY+1][locationX+1] instanceof Calliance){
                    ((Calliance) board[locationY+1][locationX+1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try {
                if(board[locationY+1][locationX] instanceof Calliance){
                    ((Calliance) board[locationY+1][locationX]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try {
                if(board[locationY-1][locationX+1] instanceof Calliance){
                    ((Calliance) board[locationY-1][locationX+1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try{
                if(board[locationY-1][locationX-1] instanceof Calliance){
                    ((Calliance) board[locationY-1][locationX-1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try{
                if(board[locationY-1][locationX] instanceof Calliance){
                    ((Calliance) board[locationY-1][locationX]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
            try{
                if(board[locationY][locationX-1] instanceof Calliance){
                    ((Calliance) board[locationY][locationX-1]).getDamage(h.ap);
                }

            }catch (ArrayIndexOutOfBoundsException e){

            }
            try{
                if(board[locationY+1][locationX-1] instanceof Calliance){
                    ((Calliance) board[locationY+1][locationX-1]).getDamage(h.ap);
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
        }

    }
    //elf range attack method to hit 24 cell around itself
    public static void elfmakesRangeAttack(Hero[][] board,Hero h,
                                           int locationY,int locationX){
        hitAroundTheCells(board,h,locationY,locationX);
        try {
            if(board[locationY+2][locationX + 2] instanceof Zorde){
                ((Zorde) board[locationY+2][locationX + 2]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }

        try{
            if(board[locationY+2][locationX+1] instanceof Zorde){
                ((Zorde) board[locationY+2][locationX+1]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try {
            if(board[locationY+1][locationX+2] instanceof Zorde){
                ((Zorde) board[locationY+1][locationX+2]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try {
            if(board[locationY][locationX+2] instanceof Zorde){
                ((Zorde) board[locationY][locationX+2]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try{
            if(board[locationY-2][locationX+2] instanceof Zorde){
                ((Zorde)board[locationY-2][locationX+2]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try{
            if(board[locationY-1][locationX+2] instanceof Zorde){
                ((Zorde) board[locationY-1][locationX+2]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try{
            if(board[locationY-2][locationX+1] instanceof Zorde){
                ((Zorde) board[locationY-2][locationX+1]).getDamage(h.ap);
            }

        }catch (ArrayIndexOutOfBoundsException e){

        }
        try{
            if(board[locationY][locationX-2] instanceof Zorde){
                ((Zorde) board[locationY][locationX-2]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }

        try{
            if(board[locationY-2][locationX-2] instanceof Zorde){
                ((Zorde) board[locationY-2][locationX-2]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try {
            if(board[locationY-2][locationX-1] instanceof Zorde){
                ((Zorde) board[locationY-2][locationX-1]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try {
            if(board[locationY-1][locationX-2] instanceof Zorde){
                ((Zorde) board[locationY-1][locationX-2]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try{
            if(board[locationY-2][locationX] instanceof Zorde){
                ((Zorde) board[locationY-2][locationX]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try{
            if(board[locationY+2][locationX-2] instanceof Zorde){
                ((Zorde) board[locationY+2][locationX-2]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try{
            if(board[locationY+1][locationX-2] instanceof Zorde){
                ((Zorde) board[locationY+1][locationX-2]).getDamage(h.ap);
            }

        }catch (ArrayIndexOutOfBoundsException e){

        }
        try{
            if(board[locationY+2][locationX-1] instanceof Zorde){
                ((Zorde) board[locationY+2][locationX-1]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
        try {
            if(board[locationY+2][locationX ] instanceof Zorde){
                ((Zorde) board[locationY+2][locationX ]).getDamage(h.ap);
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }

    }
    // clearing the output file with this method before running
    public static void clearTheFile(String name) throws IOException {
        FileWriter file1 = new FileWriter(name, false);
        PrintWriter file2 = new PrintWriter(file1, false);
        file2.flush();
        file2.close();
        file1.close();
    }
    //gets the coordinates of hero by using board and name as an integer array with lenght 2
    public static int[] getCooordinates(Hero[][] board,String s){
        int[] result=new int[2];
        for (int i=0;i< board.length;i++){
            for(int j=0;j< board.length;j++){
                if (board[i][j]!=null&&board[i][j].name.equals(s)){
                    result[0]=i;
                    result[1]=j;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws  IOException{
        clearTheFile(args[2]);
        FileWriter myWriter = new FileWriter(args[2],true);
        Hero[][] board=createBoard(0);
        String[] initials=readFile(args[0]);
        String[] commands=readFile(args[1]);
        //all the files are opened
        ArrayList<Hero> heroes=new ArrayList<>();
        //created an arraylist named heroes,then add elements in it by using polymorphism
        for (int i=0;i<initials.length;i++){
            if (initials[i].equals("BOARD")){
                String[] boardX=initials[i+1].split("x");
                board=createBoard(Integer.parseInt(boardX[0])); //board created
            }
            if (initials[i].equals("CALLIANCE")){
                int x=i+1;
                while (x<initials.length&&!initials[x].isEmpty()){
                    String[] allien=initials[x].split(" ");
                    if (allien[0].equals("ELF")){
                        Elf e=new Elf(allien[1]);
                        board[Integer.parseInt(allien[3])][Integer.parseInt(allien[2])]=e;
                        heroes.add(e);
                    }
                    if (allien[0].equals("DWARF")){
                        Dwarf d=new Dwarf(allien[1]);
                        board[Integer.parseInt(allien[3])][Integer.parseInt(allien[2])]=d;
                        heroes.add(d);
                    }
                    if (allien[0].equals("HUMAN")){
                        Human h=new Human(allien[1]);
                        board[Integer.parseInt(allien[3])][Integer.parseInt(allien[2])]=h;
                        heroes.add(h);
                    }
                    x+=1;
                }
            }
            if (initials[i].equals("ZORDE")){
                int x=i+1;
                while (x<initials.length&&!initials[x].isEmpty()){
                    String[] allien=initials[x].split(" ");
                    if (allien[0].equals("ORK")){
                        Ork o =new Ork(allien[1]);
                        board[Integer.parseInt(allien[3])][Integer.parseInt(allien[2])]=o;
                        heroes.add(o);
                    }
                    if (allien[0].equals("TROLL")){
                        Troll t=new Troll(allien[1]);
                        board[Integer.parseInt(allien[3])][Integer.parseInt(allien[2])]=t;
                        heroes.add(t);
                    }
                    if (allien[0].equals("GOBLIN")){
                        Goblin g=new Goblin(allien[1]);
                        board[Integer.parseInt(allien[3])][Integer.parseInt(allien[2])]=g;
                        heroes.add(g);
                    }
                    x+=1;
                }
            }
        }
        //all the heroes are added and writing the first look of board
        printBoard(board,heroes,myWriter);

        for(String s:commands){
            String[] lst=s.split(" ");
            String[] moves =lst[1].split(";");
            int[] list1=getCooordinates(board,lst[0]);
            for (int i=0;i<heroes.size();i++){
                try{
                    if (heroes.get(i) instanceof Dwarf && heroes.get(i).name.equals(lst[0])){

                        try{
                            dwarfMoves(board,(Dwarf) heroes.get(i),moves,heroes);
                            checkDeaths(heroes,board);
                            printBoard(board,heroes,myWriter);
                        }catch (MoveCountException m){
                            myWriter.write("\nError : Move sequence contains wrong number of move steps. Input line ignored.\n");
                        }catch (ArrayIndexOutOfBoundsException e){
                            int[] list2=getCooordinates(board,lst[0]);
                            if(!(list1[0]==list2[0]&&list1[1]==list2[1])){
                                printBoard(board,heroes,myWriter);
                            }

                            throw new BoundaryException("\nError : Game board boundaries are exceeded. Input line ignored.\n",myWriter);
                        }
                    }

                    if (heroes.get(i) instanceof Ork && heroes.get(i).name.equals(lst[0])){
                        try{
                            orkMoves(board,(Ork) heroes.get(i),moves,heroes);
                            checkDeaths(heroes,board);
                            printBoard(board,heroes,myWriter);
                        }catch (MoveCountException m){

                            myWriter.write("\nError : Move sequence contains wrong number of move steps. Input line ignored.\n");
                        }catch (ArrayIndexOutOfBoundsException e){
                            int[] list2=getCooordinates(board,lst[0]);
                            if(!(list1[0]==list2[0]&&list1[1]==list2[1])){
                                printBoard(board,heroes,myWriter);
                            }

                            throw new BoundaryException("\nError : Game board boundaries are exceeded. Input line ignored.\n",myWriter);
                        }
                    }
                    if (heroes.get(i) instanceof Human && heroes.get(i).name.equals(lst[0])){

                        try{
                            humanMoves(board,(Human) heroes.get(i),moves,heroes);
                            checkDeaths(heroes,board);
                            printBoard(board,heroes,myWriter);

                        }catch (ArrayIndexOutOfBoundsException e){
                            int[] list2=getCooordinates(board,lst[0]);
                            if(!(list1[0]==list2[0]&&list1[1]==list2[1])){
                                printBoard(board,heroes,myWriter);
                            }
                            throw new BoundaryException("\nError : Game board boundaries are exceeded. Input line ignored.\n",myWriter);
                        } catch (MoveCountException m){
                            myWriter.write("\nError : Move sequence contains wrong number of move steps. Input line ignored.\n");
                        }

                    }

                    if (heroes.get(i) instanceof Troll && heroes.get(i).name.equals(lst[0])){
                        try{
                            trollMoves(board,(Troll) heroes.get(i),moves,heroes);
                            checkDeaths(heroes,board);
                            printBoard(board,heroes,myWriter);
                        }catch (ArrayIndexOutOfBoundsException e){
                            int[] list2=getCooordinates(board,lst[0]);
                            if(!(list1[0]==list2[0]&&list1[1]==list2[1])){
                                printBoard(board,heroes,myWriter);
                            }

                            throw new BoundaryException("\nError : Game board boundaries are exceeded. Input line ignored.\n",myWriter);
                        }catch (MoveCountException m){
                            myWriter.write("\nError : Move sequence contains wrong number of move steps. Input line ignored.\n");
                        }

                    }
                    if (heroes.get(i) instanceof Goblin && heroes.get(i).name.equals(lst[0])){
                        try{
                            goblinMoves(board,(Goblin) heroes.get(i),moves,heroes);
                            checkDeaths(heroes,board);
                            printBoard(board,heroes,myWriter);
                        }catch (MoveCountException m){
                            myWriter.write("\nError : Move sequence contains wrong number of move steps. Input line ignored.\n");
                        }catch (ArrayIndexOutOfBoundsException e){
                            int[] list2=getCooordinates(board,lst[0]);
                            if(!(list1[0]==list2[0]&&list1[1]==list2[1])){
                                printBoard(board,heroes,myWriter);
                            }
                            throw new BoundaryException("\nError : Game board boundaries are exceeded. Input line ignored.\n",myWriter);
                        }
                    }

                    if (heroes.get(i) instanceof Elf && heroes.get(i).name.equals(lst[0])){
                        try{
                            elfMoves(board,(Elf) heroes.get(i),moves,heroes);
                            checkDeaths(heroes,board);
                            printBoard(board,heroes,myWriter);
                        }catch (MoveCountException m){
                            myWriter.write("\nError : Move sequence contains wrong number of move steps. Input line ignored.\n");
                        }catch (ArrayIndexOutOfBoundsException e){
                            int[] list2=getCooordinates(board,lst[0]);
                            if(!(list1[0]==list2[0]&&list1[1]==list2[1])){
                                printBoard(board,heroes,myWriter);
                            }
                            throw new BoundaryException("\nError : Game board boundaries are exceeded. Input line ignored.\n",myWriter);
                        }

                    }
                }catch ( Exception e){
                    continue;
                }
            }
        }
        checkDeaths(heroes,board);
        myWriter.write("\nGame Finished\n");
        for (Hero h:heroes){
            if (h instanceof Zorde){
                myWriter.write("Zorde Wins");
            }
            if (h instanceof Calliance){
                myWriter.write("Calliance Wins");
            }
        }
        myWriter.close();
    }
}
