import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MapBuilder {
    public static int level = 0;
    public static void main(String[] args) {
        String path = "";
        System.out.println("Введите путь:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            path =reader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        path =  path.replace("\\", "\\\\");

        getThree(path,level);
    }
    public static void getThree (String path, int lev){
        File dir = new File(path);
        File  files [] = dir.listFiles();
        String intend = "";
        for(int a = lev;a>0;a--)intend+="  ";
        for(File file :files){
            if(file.isFile()) {
                System.out.println(intend + file.getName() + " - " + file.length()+" b");
            }else {
                System.out.println(intend + file.getName());
                getThree(path + "\\" + file.getName(), lev + 1);
            }
        }
    }
}
