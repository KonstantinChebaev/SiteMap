import javax.swing.*;

public class Timer extends Thread {
    private JLabel timeLabel;
    private long startMilis;
    private long currMilis;
    private Observer obs;
    private long millis;
    private int sec;
    private int min;

    public Timer(JLabel timeLabel, Observer obs) {
        this.timeLabel = timeLabel;
        startMilis = System.currentTimeMillis();
        currMilis = startMilis;
        this.obs = obs;
        millis = 0L;
        sec = 0;
        min = 0;
    }

    @Override
    public void run() {
        super.run();
        while (!obs.isFinished()) {
            try {
                sleep(10);
                while (Observer.isMustSleep()) {
                    startMilis = System.currentTimeMillis();
                    sleep(10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            currMilis = System.currentTimeMillis();
            millis += currMilis - startMilis;
            startMilis = currMilis;
            min = (int)millis / 1000 / 60;
            sec =  (int)millis / 1000 % 1000 - min*60;

            SwingUtilities.invokeLater(() -> {
                timeLabel.setText(String.format("%d:%02d", min, sec));
            });

        }
    }


}
