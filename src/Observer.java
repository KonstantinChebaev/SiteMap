import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Observer extends Thread {
    private Form form;
    private JTextField urlField;
    private final String primeUrl;
    private ArrayList<String> total;
    private BlockingQueue<String> toSave;
    private volatile boolean isFinished;
    private static volatile boolean mustSleep;
    private StringBuilder space;
    private Document doc;


    public Observer(Form form) {
        mustSleep = false;
        isFinished = false;
        this.form = form;
        this.urlField = form.getUrlField();
        primeUrl = urlField.getText();
        toSave = new LinkedBlockingDeque<>();
        total = new ArrayList<>();
        space = new StringBuilder("   ");
        doc = null;
    }
    private synchronized void sendToPost (String str){
        toSave.add(str);
    }

    public BlockingQueue<String> getToSaveList() throws InterruptedException {
        return toSave;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean f) {
        isFinished = f;
    }

    public static boolean isMustSleep() {
        return mustSleep;
    }

    public static void setSleepMode(boolean f) {
        mustSleep = f;
    }


    @Override
    public void run() {
        isFinished = false;
        this.starter(urlField.getText());
        form.stop();
    }


    public void starter(String urlPath) {
        total.add(urlPath);
        System.out.println("start starting");
        SwingUtilities.invokeLater(() -> {
            form.setVisitLabel(Integer.toString(total.size()));
        });

        try {
            doc = Jsoup.connect(urlPath).maxBodySize(1024 * 1024 * 10)
                    .timeout(0).ignoreContentType(true)
                    .execute().parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        toSave.add(space + "--->" + urlPath);
        space.append("   ");
        Elements elmnts = doc.select("a[href]");
        for (Element element : elmnts) {
            try {
                sleep(50);
                while (mustSleep) {
                    sleep(10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            if (isFinished) {
                return;
            }
            String link = element.attr("href");
            System.out.println(space + link);
            if (link.contains("#") || link.equals("/") || urlPath.contains(".html")|| urlPath.contains("tel:+")|| urlPath.contains("mailto:")) {
                continue;
            }
            if (!link.contains(":") && !link.contains("http")) {
                this.sendToPost(space + urlPath + link);
            } else {
                this.sendToPost(space + link);
            }
            if (total.contains(link)||total.contains(urlPath + link)){
                continue;
            }

            if (link.contains(primeUrl)) {
                this.starter(link);
            } else if (!link.contains(":") && !link.contains("http") && !urlPath.contains(link)) {
                total.add(link);
                this.starter(urlPath + link);
            }
        }
        space.delete(0, 3);
        System.out.println("end starting" + space.length());
    }
}




