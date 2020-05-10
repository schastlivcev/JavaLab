package ru.kpfu.itis.rodsher.jlmq;

public class InfiniteThread extends Thread {
    private Object object;

    public InfiniteThread() {
        super();
        object = new Object();
    }

    @Override
    public void run() {
        freeze();
    }

    private void freeze() {
        synchronized (object) {
            try {
                object.wait();
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}