import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//imported necessary classes

public class Main {
    //methods to read txt in different formats
    public  static ArrayList<String> readFileAsArrayList(String arg) {
        try {

            int lenght = Files.readAllLines(Paths.get(arg)).size();
            ArrayList<String> mylist = new ArrayList<String>();
            for (String line : Files.readAllLines(Paths.get(arg))) {
                if (!line.isEmpty()){
                    mylist.add(line);
                }
            }
            return mylist;

        } catch (IOException e) {
            System.out.println("File not found");
            return null;
        }
    }
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
            System.out.println("File not found"+arg);

            return null;
        }


    }
    //All the commands methods
    public static void rate (ArrayList<User> users,String userId,String movie,int point,ArrayList<String> list,String filename) throws IOException{
        FileWriter myWriter = new FileWriter(filename,true);//opennning the output file
        myWriter.write("RATE\t"+userId+"\t"+movie+"\t"+point+"\n");//writing the command
        myWriter.write("\n");
        boolean found=true;  //bool to catch the failed situation
        for (int i=0;i< users.size();i++) {  //first for loop for the ArrayList user
            if ((users.get(i).id.equals(userId))) { //first if  for the wanted situation
                if ((users.get(i)!=null)){
                    if (!(users.get(i).ratedMovies.contains(movie))) {  //second if for the rate if it is done before or not

                        for (int j = 0; j < list.size(); j++) {
                            String[] ls = list.get(j).split("\t"); //splitting the command block

                            if (ls[1].equals(movie)) { //third if for the equality
                                users.get(i).rateTheMovie(movie, point); //rating the film by user rateTheMovie method
                                found = false; //making bool false means that command is successfull, no error
                                ls[0] = ls[0].replace(":", " ");
                                //writing to output file in correct format
                                myWriter.write("Film rated successfully" + "\n");
                                myWriter.write("Film type: " + ls[0] + "\n");
                                myWriter.write("Film title: " + ls[2] + "\n");
                                myWriter.write("\n");
                                myWriter.write("-----------------------------------------------------------------------------------------------------" + "\n");

                                break; //break for the for loop
                            }
                        }
                    } else { // else block for second if
                        //writing the output in correct format
                        myWriter.write("This film was earlier rated" + "\n");
                        myWriter.write("\n");
                        myWriter.write("-----------------------------------------------------------------------------------------------------" + "\n");
                        found = false;
                        break; //break
                    }
                }
            }
        }


        //if found is true , that means our command is failed
        if (found){
            //writing the output in correct format
            myWriter.write("Command Failed"+"\n");
            myWriter.write("User ID: "+userId+"\n");
            myWriter.write("Film ID: "+movie+"\n");
            myWriter.write("\n");
            myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        }
    myWriter.close(); //closing the output file
    }//end of the method
    public static  void add(ArrayList<FeatureFilm> featureFilms,String[] ls,String command,ArrayList<Film> allTheFilms,
                            ArrayList<String> people,String filename) throws IOException{
        FileWriter myWriter = new FileWriter(filename,true); //open the output file
        for (int i=0;i<featureFilms.size();i++){ //for loop for feature films
            if (featureFilms.get(i).id.equals(ls[2])){ //if statement to understand this feature film alreayd exist or not
                //writing the output in correct format
                myWriter.write(command+"\n");
                myWriter.write("\n");
                myWriter.write("Command Failed"+"\n");
                myWriter.write("Film ID: "+ls[2]+"\n");
                myWriter.write("Film title: "+ls[3]+"\n");
                myWriter.write("\n");
                myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
                myWriter.close();
                return; //ending the method so that method is over here
            }
        }

        //preparing the array attributes of feature films by using the argument
        String[] drcts=ls[5].split(",");
        String[] prfrms=ls[8].split(",");
        String[] gnres=ls[9].split(",");
        String[] wrts=ls[11].split(",");
        boolean bool1=false;// three bool to cathc the failed situation in direcors ,performers,writers
        boolean bool2=false;
        boolean bool3=false;
        //each nested for loops have the same algorithm and making their own bools true
        for (int i=0;i<people.size();i++){
            for (int j=0;j<drcts.length;j++){
                if (!people.contains(drcts[j])){
                   bool1=true;
                }
            }
            for (int j=0;j<prfrms.length;j++){
                if (!people.contains(prfrms[j])){
                    bool2=true;
                }
            }
            for (int j=0;j<wrts.length;j++){
                if (!people.contains(wrts[j])){
                    bool3=true;
                }
            }
        }
        //if any bool is true , then there is an failed situation
        if (bool1||bool2||bool3){
            //writing in correct format
            myWriter.write(command+"\n");
            myWriter.write("\n");
            myWriter.write("Command Failed"+"\n");
            myWriter.write("Film ID: "+ls[2]+"\n");
            myWriter.write("Film title: "+ls[3]+"\n");
            myWriter.write("\n");
            myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
            myWriter.close();
            return; //return for ending the method so that method is finished here
        }
        //if method not finished and and everyhing is fine , then
        //writing the successful form of output
        myWriter.write(command+"\n");
        myWriter.write("\n");
        myWriter.write("FeatureFilm added successfully"+"\n");
        myWriter.write("Film ID: "+ls[2]+"\n");
        myWriter.write("Film title: "+ls[3]+"\n");
        featureFilms.add(new FeatureFilm(ls[2],ls[3],ls[4],drcts,ls[6],ls[7],prfrms,gnres,ls[10],
                wrts,ls[12])); //adding to featureFilms
        allTheFilms.add(new FeatureFilm(ls[2],ls[3],ls[4],drcts,ls[6],ls[7],prfrms,gnres,ls[10],
                wrts,ls[12]));//adding to also all the films
        myWriter.write("\n");
        myWriter.write("-----------------------------------------------------------------------------------------------------\n");
        myWriter.close();//closing file

    }
    public static void wiewfilm(ArrayList<FeatureFilm> featureFilms,ArrayList<TVSeries> tvSeries,ArrayList<Documentary> documentaries
            ,ArrayList<ShortFilm> shortFilms,String id,String command,ArrayList<Person> allTheHumans,ArrayList<User> users,String filename) throws IOException{
        FileWriter myWriter = new FileWriter(filename,true); //openning the output file by myWriter
        boolean finalBool=true;// boolean value to check if command is failed or not
        //for each list , whe have the same hierachy
        for (int i=0;i<shortFilms.size();i++) {
            if (shortFilms.get(i).id.equals(id)) { //controlling the equality
                finalBool=false; //making the bool false , command not failed
                myWriter.write(command+"\n");
                myWriter.write("\n");
                myWriter.write(shortFilms.get(i).filmTitle+" ("+shortFilms.get(i).releaseDate.substring(6)+")\n");
                //then ,for each film we write the directors ,writers and genre if they exist
                //all of these have if statement because of ", " spelling
                for (int j = 0; j < shortFilms.get(i).genre.length; j++) {
                    if (j==shortFilms.get(i).genre.length-1){
                        myWriter.write(shortFilms.get(i).genre[j]);
                    }else{
                        myWriter.write(shortFilms.get(i).genre[j]+", ");
                    }

                }
                myWriter.write("\n");
                myWriter.write("Writers: ");
                for (int j = 0; j < shortFilms.get(i).writers.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (shortFilms.get(i).writers[j].equals(allTheHumans.get(k).id)) {
                            if (j==shortFilms.get(i).writers.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname);
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }

                        }
                    }
                }
                myWriter.write("\n");
                myWriter.write("Directors: ");
                for (int j = 0; j < shortFilms.get(i).directors.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (shortFilms.get(i).directors[j].equals(allTheHumans.get(k).id)) {
                            if (j==shortFilms.get(i).directors.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname);
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }

                        }
                    }


                }
                myWriter.write("\n");
                myWriter.write("Stars: ");
                for (int j = 0; j < shortFilms.get(i).cast.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (shortFilms.get(i).cast[j].equals(allTheHumans.get(k).id)) {
                            if (j==shortFilms.get(i).cast.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname);
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }


                        }
                    }


                }
                myWriter.write("\n");

                boolean bool1=true; //another bool to control the "awaiting for votes" situation
                int count=0; //values to get the point rate
                int sum=0;
                for (int j=0;j<users.size();j++){ //for loop of user list
                    for (int k=0;k<users.get(j).ratedMovies.size();k++){ //another for loop for user's rated movies list
                        if(id.equals(users.get(j).ratedMovies.get(k))){ // if id's are equal
                            count++;
                            bool1=false; //making bool false
                            sum+=users.get(j).ratedMoviesPoints.get(k);
                        }

                    }
                }

                if (bool1){// if bool is true,there is no votes
                    myWriter.write("Awaiting for votes"+"\n");
                }else{ //if bool is false
                    Double finalNum = Math.round(((double)sum)/((double)count)*10.0)/10.0; //rounding the value of our rate
                    String res=Double.toString(finalNum);//converting to String

                    res=res.replace(".",",");//replacing the "." with " ,"
                    if (res.contains("0")){ // if statement to check if number is integer or not
                        res=res.substring(0,1);
                    }
                    myWriter.write("Ratings: ");
                    myWriter.write(res+"/10 from "+count+" users"+"\n");
                }
                myWriter.write("\n");

            }
        }
        //doing the same things for featurefilms,documentaries and tv series
        for (int i=0;i<featureFilms.size();i++) {

            if (featureFilms.get(i).id.equals(id)) {
                finalBool=false;
                myWriter.write(command+"\n");
                myWriter.write("\n");
                myWriter.write(featureFilms.get(i).filmTitle+" ("+featureFilms.get(i).releaseDate.substring(6)+")\n");
                for (int j = 0; j < featureFilms.get(i).genre.length; j++) {
                    if (j==featureFilms.get(i).genre.length-1){
                        myWriter.write(featureFilms.get(i).genre[j]);
                    }else{
                        myWriter.write(featureFilms.get(i).genre[j]+", ");
                    }

                }
                myWriter.write("\n");
                myWriter.write("Writers: ");
                for (int j = 0; j < featureFilms.get(i).writers.length; j++) {

                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (featureFilms.get(i).writers[j].equals(allTheHumans.get(k).id)) {
                            if (j==featureFilms.get(i).writers.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname);
                            }else{
                            myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }

                        }
                    }


                }
                myWriter.write("\n");
                myWriter.write("Directors: ");
                for (int j = 0; j < featureFilms.get(i).directors.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (featureFilms.get(i).directors[j].equals(allTheHumans.get(k).id)) {
                            if (j==featureFilms.get(i).directors.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname);
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }

                        }
                    }
                }
                myWriter.write("\n");
                myWriter.write("Stars: ");
                for (int j = 0; j < featureFilms.get(i).cast.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (featureFilms.get(i).cast[j].equals(allTheHumans.get(k).id)) {
                            if (j==featureFilms.get(i).cast.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname);
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }


                        }
                    }
                }
                myWriter.write("\n");

                int count=0;
                int sum=0;
                boolean bool1=true;
                for (int j=0;j<users.size();j++){

                    for (int k=0;k<users.get(j).ratedMovies.size();k++){

                        if(id.equals(users.get(j).ratedMovies.get(k))){
                            count++;
                            bool1=false;
                            sum+=users.get(j).ratedMoviesPoints.get(k);

                        }

                    }
                }
                if (bool1){
                    myWriter.write("Awaiting for votes"+"\n");
                }else{
                    Double finalNum = Math.round(((double)sum)/((double)count)*10.0)/10.0;
                    String res=Double.toString(finalNum);
                    res=res.replace(".",",");
                    if (res.contains("0")){
                        res=res.substring(0,1);
                    }
                    myWriter.write("Ratings: ");
                    myWriter.write(res+"/10 from "+count+" users"+"\n");
                }


                myWriter.write("\n");





            }
        }
        for (int i=0;i<documentaries.size();i++) {


            if (documentaries.get(i).id.equals(id)) {
                finalBool=false;
                myWriter.write(command+"\n");
                myWriter.write("\n");
                myWriter.write(documentaries.get(i).filmTitle+" ("+documentaries.get(i).releaseDate.substring(6)+")\n");



                myWriter.write("\n");
                myWriter.write("Directors: ");
                for (int j = 0; j < documentaries.get(i).directors.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (documentaries.get(i).directors[j].equals(allTheHumans.get(k).id)) {
                            if (j==documentaries.get(i).directors.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname);
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }

                        }
                    }


                }
                myWriter.write("\n");
                myWriter.write("Stars: ");
                for (int j = 0; j < documentaries.get(i).cast.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (documentaries.get(i).cast[j].equals(allTheHumans.get(k).id)) {
                            if (j==documentaries.get(i).cast.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname );
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }


                        }
                    }


                }
                myWriter.write("\n");

                boolean bool1=true;
                int count=0;
                int sum=0;
                for (int j=0;j<users.size();j++){

                    for (int k=0;k<users.get(j).ratedMovies.size();k++){

                        if(id.equals(users.get(j).ratedMovies.get(k))){
                            count++;
                            bool1=false;
                            sum+=users.get(j).ratedMoviesPoints.get(k);
                        }

                    }
                }
                if (bool1){
                    myWriter.write("Awaiting for votes"+"\n");
                }else{
                    Double finalNum = Math.round(((double)sum)/((double)count)*10.0)/10.0;
                    String res=Double.toString(finalNum);
                    res=res.replace(".",",");
                    if (res.contains("0")){
                        res=res.substring(0,1);
                    }

                    myWriter.write("Ratings: ");
                    myWriter.write(res+"/10 from "+count+" users"+"\n");
                }

                myWriter.write("\n");




            }
        }
        for (int i=0;i<tvSeries.size();i++) {

            if (tvSeries.get(i).id.equals(id)) {
                finalBool=false;
                myWriter.write(command+"\n");
                myWriter.write("\n");
                myWriter.write(tvSeries.get(i).filmTitle+" ("+tvSeries.get(i).startDate.substring(6)+"-"+
                        tvSeries.get(i).startDate.substring(6)+")\n");
                myWriter.write(tvSeries.get(i).numberOfSeasons+" seasons, "+tvSeries.get(i).numberOfEpisodes+" episodes"+"\n");
                for (int j = 0; j < tvSeries.get(i).genre.length; j++) {
                    if (j==tvSeries.get(i).genre.length-1){
                        myWriter.write(tvSeries.get(i).genre[j]);
                    }else{
                        myWriter.write(tvSeries.get(i).genre[j]+", ");
                    }

                }
                myWriter.write("\n");
                myWriter.write("Writers: ");
                for (int j = 0; j < tvSeries.get(i).writers.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (tvSeries.get(i).writers[j].equals(allTheHumans.get(k).id)) {
                            if (j==tvSeries.get(i).writers.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname );
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }

                        }
                    }


                }
                myWriter.write("\n");
                myWriter.write("Directors: ");
                for (int j = 0; j < tvSeries.get(i).directors.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (tvSeries.get(i).directors[j].equals(allTheHumans.get(k).id)) {
                            if (j==tvSeries.get(i).directors.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname );
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }

                        }
                    }


                }
                myWriter.write("\n");
                myWriter.write("Stars: ");
                for (int j = 0; j < tvSeries.get(i).cast.length; j++) {
                    for (int k = 0; k < allTheHumans.size(); k++) {
                        if (tvSeries.get(i).cast[j].equals(allTheHumans.get(k).id)) {
                            if (j==tvSeries.get(i).cast.length-1){
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname );
                            }else{
                                myWriter.write(allTheHumans.get(k).name + " " + allTheHumans.get(k).surname + ", ");
                            }


                        }
                    }


                }
                myWriter.write("\n");

                boolean bool1=true;
                int count=0;
                int sum=0;
                for (int j=0;j<users.size();j++){

                    for (int k=0;k<users.get(j).ratedMovies.size();k++){

                        if(id.equals(users.get(j).ratedMovies.get(k))){
                            count++;
                            bool1=false;
                            sum+=users.get(j).ratedMoviesPoints.get(k);
                        }

                    }
                }
                if (bool1){
                    myWriter.write("Awaiting for votes"+"\n");
                }else{
                    Double finalNum = Math.round(((double)sum)/((double)count)*10.0)/10.0;
                    String res=Double.toString(finalNum);
                    res=res.replace(".",",");
                    if (res.contains("0")){
                        res=res.substring(0,1);
                    }
                    myWriter.write("Ratings: ");

                    myWriter.write(res+"/10 from "+count+" users"+"\n");
                }
                myWriter.write("\n");

            }
        }
        if (finalBool){
            myWriter.write(command+"\n");
            myWriter.write("\n");
            myWriter.write("Command Failed"+"\n");
            myWriter.write("Film ID: "+id+"\n");
            myWriter.write("\n");
        }
        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();
    }
    public static void getTheRatesOfUser(String id,ArrayList<User> users,ArrayList<Film> films,String command,String filename) throws IOException{
        FileWriter myWriter = new FileWriter(filename,true);//openning the output file
        myWriter.write(command+"\n");
        myWriter.write("\n");
        boolean bool1=true;//bool1 booleans for failed situation
        boolean bool2=true;//bool2 for "there is no any ratings so far " situation
        for (int i=0;i<users.size();i++){ //for loop of userList
            if (users.get(i).id.equals(id)){//if id equals
                bool1=false;//making bool1 false so command not failed ,there is an user
                for(int j=0;j<users.get(i).ratedMovies.size();j++){//rated movies for loop
                    for (int k=0;k<films.size();k++){ //another for loop for allThefilms
                        if (films.get(k).id.equals(users.get(i).ratedMovies.get(j))){
                            bool2=false; //making bool2 false so there is an rating
                            myWriter.write(films.get(k).filmTitle+": "+users.get(i).ratedMoviesPoints.get(j)+"\n");
                        }
                    }
                }
            }
        }
        //if bools are true ,writing the output below
        if (bool1){
            myWriter.write("Command Failed"+"\n");
            myWriter.write("User ID: "+id+"\n");
            myWriter.write("\n");
            myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
            myWriter.close();
            return; //if bool1 is true ,so bool2 is also true ,to prevent the output of bool2, end the method here
        }
        if (bool2){
            myWriter.write("There is not any ratings so far"+"\n");
        }
        myWriter.write("\n");
        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();
    }
    public static void edit(ArrayList<User> users,String userId,String filmId,int newpoint,String command,ArrayList<Film> allTheFilms
    ,String filename) throws IOException{
        FileWriter myWriter = new FileWriter(filename,true);
        myWriter.write(command+"\n");
        myWriter.write("\n");
        boolean bool1=true; // bool to conrol if command is failed or not
        for (int i=0;i<users.size();i++){ // in users
            if (users.get(i).id.equals(userId)){ //controlling the equality of ids
                for(int j=0;j<users.get(i).ratedMovies.size();j++){// in ratedMovies
                    if (users.get(i).ratedMovies.get(j).equals(filmId)){
                        users.get(i).editTheRate(j,newpoint);//calling the method in user class
                        bool1=false;//make bool1 false
                        myWriter.write("New ratings done successfully"+"\n");
                        //for loop of allTheFilms to get the film title
                        for (int z=0;z<allTheFilms.size();z++){
                            if (allTheFilms.get(z).id.equals(filmId)){
                                myWriter.write("Film title: "+allTheFilms.get(z).filmTitle+"\n");
                            }
                        }

                        myWriter.write("Your Rating: "+newpoint+"\n");
                        myWriter.write("\n");


                    }
                }
            }
        }
        if (bool1){ //if bool is true
            myWriter.write("Command Failed"+"\n");
            myWriter.write("User ID: "+userId+"\n");
            myWriter.write("Film ID: "+filmId+"\n");
            myWriter.write("\n");
        }
        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();
    }
    public static void remove(ArrayList<User> users,String userId,String filmId,String command,ArrayList<Film> allTheFilms,String filename) throws IOException{
        //this method has the same hierharcy with edit method
        FileWriter myWriter = new FileWriter(filename,true);
        myWriter.write(command+"\n");
        myWriter.write("\n");
        boolean bool1=true;

        for (int i=0;i<users.size();i++){
            if (users.get(i).id.equals(userId)){
                for(int j=0;j<users.get(i).ratedMovies.size();j++){
                    if (users.get(i).ratedMovies.get(j).equals(filmId)){
                        users.get(i).ratedMovies.remove(j); //removing from ratedMovies
                        users.get(i).ratedMoviesPoints.remove(j); //removing from ratedMoviesPoints
                        bool1=false;
                        myWriter.write("Your film rating was removed successfully"+"\n");
                        for (int z=0;z<allTheFilms.size();z++){
                            if (allTheFilms.get(z).id.equals(filmId)){
                                myWriter.write("Film Title: "+allTheFilms.get(z).filmTitle+"\n");
                            }
                        }
                        myWriter.write("\n");
                    }
                }
            }
        }
        //controlling bool to failed situation
        if (bool1){
            myWriter.write("Command Failed"+"\n");
            myWriter.write("User ID: "+userId+"\n");
            myWriter.write("Film ID: "+filmId+"\n");
            myWriter.write("\n");
        }

        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();
    }
    public static void listTheTVSeries(ArrayList<TVSeries> tvSeries,String command,String filename) throws IOException{
        FileWriter myWriter = new FileWriter(filename,true);
        myWriter.write(command+"\n");
        myWriter.write("\n");
        boolean bool1=true;// bool to control the "no result " situation
        for (int i=0;i< tvSeries.size();i++){ //for loop of tv series
            bool1=false; //making bool false means that tv series is not empty
            myWriter.write(tvSeries.get(i).filmTitle+"("+tvSeries.get(i).startDate.substring(6)+"-"+tvSeries.get(i).endDate.substring(6)+")"+"\n");
            myWriter.write(tvSeries.get(i).numberOfSeasons +" seasons and "+tvSeries.get(i).numberOfEpisodes+" episodes"+"\n");
            myWriter.write("\n");
        }
        //controlling the bool
        if (bool1){
            myWriter.write("No result"+"\n");
            myWriter.write("\n");
        }
        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();
    }
    public static void listTheFilmsByCountry(ArrayList<Film> films,String command,String countyrName,String filename) throws  IOException{
        FileWriter myWriter = new FileWriter(filename,true);
        myWriter.write(command+"\n");
        myWriter.write("\n");
        boolean bool1=true; //bool to check "no result " situation
        for(int i=0;i<films.size();i++){ //for loop of all the films

            if (films.get(i).country.equals(countyrName)){// checking equality of countries
                myWriter.write("Film title: "+films.get(i).filmTitle+"\n");
                myWriter.write(films.get(i).runtime+" min"+"\n");
                myWriter.write("Language: "+films.get(i).language+"\n");
                bool1=false;
                myWriter.write("\n");
            }

        }
        if (bool1){ //checking bool statement
            myWriter.write("No result"+"\n");
            myWriter.write("\n");
        }
        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();
    }
    public static void listTheFeatureFilmsBeforeTheDate(ArrayList<FeatureFilm> featureFilms,String command,String year,String filename) throws IOException{
        boolean bool1=true; //control the "no result" situation
        FileWriter myWriter = new FileWriter(filename,true);
        myWriter.write(command+"\n");
        myWriter.write("\n");
        for (int i=0;i< featureFilms.size();i++){ //for loop of feature films
            if (featureFilms.get(i).releaseDate.substring(6).compareTo(year)<0){ //controlling the year
                myWriter.write("Film title: "+ featureFilms.get(i).filmTitle+" ("+
                        featureFilms.get(i).releaseDate.substring(6)+")\n");
                myWriter.write(featureFilms.get(i).runtime+" min"+"\n");
                myWriter.write("Language: "+featureFilms.get(i).language+"\n");
                bool1=false; //make the bool false so that "no result " will not executed
                myWriter.write("\n");

            }
        }
        if (bool1){ //bool statement
            myWriter.write("No result"+"\n");
            myWriter.write("\n");
        }
        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();

    }
    public static void listTheFeatureFilmsAfterTheDate(ArrayList<FeatureFilm> featureFilms,String command,String year,String filename) throws IOException{
        //this method is same as before the date method
        //only difference is just ">=" instead of "<"
        boolean bool1=true;
        FileWriter myWriter = new FileWriter(filename,true);
        myWriter.write(command+"\n");
        myWriter.write("\n");

        for (int i=0;i< featureFilms.size();i++){
            if ((featureFilms.get(i).releaseDate.substring(6).compareTo(year)>=0)){
                myWriter.write("Film title: "+ featureFilms.get(i).filmTitle+" ("+
                        featureFilms.get(i).releaseDate.substring(6)+")\n");
                myWriter.write(featureFilms.get(i).runtime+" min"+"\n");
                myWriter.write("Language: "+featureFilms.get(i).language+"\n");
                bool1=false;
                myWriter.write("\n");

            }
        }
        if (bool1){
            myWriter.write("No result"+"\n");
            myWriter.write("\n");
        }
        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();

    }
    public static void listArtistsFromCountry(ArrayList<Director> directors,ArrayList<Writer> writers,ArrayList<Actor> actors,
                                              ArrayList<ChildActor> childActors,ArrayList<StuntPerformer> stuntPerformers,
                                              String country,String command,String filename) throws IOException{
        //in this method , all the lists have the same algortihm
        FileWriter myWriter = new FileWriter(filename,true);
        myWriter.write(command+"\n");
        myWriter.write("\n");
        boolean bool1=true; // bool for directors
        myWriter.write("Directors:"+"\n");
        for (int i=0 ;i<directors.size();i++){
            if (directors.get(i).country.equals(country)){
                bool1=false;
                myWriter.write(directors.get(i).name+" "+directors.get(i).surname+" "+directors.get(i).agent+"\n");
            }
        }
        if (bool1){ // checking the bool of directors
            myWriter.write("No result"+"\n");

        }
        myWriter.write("\n");
        myWriter.write("Writers:"+"\n");

        boolean bool2=true; //bool for writers
        for (int i=0 ;i<writers.size();i++){
            if (writers.get(i).country.equals(country)){
                bool2=false;
                myWriter.write(writers.get(i).name+" "+writers.get(i).surname+" "+writers.get(i).writingStyle+"\n");
            }
        }
        if (bool2){ //checking the bool of writers
            myWriter.write("No result"+"\n");

        }
        myWriter.write("\n");
        myWriter.write("Actors:"+"\n");
        boolean bool3=true; //bool of actors
        for (int i=0 ;i<actors.size();i++){

            if (actors.get(i).country.equals(country)){
                bool3=false;
                myWriter.write(actors.get(i).name+" "+actors.get(i).surname+" "+actors.get(i).height+" cm"+"\n");
            }
        }
        if (bool3){ //checking the bool of actors
            myWriter.write("No result"+"\n");

        }
        myWriter.write("\n");
        myWriter.write("ChildActors:"+"\n");
        boolean bool4=true;//bool of ChildActors
        for (int i=0 ;i<childActors.size();i++){

            if (childActors.get(i).country.equals(country)){
                bool4=false;
                myWriter.write(childActors.get(i).name+" "+childActors.get(i).surname+" "+childActors.get(i).age+"\n");
            }
        }
        if (bool4){ //checking the bool of ChildActors
            myWriter.write("No result"+"\n");

        }
        myWriter.write("\n");


        myWriter.write("StuntPerformers:"+"\n");
        boolean bool5=true; //bool of stutnt performers
        for (int i=0 ;i<stuntPerformers.size();i++){

            if (stuntPerformers.get(i).country.equals(country)){
                bool5=false;
                myWriter.write(stuntPerformers.get(i).name+" "+stuntPerformers.get(i).surname+" "+stuntPerformers.get(i).height+" cm"+"\n");

            }
        }

        if (bool5){ //chencking the bool of stunt performers
            myWriter.write("No result"+"\n");

        }
        myWriter.write("\n");
        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();
    }
    public static void listFilmsByRateDegree(ArrayList<FeatureFilm> featureFilms,ArrayList<TVSeries> tvSeries,ArrayList<Documentary> documentaries,
                                             ArrayList<ShortFilm> shortFilms,ArrayList<User> users,String command,String filename) throws IOException{
        FileWriter myWriter = new FileWriter(filename,true);
        myWriter.write(command+"\n");
        myWriter.write("\n");
        //we have the same algorithm for all the film lists
        myWriter.write("FeatureFilm"+"\n");
        ArrayList<String> sortedFeatureFilms=new ArrayList<String>(); // list to write output of the method
        //method to get the rate
        //defining the necessary bools
        boolean boolofFeatureFilms=true;
        boolean boolofShortFilms=true;
        boolean booloftvseries=true;
        boolean boolofdocumentaries=true;
        for (int i=0;i< featureFilms.size();i++){
            int count=0; //values of count and sum to get rate
            double sum=0;
            for (int j=0;j<users.size();j++){
                for (int k=0;k<users.get(j).ratedMovies.size();k++){
                    if (users.get(j).ratedMovies.get(k).equals(featureFilms.get(i).id)){
                        sum+=users.get(j).ratedMoviesPoints.get(k);//increasing sum
                        count+=1; //increasing count
                    }
                }
            }
            double rate;
            if (count==0){//if count ==0,then rate ==0
                rate=0;
            }else{
                rate=sum/count;// else rate=sum/count
            }
            // adding the desired values to the list of sortedFeatureFilms
            sortedFeatureFilms.add(rate +"\t"+count+"\t"+featureFilms.get(i).filmTitle+"\t"+featureFilms.get(i).releaseDate.substring(6));


        }
        //sort algorithm
        Collections.sort(sortedFeatureFilms, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] ls1=o1.split("\t");
                String[] ls2 =o2.split("\t");
                //ls1[0] and ls2[0] means the rate of the films
                if (ls1[0].compareTo(ls2[0])!=0){
                    return ls2[0].compareTo(ls1[0]);//sorting to the descending order
                }
                return 0;
            }
        });

        for (int i=0;i<sortedFeatureFilms.size();i++){//for loop of sorted List to write output
            boolofFeatureFilms=false; //make the bool false so that there is an result exist
            String[] ls2=sortedFeatureFilms.get(i).split("\t");//splitting the values
            Double finalNum = Math.round(Double.parseDouble(ls2[0])*10.0)/10.0;//getting the rate
            String res=Double.toString(finalNum);//converting the rate to String
            res=res.replace(".",",");//replacing "." by ","
            res=res.substring(0,3);//getting the rate in format of "#,#"
            if (res.contains("0")){ // if it is an integer,then taking the first index
                res=res.substring(0,1);
            }

            //writing to the output file in correct format
            myWriter.write(ls2[2]+" ("+ls2[3]+") "+" Ratings: "+res+"/10 from "+ls2[1]+" users"+"\n");

        }
        if (boolofFeatureFilms){ //controlling the bool of featurefilms
            myWriter.write("No result");
            myWriter.write("\n");
        }
        myWriter.write("\n");

        //all the other lists have the same algortihm so that i don't write a command block to them
        myWriter.write("ShortFilm"+"\n");
        ArrayList<String> sortedShortFilms=new ArrayList<String>();

        for (int i=0;i< shortFilms.size();i++){

            int count=0;
            double sum=0;
            for (int j=0;j<users.size();j++){
                for (int k=0;k<users.get(j).ratedMovies.size();k++){
                    if (users.get(j).ratedMovies.get(k).equals(shortFilms.get(i).id)){
                        sum+=users.get(j).ratedMoviesPoints.get(k);
                        count+=1;
                    }
                }
            }
            double rate;
            if (count==0){
                rate=0;
            }else{
                rate=sum/count;
            }
            sortedShortFilms.add(rate +"\t"+count+"\t"+shortFilms.get(i).filmTitle+"\t"+shortFilms.get(i).releaseDate.substring(6));


        }



        Collections.sort(sortedShortFilms, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] ls1=o1.split("\t");
                String[] ls2 =o2.split("\t");

                if (ls1[0].compareTo(ls2[0])!=0){
                    return ls2[0].compareTo(ls1[0]);
                }
                return 0;
            }
        });

        for (int i=0;i<sortedShortFilms.size();i++){

            String[] ls2=sortedShortFilms.get(i).split("\t");
            Double finalNum = Math.round(Double.parseDouble(ls2[0])*10.0)/10.0;
            String res=Double.toString(finalNum);
            res=res.replace(".",",");
            res=res.substring(0,3);
            boolofShortFilms=false;
            if (res.contains("0")){
                res=res.substring(0,1);
            }

            myWriter.write(ls2[2]+" ("+ls2[3]+") "+" Ratings: "+res+"/10 from "+ls2[1]+" users"+"\n");

        }
        if (boolofShortFilms){
            myWriter.write("No result");
            myWriter.write("\n");
        }
        myWriter.write("\n");

        myWriter.write("Documentary"+"\n");
        ArrayList<String> sortedDocumentaries=new ArrayList<String>();

        for (int i=0;i< documentaries.size();i++){

            int count=0;
            double sum=0;
            for (int j=0;j<users.size();j++){
                for (int k=0;k<users.get(j).ratedMovies.size();k++){
                    if (users.get(j).ratedMovies.get(k).equals(documentaries.get(i).id)){
                        sum+=users.get(j).ratedMoviesPoints.get(k);
                        count+=1;
                    }
                }
            }
            double rate;
            if (count==0){
                rate=0;
            }else{
                rate=sum/count;
            }
            sortedDocumentaries.add(rate +"\t"+count+"\t"+documentaries.get(i).filmTitle+"\t"+documentaries.get(i).releaseDate.substring(6));


        }



        Collections.sort(sortedDocumentaries, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] ls1=o1.split("\t");
                String[] ls2 =o2.split("\t");

                if (ls1[0].compareTo(ls2[0])!=0){
                    return ls2[0].compareTo(ls1[0]);
                }
                return 0;
            }
        });

        for (int i=0;i<sortedDocumentaries.size();i++){
            boolofdocumentaries=false;

            String[] ls2=sortedDocumentaries.get(i).split("\t");
            Double finalNum = Math.round(Double.parseDouble(ls2[0])*10.0)/10.0;
            String res=Double.toString(finalNum);
            res=res.replace(".",",");
            res=res.substring(0,3);
            if (res.contains("0")){
                res=res.substring(0,1);
            }


            myWriter.write(ls2[2]+" ("+ls2[3]+") "+" Ratings: "+res+"/10 from "+ls2[1]+" users"+"\n");

        }
        if (boolofdocumentaries){
            myWriter.write("No result");
            myWriter.write("\n");
        }
        myWriter.write("\n");

        myWriter.write("TVSeries"+"\n");
        ArrayList<String> sortedtvseries=new ArrayList<String>();

        for (int i=0;i< tvSeries.size();i++){
            booloftvseries=false;
            int count=0;
            double sum=0;
            for (int j=0;j<users.size();j++){
                for (int k=0;k<users.get(j).ratedMovies.size();k++){
                    if (users.get(j).ratedMovies.get(k).equals(tvSeries.get(i).id)){
                        sum+=users.get(j).ratedMoviesPoints.get(k);
                        count+=1;
                    }
                }
            }
            double rate;
            if (count==0){
                rate=0;
            }else{
                rate=sum/count;
            }
            sortedtvseries.add(rate +"\t"+count+"\t"+tvSeries.get(i).filmTitle+"\t"+tvSeries.get(i).startDate.substring(6)
            +"-"+tvSeries.get(i).endDate.substring(6));


        }



        Collections.sort(sortedtvseries, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] ls1=o1.split("\t");
                String[] ls2 =o2.split("\t");

                if (ls1[0].compareTo(ls2[0])!=0){
                    return ls2[0].compareTo(ls1[0]);
                }
                return 0;
            }
        });

        for (int i=0;i<sortedtvseries.size();i++){

            String[] ls2=sortedtvseries.get(i).split("\t");
            Double finalNum = Math.round(Double.parseDouble(ls2[0])*10.0)/10.0;
            String res=Double.toString(finalNum);
            res=res.replace(".",",");
            res=res.substring(0,3);
            if (res.contains("0")){
                res=res.substring(0,1);
            }

            myWriter.write(ls2[2]+" ("+ls2[3]+") "+" Ratings: "+res+"/10 from "+ls2[1]+" users"+"\n");

        }
        if (booloftvseries){
            myWriter.write("No result");
            myWriter.write("\n");
        }
        myWriter.write("\n");
        myWriter.write("-----------------------------------------------------------------------------------------------------"+"\n");
        myWriter.close();

    }
    //clearing the output file before running
    public static void clearTheFile(String name) throws IOException {
        FileWriter file1 = new FileWriter(name, false);
        PrintWriter file2 = new PrintWriter(file1, false);
        file2.flush();
        file2.close();
        file1.close();
    }
    //main method
    public static void main (String[] args) throws  IOException{
        clearTheFile(args[3]); //clearing the output file first
        String[] ls; //creating useable list to use in for loops
        String[] ids;

        String[] lst=readFile(args[0]); //reading people.txt as array in lst
        //creating lists of each people
        ArrayList<User> users=new ArrayList<User>();
        ArrayList<Actor> actors=new ArrayList<Actor>();
        ArrayList<ChildActor> childActors=new ArrayList<ChildActor>();
        ArrayList<Director> directors=new ArrayList<Director>();
        ArrayList<Writer> writers=new ArrayList<Writer>();
        ArrayList<StuntPerformer> stuntPerformers=new ArrayList<StuntPerformer>();
        //crating the all the humans list and their ids to use in methods
        ArrayList<Person> allTheHumans=new ArrayList<Person>();
        ArrayList<String> allTheHumansIds =new ArrayList<String>();
        for(int i=0;i< lst.length;i++){ //for loop of people.txt
            ls=lst[i].split("\t");  //splitting the line by using ls
            //for each case we add them to their list and all the humans list and also ids
            //we used polymorphisim in this assignment by using allTheHumans list
            if (ls[0].equals("Actor:")){

                actors.add( new Actor(ls[1],ls[2],ls[3],ls[4],ls[5]));
                allTheHumans.add(new Actor(ls[1],ls[2],ls[3],ls[4],ls[5]));
                allTheHumansIds.add(ls[1]);

            }
            if (ls[0].equals("ChildActor:")){
                childActors.add(new ChildActor(ls[1],ls[2],ls[3],ls[4],ls[5]));
                allTheHumans.add(new ChildActor(ls[1],ls[2],ls[3],ls[4],ls[5]));
                allTheHumansIds.add(ls[1]);

            }
            if (ls[0].equals("Director:")){
                directors.add(new Director(ls[1],ls[2],ls[3],ls[4],ls[5]));
                allTheHumans.add(new Director(ls[1],ls[2],ls[3],ls[4],ls[5]));
                allTheHumansIds.add(ls[1]);

            }

            if (ls[0].equals("Writer:")){
                writers.add(new Writer(ls[1],ls[2],ls[3],ls[4],ls[5]));
                allTheHumans.add(new Writer(ls[1],ls[2],ls[3],ls[4],ls[5]));
                allTheHumansIds.add(ls[1]);

            }
            if (ls[0].equals("StuntPerformer:")){
                ids=ls[6].split(",");
                stuntPerformers.add(new StuntPerformer(ls[1],ls[2],ls[3],ls[4],ls[5],ids));
                allTheHumans.add(new StuntPerformer(ls[1],ls[2],ls[3],ls[4],ls[5],ids));
                allTheHumansIds.add(ls[1]);

            }
            if (ls[0].equals("User:")){
                users.add(new User(ls[1],ls[2],ls[3],ls[4]));
                allTheHumans.add(new User(ls[1],ls[2],ls[3],ls[4]));
                allTheHumansIds.add(ls[1]);

            }

        }
        //reading the films.txt by using read the file method
        String[] lstfilms=readFile(args[1]);
        //crating the lists of films for each typle
        ArrayList<Film> allTheFilms=new ArrayList<Film>();
        ArrayList<FeatureFilm> featureFilms=new ArrayList<FeatureFilm>();
        ArrayList<ShortFilm> shortFilms=new ArrayList<ShortFilm>();
        ArrayList<Documentary> documentaries=new ArrayList<Documentary>();
        ArrayList<TVSeries> tvSeries=new ArrayList<TVSeries>();
        for (int i=0;i<lstfilms.length;i++){
            ls=lstfilms[i].split("\t"); //again splitting line by "\t"
            //for each case ,we add films to their list and all the film list
            if (ls[0].equals("FeatureFilm:")){
                String[] drcts=ls[4].split(",");
                String[] prfrms=ls[7].split(",");
                String[] gnres=ls[8].split(",");
                String[] wrts=ls[10].split(",");
                featureFilms.add(new FeatureFilm(ls[1],ls[2],ls[3],drcts,ls[5],ls[6],prfrms,gnres,ls[9],
                        wrts,ls[11]));
                allTheFilms.add(new FeatureFilm(ls[1],ls[2],ls[3],drcts,ls[5],ls[6],prfrms,gnres,ls[9],
                        wrts,ls[11]));


            }
            if (ls[0].equals("ShortFilm:")){
                String[] drcts=ls[4].split(",");
                String[] prfrms=ls[7].split(",");
                String[] gnres=ls[8].split(",");
                String[] wrts=ls[10].split(",");
                int k=Integer.parseInt(ls[5]);
                //i dont know its necessary or not but if shortfilm runtime is bigger than 40 ,there is an warning message
                if (k>40){
                    System.out.println("Short Film runtime cannot be more than 40");
                }else{
                shortFilms.add(new ShortFilm(ls[1],ls[2],ls[3],drcts,ls[5],ls[6],prfrms,gnres,ls[9],
                        wrts));
                allTheFilms.add(new ShortFilm(ls[1],ls[2],ls[3],drcts,ls[5],ls[6],prfrms,gnres,ls[9],
                        wrts));
                }


            }
            if (ls[0].equals("Documentary:")){
                String[] drcts=ls[4].split(",");
                String[] prfrms=ls[7].split(",");

                documentaries.add(new Documentary(ls[1],ls[2],ls[3],drcts,ls[5],ls[6],prfrms,ls[8]));
                allTheFilms.add(new Documentary(ls[1],ls[2],ls[3],drcts,ls[5],ls[6],prfrms,ls[8]));

            }
            if (ls[0].equals("TVSeries:")){
                String[] drcts=ls[4].split(",");
                String[] prfrms=ls[7].split(",");
                String[] gnres=ls[8].split(",");
                String[] wrts=ls[9].split(",");
                tvSeries.add(new TVSeries(ls[1],ls[2],ls[3],drcts,ls[5],ls[6],prfrms,gnres,
                        wrts,ls[10],ls[11],ls[12],ls[13]));
                allTheFilms.add(new TVSeries(ls[1],ls[2],ls[3],drcts,ls[5],ls[6],prfrms,gnres,
                        wrts,ls[10],ls[11],ls[12],ls[13]));
            }

        }
        //reading the commands.txt by using readFile method
        String[] commands=readFile(args[2]);
        //creating list to use in rate method specially
        ArrayList<String> list=readFileAsArrayList(args[1]);
        //for loop of commands.txt
        for (int i=0;i<commands.length;i++){
            String[] com=commands[i].split("\t"); // splitting line by using "\t"
            if (com[0].equals("RATE")){
                //rate method
                int point =Integer.parseInt(com[3]); //getting the rate as an integer
                if (point<1||point>10){ //if rate is not between 1 and 10 ,there is an error message ,and also idk if it is necessary or not
                    System.out.println("Rate should be between 1 and 10");
                }else{
                    rate(users,com[1],com[2],point,list,args[3]); //calling the rate method
                }
            }
            if (com[0].equals("ADD")){
                ls=commands[i].split("\t");// splitting line to use in list
                list.add("FeatureFilm"+"\t"+ls[2]+"\t"+ls[3]); //adding to the list to use in next rate commands
                add(featureFilms, ls,commands[i],allTheFilms,allTheHumansIds,args[3]);//calling the add merhod
            }
            if (com[0].equals("VIEWFILM")){
                //calling the wiewfilm method
                wiewfilm(featureFilms,tvSeries,documentaries
                        , shortFilms, com[1],commands[i], allTheHumans,users,args[3]);
            }
            if ((com[0].equals("LIST"))&&(com[1].equals("USER"))){
                //calling the getTheRatesOfUser method
                getTheRatesOfUser(com[2],users,allTheFilms,commands[i],args[3]);
            }
            if (com[0].equals("EDIT")){
                //calling the edit method
                int newpoint =Integer.parseInt(com[4]);
                edit(users,com[2],com[3],newpoint,commands[i],allTheFilms,args[3]);


            }
            if (com[0].equals("REMOVE")){
                // calling the remove method
               remove(users,com[2],com[3],commands[i],allTheFilms,args[3]);

            }
            if ((com[0].equals("LIST"))&&(com[1].equals("FILM"))&&(com[2].equals("SERIES"))){
                //calling the listTheTVSeries method
                listTheTVSeries(tvSeries,commands[i],args[3]);
            }
            if ((com[0].equals("LIST"))&&(com[1].equals("FILMS"))&&(com[3].equals("COUNTRY"))){
                //calling the listTheFilmsByCountry method
                listTheFilmsByCountry(allTheFilms,commands[i],com[4],args[3]);
            }
            if ((com[0].equals("LIST"))&&(com[1].equals("FEATUREFILMS"))&&(com[2].equals("BEFORE"))){
                // calling the listTheFeatureFilmsBeforeTheDate method
                listTheFeatureFilmsBeforeTheDate(featureFilms,commands[i],com[3],args[3]);
            }
            if ((com[0].equals("LIST"))&&(com[1].equals("FEATUREFILMS"))&&(com[2].equals("AFTER"))){
                // calling the listTheFeatureFilmsAfterTheDate method
                listTheFeatureFilmsAfterTheDate(featureFilms,commands[i],com[3],args[3]);
            }
            if ((com[0].equals("LIST"))&&(com[1].equals("ARTISTS"))&&(com[2].equals("FROM"))){
                // calling the listArtistsFromCountry method
                listArtistsFromCountry(directors,writers,actors,childActors,stuntPerformers,com[3],commands[i],args[3]);
            }
            if ((com[0].equals("LIST"))&&(com[1].equals("FILMS"))&&(com[2].equals("BY"))&&(com[3].equals("RATE"))&&(com[4].equals("DEGREE"))){
                // calling the listFilmsByRateDegree method
                listFilmsByRateDegree(featureFilms,tvSeries,documentaries,shortFilms,users,commands[i],args[3]);

            }


        }

    }
}