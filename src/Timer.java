import javax.swing.*;

public class Timer extends Thread {
    private JLabel timeLabel;
    private long startMilis;
    private long currMilis;
    private Observer obs;
    private long millis;

    public Timer(JLabel timeLabel, Observer obs) {
        this.timeLabel = timeLabel;
        startMilis = System.currentTimeMillis();
        currMilis = startMilis;
        this.obs = obs;
        millis = 0L;
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
                return;
            }
            currMilis = System.currentTimeMillis();
            millis += currMilis - startMilis;
            startMilis = currMilis;
            SwingUtilities.invokeLater(() -> {
                timeLabel.setText(String.format("%d:%02d", millis / 1000 / 60, millis / 1000 % 1000));
            });

        }
    }


}
