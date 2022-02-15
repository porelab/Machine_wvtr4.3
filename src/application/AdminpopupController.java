package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import report.FirstController;
import toast.MyDialoug;
import toast.Openscreen;
import toast.Toast;

public class AdminpopupController implements Initializable {
	
	
	@FXML
	Button btncancel,login;
	
	
	MyDialoug mydia;
	
	@FXML
	TextField txtuserid;
	
	@FXML
	
	PasswordField txtpass;
	
	boolean isadmin = false;

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

		login.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Login call");
				if(!txtuserid.getText().trim().equals("")&& !txtpass.getText().trim().equals(""))
				{
						
				if(!txtuserid.getText().trim().equals("M19"))
				{
				
					Toast.makeText(Main.mainstage, "Please eneter Conrrect User id.", 3000, 500, 500);
					 
				
				}
				else
				{
					if(!txtpass.getText().trim().equals("M19"))
					{
						System.out.println("Wrong pass");
						Toast.makeText(Main.mainstage, "Please eneter Conrrect password.", 3000, 500, 500);
					//	error.setVisible(true);
					}
					
					else
					{
						
						
						System.out.println("Login sucsses");
						
						Myapp.isadmin = true;
						
						if (Myapp.isadmin == true)
						{
						
							NFirstController.btnadminA.setVisible(false);
							NFirstController.btnadminlogoutA.setVisible(true);
								
						}
						
						
						System.out.println("Is Admin"+Myapp.isadmin);
						
						
							// Openscreen.open("/application/first.fxml");
								
						mydia.closeDialoug();
					}
					   
				}
				
				}
				else {
					Toast.makeText(Main.mainstage, "Fill up the required details.", 3000, 500, 500);
				}
				
				}


			
			
		});

		
	}

}
