package JavaChat.Client.View;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * 图片查看
 */
public class ViewPort extends Application {
    private String myPicPath=null;
    public ViewPort(String myPicPath) {
        this.myPicPath=myPicPath;
    }

    public static void OpenImage(String path){
        Platform.runLater(() -> {
            try {
                ViewPort v=new ViewPort(path);
                v.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static void main(String[] args) {
        OpenImage("file:D:\\JavaChatServerFile\\File\\mysql学习方向.jpg");
    }
    private int flag=90;
    private int reduceNum=10;
    private int addNum=10;
    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(700);
        stage.setWidth(800);
        Image image=new Image("file:src/JavaChat/Client/View/fxml/Image/查看图片.png");
        stage.getIcons().add(image);
        stage.setTitle("JavaChat_图片查看");
        VBox gp=new VBox();
        StackPane ap=new StackPane();
        ScrollPane sp=new ScrollPane();
        sp.setPrefHeight(570);
        sp.setPrefWidth(800);
        ap.setPrefHeight(570);
        ap.setPrefWidth(800);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //从不显示垂直ScrollBar
        sp.setPannable(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setContent(ap);
        Label label=new Label();
        ap.setAlignment(label, Pos.CENTER);
        Image seeImage=new Image(myPicPath);
        ImageView iv26=new ImageView(seeImage);
        if (seeImage.getHeight()>570){
            iv26.setFitHeight(570);
        }
        if (seeImage.getWidth()>800){
            iv26.setFitWidth(800);
        }
        final double iv26FitH = iv26.getFitHeight();
        final double iv26FitW = iv26.getFitWidth();
        label.setGraphic(iv26);
        ap.getChildren().add(label);
        //设置下面的部分
        HBox hbox=new HBox();
        Button add=new Button();

        add.setPrefWidth(40);
        Image image1=new Image("file:src/JavaChat/Client/View/fxml/Image/放大.png");
        ImageView iv1=new ImageView(image1);
        iv1.setFitHeight(image1.getHeight()/1.3);
        iv1.setFitWidth(image1.getWidth()/1.3);
        add.setGraphic(iv1);
        add.setStyle("-fx-background-color: rgba(255,255,255,0.1)");
        add.setOnMouseClicked(mouseEvent -> {
            iv26.setFitHeight(iv26.getFitHeight()+addNum);
            iv26.setFitWidth(iv26.getFitWidth()+addNum);
            label.setGraphic(iv26);
            ap.setAlignment(label, Pos.CENTER);
        });
        add.setOnMouseEntered(mouseEvent -> add.setStyle("-fx-background-color: rgba(190,190,190,0.4)"));
        add.setOnMouseExited(mouseEvent -> add.setStyle("-fx-background-color: rgba(255,255,255,0)"));
        Button reduce=new Button();
        reduce.setPrefWidth(40);
        Image image2=new Image("file:src/JavaChat/Client/View/fxml/Image/缩小.png");
        ImageView iv2=new ImageView(image2);
        iv2.setFitHeight(image2.getHeight()/1.3);
        iv2.setFitWidth(image2.getWidth()/1.3);
        reduce.setGraphic(iv2);
        reduce.setStyle("-fx-background-color: rgba(255,255,255,0)");

        reduce.setOnMouseClicked(mouseEvent -> {
           iv26.setFitHeight(iv26.getFitHeight()-reduceNum);
           iv26.setFitWidth(iv26.getFitWidth()-reduceNum);
           label.setGraphic(iv26);
           ap.setAlignment(label, Pos.CENTER);
        });
        gp.addEventFilter(ScrollEvent.SCROLL, event -> {
            double rate = (iv26.getFitWidth()*iv26.getFitHeight())/(iv26FitH*iv26FitW);
            if (event.getDeltaY() > 0) {
                if(rate > 4){
                    return;
                }
                iv26.setFitHeight(iv26.getFitHeight()+addNum);
                iv26.setFitWidth(iv26.getFitWidth()+addNum);
                label.setGraphic(iv26);
                ap.setAlignment(label, Pos.CENTER);

            } else {
                if(rate <0.16){
                    return;
                }
                iv26.setFitHeight(iv26.getFitHeight()-reduceNum);
                iv26.setFitWidth(iv26.getFitWidth()-reduceNum);
                label.setGraphic(iv26);
                ap.setAlignment(label, Pos.CENTER);
            }

        });
        reduce.setOnMouseEntered(mouseEvent -> reduce.setStyle("-fx-background-color: rgba(190,190,190,0.4)"));
        reduce.setOnMouseExited(mouseEvent -> reduce.setStyle("-fx-background-color: rgba(255,255,255,0)"));
        Button radio=new Button();
        radio.setPrefWidth(40);
        Image image3=new Image("file:src/JavaChat/Client/View/fxml/Image/旋转.png");
        ImageView iv3=new ImageView(image3);
        iv3.setFitHeight(image3.getHeight()/1.3);
        iv3.setFitWidth(image3.getWidth()/1.3);
        radio.setGraphic(iv3);
        radio.setStyle("-fx-background-color: rgba(255,255,255,0)");
        radio.setOnMouseClicked(mouseEvent -> {
            radio.setStyle("-fx-background-color: rgba(190,190,190,0.4)");
            if (label.getHeight()>570){
                label.setPrefHeight(570);
            }
            label.setRotate(flag);
            flag+=90;
        });
        radio.setOnMouseEntered(mouseEvent -> radio.setStyle("-fx-background-color: rgba(190,190,190,0.4)"));
        radio.setOnMouseExited(mouseEvent -> radio.setStyle("-fx-background-color: rgba(255,255,255,0)"));
        hbox.setSpacing(40);
        hbox.getChildren().addAll(add,radio,reduce);
        hbox.setPadding(new Insets(30,0,0,280));
        gp.getChildren().addAll(sp,hbox);
        gp.setStyle("-fx-background-color: white");
        Scene scene=new Scene(gp);
        stage.setScene(scene);
        stage.show();
    }

}