package JavaChat.Client.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ForgetView {
    public static Stage loginStage;
    public static Parent root;
    public static void start(Stage primaryStage) throws IOException {
        loginStage = primaryStage;
        root = FXMLLoader.load(ForgetView.class.getResource("fxml/Forget.fxml"));
        primaryStage.setTitle("重置密码");
        Scene scene = new Scene(root,700,450);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
    public static Object $(String id) {
        return root.lookup("#" + id);
    }
}
