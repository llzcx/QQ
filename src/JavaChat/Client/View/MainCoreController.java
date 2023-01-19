package JavaChat.Client.View;

import JavaChat.Client.Service.*;
import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.GroupMessage;
import JavaChat.Common.Pojo.Message;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import JavaChat.Client.View.emojione.Emoji;
import JavaChat.Client.View.emojione.EmojiOne;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import static javafx.scene.paint.Color.TRANSPARENT;


public class MainCoreController {
    //登录本页面的账号
    private static String AccountNumber;
    //个人信息
    public static User PersonInformation;
    /*--------------图片消息分割线------------*/
    @FXML
    private ImageView Image_File_ImageView;
    @FXML
    private Label MsgTip_Label;
    private File selectImage;
    @FXML
    private ImageView ImageClient_ImageView;
    @FXML
    private ImageView FileClient_ImageView;
    /*--------------图片消息分割线------------*/
    /*--------------消息分割线------------*/
    @FXML
    private TextArea Content_TextArea;
    @FXML
    private TextFlow flowOutput;
    @FXML
    private Button MessagePreview_Button;
    private boolean flowOutputFlag;
    /*---------------消息分割线------------*/
    /*--------------表情分割线--------------*/
    private static final boolean SHOW_MISC = false;
    @FXML
    private VBox VBoxEmojione;
    @FXML
    private ScrollPane searchScrollPane;
    @FXML
    private FlowPane searchFlowPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<Image> boxTone;
    private static boolean EmojioneFlag;
    public static Vector<String> Emoji_Vector;
    /*--------------表情分割线--------------*/
    @FXML
    private Button ReturnLogin_Button;
    @FXML
    private Button Return_onlineUser_Button;
    @FXML
    private Button Send_Button;
    @FXML
    private Label ContentErrorTip_Label;
    @FXML
    public ListView ChatList_ListView;
    @FXML
    public ListView<User> FriendList_ListView;
    @FXML
    private Label ChatTargetId_Label;
    @FXML
    private Label GroupChatTargetId_Label;
    @FXML
    private Button More_Button;
    @FXML
    private Button AddFriend_Button;
    @FXML
    private Button FriendApplication_Button;
    @FXML
    private Button AlterPerson_Button;
    @FXML
    private Button File_Button;
    @FXML
    private Button Expression_Button;
    @FXML
    private Button Image_Button;
    @FXML
    private Button AlterPassword_Button;
    @FXML
    private ComboBox SelectFriendGroup_Combobox;
    @FXML
    private Label FileErrorTip_Label;
    @FXML
    private Rectangle ChatBarrier_Rectangle;
    private File selectedFile;
    //群聊
    @FXML
    private Button CreatGroup_Button;
    @FXML
    public ListView<Group> GroupList_Listview;
    @FXML
    public ListView GroupChatList_ListView;
    @FXML
    private ImageView Head_ImageView;
    @FXML
    private Label Account_Label;
    @FXML
    private Circle FriendRed_Circle;
    @FXML
    private Circle GroupRed_Circle;
    @FXML
    ComboBox<String> CommonWords_ComboBox;
    @FXML
    private Circle GroupNotice_Circle;
    @FXML
    private Button GroupNotice_Button;

    //判断是私聊还是群聊
    private static String CheckPrivateOrGroupChat = "";
    public void Send_But_ColorEnter(){
        Send_Button.setStyle(
                "-fx-background-color: #5bc4ff;"
        );
    }

    public void Send_But_ColorExited(){
        Send_Button.setStyle(
                "-fx-background-color: #2eb6f5;"
        );
    }
    public void More_But_ColorEnter(){
        More_Button.setStyle(
                "-fx-background-color: #000000;"
        );
    }
    public void More_But_ColorExited(){
        More_Button.setStyle(
                "-fx-background-color: #000000;"
        );
    }
    public void MouseMovePressContent_TextArea(){

    }
    /*------------------好友聊天记录------------*/
    //账号->数据源数组
    public static Hashtable<String,ObservableList<Message>> FriendChatRecord= new Hashtable<>();
    public static ObservableList<Message> GetMessageObservableList(String Account){
        return FriendChatRecord.get(Account);
    }
    /*-----------------群聊聊天记录--------------*/
    //群号->数据源数组
    public static Hashtable<String,ObservableList<GroupMessage>> GroupChatRecord= new Hashtable<>();
    public static ObservableList<GroupMessage> GetGroupMessageObservableList(String Account){
        return GroupChatRecord.get(Account);
    }
    /*-----------------群------------------*/
    public static String GroupChatTarget = "";
    public static Vector<Group> Group_Vector = new Vector<>();
    public static ConcurrentHashMap<String,File> GroupHeadPortrait = new ConcurrentHashMap<>();
    public static ObservableList<Group> Group_ObservableList;
    public static HashMap<String,String> IdentityMap = new HashMap<>();
    public static void PutNewIdentity(String AccountNUmber,String GroupNumber,String Identity){
        IdentityMap.put(AccountNUmber+"$"+GroupNumber,Identity);
    }
    public static String getIdentity(String AccountNUmber, String GroupNumber){
        return IdentityMap.get(AccountNUmber+"$"+GroupNumber);
    }
    public static HashMap<String,User> GroupUser = new HashMap<>();
    /*----------------好友------------*/
    public static String ChatTarget;
    public static Vector<User> Friend_Vector = new Vector<>();
    public static ConcurrentHashMap<String,File> FriendHeadPortrait = new ConcurrentHashMap<>();
    public static ObservableList<User> ChatFriend_ObservableList;
    //初始化事件
    public void initialize(){
        //头像
        /*--------------消息分割线------------*/
        selectImage = null;
        selectedFile = null;
        Image_File_ImageView.setVisible(false);
        flowOutputFlag=false;
        flowOutput.setVisible(false);
        flowOutput.setPadding(new Insets(10));
        Content_TextArea.setFont(Font.font(16));
        Content_TextArea.textProperty().addListener(e-> {
            flowOutput.getChildren().clear();
            String text = Content_TextArea.getText();
            Queue<Object> obs = EmojiOne.getInstance().toEmojiAndText(text);
            while(!obs.isEmpty()) {
                Object ob = obs.poll();
                if(ob instanceof String) {
                    addText((String)ob);
                }
                else if(ob instanceof Emoji) {
                    Emoji emoji = (Emoji) ob;
                    flowOutput.getChildren().add(createEmojiNode_Msg(emoji));
                }
            }
        });

        /*--------------消息分割线------------*/
        /*-------------------表情分割线------------*/
        MainCoreController.Emoji_Vector = new Vector<>();
        EmojiService.PullEmoji();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        EmojioneFlag = false;
        VBoxEmojione.setVisible(false);
        if(!SHOW_MISC) {
            tabPane.getTabs().remove(tabPane.getTabs().size()-2, tabPane.getTabs().size());
        }
        ObservableList<Image> tonesList = FXCollections.observableArrayList();

        for(int i = 1; i <= 5; i++) {
            Emoji emoji = EmojiOne.getInstance().getEmoji(":thumbsup_tone"+i+":");
            Image image = ImageCache.getInstance().getImage(getEmojiImagePath(emoji.getHex()));
            tonesList.add(image);
        }
        Emoji em = EmojiOne.getInstance().getEmoji(":thumbsup:"); //default tone
        Image image = ImageCache.getInstance().getImage(getEmojiImagePath(em.getHex()));
        tonesList.add(image);
        boxTone.setItems(tonesList);
        boxTone.setCellFactory(e->new ToneCell());
        boxTone.setButtonCell(new ToneCell());
        boxTone.getSelectionModel().selectedItemProperty().addListener(e->refreshTabs());


        searchScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        searchFlowPane.prefWidthProperty().bind(searchScrollPane.widthProperty().subtract(5));
        searchFlowPane.setHgap(5);
        searchFlowPane.setVgap(5);

        txtSearch.textProperty().addListener(x-> {
            String text = txtSearch.getText();
            if(text.isEmpty() || text.length() < 2) {
                searchFlowPane.getChildren().clear();
                searchScrollPane.setVisible(false);
            } else {
                searchScrollPane.setVisible(true);
                List<Emoji> results = EmojiOne.getInstance().search(text);
                searchFlowPane.getChildren().clear();
                results.forEach(emoji ->searchFlowPane.getChildren().add(createEmojiNode(emoji)));
            }
        });


        for(Tab tab : tabPane.getTabs()) {
            ScrollPane scrollPane = (ScrollPane) tab.getContent();
            FlowPane pane = (FlowPane) scrollPane.getContent();
            pane.setPadding(new Insets(5));
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            pane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(5));
            pane.setHgap(5);
            pane.setVgap(5);

            tab.setId(tab.getText());
            ImageView icon = new ImageView();
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            switch (tab.getText().toLowerCase()) {
                case "frequently used":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":heart:").getHex())));
                    break;
                case "people":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":smiley:").getHex())));
                    break;
                case "nature":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":dog:").getHex())));
                    break;
                case "food":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":apple:").getHex())));
                    break;
                case "activity":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":soccer:").getHex())));
                    break;
                case "travel":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":airplane:").getHex())));
                    break;
                case "objects":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":bulb:").getHex())));
                    break;
                case "symbols":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":atom:").getHex())));
                    break;
                case "flags":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":flag_eg:").getHex())));
                    break;
            }

            if(icon.getImage() != null) {
                tab.setText("");
                tab.setGraphic(icon);
            }

            tab.setTooltip(new Tooltip(tab.getId()));
            tab.selectedProperty().addListener(ee-> {
                if(tab.getGraphic() == null) return;
                if(tab.isSelected()) {
                    tab.setText(tab.getId());
                } else {
                    tab.setText("");
                }
            });
        }



        boxTone.getSelectionModel().select(0);
        tabPane.getSelectionModel().select(1);
        /*-------------------表情分割线------------*/
        //本人头像
        FriendHeadPortrait.put(AccountNumber,FileUtils.ByteToFile(PersonInformation.getHeadPortrait_byte(),ClientFileAddress.HeadPortrait,PersonInformation.getHeadPortraitFile().getName()));
        Head_ImageView.setImage(FileUtils.File_Image(FriendHeadPortrait.get(AccountNumber)));
        Rectangle rectangle = new Rectangle(Head_ImageView.prefWidth(-1),Head_ImageView.prefHeight(-1));
        rectangle.setArcWidth(100);
        rectangle.setArcHeight(100);
        //AlterPerson_Button.setClip(rectangle);
        Head_ImageView.setClip(rectangle);
        Head_ImageView.setStyle(
                "-fx-border-color: #ffffff"
        );
        //初始化聊天禁框
        ChatBarrier_Rectangle.setVisible(true);
        //群资料按钮禁用
        More_Button.setDisable(true);
        //聊天目标赋值
        ChatTarget = "";
        GroupChatTarget = "";
        //常用语
        CommonWords_ComboBox.getItems().add("最近过的怎么样?");
        CommonWords_ComboBox.getItems().add("明天一起出来看电影吗?");
        CommonWords_ComboBox.getItems().add("你最近在干嘛");
        CommonWords_ComboBox.getItems().add("我该睡觉了,晚安");
        CommonWords_ComboBox.getItems().add("下午一起吃个饭?");
        CommonWords_ComboBox.setOnAction((event) -> {
            int selectedIndex = CommonWords_ComboBox.getSelectionModel().getSelectedIndex();
            Object selectedItem = CommonWords_ComboBox.getSelectionModel().getSelectedItem();
            Content_TextArea.setText(CommonWords_ComboBox.getValue());
        });
        //文件按钮初始化为不可用
        File_Button.setDisable(true);
        //5.21 debug发现必须在初始化的时候调用
        Group_ObservableList = FXCollections.observableArrayList();
        ChatFriend_ObservableList = FXCollections.observableArrayList();
        /*------------------好友群聊选择器-------------------------*/
        CheckPrivateOrGroupChat = "群聊";
        ObservableList  SelectFriendGroup_observableList =FXCollections.observableArrayList("好友列表","群聊列表");
        SelectFriendGroup_Combobox.setItems(SelectFriendGroup_observableList);
        EventHandler event = event1 -> {
            ChatBarrier_Rectangle.setVisible(false);
            if(SelectFriendGroup_Combobox.getValue().equals("好友列表")){
                GroupChatTargetId_Label.setVisible(false);
                ChatTargetId_Label.setVisible(true);
                //这2个功能在群聊没有
                FileClient_ImageView.setVisible(true);
                File_Button.setVisible(true);
                ImageClient_ImageView.setVisible(true);
                Image_Button.setVisible(true);
                //这个button用来打开群资料管理
                More_Button.setDisable(true);
                File_Button.setDisable(false);
                CheckPrivateOrGroupChat = "私聊";
                //RefreshFriendList();
                FriendList_ListView.setVisible(true);
                GroupChatList_ListView.setVisible(false);
                ChatList_ListView.setVisible(true);
                GroupList_Listview.setVisible(false);
            }else if(SelectFriendGroup_Combobox.getValue().equals("群聊列表")){
                GroupChatTargetId_Label.setVisible(true);
                ChatTargetId_Label.setVisible(false);
                FileClient_ImageView.setVisible(false);
                File_Button.setVisible(false);
                ImageClient_ImageView.setVisible(false);
                Image_Button.setVisible(false);
                More_Button.setDisable(false);
                File_Button.setDisable(true);
                CheckPrivateOrGroupChat = "群聊";
                //RefreshGroupList();
                FriendList_ListView.setVisible(false);
                GroupChatList_ListView.setVisible(true);
                ChatList_ListView.setVisible(false);
                GroupList_Listview.setVisible(true);
            }
        };
        SelectFriendGroup_Combobox.setOnAction(event);
        /*----------------------群加载区------------------*/
        GroupList_Listview.setItems(Group_ObservableList);
        GroupList_Listview.setCellFactory(new Callback<ListView<Group>, ListCell<Group>>() {
            @Override
            public ListCell<Group> call(ListView<Group> param) {
                ListCell<Group> listCell = new ListCell<Group>(){
                    @Override
                    protected void updateItem(Group item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            //setStyle("-fx-background-color:rgba(0,0,0,0)");
                            HBox hBox=new HBox();
                            FXMLLoader fxmlLoader=new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("Fxml/TEST.fxml"));
                            try {
                                hBox.getChildren().add(fxmlLoader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("列表加载错误");
                            }
                            TextController textController = fxmlLoader.getController();
                            textController.setId(item.getGroupName());
                            textController.setAccount(item.getGroupNumber());
                            textController.setHeadPortrait_ImageView(GroupHeadPortrait.get(item.getGroupNumber()));
                            textController.setOnline_ImageView(null);
                            textController.setOffline_ImageView(null);
                            this.setGraphic(hBox);
                        }
                        else
                        {
                            this.setGraphic(null);
                        }
                    }
                };
                return listCell;
            }

        });
        GroupList_Listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Group>() {
            @Override
            public void changed(ObservableValue<? extends Group> observable, Group oldValue, Group newValue) {

                if(newValue!=null){
                    ChatBarrier_Rectangle.setVisible(false);
                    //设置聊天对象
                    GroupChatTarget = newValue.getGroupNumber();
                    //设置label显示的聊天对象
                    GroupChatTargetId_Label.setText(newValue.getGroupName()+"("+newValue.getGroupNumber()+")");
                    //在运行框显示点击书否成功
                    System.out.println(GroupChatTarget);
                    //群管理按钮禁掉
                    More_Button.setDisable(false);
                }else{
                    ChatBarrier_Rectangle.setVisible(true);
                }
               //RefreshGroupMessageList();
                if(GroupChatTarget!=null && GetGroupMessageObservableList(GroupChatTarget)!=null)
                GroupChatList_ListView.setItems(GetGroupMessageObservableList(GroupChatTarget));
            }
        });
        /*----------------------好友加载区-------------------*/
        FriendList_ListView.setItems(ChatFriend_ObservableList);
        FriendList_ListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                ListCell<User> listCell = new ListCell<User>(){
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            //setStyle("-fx-background-color:rgba(0,0,0,0)");
                            HBox hBox=new HBox();
                            FXMLLoader fxmlLoader=new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("Fxml/TEST.fxml"));
                            try {
                                hBox.getChildren().add(fxmlLoader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("列表加载错误");
                            }
                            TextController textController = fxmlLoader.getController();
                            textController.setId("名字:"+item.getName());
                            textController.setHeadPortrait_ImageView(FriendHeadPortrait.get(item.getAccountNumber()));
                            textController.setAccount("账号:"+item.getAccountNumber());
                            System.out.println(item.getAccountNumber()+item.getOnline_Offline_State());
                            if(item.getOnline_Offline_State().equals("在线")){
                                textController.setOffline_ImageView(null);
                            }else{
                                textController.setOnline_ImageView(null);
                            }
                            this.setGraphic(hBox);
                        }
                        else
                        {
                            this.setGraphic(null);
                        }
                    }
                };
                ContextMenu contextMenu = new ContextMenu();
                MenuItem deleteItem = new MenuItem("删除好友");
                MenuItem ViewPersonalInformation = new MenuItem("查看好友信息");
                deleteItem.setOnAction(event -> {
                    Stage dialogStage = new Stage();
                    DialogPane dialog = new DialogPane();
                    dialog.setHeaderText("");
                    dialog.setContentText("是否要删除该好友"+listCell.getItem().getName()+"("+listCell.getItem().getAccountNumber()+")?");
                    dialog.getButtonTypes().add(ButtonType.NO);
                    dialog.getButtonTypes().add(ButtonType.OK);
                    Button exit = (Button)dialog.lookupButton(ButtonType.OK);
                    exit.setText("确认删除");
                    exit.setOnAction(event1 -> {
                        FriendApplicationService.DeleteFriend(AccountNumber,ChatTarget);
                        FriendList_ListView.getItems().remove(listCell.getItem());
                        dialogStage.close();
                    });
                    Button close = (Button)dialog.lookupButton(ButtonType.NO);
                    close.setText("取消");
                    close.setOnAction(event1 -> {
                        dialogStage.close();
                    });
                    Scene dialogScene = new Scene(dialog,TRANSPARENT);
                    dialogStage.setScene(dialogScene);

                    dialogStage.setTitle("删除好友");
                    dialogStage.initStyle(StageStyle.UTILITY);
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.show();
                });
                ViewPersonalInformation.setOnAction(event1 ->{
                    UserInformationService.PullUser(ChatTarget);
                });
                contextMenu.getItems().addAll(deleteItem);
                contextMenu.getItems().addAll(ViewPersonalInformation);
                listCell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        listCell.setContextMenu(null);
                    } else {
                        listCell.setContextMenu(contextMenu);
                    }
                });
                return listCell;
            }
        });
        //设置单击事件
        FriendList_ListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {

                if(newValue!=null){
                    ChatBarrier_Rectangle.setVisible(false);
                    //设置聊天对象
                    ChatTarget = newValue.getAccountNumber();
                    //设置label显示的聊天对象
                    ChatTargetId_Label.setText(newValue.getName()+"("+newValue.getAccountNumber()+")");
                    //在运行框显示点击书否成功
                    System.out.println(ChatTarget);
                    //群管理按钮禁掉
                    More_Button.setDisable(true);
                }else{
                    ChatBarrier_Rectangle.setVisible(true);
                }
                //RefreshFriendMessageList();
                ChatList_ListView.setItems(GetMessageObservableList(ChatTarget));
            }
        });
        /*----------------------好友聊天Listview加载区域----------------------*/
        ChatList_ListView.setCellFactory(new Callback<ListView<Pane>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Pane> param) {
                return new ListCell<Message>(){
                    @Override
                    protected void updateItem( Message item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            setStyle("-fx-background-color:rgba(0,0,0,0)");
                            //ChatListItem chatListItem = new ChatListItem(item.getId());
                            ChatListItem chatListItem = ChatListItem.ItemHashMap.get(item.getId());
                            if(AccountNumber.equals(item.getSend_User_Id()))
                            {
                                this.setGraphic(chatListItem.Right(FriendHeadPortrait.get(AccountNumber),item.getContent(), ChatBubbleTools.getWidth(item.getContent()), ChatBubbleTools.getHight(item.getContent()),item));
                            }else{
                                this.setGraphic(chatListItem.Left(FriendHeadPortrait.get(ChatTarget),item.getContent(), ChatBubbleTools.getWidth(item.getContent()), ChatBubbleTools.getHight(item.getContent()),item));
                            }
                        }
                        else
                        {
                            this.setGraphic(null);
                        }
                    }
                };
            }
        });
        /*----------------群聊listview加载区---*/
        GroupChatList_ListView.setCellFactory(new Callback<ListView<Pane>, ListCell<GroupMessage>>() {
            @Override
            public ListCell<GroupMessage> call(ListView<Pane> param) {
                return new ListCell<GroupMessage>(){
                    @Override
                    protected void updateItem( GroupMessage item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            setStyle("-fx-background-color:rgba(0,0,0,0)");
                            ChatListItem chatListItem = new ChatListItem(item.getId());
                            if(AccountNumber.equals(item.getSenderAccountNumber()))
                            {
                                this.setGraphic(chatListItem.GroupChatRight(FriendHeadPortrait.get(item.getSenderAccountNumber()),item.getContent(), ChatBubbleTools.getWidth(item.getContent()), ChatBubbleTools.getHight(item.getContent()),item));
                            }else{
                                this.setGraphic(chatListItem.GroupChatLeft(FriendHeadPortrait.get(item.getSenderAccountNumber()),item.getContent(), ChatBubbleTools.getWidth(item.getContent()), ChatBubbleTools.getHight(item.getContent()),item));
                            }
                        }
                        else
                        {
                            this.setGraphic(null);
                        }
                    }
                };
            }
        });
        FriendListService.PullFriendList(AccountNumber);
        GroupListService.PullGroupList(AccountNumber);
        Account_Label.setText(AccountNumber);
    }

    /*-----------------点击事件,调用业务区-----------------*/
    //对外暴露
    public static void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }
    public static String getAccountNumber() {return AccountNumber;}
    //退出登录
    public void ExitLogin() throws Exception{
        ChatTarget = null;
        User user = new User();
        user.setAccountNumber(AccountNumber);
        LoginService.LoginOut(user);
    }

    //返回登录界面
    public void ReturnLogin(){
        Stage stage =(Stage) ReturnLogin_Button.getScene().getWindow();
        stage.close();
        MainCoreView.root = null;
        try {
            ExitLogin();
            LoginView.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onExitButtonClick() {
        Stage dialogStage = new Stage();
        DialogPane dialog = new DialogPane();
        dialog.setHeaderText("");
        dialog.setContentText("是否要退出Client?");
        dialog.getButtonTypes().add(ButtonType.NO);
        dialog.getButtonTypes().add(ButtonType.OK);
        Button exit = (Button)dialog.lookupButton(ButtonType.OK);
        exit.setText("退出登录");
        exit.setOnAction(event -> {
            ReturnLogin();
            dialogStage.close();
        });
        Button close = (Button)dialog.lookupButton(ButtonType.NO);
        close.setText("取消");
        close.setOnAction(event -> {
            dialogStage.close();
        });
        Scene dialogScene = new Scene(dialog,TRANSPARENT);
        dialogStage.setScene(dialogScene);
        dialogStage.setTitle("退出登录");
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setResizable(false);
        dialogStage.show();
    }

    //发送消息
    public void SentContent(){
        if("".equals(Content_TextArea.getText())){
            return;
        }
        if (CheckPrivateOrGroupChat.equals("私聊") && !"".equals(ChatTarget)) {
            //在客户端发送给服务器,要求转发
            MessageService.SendTextMessageToSomeone(AccountNumber, ChatTarget, Content_TextArea.getText(), GetTimeUtils.GetNowTime());
            Content_TextArea.setText("");
        }else if(CheckPrivateOrGroupChat.equals("群聊") && !"".equals(GroupChatTarget)){
            GroupMessageService.SendGroupMessageToGroup(AccountNumber,Content_TextArea.getText(),GroupChatTarget);
            Content_TextArea.setText("");
        }
    }

    /**
     * 添加好友事件
     */
    public void AddFriend(){
        if(SearchFriendView.loginStage!=null) return;
        try {
            SearchFriendView.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开好友申请表事件
     */
    public void OpenFriendApplicationList(){
        JavaFxUtils.RedCircle_Visible(MainCoreView.$("FriendRed_Circle"),false);
        if(FriendApplicationView.loginStage!=null) return;
        try {
            FriendApplicationView.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改个人信息事件
     */
    public void AlterPersonInformation(){
        if(AlterPersonView.loginStage!=null) return;
        try {
            AlterPersonView.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改密码事件
     */
    public void AlterPassword(){
        if(AlterPasswordView.loginStage!=null) return;
        try {
            AlterPasswordView.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建群聊事件
     */
    public void CreatGroup(){
        if(CreatGroupView.loginStage!=null) return;
        try {
            CreatGroupView.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索群聊
     */
    public void SearchGroup(){
        if(SearchGroupView.loginStage!=null) return;
        try {
            SearchGroupView.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择要发送的文件
     */
    public void SelectSendFile() {
        FileChooser fileChooser = new FileChooser();
        //selectedFile返回一个File对象
        selectedFile = fileChooser.showOpenDialog(new Stage());
        if(selectedFile==null){
            Content_TextArea.setEditable(true);
            return;
        }
        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.FileChangeToByte(selectedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MessageService.SendFileMessageToSomeone(AccountNumber,ChatTarget,bytes, GetTimeUtils.GetNowTime(),selectedFile);
        Content_TextArea.setText("");
    }


    /**
     * 选择要发送的图片
     */
    public void SelectSendImage() {
        FileChooser fileChooser = new FileChooser();
        selectImage = fileChooser.showOpenDialog(new Stage());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        fileChooser.setTitle("选择发送的图片");
        if(selectImage==null){
            return;
        }
        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.FileChangeToByte(selectImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MessageService.SendImageMessageToSomeone(AccountNumber,ChatTarget, GetTimeUtils.GetNowTime(),bytes,selectImage);
        selectImage = null;
    }
    /**
     * 表情
     */
    public void Emojione(){
        if(!EmojioneFlag){
            VBoxEmojione.setVisible(true);
            EmojioneFlag = true;
        }else{
            VBoxEmojione.setVisible(false);
            EmojioneFlag = false;
        }
    }


//    public void EmojioneExit(){
//        if(!EmojioneFlag){
//            VBoxEmojione.setVisible(true);
//            EmojioneFlag = true;
//        }else{
//            VBoxEmojione.setVisible(false);
//            EmojioneFlag = false;
//        }
//    }

    /**
     * 打开群申请
     */
    public void OpenGroupApplication(){
        JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupRed_Circle"),false);
        if(GroupApplicationView.loginStage!=null) return;
        try {
            GroupApplicationView.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打开消息预览
     */
    public void MessagePreview(){
        if(!flowOutputFlag){
            flowOutput.setVisible(true);
            flowOutputFlag = true;
            MessagePreview_Button.setText("关闭预览");
        }else{
            MessagePreview_Button.setText("预览消息");
            flowOutput.setVisible(false);
            flowOutputFlag = false;
        }
    }

    /**
     * 打开群通知
     */
    public void OpenGroupNotice(){
        JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupNotice_Circle"),false);
        if(GroupNoticeView.loginStage!=null) return;
        try {
            GroupNoticeView.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //刷新群列表
    public void RefreshGroupList(){
        Group_Vector.clear();
        Group_ObservableList.clear();
        GroupListService.PullGroupList(AccountNumber);
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Group_ObservableList.addAll(Group_Vector);
        for (Group temp:Group_Vector) {
            GroupHeadPortrait.put(temp.getGroupNumber(),
                    new File(FileUtils.saveFile(temp.getGroupHeadPortraitFileBytes(), ClientFileAddress.GroupHeadPortrait,temp.getGroupHeadPortraitFile().getName())));
        }
        //群成员的头像也一并放入好友列表里
    }

    //刷新好友列表
    public void RefreshFriendList(){
        Friend_Vector.clear();
        ChatFriend_ObservableList.clear();
        FriendListService.PullFriendList(AccountNumber);
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (User temp:Friend_Vector) {
            FriendHeadPortrait.put(temp.getAccountNumber(),
                    new File(FileUtils.saveFile(temp.getHeadPortrait_byte(), ClientFileAddress.HeadPortrait,temp.getHeadPortraitFile().getName())));
        }
        //自己的
        FriendHeadPortrait.put(PersonInformation.getAccountNumber(),new File(FileUtils.saveFile(PersonInformation.getHeadPortrait_byte(), ClientFileAddress.HeadPortrait,PersonInformation.getHeadPortraitFile().getName())));
        ChatFriend_ObservableList.addAll(Friend_Vector);

    }


    /**
     * Ctrl +  Enter 发送消息
     * @param keyEvent
     */
    public void onKeyPressedTextArea(KeyEvent keyEvent) {
        if("".equals(Content_TextArea.getText())){
            return;
        }
        // 如果按下了回车键
        if (keyEvent.getCode() == KeyCode.ENTER) {
            // 获得此时的光标位置。此位置为刚刚输入的换行符之后
            int caretPosition = Content_TextArea.getCaretPosition();

            // 如果已经按下的按键中包含 Control 键
            if (keyEvent.isControlDown()) {
                SentContent();
                /*----- 如果希望发送后保留输入框文本，需要只使用下面这行代码，然后去掉清除文本框的代码 -------*/
                // this.textArea.positionCaret(caretPosition - 1);
            } else {
                // 获得输入文本，此文本不包含刚刚输入的换行符
                //String text = Content_TextArea.getText();
                // 获得光标两边的文本
                //String front = text.substring(0, caretPosition);
                //String end = text.substring(caretPosition);
                // 在光标处插入换行符
                //Content_TextArea.setText(front + System.lineSeparator() + end);
                // 将光标移至换行符
                //Content_TextArea.positionCaret(caretPosition + 1);
            }
        }
    }


    public void ManageGroup(){
        if(GroupChatTarget==null){
            return;
        }
        if(GroupManageView.loginStage!=null) return;
        try {
            GroupManageView.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User getPersonInformation() {
        return PersonInformation;
    }

    public static void setPersonInformation(User personInformation) {
        PersonInformation = personInformation;
    }

    /*--------------------表情分割线--------------------*/
    public ArrayList<Emoji> AddEmo(Vector<String> Emos){
        Vector<Emoji> vector = new Vector<>();
        for (String emo:Emos) {
            vector.add(EmojiOne.getInstance().getEmoji(emo));
        }
        return new ArrayList<>(vector);
    }
    public void refreshTabs() {
        Map<String, List<Emoji>> map = EmojiOne.getInstance().getCategorizedEmojis(boxTone.getSelectionModel().getSelectedIndex()+1);
        map.put("frequently used",AddEmo(MainCoreController.Emoji_Vector));
        for(Tab tab : tabPane.getTabs()) {
            ScrollPane scrollPane = (ScrollPane) tab.getContent();
            FlowPane pane = (FlowPane) scrollPane.getContent();
            pane.getChildren().clear();
            String category = tab.getId().toLowerCase();
            if(map.get(category) == null) continue;
            map.get(category).forEach(emoji -> pane.getChildren().add(createEmojiNode(emoji)));
        }
    }

    private Node createEmojiNode(Emoji emoji) {
        StackPane stackPane = new StackPane();
        stackPane.setMaxSize(32, 32);
        stackPane.setPrefSize(32, 32);
        stackPane.setMinSize(32, 32);
        stackPane.setPadding(new Insets(3));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(32);
        imageView.setFitHeight(32);
        imageView.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(emoji.getHex())));
        stackPane.getChildren().add(imageView);

        Tooltip tooltip = new Tooltip(emoji.getShortname());
        Tooltip.install(stackPane, tooltip);
        stackPane.setCursor(Cursor.HAND);
        ScaleTransition st = new ScaleTransition(Duration.millis(90), imageView);

        stackPane.setOnMouseEntered(e-> {
            //stackPane.setStyle("-fx-background-color: #a6a6a6; -fx-background-radius: 3;");
            imageView.setEffect(new DropShadow());
            st.setToX(1.2);
            st.setToY(1.2);
            st.playFromStart();
            if(txtSearch.getText().isEmpty())
                txtSearch.setPromptText(emoji.getShortname());
        });
        stackPane.setOnMouseExited(e-> {
            //stackPane.setStyle("");
            imageView.setEffect(null);
            st.setToX(1.);
            st.setToY(1.);
            st.playFromStart();
        });
        stackPane.setOnMouseClicked(e-> {
            //stackPane.setStyle("");

        });
        stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String name = event.getButton().name();
                //双击事件
                if(event.getClickCount()==2&&event.getButton().name().equals(MouseButton.PRIMARY.name())){

                }
                //右击事件
                if(event.getButton().name().equals(MouseButton.SECONDARY.name())){
                    if(tabPane.getSelectionModel().getSelectedIndex()==0){
                        Stage dialogStage = new Stage();
                        DialogPane dialog = new DialogPane();
                        dialog.setHeaderText("");
                        dialog.setContentText("是否要将该常用表情删除?");
                        dialog.getButtonTypes().add(ButtonType.OK);
                        dialog.getButtonTypes().add(ButtonType.NO);
                        Button close = (Button)dialog.lookupButton(ButtonType.OK);
                        close.setText("删除");
                        close.setOnAction(event1 -> {
                            EmojiService.DeleteEmoji(emoji.getShortname());
                            for (int i = 0; i < MainCoreController.Emoji_Vector.size(); i++) {
                                if(MainCoreController.Emoji_Vector.get(i).equals(emoji.getShortname())){
                                    MainCoreController.Emoji_Vector.remove(i);
                                    break;
                                }
                            }
                            refreshTabs();
                            dialogStage.close();
                        });
                        Button button = (Button)dialog.lookupButton(ButtonType.NO);
                        button.setText("算了");
                        button.setOnAction(event1 -> {
                            dialogStage.close();
                        });
                        Scene dialogScene = new Scene(dialog,TRANSPARENT);
                        dialogStage.setScene(dialogScene);
                        dialogStage.setTitle("删除表情");
                        dialogStage.initStyle(StageStyle.UTILITY);
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.setResizable(false);
                        dialogStage.show();
                    }else{
                        Stage dialogStage = new Stage();
                        DialogPane dialog = new DialogPane();
                        dialog.setHeaderText("");
                        dialog.setContentText("是否要将该表情添加到常用?");
                        dialog.getButtonTypes().add(ButtonType.OK);
                        dialog.getButtonTypes().add(ButtonType.NO);
                        Button close = (Button)dialog.lookupButton(ButtonType.OK);
                        close.setText("添加");
                        close.setOnAction(event1 -> {
                            EmojiService.AddEmoji(emoji.getShortname());
                            MainCoreController.Emoji_Vector.add(emoji.getShortname());
                            refreshTabs();
                            dialogStage.close();
                        });
                        Button button = (Button)dialog.lookupButton(ButtonType.NO);
                        button.setText("算了");
                        button.setOnAction(event1 -> {
                            dialogStage.close();
                        });
                        Scene dialogScene = new Scene(dialog,TRANSPARENT);
                        dialogStage.setScene(dialogScene);
                        dialogStage.setTitle("添加表情");
                        dialogStage.initStyle(StageStyle.UTILITY);
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.setResizable(false);
                        dialogStage.show();

                    }
                }
                //左击事件
                if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                    Content_TextArea.setText(Content_TextArea.getText()+emoji.getShortname());
                }

                if(event.getButton().name().equals(MouseButton.MIDDLE.name())){
                }

            }
        });
        return stackPane;
    }

    private String getEmojiImagePath(String hexStr) {
        return getClass().getResource("png_40/" + hexStr + ".png").toExternalForm();
    }
    public void ScrollEvent(){
    }

    class ToneCell extends ListCell<Image> {
        private final ImageView imageView;
        public ToneCell() {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            imageView = new ImageView();
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
        }
        @Override
        protected void updateItem(Image item, boolean empty) {
            super.updateItem(item, empty);
            if(item == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                imageView.setImage(item);
                setGraphic(imageView);
            }
        }
    }
    /*--------------------表情分割线--------------------*/
    /*--------------消息分割线------------*/
    private void addText(String text) {
        Text textNode = new Text(text);
        textNode.setFont(Font.font(16));
        flowOutput.getChildren().add(textNode);
    }
    private Node createEmojiNode_Msg(Emoji emoji) {
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
    /*--------------消息分割线------------*/
}

