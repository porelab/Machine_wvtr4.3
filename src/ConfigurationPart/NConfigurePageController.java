
package ConfigurationPart;

import gnu.io.CommPortIdentifier;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import toast.MyDialoug;
import toast.Openscreen;
import toast.Toast;
import Notification.Notification;
import application.DataStore;
import application.Database;
import application.Main;
import application.Myapp;

import com.jfoenix.controls.JFXToggleButton;

public class NConfigurePageController implements Initializable {

	@FXML
	AnchorPane root;
	
	@FXML
    JFXToggleButton pg1,pg2,fm1,fm2,re1,re2,tgbkeyboard,valvecflow;
	
	private Notification.Notifier notifier;
	
    @FXML
    Label comlab;

    @FXML
    ImageView imgdownarrow,imgback;
    
    @FXML
    private TextField pfm1,pfm2,txtdataint,txtstarttempsystem,txtendttempsystem,txtendthumisystem,txtstartthumisystem,txtstartthumitestconfi,txtendthumitestconfi,txtendttemptestconfi,txtstartttemptestconfi,txttempsensor,txthumiditysensor;

    @FXML
    private Button applypro;

    @FXML
    private JFXToggleButton tgb215,tgb2111;
    
    static ToggleGroup tgbtype,tgbmethod;

    @FXML
    private Button btndefaultsetting,comsave,back,btncalibration,testconfig,btnprefrance;

    @FXML
    ComboBox cmbcom;
    
    @FXML
    ComboBox<String> cmbpress;

	String propg1="low",profm1="low",propg2="low",profm2="low";
	
	String pp1scaletype="absolute",pp2scaletype="absolute",curvefit="off",crospres="0",crosflov="0";

	static String selectedrad="",Por,selectedradtesttype,selectedradtestmethod;
	
	public static boolean bolkey = false;
	
	Database db=new Database();
	
	static ToggleGroup tgb5,tgb6;
	static String selectedrad4 = "",selectedrad5 = "";
	
	@FXML
    RadioButton pressregulator,rbupright,rbinverted,rbwater,rbdesiccant;

   MyDialoug mydia;
    
	void addShortCut() {
		
		 KeyCombination backevent = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_ANY);
			
		root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				
				if(backevent.match(ke))
				{

					Openscreen.open("/application/first.fxml");
				}
				
			}
		});
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
btndefaultsetting.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
			
				mydia = new MyDialoug(Main.mainstage,
						"/ConfigurationPart/defaultsettingpopup.fxml");
				mydia.showDialoug();
			
			}
		});
		
			addShortCut();
	    setTestmethod();
	    setTesttype();
			setMainBtns();
	     setulastdata();
	     setTestLastunite();
			setLastunite();
		 setBtnClicks();
	
		setPortList();
		setCalibrationType();
		setValveselection();
		selectelowhigh();
		
	
		DataStore.getthfirstbp();
		// cmbcom.getItems().addAll("Test", "Test2", "Test3");
//		cmbpress.getItems().addAll("psi", "bar", "torr");
		
		
			 setkeyboardmode();
		
		 }

			void setMainBtns()
			{
					 Image image;
					 
					 image = new Image(this.getClass().getResourceAsStream("/ConfigurationPart/downarrow.png"));
				     imgdownarrow.setImage(image);
				
					 image = new Image(this.getClass().getResourceAsStream("/ConfigurationPart/back.png"));
				     imgback.setImage(image);
	
				
			}
	
			void setBtnClicks() {
				
				/* selected unite save in database 
				btnprefrance.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						unitesave();
					}
				});*/

		
				back.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						Openscreen.open("/application/first.fxml");
						
					}
				});
				
				comsave.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						
					comsave();
					}
				});
				
				apllaypro();
			
		
				
				
				testconfig.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						
						setValveselection();
						
						
						
						
						
						String sql = "update Ntestconfi set stemp='"+txtstartttemptestconfi.getText()+"',etemp='"+txtendttemptestconfi.getText()+"',shumi='"+txtstartthumitestconfi.getText()+"',ehumi='"+txtendthumitestconfi.getText()+"',dataint='"+txtdataint.getText()+"',testtype='"+Myapp.testtype+"',testmethod='"+Myapp.testmethod+"'"; 
						
						
						if(db.Insert(sql))
						{
							 Toast.makeText(Main.mainstage, "Successfully Save Configuration Data..", 1000, 200, 200);

						}
						else {
						System.out.println("Configration Data save d Eroorr.....");
						
						}
						
						
						
						
					}
				});
				
				
			
			}
											   
				void selectelowhigh()
				{
					if(pg1.isSelected())
					{
						propg1="high";
					}
					
					else 
					{
						propg1="low";
					}

			//system config Pressure Guage-2----------------------------
					
					if(pg2.isSelected())
						{
							propg2="high";	
						}
						
						else 
						{
							propg2="low";
						}
					
			//system config Flow Meter-1----------------------------							
				

					if(fm1.isSelected())
							{
								profm1="high";
							}
							
							else 
							{
								profm1="low";
							}
							

						if(fm2.isSelected())
								{
								profm2="high";
								}
								
								else 
								{

									profm2="low";
								}
								
						
						

								

				}
				
				void setValveselection()
				{

					
					
					if(valvecflow.isSelected())
					{
						crosflov="1";
					}
					
					else 
					{
						crosflov="0";
					}
				}
				
				public void setPortList()
				{
					System.out.println("Loading list of ports");
					
					Enumeration pList = CommPortIdentifier.getPortIdentifiers();
					

					while (pList.hasMoreElements()) {
						
						CommPortIdentifier cpi = (CommPortIdentifier) pList.nextElement();
					    System.out.print("Port " + cpi.getName() +" "+cpi.getPortType());
					    cmbcom.getItems().add(cpi.getName());
					    
					   
					    
					}
					   
					
				}
				
				void unitesave() {

					String upress, uflow, roundoff, ulenghth;

					upress = cmbpress.getSelectionModel().getSelectedItem();
					
					String unites = "update unite set pressure='" + upress + "'";

					Database dd = new Database();

					if (dd.Insert(unites)) {
						Toast.makeText(Main.mainstage, "Successfully saved selected Unites", 1000, 200, 200);

					}

				}

				
				

				
				
				
		void comsave()
		{
			try{
				if(cmbcom.getSelectionModel().getSelectedItem()==null)
				{
					System.out.println("No Comport found");
				}
				else
				{
				//	notifier.notify( NotificationBuilder.create().title("Save.").message(cmbcom.getSelectionModel().getSelectedItem()+" save.").image(Notification.SUCCESS_ICON).build());
					 Toast.makeText(Main.mainstage, "Successfully saved "+cmbcom.getSelectionModel().getSelectedItem(), 1000, 200, 200);

					System.out.println("Com set to:"+cmbcom.getSelectionModel().getSelectedItem());
					
					String query = "update configdata set comport='"+cmbcom.getSelectionModel().getSelectedItem()+"'"; 
				
					Database dd = new Database();
					dd.Insert(query);
					comlab.setText(""+cmbcom.getSelectionModel().getSelectedItem());
				  

				}
				}
				catch(Exception e)
				{
					
				}
			
			
			if (tgbkeyboard.isSelected()) {
				bolkey=true;
				Myapp.tabletmode="true";
				
	        } else {
	        	bolkey=false;
	        	Myapp.tabletmode="false";
			
	        }
			
			String query = "update keyboardmode set mode='"+Myapp.tabletmode+"'";

			db.Insert(query);
			
			
			
		}
		
		void apllaypro()
		{
			selectelowhigh();
			applypro.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					
					
		/*			
					String type="Pro";
				
					try {
						String sql1 ="delete from configdata";
						db.Insert(sql1);
						
					} catch (Exception e)
					{
					
					}*/
					String sql = "update Nsystem set stemp='"+txtstarttempsystem.getText()+"',etemp='"+txtendttempsystem.getText()+"',shumi='"+txtstartthumisystem.getText()+"',ehumi='"+txtendthumisystem.getText()+"',tempsensor='"+txttempsensor.getText()+"',humisensor='"+txthumiditysensor.getText()+"'"; 

					
					if(db.Insert(sql))
					{
						 Toast.makeText(Main.mainstage, "Successfully Apply", 1000, 200, 200);
							System.out.println("applypro------>"+sql);
							

					}
					else {
						System.out.println("Configration Data save d Eroorr.....");
					}

				
					
					
				}
			});
		}
		
		void setTestLastunite() {
			List<List<String>> ll = db.getData("select * from testunite");

			String pg1 = (ll.get(0).get(0));
			String pg2 = (ll.get(0).get(1));
		
		

				
			
		
		
		}
		
		void setLastunite() {
			List<List<String>> ll = db.getData("select * from unite");
			String upres = (ll.get(0).get(0));
		
		//	cmbpress.setValue(upres);
		

		}

		/*Test Type*/
		
		void setTesttype() {

			tgbtype = new ToggleGroup();

			rbupright.setToggleGroup(tgbtype);
			rbupright.setUserData("1");
			rbinverted.setToggleGroup(tgbtype);
			rbinverted.setUserData("2");
			
			selectedradtesttype  = "1";
			Myapp.testtype = "Upright";

			tgbtype.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> arg0,
						Toggle arg1, Toggle arg2) {
					if (arg2 == null)
						arg1.setSelected(true);
					selectedradtesttype = arg2.getUserData().toString();

					if (selectedrad4.equals("1")) {
						Myapp.testtype = "Upright";
					}

					
					else {
						Myapp.testtype = "Inverted";
					}

				}

			}

			);

		}

	/*Test Method*/
		
		void setTestmethod() {

			tgbmethod = new ToggleGroup();

			rbwater.setToggleGroup(tgbmethod);
			rbwater.setUserData("1");
			rbdesiccant.setToggleGroup(tgbmethod);
			rbdesiccant.setUserData("2");
			
			selectedradtestmethod  = "1";
			Myapp.testmethod = "Water";

			tgbmethod.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> arg0,
						Toggle arg1, Toggle arg2) {
					if (arg2 == null)
						arg1.setSelected(true);
					selectedradtestmethod = arg2.getUserData().toString();

					if (selectedradtestmethod.equals("1")) {
						Myapp.testmethod = "Water";
					}

					
					else {
						Myapp.testmethod = "Desiccant";
					}

				}

			}

			);

		}

		
		
		public void setulastdata()
		{	
			comlab.setText(DataStore.getCom());

			
			try{
				Database db=new Database();
		
				List<List<String>> ll=db.getData("select * from Nsystem");
				
				System.out.println("All Data ---->"+ll);
					txtstarttempsystem.setText(ll.get(0).get(0));
					txtendttempsystem.setText(ll.get(0).get(1));
					txtstartthumisystem.setText(ll.get(0).get(2));
					txtendthumisystem.setText(ll.get(0).get(3));
				
					txttempsensor.setText(ll.get(0).get(4));
					txthumiditysensor.setText(ll.get(0).get(5));
				
					
					List<List<String>> l2=db.getData("select * from Ntestconfi");
					System.out.println("All Data ---->"+l2);
					txtstartttemptestconfi.setText(l2.get(0).get(0));
					txtendttemptestconfi.setText(l2.get(0).get(1));
					txtstartthumitestconfi.setText(l2.get(0).get(2));
					txtendthumitestconfi.setText(l2.get(0).get(3));

					txtdataint.setText(l2.get(0).get(4));
					String testtype = "" + l2.get(0).get(5);
					String testmethod = "" + l2.get(0).get(6);
					

					
					/*Test Type*/
					if (testtype.equals("Upright")) {
						rbupright.selectedProperty().set(true);
						Myapp.testtype = "Upright";

					}
					else {
						rbinverted.selectedProperty().set(true);
						Myapp.testtype = "Inverted";

					}
					
					/*Test Method*/
					if (testmethod.equals("Water")) {
						rbwater.selectedProperty().set(true);
						Myapp.testmethod = "Water";

					}
					else {
						rbdesiccant.selectedProperty().set(true);
						Myapp.testmethod = "Desiccant";

					}
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			
			}	
		void setkeyboardmode() {
			
			Database db=new Database();
			
			List<List<String>> ll=db.getData("select * from keyboardmode");
			String mode =(ll.get(0).get(0));
			
			if(mode.equals("true"))
			{
				tgbkeyboard.setSelected(true);	
				bolkey=true;
			}
			else
			{
				tgbkeyboard.setSelected(false);	
				bolkey=false;
			}
			
			
		}
		
		void setCalibrationType() {

			tgb5 = new ToggleGroup();

			pressregulator.setToggleGroup(tgb5);
			pressregulator.setUserData("1");
			

			selectedrad4 = "1";


			tgb5.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> arg0,
						Toggle arg1, Toggle arg2) {
					if (arg2 == null)
						arg1.setSelected(true);
					selectedrad4 = arg2.getUserData().toString();

					if (selectedrad4.equals("1")) {
						
						
					}

					else if (selectedrad4.equals("2")) {
						

					}


					else if (selectedrad4.equals("3")) {
						System.out.println("3");
					}

					else if (selectedrad4.equals("4")) {
					

					}
					else {

					}
				}

			}
		);

	}

	

}
