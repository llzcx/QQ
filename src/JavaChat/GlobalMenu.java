package JavaChat;

import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.ServerFileAddress;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.UUID;


@SuppressWarnings("restriction")
public class GlobalMenu extends ContextMenu {
    /* 单例 */
    private static GlobalMenu INSTANCE = null;
    /** * 私有构造函数 */
    private GlobalMenu() {
        MenuItem settingMenuItem = new MenuItem("set");
        MenuItem updateMenuItem = new MenuItem("update");
        MenuItem feedbackMenuItem = new MenuItem("help");
        MenuItem aboutMenuItem = new MenuItem("File");
        MenuItem companyMenuItem = new MenuItem("about");
        getItems().add(settingMenuItem);
        getItems().add(updateMenuItem);
        getItems().add(companyMenuItem);
        getItems().add(feedbackMenuItem);
        getItems().add(aboutMenuItem);
    }
        /** * 获取实例 * @return GlobalMenu */
        public static GlobalMenu getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new GlobalMenu();
            }
            return INSTANCE;
        }

    public static void addRightMenu(ListView treeView) {
        treeView.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Node node = event.getPickResult().getIntersectedNode();
                //给node对象添加下来菜单；
                GlobalMenu.getInstance().show(node, javafx.geometry.Side.BOTTOM,event.getSceneX(),event.getSceneY());
//                String name = (String) ((TreeItem) treeView.getSelectionModel().getSelectedItem()).getValue();
//                System.out.println("Node click: " + name);
            }
        });
    }

    public static void demo(){

    }




}
