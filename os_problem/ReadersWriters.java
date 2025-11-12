/**
 * READERS-WRITERS PROBLEM
 * 
 * Problem: Multiple threads want to access shared data.
 * - READERS: Can read simultaneously (multiple readers at once)
 * - WRITERS: Need exclusive access (only one writer at a time, no readers)
 * 
 * Rules:
 * 1. Multiple readers can read at the same time (shared access)
 * 2. Only one writer can write at a time (exclusive access)
 * 3. Writers block all readers (no reading while writing)
 * 4. Readers block writers (no writing while reading)
 * 
 * Real-world example: Database with SELECT (readers) and UPDATE (writers)
 */
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ReadersWriters {
    // ========== CONFIGURATION ==========
    private static final int NUM_READERS = 5; // Number of reader threads
    private static final int NUM_WRITERS = 2; // Number of writer threads
    
    // ========== SHARED DATA ==========
    // This is the resource that readers and writers access
    private static int sharedData = 0;
    
    // ========== SYNCHRONIZATION PRIMITIVES ==========
    // readCount: How many readers are currently reading
    private static int readCount = 0;
    
    // mutex: Protects readCount from race conditions
    // Only one thread can modify readCount at a time
    private static final Semaphore mutex = new Semaphore(1);
    
    // writeMutex: Ensures exclusive access for writers
    // When acquired, no other writer or reader can access sharedData
    private static final Semaphore writeMutex = new Semaphore(1);
    
    public static void main(String[] args) {
        System.out.println("=== READERS-WRITERS PROBLEM ===\n");
        System.out.println("Shared data starts at: " + sharedData);
        System.out.println("Creating " + NUM_READERS + " readers and " + NUM_WRITERS + " writers\n");
        
        // Create and start all reader threads
        for (int i = 0; i < NUM_READERS; i++) {
            new Thread(new Reader(i)).start();
        }
        
        // Create and start all writer threads
        for (int i = 0; i < NUM_WRITERS; i++) {
            new Thread(new Writer(i)).start();
        }
    }
    
    /**
     * READER THREAD
     * 
     * Readers can read simultaneously, but must coordinate to:
     * - Block writers when ANY reader is reading
     * - Allow writers when NO readers are reading
     */
    static class Reader implements Runnable {
        private final int id;
        
        Reader(int id) {
            this.id = id;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    // ========== ENTRY SECTION: Request permission to read ==========
                    
                    // Step 1: Lock readCount (only one reader can modify it at a time)
                    mutex.acquire();
                    
                    // Step 2: Increment the number of active readers
                    readCount++;
                    
                    // Step 3: If this is the FIRST reader, block all writers
                    // This ensures no writer can write while ANY reader is reading
                    if (readCount == 1) {
                        writeMutex.acquire(); // Block writers
                    }
                    
                    // Step 4: Unlock readCount (other readers can now update it)
                    mutex.release();
                    
                    // ========== CRITICAL SECTION: Actually reading the data ==========
                    // Multiple readers can be here simultaneously!
                    System.out.println("Reader " + id + " is reading data: " + sharedData);
                    sleep(2); // Simulate reading time
                    System.out.println("Reader " + id + " finished reading");
                    
                    // ========== EXIT SECTION: Release permission ==========
                    
                    // Step 5: Lock readCount again
                    mutex.acquire();
                    
                    // Step 6: Decrement the number of active readers
                    readCount--;
                    
                    // Step 7: If this is the LAST reader, allow writers again
                    // Only when NO readers are reading can writers proceed
                    if (readCount == 0) {
                        writeMutex.release(); // Allow writers
                    }
                    
                    // Step 8: Unlock readCount
                    mutex.release();
                    
                    // ========== NON-CRITICAL SECTION: Do other work ==========
                    sleep(3); // Do something else before reading again
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void sleep(int seconds) {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * WRITER THREAD
     * 
     * Writers need EXCLUSIVE access:
     * - No other writer can write at the same time
     * - No readers can read while a writer is writing
     */
    static class Writer implements Runnable {
        private final int id;
        
        Writer(int id) {
            this.id = id;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    // ========== ENTRY SECTION: Request exclusive access ==========
                    // acquire() will block if:
                    // - Another writer is writing, OR
                    // - Any reader is reading (writeMutex is held by first reader)
                    writeMutex.acquire();
                    
                    // ========== CRITICAL SECTION: Actually writing the data ==========
                    // Only ONE writer can be here at a time!
                    // No readers can read while we're here!
                    int oldValue = sharedData;
                    int newValue = sharedData + 1;
                    System.out.println("Writer " + id + " is writing: " + oldValue + " -> " + newValue);
                    sleep(2); // Simulate writing time
                    sharedData = newValue; // Update the shared data
                    System.out.println("Writer " + id + " finished writing. New value: " + sharedData);
                    
                    // ========== EXIT SECTION: Release exclusive access ==========
                    // Now other writers or readers can proceed
                    writeMutex.release();
                    
                    // ========== NON-CRITICAL SECTION: Do other work ==========
                    sleep(4); // Do something else before writing again
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void sleep(int seconds) {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

