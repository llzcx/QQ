package JavaChat.Client.View;

import JavaChat.Common.Utils.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;

public class TextController {
//    public static Image zaiixanimage= FileUtils.File_Image(new File(""));
//    public static Image buzaixianImage = FileUtils.File_Image(new File(""));
    @FXML
    public ImageView HeadPortrait_ImageView;
    @FXML
    public Text id;
    @FXML
    public Text qianming;
    @FXML
    public ImageView offline_ImageView;
    @FXML
    public  ImageView online_ImageView;
    @FXML
    public Label Account;
    @FXML
    public void initialize(){
        //online_ImageView.setImage(FileUtils.File_Image(new File("D:\\JavaChatServerFile\\HeadPortrait_Image\\菈妮.jpg")));
        //offline_ImageView.setImage(FileUtils.File_Image(new File("D:\\JavaChatServerFile\\HeadPortrait_Image\\菈妮.jpg")));
        //online_ImageView.setVisible(false);
        //online_ImageView.setImage(FileUtils.File_Image(new File("C:\\Users\\陈翔\\IdeaProjects\\untitled5\\src\\JavaChat\\Client\\View\\fxml\\Image\\zaixian.png")));
    }
    public ImageView getHeadPortrait_ImageView() {
        return HeadPortrait_ImageView;
    }

    public void setHeadPortrait_ImageView(File HeadPortrait) {
        this.HeadPortrait_ImageView.setImage(FileUtils.File_Image(HeadPortrait));
    }

    public String getId() {
        return id.getText();
    }

    public void setId(String id) {
        this.id.setText(id);
    }

    public String getQianming() {
        return qianming.getText();
    }

    public void setQianming(String qianming) {
        this.qianming.setText(qianming);
    }

    public void setHeadPortrait_ImageView(Image headPortrait_ImageView) {
        HeadPortrait_ImageView.setImage(headPortrait_ImageView);
    }

    public Image getOffline_ImageView() {
        return offline_ImageView.getImage();
    }

    public void setOffline_ImageView(Image image) {
        offline_ImageView.setImage(image);
    }

    public Image getOnline_ImageView() {
        return online_ImageView.getImage();
    }

    public void setOnline_ImageView(Image image) {
        online_ImageView.setImage(image);
    }

    public void setOnline_ImageViewVisible(boolean flag) {
        this.online_ImageView.setVisible(flag);
    }
    public void setOffline_ImageViewVisible(boolean flag) {
        this.offline_ImageView.setVisible(flag);
    }

    public String getAccount() {
        return Account.getText();
    }

    public void setAccount(String account) {
        Account.setText(account);
    }
}