import JavaChat.Common.Pojo.FragmentFile;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

public class ListViewContextMenuExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        ListView<String> listView = new ListView<>();

        listView.getItems().addAll("One", "Two", "Three");

        listView.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();


            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
            editItem.setOnAction(event -> {
                String item = cell.getItem();
                // code to edit item...
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
            deleteItem.setOnAction(event -> listView.getItems().remove(cell.getItem()));
            contextMenu.getItems().addAll(editItem, deleteItem);

           cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });

        BorderPane root = new BorderPane(listView);
        primaryStage.setScene(new Scene(root, 250, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        String N = "12345 20220419 111.exe";
        File file = new File("D:\\JavaChatServerFile\\File\\111.exe");
        byte[] FileByte = new byte[0];
        try {
            FileByte = FileUtils.FileChangeToByte(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] finalFileByte = FileByte;
        Thread t = new Thread(() -> {
            int lastLen = finalFileByte.length%1024;
            String time = GetTimeUtils.GetNowTime();
            Object[] bs = FileUtils.splitByteArr(finalFileByte,1024);
            byte[] b = (byte[]) bs[bs.length-1];
            bs[bs.length-1] = new byte[lastLen];
            for (int i = 0; i < lastLen ; i++) {
                ((byte[])bs[bs.length-1])[i] = b[i];
            }

            String path = ServerFileAddress.File+"\\"+N;
            for (int i = 0; i < bs.length; i++) {
                FileUtils.saveFile((byte[])bs[i], path,((Integer)(i+1)).toString());
            }

            byte[] bytes = new byte[0];

            for (int i = 1; i <= bs.length; i++) {
                String path1 = path +"\\"+ ((Integer)i).toString();
                File file1 =new File(path1);
                byte[] newbytes = new byte[0];
                try {
                    newbytes = FileUtils.FileChangeToByte(file1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bytes = FileUtils.byteMerger(bytes,newbytes);
            }

            String p =FileUtils.saveFile(bytes,ClientFileAddress.File,file.getName());
            System.out.println(p);

        });
        t.start();
    }

}