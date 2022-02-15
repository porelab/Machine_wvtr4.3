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

public class PopfxmlController implements Initializable {

	@FXML
	AnchorPane root1;
	
	int valbpt,valtestacc,valdatas;

	@FXML
	TextField pendp1, pendp2;

	@FXML
	Label  pendp,pthresold, pfluid;

	@FXML
	TextField psid,lotno,ptfact,pbptacc,txtdatastability,pporoacc;
	
	@FXML
	ComboBox<String> cmbtesttrial,cmbfluid,cmbbptthresold;
	
	@FXML
	Button starttest, btncancel;

	@FXML
	ComboBox<String> cmboproject;

	@FXML
	RadioButton bwd, dbw, bdw,datas1,datas2,datas3;

	static String selectedrad6 = "",selectedrad7 = "";

	static ToggleGroup tgb6,tgb7;

    @FXML
    private Button testaccbtnsub,testaccbtnplus,datasbtnsub,datasbtnplus,bptbtnsub,bptbtnplus;

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println("I am in Quick test");
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
				.getData("select * from projects where useremailid='"
						+ Myapp.email + "' and project='" + projectname + "' ");

		String classificati = "" + alldata.get(0).get(3);

		// System.out.println("Claidifcation"+alldata.get(0).get(3));
		String splate = "" + alldata.get(0).get(11);
	
		// Industry name //
		psid.setText(alldata.get(0).get(0));
		ptfact.setText(alldata.get(0).get(10));
		pendp1.setText(alldata.get(0).get(14));
		pbptacc.setText(alldata.get(0).get(15));
		
		
		pporoacc.setText(alldata.get(0).get(16));
		lotno.setText(alldata.get(0).get(17));
		pendp2.setText(alldata.get(0).get(18));
		
			/*New Chnages 01-07-2019*/
		
		cmbbptthresold.setValue(alldata.get(0).get(5));
		cmbfluid.setValue(alldata.get(0).get(6));
		cmbtesttrial.setValue(alldata.get(0).get(9));
		
		String accstability= (alldata.get(0).get(21));
		
		String stabilitytype = "" + alldata.get(0).get(20);
		Myapp.accstability = (accstability);

		// Stability Type

		if (stabilitytype.equals("1")) {
			datas1.selectedProperty().set(true);
			Myapp.stabilitytype = "1";
			txtdatastability.setText(accstability);

		}

		else if (stabilitytype.equals("2")) {
			datas2.selectedProperty().set(true);
			Myapp.stabilitytype = "2";
			txtdatastability.setText(accstability);
		}

		else {
			datas3.selectedProperty().set(true);
			Myapp.stabilitytype = "3";
			txtdatastability.setText(accstability);
		}


		Myapp.thresold = alldata.get(0).get(5);
		Myapp.sampleid = alldata.get(0).get(0);
		Myapp.testtrial = alldata.get(0).get(9);
		Myapp.tfactore = alldata.get(0).get(10);
		Myapp.indtype = alldata.get(0).get(1);
		Myapp.materialapp = alldata.get(0).get(2);

		String[] s = alldata.get(0).get(6).toString().split(":");
		Myapp.fluidname = "" + s[0];
		Myapp.fluidvalue = "" + s[1];
		Myapp.splate = splate;
		Myapp.thikness = alldata.get(0).get(12);
		Myapp.materialtype = alldata.get(0).get(13);
		Myapp.endpress = alldata.get(0).get(14);
		Myapp.accbpt = (alldata.get(0).get(15));
		Myapp.accstep= (alldata.get(0).get(16));
		Myapp.lotnumber = (alldata.get(0).get(17));
		Myapp.startpress = (alldata.get(0).get(18));
		
		Myapp.accstability = (alldata.get(0).get(21));

		valbpt = (int) (Double.parseDouble(Myapp.accbpt));
		valtestacc = (int) (Double.parseDouble(Myapp.accstep));
		valdatas = (int) (Double.parseDouble(Myapp.accstability));
		
		
		
		String testsequance = "" + alldata.get(0).get(19);
		Myapp.testsequence = testsequance;
//		System.out.println("Test Seu....................." + testsequance);

		if (testsequance.equals("WUPDUP")) {
			bwd.selectedProperty().set(true);
			Myapp.testsequence = "WUPDUP";

		}

		else if (testsequance.equals("DUPWUP")) {
			dbw.selectedProperty().set(true);
			Myapp.testsequence = "DUPWUP";

		}

		else {
			bdw.selectedProperty().set(true);
			Myapp.testsequence = "WUPDCALCULATED";

		}

	}

	void LoadProject() {

		Database d = new Database();
		List<List<String>> info = d
				.getData("select project from projects where useremailid='"
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
		cmbtesttrial.setItems(FXCollections.observableArrayList("1", "2", "3", "4",
				"5", "6", "7", "8", "9", "10"));
		
		cmbbptthresold.setItems(FXCollections.observableArrayList("First bubble","Moderate","Continous"));

		getFluiddata();
		
		setDataStability();
		
		setAccuracybtn();
		
	}
	
	void getFluiddata() {
		Database d = new Database();
		List<List<String>> info = d.getData("select * from fluiddata");

		try {

		//	cmbfluid.getItems().add("Add Fluid");
			for (int i = 0; i < info.size(); i++) {
				cmbfluid.getItems().add(
						info.get(i).get(0) + ":" + info.get(i).get(1));

			}
		} catch (Exception e) {
			Exception ss;
		}

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
		Myapp.stabilitytype = "1";

		tgb7.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0,
					Toggle arg1, Toggle arg2) {
				if (arg2 == null)
					arg1.setSelected(true);
				selectedrad7 = arg2.getUserData().toString();

				if (selectedrad7.equals("1")) {
					Myapp.stabilitytype = "1";
				}

				else if (selectedrad7.equals("2")) {
					Myapp.stabilitytype = "2";
				} else {
					Myapp.stabilitytype = "3";
				}
			}
		});

	}

	void setAccuracybtn() {
		bptbtnplus.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (valbpt != 100) {
			
					valbpt = valbpt + 5;
					pbptacc.setText("" + valbpt);
					Myapp.accbpt = ""+valbpt;
					

				}

			}
		});

		bptbtnsub.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (valbpt != 10) {
					valbpt = valbpt - 5;

					pbptacc.setText(""+valbpt);
					Myapp.accbpt = ""+valbpt;

				}
				
			}
		});

/*Test Acc*/
		testaccbtnplus.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (valtestacc != 100) {
			
					valtestacc = valtestacc + 10;
					pporoacc.setText("" + valtestacc);
					Myapp.accstep = ""+valtestacc;
					

				}

			}
		});

		testaccbtnsub.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (valtestacc != 10) {
					valtestacc = valtestacc - 10;

					pporoacc.setText(""+valtestacc);
					Myapp.accstep = ""+valtestacc;

				}
				
			}
		});

		/*Data stability*/
		datasbtnplus.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (valdatas != 100) {
			
					valdatas = valdatas + 10;
					txtdatastability.setText("" + valdatas);
					Myapp.accstability = ""+valdatas;
					

				}

			}
		});

		datasbtnsub.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (valdatas != 10) {
					valdatas = valdatas - 10;

					txtdatastability.setText(""+valdatas);
					Myapp.accstability = ""+valdatas;

				}
				
			}
		});

		
		
	}


	void setLastData() {
		
		
		
		Database d1 = new Database();
		List<List<String>> alldata = d1
				.getData("select * from lastprojects where lid='" + Myapp.email
						+ "' ");

		
		psid.setText(alldata.get(0).get(1));
		ptfact.setText(alldata.get(0).get(8));
		pendp1.setText(alldata.get(0).get(14));
		pbptacc.setText(alldata.get(0).get(15));
		pporoacc.setText(alldata.get(0).get(16));
		lotno.setText(alldata.get(0).get(17));
		pendp2.setText(alldata.get(0).get(18));
		
		
		/*New changes 01-07-2019*/
		
		cmbbptthresold.setValue(alldata.get(0).get(6));
		cmbfluid.setValue(alldata.get(0).get(7));
		cmbtesttrial.setValue(alldata.get(0).get(9));
		
		Myapp.thresold = alldata.get(0).get(6);
		Myapp.sampleid = alldata.get(0).get(1);
		Myapp.testtrial = (alldata.get(0).get(9));
		Myapp.tfactore = (alldata.get(0).get(8));
		Myapp.indtype = alldata.get(0).get(2);
		Myapp.materialapp = alldata.get(0).get(3);

		String[] s = alldata.get(0).get(7).toString().split(":");
		Myapp.fluidname = "" + s[0];
		Myapp.fluidvalue = "" + s[1];
		Myapp.splate = alldata.get(0).get(11);
		Myapp.thikness = alldata.get(0).get(12);
		Myapp.materialtype = alldata.get(0).get(13);
		Myapp.endpress = (alldata.get(0).get(14));
		Myapp.accbpt = (alldata.get(0).get(15));
		Myapp.accstep = (alldata.get(0).get(16));
		Myapp.lotnumber = (alldata.get(0).get(17));
		Myapp.startpress = (alldata.get(0).get(18));
		String testsequance = "" + alldata.get(0).get(19);

		Myapp.crossection = (alldata.get(0).get(5));
		


		String stabilitytype = "" + alldata.get(0).get(20);
		String accstability= (alldata.get(0).get(21));
		Myapp.accstability =""+ accstability;

		valbpt = (int) (Double.parseDouble(Myapp.accbpt));
		valtestacc = (int) (Double.parseDouble(Myapp.accstep));
		valdatas = (int) (Double.parseDouble(Myapp.accstability));
		
		
		if (testsequance.equals("WUPDUP")) {
			bwd.selectedProperty().set(true);
			Myapp.testsequence = "WUPDUP";

		}

		else if (testsequance.equals("DUPWUP")) {
			dbw.selectedProperty().set(true);
			Myapp.testsequence = "DUPWUP";

		}

		else {
			bdw.selectedProperty().set(true);
			Myapp.testsequence = "WUPDCALCULATED";

		}


		// Stability Type

				if (stabilitytype.equals("1")) {
					datas1.selectedProperty().set(true);
					Myapp.stabilitytype = "1";
					txtdatastability.setText(accstability);

				}

				else if (stabilitytype.equals("2")) {
					datas2.selectedProperty().set(true);
					Myapp.stabilitytype = "2";
					txtdatastability.setText(accstability);
				}

				else {
					datas3.selectedProperty().set(true);
					Myapp.stabilitytype = "3";
					txtdatastability.setText(accstability);
				}
		
		
	}

	void teststart() {

		
		Myapp.endpress = pendp1.getText();
		Myapp.lotnumber = lotno.getText();
		Myapp.startpress = pendp2.getText();
		 Myapp.accbpt = pbptacc.getText();
		 Myapp.accstep = pporoacc.getText();
		 Myapp.accstability = txtdatastability.getText();
		 Myapp.sampleid = psid.getText();
		 Myapp.thresold=cmbbptthresold.getSelectionModel().getSelectedItem();
			
		
		 
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
		
		ptfact.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(ptfact, null, "Tortuosity Factor", null);

			}
		});
		
		
		
		
		pendp2.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(pendp2, null, "Start Pressure", null);

			}
		});

		pendp1.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(pendp1, null,  "End Pressure",null);
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
