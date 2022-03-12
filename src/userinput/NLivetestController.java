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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;

import Notification.Notification.Notifier;
import application.DataStore;
import application.Main;
import application.Myapp;
import application.SerialWriter;
import application.writeFormat;
import communicationProtocol.Mycommand;
import data_read_write.CsvWriter;
import data_read_write.PolynomialRegression;
import de.tesis.dynaware.javafx.fancychart.zoom.Zoom;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.colors.Bright;
import extrafont.Myfont;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
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
import toast.MyDialoug;
import toast.Toast;

public class NLivetestController implements Initializable {

	static SimpleBooleanProperty isRestart;

	static File savefile;

	@FXML
	Label lblfilename, lblbpc;

	double stepsizepercentage = 0.2, maxpressureinstepsize = 1, mindelay = 200, maxdelay = 2000, minavg = 2,
			maxavg = 12;

	@FXML
	Rectangle manualblock;

	boolean isAuto = true;

	List<String> tChamber, hChamber, tBottom, hBottom, tTop, hTop, tlist, svp, avp, mass;

	ListChangeListener<Double> bubblelistener1;
	int skip, skipwet = 0, skipdry = 0;
	MyDialoug mydia;
	// for start popup
	static SimpleBooleanProperty stprop = new SimpleBooleanProperty(false);
	static SimpleBooleanProperty stpropcan = new SimpleBooleanProperty(false);

	static SimpleBooleanProperty isBubbleStart;
	static SimpleBooleanProperty isSkiptest;
	static SimpleBooleanProperty isDryStart;

	AudioClip tones;

	@FXML
	private Button btnabr, starttestdry, starttest, stoptest, starttestwet, startautotest,btninfo;

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

	int testno = 1;

	Myfont f = new Myfont(22);

	double calculationdia = 0;

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

	long tempt1 = 0;

	int testtype = 0; // 0 for bubble 1 for wet 2 for dry
	SerialReader in;

	

	@FXML
	Label lblresult, lbltesttype;

	int hmin = 48, hmax = 53, tmin = 23, tmax = 26;
	int runningCondition = 0;

	int maxDatapoint = 100;
	int delayinterval = 500;

	long testtime = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		Myapp.PrintAll();
		DataStore.getsystemdata();
		try {
			hmin = Integer.parseInt(DataStore.getHmin());
			hmax = Integer.parseInt(DataStore.getHmax());
			tmin = Integer.parseInt(DataStore.getTmin());
			tmax = Integer.parseInt(DataStore.getHmax());
			maxDatapoint = Integer.parseInt(Myapp.testdata);
			delayinterval = Integer.parseInt(Myapp.dataint)*1000;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in data parse ");
		}

		tones = new AudioClip(NLivetestController.class.getResource("stoptone.mp3").toString());

		addShortCut();
		isRestart = new SimpleBooleanProperty(false);
	
		// Myapp.testtrial = "4";

		// DataStore.getconfigdata();

		isBubbleStart = new SimpleBooleanProperty(false);
		isDryStart = new SimpleBooleanProperty(false);
		isSkiptest=new SimpleBooleanProperty(false);
;		lblfilename.setText(Myapp.sampleid);

		setPlateval();
		lbltesttype.setText("WVTR");

		connectHardware();
		setButtons();
		setGauges();
		setGraph();
		setGraph12();

		isBubbleStart.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if (newValue) {
					System.out.println("bubble call");
					bubbleClick();
				}

			}
		});

		isSkiptest.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if (newValue) {
					System.out.println("bubble call");
					stopTest();
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

				mydia = new MyDialoug(Main.mainstage, "/userinput/Skiptestpopup.fxml");
				mydia.showDialoug();

			}
		});

		tChamber = new ArrayList<>();
		hChamber = new ArrayList<>();
		tBottom = new ArrayList<>();
		hBottom = new ArrayList<>();
		tTop = new ArrayList<>();
		hTop = new ArrayList<String>();
		tlist = new ArrayList<String>();
		svp = new ArrayList<String>();
		avp = new ArrayList<String>();
		mass = new ArrayList<String>();

		startautotest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				// starttest.setDisable(true);

				// dryClick(0); //permeability
				isRestart.set(false);
				mydia = new MyDialoug(Main.mainstage, "/userinput/Re-testpopup.fxml");
				mydia.showDialoug();

			}
		});

		// setTimer();

		// sendSetting(100);

		isRestart.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				restartTest();
			}
		});

	}

	void bubbleClick() {

		testtype = 0;
		stoptest.setDisable(true);
		status.setText("WVTR running..");
		lblcurranttest.setText("WVTR");

		flowserireswet.getData().clear();
		pressureserireswet.getData().clear();

		starttestdry.setDisable(true);
		flowserires.getData().clear();
		pressureserires.getData().clear();
		clearList();

		skip = 0;
		yAxis.setLabel("Mass (g)");
		xAxis.setLabel("Time (min)");

		tempt1 = 0;
		starttest.setDisable(true);

		testtime = System.currentTimeMillis();

		countbp = 0;
		// starttest.setVisible(false);

		t2test = System.currentTimeMillis();
		series1.getData().clear();
		series2.getData().clear();

//		series1.getData().add(new XYChart.Data(0, thval));
//		series1.getData().add(new XYChart.Data(conditionpressure, thval));

		ind = 0;
		t1 = System.currentTimeMillis();

		new Thread(new Runnable() {

			@Override
			public void run() {

				Mycommand.setDelay(delayinterval, 500);
				Mycommand.sendAdcEnableBits("111111", 1000);
				Mycommand.startADC(2500);

			}
		}).start();

	}
	void createCsvTableBubble() {

		Myapp.uid="N0049";
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

			cs.wtirefile(f.getPath() + "/" + Myapp.sampleid + "_" + findInt(ff) + ".csv");

			cs.firstLine("wvtr");
			cs.newLine("testname", "wvtr");
			cs.newLine("sample", Myapp.sampleid);
			cs.newLine("thikness", Myapp.thikness);

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
			//cs.newLine("crosssection", Myapp.crossection);
			cs.newLine("materialtype", Myapp.materialtype);
			// cs.newLine("tfact", Myapp.tfactore);
			cs.newLine("splate", Myapp.splate);

			cs.newLine("testdata",Myapp.testdata);
			cs.newLine("datatype",Myapp.dataint);
			cs.newLine("testtype",Myapp.testtype);
			cs.newLine("method",Myapp.testmethod);
			
			cs.newLine("tChamber", tChamber);
			cs.newLine("hChamber", hChamber);
			cs.newLine("tBottom", tBottom);
			cs.newLine("hBottom", hBottom);
			cs.newLine("tTop", tTop);
			cs.newLine("hTop", hTop);
			cs.newLine("t", tlist);
			cs.newLine("svp", svp);
			cs.newLine("avp", avp);
			cs.newLine("mass", mass);
			

			
		
			List<String> tt=new ArrayList<String>(tlist);
			List<String> ma=new ArrayList<String>(mass);
			
			getWetFlowSmooth(tt, ma, 50000, 1);
	
			cs.newLine("timeline",tt);
			cs.newLine("massline",ma);
			
			
			double calTime=Double.parseDouble(tlist.get(tlist.size()/2));
			double calG=getFlowPointOn(tlist,mass, 50000, 1,calTime);
			double area=(double)(3.14*calculationdia*calculationdia)/4;
			double wvtr=(double)(calG/calTime)/area;
			
			
			System.out.println("Calculation\nCalTime : "+calTime+"\nCalG : "+calG+"\narea : "+area+"\nWvtr : "+wvtr);
			
			
			double svpp=Double.parseDouble(svp.get(svp.size()/2));
			double h22=Double.parseDouble(hBottom.get(hBottom.size()/2));
			double h33=Double.parseDouble(hChamber.get(hChamber.size()/2));
			double per=wvtr/(svpp*(h22-h33));
			
			
			double permiability=per*Double.parseDouble(Myapp.thikness);
			
			cs.newLine("permeance",""+per);
			cs.newLine("wvtr",""+wvtr);
			cs.newLine("diameter",calculationdia+"");
			cs.newLine("permiability",permiability+"");
					
			
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

	void setPoints(double h1, double h2, double h3, double tt1, double tt2, double tt3) {
		System.out.println("H1 : " + h1);
		System.out.println("H2 : " + h2);
		System.out.println("H3 : " + h3);
		System.out.println("T1 : " + tt1);
		System.out.println("T2 : " + tt2);
		System.out.println("T3 : " + tt3);

		double h = h1;
		double t = tt1;

		// running condition 1

		//stop condition
		
		if (testtime != 1) {
			double an = (double) ((System.currentTimeMillis() - testtime) / 1000);
			if (an > (60 * 60)) {

				System.out.println("End condition time");
				stopTest();
				
				return;
			}
		}
		if(tTop.size()>=maxDatapoint)
		{
			System.out.println("End condition points");
			stopTest();
			
		}
		
		
		showOnGraph(h1, h2, h3, tt1, tt2, tt3);
		//start condition

//		if (t >= tmin && t <= tmax && h >= hmin && h <= hmax) {
//			testtime=1;
//			runningCondition = 0;
//			showOnGraph(h1, h2, h3, tt1, tt2, tt3);
//		} else if (t < tmin && runningCondition == 0) {
//			runningCondition = 1;
//			Mycommand.valveOn('2', 0);
//			Mycommand.valveOn('4', 1200);
//		} else if (t > tmax && runningCondition == 0) {
//			runningCondition = 2;
//			Mycommand.valveOn('4', 0);
//		} else if (h < hmin && runningCondition == 0) {
//			runningCondition = 3;
//			Mycommand.valveOn('3', 0);
//			Mycommand.valveOn('4', 1200);
//
//		} else if (h > hmax && runningCondition == 0) {
//			runningCondition = 4;
//			Mycommand.valveOn('2', 0);
//			Mycommand.valveOn('4', 1200);
//		}
//
//		// ......
//
//		if (t >= tmin && runningCondition == 1) {
//			// sd
//			Mycommand.valveOff('2', 0);
//			Mycommand.valveOff('4', 1200);
//			runningCondition = 0;
//		}
//
//		if (t <= tmax && runningCondition == 2) {
//			Mycommand.valveOff('4', 0);
//			runningCondition = 0;
//		
//		}
//
//		if (h >= hmin && runningCondition == 3) {
//			// sd
//			Mycommand.valveOff('3', 0);
//			Mycommand.valveOff('4', 1200);
//			runningCondition = 0;
//		}
//
//		if (h <= hmax && runningCondition == 4) {
//			Mycommand.valveOff('2', 0);
//			Mycommand.valveOff('4', 1200);
//			runningCondition = 0;
//		}

	}

	void showOnGraph(double h1, double h2, double h3, double tt1, double tt2, double tt3) {

		if (tempt1 == 0) {
			tempt1 = System.currentTimeMillis();
		}

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				double time = getTime();
				double t = time / 60;
				System.out.println("Min : " + t);
				
				

				double svp1=0.854*Math.getExponent(0.05*tt3);
				double avp1=h3*svp1;
				double mass1=(avp1*1000)/(461.5*(tt3+273.15));
				
				
				tTop.add(""+tt1);
				hTop.add(""+h1);
				
				tBottom.add(""+tt2);
				hBottom.add(""+h2);
				
				tChamber.add(""+tt3);
				hChamber.add(""+h3);
				
				svp.add(""+svp1);
				avp.add(""+avp1);
				mass.add(""+mass1);
				tlist.add(""+time);
				
				
				series2.getData().add(new XYChart.Data(t, mass1));
				flowserireswet.getData().add(new XYChart.Data(t, tt3));
				pressureserireswet.getData().add(new XYChart.Data(t, h3));
				
//				series2.getData().add(new XYChart.Data(t, tt3));
//				series1.getData().add(new XYChart.Data(t, h3));
//				flowserires.getData().add(new XYChart.Data(t, tt1));
//				flowserireswet.getData().add(new XYChart.Data(t, h1));
//				pressureserires.getData().add(new XYChart.Data(t, tt2));
//				pressureserireswet.getData().add(new XYChart.Data(t, h2));
			}
		});

	}

	void stopTest() {
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				testtype=5;
				starttest.setDisable(false);
				status.setText("Test hase been Stop");
			
				starttestdry.setDisable(false);
				starttestwet.setDisable(true);
				starttest.setDisable(false);
				autotest.setDisable(false);

				Mycommand.stopADC(0);
				Mycommand.valveOff('2', 1000);
				Mycommand.valveOff('3', 2000);
				Mycommand.valveOff('4', 3000);

				createCsvTableBubble();
			
				clearList();
			}
		});

		

	}

	void clearList() {
		tChamber.clear();
		hChamber.clear();
		tBottom.clear();
		hBottom.clear();
		tTop.clear();
		hTop.clear();
		tlist.clear();
		svp.clear();
		avp.clear();
		mass.clear();
	}

	// setting plate value ..
	void setPlateval() {
		if (Myapp.splate.equals("Small")) {
			calculationdia = 40;
		} else if (Myapp.splate.equals("Large")) {

			calculationdia = 80;
		} else {

			calculationdia = 60;
		}
	}

	// set all shortcut
	void addShortCut() {
		KeyCombination backevent = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_ANY);

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
		// Myapp.testtrial = "4";
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
					String temp = ss.get(i).toString().substring(0, ss.get(i).indexOf("."));
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

					String temp = ss.get(i).toString().substring(0, ss.get(i).indexOf("."));
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
		 * if (round == 2) { r = (double) Math.round(r * 100) / 100; } else if (round ==
		 * 3) { r = (double) Math.round(r * 1000) / 1000;
		 * 
		 * } else { r = (double) Math.round(r * 10000) / 10000;
		 * 
		 * }
		 */
		r = (double) Math.round(r * 100) / 100;

		return r + "";

	}

	// set main graphs....
	void setGraph() {
		root.getChildren().add(sc);
		// .pressure_max = Integer.parseInt(Myapp.endpress);
		sc.setAxisSortingPolicy(SortingPolicy.Y_AXIS.NONE);
		sc.setAxisSortingPolicy(SortingPolicy.X_AXIS.NONE);

		sc.setAnimated(false);
		sc.setLegendVisible(false);
		yAxis.setLabel("WVTR");
		xAxis.setLabel("Time");
		sc.setCreateSymbols(true);
		series1.setName("Temp (°C)");
		series2.setName("%rH");
		sc.getData().addAll(series1, series2);

		// sc.setTitle("Flow Vs Pressure");

		sc.prefWidthProperty().bind(root.widthProperty());
		sc.prefHeightProperty().bind(root.heightProperty());

		// xAxis.setUpperBound(conditionpressure);
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
		sc1.getData().addAll(flowserireswet, flowserires);
		sc1.getStylesheets().add(getClass().getResource("dynamicgraph2.css").toExternalForm());
		// sc1.setTitle("Flow Vs Time");

		yAxis1.setLabel("Temperature");
		xAxis1.setLabel("Time");

		sc1.prefWidthProperty().bind(root1.widthProperty());
		sc1.prefHeightProperty().bind(root1.heightProperty());

		root2.getChildren().add(sc2);
		sc2.setAxisSortingPolicy(SortingPolicy.Y_AXIS.NONE);
		sc2.setAnimated(false);
		sc2.setLegendVisible(false);
		sc2.setCreateSymbols(true);
		// pressureserires.setName("Pressure-test");
		sc2.getData().addAll(pressureserireswet, pressureserires);

		yAxis2.setLabel("Humidity");
		xAxis2.setLabel("Time");
		// sc2.setTitle("Pressure Vs Time");
		sc2.prefWidthProperty().bind(root2.widthProperty());
		sc2.prefHeightProperty().bind(root2.heightProperty());
		sc2.getStylesheets().add(getClass().getResource("dynamicgraph2.css").toExternalForm());

		Zoom zoom = new Zoom(sc2, root2);

		Zoom zoom1 = new Zoom(sc1, root1);
	}

	// set pressure and flow gauges
	void setGauges() {
		HBox v = new HBox(10);

		v.setMinWidth(guages.getPrefWidth());
		v.setMaxHeight(guages.getPrefHeight());
		v.setMaxWidth(guages.getPrefWidth());

		gauge5 = TileBuilder.create().skinType(SkinType.BAR_GAUGE)
				// .prefSize(TILE_WIDTH, TILE_HEIGHT)
				.minValue(0)

				.barBackgroundColor(Color.GRAY).backgroundColor(Color.valueOf("#f1f1f1"))
				.maxValue(Integer.parseInt(DataStore.getH())).startFromZero(true).thresholdVisible(false)
				.title("Humidity ").unit("%RH")

				.textColor(Color.GRAY).unitColor(Color.GRAY).titleColor(Color.GRAY).valueColor(Color.GRAY)
				.gradientStops(new Stop(0, Bright.YELLOW_ORANGE),
						new Stop((double)100*Integer.parseInt(DataStore.getHmin())/Integer.parseInt(DataStore.getH()), Bright.GREEN_YELLOW), 
						new Stop((double)100*Integer.parseInt(DataStore.getHmax())/Integer.parseInt(DataStore.getH()), Bright.GREEN_YELLOW),
						new Stop(1, Bright.YELLOW_ORANGE))
				.strokeWithGradient(true).animated(true).build();

		gauge = TileBuilder.create().skinType(SkinType.BAR_GAUGE)
				// .prefSize(TILE_WIDTH, TILE_HEIGHT)
				.minValue(0).barBackgroundColor(Color.GRAY).backgroundColor(Color.valueOf("#f1f1f1"))
				.maxValue(Integer.parseInt(DataStore.getT())).startFromZero(true).thresholdVisible(false)
				.title("Temperature").textColor(Color.GRAY).unitColor(Color.GRAY).titleColor(Color.GRAY)
				.valueColor(Color.GRAY).unit("°C")
				.gradientStops(new Stop(0, Bright.YELLOW_ORANGE),
						new Stop((double)100*Integer.parseInt(DataStore.getTmin())/Integer.parseInt(DataStore.getT()), Bright.GREEN_YELLOW), 
						new Stop((double)100*Integer.parseInt(DataStore.getTmax())/Integer.parseInt(DataStore.getT()), Bright.GREEN_YELLOW),
						new Stop(1, Bright.YELLOW_ORANGE))
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
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

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
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

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
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
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
		btninfo.getStyleClass().add("transperant_comm");

		btninfo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				testinfo();

			}
		});
		
		
		btnabr.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				testabourd();
			}
		});

	}

	// set test stop popup
		void testinfo() {

			mydia = new MyDialoug(Main.mainstage, "/userinput/Testinfopopup.fxml");

			mydia.showDialoug();

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
				node.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {

					private static final int X_OFFSET = 15;
					private static final int Y_OFFSET = -5;
					Label label = new Label();

					@Override
					public void handle(final MouseEvent event) {
						// System.out.println("MOuse Event");
						final String colorString = "#cfecf0";
						label.setFont(new Font(20));
						popup.getContent().setAll(label);
						label.setStyle(
								"-fx-background-color: " + colorString + "; -fx-border-color: " + colorString + ";");
						label.setText("x=" + data.getXValue() + ", y=" + data.getYValue());
						popup.show(data.getNode().getScene().getWindow(), event.getScreenX() + X_OFFSET,
								event.getScreenY() + Y_OFFSET);
						event.consume();
					}

					public EventHandler<MouseEvent> init() {
						label.getStyleClass().add("chart-popup-label");
						return this;
					}

				}.init());

				node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {

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

					if (readData.get(i) == 'F' && readData.get(i + 1) == (int) 'M'
							&& readData.get(i + 2) == (int) 'A') {
						double h1 = 0, h2 = 0, h3 = 0, t1 = 0, t2 = 0, t3 = 0;

						List<Integer> reading = getAdcData(readData);

						h1 = (double) reading.get(0) * Integer.parseInt(DataStore.getH()) / 65535;
						h2 = (double) reading.get(1) * Integer.parseInt(DataStore.getH()) / 65535;
						h3 = (double) reading.get(2) * Integer.parseInt(DataStore.getH()) / 65535;

						t1 = (double) reading.get(3) * Integer.parseInt(DataStore.getT()) / 65535;
						t2 = (double) reading.get(4) * Integer.parseInt(DataStore.getT()) / 65535;
						t3 = (double) reading.get(5) * Integer.parseInt(DataStore.getT()) / 65535;

						System.out.println("" + reading);

						// System.out.println("Pr1 : " + pr + "\nFc : " + fl);

						DataStore.liveflow.set(h1);

						DataStore.livepressure.set(t1);

						if (testtype == 0) {

							setPoints(h1, h2, h3, t1, t2, t3);

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

	
	// csv create function
	public double getFlowPointOn(List<String> pressure, List<String> flow,int range, int degree,double pressurePoint)
	{ 
	double newFlow=0;

	List<Double> xx = new ArrayList<Double>();
	List<Double> yy = new ArrayList<Double>();

	for (int i = 0; i < pressure.size(); i++) {
		xx.add(Double.parseDouble(pressure.get(i)));
		yy.add(Double.parseDouble(flow.get(i)));
	}

	Map<Integer, Double> peaks = new HashMap<Integer, Double>();

	Map<Integer, Double> xpoints = new HashMap<Integer, Double>();
	for (int i = 1; i < xx.size() - 1; i++) {

		if (yy.get(i) > yy.get(i - 1) && yy.get(i) > yy.get(i + 1)) {

			int n = (int) Math.abs(yy.get(i) - yy.get(i - 1));
			int n1 = (int) Math.abs(yy.get(i) - yy.get(i + 1));
			if (n > range && n1 > range) {
				peaks.put(i, yy.get(i));
				xpoints.put(i, xx.get(i));
				System.out.println(i + " - Peak up : " + n + " y[i] : "
						+ yy.get(i - 1) + "-" + yy.get(i) + "-"
						+ yy.get(i + 1));
			}

		} else if (yy.get(i) < yy.get(i - 1) && yy.get(i) < yy.get(i + 1)) {

			int n = (int) Math.abs(yy.get(i) - yy.get(i - 1));
			int n1 = (int) Math.abs(yy.get(i) - yy.get(i + 1));
			if (n > range && n1 > range) {
				peaks.put(i, yy.get(i));
				xpoints.put(i, xx.get(i));
				System.out.println(i + " - Peak up : " + n + " y[i] : "
						+ yy.get(i - 1) + "-" + yy.get(i) + "-"
						+ yy.get(i + 1));
			}

		}
	}

	// System.out.println("Original : "+xx);
	List<Integer> pp = new ArrayList<Integer>(peaks.keySet());

	for (int i = 0; i < pp.size(); i++) {

		int mm = pp.get(i) - i;
	//	System.out.println("Removing : " + yy.get(mm));

		yy.remove(mm);
		xx.remove(mm);
	}

	System.out.println(yy.size() + " : " + xx.size());

	if (xx.size() <= degree) {
		degree = xx.size();
	}

	PolynomialRegression p = new PolynomialRegression(xx, yy, degree, "n");
	double[] re = p.polyfit();

	for (int i = 0; i < peaks.size(); i++) {

		double xpoint = xpoints.get(pp.get(i));

		System.out.println("" + pp.get(i) + "- >" + xpoint);
		double ypoint = p.polynomial(xpoint, re);

		xx.add(pp.get(i), xpoint);
		yy.add(pp.get(i), ypoint);

	}

	
		newFlow = p.polynomial(pressurePoint, re);

	System.out.println("New flow point :  "+newFlow+" at pressure : "+pressurePoint);
	return newFlow;
	}

	public void getWetFlowSmooth(List<String> pressure, List<String> flow,
			int range, int degree) {

		List<Double> xx = new ArrayList<Double>();
		List<Double> yy = new ArrayList<Double>();

		for (int i = 0; i < pressure.size(); i++) {
			xx.add(Double.parseDouble(pressure.get(i)));
			yy.add(Double.parseDouble(flow.get(i)));
		}

		Map<Integer, Double> peaks = new HashMap<Integer, Double>();

		Map<Integer, Double> xpoints = new HashMap<Integer, Double>();
		for (int i = 1; i < xx.size() - 1; i++) {

			if (yy.get(i) > yy.get(i - 1) && yy.get(i) > yy.get(i + 1)) {

				int n = (int) Math.abs(yy.get(i) - yy.get(i - 1));
				int n1 = (int) Math.abs(yy.get(i) - yy.get(i + 1));
				if (n > range && n1 > range) {
					peaks.put(i, yy.get(i));
					xpoints.put(i, xx.get(i));
					System.out.println(i + " - Peak up : " + n + " y[i] : "
							+ yy.get(i - 1) + "-" + yy.get(i) + "-"
							+ yy.get(i + 1));
				}

			} else if (yy.get(i) < yy.get(i - 1) && yy.get(i) < yy.get(i + 1)) {

				int n = (int) Math.abs(yy.get(i) - yy.get(i - 1));
				int n1 = (int) Math.abs(yy.get(i) - yy.get(i + 1));
				if (n > range && n1 > range) {
					peaks.put(i, yy.get(i));
					xpoints.put(i, xx.get(i));
					System.out.println(i + " - Peak up : " + n + " y[i] : "
							+ yy.get(i - 1) + "-" + yy.get(i) + "-"
							+ yy.get(i + 1));
				}

			}
		}

		System.out.println("Original : " + yy);
		// System.out.println("Original : "+xx);
		List<Integer> pp = new ArrayList<Integer>(peaks.keySet());

		for (int i = 0; i < pp.size(); i++) {

			int mm = pp.get(i) - i;
			System.out.println("Removing : " + yy.get(mm));

			yy.remove(mm);
			xx.remove(mm);
		}

		System.out.println(yy.size() + " : " + xx.size());

		if (xx.size() <= degree) {
			degree = xx.size();
		}

		PolynomialRegression p = new PolynomialRegression(xx, yy, degree, "n");
		double[] re = p.polyfit();

		for (int i = 0; i < peaks.size(); i++) {

			double xpoint = xpoints.get(pp.get(i));

			System.out.println("" + pp.get(i) + "- >" + xpoint);
			double ypoint = p.polynomial(xpoint, re);

			xx.add(pp.get(i), xpoint);
			yy.add(pp.get(i), ypoint);

		}

		
		flow.clear();

		for (int i = 0; i < xx.size(); i++) {
			double xpoint = xx.get(i);
			double ypoint = p.polynomial(xpoint, re);

			flow.add("" + ypoint);
		}

	}


	// show result popup
	void showResultPopup() {
		Platform.runLater(new Runnable() 
		{
			
			@Override
			public void run() 
			{
				tones.play();
				mydia = new MyDialoug(Main.mainstage, "/userinput/popupresult.fxml");
				mydia.showDialoug();				
			}
		});
	}

	// show start test popup
	void showBubblePopup() {
		mydia = new MyDialoug(Main.mainstage, "/userinput/Wetpopup.fxml");

		mydia.showDialoug();
	}

}
