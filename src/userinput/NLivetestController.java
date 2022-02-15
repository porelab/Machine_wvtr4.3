package userinput;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.LineChart.SortingPolicy;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import toast.LoadAnchor;
import toast.MyDialoug;
import toast.Toast;
import userinput.NLivetestControllerold.SerialReader;
import Notification.Notification.Notifier;
import application.DataStore;
import application.Main;
import application.Myapp;
import application.SerialWriter;
import application.writeFormat;
import communicationProtocol.Mycommand;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;

import data_read_write.CalculatePorometerData;
import data_read_write.CsvWriter;
import de.tesis.dynaware.javafx.fancychart.zoom.Zoom;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.Dark;
import extrafont.Myfont;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class NLivetestController implements Initializable {

	static SimpleBooleanProperty isRestart;

	static File savefile;

	@FXML
	Label lblfilename, lblbpc;

	double stepsizepercentage = 0.2, maxpressureinstepsize = 1, mindelay = 200,
			maxdelay = 2000, minavg = 2, maxavg = 12;

	@FXML
	Rectangle manualblock;

	boolean isAuto = true;

	List<String> p1list, p2list, daltatlist, flowlist, bans, daltaplist,
			darcylist, wetplist, wetflist, dryplist, dryflist, tlist;

	ListChangeListener<Double> bubblelistener1;
	int skip, skipwet = 0, skipdry = 0;
	MyDialoug mydia;
	// for start popup
	static SimpleBooleanProperty stprop = new SimpleBooleanProperty(false);
	static SimpleBooleanProperty stpropcan = new SimpleBooleanProperty(false);

	static SimpleBooleanProperty isBubbleStart;

	static SimpleBooleanProperty isDryStart;

	AudioClip tones;

	@FXML
	private Button btnabr, starttestdry, starttest, stoptest, starttestwet,
			startautotest;

	int thval = 3000;
	int delayinauto = 2500;

	@FXML
	Label lbltestnom, lbltestdurationm, status, lblcurranttest;

	public static JFXDialog df;
	public static JFXDialog df1;

	@FXML
	AnchorPane guages, ancregu1, ancregu2, ancpg1, ancpg2, ancpg3, ancpg4;

	@FXML
	AnchorPane root, mainroot;

	private Tile gauge;
	private Tile gauge5;

	@FXML
	JFXToggleButton autotest;

	@FXML
	ToggleButton chamberonoff;

	int countbp = 0;
	writeFormat wrd;
	double p1 = 0, p2, bbp, bbf, bbd;

	String typeoftest = "";
	static int i2 = 0;
	boolean both = false, bothbw = false;
	long t1, t2, t1test, t2test;
	boolean once = true;
	int yi = 0;
	int ind = 0;
	static double p_inc = 0.0;
	int data_length = 0;
	double flowint = 0;
	final NumberAxis xAxis = new NumberAxis();
	final NumberAxis yAxis = new NumberAxis();

	LineChart<Number, Number> sc = new LineChart<>(xAxis, yAxis);
	XYChart.Series series1 = new XYChart.Series();
	XYChart.Series series2 = new XYChart.Series();

	private static final int DATA_POINT_POPUP_WIDTH = 30;
	private static final int DATA_POINT_POPUP_HEIGHT = 15;
	private static final int RGB_MAX = 255;

	private Notifier notifier;

	int testno = 1, trails;

	Myfont f = new Myfont(22);

	double calculationdia = 0;
	double conditionflow, conditionpressure;

	double curpre, curflow;

	double darcyavg = 0;

	@FXML
	AnchorPane root1, root2;

	final NumberAxis xAxis1 = new NumberAxis();
	final NumberAxis yAxis1 = new NumberAxis();
	LineChart<Number, Number> sc1 = new LineChart<>(xAxis1, yAxis1);
	XYChart.Series flowserires = new XYChart.Series();
	XYChart.Series flowserireswet = new XYChart.Series();

	final NumberAxis xAxis2 = new NumberAxis();
	final NumberAxis yAxis2 = new NumberAxis();
	LineChart<Number, Number> sc2 = new LineChart<>(xAxis2, yAxis2);
	XYChart.Series pressureserires = new XYChart.Series();
	XYChart.Series pressureserireswet = new XYChart.Series();

	long tempt1;

	int testtype = 0; // 0 for bubble 1 for wet 2 for dry
	SerialReader in;

	static boolean isSkiptest = false;

	@FXML
	Label lblresult, lbltesttype;

	// setting thresold value from user selection and value from configure
	// screen.
	void setThresoldval() {
		System.out.println("TH type : " + Myapp.thresold);
		if (Myapp.thresold.equals("First Bubble")) {

			try {
				System.out.println("first data : " + DataStore.getthfirstbp());
				thval = Integer.parseInt(DataStore.getthfirstbp());
			} catch (Exception e) {

				thval = 1000;
			}

		} else if (Myapp.thresold.equals("Moderate")) {

			try {
				thval = Integer.parseInt(DataStore.getthmoderat());
			} catch (Exception e) {
				thval = 3000;

			}

		} else if (Myapp.thresold.equals("Continous")) {

			try {
				thval = Integer.parseInt(DataStore.getthcontinous());
			} catch (Exception e) {
				thval = 5000;

			}

		}

	}

	// stop test function it is used when test is completed or in while
	// running...

	void stopTest() {

		starttest.setDisable(false);
		status.setText("Test hase been Stop");
		// TODO Auto-generated method stub
		writeFormat wrD = new writeFormat();
		wrD.stopTN();
		wrD.addLast();

		sendData(wrD);
		starttestdry.setDisable(false);
		starttestwet.setDisable(true);
		starttest.setDisable(false);
		// DataStore.intList.get("70").removeListener(bubblelistener);

		autotest.setDisable(false);

		p1list.clear();
		p2list.clear();
		daltaplist.clear();
		daltatlist.clear();
		bans.clear();
		flowlist.clear();
	}

	// setting plate value ..
	void setPlateval() {
		if (Myapp.splate.equals("Small")) {
			calculationdia = 1;
		} else if (Myapp.splate.equals("Large")) {

			calculationdia = 9.3;
		} else {

			calculationdia = 2.3;
		}
	}

	// set all shortcut
	void addShortCut() {
		KeyCombination backevent = new KeyCodeCombination(KeyCode.B,
				KeyCombination.CONTROL_ANY);

		mainroot.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {

				if (backevent.match(ke)) {
					testabourd();
				}

			}
		});

	}

	// set hardware connection status.. and if it connected then create
	// communication bridge with it.
	void connectHardware() {
		Myapp.testtrial = "4";
		in = new SerialReader(DataStore.in);

		try {
			DataStore.serialPort.removeEventListener();
			DataStore.serialPort.addEventListener(in);
			DataStore.serialPort.notifyOnDataAvailable(true);
			setTimer();
			status.setText("Hardware Connected");

		} catch (TooManyListenersException e) {

			MyDialoug.showError(102);
			status.setText("Hardware Problem");
		} catch (Exception e) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					status.setText("Hardware Problem");
					MyDialoug.showError(102);

				}
			});

		}

	}

	// setting all functionality and sequence.

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		Myapp.PrintAll();

		tones = new AudioClip(NLivetestControllerold.class.getResource(
				"stoptone.mp3").toString());

		if (DataStore.getchambertype().equals("Autometed")) {
			chamberonoff.setVisible(true);
		} else {
			chamberonoff.setVisible(false);
		}
		chamberonoff.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				if (!chamberonoff.isSelected()) {
					writeFormat wrd = new writeFormat();
					wrd.addStart();
					wrd.addChar('A');
					wrd.addChar('C');
					wrd.addChar('0');
					wrd.addBlank(2);
					wrd.addLast();
					sendData(wrd);
					System.out.println("Pc: Closing chamber");
					chamberonoff.setText("Open Chamber");
				} else {

					writeFormat wrd = new writeFormat();
					wrd.addStart();
					wrd.addChar('A');
					wrd.addChar('C');
					wrd.addChar('1');
					wrd.addBlank(2);
					wrd.addLast();
					sendData(wrd);
					System.out.println("Pc: Opening chamber");
					chamberonoff.setText("Close Chamber");
				}

			}
		});
		addShortCut();
		isRestart = new SimpleBooleanProperty(false);
		isSkiptest = false;
		Myapp.testtrial = "4";
		trails = Integer.parseInt(Myapp.testtrial);

		DataStore.getconfigdata();
		conditionflow = (double) Double.parseDouble(DataStore.getFc()) * 0.80;
		conditionpressure = Double.parseDouble(Myapp.endpress);

		isBubbleStart = new SimpleBooleanProperty(false);
		isDryStart = new SimpleBooleanProperty(false);
		lblfilename.setText(Myapp.sampleid);

		setThresoldval();
		setPlateval();
		lbltesttype.setText("Bubble Point");

		connectHardware();
		setButtons();
		setGauges();
		setGraph();
		setGraph12();

		isBubbleStart.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {

				if (newValue) {
					System.out.println("bubble call");
					bubbleClick();
				}

			}
		});

		// setTableList();

		autotest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				isAuto = autotest.isSelected();
				if (!autotest.isSelected()) {
					manualblock.setVisible(false);
					startautotest.setVisible(false);
				} else {

					manualblock.setVisible(true);
					startautotest.setVisible(true);
				}

			}
		});
		autotest.setSelected(true);

		starttestdry.setDisable(false);
		starttestwet.setDisable(true);

		stoptest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				// stopTest();

				mydia = new MyDialoug(Main.mainstage,
						"/userinput/Skiptestpopup.fxml");
				mydia.showDialoug();

			}
		});

		p1list = new ArrayList<>();
		p2list = new ArrayList<>();
		daltatlist = new ArrayList<>();
		flowlist = new ArrayList<>();
		bans = new ArrayList<>();
		daltaplist = new ArrayList<String>();
		darcylist = new ArrayList<String>();
		dryflist = new ArrayList<String>();
		dryplist = new ArrayList<String>();
		wetflist = new ArrayList<String>();
		wetplist = new ArrayList<String>();

		tlist = new ArrayList<>();
		startautotest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				// starttest.setDisable(true);

				// dryClick(0); //permeability

				isRestart.set(false);
				mydia = new MyDialoug(Main.mainstage,
						"/userinput/Re-testpopup.fxml");
				mydia.showDialoug();

			}
		});

		// setTimer();

		sendSetting(100);

		isRestart.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				restartTest();
			}
		});

	}

	void restartTest() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				status.setText("Restarting test...");
				// TODO Auto-generated method stub
				stopTest();

				try {
					Thread.sleep(6000);
				} catch (Exception e) {

				}
				showBubblePopup();

			}
		});
	}

	void bubbleClick() {
		
		System.out.println("Flow ontrller : "+DataStore.getFc());
		int flowcon=Integer.parseInt(DataStore.getFc());
		System.out.println("Flow ontrller : "+flowcon);
		int per=Integer.parseInt(Myapp.accbpt);
		
		int flowcounnnt=(int)65535*per/100;
		
		
		
		testtype=0;
		stoptest.setDisable(true);
		status.setText("Bubble-Point running..");
		lblcurranttest.setText("F/PT vs Time");

		flowserireswet.getData().clear();
		pressureserireswet.getData().clear();

		starttestdry.setDisable(true);
		flowserires.getData().clear();
		pressureserires.getData().clear();
		p1list.clear();
		p2list.clear();
		daltatlist.clear();
		flowlist.clear();
		bans.clear();
		daltaplist.clear();
		darcylist.clear();
		tlist.clear();

		skip = 0;
		yAxis.setLabel("F/PT");
		xAxis.setLabel("Time");

		tempt1 = System.currentTimeMillis();

		starttest.setDisable(true);

		countbp = 0;
		// starttest.setVisible(false);

		t2test = System.currentTimeMillis();
		series1.getData().clear();
		series2.getData().clear();

		series1.getData().add(new XYChart.Data(0, thval));
		series1.getData().add(new XYChart.Data(conditionpressure, thval));

		ind = 0;
		t1 = System.currentTimeMillis();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				int minde = 1500;
				Mycommand.setDACValue('2', flowcounnnt, 0);
				try {

					Thread.sleep(minde);
				} catch (Exception e) {

				}
				Mycommand.valveOff('4', 0);
				try {

					Thread.sleep(minde);
				} catch (Exception e) {

				}
				Mycommand.valveOn('2', 0);
				try {

					Thread.sleep(minde);
				} catch (Exception e) {

				}
				Mycommand.valveOn('5', 0);
				try {

					Thread.sleep(minde);
				} catch (Exception e) {

				}
				Mycommand.setDACValue('1', flowcounnnt, 0);

				try {

					Thread.sleep(minde + 3000);
				} catch (Exception e) {

				}
				Mycommand.valveOff('5', 0);

				try {

					Thread.sleep(minde);
				} catch (Exception e) {

				}

				Mycommand.setDelay(400, 0);
				Mycommand.sendAdcEnableBits("101", 1000);
				Mycommand.startADC(2500);

			}
		}).start();

	}

	// set event on bubble test
	void bubbleClickold() {
		stoptest.setDisable(true);
		status.setText("Bubble-Point running..");
		lblcurranttest.setText("F/PT vs Time");

		flowserireswet.getData().clear();
		pressureserireswet.getData().clear();

		starttestdry.setDisable(true);
		flowserires.getData().clear();
		pressureserires.getData().clear();
		p1list.clear();
		p2list.clear();
		daltatlist.clear();
		flowlist.clear();
		bans.clear();
		daltaplist.clear();
		darcylist.clear();
		tlist.clear();

		skip = 0;
		yAxis.setLabel("F/PT");
		xAxis.setLabel("Time");

		tempt1 = System.currentTimeMillis();

		starttest.setDisable(true);

		countbp = 0;
		// starttest.setVisible(false);

		wrd = new writeFormat();
		wrd.startBpN();
		wrd.addLast();
		sendData(wrd, 1000);

		t2test = System.currentTimeMillis();
		series1.getData().clear();
		series2.getData().clear();

		series1.getData().add(new XYChart.Data(0, thval));
		series1.getData().add(new XYChart.Data(conditionpressure, thval));

		ind = 0;
		t1 = System.currentTimeMillis();

		try {
			testtype = 0;
		} catch (Exception e) {

		}

	}

	// calculate darcy
	String getDarcy(double ddp, double ddf) {

		double k = 0;
		try {

			ddp = (double) ddp / 14.696;
			// ddf = ddf / 60;
			double vis = Double.parseDouble(Myapp.fluidvalue);
			double len = Double.parseDouble(Myapp.thikness);
			k = (4 * (ddf * vis * len))
					/ (calculationdia * calculationdia * 3.141592653589793 * ddp);

			darcyavg = darcyavg + k;

		} catch (Exception e) {
		}
		return "" + k;
	}

	// calculate darcy avg
	double getDarcyAvg() {
		double ans;

		ans = (double) darcyavg / darcylist.size();

		ans = (double) Math.round(ans * 100) / 100;

		return ans;
	}

	// get differencial time
	double getTime() {
		double an = (double) ((System.currentTimeMillis() - tempt1) / 1000);
		return an;
	}

	// set label font type
	void setLabelFont() {
		lbltestdurationm.setFont(f.getM_M());
		lbltestnom.setFont(f.getM_M());
	}

	// find file last added digit
	int findInt(String[] s) {
		try {

			List<String> s1 = Arrays.asList(s);
			ArrayList<String> ss = new ArrayList<String>(s1);

			ArrayList<Integer> ll = new ArrayList<Integer>();
			for (int i = 0; i < ss.size(); i++) {
				// System.out.println(ss.get(i));

				try {
					String temp = ss.get(i).toString()
							.substring(0, ss.get(i).indexOf("."));
					String[] data = temp.split("_");
					System.out.println(temp);

					ll.add(Integer.parseInt(data[data.length - 1]));
				} catch (Exception e) {

				}

			}

			if (ll.size() > 0) {

				return Collections.max(ll) + 1;
			} else {

				return 1;
			}
			// return 0;
		} catch (Exception e) {
			return 1;
		}
	}

	List<Integer> getAdcData(List<Integer> data) {
		List<Integer> d = new ArrayList<Integer>();

		System.out.println("READ .... ");
		for (int i = 4; i < 49; i = i + 3) {
			d.add(getIntFromBit(data.get(i), data.get(i + 1), data.get(i + 2)));

		}
		System.out.println("READ DONE ..." + d.size());
		System.out.println("Adc Data :" + d);
		return d;
	}

	int getIntFromBit(int a1, int a2, int a3) {
		System.out.println(a1 + " : " + a2 + ": " + a3);
		int a = 0;

		a = a1 << 16;
		a2 = a2 << 8;
		a = a | a2;
		a = a | a3;

		return a;
	}

	int findInt1(String[] s) {
		try {
			Date d1 = new Date();
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyy");
			String date1 = DATE_FORMAT.format(d1);

			List<String> s1 = Arrays.asList(s);
			ArrayList<String> ss = new ArrayList<String>(s1);

			ArrayList<Integer> ll = new ArrayList<Integer>();
			for (int i = 0; i < ss.size(); i++) {
				if (ss.get(i).contains(date1)) {
					// System.out.println(ss.get(i));

					String temp = ss.get(i).toString()
							.substring(0, ss.get(i).indexOf("."));
					String[] data = temp.split("_");
					System.out.println(temp);

					ll.add(Integer.parseInt(data[2]));

				} else {
					ss.remove(i);
				}
			}

			// return 0;
			return Collections.max(ll) + 1;
		} catch (Exception e) {
			return 1;
		}
	}

	// set round off all points
	public String getRound(Double r, int round) {

		/*
		 * if (round == 2) { r = (double) Math.round(r * 100) / 100; } else if
		 * (round == 3) { r = (double) Math.round(r * 1000) / 1000;
		 * 
		 * } else { r = (double) Math.round(r * 10000) / 10000;
		 * 
		 * }
		 */
		r = (double) Math.round(r * 100) / 100;

		return r + "";

	}

	// get diameter from bubble point pressure.
	public String getBubbledia(double bp) {
		double st = Double.parseDouble(Myapp.fluidvalue);

		// double d1=(double)0.415 * st/bp;
		double d1 = (double) (4 * st * 10000 * Double
				.parseDouble(Myapp.tfactore)) / (bp * 68947.6);

		if (Myapp.crossection.equals("Eliptical")) {
			d1 = d1 * 0.72;
		} else if (Myapp.crossection.equals("Slit")) {
			d1 = d1 * 0.71;
		} else if (Myapp.crossection.equals("Rectangular")) {
			d1 = d1 * 0.75;
		}

		String d = getRound(d1, 4);

		String sst = "surface : " + st + "\nbpressure: " + bp + "\nDiameter : "
				+ d + "\nCrosssection : " + Myapp.crossection + "\nTfactor :"
				+ Myapp.tfactore;
		System.out.println(sst);
		// para.setText(sst);

		return "" + d;
	}

	// set main graphs....
	void setGraph() {
		root.getChildren().add(sc);
		DataStore.pressure_max = Integer.parseInt(Myapp.endpress);
		sc.setAxisSortingPolicy(SortingPolicy.Y_AXIS.NONE);
		sc.setAxisSortingPolicy(SortingPolicy.X_AXIS.NONE);

		sc.setAnimated(false);
		sc.setLegendVisible(false);
		yAxis.setLabel("F/PT");
		xAxis.setLabel("Time");
		sc.setCreateSymbols(true);
		series1.setName("Dry-Test");
		series2.setName("Wet-Test");
		sc.getData().addAll(series1, series2);

		// sc.setTitle("Flow Vs Pressure");

		sc.prefWidthProperty().bind(root.widthProperty());
		sc.prefHeightProperty().bind(root.heightProperty());

		trails = Integer.parseInt(Myapp.testtrial);

		xAxis.setUpperBound(conditionpressure);
		xAxis.setAutoRanging(true);
		Zoom zoom = new Zoom(sc, root);

	}

	// asdasd

	// set two graphs pressure and flow.....
	void setGraph12() {
		root1.getChildren().add(sc1);
		sc1.setAxisSortingPolicy(SortingPolicy.Y_AXIS.NONE);
		sc1.setAnimated(false);
		sc1.setLegendVisible(false);
		sc1.setCreateSymbols(true);
		flowserires.setName("Dry-Test");
		sc1.getData().addAll(flowserireswet, flowserires);
		sc1.getStylesheets().add(
				getClass().getResource("dynamicgraph2.css").toExternalForm());
		// sc1.setTitle("Flow Vs Time");

		yAxis1.setLabel("Flow");
		xAxis1.setLabel("Time");
		yAxis2.setLabel("Pressure");
		xAxis2.setLabel("Time");

		sc1.prefWidthProperty().bind(root1.widthProperty());
		sc1.prefHeightProperty().bind(root1.heightProperty());

		root2.getChildren().add(sc2);
		sc2.setAxisSortingPolicy(SortingPolicy.Y_AXIS.NONE);
		sc2.setAnimated(false);
		sc2.setLegendVisible(false);
		sc2.setCreateSymbols(true);
		// pressureserires.setName("Pressure-test");
		sc2.getData().addAll(pressureserireswet, pressureserires);

		// sc2.setTitle("Pressure Vs Time");
		sc2.prefWidthProperty().bind(root2.widthProperty());
		sc2.prefHeightProperty().bind(root2.heightProperty());
		sc2.getStylesheets().add(
				getClass().getResource("dynamicgraph2.css").toExternalForm());

		Zoom zoom = new Zoom(sc2, root2);

		Zoom zoom1 = new Zoom(sc1, root1);
	}

	// set pressure and flow gauges
	void setGauges() {
		HBox v = new HBox(10);

		v.setMinWidth(guages.getPrefWidth());
		v.setMaxHeight(guages.getPrefHeight());
		v.setMaxWidth(guages.getPrefWidth());

		gauge5 = TileBuilder
				.create()
				.skinType(SkinType.BAR_GAUGE)
				// .prefSize(TILE_WIDTH, TILE_HEIGHT)
				.minValue(0)

				.barBackgroundColor(Color.GRAY)
				.backgroundColor(Color.valueOf("#f1f1f1"))
				.maxValue(conditionpressure)
				.startFromZero(true)
				.thresholdVisible(false)
				.title("Pressure")
				.unit("Psi")

				.textColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.titleColor(Color.GRAY)
				.valueColor(Color.GRAY)
				.gradientStops(new Stop(0, Bright.BLUE),
						new Stop(0.1, Bright.BLUE_GREEN),
						new Stop(0.2, Bright.GREEN),
						new Stop(0.3, Bright.GREEN_YELLOW),
						new Stop(0.4, Bright.YELLOW),
						new Stop(0.5, Bright.YELLOW_ORANGE),
						new Stop(0.6, Bright.ORANGE),
						new Stop(0.7, Bright.ORANGE_RED),
						new Stop(0.8, Bright.RED), new Stop(1.0, Dark.RED))
				.strokeWithGradient(true).animated(true).build();

		gauge = TileBuilder
				.create()
				.skinType(SkinType.BAR_GAUGE)
				// .prefSize(TILE_WIDTH, TILE_HEIGHT)
				.minValue(0)
				.barBackgroundColor(Color.GRAY)
				.backgroundColor(Color.valueOf("#f1f1f1"))
				.maxValue(conditionflow)
				.startFromZero(true)
				.thresholdVisible(false)
				.title("Flow")
				.textColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.titleColor(Color.GRAY)
				.valueColor(Color.GRAY)
				.unit("Sccm")
				.gradientStops(new Stop(0, Bright.BLUE),
						new Stop(0.1, Bright.BLUE_GREEN),
						new Stop(0.2, Bright.GREEN),
						new Stop(0.3, Bright.GREEN_YELLOW),
						new Stop(0.4, Bright.YELLOW),
						new Stop(0.5, Bright.YELLOW_ORANGE),
						new Stop(0.6, Bright.ORANGE),
						new Stop(0.7, Bright.ORANGE_RED),
						new Stop(0.8, Bright.RED), new Stop(1.0, Dark.RED))
				.strokeWithGradient(true).animated(true).build();

		// v.getChildren().add(ll);
		// v.getChildren().add(gauge5);

		// v.getChildren().add(new Separator());
		// gauge5.valueProperty().bind(DataStore.livepressure);

		v.getChildren().add(gauge5);
		v.getChildren().add(gauge);

		// v.getChildren().add(ll2);
		// v.getChildren().add(ll22);
		// v.getChildren().add(gauge);

		Myapp.ftype.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {

				String pp = "" + newValue;

				Platform.runLater(new Runnable() {

					@Override
					public void run() {

						// TODO Auto-generated method stub
						if (Integer.parseInt("" + newValue) == 1) {
							// ll22.setText("Flow Type : Low");
						} else {
							// ll22.setText("Flow Type : High");
						}
					}
				});

			}

		});

		DataStore.livepressure.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {

				String pp = "" + newValue;

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						gauge5.setValue((double) newValue);

					}
				});

			}

		});

		DataStore.liveflow.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				System.out.println("New - > FLOW   :" + newValue);
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String ff = newValue + "";

						gauge.setValue((double) newValue);

					}
				});

			}

		});

		// v.getChildren().add(new Separator());

		guages.getChildren().add(v);

	}

	void setTimer() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showBubblePopup();

					}
				});
			}

		};
		timer.schedule(task, 2000);
	}

	// set all button events
	void setButtons() {

		btnabr.getStyleClass().add("transperant_comm");
		startautotest.getStyleClass().add("transperant_comm");
		stoptest.getStyleClass().add("transperant_comm");

		btnabr.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				testabourd();
			}
		});

	}

	// stop test popup
	void testabourd() {

		mydia = new MyDialoug(Main.mainstage, "/userinput/Nabourdpopup.fxml");
		// mydia = new MyDialoug(Main.mainstage, "/userinput/Nresult.fxml");

		mydia.showDialoug();

	}

	// set points on mouse over event
	private void setDataPointPopup(XYChart<Number, Number> sc) {
		final Popup popup = new Popup();
		popup.setHeight(20);
		popup.setWidth(60);

		for (int i = 0; i < sc.getData().size(); i++) {
			final int dataSeriesIndex = i;
			final XYChart.Series<Number, Number> series = sc.getData().get(i);
			for (final Data<Number, Number> data : series.getData()) {
				final Node node = data.getNode();
				node.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
						new EventHandler<MouseEvent>() {

							private static final int X_OFFSET = 15;
							private static final int Y_OFFSET = -5;
							Label label = new Label();

							@Override
							public void handle(final MouseEvent event) {
								// System.out.println("MOuse Event");
								final String colorString = "#cfecf0";
								label.setFont(new Font(20));
								popup.getContent().setAll(label);
								label.setStyle("-fx-background-color: "
										+ colorString + "; -fx-border-color: "
										+ colorString + ";");
								label.setText("x=" + data.getXValue() + ", y="
										+ data.getYValue());
								popup.show(data.getNode().getScene()
										.getWindow(), event.getScreenX()
										+ X_OFFSET, event.getScreenY()
										+ Y_OFFSET);
								event.consume();
							}

							public EventHandler<MouseEvent> init() {
								label.getStyleClass().add("chart-popup-label");
								return this;
							}

						}.init());

				node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
						new EventHandler<MouseEvent>() {

							@Override
							public void handle(final MouseEvent event) {
								popup.hide();
								event.consume();
							}
						});

				// this handler selects the corresponding table item when a data
				// item in the chart was clicked.

			}
		}

	}

	// data send to MCU
	public void sendData(writeFormat w) {
		System.out.println("Sending Data......");
		w.showData();
		Thread t = new Thread(new SerialWriter(DataStore.out, w));
		t.start();

	}

	// send data to MCU after some delay
	void sendData(writeFormat w, int slp) {
		System.out.println("Sending Data......");
		w.showData();
		Thread t = new Thread(new SerialWriter(DataStore.out, w, slp));
		try {

			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// set value to ascii and package
	public static List<Integer> getValueList(int val) {
		String pad = "000000";
		String st = "" + Integer.toHexString(val);
		String st1 = (pad + st).substring(st.length());
		List<Integer> ls = new ArrayList<Integer>();

		int n = (int) Long.parseLong(st1.substring(0, 2), 16);
		int n1 = (int) Long.parseLong(st1.substring(2, 4), 16);
		int n2 = (int) Long.parseLong(st1.substring(4, 6), 16);
		ls.add(n);
		ls.add(n1);
		ls.add(n2);

		return ls;
	}

	// set flow accuricy
	List<Integer> getFlowAccuricy() {
		/*
		 * int fl = Integer.parseInt(Myapp.accbpt); fl = fl - 100; int fc =
		 * Integer.parseInt(DataStore.getFc()); int s1 = (int) fl * fc / 100;
		 * 
		 * int fla = (int) s1 * 65535 / fc;
		 * System.out.println("Flow Accuracy : " + s1 + " : " + fla);
		 * List<Integer> data = getValueList(fla);
		 */

		int fl = Integer.parseInt(Myapp.accbpt);
		int max = 65535;
		int c = fl * max / 100;
		System.out.println("Count set flow controller:" + c);
		List<Integer> data = getValueList(c);
		return data;
	}

	// set step size
	List<Integer> getStepSize() {
		int fl = Integer.parseInt(Myapp.accstep);
		int pr = Integer.parseInt(DataStore.getPr());

		fl = 110 - fl;

		double min = (double) pr * stepsizepercentage / 100;
		double val = 0;

		System.out.println("Stpeps percentage : " + fl + " min : " + min);
		if (fl == 10) {
			val = min;
		} else if (fl == 100) {
			val = maxpressureinstepsize;
		} else {
			double s1 = (maxpressureinstepsize - min) / 9;
			val = (((fl - 10) / 10) * s1) + min;
			System.out.println("Val : " + val);
		}

		double fla = (double) val * 65535 / pr;
		System.out.println("Val in 65535 : " + fla);
		if (fla < 132) {
			fla = 132;
		}
		System.out.println("Step Size : " + fla);
		List<Integer> data = getValueList((int) fla);
		return data;
	}

	// send setting to MCU
	void sendSetting(int delay) {
		System.out.println("Sending settings");
		writeFormat wrD = new writeFormat();
		wrD.addChar('A');
		wrD.addChar('S');
		wrD.addData1(getFlowAccuricy());
		wrD.addData1(getStepSize());
		wrD.addChar(Myapp.stabilitytype.charAt(0));

		if (Myapp.stabilitytype.equals("1")) {
			wrD.addBlank(3);
			int fl = Integer.parseInt(Myapp.accstability);
			int val = 0;
			if (fl == 10) {
				val = (int) mindelay;
			} else {
				int v = (int) (maxdelay - mindelay) / 9;
				int c = (int) ((fl - 10) / 10) * v;
				val = c + (int) mindelay;
			}
			System.out.println("Delay : " + val);
			wrD.addData1(getValueList(val));

		} else if (Myapp.stabilitytype.equals("2")) {

			int fl = Integer.parseInt(Myapp.accstability);
			int val = 0;
			if (fl == 10) {
				val = (int) minavg;
			} else if (fl == 100) {
				val = (int) maxavg;
			} else {
				int v = (int) (maxavg - minavg) / 9;
				int c = (int) ((fl - 10) / 10) * v;
				val = c + (int) minavg;
			}

			System.out.println("Sample avg : " + val);

			wrD.addData1(getValueList(val));
			wrD.addData1(getValueList((int) mindelay));
		} else {

		}

		wrD.addBlank(3);
		wrD.addLast();
		sendData(wrD, delay);

		writeFormat ww = new writeFormat();
		ww.addStart();
		ww.addChar('D');
		ww.addChar('A');
		ww.addData1(getValueList(700));
		ww.addData1(getValueList(700));
		ww.addData1(getValueList(700));
		ww.addData1(getValueList(700));
		ww.addLast();
		sendData(ww, 500);
	}

	// set all incomming packet event...
	public class SerialReader implements SerialPortEventListener {

		InputStream in;
		int ind = 0;
		List<Integer> readData = new ArrayList<Integer>();

		public SerialReader(InputStream in) {
			this.in = in;
			DataStore.getconfigdata();
		}

		public void serialEvent(SerialPortEvent arg0) {
			int data;
			try {
				int len = 0;
				char prev = '\0';
				// System.out.println("Reading Started:");

				while ((data = in.read()) > -1) {

					if (data == '\n' && prev == 'E') {
						break;
					}
					if (len > 0 || (data == '\r' && prev == '\n')) {
						readData.add(data);

						len++;
					}
					prev = (char) data;
					System.out.print(prev);

					// System.out.print(new String(buffer,0,len));
				}

				for (int i = 1; i < readData.size(); i++) {

					if (readData.get(i) == 'F'
							&& readData.get(i + 1) == (int) 'M'
							&& readData.get(i + 2) == (int) 'A') {
						double pr = 0, fl = 0;
						List<Integer> reading = getAdcData(readData);

						int maxpre = Integer.parseInt(DataStore.getPg2());
						pr = (double) reading.get(2) * maxpre / 65535;

						fl = (double) reading.get(0)
								* Integer.parseInt(DataStore.getFc()) / 65535;

						System.out.println("" + reading);

						System.out.println("Pr1 : " + pr + "\nFc : " + fl);

						DataStore.liveflow.set(fl);

						DataStore.livepressure.set(pr);

						if (testtype == 0) {

							setBubblePoints(pr, fl);

						}

					}

					else if (readData.get(i) == (int) 'T'
							&& readData.get(i + 1) == (int) 'D') {
						int prd, fld;
						double pr = 0, fl = 0;

						// pressure.....
						int a = 0, a1, a2, a3;
						a1 = readData.get(i + 5);
						a2 = readData.get(i + 6);
						a3 = readData.get(i + 7);

						System.out.println("Original Pressure Data BIT :" + a1
								+ " : " + a2 + " : " + a3);

						a = a1 << 16;
						a2 = a2 << 8;
						a = a | a2;
						a = a | a3;
						prd = a;

						if (readData.get(i + 2) == (int) 'P'
								&& readData.get(i + 3) == (int) 'R') {
							if (readData.get(i + 4) == (int) '1') {
								int maxpre = Integer
										.parseInt(DataStore.getPr());
								pr = (double) a * maxpre / 65535;

								System.out
										.println(" Pressure Regulator..... : "
												+ pr);

							}

						} else if (readData.get(i + 2) == (int) 'P'
								&& readData.get(i + 3) == (int) 'G') {
							if (readData.get(i + 4) == (int) '1') {
								int maxpre = Integer.parseInt(DataStore
										.getPg1());
								pr = (double) a * maxpre / 65535;

								// pr = pr - 14.7;
								// b1 = b1 - Myapp.pg1offset.get();
								System.out
										.println(" Pressure guage1 original..... : "
												+ a);

								System.out.println(" Pressure guage1 ..... : "
										+ pr);

							} else if (readData.get(i + 4) == (int) '2') {
								int maxpre = Integer.parseInt(DataStore
										.getPg2());
								System.out.println("Pressure Gauge2 Org Data "
										+ a);
								pr = (double) a * maxpre / 65535;

								// b1 = b1 - Myapp.pg2offset.get();

								System.out.println(" Pressure gauge2 ..... : "
										+ pr);

							} else {
								char c = (char) (int) readData.get(i + 6);
								System.out.println("Not found : "
										+ readData.get(i + 6) + " : " + c);
							}
						}

						// flow.....
						a1 = readData.get(i + 11);
						a2 = readData.get(i + 12);
						a3 = readData.get(i + 13);

						System.out.println("Original Flow Data BIT :" + a1
								+ " : " + a2 + " : " + a3);
						a = a1 << 16;
						a2 = a2 << 8;
						a = a | a2;
						a = a | a3;
						fld = a;

						if (readData.get(i + 8) == (int) 'F'
								&& readData.get(i + 9) == (int) 'C') {
							if (readData.get(i + 10) == (int) '1') {
								fl = (double) a
										* Integer.parseInt(DataStore.getFc())
										/ 65535;

								System.out.println("Flow Controller :  ... :"
										+ fl);

							}

						} else if (readData.get(i + 8) == (int) 'F'
								&& readData.get(i + 9) == (int) 'M') {
							if (readData.get(i + 10) == (int) '1') {
								fl = (double) a
										* Integer.parseInt(DataStore.getFm1())
										/ 65535;
								System.out.println(" Flow meter 1.... : " + a);

								// b=(double)a*8000/65535;
								System.out.println("Flow Meter 1 : ... :" + fl);

							} else if (readData.get(i + 10) == (int) '2') {
								fl = (double) a
										* Integer.parseInt(DataStore.getFm2())
										/ 65535;
								// b=(double)a*8000/65535;
								System.out.println("Flow Meter 2 : ... :" + fl);

							}
						}

						// setdata

						DataStore.liveflow.set(fl);

						DataStore.livepressure.set(pr);

						if (testtype == 0) {
							setBubblePoints(pr, fl);
						}

					}

					if (readData.get(i) == 80 && readData.get(i + 5) == 70) {

						double pr, fl;
						// System.out.println("Pressure:
						// "+Integer.parseInt(Integer.toHexString(readData.get(i+1))+""+Integer.toHexString(readData.get(i+2))+""+Integer.toHexString(readData.get(i+3)),16));
						int a = 0, a1, a2, a3;
						a1 = readData.get(i + 2);
						a2 = readData.get(i + 3);
						a3 = readData.get(i + 4);
						System.out
								.println("...........................................................\n\nOriginal Pressure Data BIT :"
										+ a1 + " : " + a2 + " : " + a3);

						a = a1 << 16;
						a2 = a2 << 8;
						a = a | a2;
						a = a | a3;
						System.out.println("Original Pressure Data ..... : "
								+ a);

						// double b=(double)a*110/16777215;

						double b1 = 0;
						// b = Math.round(b*10000)/10000D;
						if (readData.get(i + 1) == 49) {
							int maxpre = Integer.parseInt(DataStore.getPr());
							b1 = (double) a * maxpre / 65535;

							System.out.println(" Pressure Regulator..... : "
									+ b1);

							DataStore.spr.set(b1);

						} else if (readData.get(i + 1) == 50) {
							int maxpre = Integer.parseInt(DataStore.getPg1());
							b1 = (double) a * maxpre / 65535;

							// b1 = b1 - Myapp.pg1offset.get();
							System.out
									.println(" Pressure guage1 ..... : " + b1);

							DataStore.spg1.set(b1);

							DataStore.sv3.set(true);

						} else if (readData.get(i + 1) == 51) {
							int maxpre = Integer.parseInt(DataStore.getPg2());
							System.out.println("Pressure Gauge2 Org Data " + a);
							b1 = (double) a * maxpre / 65535;

							// b1 = b1 - Myapp.pg2offset.get();

							System.out
									.println(" Pressure gauge2 ..... : " + b1);

							DataStore.spg2.set(b1);
							DataStore.sv3.set(false);
						}

						a1 = readData.get(i + 7);
						a2 = readData.get(i + 8);
						a3 = readData.get(i + 9);

						System.out.println("Original Flow Data BIT :" + a1
								+ " : " + a2 + " : " + a3);
						a = a1 << 16;
						a2 = a2 << 8;
						a = a | a2;
						a = a | a3;
						System.out.println("Original Flow Data ..... : " + a);
						double b = 0;
						// System.out.println("BIT VALUE:"+readData.get(i+1));
						if (readData.get(i + 6) == 49) {
							b = (double) a
									* Integer.parseInt(DataStore.getFc())
									/ 65535;

							System.out.println("Flow Controller :  ... :" + b);
							DataStore.sfc.set((int) b);

						} else if (readData.get(i + 6) == 50) {
							b = (double) a
									* Integer.parseInt(DataStore.getFm1())
									/ 65535;
							// b=(double)a*8000/65535;
							System.out.println("Flow Meter 1 : ... :" + b);
							DataStore.sfm1.set((int) b);
							DataStore.sv1.set(true);
							DataStore.sv2.set(false);
						} else if (readData.get(i + 6) == 51) {
							b = (double) a
									* Integer.parseInt(DataStore.getFm2())
									/ 65535;
							// b=(double)a*200000/65535;
							System.out.println("Flow Meter 2 : ... :" + b);
							DataStore.sfm2.set((int) b);
							DataStore.sv1.set(false);
							DataStore.sv2.set(true);
						}

						fl = b;
						DataStore.liveflow.set((double) b);
						pr = b1;
						DataStore.livepressure.set(b1);

						// System.out.println("test tyepe : " + testtype);
						if (testtype == 0) {
							setBubblePoints(pr, fl);
						}

					}

					readData.clear();
					break;

				}

			} catch (IOException e) {
				DataStore.serialPort.removeEventListener();
				MyDialoug.showErrorHome(103);

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						status.setText("Connection has been lost");
					}
				});
				System.out.println("Live screen error :" + e.getMessage());

			}

		}

	}

	void setBubblePoints(double pr, double fl) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				flowserires.getData().add(new XYChart.Data(getTime(), fl));
				pressureserires.getData().add(new XYChart.Data(getTime(), pr));

			}
		});

		p2 = pr;
		t2 = System.currentTimeMillis();

		if (p2 < conditionpressure) {
			if (p2 != 0 && p1 != p2) {
				System.out.println("IN if p2!=0 and  " + p1 + " - " + p2);

				double deltap;// = (double) p2 - p1;
				double deltat = (double) (t2 - t1) / 1000;

				if (p2 > p1) {
					deltap = (double) p2 - p1;
				} else {
					deltap = (double) p1 - p2;
				}

				double ans;

				System.out.println("Record Number : " + ind);
				ans = (fl * deltat) / deltap;

				System.out.println("Flow : " + fl + "\nP1 : " + p1 + "\nP2 : "
						+ p2 + "\nT1 :" + t1 + "\n T2 : " + t2);
				System.out.println("Delta P : " + deltap + " \nDelta T : "
						+ (deltat));
				System.out.println("Answer F/PT : " + ans);

				if (ans > 0) {
					// if(ans<Double.parseDouble(DataStore.thresoldvalue))
					p1list.add("" + p1);
					p2list.add("" + p2);
					daltaplist.add("" + deltap);
					daltatlist.add("" + deltat);
					flowlist.add("" + fl);
					bans.add("" + ans);
					tlist.add("" + getTime());
					if (ans < thval) {
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// System.out.println("Size of list is : "+DataStore.intList.get(80).size()+"
								// .... INDEX : "+ind);
								series2.getData().add(new XYChart.Data(

								getTime(), ans));

								series1.getData().remove(1);
								series1.getData()
										.add(new XYChart.Data(getTime() + 10,
												thval));

								ind++;
								countbp = 0;

							}
						});

					} else {

						countbp++;

						if (countbp > 2) {
							Platform.runLater(new Runnable() {

								@Override
								public void run() {

									series2.getData().add(
											new XYChart.Data(getTime(), ans));
									series1.getData().remove(1);
									series1.getData().add(
											new XYChart.Data(getTime() + 10,
													thval));

									// bbp=(double)DataStore.intList.get("80").get(DataStore.intList.get("80").size()-4);
									// bbf=(double)DataStore.intList.get("70").get(DataStore.intList.get("70").size()-4);

									bbp = Double.parseDouble(p2list.get(p2list
											.size() - 3));

									System.out.println("Pressure points : "
											+ p2list);
									System.out.println("BBBBBBPPPPPPPPPP : "
											+ bbp);

									System.out
											.println("Completedd test number "
													+ testno
													+ " in "
													+ ((t2test - t1test) / 1000)
													+ " seconds ");
									ind++;
									String bubblepoint = getBubbledia(bbp);
									bbd = Double.parseDouble(bubblepoint);

									lblbpc.setText("Bubble Point : " + bbd
											+ " µ");

									Myapp.bps.put("" + bbp, "" + bubblepoint);

									Mycommand.stopADC(0);
									Mycommand.valveOn('5', 500);
									Mycommand.setDACValue('1', 0, 1000);
									Mycommand.setDACValue('2', 0, 1800);

									testtype = 5;
									createCsvTableBubble();

								}

							});

						} else {
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method
									// stub

									series2.getData().add(
											new XYChart.Data(getTime(), ans));
									series1.getData().remove(1);
									series1.getData().add(
											new XYChart.Data(getTime() + 10,
													thval));

									ind++;
								}
							});

						}
						System.out
								.println("--->>>>> Value of trigger counter :"
										+ countbp);

					}

					// bubble point complete send code
				} else {
					ind++;
				}
				t1 = t2;
				p1 = p2;
			} else {
				t1 = System.currentTimeMillis();
				ind++;
			}
		} else {

			testtype = 5;
			// DataStore.serialPort.removeEventListener();
			Mycommand.stopADC(0);
			Mycommand.valveOn('5', 500);
			Mycommand.setDACValue('1', 0, 1000);
			Mycommand.setDACValue('2', 0, 1800);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					System.out.println("NO buuble fund");
					status.setText("No Bubble Point found");
					Toast.makeText(Main.mainstage, "No bubble point found",
							1500, 100, 100);
				}
			});

			starttest.setDisable(false);
		}

	}

	// set bubble points from MCU and checking conditions
	void setBubblePointsold(double pr, double fl) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				flowserires.getData().add(new XYChart.Data(getTime(), fl));
				pressureserires.getData().add(new XYChart.Data(getTime(), pr));

			}
		});

		if (ind > skip) {

			p2 = pr;
			t2 = System.currentTimeMillis();

			if (p2 < conditionpressure) {
				if (p2 != 0 && p1 != p2) {
					System.out.println("IN if p2!=0 and  " + p1 + " - " + p2);

					double deltap;// = (double) p2 - p1;
					double deltat = (double) (t2 - t1) / 1000;

					if (p2 > p1) {
						deltap = (double) p2 - p1;
					} else {
						deltap = (double) p1 - p2;
					}

					/*
					 * if(deltat>2) { deltat=1.001; }
					 */
					double ans;

					System.out.println("Record Number : " + ind);
					ans = (fl * deltat) / deltap;

					System.out.println("Flow : " + fl + "\nP1 : " + p1
							+ "\nP2 : " + p2 + "\nT1 :" + t1 + "\n T2 : " + t2);
					System.out.println("Delta P : " + deltap + " \nDelta T : "
							+ (deltat));
					System.out.println("Answer F/PT : " + ans);

					if (ans > 0) {
						// if(ans<Double.parseDouble(DataStore.thresoldvalue))
						p1list.add("" + p1);
						p2list.add("" + p2);
						daltaplist.add("" + deltap);
						daltatlist.add("" + deltat);
						flowlist.add("" + fl);
						bans.add("" + ans);

						tlist.add("" + getTime());
						if (ans < thval) {
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									// System.out.println("Size of list is : "+DataStore.intList.get(80).size()+"
									// .... INDEX : "+ind);
									series2.getData().add(new XYChart.Data(

									getTime(), ans));

									series1.getData().remove(1);
									series1.getData().add(
											new XYChart.Data(getTime() + 10,
													thval));

									ind++;
									countbp = 0;

								}
							});

						} else {

							countbp++;

							if (countbp > 2) {
								Platform.runLater(new Runnable() {

									@Override
									public void run() {

										series2.getData()
												.add(new XYChart.Data(
														getTime(), ans));
										series1.getData().remove(1);
										series1.getData().add(
												new XYChart.Data(
														getTime() + 10, thval));

										// bbp=(double)DataStore.intList.get("80").get(DataStore.intList.get("80").size()-4);
										// bbf=(double)DataStore.intList.get("70").get(DataStore.intList.get("70").size()-4);

										bbp = Double.parseDouble(p2list
												.get(p2list.size() - 3));

										System.out.println("Pressure points : "
												+ DataStore.intList.get("80"));
										System.out
												.println("BBBBBBPPPPPPPPPP : "
														+ bbp);
										System.out.println("Index : " + ind);

										System.out
												.println("Completedd test number "
														+ testno
														+ " in "
														+ ((t2test - t1test) / 1000)
														+ " seconds ");

										ind++;

										String bubblepoint = getBubbledia(bbp);
										bbd = Double.parseDouble(bubblepoint);

										lblbpc.setText("Bubble Point : " + bbd
												+ " µ");

										Myapp.bps.put("" + bbp, ""
												+ bubblepoint);
										wrd = new writeFormat();
										wrd.stopBpN();
										wrd.addLast();
										sendData(wrd);
										createCsvTableBubble();

										testtype = 5;

									}

								});

							} else {
								Platform.runLater(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method
										// stub

										series2.getData()
												.add(new XYChart.Data(
														getTime(), ans));
										series1.getData().remove(1);
										series1.getData().add(
												new XYChart.Data(
														getTime() + 10, thval));

										ind++;
									}
								});

							}
							System.out
									.println("--->>>>> Value of trigger counter :"
											+ countbp);

						}

						// bubble point complete send code
					} else {
						ind++;
					}
					t1 = t2;
					p1 = p2;
				} else {
					t1 = System.currentTimeMillis();
					ind++;
				}
			} else {

				// DataStore.serialPort.removeEventListener();
				wrd = new writeFormat();
				wrd.stopBpN();
				wrd.addLast();
				sendData(wrd);

				writeFormat wrD = new writeFormat();
				wrD.stopTN();
				wrD.addLast();

				sendData(wrD, 1000);

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						status.setText("No Bubble Point found");

					}
				});

				starttest.setDisable(false);
			}

		}

		else {
			System.out.println("Skip data  : " + ind);
			ind++;
			t1 = System.currentTimeMillis();

			if (ind == skip) {

			}

		}

	}

	// csv create function
	void createCsvTableBubble() {

		try {
			System.out.println("csv creating........");
			CsvWriter cs = new CsvWriter();

			Date d1 = new Date();
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyy");
			String date1 = DATE_FORMAT.format(d1);

			File fff = new File("TableCsvs");
			if (!fff.exists()) {
				fff.mkdir();
			}

			File fffff = new File("TableCsvs/" + Myapp.uid);
			if (!fffff.exists()) {
				fffff.mkdir();
			}

			File f = new File(fffff.getPath() + "/" + Myapp.sampleid);
			if (!f.isDirectory()) {
				f.mkdir();
				System.out.println("Dir csv folder created");
			}

			String[] ff = f.list();

			CalculatePorometerData c = new CalculatePorometerData();

			cs.wtirefile(f.getPath() + "/" + Myapp.sampleid + "_" + findInt(ff)
					+ ".csv");

			cs.firstLine("bubblepoint");
			cs.newLine("testname", "bubblepoint");
			cs.newLine("bpressure", getRound(bbp, 4));
			cs.newLine("bdiameter", "" + bbd);
			cs.newLine("sample", Myapp.sampleid);
			cs.newLine("samplediameter", "" + calculationdia);
			cs.newLine("thikness", Myapp.thikness);
			cs.newLine("fluidname", Myapp.fluidname);
			cs.newLine("fluidvalue", Myapp.fluidvalue);
			cs.newLine("thresold", "" + thval);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

			Date date = new Date();
			t1test = System.currentTimeMillis();
			int s = (int) (t1test - t2test) / 1000;

			int hour = (s / 3600);
			int min = (s / 60) % 60;
			int remsec = (s % 60);
			String durr = "";
			if (hour != 0) {
				durr = hour + " hr:" + min + " min:" + remsec + " sec";
			} else {
				durr = min + " min:" + remsec + " sec";
			}

			cs.newLine("duration", durr);
			cs.newLine("durationsecond", s + "");
			cs.newLine("testtime", timeFormat.format(date));
			cs.newLine("testdate", dateFormat.format(date));
			cs.newLine("customerid", Myapp.uid);

			cs.newLine("indistry", Myapp.indtype);
			cs.newLine("application", Myapp.materialapp);
			cs.newLine("materialclassification", Myapp.classification);
			cs.newLine("crosssection", Myapp.crossection);
			cs.newLine("materialtype", Myapp.materialtype);
			cs.newLine("tfact", Myapp.tfactore);
			cs.newLine("splate", Myapp.splate);

			cs.newLine("flow", flowlist);
			cs.newLine("Dt", daltatlist);
			cs.newLine("p1", p1list);
			cs.newLine("p2", p2list);
			cs.newLine("Dp", daltaplist);
			cs.newLine("ans", bans);
			cs.newLine("t", tlist);

			savefile = new File(cs.filename);
			cs.closefile();
			showResultPopup();

			System.out.println("csv Created");

		} catch (Exception e) {
			e.printStackTrace();
		}

		// LoadAnchor.LoadCreateTestPage();
		// LoadAnchor.LoadReportPage();
	}

	// show result popup
	void showResultPopup() {
		tones.play();
		mydia = new MyDialoug(Main.mainstage, "/userinput/popupresult.fxml");

		mydia.showDialoug();
	}

	// show start test popup
	void showBubblePopup() {
		mydia = new MyDialoug(Main.mainstage, "/userinput/Wetpopup.fxml");

		mydia.showDialoug();
	}

	// send stop protocol to MCU
	void sendStop() {
		writeFormat wrD = new writeFormat();
		wrD.stopTN();
		wrD.addLast();
		sendData(wrD, 1000);

	}

}
