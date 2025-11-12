/**
 * DINING PHILOSOPHERS PROBLEM
 * 
 * Problem: 5 philosophers sit around a table with 5 chopsticks.
 * Each philosopher needs 2 chopsticks to eat (left and right).
 * 
 * Challenge: Prevent deadlock when all philosophers try to eat simultaneously.
 * 
 * This implementation uses semaphores to represent chopsticks.
 * WARNING: This basic version can still deadlock if all philosophers
 * pick up their left chopstick at the same time!
 */
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class DiningPhilosophers {
    // ========== CONFIGURATION ==========
    private static final int NUM_PHILOSOPHERS = 5; // Number of philosophers around the table
    
    // ========== SHARED RESOURCES ==========
    // Each chopstick is a semaphore with 1 permit (available = 1, taken = 0)
    // Semaphore(1) means only 1 philosopher can hold this chopstick at a time
    private static final Semaphore[] chopsticks = new Semaphore[NUM_PHILOSOPHERS];

    public static void main(String[] args) {
        System.out.println("=== DINING PHILOSOPHERS PROBLEM ===\n");
        System.out.println("Setting up " + NUM_PHILOSOPHERS + " philosophers and " + NUM_PHILOSOPHERS + " chopsticks\n");
        
        // Initialize each chopstick as a semaphore with 1 permit (available)
        // Semaphore(1) = binary semaphore (like a mutex)
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            chopsticks[i] = new Semaphore(1);
        }

        // Create and start one thread for each philosopher
        // Each philosopher runs independently in parallel
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            new Thread(new Philosopher(i)).start();
        }
    }

    /**
     * Each philosopher represents a thread that:
     * 1. Thinks (doesn't need resources)
     * 2. Tries to pick up 2 chopsticks (needs resources)
     * 3. Eats (uses resources)
     * 4. Puts down chopsticks (releases resources)
     * 5. Repeats forever
     */
    static class Philosopher implements Runnable {
        private final int id; // Philosopher ID (0 to NUM_PHILOSOPHERS-1)

        Philosopher(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                // ========== STEP 1: THINKING (No resources needed) ==========
                System.out.println("Philosopher " + id + " is thinking...");
                sleep(1); // Think for 1 second

                try {
                    // ========== STEP 2: PICK UP LEFT CHOPSTICK ==========
                    // Each philosopher's left chopstick is at position [id]
                    // acquire() blocks if chopstick is taken (permit = 0)
                    // acquire() succeeds if chopstick is available (permit = 1)
                    chopsticks[id].acquire();
                    System.out.println("Philosopher " + id + " picked up LEFT chopstick " + id);

                    // ========== STEP 3: PICK UP RIGHT CHOPSTICK ==========
                    // Right chopstick is at position [(id + 1) % NUM_PHILOSOPHERS]
                    // The modulo (%) wraps around: philosopher 4's right is chopstick 0
                    // This creates the circular arrangement
                    chopsticks[(id + 1) % NUM_PHILOSOPHERS].acquire();
                    System.out.println("Philosopher " + id + " picked up RIGHT chopstick " + ((id + 1) % NUM_PHILOSOPHERS));

                    // ========== STEP 4: EATING (Has both chopsticks) ==========
                    System.out.println("Philosopher " + id + " is EATING (has both chopsticks)");
                    sleep(2); // Eat for 2 seconds

                    // ========== STEP 5: PUT DOWN LEFT CHOPSTICK ==========
                    // release() gives the permit back, making chopstick available again
                    chopsticks[id].release();
                    System.out.println("Philosopher " + id + " put down LEFT chopstick " + id);

                    // ========== STEP 6: PUT DOWN RIGHT CHOPSTICK ==========
                    chopsticks[(id + 1) % NUM_PHILOSOPHERS].release();
                    System.out.println("Philosopher " + id + " put down RIGHT chopstick " + ((id + 1) % NUM_PHILOSOPHERS));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                // Loop back to thinking (infinite cycle)
            }
        }

        /**
         * Helper method to sleep for specified seconds
         * Makes the simulation more visible and realistic
         */
        private void sleep(int seconds) {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
