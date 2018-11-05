import java.util.*;

public class test {
    public static void main(String[] args) {

        System.out.println(sumOfAllDigits(982));


        List<Integer> listOfIntegers = new ArrayList<>();
        listOfIntegers.add(4);
        listOfIntegers.add(1);
        listOfIntegers.add(4);
        listOfIntegers.add(2);
        listOfIntegers.add(3);
        listOfIntegers.add(9);
        listOfIntegers.add(9);
        listOfIntegers.add(5);
        List<Integer> listOfUniqueIntegers = findUnique(listOfIntegers);
        System.out.println(listOfUniqueIntegers.toString());

        String str = "Hello There My Friend";
        System.out.println(reverseStringAndLetters(str));

        int[] coinNominations = {25, 10, 5, 1};
        int centsBack = 97;
        System.out.println(leastNumberOfCoins(centsBack, coinNominations));
    }

    private static String leastNumberOfCoins(int centsBack, int[] coinNominations) {

        int remainingToGive = centsBack;
        int[] numberOfCoins = new int[coinNominations.length];
        int i = 0;
        while(remainingToGive > 0 && i < coinNominations.length){
            if(remainingToGive < coinNominations[i]) {
                i++;
            }
            else{
                remainingToGive -= coinNominations[i];
                numberOfCoins[i]++;
            }
        }

        String coinsReturned = "The Least Number of Coins Needeed: \n";
        StringBuilder stringBuilder = new StringBuilder(coinsReturned);
        if(numberOfCoins[0] != 0)
            stringBuilder.append("25 cents: " + numberOfCoins[0] + "\n");
        if(numberOfCoins[1] != 0)
            stringBuilder.append("10 cents: " + numberOfCoins[1] + "\n");
        if(numberOfCoins[2] != 0)
            stringBuilder.append("5 cents: " + numberOfCoins[2] + "\n");
        if(numberOfCoins[3] != 0)
            stringBuilder.append("1 cent: " + numberOfCoins[3] + "\n");

        //System.out.println(Arrays.toString(numberOfCoins));

        return stringBuilder.toString();
    }

    private static String reverseStringAndLetters(String str) {
        String[] tokens = str.split("[ ]+");
        for (int i = 0; i < tokens.length/2; i++) {
            swap(tokens, i , tokens.length-i-1);
        }
        return Arrays.toString(tokens);
    }

    private static void swap(String[] tokens, int index1, int index2) {
        String temp = reverseLetters(tokens[index1]);
        tokens[index1] = reverseLetters(tokens[index2]);
        tokens[index2] = temp;
    }

    private static String reverseLetters(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.reverse();
        return stringBuilder.toString();
    }

    private static List<Integer> findUnique(List<Integer> listOfIntegers) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < listOfIntegers.size(); i++) {
            if(!map.containsKey(listOfIntegers.get(i)))
                map.put(listOfIntegers.get(i), 1);
        }

        //conver HashMap to List
        return new ArrayList<>(map.keySet());
    }

    private static int sumOfAllDigits(int number) {
        int total = 0;
        while (number > 0){
            total += number % 10;
            number /= 10;
        }
        return total;
    }
}
