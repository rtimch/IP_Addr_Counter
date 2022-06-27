import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CreateData {
    public static void main(String[] args) {
        try(FileWriter writer = new FileWriter("testData.txt", false)){
            Random rand = new Random();
            for(int i = 0; i < 100_000_000; i++){
                String text = rand.nextInt(256) + "." + rand.nextInt(256) + "." +rand.nextInt(256) + "." + rand.nextInt(256) + "\n";
                writer.write(text);
            }
            writer.flush();
        }
        catch (IOException e){
            System.out.println(e.fillInStackTrace());
        }
    }
}
