import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Saver extends Thread {
    private String path;
    private String fileName;
    private Observer obs;
    private int totalCount;
    private Form form;

    public Saver(Observer obs, String path, String fileName, Form form) {
        this.path = path;
        this.fileName = fileName.replaceAll("[^\\w]", "") + ".txt";
        this.obs = obs;
        totalCount = 0;
        this.form = form;
    }

    @Override
    public void run() {
        super.run();
        Path pathMaster = Paths.get(path, fileName);
        File file = pathMaster.toFile();
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true))) {
                while (!obs.isFinished()) {
                    try {
                        sleep(10);
                        while (Observer.isMustSleep()) {
                            sleep(10);
                        }
                        if (obs.hasAnyAdress()) {
                            out.write(obs.getNextAdress());
                            out.newLine();
                            totalCount++;
                            form.setWriteLabel(Integer.toString(totalCount));
                        }
                    } catch (InterruptedException e) {
                        return;
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
