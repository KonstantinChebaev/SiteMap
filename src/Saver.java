import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class Saver extends Thread {
    private String path;
    private String fileName;
    private Observer obs;
    private int totalCount;
    private Form form;
    private BlockingQueue<String> toSave;

    public Saver(Observer obs, String path, String fileName, Form form) {
        this.path = path;
        this.fileName = fileName.replaceAll("[^\\w]", "") + ".txt";
        this.obs = obs;
        totalCount = 0;
        this.form = form;
        try {
            toSave = obs.getToSaveList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }

    @Override
    public void run() {
        Path pathMaster = Paths.get(path, fileName);
        File file = pathMaster.toFile();
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true))) {
                while (!(Thread.currentThread().isInterrupted())) {
                    try {
                        final String nextLine = toSave.take();
                        out.write(nextLine);
                        out.newLine();
                        totalCount++;
                        SwingUtilities.invokeLater(() -> {
                            form.setWriteLabel(Integer.toString(totalCount));
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                }
                final LinkedList <String> remainingObjects = new LinkedList<>();
                toSave.drainTo(remainingObjects);
                for(String nextLine : remainingObjects) {
                    out.write(nextLine);
                    out.newLine();
                    totalCount++;
                    SwingUtilities.invokeLater(() -> {
                        form.setWriteLabel(Integer.toString(totalCount));
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
