package lab2;

import java.util.concurrent.CountDownLatch;

public class Sub1 {
    public synchronized void run() throws InterruptedException {
        // Варіант 18, 1) MА= min(D+ В)*MD*MT+MX*ME;

        System.out.println("Starting sub1...");
        long startTime = System.nanoTime();

        Vector D = new Vector("./sub1/D.txt").filledWithRandomValues();
        Vector B = new Vector("./sub1/B.txt").filledWithRandomValues();

        Matrix MD = new Matrix("./sub1/MD.txt").filledWithRandomValues();
        Matrix MT = new Matrix("./sub1/MT.txt").filledWithRandomValues();
        Matrix MX = new Matrix("./sub1/MX.txt").filledWithRandomValues();
        Matrix ME = new Matrix("./sub1/ME.txt").filledWithRandomValues();

        double[] min_D_B = new double[1];

        CountDownLatch counter = new CountDownLatch(3);
        Thread task1 = new Thread(() -> {
            min_D_B[0] = D.sumWithVector(B).findMin();
            counter.countDown();
        });

        Thread task2 = new Thread(() -> {
            MX.multiplyWithMatrix(ME);
            counter.countDown();
        });

        Thread task3 = new Thread(() -> {
            MD.multiplyWithMatrix(MT);
            counter.countDown();
        });

        Thread task4 = new Thread(() -> {
            System.out.println("MA:");
            try {
                counter.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MX.multiplyWithMatrix(MD).multiplyWithDouble(min_D_B[0]).printToConsole().saveToFile("./sub1/MA.txt");
            long elapsedTime = System.nanoTime() - startTime;
            System.out.println("Total execution time sub1 in millis: " + elapsedTime / 1000000);

        });

        task1.start();
        task2.start();
        task3.start();

        task1.join();
        task2.join();
        task3.join();

        task4.start();
        task4.join();

    }

}
