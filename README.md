# How to run assignment
- Clone repository.
- Open Command Prompt.
- Traverse local device to the repository's directory.
- In Command Prompt, write `java primesMultithread.java` and hit Enter.
- The external file with the results should be in the repository folder as "primes.txt".

# Results in chronological order
- Time taken for the threads to run
- Sum of all the prime numbers
- The number of prime numbers
- The top 10 prime numbers

 # Summary

The program equally partitions the range of numbers (up to 10^8) into segments and assigns each segment to a separate thread, for a total of 8 threads, for parallel processing. This allows for a more efficient approach that involves no race conditions and sharing of resources. The isPrime function utilizes Trivial Division, which is of O(sqrt(n)) runtime but is deterministic, ensuring all the correct numbers that are joined into the main thread are prime numbers. Trivial Division also checks divisibility up to the square root of the number, reducing unnecessary iterations, contributing to further efficiency. In regard to experimental evaluation, I tested my program with the Trial Division and Miller-Rabin test methods for finding prime numbers. The former was better in time and accuracy while the latter took longer, likely due to the k number of tests per number in each thread, and did not have all the correct number of primes as Miller-Rabin is a probabilistic algorithm. Results for this conclusion are below:

- Trail Division: 
    - Time taken for the threads to process: 14196
    - Sum of the prime numbers: 279209790387276
    - Number of prime numbers: 5761455
    - Top 10 prime numbers: 99999787 99999821 99999827 99999839 99999847 99999931 99999941 99999959 99999971 99999989 

- Miller-Rabin:
    - Time taken for the threads to process: 702957
    - Sum of the prime numbers: 279209258578718
    - Number of prime numbers: 5761339
    - Top 10 prime numbers: 99999787 99999821 99999827 99999839 99999847 99999931 99999941 99999959 99999971 99999989 
