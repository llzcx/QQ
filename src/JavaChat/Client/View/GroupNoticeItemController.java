package JavaChat.Client.View;

import JavaChat.Common.Pojo.GroupNotice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GroupNoticeItemController {
    @FXML
    private Label GroupNumber;
    @FXML
    private Label Event;
    @FXML
    private ImageView ActiveHeadPortrait;

    private GroupNotice groupNotice;
    @FXML
    public void initialize(){

    }

    public String getGroupNumber() {
        return GroupNumber.getText();
    }

    public void setGroupNumber(String groupNumber) {
        GroupNumber.setText(groupNumber);
    }



    public String getEvent() {
        return Event.getText();
    }

    public void setEvent(String event) {
        Event.setText(event);
    }

    public Image getActiveHeadPortrait() {
        return ActiveHeadPortrait.getImage();
    }

    public void setActiveHeadPortrait(Image activeHeadPortrait) {
        ActiveHeadPortrait.setImage(activeHeadPortrait);
    }

    public GroupNotice getGroupNotice() {
        return groupNotice;
    }

    public void setGroupNotice(GroupNotice groupNotice) {
        this.groupNotice = groupNotice;
    }

}
