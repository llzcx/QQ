package JavaChat.Client.View;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CreatGroupView {
    private static double oldStageX;
    private static double oldStageY;
    private static double oldScreenX;
    private static double oldScreenY;
    static Stage loginStage;
    public static Parent root;
    public static void start(Stage primaryStage) throws IOException {
        loginStage = primaryStage;
        root = FXMLLoader.load(CreatGroupView.class.getResource("fxml/CreateGroup.fxml"));
        primaryStage.setTitle("创建群界面");
        Scene scene = new Scene(root,600,400);
        //        鼠标按下事件
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                oldStageX = primaryStage.getX();
                oldStageY = primaryStage.getY();
                oldScreenX = event.getScreenX();
                oldScreenY = event.getScreenY();
            }
        });

        //鼠标拖拽
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //新位置
                //拖拽前后的鼠标差值加上原始窗体坐标值
                primaryStage.setX(event.getScreenX() - oldScreenX + oldStageX);
                primaryStage.setY(event.getScreenY() - oldScreenY + oldStageY);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
    public static Object $(String id) {
        return (Object) root.lookup("#" + id);
    }

}
