package report;

import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.Main;
import data_read_write.DatareadN;
import pdfreport.ExcelReport;
import pdfreport.Multiplepororeport;
import pdfreport.Singlepororeport;
import toast.MyDialoug;
import toast.Toast;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PdfselectionController implements Initializable {

	@FXML
	Button btncancel, pdfsave, btnbrows,excelsave,btnbrows1;

	@FXML
	TextField txtcomname;
	

	@FXML
	TextArea txtnotes;

	@FXML
	CheckBox chkrow, flowvspre,chkcoverpage;

	@FXML
	ImageView pic,pic1;

	String imgpath = "", imgpath1 = "";

	@FXML
	javafx.scene.control.Label lblbrowse;

	List<String> graphs;
	
	boolean bchkcoverpage, bchkrowdata, bflowvspre;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub


		bchkcoverpage = true;
		chkcoverpage.selectedProperty().addListener(
				new ChangeListener<Boolean>() {

					@Override
					public void changed(
							ObservableValue<? extends Boolean> arg0,
							Boolean arg1, Boolean arg2) {
						if (arg2) {
							bchkcoverpage = true;
							pic1.setVisible(true);
							btnbrows1.setVisible(true);

						} else {
							bchkcoverpage = false;
							btnbrows1.setVisible(false);
							pic1.setVisible(false);

						}

					}
				});

		
		graphs = new ArrayList<String>();
		
		if(ReportController.list_d.size()>1)
		{
			
			lblbrowse.setVisible(false);
			btnbrows.setVisible(false);
			excelsave.setVisible(false);
		}

		txtnotes.setText("The following test Procedure is based on ASTM F316 (Standard Test Method for Pore Size Characterization.)");

		/*Close Popup*/
		btncancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				MyDialoug.closeDialoug();
			}
		});
		/*Excel File Save*/
		excelsave.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!txtcomname.getText().equals(""))
				{
				MyDialoug.closeDialoug();
				try {
					ExcelReport e=new ExcelReport();
					e.exportToExcel(ReportController.pdffilepath.getPath()+".xlsx", ReportController.list_d.get(0), txtnotes.getText(),txtcomname.getText());
				
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + ReportController.pdffilepath.getAbsolutePath()+".xlsx");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				else {
					Toast.makeText(Main.mainstage, "Please enter companyname", 1500, 500, 500);

				}

			}
		});


		/*PDF File Save*/
		pdfsave.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				if (!txtcomname.getText().equals("")) {
					MyDialoug.closeDialoug();

					 saveReport(ReportController.pdffilepath.getPath()+".pdf");
					 try {
							Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + ReportController.pdffilepath.getAbsolutePath()+".pdf");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				} else {
					Toast.makeText(Main.mainstage, "Please enter companyname",
							1500, 500, 500);

				}

			}
		});

		/*Sample Image Browse*/
		btnbrows.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				handleUpload();
			}
		});

		btnbrows1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				handleUpload1();
			}
		});

	}

	void saveReport(String path) {
		boolean bchkrowdata, bflowvspre;
		
		if (flowvspre.isSelected()) {
			graphs.add("1");
		} else {
			graphs.add("0");
		}

		if (chkrow.isSelected()) {
			bchkrowdata = true;
		} else {
			bchkrowdata = false;
		}
		if (ReportController.list_d.size() == 1) {

			Singlepororeport sp = new Singlepororeport();
			sp.Report(path, ReportController.list_d.get(0), txtnotes.getText(),
					txtcomname.getText(), imgpath, graphs, bchkrowdata, bchkcoverpage, imgpath1);

		} else {
			Multiplepororeport mp = new Multiplepororeport();
			mp.Report(path, ReportController.list_d, txtnotes.getText(), txtcomname.getText(),
					graphs, bchkrowdata,bchkcoverpage, imgpath1);
		}
	}

	@FXML
	public void handleUpload() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter

		// Show open file dialog
		File file = fileChooser.showOpenDialog(MyDialoug.dialog);

		try {

			BufferedImage bufferedImage = ImageIO.read(file);
			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			pic.setImage(image);
			imgpath = file.getPath();
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public void handleUpload1() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter

		// Show open file dialog
		File file = fileChooser.showOpenDialog(MyDialoug.dialog);

		try {

			BufferedImage bufferedImage = ImageIO.read(file);
			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			pic1.setImage(image);
			imgpath1 = file.getPath();
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	
}
