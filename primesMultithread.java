import java.io.FileWriter;
import java.io.IOException;
// import java.math.BigInteger; // THIS IS FOR TESTING WITH MILLER-RABIN
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// import java.util.Random; // THIS IS FOR TESTING WITH MILLER-RABIN

public class primesMultithread {
    private static final int NUM_THREADS = 8;
    // private static final int MILLER_RABIN_ITERATIONS = 5; // THIS IS FOR TESTING WITH MILLER-RABIN

    public static void main(String[] args) {
        // Declare the maximum value to find primes up to from 0
        int limit = (int) Math.pow(10, 8);

        // Declare threads and the array of objects with the number of desired threads
        Thread[] threads = new Thread[NUM_THREADS];
        PrimeInfo[] primeInfoTasks = new PrimeInfo[NUM_THREADS];

        // Evenly separate the number of values to check if they're prime among the threads
        int segmentSize = limit / NUM_THREADS;

        // Start value is 2 as 0 and 1 are not prime values
        int start = 2;

        // Current end value is the end of the values that will be processed by the first thread
        int end = segmentSize;

        // Get the start time right before the threads start processing
        long startTime = System.currentTimeMillis();

        // Declare the method to create and write to an external file
        try (FileWriter writer = new FileWriter("primes.txt")) {
            // For each thread
            for (int i = 0; i < NUM_THREADS; i++) {

                // If the program has reached the last thread
                if (i == NUM_THREADS - 1) {

                    // The end is the limit
                    end = limit;
                }

                // Initialize the current object in the array to contain the information regarding the current set of values, give the
                    // object to its respective current thread, and start the thread
                primeInfoTasks[i] = new PrimeInfo(start, end);
                threads[i] = new Thread(primeInfoTasks[i]);
                threads[i].start();

                // Move the start and end to the next set of values to process
                start = end + 1;
                end += segmentSize;
            }

            // Initialize variables to contain the sum of the prime values, the total number of prime values, and the top 10 prime values
                // from 0 to the limit
            long sum = 0;
            int totalPrimeCount = 0;
            List<Integer> topPrimes = new ArrayList<>();

            // For each thread
            for (int i = 0; i < NUM_THREADS; i++) {
                try {

                    // Get the current thread
                    threads[i].join();

                    // Get the sum of the current thread's object's getSum function
                    sum += primeInfoTasks[i].getSum();

                    // Get the totle number of primes of the current thread's object's getPrimeCount function
                    totalPrimeCount += primeInfoTasks[i].getPrimeCount();
                    // Get the top 10 primes of the current thread's object's getTopPrimes function
                    topPrimes.addAll(primeInfoTasks[i].getTopPrimes());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Sort and retain only the top 10 primes
            Collections.sort(topPrimes, Collections.reverseOrder());
            topPrimes = topPrimes.subList(0, Math.min(topPrimes.size(), 10));

            Collections.reverse(topPrimes);

            // Get the end time right after the threads finish processing
            long endTime = System.currentTimeMillis();

            try {

                // Output the thread processing time, sum of the primes, total number of prime values, and the top 10 primes to the external file
                writer.write((endTime - startTime) + "\n");
                writer.write(sum + "\n");
                writer.write(totalPrimeCount + "\n");
                for (int prime : topPrimes) {
                    writer.write(prime + " ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to reserve an ArrayList
    public ArrayList<Integer> reverseArrayList(ArrayList<Integer> alist)
    {
        for (int i = 0; i < alist.size() / 2; i++) {
            Integer temp = alist.get(i);
            alist.set(i, alist.get(alist.size() - i - 1));
            alist.set(alist.size() - i - 1, temp);
        }
 
        // Return the reversed arraylist
        return alist;
    }

    // Create class to handle run() and other necessary functions to get the desired outputted information
    static class PrimeInfo implements Runnable {
        private int start;
        private int end;
        private int primeCount;
        private List<Integer> topPrimes = new ArrayList<>();
        private long sum;

        public PrimeInfo(int start, int end) {
            this.start = start;
            this.end = end;
            this.sum = 0;
        }

        // Get the sum of the primes for the thread
        public long getSum() {
            return sum;
        }

        // Get the total number of primes for the thread
        public int getPrimeCount() {
            return primeCount;
        }

        // Get the top 10 prime values for the thread
        public List<Integer> getTopPrimes() {
            return topPrimes;
        }

        // Function to determine if the current value is prime
        public boolean isPrime(int number) {
            if (number < 2) {
                return false;
            }

            for (int i = 2; i <= Math.sqrt(number); i++) {
                if (number % i == 0) {
                    return false;
                }
            }

            return true;
        }

        /* THIS IS FOR TESTING WITH MILLER-RABIN
        public boolean isPrime(BigInteger number) {
            BigInteger TWO = BigInteger.valueOf(2);
            BigInteger THREE = BigInteger.valueOf(3);

            if (number.compareTo(THREE) < 0) {
                return number.equals(TWO); // 2 is prime, others less than 3 are not
            }

            // Ensure n is odd
            if (number.mod(TWO).equals(BigInteger.ZERO)) {
                return false;
            }

            BigInteger d = number.subtract(BigInteger.ONE);
            int s = d.getLowestSetBit();
            d = d.shiftRight(s);

            for (int i = 0; i < MILLER_RABIN_ITERATIONS; i++) {
                BigInteger a = BigInteger.probablePrime(number.bitLength(), new Random());
                BigInteger x = a.modPow(d, number);

                if (x.equals(BigInteger.ONE) || x.equals(number.subtract(BigInteger.ONE))) {
                    continue;
                }

                for (int r = 1; r < s; r++) {
                    x = x.modPow(TWO, number);
                    if (x.equals(BigInteger.ONE)) {
                        return false;
                    }
                    if (x.equals(number.subtract(BigInteger.ONE))) {
                        break;
                    }
                }

                if (!x.equals(number.subtract(BigInteger.ONE))) {
                    return false;
                }
            }

            return true;
        }*/

        // Thread's function to get the necessary output information for the current segment of values between start and end
        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                // if (isPrime(BigInteger.valueOf(i))) { // THIS IS FOR TESTING WITH MILER-RABIN
                if (isPrime(i)) {
                    sum += i;
                    primeCount++;
                    topPrimes.add(i);
                }
            }
        }
    }
}

