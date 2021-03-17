package lab2;

import java.util.concurrent.CountDownLatch;

public class Sub2 {

    public synchronized void run() throws InterruptedException {
        // Варіант 18, 2) C=В*МT+D*MX*a;

        System.out.println("Starting sub2...");
        long startTime = System.nanoTime();

        Vector B = new Vector("./sub2/B.txt").filledWithRandomValues();
        Vector D = new Vector("./sub2/D.txt").filledWithRandomValues();

        Matrix MT = new Matrix("./sub2/MT.txt").filledWithRandomValues();
        Matrix MX = new Matrix("./sub2/MX.txt").filledWithRandomValues();

        double fixed_a = 2;

        CountDownLatch counter = new CountDownLatch(2);

        Thread task1 = new Thread(() -> {
            Matrix copy_MT = MT;
            B.multiplyWithMatrix(copy_MT);
            counter.countDown();
        });

        Thread task2 = new Thread(() -> {
            Matrix copy_MX = MX;
            D.multiplyWithDouble(fixed_a).multiplyWithMatrix(copy_MX);
            counter.countDown();
        });

        Thread task3 = new Thread(() -> {
            System.out.println("C:");
            try {
                counter.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            D.sumWithVector(B).printToConsole().saveToFile("./sub2/C.txt");
            long elapsedTime = System.nanoTime() - startTime;
            System.out.println("Total execution time sub2 in millis: " + elapsedTime / 1000000);
        });

        task1.start();
        task2.start();

        task1.join();
        task2.join();

        task3.start();
        task3.join();

    }

}
