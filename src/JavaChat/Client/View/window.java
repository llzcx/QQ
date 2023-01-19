package JavaChat.Client.View;

import javafx.scene.Parent;

import javafx.stage.Stage;
public abstract class window extends Stage {
    Parent root;
    public Object $(String id) {
        return (Object) root.lookup("#" + id);
    }
    public Parent getRoot() {
        return root;
    }

}
