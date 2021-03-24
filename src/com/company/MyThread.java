package com.company;

public class MyThread extends Thread{

    private final String threadName;

    MyThread( String name) {
        threadName = name;
    }

    public void run()
    {
        System.out.println("Thread " +  threadName + " exiting.");
    }

}
