import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Form {
    private JFileChooser fileChooser;
    private JPanel rootPanel;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton startButon;
    private JTextField urlField;
    private JPanel filePanel;
    private JButton chooserButton;
    private JTextField pathField;
    private JPanel animePanel;
    private JPanel soughtPanel;
    private JPanel mainPanel;
    private JPanel visitPanel;
    private JPanel sitePanel;
    private JPanel tinePanel;
    private JLabel visitLabel;
    private JLabel writeLabel;
    private JLabel timeLabel;
    private Observer obs;
    private Saver saver;
    private Timer timer;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setRootPanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
    }

    public JPanel getFilePanel() {
        return filePanel;
    }

    public void setFilePanel(JPanel filePanel) {
        this.filePanel = filePanel;
    }

    public void stop (){
        obs.setFinished(true);
        obs.interrupt();
        saver.interrupt();
        timer.interrupt();
        pathField.setEditable(true);
        urlField.setEditable(true);
        startButon.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
    }


    public Form() {
        Form form = this;
        chooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("D:/Materials"));
                fileChooser.setDialogTitle("Выберите папку");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = fileChooser.showDialog(null, "Выбрать папку");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    pathField.setText(file.getPath());
                }

            }
        });
        startButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Observer.isMustSleep()) {
                    Observer.setSleepMode(false);
                } else {
                    obs = new Observer(form);
                    obs.start();
                    saver = new Saver(obs, pathField.getText(), urlField.getText(), form);
                    saver.start();
                    timer = new Timer(timeLabel, obs);
                    timer.start();
                    pathField.setEditable(false);
                    urlField.setEditable(false);
                }
                startButon.setEnabled(false);
                stopButton.setEnabled(true);
                pauseButton.setEnabled(true);
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obs.setFinished(true);
                obs.interrupt();
                saver.interrupt();
                timer.interrupt();
                pathField.setEditable(true);
                urlField.setEditable(true);
                startButon.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Observer.setSleepMode(true);
                startButon.setEnabled(true);
                pauseButton.setEnabled(false);
            }
        });
    }

    public JTextField getPathField() {
        return pathField;
    }

    public void setPathField(JTextField pathField) {
        this.pathField = pathField;
    }

    public JTextField getUrlField() {
        return urlField;
    }

    public void setUrlField(JTextField urlField) {
        this.urlField = urlField;
    }


    public void setVisitLabel(String str) {
        visitLabel.setText(str);
    }

    public void setWriteLabel(String str) {
        writeLabel.setText(str);
    }

}
