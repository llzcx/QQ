package JavaChat.Client.View;

import JavaChat.Client.Service.EmojiService;
import JavaChat.Client.Service.MessageService;
import JavaChat.Client.Service.UserInformationService;
import JavaChat.Client.View.emojione.Emoji;
import JavaChat.Client.View.emojione.EmojiOne;
import JavaChat.Common.Pojo.GroupMessage;
import JavaChat.Common.Pojo.Message;
import JavaChat.Common.Transfer.FileMessageState;
import JavaChat.Common.Utils.ClientFileAddress;
import JavaChat.Common.Utils.CreatString;
import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.JavaFxUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.print.PrinterGraphics;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Queue;

import static javafx.scene.paint.Color.TRANSPARENT;

/**
 * 该类为聊天气泡左/右,通过方法可以返回根节点
 */
public class ChatListItem {
    public static HashMap<Integer,ProgressBar> hm = new HashMap<>();
    public static HashMap<Integer,ChatListItem> ItemHashMap = new HashMap<>();
    private int Id;
    private Pane pane;
    private TextFlow textFlow;
    private Pane left;
    private Pane right;
    private Button arrow;
    private Button AgreeFile_Button;
    private Button RejectFile_Button;
    private Label FileTip_Label;
    private Label Time_Label;
    private ImageView HeadPortrait;
    private Button UserInf;
    private Label Name;
    private ImageView ImageMsg;
    private Label title;
    private ImageView FileFinished;
    private Label FileName;
    public ChatListItem(int id) {
        this.Id = id;
        pane = new Pane();
        HeadPortrait = new ImageView();
        UserInf = new Button();
        textFlow = new TextFlow();
        //设置预留大小
        pane.setPrefSize(730, 150);
        left = new Pane();
        right = new Pane();
        arrow = new Button();
        arrow.setDisable(false);
        arrow.setPrefSize(32, 32);
        left.setPrefSize(580, 70);
        right.setPrefSize(580, 70);
        //在css文件中
        pane.getStyleClass().add("pane");
        left.getStyleClass().add("pane");
        right.getStyleClass().add("pane");
        HeadPortrait.setFitHeight(50);
        HeadPortrait.setFitWidth(50);
        UserInf.setPrefSize(50,50);
        UserInf.setStyle(
                "-fx-background-color: TRANSPARENT"
        );
        textFlow.setPrefSize(480,50);
        AgreeFile_Button = new Button();
        AgreeFile_Button.setText("接收");
        AgreeFile_Button.setPrefHeight(30);
        AgreeFile_Button.setPrefWidth(50);
        AgreeFile_Button.setStyle(
                "-fx-text-fill: #00c2ff;-fx-background-color: TRANSPARENT"
        );
        AgreeFile_Button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AgreeFile_Button.setStyle(
                        "-fx-text-fill: #00c2ff;-fx-background-color: TRANSPARENT;-fx-underline: true ;"
                );
            }
        });
        AgreeFile_Button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AgreeFile_Button.setStyle(
                        "-fx-text-fill: #00c2ff;-fx-background-color: TRANSPARENT;-fx-underline: false ;"
                );
            }
        });
        RejectFile_Button = new Button();
        RejectFile_Button.setText("拒绝");
        RejectFile_Button.setPrefHeight(30);
        RejectFile_Button.setPrefWidth(50);
        FileTip_Label = new Label();
        FileTip_Label.setPrefHeight(30);
        FileTip_Label.setPrefWidth(200);
        FileTip_Label.setText("是否接收此文件?");
        Time_Label = new Label();
        Time_Label.setPrefSize(500,30);
        Time_Label.setStyle(
                "-fx-text-fill: #000000"
        );
        Name = new Label();
        Name.setPrefSize(70,10);
        Name.setStyle("-fx-text-fill: BLACK");
        ImageMsg = new ImageView();
        ImageMsg.setFitHeight(100);
        if(hm.get(Id)!=null)hm.get(Id).setPrefSize(100,20);
        title = new Label();
        title.setPrefSize(40,10);
        title.setAlignment(Pos.CENTER);
        FileFinished = new ImageView();
        FileFinished.setImage(FileUtils.File_Image(new File("C:\\Users\\陈翔\\IdeaProjects\\untitled5\\src\\JavaChat\\Client\\View\\fxml\\Image\\完毕.png")));
        FileFinished.setFitWidth(30);
        FileFinished.setFitHeight(30);
        FileName = new Label();
        FileName.setStyle(
                "-fx-text-fill: BLACK;"
        );
    }

    /**
     * 好友聊天[左]
     * @param ihead
     * @param itext
     * @param width
     * @param hight
     * @param message
     * @return
     */
    public Pane Left(File ihead, String itext, double width, double hight,Message message) {//别人的消息
        if(left.getChildren().size()>=2){
            return pane;
        }
        UserInf.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String name = event.getButton().name();
                //左击事件
                if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                    UserInformationService.PullUser(message.getSend_User_Id());
                }
            }
        });
        Image image = FileUtils.File_Image(ihead);
        HeadPortrait.setImage(image);
        HeadPortrait.setLayoutX(10);
        HeadPortrait.setLayoutY(10);
        UserInf.setLayoutX(10);
        UserInf.setLayoutY(10);
        Time_Label.setLayoutX(10+50+40);
        Time_Label.setLayoutY(5);
        Time_Label.setText(message.getSend_Time());
        arrow.getStyleClass().add("leftarrow");
        arrow.setLayoutY(40);
        arrow.setLayoutX(85);
        left.getChildren().add(Time_Label);
        left.getChildren().add(HeadPortrait);
        left.getChildren().add(UserInf);
        if(message.getMesType().equals("文本消息")){
            textFlow.setStyle(
                    "-fx-background-color: #fffabd;-fx-border-radius: 10px"
            );
            pane.setPrefWidth(width);
            pane.setPrefHeight(pane.getPrefHeight()+25);
            right.setPrefHeight(hight);
            right.setPrefWidth(width);
            textFlow.setPrefSize(width, hight);
            textFlow.setLayoutX(100);
            textFlow.setLayoutY(30);
            //添加文本,表情
            Queue<Object> obs = EmojiOne.getInstance().toEmojiAndText(itext);
            while(!obs.isEmpty()) {
                Object ob = obs.poll();
                if(ob instanceof String) {
                    addText((String)ob);
                }
                else if(ob instanceof Emoji) {
                    Emoji emoji = (Emoji) ob;
                    textFlow.getChildren().add(createEmojiNode(emoji));
                }
            }
            left.getChildren().add(arrow);
            left.getChildren().add(textFlow);
        }else if(message.getMesType().equals("文件消息")){
            if(hm.get(message.getId())==null)
            hm.put(message.getId(),new ProgressBar(0));
            FileTip_Label.setText("是否接收该文件?");
            AgreeFile_Button.setOnAction(event -> {
                message.setFileMessageState(FileMessageState.AGREE);
                MessageService.AgreeFileMessage(message);
                //ban掉该按钮
                AgreeFile_Button.setVisible(false);
                RejectFile_Button.setVisible(false);
                FileTip_Label.setVisible(false);
            });
            RejectFile_Button.setOnAction(event -> {
                message.setFileMessageState(FileMessageState.REJECT);
                MessageService.RejectFileMessage(message);
                //ban掉该按钮
                AgreeFile_Button.setVisible(false);
                RejectFile_Button.setVisible(false);
                FileTip_Label.setVisible(false);
            });
            ImageMsg.setLayoutX(100);
            ImageMsg.setLayoutY(30);
            Image imageMsg = FileUtils.File_Image(new File("src\\JavaChat\\Client\\View\\fxml\\Image\\文件框.png"));
            ImageMsg.setImage(imageMsg);
            //固定高度,按图片比例缩放
            double imagewight = 100.0*imageMsg.getWidth()/imageMsg.getHeight();
            ImageMsg.setFitWidth(imagewight);
            pane.setPrefWidth(imagewight);
            pane.setPrefHeight(pane.getPrefHeight()+25);
            left.setPrefWidth(imagewight);
            String path1 = ClientFileAddress.File + "\\" + CreatString.RemoveOther(message.getSend_User_Id() + message.getSend_Time() + message.getFileName())+"\\"+message.getFileName();
            ImageMsg.setOnMouseClicked(event -> {
                try {
                    if(!FileUtils.CheckFileIsExists(path1)){
                        Stage dialogStage = new Stage();
                        DialogPane dialog = new DialogPane();
                        dialog.setHeaderText("");
                        dialog.setContentText("文件已经被删除,移动或者修改,是否重新下载?");
                        dialog.getButtonTypes().add(ButtonType.OK);
                        dialog.getButtonTypes().add(ButtonType.NO);
                        Button close = (Button)dialog.lookupButton(ButtonType.OK);
                        close.setText("重新下载");
                        close.setOnAction(event1 -> {
                          MessageService.AgreeFileMessage(message);
                          dialogStage.close();
                        });
                        Button button = (Button)dialog.lookupButton(ButtonType.NO);
                        button.setText("不用了");
                        button.setOnAction(event1 -> {
                            dialogStage.close();
                        });
                        Scene dialogScene = new Scene(dialog,TRANSPARENT);
                        dialogStage.setScene(dialogScene);
                        dialogStage.setTitle("错误");
                        dialogStage.initStyle(StageStyle.UTILITY);
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.setResizable(false);
                        dialogStage.show();
                    }else{
                        Desktop.getDesktop().open(new File(path1));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            left.getChildren().add(ImageMsg);
            AgreeFile_Button.setPrefSize(60,20);
            RejectFile_Button.setPrefSize(30,20);
            AgreeFile_Button.setLayoutX(100+20);
            AgreeFile_Button.setLayoutY(30+64.516129+10);
            RejectFile_Button.setLayoutX(100+20+30+10);
            RejectFile_Button.setLayoutY(30+64.516129+10);
            FileName.setPrefSize(170,10);
            FileName.setLayoutX(100+64.516129);
            FileName.setLayoutY(40);
            FileName.setText(message.getFileName());
            left.getChildren().add(FileName);
            if(message.getFileMessageState()==FileMessageState.UNTREATED){
                //文件不存在就添加接受文件按钮
                if(!FileUtils.CheckFileIsExists(path1)){
                    left.getChildren().add(AgreeFile_Button);
                }else{
                    hm.get(Id).setVisible(false);
                }
            }
            FileFinished.setLayoutX(70+imagewight-5);
            FileFinished.setLayoutY(30+40);
            FileFinished.setVisible(false);
            left.getChildren().add(FileFinished);
            if(hm.get(Id)!=null){
                hm.get(Id).setPrefSize(imagewight-64.516129-5,4);
                hm.get(Id).setLayoutX(100+64.516129);
                hm.get(Id).setLayoutY(30+40);
                hm.get(Id).setStyle(
                        "  -fx-background-color: #37C796;-fx-background-insets: 3 3 4 3;-fx-background-radius: 2;-fx-padding: 0.75;"
                );
                left.getChildren().add(hm.get(Id));
            }

        }else if(message.getMesType().equals("图片消息")){
            ImageMsg.setLayoutX(100);
            ImageMsg.setLayoutY(30);
            File file = FileUtils.ByteToFile(message.getBytes(), ClientFileAddress.ImageMsg,message.getFile().getName());
            Image imageMsg = FileUtils.File_Image(file);
            ImageMsg.setImage(imageMsg);
            //固定高度,按图片比例缩放
            double imagewight = 100.0*imageMsg.getWidth()/imageMsg.getHeight();
            ImageMsg.setFitWidth(imagewight);
            pane.setPrefWidth(imagewight);
            pane.setPrefHeight(pane.getPrefHeight()+25);
            left.setPrefWidth(imagewight);
            ImageMsg.setOnMouseClicked(event -> ViewPort.OpenImage("file:"+file.getAbsolutePath()));
            left.getChildren().add(ImageMsg);
        }

        pane.getChildren().add(left);

        return pane;


    }


    /**
     * 群聊天[左]
     * @param ihead
     * @param itext
     * @param width
     * @param hight
     * @param groupMessage
     * @return
     */
    public Pane GroupChatLeft(File ihead, String itext, double width, double hight,GroupMessage groupMessage) {//别人的消息
        UserInf.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String name = event.getButton().name();
                //左击事件
                if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                    UserInformationService.PullUser(groupMessage.getSenderAccountNumber());
                }
            }
        });
        Image image = FileUtils.File_Image(ihead);
        HeadPortrait.setImage(image);
        textFlow.setStyle(
                "-fx-background-color: #fffabd;-fx-border-radius: 10px"
        );
        arrow.getStyleClass().add("leftarrow");
//        AgreeFile_Button.getStyleClass().add("AgreeFile");
//        RejectFile_Button.getStyleClass().add("RejectFile");
        pane.setPrefHeight(110 + hight);
        left.setPrefHeight(30 + hight);
        HeadPortrait.setLayoutX(10);
        HeadPortrait.setLayoutY(10);
        UserInf.setLayoutX(10);
        UserInf.setLayoutY(10);
        Time_Label.setLayoutX(10 + 50 + 40 + 75 + 100 + 30);
        Time_Label.setLayoutY(0);
        Name.setLayoutX(10 + 5 + 50 + 40);
        Name.setLayoutY(5+5);
        textFlow.setPrefSize(width, hight);
        textFlow.setLayoutX(100);
        textFlow.setLayoutY(30);
        arrow.setLayoutY(40);
        arrow.setLayoutX(85);
        title.setLayoutX(10+5+50);
        title.setLayoutY(5+5);
        if(MainCoreController.getIdentity(groupMessage.getSenderAccountNumber(),groupMessage.getGroupNumber())==null){
            title.setText("不是群成员");
            title.setStyle("-fx-text-fill: BALCK;-fx-background-color: #ffffff");
        }else{
            SetIdentity(MainCoreController.getIdentity(groupMessage.getSenderAccountNumber(),groupMessage.getGroupNumber()));
        }
        Queue<Object> obs = EmojiOne.getInstance().toEmojiAndText(itext);
        while(!obs.isEmpty()) {
            Object ob = obs.poll();
            if(ob instanceof String) {
                addText((String)ob);
            }
            else if(ob instanceof Emoji) {
                Emoji emoji = (Emoji) ob;
                textFlow.getChildren().add(createEmojiNode(emoji));
            }
        }
        addText("  ");
        Time_Label.setText(groupMessage.getSendTime());
        left.getChildren().add(Time_Label);
        if(MainCoreController.GroupUser.get(groupMessage.getSenderAccountNumber())!=null){
            Name.setText(MainCoreController.GroupUser.get(groupMessage.getSenderAccountNumber()).getName());
        }
        left.getChildren().add(Name);
        left.getChildren().add(HeadPortrait);
        left.getChildren().add(UserInf);
        left.getChildren().add(textFlow);
        left.getChildren().add(arrow);
        left.getChildren().add(title);
        pane.getChildren().add(left);
        return pane;


    }

    /**
     * 好友聊天[右]
     * @param ihead
     * @param itext
     * @param width
     * @param hight
     * @param message
     * @return
     */
    public Pane Right(File ihead, String itext, double width, double hight,Message message) {//自己的消息
        if(right.getChildren().size()>=2){
            return pane;
        }
        UserInf.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String name = event.getButton().name();
                //左击事件
                if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                    UserInformationService.PullUser(message.getSend_User_Id());
                }
            }
        });
        Image image = FileUtils.File_Image(ihead);
        HeadPortrait.setImage(image);
        HeadPortrait.setLayoutY(10);
        HeadPortrait.setLayoutX(510);
        UserInf.setLayoutY(10);
        UserInf.setLayoutX(510);
        arrow.getStyleClass().add("rightarrow");
        arrow.setLayoutY(40);
        arrow.setLayoutX(475);
        Time_Label.setLayoutX(510-50-100);
        Time_Label.setLayoutY(5);
        Time_Label.setText(message.getSend_Time());
        right.getChildren().add(Time_Label);
        right.getChildren().add(HeadPortrait);
        right.getChildren().add(UserInf);
        right.setLayoutX(150);

        if(message.getMesType().equals("文本消息")){
            textFlow.setStyle(
                    "-fx-background-color: #87c8e8;-fx-border-radius: 10px"
            );
            pane.setPrefHeight(110 + hight);
            pane.setPrefWidth(width);
            pane.setPrefHeight(pane.getPrefHeight()+25);
            right.setPrefHeight(hight);
            right.setPrefWidth(width);
            textFlow.setPrefSize(width, hight);
            textFlow.setLayoutY(30);
            textFlow.setLayoutX(480 - width);
            Queue<Object> obs = EmojiOne.getInstance().toEmojiAndText(itext);
            while(!obs.isEmpty()) {
                Object ob = obs.poll();
                if(ob instanceof String) {
                    addText((String)ob);
                }
                else if(ob instanceof Emoji) {
                    Emoji emoji = (Emoji) ob;
                    textFlow.getChildren().add(createEmojiNode(emoji));
                }
            }
            right.getChildren().add(textFlow);
            right.getChildren().add(arrow);
            right.setLayoutX(150);
        }else if(message.getMesType().equals("文件消息")){
            File file = new File("src\\JavaChat\\Client\\View\\fxml\\Image\\文件框.png");
            Image imageMsg = FileUtils.File_Image(file);
            ImageMsg.setImage(imageMsg);
            double imagewight = 100*imageMsg.getWidth()/imageMsg.getHeight();
            ImageMsg.setFitWidth(imagewight);
            ImageMsg.setLayoutX(480-imagewight);
            ImageMsg.setLayoutY(30);
            //ImageMsg.setOnMouseClicked(event -> { });
            pane.setPrefWidth(imagewight);
            pane.setPrefHeight(pane.getPrefHeight()+25);
            right.setPrefWidth(imagewight);
            right.getChildren().add(ImageMsg);
            FileName.setPrefSize(170,10);
            FileName.setLayoutX(480-imagewight+64.516129);
            FileName.setLayoutY(40);
            FileName.setText(message.getFileName());
            right.getChildren().add(FileName);
            FileFinished.setLayoutX(480-imagewight+64.516129+5);
            FileFinished.setLayoutY(30+40);
            FileFinished.setVisible(false);
            left.getChildren().add(FileFinished);
            if(hm.get(Id)!=null){
                hm.get(Id).setPrefSize(imagewight-64.516129,4);
                hm.get(Id).setLayoutX(480-imagewight+64.516129);
                hm.get(Id).setLayoutY(30+40);
                hm.get(Id).setStyle(
                        "-fx-background-color: rgba(57,38,38,0.27);-fx-text-fill: #ffffff;scrollbar-face-color:rgb(238,149,0);-fx-fill: #0009f1"
                );
                right.getChildren().add(hm.get(Id));
            }
        }else if(message.getMesType().equals("图片消息")) {
            File file = FileUtils.ByteToFile(message.getBytes(), ClientFileAddress.ImageMsg,message.getFile().getName());
            Image imageMsg = FileUtils.File_Image(file);
            ImageMsg.setImage(imageMsg);
            double imagewight = 100*imageMsg.getWidth()/imageMsg.getHeight();
            ImageMsg.setFitWidth(imagewight);
            ImageMsg.setLayoutX(480-imagewight);
            ImageMsg.setLayoutY(30);
            pane.setPrefWidth(imagewight);
            pane.setPrefHeight(pane.getPrefHeight()+25);
            right.setPrefWidth(imagewight);
            ImageMsg.setOnMouseClicked(event -> ViewPort.OpenImage("file:"+file.getAbsolutePath()));
            right.getChildren().add(ImageMsg);
        }

        pane.getChildren().add(right);

        return pane;

    }

    /**
     * 群聊天[右]
     * @param ihead
     * @param itext
     * @param width
     * @param hight
     * @param groupMessage
     * @return
     */
    public Pane GroupChatRight(File ihead, String itext, double width, double hight, GroupMessage groupMessage) {//自己的消息
        UserInf.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String name = event.getButton().name();
                //左击事件
                if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                    UserInformationService.PullUser(groupMessage.getSenderAccountNumber());
                }
            }
        });
        Image image = FileUtils.File_Image(ihead);
        HeadPortrait.setImage(image);
        textFlow.setStyle(
                "-fx-background-color: #87c8e8;-fx-border-radius: 10px"
        );
        arrow.getStyleClass().add("rightarrow");
        HeadPortrait.setLayoutY(10);
        HeadPortrait.setLayoutX(510);
        UserInf.setLayoutY(10);
        UserInf.setLayoutX(510);
        Time_Label.setLayoutX(510-50-100-200);
        Time_Label.setLayoutY(0);
        pane.setPrefHeight(110 + hight);
        right.setPrefHeight(30 + hight);
        textFlow.setPrefSize(width, hight);
        textFlow.setLayoutY(30);
        textFlow.setLayoutX(480 - width);
        arrow.setLayoutY(40);
        arrow.setLayoutX(475);
        Name.setText(MainCoreController.GroupUser.get(groupMessage.getSenderAccountNumber()).getName());
        Name.setLayoutX(510-5-40-70);
        Name.setLayoutY(5+5);
        title.setLayoutX(510-5-40);
        title.setLayoutY(5+5);
        if(MainCoreController.getIdentity(groupMessage.getSenderAccountNumber(),groupMessage.getGroupNumber())==null){
            title.setText("不是群成员");
            title.setStyle("-fx-text-fill: BLACK;-fx-background-color: #ffffff");
        }else{
            SetIdentity(MainCoreController.getIdentity(groupMessage.getSenderAccountNumber(),groupMessage.getGroupNumber()));
        }
        Queue<Object> obs = EmojiOne.getInstance().toEmojiAndText(itext);
        while(!obs.isEmpty()) {
            Object ob = obs.poll();
            if(ob instanceof String) {
                addText((String)ob);
            }
            else if(ob instanceof Emoji) {
                Emoji emoji = (Emoji) ob;
                textFlow.getChildren().add(createEmojiNode(emoji));
            }
        }
        Time_Label.setText(groupMessage.getSendTime());
        right.getChildren().add(Time_Label);
        right.getChildren().add(Name);
        right.getChildren().add(HeadPortrait);
        right.getChildren().add(UserInf);
        right.getChildren().add(textFlow);
        right.getChildren().add(arrow);
        right.getChildren().add(title);
        right.setLayoutX(150);
        pane.getChildren().add(right);

        return pane;

    }

    private Node createEmojiNode(Emoji emoji) {
        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(3));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(32);
        imageView.setFitHeight(32);
        imageView.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(emoji.getHex())));
        stackPane.getChildren().add(imageView);

        Tooltip tooltip = new Tooltip(emoji.getShortname());
        Tooltip.install(stackPane, tooltip);
        stackPane.setCursor(Cursor.HAND);
        stackPane.setOnMouseEntered(e-> {
            stackPane.setStyle("-fx-background-color: #a6a6a6; -fx-background-radius: 3;");
        });
        stackPane.setOnMouseExited(e-> {
            stackPane.setStyle("");
        });
        return stackPane;
    }

    private Node createImageNode(Image image) {
        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(3));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());
        imageView.setImage(image);
        stackPane.getChildren().add(imageView);
        stackPane.setCursor(Cursor.HAND);
        stackPane.setOnMouseEntered(e-> {
            stackPane.setStyle("-fx-background-color: #a6a6a6; -fx-background-radius: 3;");
        });
        stackPane.setOnMouseExited(e-> {
            stackPane.setStyle("");
        });
        return stackPane;
    }

    private String getEmojiImagePath(String hexStr) {
        return getClass().getResource("png_40/" + hexStr + ".png").toExternalForm();
    }

    private void addText(String text) {
        Text textNode = new Text(text);
        textNode.setFont(Font.font(16));
        textFlow.getChildren().add(textNode);
    }

    public Pane getPane(){
        return pane;
    }
    public ProgressBar getProcess(int id){
        return hm.get(id);
    }
    public Object $(String id) {
        if(pane.lookup("#" + id)!=null){
            return pane.lookup("#" + id);
        }else if(left.lookup("#" + id)!=null){
            return left.lookup("#" + id);
        }else if(right.lookup("#" + id)!=null){
            return right.lookup("#" + id);
        }
        return null;
    }
    public ImageView getFileFinished(){
        return FileFinished;
    }

    public void SetADMINISTRATORColor(){
        title.setText("管理员");
        title.setStyle(
                "-fx-text-fill: WHITE;-fx-background-color: #72d715"
        );
    }

    public void SetLEADERColor(){
        title.setText("群主");
        title.setStyle(
                "-fx-text-fill: WHITE;-fx-background-color: #ef9a0e"
        );
    }
    public void SetODINARYColor(){
        title.setText("群员");
        title.setStyle(
                "-fx-text-fill: WHITE;-fx-background-color: #bab3aa"
        );
    }

    public void SetIdentity(String ide){
        if(ide.equals("群主")){
            SetLEADERColor();
        }else if(ide.equals("管理员")){
            SetADMINISTRATORColor();
        }else if(ide.equals("普通群员")){
            SetODINARYColor();
        }else if(ide.equals("")){
            title.setText("非群员");
        }
    }

}
