package application;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import ConfigurationPart.NConfigurePageController;
import toast.MyDialoug;
import toast.Openscreen;
import toast.Toast;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import junit.extensions.TestDecorator;

public class PopfxmlController implements Initializable {

	@FXML
	AnchorPane root1;
	
	int valbpt,valtestacc,valdatas;


	@FXML
	Label  pendp,pthresold, pfluid;

	@FXML
	TextField psid,lotno,txtthickness,txttestdata,txtdatainterval;
	
	@FXML
	Button starttest, btncancel;

	@FXML
	ComboBox<String> cmboproject;

	@FXML
	RadioButton bwd, dbw, bdw,datas1,datas2,datas3;

	static String selectedradtm = "",selectedrad6 = "",selectedrad7 = "";

	static ToggleGroup tgb6,tgb7,tgbtm;
	


	    @FXML
	    private RadioButton rbupright;

	    @FXML
	    private RadioButton rbinverted;

	    @FXML
	    private RadioButton rbwater;

	    @FXML
	    private RadioButton rbdesiccant;

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println("I am in Quick test");
		setTesType();
		setTestMethod();
		setAllThings();
		
		setTestSequence();
		// addShortCut();
		LoadProject();
	
		setkeyboardmode();

		if (NConfigurePageController.bolkey) {
			setClickkeyboard();

		} else {
		
		}
		
		btncancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				MyDialoug.closeDialoug();

			}
		});

		starttest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				teststart();

			}
		});

		
	}
	
	void setTesType() {

		tgb6 = new ToggleGroup();

		rbupright.setToggleGroup(tgb6);
		rbupright.setUserData("1");
		rbinverted.setToggleGroup(tgb6);
		rbinverted.setUserData("2");
	
		selectedrad6 = "1";
		Myapp.testsequence = "Upright";

		tgb6.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0,
					Toggle arg1, Toggle arg2) {

				selectedrad6 = arg2.getUserData().toString();

				if (selectedrad6.equals("1")) {
					Myapp.testsequence = "Upright";
				}

				 else {
					Myapp.testsequence = "Inverted";
				}

			}
		});

	}

	
		void setTestMethod() {

		tgbtm = new ToggleGroup();

		
		rbwater.setToggleGroup(tgbtm);
		rbwater.setUserData("1");
		rbdesiccant.setToggleGroup(tgbtm);
		rbdesiccant.setUserData("2");
		

		selectedradtm = "1";
		Myapp.testmethod = "Water";

		tgbtm.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {

					@Override
					public void changed(ObservableValue<? extends Toggle> arg0,
							Toggle arg1, Toggle arg2) {

						selectedradtm = arg2.getUserData().toString();

						if (selectedradtm.equals("1")) {
							Myapp.testmethod = "Water";
						}

						 else {
							Myapp.testmethod = "Desiccant";
						}

					}
				});

	}

	

	void addShortCut() {

		root1.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {

				if (ke.getCode() == KeyCode.ENTER) {
					teststart();
				}

			}
		});

	}

	void LoadProjectData(String projectname) {
		Database d = new Database();

		List<List<String>> alldata = d
				.getData("select * from Nprojects where useremailid='"
						+ Myapp.email + "' and project='" + projectname + "' ");

		psid.setText(alldata.get(0).get(0));
		lotno.setText(alldata.get(0).get(2));
		txtthickness.setText(alldata.get(0).get(4));
		
		txttestdata.setText(alldata.get(0).get(9));		
		txtdatainterval.setText(alldata.get(0).get(10));

		String testtype = "" + alldata.get(0).get(11);
		String testmethod = "" + alldata.get(0).get(12);
		
		
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
		
		
		
		/*New changes 01-07-2019*/
		
		
		Myapp.sampleid = alldata.get(0).get(0);
		Myapp.lotnumber = alldata.get(0).get(2);
		Myapp.thikness = alldata.get(0).get(3);
		Myapp.indtype = alldata.get(0).get(4);
		Myapp.materialtype = alldata.get(0).get(5);
		Myapp.classification = alldata.get(0).get(6);
		Myapp.materialapp = alldata.get(0).get(7);
		Myapp.splate = alldata.get(0).get(8);
		
		Myapp.testdata = alldata.get(0).get(9);
		Myapp.dataint = alldata.get(0).get(10);
		Myapp.testtype = alldata.get(0).get(11);
		Myapp.testmethod= alldata.get(0).get(12);
	
		
		
		
		
		
		
	}

	void LoadProject() {

		Database d = new Database();
		List<List<String>> info = d
				.getData("select project from Nprojects where useremailid='"
						+ Myapp.email + "'");
		try {
			cmboproject.getItems().add("Last Project");

			for (int i = 0; i < info.size(); i++) {
				cmboproject.getItems().add(info.get(i).get(0));

			}
		} catch (Exception e) {
			Exception ss;
		}

		if (cmboproject.getItems().size() > 0) {
			cmboproject.valueProperty().addListener(
					new ChangeListener<String>() {

						@Override
						public void changed(
								ObservableValue<? extends String> observable,
								String oldValue, String newValue) {
							// TODO Auto-generated method stub

							if (newValue.equals("Last Project")) {

								setLastData();

							} else {
								LoadProjectData(newValue);
							}
						}
					});

			cmboproject.getSelectionModel().select(0);

		}

	}

	void setTestSequence() {

		tgb6 = new ToggleGroup();

		bwd.setToggleGroup(tgb6);
		bwd.setUserData("1");		
		dbw.setToggleGroup(tgb6);
		dbw.setUserData("2");
		bdw.setToggleGroup(tgb6);
		bdw.setUserData("3");

		selectedrad6 = "1";
		Myapp.testsequence = "WUPDUP";

		tgb6.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0,
					Toggle arg1, Toggle arg2) {

				selectedrad6 = arg2.getUserData().toString();

				if (selectedrad6.equals("1")) {
					Myapp.testsequence = "WUPDUP";
				}

				else if (selectedrad6.equals("2")) {
					Myapp.testsequence = "DUPWUP";
				} else {
					Myapp.testsequence = "WUPDCALCULATED";
				}

				System.out.println("Select : " + selectedrad6);
			}
		});

	}
	void setAllThings()
	{
		
		setDataStability();
		
		
	}
	
	

	void setDataStability() {

		tgb7 = new ToggleGroup();

		datas1.setToggleGroup(tgb7);
		datas1.setUserData("1");
		datas2.setToggleGroup(tgb7);
		datas2.setUserData("2");
		datas3.setToggleGroup(tgb7);
		datas3.setUserData("3");

		selectedrad7 = "1";

	
	


		
		
	}


	void setLastData() {
		
		
		
		Database d1 = new Database();
		List<List<String>> alldata = d1
				.getData("select * from lastNprojects where lid='" + Myapp.email
						+ "' ");

		
		psid.setText(alldata.get(0).get(1));
		lotno.setText(alldata.get(0).get(2));
		txtthickness.setText(alldata.get(0).get(3));
		
		txttestdata.setText(alldata.get(0).get(9));		
		txtdatainterval.setText(alldata.get(0).get(10));

		String testtype = "" + alldata.get(0).get(11);
		String testmethod = "" + alldata.get(0).get(12);
		
		
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
		
		
		
		/*New changes 01-07-2019*/
		
		
		Myapp.sampleid = alldata.get(0).get(1);
		Myapp.lotnumber = alldata.get(0).get(2);
		Myapp.thikness = alldata.get(0).get(3);
		Myapp.indtype = alldata.get(0).get(4);
		Myapp.materialtype = alldata.get(0).get(5);
		Myapp.classification = alldata.get(0).get(6);
		Myapp.materialapp = alldata.get(0).get(7);
		Myapp.splate = alldata.get(0).get(8);
		
		Myapp.testdata = alldata.get(0).get(9);
		Myapp.dataint = alldata.get(0).get(10);
		Myapp.testtype = alldata.get(0).get(11);
		Myapp.testmethod= alldata.get(0).get(12);
	
		
		
	}

	void teststart() {

		
			 Myapp.sampleid = psid.getText();
			 Myapp.lotnumber = lotno.getText();
			 Myapp.thikness = txtthickness.getText();
				
				Myapp.testdata = txttestdata.getText();
				Myapp.dataint = txtdatainterval.getText();

			
		
		 
		MyDialoug.closeDialoug();
		Openscreen.open("/userinput/Nlivetest.fxml");

	}
	void setkeyboardmode() {

		Database db = new Database();

		List<List<String>> ll = db.getData("select * from keyboardmode");
		String mode = (ll.get(0).get(0));

		if (mode.equals("true")) {

			NConfigurePageController.bolkey = true;
		} else {

			NConfigurePageController.bolkey = false;
		}
		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {Myapp.virtualStage = null;
					if (Myapp.virtualStage == null) {

						Parent root2;
						root2 = FXMLLoader.load(getClass().getResource(
								"/application/keyboard.fxml"));
						Myapp.virtualStage = new Stage();
						Myapp.virtualStage.setTitle("Keyboard");
						Myapp.virtualStage.initStyle(StageStyle.UTILITY);
						Myapp.virtualStage.initOwner(MyDialoug.dialog);
						Myapp.virtualStage.initModality(Modality.WINDOW_MODAL);
						Myapp.virtualStage.setScene(new Scene(root2, 805, 350));
						
						Myapp.virtualStage.setOnShown(new EventHandler<WindowEvent>() {
							
							@Override
							public void handle(WindowEvent event) {
								// TODO Auto-generated method stub
								Myapp.keyboardinput
								.setText(Myapp.currTf
										.getText());
							}
						});

						Myapp.virtualStage
								.setOnCloseRequest(new EventHandler<WindowEvent>() {

									@Override
									public void handle(WindowEvent arg0) {

										Myapp.currTf
												.setText(Myapp.keyboardinput
														.getText());
									}
								});

					}
					else
					{
						Myapp.virtualStage.initOwner(MyDialoug.dialog);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	public static void setVitual(TextField tf, TextField nextTf, String lbl,
			String nextlbl) {
		try {
			// root2 =
			// FXMLLoader.load(getClass().getClassLoader().getResource("/application/keyboard.fxml"));
			Myapp.currTf = tf;
			Myapp.nextTf = nextTf;
			Myapp.nextlbl=nextlbl;
			Myapp.lblkeyboard.setText(lbl);
			Myapp.virtualStage.show();

			// Hide this current window (if this is what you want)
			// ((Node)(event.getSource())).getScene().getWindow().hide();

			// Set position of second window, related to primary window.
			// stage1.setX(primaryStage.getX() + 200);
			// stage1.setY(primaryStage.getY() + 100);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}


	void setClickkeyboard() {
		
	
		psid.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(psid, null, "Sample ID", null);

			}
		});
		
		lotno.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(lotno, null, "Lot Number", null);

			}
		});
		
			lotno.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(lotno, null, "Lot No.", null);
			}
		});
	}

	
}
