package JavaChat.Client.View;

import JavaChat.Client.Service.GroupApplicationService;
import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.RegularExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CreatGroupController {
    //群名字
    @FXML
    private TextField GroupName_TextField;
    private Boolean GroupNameKey;
    @FXML
    private Label GroupNameErrorTip_Label;
    //创建群按钮
    @FXML
    private Button CreatGroup_Button;
    @FXML
    private Label CreatGroupErrorTip_Label;
    private Boolean CreateGroupKey;
    //群简介公告
    @FXML
    private TextArea GroupAnnouncement_TextArea;
    //头像
    @FXML
    private ImageView HeadPortrait_ImageView;
    private File selectedFile;
    private byte[] bytes;
    //上传头像按钮
    @FXML
    private Button UploadAvatar_Button;
    @FXML
    private Label UploadAvatarErrorTip_Label;
    private Boolean HeadPortraitKey;
    //关闭
    @FXML private Button Close_Button;

    public void initialize(){
        GroupNameKey = false;
        HeadPortraitKey = false;
        CreateGroupKey = false;
        GroupNameErrorTip_Label.setText("请输入正确的群名字");
        UploadAvatarErrorTip_Label.setText("请上传群头像");
    }
    //群名字输入框监听事件
    public void KeyReleasedEmail_TextField(){
        if(RegularExpression.CheckName(GroupName_TextField.getText())){
            GroupNameKey = true;
            GroupNameErrorTip_Label.setText("");
        }else{
            GroupNameKey = false;
            GroupNameErrorTip_Label.setText("请输入正确的群名字");
        }
    }

    public void UploadAvatar(){
        FileChooser fileChooser = new FileChooser();
        //selectedFile返回一个File对象
        selectedFile = fileChooser.showOpenDialog(new Stage());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        System.out.println(selectedFile);
        if(selectedFile==null){
            UploadAvatarErrorTip_Label.setText("请上传您的头像");
            HeadPortraitKey =false;
            return; }
        UploadAvatarErrorTip_Label.setText("");
        HeadPortraitKey = true;
        Image image = null;
        image = FileUtils.File_Image(selectedFile);
        try {
            bytes = FileUtils.FileChangeToByte(selectedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(image);
        HeadPortrait_ImageView.setImage(image);
    }
    //创建监听
    public void EnterCreateGroup_Button(){
        CreatGroup_Button.setStyle(
                "-fx-background-color: #00f1ea;"
        );
        System.out.println(GroupNameKey+" "+HeadPortraitKey);
        if(HeadPortrait_ImageView!=null&&RegularExpression.CheckName(GroupName_TextField.getText())){
           GroupNameErrorTip_Label.setText("");
           GroupNameKey = true;
            UploadAvatarErrorTip_Label.setText("");
           HeadPortraitKey = true;
        }
        if(GroupNameKey&&HeadPortraitKey){
           CreateGroupKey = true;
        }else{
            CreateGroupKey = false;
            CreatGroupErrorTip_Label.setText("请务必先将创建群所需的信息输入完整");
        }
    }
    public void But_ColorExited(){
        CreatGroup_Button.setStyle(
                "-fx-background-color: #48bcff;"
        );
    }
    public void Closely(){
        Stage stage =(Stage) Close_Button.getScene().getWindow();
        stage.close();
        CreatGroupView.loginStage = null;
    }


    public void CreateGroup(){
        if(!CreateGroupKey){
            return;
        }
        GroupApplicationService.CreatGroupChat(MainCoreController.getAccountNumber(),GroupName_TextField.getText(),selectedFile,bytes);
    }




}
