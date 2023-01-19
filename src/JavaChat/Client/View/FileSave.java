package JavaChat.Client.View;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;


public class FileSave extends Application {

    private Button btnOpen,btnSave;
    private TextArea textArea = new TextArea();
    private BorderPane borderPane = new BorderPane();

    //创建单行面板，面板中各组件的间距为30像素
    private HBox hBox = new HBox(30);

    @Override
    public void start(Stage primaryStage) throws Exception {
        btnOpen = new Button("选取");
        btnSave = new Button("存盘");
        //将两个按钮添加到单行面板中
        hBox.getChildren().addAll(btnOpen,btnSave);
        //单行面板中设置组件水平居中放置
        hBox.setAlignment(Pos.CENTER);
        borderPane.setBottom(hBox);
        borderPane.setCenter(textArea);
        //按钮事件监听
        btnOpen.setOnAction(e ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("文件选择对话框");
            //设置将当前目录作为初始显示目录
            fileChooser.setInitialDirectory(new File("."));
            //创建文件选择过滤器
            FileChooser.ExtensionFilter filter =
                    new FileChooser.ExtensionFilter("所有.java文件","*.java");
            //设置文件过滤器
            fileChooser.getExtensionFilters().add(filter);
            //创建打开文件选择对话框
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file!=null){
                try{
                    Scanner scanner = new Scanner(file);
                    StringBuffer stringBuffer = new StringBuffer();
                    while(scanner.hasNext()){
                        //每次读一行，并在其后添加回车符和换行符
                        stringBuffer.append(scanner.nextLine()+"\r\n");
                    }
                    //将读取到的数据放入文本区中显示
                    textArea.setText(stringBuffer.toString());
                }catch(FileNotFoundException ex){}
            }else{
                textArea.setText("没有选择文件");
            }
        });
        btnSave.setOnAction(e->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("文件保存对话框");
            fileChooser.setInitialDirectory(new File("."));
            FileChooser.ExtensionFilter filter =
                    new FileChooser.ExtensionFilter(".java","*.java");
            fileChooser.getExtensionFilters().add(filter);
            File file = fileChooser.showSaveDialog(primaryStage);
            if(file != null){
                //将文本区中的内容转化为字节存入数组中并写入对应的文件
                try{
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    byte[] b = (textArea.getText()).getBytes();
                    out.write(b,0,b.length);
                    out.close();
                }catch(IOException ex){}
            }
        });
        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("文件选择对话框程序");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}