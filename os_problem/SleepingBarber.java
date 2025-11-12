/**
 * SLEEPING BARBER PROBLEM
 * 
 * Problem: A barbershop has:
 * - 1 barber (can only serve one customer at a time)
 * - 1 barber chair (where haircut happens)
 * - N waiting chairs (customers wait here)
 * 
 * Rules:
 * 1. If no customers, barber sleeps
 * 2. When customer arrives:
 *    - If barber is sleeping, wake him up
 *    - If barber is busy and chair available, customer waits
 *    - If all chairs full, customer leaves
 * 3. When barber finishes:
 *    - If customers waiting, serve next one
 *    - If no customers, go to sleep
 * 
 * This is a classic producer-consumer problem with bounded buffer!
 */
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SleepingBarber {
    // ========== CONFIGURATION ==========
    private static final int NUM_CHAIRS = 3; // Number of waiting chairs in barbershop
    private static final int NUM_CUSTOMERS = 10; // Total customers to simulate
    
    // ========== SYNCHRONIZATION PRIMITIVES ==========
    
    // customers: Signals that a customer is waiting
    // - Barber waits on this (sleeps if no customers)
    // - Customers signal this (wake barber)
    // - Initialized to 0 (no customers initially)
    private static final Semaphore customers = new Semaphore(0);
    
    // barber: Signals that barber is ready to cut hair
    // - Customers wait on this (wait for barber to be ready)
    // - Barber signals this (when ready to serve)
    // - Initialized to 0 (barber not ready initially)
    private static final Semaphore barber = new Semaphore(0);
    
    // accessSeats: Mutex to protect waiting room (prevents race conditions)
    // - Only one thread can check/modify waiting room at a time
    // - Initialized to 1 (unlocked)
    private static final Semaphore accessSeats = new Semaphore(1);
    
    // waitingCustomers: Count of customers currently in waiting room
    // - AtomicInteger ensures thread-safe increment/decrement
    private static final AtomicInteger waitingCustomers = new AtomicInteger(0);
    
    public static void main(String[] args) {
        System.out.println("=== SLEEPING BARBER PROBLEM ===\n");
        System.out.println("Barbershop setup:");
        System.out.println("  - 1 barber");
        System.out.println("  - 1 barber chair");
        System.out.println("  - " + NUM_CHAIRS + " waiting chairs\n");
        
        // Create and start the barber thread (runs forever)
        Thread barberThread = new Thread(new Barber());
        barberThread.start();
        
        // Create and start customer threads (arrive one by one)
        for (int i = 0; i < NUM_CUSTOMERS; i++) {
            new Thread(new Customer(i)).start();
            // Stagger customer arrivals (1 second apart)
            // Makes the simulation more realistic and easier to follow
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * BARBER THREAD
     * 
     * The barber's job:
     * 1. Sleep when no customers
     * 2. Wake up when customer arrives
     * 3. Serve customers one by one
     * 4. Go back to sleep if no more customers
     */
    static class Barber implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    // ========== STEP 1: SLEEP (Wait for customers) ==========
                    System.out.println("Barber is sleeping... (no customers)");
                    // acquire() blocks if no permits available
                    // Since customers starts at 0, barber sleeps until a customer signals
                    customers.acquire(); // Wait for a customer (blocks here if no customers)
                    
                    // ========== STEP 2: WAKE UP (Customer arrived) ==========
                    // A customer has signaled! Now we need to:
                    // 1. Update waiting room count
                    // 2. Signal that barber is ready
                    
                    // Lock the waiting room to safely update it
                    accessSeats.acquire();
                    
                    // One customer is leaving the waiting room (coming to barber chair)
                    waitingCustomers.decrementAndGet();
                    
                    // Signal that barber is ready to cut hair
                    // This will wake up the waiting customer
                    barber.release();
                    
                    // Unlock the waiting room
                    accessSeats.release();
                    
                    // ========== STEP 3: CUT HAIR (Serve customer) ==========
                    System.out.println("Barber is cutting hair...");
                    TimeUnit.SECONDS.sleep(3); // Simulate haircut time
                    System.out.println("Barber finished cutting hair");
                    
                    // Loop back to check for more customers (or sleep if none)
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * CUSTOMER THREAD
     * 
     * Each customer's journey:
     * 1. Arrive at barbershop
     * 2. Check if waiting chair available
     * 3. If yes: sit and wait, wake barber if sleeping
     * 4. If no: leave (barbershop full)
     * 5. Wait for barber to be ready
     * 6. Get haircut
     */
    static class Customer implements Runnable {
        private final int id;
        
        Customer(int id) {
            this.id = id;
        }
        
        @Override
        public void run() {
            try {
                // ========== STEP 1: ARRIVE AT BARBERSHOP ==========
                System.out.println("Customer " + id + " arrived at barbershop");
                
                // Lock the waiting room to check availability safely
                accessSeats.acquire();
                
                // ========== STEP 2: CHECK IF CHAIR AVAILABLE ==========
                if (waitingCustomers.get() < NUM_CHAIRS) {
                    // ========== CASE A: CHAIR AVAILABLE - SIT AND WAIT ==========
                    
                    // Increment waiting room count
                    waitingCustomers.incrementAndGet();
                    System.out.println("Customer " + id + " is waiting (waiting room: " + 
                                     waitingCustomers.get() + "/" + NUM_CHAIRS + ")");
                    
                    // Signal that a customer is waiting
                    // This will wake the barber if he's sleeping
                    // If barber is already awake, this adds to the queue
                    customers.release();
                    
                    // Unlock waiting room (we're done modifying it)
                    accessSeats.release();
                    
                    // ========== STEP 3: WAIT FOR BARBER TO BE READY ==========
                    // The barber will signal when he's ready to serve us
                    barber.acquire(); // Wait here until barber signals
                    
                    // ========== STEP 4: GETTING HAIRCUT ==========
                    // We're now in the barber chair!
                    // The barber is cutting our hair (happens in barber thread)
                    System.out.println("Customer " + id + " is getting a haircut");
                    // Haircut happens here (barber thread is working)
                    
                } else {
                    // ========== CASE B: NO CHAIRS AVAILABLE - LEAVE ==========
                    System.out.println("Customer " + id + " left - no free chairs available (barbershop full!)");
                    accessSeats.release(); // Unlock before leaving
                }
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

