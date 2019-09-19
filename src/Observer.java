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
    private boolean finish;
    private static boolean mustSleep;
    private StringBuilder space;


    Document doc = null;

    public Observer(Form form) {
        mustSleep = false;
        finish = false;
        this.form = form;
        this.urlField = form.getUrlField();
        primeUrl = urlField.getText();
        toSave = new LinkedBlockingDeque<>();
        total = new ArrayList<>();
        space = new StringBuilder("   ");

    }

    public String getNextAdress() throws InterruptedException {
        return toSave.take();
    }

    public Boolean hasAnyAdress() {
        return !toSave.isEmpty();
    }

    public Boolean isFinished() {
        return finish;
    }

    public void setFinished(boolean f) {
        finish = f;
    }

    public static Boolean isMustSleep() {
        return mustSleep;
    }

    public static void setSleepMode(boolean f) {
        mustSleep = f;
    }


    @Override
    public void run() {
        super.run();
        finish = false;
        this.starter(urlField.getText());
        finish = true;
    }


    public void starter(String urlPath) {
        SwingUtilities.invokeLater(() -> {
            form.setVisitLabel(Integer.toString(total.size()));
        });

        try {
            doc = Jsoup.connect(urlPath).maxBodySize(1024 * 1024 * 10)
                    .timeout(0).ignoreContentType(true)
                    .execute().parse();
        } catch (org.jsoup.HttpStatusException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(space + "--->" + urlPath);
        toSave.add(space + "--->" + urlPath);
        space.append("   ");
        Elements links = doc.select("a[href]");
        for (Element element : links) {
            try {
                sleep(500);
                while (mustSleep) {
                    sleep(10);
                }
            } catch (InterruptedException e) {
                return;
            }
            if (finish) {
                return;
            }
            String link = element.attr("href");

            if (link.contains("#") || link.equals("/")) {
                continue;
            }

            System.out.println(space + link);

            if (!link.contains(":") && !link.contains("http")) {
                toSave.add(space + urlPath + link);
            } else {
                toSave.add(space + link);
            }
            if (total.contains(link) || urlPath.contains(".html")) {
                continue;
            }

            total.add(link);

            if (link.contains(primeUrl)) {
                this.starter(link);
            } else if (!link.contains(":") && !link.contains("http")) {
                this.starter(urlPath + link);
            }
        }
        space.substring(0, space.length() - 3);
    }
}




