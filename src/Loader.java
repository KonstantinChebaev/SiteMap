import javax.swing.*;

/*

итак
у нас есть вот такие штуки
http://petrushin.ru/
http://tvoypsiholog.com/
https://mariadolgopolova.ru/
https://sashastrogonova.ru/
http://nadezhkin-ilya.ru/

 */


public class Loader {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                Form form = new Form();
                frame.setContentPane(form.getRootPanel());
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setSize(450,400);
                frame.setResizable(false);
                frame.setTitle("КАРТА САЙТА");
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

}
