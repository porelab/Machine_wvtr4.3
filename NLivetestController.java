package userinput;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.LineChart.SortingPolicy;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import toast.MyDialoug;
import toast.Openscreen;
import Notification.Notification;
import Notification.Notification.Notifier;
import Notification.NotificationBuilder;
import Notification.NotifierBuilder;
import application.DataStore;
import application.Main;
import application.Myapp;
import application.SerialCommunicator.SerialWriter;
import application.writeFormat;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;

import data_read_write.CalculatePorometerData;
import data_read_write.CsvWriter;
import de.tesis.dynaware.javafx.fancychart.zoom.Zoom;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import extrafont.Myfont;

public class NLivetestController implements Initializable {

	@FXML
	Rectangle manualblock;

	boolean isAuto = true;

	List<String> p1list, p2list, daltatlist, flowlist, bans, daltaplist,
			darcylist, wetplist, wetflist, dryplist, dryflist;

	ListChangeListener<Double> bubblelistener1;
	int skip;
	MyDialoug mydia;
	// for start popup
	static SimpleBooleanProperty stprop = new SimpleBooleanProperty(false);
	static SimpleBooleanProperty stpropcan = new SimpleBooleanProperty(false);

	@FXML
	private Button btnabr, btnrefrace, btnstart, starttestdry, starttest,
			stoptest, starttestwet, startautotest;

	int thval = 500;
	int delayinauto=1500;

	@FXML
	Label lbltestnom, lbltestdurationm, para;

	public static JFXDialog df;
	public static JFXDialog df1;
	@FXML
	AnchorPane guages, ancregu1, ancregu2, ancpg1, ancpg2, ancpg3, ancpg4;

	
	
	@FXML
	AnchorPane root,mainroot;

	private Gauge gauge;
	private Gauge gauge5;

	@FXML
	JFXToggleButton autotest;

	@FXML
	ListView<String> lvtest, lvbp, lvp;

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
	void addShortCut() {
		 KeyCombination backevent = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_ANY);
			
			mainroot.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent ke) {
					
					if(backevent.match(ke))
					{
						testabourd();
						}
					
				}
			});

			

	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		addShortCut();
		Myapp.testtrial = "4";
		trails = Integer.parseInt(Myapp.testtrial);
		conditionflow = (double) Double.parseDouble(DataStore.getFm2()) * 0.80;
		conditionpressure = Double.parseDouble(Myapp.endpress);
		
		if (Myapp.splate.equals("Small")) {
			calculationdia = 2.25;
		} else if (Myapp.splate.equals("Large")) {

			calculationdia = 2.25;
		} else {

			calculationdia = 2.25;
		}

		setButtons();
		setGauges();
		setGraph();
		setGraph12();

		// setTableList();

		
		startautotest.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
			
				autotest.setDisable(true);
				bubbleClick();
			}
		});
		
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

		starttestdry.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				autotest.setDisable(true);
				dryClick(0);
			}
		});

		starttestwet.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				wetClick(0);
			}
		});

		stoptest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				writeFormat wrD = new writeFormat();
				wrD.stopT();
				wrD.addBlank(4);
				wrD.addChkSm();
				wrD.addLast();

				sendData(wrD);
				starttestdry.setDisable(false);
				starttestwet.setDisable(true);
				starttest.setDisable(false);
				// DataStore.intList.get("70").removeListener(bubblelistener);

				autotest.setDisable(false);
				System.out.println("CSV - >>>>>>>>>>>>>>>>>>>> "
						+ p1list.size());

				if (p1list.size() > 0) {
					createCsvTableBubble();
					p1list.clear();
					p2list.clear();
					daltaplist.clear();
					daltatlist.clear();
					bans.clear();
					flowlist.clear();
				}

				DataStore.intList.get("70").removeListener(bubblelistener1);

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

		starttest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				autotest.setDisable(true);
				bubbleClick();

			}
		});

	}

	void startWettest(int delay) {

		wrd = new writeFormat();
		wrd.addStart();
		wrd.addChar('W');

		wrd.addBlank(3);
		wrd.addChkSm();
		wrd.addLast();
		sendData(wrd,delay);
		series1.getData().clear();
		series2.getData().clear();

		ind = 0;
		yAxis.setLabel("Flow");
		DataStore.intList.get("80").clear();
		DataStore.intList.get("70").clear();

		setWettestListener();
		DataStore.intList.get("70").addListener(bubblelistener1);

	}

	void bubbleClick() {
		flowserireswet.getData().clear();
		pressureserireswet.getData().clear();

		starttestdry.setDisable(true);
		flowserires.getData().clear();
		pressureserires.getData().clear();
		para.setText("bubble test running..");
		p1list.clear();
		p2list.clear();
		daltatlist.clear();
		flowlist.clear();
		bans.clear();
		daltaplist.clear();
		darcylist.clear();

		skip = 5;
		yAxis.setLabel("F/PT");
		xAxis.setLabel("Time");

		tempt1 = System.currentTimeMillis();
		setBubbleListener1();
		startBubblepoint1(testno);
		starttest.setDisable(true);
	}

	void dryClick(int delay) {
		flowserires.getData().clear();
		pressureserires.getData().clear();
		dryplist.clear();
		dryflist.clear();
		darcylist.clear();
	
		skip = 1;
		darcyavg = 0;
		setPermiabilityListener();
		startPermeability(delay);

		tempt1 = System.currentTimeMillis();
		starttestdry.setDisable(true);
	}

	void wetClick(int delay) {

		flowserireswet.getData().clear();
		pressureserireswet.getData().clear();

		flowserires.getData().clear();
		pressureserires.getData().clear();
		starttestwet.setDisable(true);
		wetflist.clear();
		wetplist.clear();
		skip = 1;
		yAxis.setLabel("Flow");

		xAxis.setLabel("Pressure");
		startWettest(delay);

		tempt1 = System.currentTimeMillis();

	}

	void startPermeability(int delay) {

		sendSetting(1000);

		
		
					
					wrd = new writeFormat();
					wrd.addStart();
					wrd.addChar('D');
					yAxis.setLabel("Flow");

					xAxis.setLabel("Pressure");
					wrd.addBlank(3);
					wrd.addChkSm();
					wrd.addLast();
					sendData(wrd, 2500);
					series2.getData().clear();

					dryplist.add(0,"0.0");
					dryflist.add(0,"0.0");
					series2.getData().add(new XYChart.Data(0, 0));
					
					ind = 0;

					DataStore.intList.get("80").clear();
					DataStore.intList.get("70").clear();

					DataStore.intList.get("70").addListener(bubblelistener1);

				
				
				

	}

	void setPermiabilityListener() {
		series2.getData().clear();
		System.out.println("Test will be stop on\nPressure : "
				+ DataStore.pressure_max + "\nor\nFlow : "
				+ (Double.parseDouble(DataStore.getFm1()) * 0.8));
		bubblelistener1 = new ListChangeListener<Double>() {

			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends Double> arg0) {

				curflow = (double) DataStore.intList.get("70").get(ind);
				curpre = (double) DataStore.intList.get("80").get(ind);

				if (curpre < conditionpressure && curflow < conditionflow) {

					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							series2.getData().add(
									new XYChart.Data(curpre, curflow));
							dryplist.add("" + curpre);
							dryflist.add("" + curflow);
							darcylist.add("" + getDarcy(curpre, curflow));
							ind++;
							flowserires.getData().add(
									new XYChart.Data(getTime(), curflow));
							pressureserires.getData().add(
									new XYChart.Data(getTime(), curpre));

						}
					});
				} else {

					System.out.println("Stop is tirgger ");

					DataStore.intList.get("70").removeListener(bubblelistener1);

					wrd = new writeFormat();
					wrd.addStop();
					wrd.addChar('D');
					wrd.addBlank(4);
					wrd.addChkSm();
					wrd.addLast();
					sendData(wrd);

					
					try{

						autotest.setDisable(false);
						if(dryflist.size()>0&&wetflist.size()>0)
						{
					createCsvTable();
						}
						else
						{
							System.out.println("NO file created");
							
						}
							
					}
					catch(Exception e){
					}
					writeFormat wrD = new writeFormat();
					wrD.stopT();
					wrD.addBlank(4);
					wrD.addChkSm();
					wrD.addLast();
					sendData(wrD, 1000);

					setDataPointPopup(sc);

				}

			}
		};
	}

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

	double getDarcyAvg() {
		double ans;

		ans = (double) darcyavg / darcylist.size();

		ans = (double) Math.round(ans * 100) / 100;

		return ans;
	}

	void setWettestListener() {
		series1.getData().clear();
		System.out.println("Test will be stop on\nPressure : "
				+ DataStore.pressure_max + "\nor\nFlow : "
				+ (Double.parseDouble(DataStore.getFm1()) * 0.8));
		bubblelistener1 = new ListChangeListener<Double>() {

			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends Double> arg0) {

				curflow = (double) DataStore.intList.get("70").get(ind);
				curpre = (double) DataStore.intList.get("80").get(ind);

				if (curpre < conditionpressure && curflow < conditionflow) {

					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							series1.getData().add(
									new XYChart.Data(curpre, curflow));
							wetplist.add("" + curpre);
							wetflist.add("" + curflow);
							ind++;

							flowserireswet.getData().add(
									new XYChart.Data(getTime(), curflow));
							pressureserireswet.getData().add(
									new XYChart.Data(getTime(), curpre));

						}
					});
				} else {

					System.out.println("Stop is tirgger \nCurrent : " + curflow
							+ " - " + curpre + "\nCondition : " + conditionflow
							+ " - " + conditionpressure);

					DataStore.intList.get("70").removeListener(bubblelistener1);

					wrd = new writeFormat();
					wrd.addStop();
					wrd.addChar('W');
					wrd.addBlank(4);
					wrd.addChkSm();
					wrd.addLast();
					sendData(wrd);
					
					if(isAuto)
					{
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub

								try{
									Thread.sleep(10000);
								dryClick(5000);	
								}
								catch(Exception e)
								{
									
								}
							}
						});
					}

					setDataPointPopup(sc);

					starttestdry.setDisable(false);
				}

			}
		};
	}

	void createCsvTableBubble() {

		try {
			System.out.println("csv creating........");
			CsvWriter cs = new CsvWriter();

			Date d1 = new Date();
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
			String date1 = DATE_FORMAT.format(d1);

			File fff = new File("TableCsvs_bubble");
			if (!fff.exists()) {
				fff.mkdir();
			}

			File f = new File(fff.getPath() + "/" + date1);
			if (!f.isDirectory()) {
				f.mkdir();
				System.out.println("Dir csv folder created");
			}

			File[] ff = f.listFiles();

			cs.wtirefile(f.getPath() + "/test_" + Myapp.sampleid + ""
					+ ff.length + ".csv");

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
			String durr = hour + ":" + min + ":" + remsec;

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

			cs.closefile();
			System.out.println("csv Created");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	double getTime() {
		double an = (double) ((System.currentTimeMillis() - tempt1) / 1000);
		return an;
	}

	void setBubbleListener1() {
		bubblelistener1 = new ListChangeListener<Double>() {

			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends Double> arg0) {

				if (ind > skip) {

					p2 = (double) DataStore.intList.get("80").get(ind);
					t2 = System.currentTimeMillis();

					if (p2 < conditionpressure) {

						if (p2 != 0 && p1 != p2) {

							double deltap = (double) p2 - p1;
							double deltat = (double) (t2 - t1) / 1000;

							/*
							 * if(deltat>2) { deltat=1.001; }
							 */
							double ans;

							System.out.println("Record Number : " + ind);
							ans = (double) (DataStore.intList.get("70")
									.get(ind) * deltat) / deltap;

							p1list.add("" + p1);
							p2list.add("" + p2);
							daltaplist.add("" + deltap);
							daltatlist.add("" + deltat);
							flowlist.add(""
									+ DataStore.intList.get("70").get(ind));
							bans.add("" + ans);

							System.out.println("Flow : "
									+ DataStore.intList.get("70").get(ind)
									+ "\nP1 : " + p1 + "\nP2 : " + p2
									+ "\nT1 :" + t1 + "\n T2 : " + t2);
							System.out.println("Delta P : " + deltap
									+ " \nDelta T : " + (deltat));
							System.out.println("Answer F/PT : " + ans);

							if (ans > 0) {
								// if(ans<Double.parseDouble(DataStore.thresoldvalue))

								if (ans < thval) {
									Platform.runLater(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											// System.out.println("Size of list is : "+DataStore.intList.get(80).size()+" ....  INDEX : "+ind);
											series2.getData()
													.add(new XYChart.Data(
															
																	getTime(),
															ans));

											
											
											series1.getData().remove(1);
											series1.getData().add(new XYChart.Data(getTime()+10, thval));

											
											
											flowserires
													.getData()
													.add(new XYChart.Data(
															getTime(),
															(double) DataStore.intList
																	.get("70")
																	.get(ind)));
											pressureserires
													.getData()
													.add(new XYChart.Data(
															getTime(),
															(double) DataStore.intList
																	.get("80")
																	.get(ind)));

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
																getTime(),
																ans));
												series1.getData().remove(1);
												series1.getData().add(new XYChart.Data(getTime()+10, thval));

												flowserires
														.getData()
														.add(new XYChart.Data(
																getTime(),
																(double) DataStore.intList
																		.get("70")
																		.get(ind)));
												pressureserires
														.getData()
														.add(new XYChart.Data(
																getTime(),
																(double) DataStore.intList
																		.get("80")
																		.get(ind)));
												// bbp=(double)DataStore.intList.get("80").get(DataStore.intList.get("80").size()-4);
												// bbf=(double)DataStore.intList.get("70").get(DataStore.intList.get("70").size()-4);

												bbp = (double) DataStore.intList
														.get("80")
														.get(DataStore.intList
																.get("80")
																.size() - 3);
												bbf = (double) DataStore.intList
														.get("70")
														.get(DataStore.intList
																.get("70")
																.size() - 1);

												System.out.println("Pressure points : "
														+ DataStore.intList
																.get("80"));
												System.out
														.println("BBBBBBPPPPPPPPPP : "
																+ bbp);
												System.out.println("Index : "
														+ ind);

												System.out
														.println("Completedd test number "
																+ testno
																+ " in "
																+ ((t2test - t1test) / 1000)
																+ " seconds ");

												ind++;

												String bubblepoint = getBubbledia(bbp);
												bbd = Double
														.parseDouble(bubblepoint);

												Myapp.bps.put("" + bbp, ""
														+ bubblepoint);

												createCsvTableBubble();

												wrd = new writeFormat();
												wrd.addStop();
												wrd.addChar('B');
												wrd.addChar('P');
												wrd.addBlank(3);
												wrd.addChkSm();
												wrd.addLast();
												sendData(wrd);

												if(isAuto)
												{
													Platform.runLater(new Runnable() {
														
														@Override
														public void run() {
														

															wetClick(delayinauto);
														}
													});
												}
												starttestwet.setDisable(false);

											}

										});

										DataStore.intList
												.get("70")
												.removeListener(bubblelistener1);

									} else {
										Platform.runLater(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												
												series2.getData()
														.add(new XYChart.Data(
																getTime(),
																ans));
												series1.getData().remove(1);
												series1.getData().add(new XYChart.Data(getTime()+10, thval));

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

						DataStore.intList.get("70").removeListener(
								bubblelistener1);

						wrd = new writeFormat();
						wrd.addStop();
						wrd.addChar('B');
						wrd.addChar('P');
						wrd.addBlank(3);
						wrd.addChkSm();
						wrd.addLast();
						sendData(wrd);

						writeFormat wrD = new writeFormat();
						wrD.stopT();
						wrD.addBlank(4);
						wrD.addChkSm();
						wrD.addLast();

						sendData(wrD, 1000);

						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								para.setText("No Bubble Point found");

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

						writeFormat wrD = new writeFormat();
						wrD.addChar('V');
						wrD.addChar('6');
						wrD.addChar('0');
						wrD.addChkSm();
						wrD.addLast();
						sendData(wrD);
					}

				}

			}
		};
	}

	void setLabelFont() {
		lbltestdurationm.setFont(f.getM_M());
		lbltestnom.setFont(f.getM_M());

	}

	void setListenerForStartTestPopup() {
		stprop.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				System.out.println(arg2 + "");
				if (arg2) {
					starttest.setVisible(false);
					// startBubblepoint(testno);
					skip = 5;
					startBubblepoint1(testno);

					stprop.set(false);
				}

			}
		});

		stpropcan.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				System.out.println(arg2 + "");
				if (arg2) {
					starttest.setVisible(true);
					stpropcan.set(false);
				}

			}
		});

	}

	void startBubble(int no, boolean popup) {

		if (no != 0) {

			if (popup) {
				starttest.setVisible(true);
				starttest.setText("Start Test " + testno);
				System.out.println("Number :" + no);
				conpopup();

				System.out.println("Number minus");

			}

		} else {

			writeFormat wrD = new writeFormat();
			wrD.stopT();
			wrD.addBlank(4);
			wrD.addChkSm();
			wrD.addLast();
			sendData(wrD);

			Openscreen.open("/application/first.fxml");

		}
	}

	void createCsvTable() {

		try {
			System.out.println("csv creating........");
			CsvWriter cs = new CsvWriter();

			Date d1 = new Date();
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
			String date1 = DATE_FORMAT.format(d1);

			File fff = new File("TableCsvs");
			if (!fff.exists()) {
				fff.mkdir();
			}

			File f = new File(fff.getPath() + "/" + Myapp.sampleid);
			if (!f.isDirectory()) {
				f.mkdir();
				System.out.println("Dir csv folder created");
			}

			File[] ff = f.listFiles();

			CalculatePorometerData c = new CalculatePorometerData();
			
			cs.wtirefile(f.getPath() + "/test_" + Myapp.sampleid + ""
					+ ff.length + ".csv");

			cs.firstLine("porometer");
			cs.newLine("testname", "porometer");
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
			String durr = hour + ":" + min + ":" + remsec;

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

			cs.newLine("bpressure", bbp + "");
			cs.newLine("bdiameter", "" + bbd);
			cs.newLine("bubble flow", flowlist);
			cs.newLine("Dt", daltatlist);
			cs.newLine("p1", p1list);
			cs.newLine("p2", p2list);
			cs.newLine("Dp", daltaplist);
			cs.newLine("fpt", bans);

			cs.newLine("darcy avg", "" + getDarcyAvg());
			cs.newLine("dpressure", dryplist);
			cs.newLine("dflow", dryflist);
			cs.newLine("darcy", darcylist);

			
			//int cur=Integer.parseInt(Myapp.curvedata);
			
			//	int degree=cur*35/100;
			
			
			
			//c.getWetFlowSmooth(wetplist, wetflist, 1000, degree);
			
			
			
			List<String> diameters = c.getDiameter(wetplist, Myapp.fluidvalue);
			List<String> newdryflow = c.getNewDryFlow(dryplist, dryflist,
					wetplist,8);
			List<String> cff = c.getCff(newdryflow, wetflist);
			List<String> ffp = c.getIncrFF(cff);
			List<String> halfdry = c.getHalfDryFlow(newdryflow);
			List<String> psd = c.getPSD(ffp, diameters);
			List<String> avgdia = c.getAvgDiameter(diameters);

			cs.newLine("wpressure", wetplist);
			cs.newLine("wflow", wetflist);
			cs.newLine("dflownew", newdryflow);

			cs.newLine("diameter", diameters);

			cs.newLine("avgdia", avgdia);
			cs.newLine("cff", cff);
			cs.newLine("ffp", ffp);
			cs.newLine("hdf", halfdry);
			cs.newLine("psd", psd);
			
			cs.newLine("pg1value",DataStore.getPg1());
			cs.newLine("pg2value",DataStore.getPg2());

			cs.newLine("pg1offset",Myapp.pg1offset.get()+"");
			cs.newLine("pg2offset",Myapp.pg2offset.get()+"");

			double meanp = c.getMeanPressure(wetplist, halfdry, wetflist);

			String meand = c.getDiameterCalulate(meanp, Myapp.fluidvalue);

			cs.newLine("meanp", "" + meanp);
			cs.newLine("meand", "" + meand);

			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					para.setText("Bubble pressure :" + getRound(bbp, 4) + "\n"
							+ "Bubble Diameter :" + bbd + "\n" + "Mean Pressure :"
							+ meanp + "\n" + "Mean Diameter :" + meand);

				}
			});
			
			p1list.clear();
			p2list.clear();
			daltaplist.clear();
			daltatlist.clear();
			bans.clear();
			flowlist.clear();
			wetflist.clear();
			dryflist.clear();
			wetplist.clear();
			dryplist.clear();

			darcylist.clear();

			cs.closefile();
			System.out.println("csv Created");
			Openscreen.open("/userinput/Nresult.fxml");

		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

	public void conpopup() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				mydia = new MyDialoug(Main.mainstage,
						"/userinput/Nteststartpopup.fxml");
				mydia.showDialoug();
			}
		});

	}

	void startBubblepoint1(final int numb) {

		countbp = 0;
		// starttest.setVisible(false);

		sendSetting(0);

		writeFormat wrD = new writeFormat();
		wrD.addChar('V');
		wrD.addChar('6');
		wrD.addChar('1');
		wrD.addChkSm();
		wrD.addLast();
		sendData(wrD, 1000);

		wrd = new writeFormat();
		wrd.addStart();
		wrd.addChar('B');
		wrd.addChar('P');

		wrd.addBlank(2);
		wrd.addChkSm();
		wrd.addLast();
		sendData(wrd, delayinauto);

		t2test = System.currentTimeMillis();
		series1.getData().clear();
		series2.getData().clear();

		series1.getData().add(new XYChart.Data(0, thval));
		series1.getData().add(new XYChart.Data(conditionpressure, thval));

		ind = 0;
		t1 = System.currentTimeMillis();

		DataStore.intList.get("80").clear();
		DataStore.intList.get("70").clear();

		DataStore.intList.get("70").addListener(bubblelistener1);

	}

	public String getRound(Double r, int round) {

		if (round == 2) {
			r = (double) Math.round(r * 100) / 100;
		} else if (round == 3) {
			r = (double) Math.round(r * 1000) / 1000;

		} else {
			r = (double) Math.round(r * 10000) / 10000;

		}

		return r + "";

	}

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
		para.setText(sst);

		return "" + d;
	}

	void setTableList() {
		for (int i = 1; i <= trails; i++) {
			lvtest.getItems().add(Myapp.sampleid + "-" + i);
			lvbp.getItems().add("pending");
			lvp.getItems().add("pending");
		}

	}

	void setGraph() {
		root.getChildren().add(sc);
		DataStore.pressure_max = Integer.parseInt(Myapp.endpress);
		sc.setAxisSortingPolicy(SortingPolicy.Y_AXIS.NONE);
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

	void setGraph12() {
		root1.getChildren().add(sc1);
		sc1.setAxisSortingPolicy(SortingPolicy.Y_AXIS.NONE);
		sc1.setAnimated(false);
		sc1.setLegendVisible(false);
		sc1.setCreateSymbols(true);
		flowserires.setName("Dry-Test");
		sc1.getData().addAll(flowserireswet, flowserires);

		// sc1.setTitle("Flow Vs Time");
		sc1.prefWidthProperty().bind(root1.widthProperty());
		sc1.prefHeightProperty().bind(root1.heightProperty());

		root2.getChildren().add(sc2);
		sc2.setAxisSortingPolicy(SortingPolicy.Y_AXIS.NONE);
		sc2.setAnimated(false);
		sc2.setLegendVisible(false);
		sc2.setCreateSymbols(true);
		// pressureserires.setName("Pressure-test");
		sc2.getData().addAll(pressureserireswet, pressureserires);

		sc2.setTitle("Pressure Vs Time");
		sc2.prefWidthProperty().bind(root2.widthProperty());
		sc2.prefHeightProperty().bind(root2.heightProperty());

		Zoom zoom = new Zoom(sc2, root2);

		Zoom zoom1 = new Zoom(sc1, root1);
	}

	void setGauges() {
		VBox v = new VBox(10);
		v.setMinWidth(guages.getPrefWidth());

		gauge5 = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)
				// .title("Pressure")
				.unit("Psi")
				.maxValue(conditionpressure)
				.barColor(Color.CRIMSON)
				.valueColor(Color.web("#727376"))
				.titleColor(Color.web("#727376"))
				.unitColor(Color.web("#727376"))
				.thresholdVisible(false)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				.gradientBarStops(new Stop(0.00, Color.GREEN),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.build();

		Label ll = new Label("Pressure");
		ll.setAlignment(Pos.BASELINE_CENTER);
		ll.setMinWidth(guages.getPrefWidth());
		ll.setTextFill(Color.web("#727376"));
		ll.setStyle("-fx-font: 20px");
		ll.setFont(f.getM_M());

		BorderPane b1 = new BorderPane();
		b1.setCenter(gauge5);
		b1.setBottom(ll);
		v.getChildren().add(b1);

		// v.getChildren().add(ll);
		// v.getChildren().add(gauge5);

		// v.getChildren().add(new Separator());
		// gauge5.valueProperty().bind(DataStore.livepressure);

		System.out.println("Condition flow of gauge : " + conditionflow);
		gauge = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)
				// .title("Pressure")
				.unit("Flow")
				.maxValue(conditionflow)
				.barColor(Color.CRIMSON)
				.valueColor(Color.web("#727376"))
				.titleColor(Color.web("#727376"))
				.unitColor(Color.web("#727376"))
				.thresholdVisible(false)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				.gradientBarStops(new Stop(0.00, Color.GREEN),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.build();
		Label ll2 = new Label("Flow");
		ll2.setAlignment(Pos.BASELINE_CENTER);
		ll2.setMinWidth(guages.getPrefWidth());
		ll2.setTextFill(Color.web("#727376"));
		ll2.setStyle("-fx-font: 20px");
		ll2.setFont(f.getM_M());

		Label ll22 = new Label();
		ll22.setTextFill(Color.web("#727376"));
		ll22.setStyle("-fx-font: 20px");
		ll22.setFont(f.getM_M());

		BorderPane b2 = new BorderPane();
		b2.setCenter(gauge);
		b2.setBottom(ll2);
		v.getChildren().add(b2);

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
						ll.setText("Pressure : "
								+ pp.substring(0, pp.indexOf('.') + 3));

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
						try {
							ll2.setText("Flow : "
									+ ff.substring(0, ff.indexOf('.') + 3));
						} catch (Exception e) {
							ll2.setText("Flow : " + ff);

						}
					}
				});

			}

		});

		// v.getChildren().add(new Separator());

		guages.getChildren().add(v);

	}

	void setButtons() {

		btnabr.getStyleClass().add("transperant_comm");
		btnrefrace.getStyleClass().add("transperant_comm");
		btnstart.getStyleClass().add("transperant_comm");

		btnabr.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				testabourd();
			}
		});

	}

	void testabourd() {

		mydia = new MyDialoug(Main.mainstage, "/userinput/Nabourdpopup.fxml");
		mydia.showDialoug();

	}

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

	private void setDataPointPopup() {
		final Popup popup = new Popup();
		popup.setHeight(DATA_POINT_POPUP_HEIGHT);
		popup.setWidth(DATA_POINT_POPUP_WIDTH);

		for (int i = 0; i < sc.getData().size(); i++) {
			final int dataSeriesIndex = i;
			final XYChart.Series<Number, Number> series = sc.getData().get(i);
			for (final Data<Number, Number> data : series.getData()) {
				final Node node = data.getNode();
				node.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
						new EventHandler<MouseEvent>() {

							private static final int X_OFFSET = 15;
							private static final int Y_OFFSET = -5;
							final Label label = new Label();

							@Override
							public void handle(final MouseEvent event) {
								// System.out.println("MOuse Event");
								final String colorString = "#cfecf0";
								popup.getContent().setAll(label);
								label.setStyle("-fx-background-color: "
										+ colorString + "; -fx-border-color: "
										+ colorString + ";");
								String xdata = data.getXValue() + "";
								String ydata = data.getYValue() + "";

								label.setText("x="
										+ xdata.substring(0,
												xdata.indexOf(".") + 3)
										+ ",\ny="
										+ ydata.substring(0,
												ydata.indexOf(".") + 3));
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

	public void sendData(writeFormat w) {
		System.out.println("Sending Data......");
		w.showData();
		Thread t = new Thread(new SerialWriter(DataStore.out, w));
		t.start();

	}

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

	void sendSetting(int delay) {
		System.out.println("Sending settings");
		List<Integer> str = getValueList(Integer.parseInt(Myapp.accbpt));
		List<Integer> str1 = getValueList(Integer.parseInt(Myapp.accporo));
		List<Integer> str2 = getValueList(1);
		List<Integer> str3 = getValueList(1);

		writeFormat wrD = new writeFormat();

		wrD.addData2('A', str);
		wrD.addData2('B', str1);
		wrD.addData2('C', str2);
		wrD.addData2('D', str3);
		wrD.addChkSm();
		wrD.addLast();
		sendData(wrD,delay);
	}

}
