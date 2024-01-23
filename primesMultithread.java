package COP4520_Assignment_1;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class primesMultithread {
    private static final int NUM_THREADS = 8;

    public static void main(String[] args) {
        int limit = (int) Math.pow(10, 8);

        Thread[] threads = new Thread[NUM_THREADS];
        PrimeInfo[] primeInfoTasks = new PrimeInfo[NUM_THREADS];

        int segmentSize = limit / NUM_THREADS;
        int start = 2;
        int end = segmentSize;

        long startTime = System.currentTimeMillis();

        try (FileWriter writer = new FileWriter("primes.txt")) {
            for (int i = 0; i < NUM_THREADS; i++) {
                if (i == NUM_THREADS - 1) {
                    end = limit;
                }

                primeInfoTasks[i] = new PrimeInfo(start, end);
                threads[i] = new Thread(primeInfoTasks[i]);
                threads[i].start();

                start = end + 1;
                end += segmentSize;
            }

            long sum = 0;
            int totalPrimeCount = 0;
            List<Integer> topPrimes = new ArrayList<>();

            for (int i = 0; i < NUM_THREADS; i++) {
                try {
                    threads[i].join();
                    sum += primeInfoTasks[i].getSum();
                    totalPrimeCount += primeInfoTasks[i].getPrimeCount();
                    topPrimes.addAll(primeInfoTasks[i].getTopPrimes());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Sort and retain only the top 10 primes
            Collections.sort(topPrimes, Collections.reverseOrder());
            topPrimes = topPrimes.subList(0, Math.min(topPrimes.size(), 10));

            long endTime = System.currentTimeMillis();

            try {
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

        public long getSum() {
            return sum;
        }

        public int getPrimeCount() {
            return primeCount;
        }

        public List<Integer> getTopPrimes() {
            return topPrimes;
        }

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

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                if (isPrime(i)) {
                    sum += i;
                    primeCount++;
                    topPrimes.add(i);
                }
            }
        }
    }
}

