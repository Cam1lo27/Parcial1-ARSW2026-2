package edu.eci.arsw.blacklistvalidator;

public class OccurrencesCounter {

    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int get() {
        return count;
    }
}
