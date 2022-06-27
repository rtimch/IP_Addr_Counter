import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    private static final String IPV4_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    private static final Pattern pattern = Pattern.compile(IPV4_PATTERN);
    private static final String PATH = "testData.txt";

    public static void main(String[] args) {


        HLL hyperLogLog = new HLL(12);

        try(FileReader fileReader = new FileReader(PATH)){
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String address;
            while ( (address = bufferedReader.readLine()) != null){
                Matcher matcher = pattern.matcher(address);
                if (matcher.matches()) {
                   hyperLogLog.addHash(Integer.toBinaryString(address.hashCode()));
               }
            }
            System.out.println("Amount of unique IPv4 addresses:\t" + hyperLogLog.getEstimatedCardinality() + "\nCount of hashes: " + hyperLogLog.getCountHash());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
