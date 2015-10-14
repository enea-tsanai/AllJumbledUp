package AllJumbledUp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by enea on 10/9/15.
 */
public class MenuController {

    private AllJumbledUp allJumbledUp;

    @FXML
    private void initialize() {


    }


    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.print("Button clicked!");
        allJumbledUp.showMainGameScene();
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MenuController() {
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param allJumbledUpApp
     */
    public void setMainApp(AllJumbledUp allJumbledUpApp) {
        System.out.println("Controller started");
        this.allJumbledUp = allJumbledUpApp;
        //bindData();
    }

}
