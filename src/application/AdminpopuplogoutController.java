package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import toast.MyDialoug;

public class AdminpopuplogoutController implements Initializable{
	
	@FXML
	Button btnlogout,login,btncancel;
	
	MyDialoug mydia;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		btncancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				mydia.closeDialoug();

			}
		});
		
		btnlogout.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				Myapp.isadmin = false;
				if (Myapp.isadmin == false)
				{
				
					NFirstController.btnadminA.setVisible(true);
					NFirstController.btnadminlogoutA.setVisible(false);
						
				}

				mydia.closeDialoug();
			}
		});
		
	}

}
