import java.io.*;

public class Saver extends Thread {
    private String path;
    private String fileName;
    private Observer obs;
    private int totalCount;
    private Form form;

    public Saver(Observer obs, String path, String fileName, Form form) {
        this.path = path;
        this.fileName = fileName.replaceAll("[^\\w]", "");
        this.obs = obs;
        totalCount = 0;
        this.form = form;
    }

    @Override
    public void run() {
        super.run();
        File file = new File(path + "\\" + fileName + ".txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
            try {
                while (!obs.isFinished()) {
                    try {
                        sleep(10);
                        while (Observer.isMustSleep()) {
                            sleep(10);
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                    if (obs.hasAnyAdress()) {
                        out.write(obs.getNextAdress());
                        out.newLine();
                        totalCount++;
                        form.setWriteLabel(Integer.toString(totalCount));
                    }

                }
            } finally {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
