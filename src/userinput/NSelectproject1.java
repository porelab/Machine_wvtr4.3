package userinput;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import toast.MyDialoug;
import toast.Openscreen;
import toast.Systemtime;
import toast.Toast;
import ConfigurationPart.NConfigurePageController;
import application.Database;
import application.Main;
import application.Myapp;

import com.google.firebase.database.core.Platform;
import com.jfoenix.controls.JFXSlider;

import extrafont.Myfont;

public class NSelectproject1 implements Initializable {

	@FXML
	AnchorPane root, ancaddfluid;

	@FXML
	private ProgressBar prgmain;

	@FXML
	private TabPane maintab;

	@FXML
	private ImageView imgdownarrow, imgback, imgdownarrow1, imgdownarrow2,
			imgdownarrow3, imgdownarrow4, imgheader, keyboard;

	@FXML
	private ComboBox<String> cmboproject, cmbtestt, cmbselectind, cmbmateriala,
			cmbselectflui;

	@FXML
	private Button btnnext, start, btnaddfluid, btnfluiddelete;

	@FXML
	private Button btnback, btnmainback, btndelete;

	@FXML
	private ToggleButton ms1_1, ms1_2, ms1_3, ms1_4, ms1_5, ms2_1, ms2_2,
			ms2_3, ms2_4, ms2_5, ms2_6, ms2_7, tc_13, tc_12, tc_11, tc_21,
			tc_22, tc_23;

	@FXML
	private ImageView tfact;

	@FXML
	private JFXSlider slaccuracy;

	@FXML
	private Button btnsub, btnplus;

	@FXML
	RadioButton rbhybic, rbhylic, rbunknown, bwd, dbw, bdw, datas1, datas2,
			datas3;

	@FXML
	private JFXSlider bpind;

	@FXML
	private JFXSlider poroind, curvefit;

	static String selectedrad4 = "", selectedrad6 = "", selectedrad7 = "";

	static ToggleGroup tgb5, tgb6, tgb7;

	@FXML
	private TextField txttfvalue, txtsampleid, txtthiknes, txtendpre, txtlotno,
			txtstartpre, txtnfname, txtnfvalue;

	public static TextField txtsampleid1;

	int val = 0;

	@FXML
	Label lblot, lblproject, lblsampleid, lblindtype, lblmaterialapp,
			lblmaterialtype, lbltfacts, samname21, lblenterrange, lblthikness,
			lblfluid, lblbpt, lblporo, lblendpress;

	@FXML
	Label lbltimedate;

	Myfont f = new Myfont(22);

	Image image;

	boolean isproject = false;

	ToggleGroup m1group = new ToggleGroup();
	ToggleGroup m2group = new ToggleGroup();

	ToggleGroup t1group = new ToggleGroup();
	ToggleGroup t2group = new ToggleGroup();

	MyDialoug mm;

	@FXML
	Label lblsample, lblmaterial, lbltestcon;

	@FXML
	Circle c1, c2, c3;

	@FXML
	ImageView ic2, ic1, ic3;

	@FXML
	Button btnkeyboard;

	public static Button btnkeyboard1;

	void addShortCut() {

		KeyCombination backevent = new KeyCodeCombination(KeyCode.B,
				KeyCombination.CONTROL_ANY);
		KeyCombination startevent = new KeyCodeCombination(KeyCode.S,
				KeyCombination.CONTROL_ANY);
		KeyCombination nextevent = new KeyCodeCombination(KeyCode.RIGHT,
				KeyCombination.CONTROL_ANY);
		KeyCombination preevent = new KeyCodeCombination(KeyCode.LEFT,
				KeyCombination.CONTROL_ANY);

		root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {

				if (backevent.match(ke)) {

					Openscreen.open("/application/first.fxml");
				} else if (startevent.match(ke)) {

					if (cmboproject.getSelectionModel().getSelectedIndex() > 0) {

						Toast.makeText(Main.mainstage, "Start Test...", 1000,
								200, 200);
					} else {
						Toast.makeText(Main.mainstage, "Please select Project",
								1000, 200, 200);

					}

				} else if (nextevent.match(ke)) {
					// System.out.println("right click");
					nextClick();
				} else if (preevent.match(ke)) {

					// System.out.println("back click");
					backClick();
				}

			}
		});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		curvefit.setMin(10);
		poroind.setMin(10);
		bpind.setMin(10);

		bpind.setValue(100);
		poroind.setValue(50);
		curvefit.setValue(50);

		txtsampleid1 = txtsampleid;

		setkeyboardmode();

		if (NConfigurePageController.bolkey) {
			btnkeyboard.setVisible(false);
			setClickkeyboard();

		} else {
			btnkeyboard.setVisible(false);

		}

		btnkeyboard1 = btnkeyboard;
		txttfvalue.setText("0.1");
		txtsampleid.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				// TODO Auto-generated method stub
				samname21.setText(newValue + " ?");

			}
		});

		btnkeyboard.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				Parent root2;
				try {
					// root2 =
					// FXMLLoader.load(getClass().getClassLoader().getResource("/application/keyboard.fxml"));

					root2 = FXMLLoader.load(getClass().getResource(
							"/application/keyboard.fxml"));
					System.out.println("PATH" + root2);
					Stage stage1 = new Stage();
					stage1.setTitle("Keyboard");
					stage1.initOwner(Main.mainstage);
					stage1.initModality(Modality.WINDOW_MODAL);
					stage1.setScene(new Scene(root2, 500, 200));
					stage1.show();
					// Hide this current window (if this is what you want)
					// ((Node)(event.getSource())).getScene().getWindow().hide();

					// Set position of second window, related to primary window.
					// stage1.setX(primaryStage.getX() + 200);
					// stage1.setY(primaryStage.getY() + 100);

				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Cath");
				}
			}
		});

		lbltimedate.textProperty().bind(Systemtime.currenttime);
		addShortCut();
		maintab.setFocusTraversable(false);
		cmboproject.requestFocus();
		setMainbtns();
		setAllThings();
		setTestConfiguration2();
		setLabels();
		fontload();
		setNextanBackBtn();

		setMaterialSpecification1();
		setMaterialSpecification2();
		setMaterialType();
		setTestSequence();
		setDataStability();
		setTfactor();
		setTestConfiguration1();
		

		LoadProject();
		cmboproject.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				// TODO Auto-generated method stub

				try {
					if (newValue.equals("New Project")) {
						isproject = false;

						txtsampleid.setText("");
						cmbtestt.setValue("1");
						txtthiknes.setText("");
						txtlotno.setText("");
						btndelete.setVisible(false);

					} else {
						btndelete.setVisible(true);
						isproject = true;
						LoadProjectData(newValue);
					}
				} catch (Exception e) {
					txtsampleid.setText("");
					cmbtestt.setValue("1");
					txtthiknes.setText("");
					txtlotno.setText("");
					btndelete.setVisible(false);
				}
			}
		});

		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mm = new MyDialoug(Main.mainstage,
						"/userinput/saveprojectpopup.fxml");

			}
		});

		start.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (!txtendpre.getText().trim().equals("")
						&& !txtstartpre.getText().trim().equals("")) {

					try {
						int re = Integer.parseInt(txtstartpre.getText());
						int re1 = Integer.parseInt(txtendpre.getText());

						Myapp.sampleid = "" + txtsampleid.getText();
						Myapp.testtrial = "" + cmbtestt.getValue();
						Myapp.indtype = "" + cmbselectind.getValue();
						Myapp.materialapp = "" + cmbmateriala.getValue();
						String[] s = cmbselectflui.getValue().toString()
								.split(":");
						Myapp.fluidname = "" + s[0];
						Myapp.fluidvalue = "" + s[1];
						Myapp.thikness = "" + txtthiknes.getText();
						Myapp.endpress = "" + txtendpre.getText();
						Myapp.tfactore = "" + txttfvalue.getText();
						Myapp.lotnumber = "" + txtlotno.getText();
						Myapp.startpress = "" + txtstartpre.getText();

						startClick();
					} catch (Exception es) {
						Toast.makeText(Main.mainstage,
								"Please enter valid input.", 1500, 500, 500);
					}

				} else {
					Toast.makeText(Main.mainstage, "Please enter all details",
							1500, 500, 500);
				}

			}
		});

	}

	/* set image in button */
	void setMainbtns() {
		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/tfact-6.png"));
		tfact.setImage(image);

		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/back.png"));
		imgback.setImage(image);

		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/keyboard.png"));
		keyboard.setImage(image);

		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/downarrow.png"));
		imgdownarrow.setImage(image);

		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/downarrow.png"));
		imgdownarrow1.setImage(image);

		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/downarrow.png"));
		imgdownarrow2.setImage(image);

		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/downarrow.png"));
		imgdownarrow3.setImage(image);

		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/downarrow.png"));
		imgdownarrow4.setImage(image);

		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/header.png"));
		imgheader.setImage(image);

		btnaddfluid.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				setNewfluiddata();

			}
		});

		btnfluiddelete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Deletefluid();
			}
		});

		btnmainback.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				Openscreen.open("/application/first.fxml");

			}
		});

		btndelete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				setDeleteProject();
			}
		});

	}

	void setAllThings() {

		slaccuracy.setValue(0f);

		bpind.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {

				double v = (double) newValue;

				Myapp.accbpt = "" + (int) v;

			}

		});

		poroind.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {

				double v = (double) newValue;

				Myapp.accstep = "" + (int) v;

			}

		});

		curvefit.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {

				double v = (double) newValue;

				Myapp.accstability = "" + (int) v;
			}
		});

		cmbselectind.setItems(FXCollections.observableArrayList("Biotech",
				"Textile", "Non-woven & Paper", "Filtration", "Pharma",
				"Polymer", "Battery", "Packaging", "Enviroment & Hygene",
				"Chemical", "Ceramics", "Acoustics", "Other"));

		cmbmateriala.setItems(FXCollections.observableArrayList("Fibrous",
				"Particulate", "Consolidated", "Coated", "laminated"));

		cmbtestt.setItems(FXCollections.observableArrayList("1", "2", "3", "4",
				"5", "6", "7", "8", "9", "10"));
		cmbtestt.getSelectionModel().select(0);

		cmbselectflui.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				// TODO Auto-generated method stub

				try {
					if (newValue.equals("Add Fluid")) {
						ancaddfluid.setVisible(true);

					} else {

						ancaddfluid.setVisible(false);

						String deld = "select flag from fluiddata where name='"
								+ newValue.substring(0, newValue.indexOf(":"))
								+ "'";

						Database d = new Database();
						List<List<String>> data = d.getData(deld);

						System.out.println("Data : " + data + " for "
								+ newValue);
						if (data.get(0).get(0).equals("0")) {
							btnfluiddelete.setVisible(true);
						} else {
							btnfluiddelete.setVisible(false);

						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					ancaddfluid.setVisible(false);
				}
			}
		});

		getFluiddata();

		cmbselectind.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if (newValue.equals("Biotech")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"Tissue", "Bandage", "Implant", "Body Part",
							"Filter", "Skin"));

				} else if (newValue.equals("Other")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.setItems(FXCollections
							.observableArrayList("Other"));

				} else if (newValue.equals("Textile")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.getSelectionModel().clearSelection();
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"Geotextiles", "Bullet Proof Vests", "Space suits",
							"Automotive", "Medical", "Nanotechnology",
							"Construction", "Agrotextiles", "Packaging",
							"Sports"));

				} else if (newValue.equals("Non-woven & Paper")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.getSelectionModel().clearSelection();
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"Aeronautical", "Automotive", "Construction",
							"Filtration", "Medical", "Agriculture",
							"Home Furnishing", "Clothing", "Geo textiles",
							"Coated Paper", "Packaging", "Felt"));
				} else if (newValue.equals("Filtration")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.getSelectionModel().clearSelection();
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"Felts", "Wire Mesh", "Textiles", "Perforated",
							"water filter", "air filter", "oil filter",
							"Minipleat HEPA Filters", "Rigidpack",
							"Multiwedge", "Grease Filter", "Carbon Panel",
							"Eco Bag Filter", "Carbon CF Filter", "Cartridge",
							"Strainers", "Hydraulic Filters"));
				} else if (newValue.equals("Pharma")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.getSelectionModel().clearSelection();
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"Powdered Drugs", "Packaging materials", "Pouches"));
				} else if (newValue.equals("Polymer")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.getSelectionModel().clearSelection();
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"ABS (Acrylonitrile-butadiene-styrene)",
							"Acrylics(poly-methyl-methacrylate)",
							"Fluorocarbons(PTFE or TFE)", "Polycarbonates",
							"Polyethylene", "Polypropylene", "Polystyrene",
							"Polyester", "Epoxies", "Phenolics"));
				} else if (newValue.equals("Battery")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.setItems(FXCollections
							.observableArrayList("battery separators"));
				} else if (newValue.equals("Packaging")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"liquid", "solid", "moisture"));

				} else if (newValue.equals("Enviroment & Hygene")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"Diapers", "Tissues", "Paper towel", "Sheets",
							"Cosmetics"));

					// System.out.println("select text");
				} else if (newValue.equals("Chemical")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"Zeolites", "Catalysts", "Carbon black",
							"Abrasives", "Fertilizers", "Metal powders"));

					// System.out.println("select text");
				} else if (newValue.equals("Ceramics")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.setItems(FXCollections.observableArrayList(
							"Automotive", "Electronics", "Filtration",
							"Aerospace", "Composites", "Coatings", "Implants",
							"Enviromental", "Chemical"));

				} else if (newValue.equals("Acoustics")) {
					cmbmateriala.setVisible(true);
					cmbmateriala.setItems(FXCollections
							.observableArrayList("Foams", "Barriers",
									"Composites", "Diffusers",
									"Ceiling Baffles", "Fiber Glass",
									"Blankets", "Wall Panels", "Automotive",
									"Aircraft", "Building"));

				}
				cmbmateriala.getSelectionModel().select(0);
			}
		});
	}

	/* Get Fluid in Databse */
	void getFluiddata() {
		Database d = new Database();
		List<List<String>> info = d.getData("select * from fluiddata");

		try {

			cmbselectflui.getItems().add("Add Fluid");
			for (int i = 0; i < info.size(); i++) {
				cmbselectflui.getItems().add(
						info.get(i).get(0) + ":" + info.get(i).get(1));

			}
		} catch (Exception e) {
			Exception ss;
		}

	}

	/* Add new Fluid ame and value */
	void setNewfluiddata() {

		Database d1 = new Database();

		if (d1.Insert("INSERT INTO fluiddata VALUES('" + txtnfname.getText()
				+ "','" + txtnfvalue.getText() + "','0')")) {
			cmbselectflui.getItems().add(
					txtnfname.getText() + ":" + txtnfvalue.getText());
			cmbselectflui.getSelectionModel().select(
					txtnfname.getText() + ":" + txtnfvalue.getText());

			Toast.makeText(Main.mainstage,
					"Successfully New Fluid Data Added.", 1500, 500, 500);
		} else {
		}

	}

	/* Fluid delete */
	void Deletefluid() {
		String ss = cmbselectflui.getSelectionModel().getSelectedItem();
		String del = "delete from fluiddata where name='"
				+ ss.substring(0, ss.indexOf(":")) + "'";
		Database database = new Database();
		if (database.Insert(del)) {
			cmbselectflui.getItems().remove(ss);
		} else {

		}
	}

	/* set css class in Label */
	void setLabels() {
		lblproject.setFont(f.getM_M());
		lblsampleid.setFont(f.getM_M());
		lblot.setFont(f.getM_M());
		lblindtype.setFont(f.getM_M());
		lblmaterialtype.setFont(f.getM_M());
		lblmaterialapp.setFont(f.getM_M());
		lbltfacts.setFont(f.getM_M());
		samname21.setFont(f.getM_M());
		lblenterrange.setFont(f.getM_M());
		txttfvalue.setFont(f.getM_M());
		lblthikness.setFont(f.getM_M());
		lblfluid.setFont(f.getM_M());
		// lblporo.setFont(f.getM_M());
		lblfluid.setFont(f.getM_M());
		rbhylic.setFont(f.getM_M());
		rbhybic.setFont(f.getM_M());
		rbunknown.setFont(f.getM_M());

		// start.setFont(f.getM_M());

	}

	void fontload() {

		Font.loadFont(
				NSelectproject1.class.getResource("Montserrat-SemiBold.ttf")
						.toExternalForm(), 25);
		Font.loadFont(NSelectproject1.class.getResource("Roboto-Regular.ttf")
				.toExternalForm(), 25);
		Font.loadFont(NSelectproject1.class
				.getResource("Montserrat-Medium.ttf").toExternalForm(), 25);
		Font.loadFont(NSelectproject1.class.getResource("BebasNeue.otf")
				.toExternalForm(), 25);

	}

	void setNextanBackBtn() {
		btnnext.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				nextClick();

			}
		});

		btnback.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				backClick();
			}
		});

	}

	/* Main Test Setup Next Button */
	void nextClick() {
		boolean isvalid = true;
		// TODO Auto-generated method stub
		int oldpos = maintab.getSelectionModel().getSelectedIndex();

		if ((oldpos + 1) == 1) {
			boolean bol = true;
			if (txtsampleid.getText().toString().contains(".")
					|| txtsampleid.getText().toString().contains("#")
					|| txtsampleid.getText().toString().contains("_")
					|| txtsampleid.getText().toString().contains("-")) {
				bol = false;
			}
			if (!txtsampleid.getText().trim().equals("")
					&& !txtlotno.getText().trim().equals("")
					&& cmbtestt.getSelectionModel().getSelectedItem() != null) {
				if (bol) {

					prgmain.setProgress(0.41);
				} else {
					isvalid = false;
					Toast.makeText(Main.mainstage,
							"Special characters not allowed in SampleID", 1500,
							500, 500);

				}
			} else {
				isvalid = false;
				Toast.makeText(Main.mainstage, "Please enter all details",
						1500, 500, 500);

			}

		} else if ((oldpos + 1) == 2) {
			if (cmbmateriala.getSelectionModel().getSelectedItem() != null
					&& cmbselectind.getSelectionModel().getSelectedItem() != null) {
				lblsample.setTextFill(Paint.valueOf("#0d3c88"));
				lblmaterial.setTextFill(Paint.valueOf("#0d3c88"));
				lbltestcon.setTextFill(Paint.valueOf("#727376"));

				c1.setVisible(false);
				ic1.setVisible(true);
				c2.setFill(Paint.valueOf("#0d3c88"));
				prgmain.setProgress(0.50);
			} else {
				isvalid = false;
				Toast.makeText(Main.mainstage, "Please enter all details",
						1500, 500, 500);

			}

		} else if ((oldpos + 1) == 3) {

			prgmain.setProgress(0.58);

		} else if ((oldpos + 1) == 4) {
			prgmain.setProgress(0.68);

		} else if ((oldpos + 1) == 5) {
			lblsample.setTextFill(Paint.valueOf("#0d3c88"));
			lblmaterial.setTextFill(Paint.valueOf("#0d3c88"));
			lbltestcon.setTextFill(Paint.valueOf("#0d3c88"));

			c1.setVisible(false);
			c2.setVisible(false);
			ic1.setVisible(true);
			ic2.setVisible(true);
			c3.setFill(Paint.valueOf("#0d3c88"));
			prgmain.setProgress(0.79);

		} else if ((oldpos + 1) == 6) {
			if (cmbselectflui.getSelectionModel().getSelectedItem() != null
					&& !txtthiknes.getText().trim().equals("")) {
				try {
					Float re = Float.parseFloat(txtthiknes.getText());

				} catch (Exception es) {
					isvalid = false;
					Toast.makeText(Main.mainstage, "Please enter valid input.",
							1500, 500, 500);
				}
				prgmain.setProgress(0.90);

			} else {
				isvalid = false;
				Toast.makeText(Main.mainstage, "Please enter all details",
						1500, 500, 500);
			}

		} else if ((oldpos + 1) == 7) {
			ic3.setVisible(true);
			prgmain.setProgress(1);

		}

		if (isvalid) {
			if (oldpos + 1 > 0) {
				btnback.setVisible(true);
			}

			if (oldpos == 6) {
				btnnext.setVisible(false);
				maintab.getSelectionModel().select(oldpos + 1);

			} else {

				btnnext.setVisible(true);
				maintab.getSelectionModel().select(oldpos + 1);

			}
		}
	}

	void setMaterialSpecification1() {

		m1group.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle oldToggle, Toggle newToggle) {
						if (newToggle == null)
							oldToggle.setSelected(true);
						// System.out.println(m1group.getSelectedToggle().getUserData());
						Myapp.classification = ""
								+ m1group.getSelectedToggle().getUserData();

					}
				});

		setToggleButtonProperty(ms1_1, "Fibrous", "/userinput/m1_1.png",
				m1group);
		setToggleButtonProperty(ms1_2, "Coated", "/userinput/m1_2.png", m1group);
		setToggleButtonProperty(ms1_3, "Consolidate", "/userinput/m1_3.png",
				m1group);
		setToggleButtonProperty(ms1_4, "Laminated", "/userinput/m1_4.png",
				m1group);
		setToggleButtonProperty(ms1_5, "Unknown", "/userinput/7.png", m1group);

		m1group.selectToggle(ms1_1);

	}

	void setMaterialSpecification2() {

		m2group.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle oldToggle, Toggle newToggle) {
						if (newToggle == null)
							oldToggle.setSelected(true);
						// System.out.println(m2group.getSelectedToggle()
						// .getUserData());

						Myapp.crossection = ""
								+ m2group.getSelectedToggle().getUserData();
					}

				});

		setToggleButtonProperty(ms2_1, "Triangular", "/userinput/1.png",
				m2group);
		setToggleButtonProperty(ms2_2, "Rectangular", "/userinput/2.png",
				m2group);
		setToggleButtonProperty(ms2_3, "Circular", "/userinput/3.png", m2group);
		setToggleButtonProperty(ms2_4, "Eliptical", "/userinput/4.png", m2group);
		setToggleButtonProperty(ms2_5, "Square", "/userinput/5.png", m2group);
		setToggleButtonProperty(ms2_6, "Slit", "/userinput/6.png", m2group);
		setToggleButtonProperty(ms2_7, "Unknown", "/userinput/7.png", m2group);

		m2group.selectToggle(ms2_1);
	}

	void setToggleButtonProperty(ToggleButton tb, String title, String img,
			ToggleGroup group) {
		Image image = new Image(this.getClass().getResourceAsStream(img));
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(150);
		imageView.setFitHeight(160);
		tb.setGraphic(imageView);
		tb.setContentDisplay(ContentDisplay.TOP);
		tb.setToggleGroup(group);
		tb.setUserData(title);
		tb.setText(title);

	}

	void backClick() {

		int oldpos = maintab.getSelectionModel().getSelectedIndex();
		if (oldpos - 1 < 7) {
			btnnext.setVisible(true);
		}

		if (oldpos == 0) {
			btnback.setVisible(false);

		} else {
			if (oldpos - 1 == 0) {
				btnback.setVisible(false);
			} else {
				btnback.setVisible(true);
			}
			maintab.getSelectionModel().select(oldpos - 1);

		}

		if ((oldpos - 1) == 0) {
			prgmain.setProgress(0.33);
			lblsample.setTextFill(Paint.valueOf("#0d3c88"));
			lblmaterial.setTextFill(Paint.valueOf("#727376"));
			lbltestcon.setTextFill(Paint.valueOf("#727376"));

		} else if ((oldpos - 1) == 1) {

			prgmain.setProgress(0.41);

		} else if ((oldpos - 1) == 2) {
			prgmain.setProgress(0.50);

		} else if ((oldpos - 1) == 3) {
			prgmain.setProgress(0.58);
			lblsample.setTextFill(Paint.valueOf("#727376"));
			lblmaterial.setTextFill(Paint.valueOf("#0d3c88"));
			lbltestcon.setTextFill(Paint.valueOf("#727376"));

		} else if ((oldpos - 1) == 4) {
			prgmain.setProgress(0.68);

		} else if ((oldpos - 1) == 5) {
			prgmain.setProgress(0.79);

		} else if ((oldpos - 1) == 6) {
			prgmain.setProgress(0.90);

		} else if ((oldpos - 1) == 7) {
			prgmain.setProgress(1);

		}

	}

	/* tortuosity set */
	void setTfactor() {
		btnplus.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				if (val != 100) {
					val = val + 20;
					txttfvalue.setText("" + ((double) val / 100));

					slaccuracy.setValue(val);
					System.out.println("valadd" + slaccuracy.valueProperty());

				}

			}
		});

		btnsub.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (val != 0) {
					val = val - 20;
					slaccuracy.setValue(val);

					txttfvalue.setText("" + ((double) val / 100));

				}
				if (slaccuracy.getValue() == 0) {
					txttfvalue.setText("0.1");
				}
			}
		});

		slaccuracy.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				Myapp.tfactore = "" + (double) newValue / 100;

				if (newValue.equals(0.0)) {

					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/tfact-6.png"));
					tfact.setImage(image);

				}

				else if (newValue.equals(20.0)) {

					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/tfact-5.png"));
					tfact.setImage(image);

				} else if (newValue.equals(40.0)) {

					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/tfact-4.png"));
					tfact.setImage(image);

				} else if (newValue.equals(60.0)) {

					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/tfact-3.png"));
					tfact.setImage(image);

				} else if (newValue.equals(80.0)) {

					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/tfact-2.png"));
					tfact.setImage(image);

				} else {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/tfact-1.png"));
					tfact.setImage(image);

				}

			}

		});

		setTextwatchr_tfactor();

	}

	void setTextwatchr_tfactor() {

		Pattern validEditingState = Pattern
				.compile("((0?(\\.[0-9]*)?)|(1?(\\.0)?))");

		UnaryOperator<TextFormatter.Change> filter = c -> {
			String text = c.getControlNewText();
			if (validEditingState.matcher(text).matches()) {
				return c;
			} else {
				return null;
			}
		};

		StringConverter<Double> converter = new StringConverter<Double>() {

			@Override
			public Double fromString(String s) {
				if (s.isEmpty() || "-".equals(s) || ".".equals(s)
						|| "-.".equals(s)) {
					return 0.0;
				} else {
					return Double.valueOf(s);
				}
			}

			@Override
			public String toString(Double d) {
				return d.toString();
			}
		};

		TextFormatter<Double> textFormatter = new TextFormatter<>(converter,
				0.1, filter);

		txttfvalue.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				System.out.println(txttfvalue.getText());
				try {
					double d = Double.parseDouble(txttfvalue.getText());

					double dtemp = (double) d * 100 / 20;
					System.out.println("answer:" + dtemp);
					int x1 = (int) dtemp;
					int x = (int) Math.round(dtemp);
					System.out.println("value  :" + x1 + " - roundoff: " + x);
					if (x == x1) {
						slaccuracy.setValue(20 * x);
					} else {
						slaccuracy.setValue((x1 * 20) + 20);
					}
					// slaccuracy.setValue(x*10);

				} catch (Exception e) {
					slaccuracy.setValue(0);
					System.out.println("Error");
				}
			}
		});
		txttfvalue.setTextFormatter(textFormatter);

	}

	/* Tortuosity Value Set */
	void setDefaultValue() {
		try {

			double d = Double.parseDouble(Myapp.tfactore);

			double dtemp = (double) d * 100 / 20;
			System.out.println("answer:" + dtemp);
			int x1 = (int) dtemp;
			int x = (int) Math.round(dtemp);
			System.out.println("value  :" + x1 + " - roundoff: " + x);
			if (x == x1) {
				slaccuracy.setValue(20 * x);
			} else {
				slaccuracy.setValue((x1 * 20) + 20);
			}
			// slaccuracy.setValue(x*10);

		} catch (Exception e) {
			e.printStackTrace();
			slaccuracy.setValue(0);
			System.out.println("Error");
		}
	}

	/* Sample Plate Selection */
	void setTestConfiguration1() {

		t1group.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle oldToggle, Toggle newToggle) {
						if (newToggle == null)
							oldToggle.setSelected(true);
						System.out.println(t1group.getSelectedToggle()
								.getUserData());
						Myapp.splate = ""
								+ t1group.getSelectedToggle().getUserData();

					}
				});

		setToggleButtonProperty(tc_11, "Small", "/userinput/small.png", t1group);
		setToggleButtonProperty(tc_12, "Medium", "/userinput/medium.png",
				t1group);
		setToggleButtonProperty(tc_13, "Large", "/userinput/large.png", t1group);
		t1group.selectToggle(tc_11);

	}

	void setTestConfiguration2() {

		t2group.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle oldToggle, Toggle newToggle) {
						if (newToggle == null)
							oldToggle.setSelected(true);
						System.out.println(t2group.getSelectedToggle()
								.getUserData());
						Myapp.thresold = ""
								+ t2group.getSelectedToggle().getUserData();

					}
				});

		setToggleButtonProperty(tc_21, "First Bubble",
				"/userinput/firstbubble.png", t2group);
		setToggleButtonProperty(tc_22, "Moderate",
				"/userinput/moderatebubble.png", t2group);
		setToggleButtonProperty(tc_23, "Continous",
				"/userinput/continousbubble.png", t2group);
		t2group.selectToggle(tc_21);

	}

	/* Material Type Selection */
	void setMaterialType() {

		tgb5 = new ToggleGroup();

		rbhybic.setToggleGroup(tgb5);
		rbhybic.setUserData("1");
		rbhylic.setToggleGroup(tgb5);
		rbhylic.setUserData("2");
		rbunknown.setToggleGroup(tgb5);
		rbunknown.setUserData("3");

		selectedrad4 = "1";
		Myapp.materialtype = "Hydrophobic";

		tgb5.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0,
					Toggle arg1, Toggle arg2) {
				if (arg2 == null)
					arg1.setSelected(true);
				selectedrad4 = arg2.getUserData().toString();

				if (selectedrad4.equals("1")) {
					Myapp.materialtype = "Hydrophobic";
				}

				else if (selectedrad4.equals("2")) {
					Myapp.materialtype = "Hydrophilic";

				}

				else {
					Myapp.materialtype = "Unknown";
				}

			}

		}

		);

	}

	/* Select Test Type. */
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
				if (arg2 == null)
					arg1.setSelected(true);
				selectedrad6 = arg2.getUserData().toString();

				System.out.println("SElect ted record................"
						+ selectedrad6);

				if (selectedrad6.equals("1")) {
					Myapp.testsequence = "WUPDUP";
				}

				else if (selectedrad6.equals("2")) {
					Myapp.testsequence = "DUPWUP";
				} else {
					Myapp.testsequence = "WUPDCALCULATED";
				}
			}
		});

	}

	/* Data Stability Selection */
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

	/* Load All Saved Project List */
	void LoadProject() {

		Database d = new Database();
		cmboproject.getItems().clear();
		List<List<String>> info = d
				.getData("select project from projects where useremailid='"
						+ Myapp.email + "'");
		try {
			cmboproject.getItems().add("New Project");

			for (int i = 0; i < info.size(); i++) {
				cmboproject.getItems().add(info.get(i).get(0));

			}
		} catch (Exception e) {
			Exception ss;
		}

	}

	/* Selected Project Id to Load Test Setup Data in database */
	void LoadProjectData(String projectname) {
		Database d = new Database();
		isproject = true;
		List<List<String>> alldata = d
				.getData("select * from projects where useremailid='"
						+ Myapp.email + "' and project='" + projectname + "' ");

		// System.out.println("alalalalal"+alldata);
		txtsampleid.setText(alldata.get(0).get(0));
		cmbtestt.setValue(alldata.get(0).get(9));

		// System.out.println("Trial dkdkkdkd"+alldata.get(0).get(9));

		cmbselectind.setValue(alldata.get(0).get(1));
		cmbmateriala.setValue(alldata.get(0).get(2));
		cmbselectflui.setValue(alldata.get(0).get(6));

		// slaccuracy.setValue(Double.parseDouble(alldata.get(0).get(10)));
		txttfvalue.setText(alldata.get(0).get(10));
		Myapp.tfactore = "" + Double.parseDouble(alldata.get(0).get(10));
		val = (int) (Double.parseDouble(Myapp.tfactore) * 100);
		// System.out.println("Tfactor "+Myapp.tfactore);

		setDefaultValue();
		txtthiknes.setText(alldata.get(0).get(12));
		txtendpre.setText(alldata.get(0).get(14));
		bpind.setValue(Double.parseDouble(alldata.get(0).get(15)));
		poroind.setValue(Double.parseDouble(alldata.get(0).get(16)));
		curvefit.setValue(Double.parseDouble(alldata.get(0).get(21)));
		txtlotno.setText(alldata.get(0).get(17));
		txtstartpre.setText(alldata.get(0).get(18));

		String classificati = "" + alldata.get(0).get(3);

		// System.out.println("Claidifcation"+alldata.get(0).get(3));
		String crossection = "" + alldata.get(0).get(4);
		String thresold = "" + alldata.get(0).get(5);
		String splate = "" + alldata.get(0).get(11);
		String materialtype = "" + alldata.get(0).get(13);
		String testsequance = "" + alldata.get(0).get(19);
		String stabilitytype = "" + alldata.get(0).get(20);

		System.out.println("BP Thresol--->"+thresold);
		// Material Specification

		if (classificati.equals("Fibrous")) {
			m1group.selectToggle(ms1_1);
			Myapp.classification = "Fibrous";

		}

		else if (classificati.equals("Coated")) {
			m1group.selectToggle(ms1_2);
			Myapp.classification = "Coated";

		} else if (classificati.equals("Particulate")) {
			m1group.selectToggle(ms1_3);
			Myapp.classification = "Particulate";

		}

		else if (classificati.equals("Laminated")) {
			m1group.selectToggle(ms1_4);
			Myapp.classification = "Laminated";

		} else {
			m1group.selectToggle(ms1_5);
			Myapp.classification = "Unknown";

		}

		/* Cross section selection */

		if (crossection.equals("Triangular")) {

			m2group.selectToggle(ms2_1);
			Myapp.crossection = "Triangular";
		}

		else if (crossection.equals("Rectangular")) {
			m2group.selectToggle(ms2_2);
			Myapp.crossection = "Rectangular";

		} else if (crossection.equals("Circular")) {
			m2group.selectToggle(ms2_3);
			Myapp.crossection = "Circular";
		}

		else if (crossection.equals("Eliptical")) {

			m2group.selectToggle(ms2_4);
			Myapp.crossection = "Eliptical";
		}

		else if (crossection.equals("Square")) {
			m2group.selectToggle(ms2_5);
			Myapp.crossection = "Square";
		}

		else {

			m2group.selectToggle(ms2_6);
			Myapp.crossection = "Unknown";
		}

		/* Thresold */

		System.out.println("First Bu------------>"+thresold);
		if (thresold.equals("First Bubble")) {
			t2group.selectToggle(tc_11);
			Myapp.thresold = "First Bubble";
			System.out.println("1----First Bu------------>"+thresold);
		} else if (thresold.equals("Moderate")) {
			t2group.selectToggle(tc_12);
			Myapp.thresold = "Moderate";
			System.out.println("2----First Bu------------>"+thresold);
		} else if (thresold.equals("Continous")) {
			t2group.selectToggle(tc_13);
			Myapp.thresold = "Continous";
			System.out.println("3----First Bu------------>"+thresold);
		}

		// Material tyep hydro

		if (materialtype.equals("Hydrophobic")) {
			rbhybic.selectedProperty().set(true);
			Myapp.materialtype = "Hydrophobic";

		}

		else if (materialtype.equals("Hydrophilic")) {
			rbhylic.selectedProperty().set(true);
			Myapp.materialtype = "Hydrophilic";

		}

		else {
			rbunknown.selectedProperty().set(true);
			Myapp.materialtype = "Unknown";

		}

		/* splate */
		if (splate.equals("small")) {
			t1group.selectToggle(tc_21);
			Myapp.splate = "small";
		} else if (splate.equals("medium")) {
			t1group.selectToggle(tc_22);
			Myapp.splate = "medium";

		} else if (splate.equals("large")) {
			t1group.selectToggle(tc_23);
			Myapp.thresold = "large";

		}
		// Test Sequance

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

		}

		else if (stabilitytype.equals("2")) {
			datas2.selectedProperty().set(true);
			Myapp.stabilitytype = "2";

		}

		else {
			datas3.selectedProperty().set(true);
			Myapp.stabilitytype = "3";

		}

	}

	/* Check Selected project or Not */
	void startClick() {

		System.out.println("is : " + isproject);
		lastproject();
		// TODO Auto-generated method stub
		if (!isproject) {

			mm.showDialoug();

		} else {
			setupdatedProjectData();
			Openscreen.open("/userinput/Nlivetest.fxml");

		}

	}

	/* Selected Project in update data */
	void setupdatedProjectData() {
		Database d1 = new Database();

		String updatequry = "update projects set sampleid='" + Myapp.sampleid
				+ "',indtype='" + Myapp.indtype + "',materialtype='"
				+ Myapp.materialapp + "',clasification='"
				+ Myapp.classification + "',crosssection='" + Myapp.crossection
				+ "',thresold='" + Myapp.thresold + "',fluid='"
				+ cmbselectflui.getValue() + "',tfactor='" + Myapp.tfactore
				+ "',testtrial='" + Myapp.testtrial + "',splate='"
				+ Myapp.splate + "',thikness='" + txtthiknes.getText()
				+ "',materialtyephydro='" + Myapp.materialtype + "',endpress='"
				+ txtendpre.getText() + "',accbpt='" + Myapp.accbpt
				+ "',accporo='" + Myapp.accstep + "',lotno='" + Myapp.lotnumber
				+ "',startpress='" + Myapp.startpress + "',testsequence='"
				+ Myapp.testsequence + "',stabilitytype='"
				+ Myapp.stabilitytype + "',accstability='" + Myapp.accstability
				+ "'  where sampleid='" + Myapp.sampleid + "'";

		if (d1.Insert(updatequry)) {
			// System.out.println(" Updated Projec Data");
		} else {

			// System.out.println("Not Updated");
		}
	}

	/* Load Last test Data */
	void lastproject() {
		Database d1 = new Database();
		try {

			Myapp.sampleid = "" + txtsampleid.getText();
			Myapp.testtrial = "" + cmbtestt.getValue();
			Myapp.indtype = "" + cmbselectind.getValue();
			Myapp.materialapp = "" + cmbmateriala.getValue();
			String[] s = cmbselectflui.getValue().toString().split(":");
			Myapp.fluidname = "" + s[0];
			Myapp.fluidvalue = "" + s[1];
			Myapp.thikness = "" + txtthiknes.getText();
			Myapp.endpress = "" + txtendpre.getText();
			Myapp.lotnumber = "" + txtlotno.getText();
			Myapp.startpress = "" + txtstartpre.getText();

			if (!d1.isExist("select * from lastprojects where lid='"
					+ Myapp.email + "'")) {

				if (d1.Insert("INSERT INTO lastprojects VALUES('" + Myapp.email
						+ "','" + Myapp.sampleid + "','" + Myapp.indtype
						+ "','" + Myapp.materialapp + "','"
						+ Myapp.classification + "','" + Myapp.crossection
						+ "','" + Myapp.thresold + "','"
						+ cmbselectflui.getValue() + "','" + Myapp.tfactore
						+ "','" + Myapp.testtrial + "','" + Myapp.username
						+ "','" + Myapp.splate + "','" + txtthiknes.getText()
						+ "','" + Myapp.materialtype + "','"
						+ txtendpre.getText() + "','" + Myapp.accbpt + "','"
						+ Myapp.accstep + "','" + Myapp.lotnumber + "','"
						+ Myapp.startpress + "', '" + Myapp.testsequence
						+ "', '" + Myapp.stabilitytype + "', '"
						+ Myapp.accstability + "')")) {
					// System.out.println("Last Project Insert data");
				} else {
					// System.out.println(" Last Project  not insertetd");
				}
			} else {

				String updatequry = "update lastprojects set sampleid='"
						+ Myapp.sampleid + "',indtype='" + Myapp.indtype
						+ "',materialtype='" + Myapp.materialapp
						+ "',clasification='" + Myapp.classification
						+ "',crosssection='" + Myapp.crossection
						+ "',thresold='" + Myapp.thresold + "',fluid='"
						+ cmbselectflui.getValue() + "',tfactor='"
						+ Myapp.tfactore + "',testtrial='" + Myapp.testtrial
						+ "',uname='" + Myapp.username + "',splate='"
						+ Myapp.splate + "',thikness='" + txtthiknes.getText()
						+ "',materialtyephydro='" + Myapp.materialtype
						+ "',endpress='" + txtendpre.getText() + "',accbpt='"
						+ Myapp.accbpt + "',accporo='" + Myapp.accstep
						+ "',lotno='" + Myapp.lotnumber + "',startpress='"
						+ Myapp.startpress + "',testsequence='"
						+ Myapp.testsequence + "',stabilitytype='"
						+ Myapp.stabilitytype + "',accstability='"
						+ Myapp.accstability + "'  where lid='" + Myapp.email
						+ "'";

				if (d1.Insert(updatequry)) {
					// System.out.println(" Updated");
				} else {

					// System.out.println("Not Updated");
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/* Delete Selected Project */
	void setDeleteProject() {

		Database d = new Database();
		String pname = cmboproject.getSelectionModel().getSelectedItem();

		String delete1 = "delete from projects where project='" + pname + "'";

		if (d.Insert(delete1)) {
			LoadProject();
			Toast.makeText(Main.mainstage, "Successfully deleted Project..",
					1000, 200, 200);

		} else {

			// System.out.println("Not Delete Project");
		}
	}

	/* Keyboard Mode Checking */
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

				try {
					if (Myapp.virtualStage == null) {

						Parent root2;
						root2 = FXMLLoader.load(getClass().getResource(
								"/application/keyboard.fxml"));
						Myapp.virtualStage = new Stage();
						Myapp.virtualStage.setTitle("Keyboard");
						Myapp.virtualStage.initStyle(StageStyle.UTILITY);
						Myapp.virtualStage.initOwner(Main.mainstage);
						Myapp.virtualStage.initModality(Modality.WINDOW_MODAL);
						Myapp.virtualStage.setScene(new Scene(root2, 805, 350));

						Myapp.virtualStage
								.setOnShown(new EventHandler<WindowEvent>() {

									@Override
									public void handle(WindowEvent event) {
										// TODO Auto-generated method stub
										Myapp.keyboardinput
												.setText(Myapp.currTf.getText());
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
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	/* Open Keyboard Scren stage. */
	public static void setVitual(TextField tf, TextField nextTf, String lbl,
			String nextlbl) {
		try {
			// root2 =
			// FXMLLoader.load(getClass().getClassLoader().getResource("/application/keyboard.fxml"));
			Myapp.currTf = tf;
			Myapp.nextTf = nextTf;
			Myapp.nextlbl = nextlbl;
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

	/* Virtual Keyboard Textbox Click Event. */
	void setClickkeyboard() {
		txtsampleid.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(txtsampleid, txtlotno, "Sample Id.", "Lot No .");

			}
		});

		txtlotno.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(txtlotno, null, "Lot No.", null);
			}
		});

		txtthiknes.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(txtthiknes, null, "Enter Thikness", null);

			}
		});

		txtstartpre.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(txtstartpre, txtendpre, "Start Pressure",
						"End Pressure");

			}
		});

		txtendpre.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				setVitual(txtendpre, null, "End Pressure", null);
			}
		});

	}

}
