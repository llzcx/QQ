package JavaChat.Common.Utils;


import JavaChat.Client.View.AlterPasswordView;
import JavaChat.Client.View.MainCoreController;
import JavaChat.Common.Pojo.GroupMessage;
import JavaChat.Common.Pojo.Message;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.List;
import java.util.Vector;

import static javafx.scene.paint.Color.TRANSPARENT;

public class JavaFxUtils {

    public static void ShowStringOnLabel(Object label, String tip) {

        Thread t = new Thread(() -> {
            try {
                if (label != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((Label) (label)).setText(tip);
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
                Thread.sleep(3000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //防止登录页面被关闭了
                        if (label != null) {
                            ((Label) (label)).setText("");
                        }
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();

    }
    public static String getStringFromLabel(Object label){
        final String[] ans = {""};
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ans[0] = ((Label) (label)).getText();
                        }
                    });
                    return ans[0];
    }
    public static void ShowStringOnLabelForever(Object label, String tip) {

        Thread t = new Thread(() -> {
            try {
                if (label != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((Label) (label)).setText(tip);
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }

    public static void ShowStringOnTextFieldForever(Object TextField, String tip) {

        Thread t = new Thread(() -> {
            try {
                if (TextField != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((TextField) (TextField)).setText(tip);
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }

    public static void ShowStringOnTextAreaForever(Object nTextArea, String tip) {

        Thread t = new Thread(() -> {
            try {
                if (nTextArea != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((TextArea) (nTextArea)).setText(tip);
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }
    public static void ShowStringOnTextField(Object textField, String tip) {
        Thread t = new Thread(() -> {
            try {
                if (textField != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((TextField) (textField)).setText(tip);
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }


    public static void SetImageOnImageView(Object imageView, File file) {
        Thread t = new Thread(() -> {
            try {
                if (imageView != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((ImageView) (imageView)).setImage(FileUtils.File_Image(file));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }

    public static void RedCircle_Visible(Object circle,Boolean flag) {
        Thread t = new Thread(() -> {
            try {
                if (circle != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((Circle) (circle)).setVisible(flag);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }

    public static void AddToObservableList(ObservableList observableList, Object object) {
        Thread t = new Thread(() -> {
            try {
                if (observableList != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                observableList.add(object);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }

    public static void AddToObservableList(ObservableList observableList, Vector<Object> vector) {
        Thread t = new Thread(() -> {
            try {
                if (observableList != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                observableList.addAll(vector);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }

    public static void InversionAddToObservableList(ObservableList observableList, Vector<Object> vector) {
        Thread t = new Thread(() -> {
            try {
                if (observableList != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Vector<Object> vector1 = new Vector<>();
                                vector1.addAll(observableList);
                                observableList.clear();
                                for (int i = vector.size()-1; i >=0 ; i--) {
                                    observableList.add(vector.get(i));
                                }
                                observableList.addAll(vector1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }


    public static void ChangeObservableList(ObservableList observableList, int index, Object object) {
        Thread t = new Thread(() -> {
            try {
                if (observableList != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                observableList.set(index,object);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public static void HideOrShowButton(Object button, boolean flag){
        Thread t = new Thread(() -> {
            try {
                if (button != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((Button)(button)).setVisible(flag);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
    public static void DeleteObservableList(ObservableList observableList, int index) {
        Thread t = new Thread(() -> {
            try {
                if (observableList != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(observableList.get(index)!=null){
                                    observableList.remove(index);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public static void scrollListViewToBottom(ListView listView) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        List items = listView.getItems();
                        int index = items.size();
                        listView.scrollTo(index);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * rectangle的可见/不可见
     * @param rec
     * @param flag
     */
    public static void HideOrShowRectangle(Object rec, boolean flag){
        Thread t = new Thread(() -> {
            try {
                if (rec != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((Rectangle)(rec)).setVisible(flag);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }


    /**
     * 改变javafx进度条
     * @param progressBar 进度条
     * @param max 最大
     * @param current 当前
     * @param flag 显示?or不显示
     */
    public static void ChangeProgressBar(Object progressBar,double max,double current,boolean flag){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ((ProgressBar)(progressBar)).setProgress(current/max);
                    ((ProgressBar)(progressBar)).setVisible(flag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void OpenFileChoose(String path){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("data/json/invoices"));
        File selectedDirectory = directoryChooser.showDialog(new Stage());
    }



    public static void ClearObservableList(Object observableList){
        Thread t = new Thread(() -> {
            try {
                if (observableList != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((ObservableList)observableList).clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public static void SetImageViewVisible(Object imageView,boolean flag){
        Thread t = new Thread(() -> {
            try {
                if (imageView != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((ImageView)imageView).setVisible(flag);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("imageView控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public static void SetProgressVisible(Object progress,boolean flag){
        Thread t = new Thread(() -> {
            try {
                if (progress != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((ProgressBar)progress).setVisible(flag);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    System.out.println("控件没有找到");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public static void ShowTipDialogStage(String title, String ContentText){
        Thread t = new Thread(() -> {
            try {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Stage dialogStage = new Stage();
                                DialogPane dialog = new DialogPane();
                                dialog.setHeaderText("");
                                dialog.setContentText(ContentText);
                                dialog.getButtonTypes().add(ButtonType.NO);
                                Button close = (Button)dialog.lookupButton(ButtonType.NO);
                                close.setText("关闭");
                                close.setOnAction(event -> dialogStage.close());
                                Scene dialogScene = new Scene(dialog,TRANSPARENT);
                                dialogStage.setScene(dialogScene);
                                dialogStage.setTitle(title);
                                dialogStage.initStyle(StageStyle.UTILITY);
                                dialogStage.initModality(Modality.WINDOW_MODAL);
                                dialogStage.setResizable(false);
                                dialogStage.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public static void ShowNewEventStage(String title, String ContentText, EventHandler<ActionEvent> value,String eventName1){
        Thread t = new Thread(() -> {
            try {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Stage dialogStage = new Stage();
                            DialogPane dialog = new DialogPane();
                            dialog.setHeaderText("");
                            dialog.setContentText(ContentText);
                            dialog.getButtonTypes().add(ButtonType.OK);
                            dialog.getButtonTypes().add(ButtonType.NO);
                            Button exit = (Button)dialog.lookupButton(ButtonType.OK);
                            Button close = (Button)dialog.lookupButton(ButtonType.NO);
                            exit.setText(eventName1);
                            exit.setOnAction(value);
                            close.setText("关闭");
                            close.setOnAction(event -> dialogStage.close());
                            Scene dialogScene = new Scene(dialog,TRANSPARENT);
                            dialogStage.setScene(dialogScene);
                            dialogStage.setTitle(title);
                            dialogStage.initStyle(StageStyle.UTILITY);
                            dialogStage.initModality(Modality.WINDOW_MODAL);
                            dialogStage.setResizable(false);
                            dialogStage.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public static void CloseStage(Object stage){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ((Stage)stage).close();
            }
        });
    }

}
