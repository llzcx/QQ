package JavaChat.Client.ClientMain;
import JavaChat.Client.View.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main {
    public static class View extends Application {
        public static void main(String[] args){
            launch(args);
        }
        @Override
        public void start(Stage primaryStage) throws Exception{
            LoginView.start(primaryStage);
        }
    }
}


