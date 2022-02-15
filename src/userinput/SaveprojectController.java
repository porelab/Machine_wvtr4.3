package userinput;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import toast.MyDialoug;
import toast.Openscreen;
import application.Database;
import application.Main;
import application.Myapp;

import com.jfoenix.controls.JFXTextField;

import extrafont.Myfont;

public class SaveprojectController implements Initializable {
	
	 @FXML
	    private JFXTextField projectname;

	    @FXML
	    private Button btnsave,btncancel;

	    Myfont f=new Myfont(22);
	    
	    @FXML
	    Label lblerror;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		/*New test setup complete after open save project popup*/
		
		 btncancel.getStyleClass().add("transperant_comm");
		  
		//setLabelFont();
		 
		
		btncancel.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				MyDialoug.closeDialoug();
				System.out.println("canclelll NTNTN");
			}
		});
		
		btnsave.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!projectname.getText().trim().equals(""))
				{
				setProjectData();
				Openscreen.open("/userinput/Nlivetest.fxml");
				lblerror.setVisible(false);
				MyDialoug.closeDialoug();
				
				}
				else {
				lblerror.setVisible(true);
				}
			}
		});
				
	}

	void setLabelFont()
	{
		btnsave.setFont(f.getM_M());
		projectname.setFont(f.getM_M());

	}
	
	
	void setProjectData()
	{
		Database d1=new Database();
		
		if(d1.Insert("INSERT INTO projects VALUES('"+Myapp.sampleid+"','"+Myapp.indtype+"','"+Myapp.materialapp+"','"+Myapp.classification+"','"+Myapp.crossection+"','"+Myapp.thresold+"','"+Myapp.fluidname+":"+Myapp.fluidvalue+"','"+Myapp.email+"','"+projectname.getText()+"','"+Myapp.testtrial+"','"+Myapp.tfactore+"','"+Myapp.splate+"','"+Myapp.thikness+"','"+Myapp.materialtype+"','"+Myapp.endpress+"','"+Myapp.accbpt+"','"+Myapp.accstep+"','"+Myapp.lotnumber+"','"+Myapp.startpress+"','"+Myapp.testsequence+"','"+Myapp.stabilitytype+"','"+Myapp.accstability+"')"))
		{
			
		System.out.println("Insert data New Project");
		}
		else
		{
			System.out.println("not insertetd");
		}

	}
	
	
}
