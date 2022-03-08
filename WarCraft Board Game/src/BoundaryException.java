import java.io.FileWriter;

public class BoundaryException extends ArrayIndexOutOfBoundsException{
    BoundaryException(String message, FileWriter myWriter){
        try {
            myWriter.write(message);
        }catch (Exception e){

        }


    }
}
