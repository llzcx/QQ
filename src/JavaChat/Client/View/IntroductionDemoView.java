package JavaChat.Client.View;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class IntroductionDemoView {
    static Stage loginStage;
    public static Parent root = null;
    public static void start(Stage primaryStage) throws IOException {
        loginStage = primaryStage;
        root = FXMLLoader.load(GroupNoticeView.class.getResource("fxml/IntroductionDemo.fxml"));
        primaryStage.setTitle("个人介绍");
    }
}
