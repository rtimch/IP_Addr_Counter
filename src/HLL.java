/**
* This class implements HyperLogLog algorithm.
* For a complete understanding, check out https://static.googleusercontent.com/media/research.google.com/ru//pubs/archive/40671.pdf
* */

public class HLL {
    /**Array of registers for storage of leading zero's numbers*/
    int[] registers;
    /**Corection factor (see the paper)*/
    final double alphaM;
    /**Amount of first bits in hashcode for counting amount of registers*/
    final int p;
    /**Amount of registers*/
    final int m;
    /**Amount of hashcodes read*/
    long countHash;


    public HLL(int p){
        this.p = p;
        m = 1 << p;
        alphaM = defineAlphaM(m);
        registers = new int[m];
        countHash = 0;
    }

    public long getCountHash() {
        return countHash;
    }

    /**See the paper (https://static.googleusercontent.com/media/research.google.com/ru//pubs/archive/40671.pdf)*/
    private double defineAlphaM(int m){
        switch (m) {
            case 16:
                return 0.673;
            case 32:
                return 0.697;
            case 64:
                return 0.709;
            default:
                return 0.7213 / (1 + 1.079 / m);
        }
    }

    public void addHash(String hash){
        StringBuilder hashBuilder = new StringBuilder(hash);

        while (hashBuilder.length() < 32){
            hashBuilder.insert(0, "0");
        }

        hash = hashBuilder.toString();

        int index;
        index = Integer.parseInt(hash.substring(0, p), 2);
        String checkForZeros = hash.substring(p);
        registers[index] = Math.max(registers[index], getLeadingZeros(checkForZeros));
        countHash++;
    }

    /**Returns  the number of leading zeros in the binary string input plus one
     * @param input - binary sequence {0,1}
     */
    int getLeadingZeros(String input){
        int zeros = 0;
        for(char c : input.toCharArray()){
            if (c == '1')
                break;
            else
                zeros++;
        }
        return zeros+1;
    }

    public long getEstimatedCardinality(){
        double registersSUM = 0;
        int registersZeros = 0;
        double estimatedCardinality;
        for (int register : registers) {
            if (register == 0)
                registersZeros++;
            registersSUM += 1.0 / (1 << register);
        }

        double E = alphaM * m * m / registersSUM;
        //Small range correction
        if (E <= 2.5 * m){
            if(registersZeros > 0)
                return Math.round(linearCounting(m, registersZeros));
            else
                return Math.round(E);
        }
        //Large range correction
        else if (E <= ((long) 1 << 32) / 30.0){
            return Math.round(E);
        }
        else {
            estimatedCardinality = -1.0 * ((long) 1 << 32) * Math.log(1 - (E / ((long) 1 << 32)));
            return Math.round(estimatedCardinality);
        }
    }
    /**Returns the LinearCounting cardinality estimate*/
    double linearCounting(int m, int V){
        return m * Math.log(m / V);
    }
}
