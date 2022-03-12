package pdfreport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;

import application.DataStore;
import application.Myapp;

import com.google.cloud.firestore.collection.LLRBNode.Color;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.scenario.effect.ImageData;

import data_read_write.DatareadN;

public class Singlepororeportblood {

	String[] suffixes =
			// 0 1 2 3 4 5 6 7 8 9
							{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
			// 10 11 12 13 14 15 16 17 18 19
									"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
			// 20 21 22 23 24 25 26 27 28 29
									"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
			// 30 31
									"th", "st" };
	
	String notes;
	String companyname;
	String imgpath,imgpath1;
	List<String> graphs;
	DatareadN d;

	
	List<String> flow,dt,p1,p2,dp,ans;

	boolean isRow = false;
	boolean isHeaderadd = false;

	// Row Data
	BaseColor backcellcoltable = new BaseColor(130, 130, 130);
	BaseColor backcellcol = new BaseColor(230, 230, 230);
	Font tablemean = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD,
			new BaseColor(100, 100, 100));

	Font rowhed = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD,
			new BaseColor(255, 255, 255));

	Font unitlabrow = FontFactory.getFont(FontFactory.HELVETICA, 8,
			Font.NORMAL, new BaseColor(255, 255, 255));

	public static final String FONT = "/OpenSansCondensed-Light.ttf";
	public static final String FONTbb = "/OpenSansCondensed-Light.ttf";
	public static final String flegend = "/FiraSans-Regular.ttf";
	public static final String testnamefont = "/font/BebasNeue Book.ttf";
	public static final String coverptestname = "/font/Roboto-Regular.ttf";
	public static final String testtype = "/font/Roboto-Black.ttf";

	Font ftime = FontFactory.getFont(testtype, 10, Font.ITALIC, new BaseColor(
			0, 0, 0));
	Font comname = FontFactory.getFont(testtype, 12, Font.NORMAL,
			new BaseColor(0, 0, 0));

	Document document = new Document(PageSize.A4, 36, 36, 90, 36);
	PdfWriter writer;
	Font blueFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9,
			Font.NORMAL, new CMYKColor(100, 25, 0, 20));
	Font resultwhitecolorq = FontFactory.getFont(FontFactory.HELVETICA, 9,
			Font.NORMAL, new CMYKColor(1, 1, 1, 1));
	Font resultwhitecolora = FontFactory.getFont(FontFactory.HELVETICA, 9,
			Font.BOLD, new CMYKColor(1, 1, 1, 1));
	Font legendcircle = FontFactory.getFont(FontFactory.HELVETICA, 16,
			Font.NORMAL, new CMYKColor(1f, 0.34f, 0f, 0.29f));
	
	

	

	// First page new
	// Font filetypes = FontFactory.getFont(FontFactory.HELVETICA, 10,
	// Font.NORMAL, new CMYKColor(0f,0f,0f,0f));
	Font testname = FontFactory.getFont(coverptestname, 25, Font.BOLD,
			new CMYKColor(1f, 0.34f, 0f, 0.29f));

	Font fname = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL,
			new BaseColor(96, 96, 98));

	Font sampleinfoq = FontFactory.getFont(FontFactory.HELVETICA, 11,
			Font.BOLD, new BaseColor(81, 81, 83));

	Font whitecol = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD,
			new BaseColor(255, 255, 255));

	Font sampleinfoa = FontFactory.getFont(FontFactory.HELVETICA, 11,
			Font.NORMAL, new BaseColor(90, 90, 92));
	Font sampleinfoqh = FontFactory.getFont(FontFactory.HELVETICA, 15,
			Font.BOLD, new BaseColor(81, 81, 83));

	// NEW TABLE

	Font sampleinfola = FontFactory.getFont(FontFactory.HELVETICA, 12,
			Font.NORMAL, new BaseColor(255, 255, 255));
	Font sampleinfoans = FontFactory.getFont(FontFactory.HELVETICA, 12,
			Font.BOLD, new BaseColor(100, 100, 100));
	Font unitlab = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD,
			new BaseColor(255, 255, 255));

	Font graphtitle = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD,
			getColor(14));
	Font legendtitle = FontFactory.getFont(FontFactory.HELVETICA, 12,
			Font.NORMAL, new BaseColor(96, 96, 98));
	Font legenddot = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);

	// Address
	Font addresslab = FontFactory.getFont(FontFactory.HELVETICA, 11,
			Font.NORMAL, new BaseColor(81, 81, 83));

	// Notes
	Font noteslab = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD,
			new BaseColor(81, 81, 83));

	// Notes desc
	Font notesdeslab = FontFactory.getFont(FontFactory.HELVETICA, 11,
			Font.NORMAL, new BaseColor(81, 81, 83));

	List<String> clr = new ArrayList<String>();
	
	

	public Singlepororeportblood() {
		clr.add("#000080");
		clr.add("#0000FF");
		clr.add("#00FFFF");
		clr.add("#008000");
		clr.add("#00FF00");
		clr.add("#FF00FF");
	}
	public Font font;

	public String text;

	public void WatermarkPageEvent(String text) {
		this.text = text;
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.GRAY);
		font.setStyle(Font.BOLD);
		this.font = font;
	}
	/* Main Function Create Report */
	public void Report(String path, DatareadN d, String notes, String comname,
			String imgpath, List<String> graphs, Boolean btabledata, Boolean bcoverpage,String imgpath1) {

		this.companyname = comname;
		this.notes = notes;
		this.imgpath = imgpath;
		this.imgpath1 = imgpath1;
		this.graphs = graphs;
		this.d = d;

		try {

			writer = PdfWriter
					.getInstance(document, new FileOutputStream(path));

			// write to document
			document.open();

			if (bcoverpage == true) {
				coverpage(d);
			}

			document.newPage();
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			writer.setPageEvent(event);

			sampleinfo(d);

			document.newPage();

			File folder = new File("mypic");
			File[] listOfFiles = folder.listFiles();
			int nflag = 0;

			System.out.println("Selection :" + graphs);

			for (int i = 0; i < 1; i++) {

				System.out.println("File :" + listOfFiles[i].getName() + " : "
						+ graphs.get(i));

				if (graphs.get(i).equals("1")) {
					resultgraph(listOfFiles[i]);
					

					
					
					
					

					if (nflag % 2 == 1) {
						document.newPage();

					}

					nflag++;

				}

			}
			
			
			
			if (btabledata == true) {
				document.newPage();
				Paragraph pp6 = new Paragraph(20);
				pp6.add(new Chunk("\n"));
				try {
					document.add(pp6);
				} catch (DocumentException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				rowData();
			}
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/* RGB Color Code */
	BaseColor getColor(int i) {
		List<BaseColor> clrs = new ArrayList<>();
		clrs.add(new BaseColor(255, 00, 00));
		clrs.add(new BaseColor(00, 81, 212));
		clrs.add(new BaseColor(255, 204, 00));
		clrs.add(new BaseColor(143, 27, 145));
		clrs.add(new BaseColor(254, 153, 1));
		clrs.add(new BaseColor(00, 186, 00));
		clrs.add(new BaseColor(3, 179, 255));
		clrs.add(new BaseColor(255, 110, 137));
		clrs.add(new BaseColor(163, 144, 00));
		clrs.add(new BaseColor(47, 41, 73));
		clrs.add(new BaseColor(162, 134, 219));
		clrs.add(new BaseColor(104, 67, 51));
		clrs.add(new BaseColor(215, 167, 103));
		clrs.add(new BaseColor(27, 222, 222));
		clrs.add(new BaseColor(62, 64, 149));

		return clrs.get(i);

	}
	/* Cover Page */
	void coverpage(DatareadN d) {
		try {

			Image img = Image.getInstance("dddd (2).jpg");
			img.scaleAbsolute(800, 10);
			img.setAbsolutePosition(0f, 832f);
			document.add(img);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {

			Image imgs = Image.getInstance("logo1.png");
			imgs.scaleAbsolute(250f, 76f);
			// imgs.setAbsolutePosition(400f, 745f);
			imgs.setAbsolutePosition(330f, 735f);
			document.add(imgs);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {

			// Image img1 = Image.getInstance("f1.jpg");
			Image img1;
		//	System.out.println("ghhghghghg"+imgpath1);
			if (!imgpath1.equals("")) {
				img1 = Image.getInstance(imgpath1);
			} else {
				img1 = Image.getInstance("f1.jpeg");
			}
			img1.scaleAbsolute(595, 480);
			img1.setAbsolutePosition(0f, 203f);
			document.add(img1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		

		Paragraph pp5 = new Paragraph(530);
		pp5.add(new Chunk("\n"));
		try {
			document.add(pp5);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		PdfPTable resulttable = new PdfPTable(1); // 4 columns.
		resulttable.setWidthPercentage(120); // Width 100%
		try {
			resulttable.setWidths(new int[] { 100 });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPCell r1 = new PdfPCell(new Paragraph("", graphtitle));
		r1.setBorder(1);
		r1.setBorderWidth(3f);
		r1.setBorder(r1.BOTTOM);
		r1.setBorderColor(getColor(14));
		r1.setPaddingTop(4);
		r1.setFixedHeight(20f);
		r1.setHorizontalAlignment(Element.ALIGN_LEFT);
		r1.setVerticalAlignment(Element.ALIGN_CENTER);
		resulttable.addCell(r1);

		try {
			document.add(resulttable);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Paragraph pp6 = new Paragraph(-13);
		pp6.add(new Chunk("\n"));
		try {
			document.add(pp6);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		PdfPTable filetype = new PdfPTable(1);
		filetype.setWidthPercentage(40);
		filetype.setHorizontalAlignment(Element.ALIGN_RIGHT);
		try {
			filetype.setWidths(new int[] { 100 });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Front page

		Font testt = FontFactory.getFont(testtype, 10);// Main Report Title name
		testt.setColor(BaseColor.WHITE);

		Font date = FontFactory.getFont(testtype, 10);// Main Report Title name
		date.setColor(BaseColor.BLACK);

		PdfPCell r3 = new PdfPCell(
				new Paragraph("WVTR TEST REPORT", testt));
		r3.setBorder(0);
		r3.setBackgroundColor(getColor(14));
		r3.setFixedHeight(25f);
		r3.setHorizontalAlignment(Element.ALIGN_CENTER);
		r3.setVerticalAlignment(Element.ALIGN_MIDDLE);

		filetype.addCell(r3);

		try {
			document.add(filetype);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Paragraph p = new Paragraph(20);
		p.add(new Chunk("\n"));
		try {
			document.add(p);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		PdfPTable testnametab = new PdfPTable(2); // 2 columns.
		testnametab.setWidthPercentage(100); // Width 100%
		testnametab.setSpacingBefore(30f); // Space before table

		try {
			testnametab.setWidths(new int[] { 40, 60 });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Font covertime = FontFactory.getFont(
				"./font/RobotoCondensed-Italic.ttf", BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED, 10);
		covertime.setColor(BaseColor.BLACK);

		PdfPCell t0 = new PdfPCell(new Paragraph("DATE : " + getCurrentData(),
				date));
		t0.setBorder(0);
		// t0.setBorder(t0.BOTTOM | t0.TOP);
		t0.setBorderWidth(3f);
		t0.setBorderColor(BaseColor.BLUE);
		t0.setFixedHeight(35f);
		t0.setHorizontalAlignment(Element.ALIGN_LEFT);
		t0.setVerticalAlignment(Element.ALIGN_BOTTOM);
		testnametab.addCell(t0);

		// Font titleFont=FontFactory.getFont(FONT, 45);//Main Report Title name
		// titleFont.setColor(getColor(6));

		// Font tt=FontFactory.getFont(testnamefont, 50);//Main Report Title
		// name
		// tt.setColor(getColor(6));

		Font testname = FontFactory.getFont("./font/BebasNeue Book.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 40);
		testname.setColor(getColor(14));

		PdfPCell t22 = new PdfPCell(new Paragraph(companyname, testname));
		t22.setBorder(0);
		// t22.setBorder(t22.BOTTOM | t22.TOP);
		t22.setBorderWidth(3f);
		t22.setFixedHeight(100f);
		t22.setRowspan(2);
		t22.setHorizontalAlignment(Element.ALIGN_RIGHT);
		t22.setVerticalAlignment(Element.ALIGN_TOP);
		testnametab.addCell(t22);

		PdfPCell t2 = new PdfPCell(new Paragraph("Sample ID : "
				+ d.data.get("sample"), date));
		t2.setBorder(0);
		// t2.setBorder(t2.BOTTOM | t2.TOP);
		t2.setBorderWidth(3f);
		t2.setBorderColor(BaseColor.BLUE);
		t2.setPaddingTop(0);
		t2.setFixedHeight(15f);
		t2.setHorizontalAlignment(Element.ALIGN_LEFT);
		t2.setVerticalAlignment(Element.ALIGN_CENTER);
		testnametab.addCell(t2);

		Font compname = FontFactory.getFont("./font/Roboto-Regular.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14);
		compname.setColor(BaseColor.BLACK);

		try {
			document.add(testnametab);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/* current Date */
	String getCurrentData() {
		String[] suffixes =
		// 0 1 2 3 4 5 6 7 8 9
		{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 10 11 12 13 14 15 16 17 18 19
				"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
				// 20 21 22 23 24 25 26 27 28 29
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 30 31
				"th", "st" };

		Date date = new Date();
		SimpleDateFormat formatDayOfMonth = new SimpleDateFormat("d");
		int day = Integer.parseInt(formatDayOfMonth.format(date));
		String dayStr = day + suffixes[day];

		DateFormat dateFormat = new SimpleDateFormat(" MMMM yyyy | HH:mm aa");
		Calendar cal = Calendar.getInstance();

		// System.out.println(dateFormat.format(cal.getTime()));

		return dayStr + dateFormat.format(cal.getTime());

	}
	/* Single Test File Information Display in Table */
	void sampleinfo(DatareadN d) {

		int r = 130;
		int g = 130;
		int b = 130;
		BaseColor backcellcoltable = new BaseColor(r, g, b);
		BaseColor backcellcoltable1 = new BaseColor(230, 230, 230);

		PdfPTable addresstable = new PdfPTable(1); // 4 columns.
		addresstable.setWidthPercentage(100); // Width 100%
		addresstable.setSpacingBefore(0f); // Space before table
		addresstable.setSpacingAfter(0f); // Space after table

		// Set Column widths
		try {
			addresstable.setWidths(new int[] { 100 });
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PdfPCell a1 = new PdfPCell(new Paragraph("Material Intelligence Lab",
				addresslab));
		a1.setPaddingLeft(10);
		a1.setPaddingTop(1);
		a1.setBorder(0);
		// a1.setBorder(a1.LEFT | a1.RIGHT);
		a1.setFixedHeight(15f);
		// a1.setBackgroundColor(backcellcoltable1);
		a1.setBorderColor(new BaseColor(130, 130, 130));
		a1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		a1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell a2 = new PdfPCell(new Paragraph("www.m19.io", addresslab));
		a2.setPaddingLeft(10);
		a2.setPaddingTop(1);
		a2.setBorder(0);
		// a2.setBorder(a2.LEFT | a2.RIGHT);
		a2.setFixedHeight(15f);
		// a2.setBackgroundColor(backcellcoltable1);
		a2.setBorderColor(new BaseColor(130, 130, 130));
		a2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		a2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell a3 = new PdfPCell(new Paragraph("info@m19.io", addresslab));
		a3.setPaddingLeft(10);
		a3.setPaddingTop(1);
		a3.setBorder(0);
		// a3.setBorder(a3.LEFT | a3.RIGHT);
		a3.setFixedHeight(15f);
		// a3.setBackgroundColor(backcellcoltable1);
		a3.setBorderColor(new BaseColor(130, 130, 130));
		a3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		a3.setVerticalAlignment(Element.ALIGN_MIDDLE);

		addresstable.addCell(a1);
		addresstable.addCell(a2);
		addresstable.addCell(a3);

		try {
			document.add(addresstable);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Paragraph p = new Paragraph(15);
		p.add(new Chunk("\n"));
		try {
			document.add(p);
		} catch (DocumentException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}

		PdfPTable tablem = new PdfPTable(2); // 4 columns.
		tablem.setWidthPercentage(100); // Width 100%
		tablem.setSpacingBefore(0f); // Space before table
		tablem.setSpacingAfter(0f); // Space after table

		// Set Column widths
		try {
			tablem.setWidths(new int[] { 50, 50 });
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PdfPCell cell1h = new PdfPCell(new Paragraph("Sample Information", whitecol));
		cell1h.setBorder(1);
		cell1h.setBorder(cell1h.TOP);
		cell1h.setBorderColor(new BaseColor(255, 255, 255));
		cell1h.setBackgroundColor(backcellcoltable);
		cell1h.setPaddingLeft(10);
		cell1h.setPaddingTop(1);
		cell1h.setFixedHeight(30f);
		cell1h.setColspan(2);
		cell1h.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1h.setVerticalAlignment(Element.ALIGN_MIDDLE);

	

		PdfPCell cell3 = new PdfPCell(new Paragraph("Test Date", sampleinfoq));
		cell3.setBorder(1);
		cell3.setBorder(cell3.LEFT | cell3.RIGHT);
		cell3.setPaddingLeft(10);
		cell3.setPaddingTop(1);
		cell3.setBackgroundColor(backcellcoltable1);
		cell3.setFixedHeight(25f);
		cell3.setBorderColor(new BaseColor(130, 130, 130));
		cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell4 = new PdfPCell(new Paragraph("" + d.data.get("testdate")
				+ " | " + d.data.get("testtime"), sampleinfoa));
		cell4.setBorder(1);
		cell4.setBorder(cell4.RIGHT);
		cell4.setBorderColor(new BaseColor(130, 130, 130));
		cell4.setPaddingLeft(10);
		cell4.setPaddingTop(1);
		cell4.setBackgroundColor(backcellcoltable1);
		cell4.setFixedHeight(25f);
		cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell5 = new PdfPCell(new Paragraph("Sample ID", sampleinfoq));
		// cell5 = new PdfPCell(new Phrase("jj"+d.starttime));
		cell5.setPaddingTop(1);
		cell5.setPaddingLeft(10);
		cell5.setBorder(1);
		cell5.setFixedHeight(25f);
		// cell5.setBackgroundColor(backcellcoltable1);
		cell5.setBorder(cell5.LEFT | cell5.RIGHT);
		cell5.setBorderColor(new BaseColor(130, 130, 130));
		cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell51 = new PdfPCell(new Paragraph("" + d.data.get("sample"),
				sampleinfoa));
		cell51.setBorder(1);
		cell51.setBorder(cell51.RIGHT);
		cell51.setBorderColor(new BaseColor(130, 130, 130));
		cell51.setPaddingLeft(10);
		// cell51.setBackgroundColor(backcellcoltable1);
		cell51.setPaddingTop(1);
		cell51.setFixedHeight(25f);
		cell51.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell51.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell c1 = new PdfPCell(new Paragraph("Diameter", sampleinfoq));
		c1.setPaddingLeft(10);
		c1.setPaddingTop(1);
		c1.setBorder(1);
		c1.setBorder(c1.LEFT | c1.RIGHT);
	//	c1.setBackgroundColor(backcellcoltable1);
		c1.setBorderColor(new BaseColor(130, 130, 130));
		c1.setFixedHeight(25f);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell c2 = new PdfPCell(new Paragraph("" + d.data.get("splate"), sampleinfoa));
		c2.setBorder(1);
		c2.setBorder(c2.RIGHT);
		c2.setBorderColor(new BaseColor(130, 130, 130));
	//	c2.setBackgroundColor(backcellcoltable1);
		c2.setPaddingTop(1);
		c2.setPaddingLeft(10);
		c2.setFixedHeight(25f);
		c2.setHorizontalAlignment(Element.ALIGN_LEFT);
		c2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f11 = new PdfPCell(new Paragraph("Thickness", sampleinfoq));
		f11.setPaddingLeft(10);
		f11.setBackgroundColor(backcellcoltable1);
		f11.setPaddingTop(1);
		f11.setBorder(1);
		f11.setFixedHeight(25f);
		f11.setBorder(f11.LEFT | f11.RIGHT);
		f11.setBorderColor(new BaseColor(130, 130, 130));
		f11.setHorizontalAlignment(Element.ALIGN_LEFT);
		f11.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f22 = new PdfPCell(new Paragraph("" + d.data.get("thikness"),
				sampleinfoa));
		f22.setBorder(1);
		f22.setBorder(f22.RIGHT);
		f22.setBorderColor(new BaseColor(130, 130, 130));
		f22.setPaddingLeft(10);
		f22.setBackgroundColor(backcellcoltable1);
		f22.setPaddingTop(1);
		f22.setFixedHeight(25f);
		f22.setHorizontalAlignment(Element.ALIGN_LEFT);
		f22.setVerticalAlignment(Element.ALIGN_MIDDLE);
	

		PdfPCell t3 = new PdfPCell(new Paragraph("Test Method", sampleinfoq));
		t3.setPaddingLeft(10);
		t3.setPaddingTop(1);
		t3.setBorder(1);
		t3.setBorder(t3.LEFT | t3.RIGHT|t3.BOTTOM);
		t3.setFixedHeight(25f);
		t3.setBorderColor(new BaseColor(130, 130, 130));
		
		t3.setHorizontalAlignment(Element.ALIGN_LEFT);
		t3.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell t4 = new PdfPCell(new Paragraph("" + d.data.get("method"), sampleinfoa));
		t4.setBorder(1);

		t4.setBorder(t4.RIGHT|t4.BOTTOM);
		t4.setBorderColor(new BaseColor(130, 130, 130));
		t4.setPaddingLeft(10);
		t4.setFixedHeight(25f);
		
		t4.setPaddingTop(1);
		t4.setHorizontalAlignment(Element.ALIGN_LEFT);
		t4.setVerticalAlignment(Element.ALIGN_MIDDLE);

	
		

		

		

		tablem.addCell(cell1h);


		tablem.addCell(cell5);
		tablem.addCell(cell51);
		
		tablem.addCell(cell3);
		tablem.addCell(cell4);
		
		
		tablem.addCell(c1);
		tablem.addCell(c2);
		
		tablem.addCell(f11);
		tablem.addCell(f22);
		
		tablem.addCell(t3);
		tablem.addCell(t4);
		
		
		

		try {
			document.add(tablem);
		} catch (DocumentException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		 backcellcoltable = new BaseColor(62, 64, 149);
		
		

			PdfPCell lblresult = new PdfPCell(new Paragraph("Result", sampleinfoqh));


			lblresult.setBorder(0);
			lblresult.setColspan(7);
			// lblresult.setBorder(lblresult.BOTTOM);
			lblresult.setFixedHeight(50f);
			// lblresult.setBackgroundColor(backcellcoltable1);
			lblresult.setBorderColor(new BaseColor(130, 130, 130));
			lblresult.setHorizontalAlignment(Element.ALIGN_CENTER);
			lblresult.setVerticalAlignment(Element.ALIGN_MIDDLE);

		 
		 PdfPTable resulttable = new PdfPTable(7); // 4 columns.
		resulttable.setWidthPercentage(100); // Width 100%
		resulttable.setSpacingBefore(25f); // Space before table
		resulttable.setSpacingAfter(0f); // Space after

		// Set Column widths
		try {
			resulttable.setWidths(new int[] { 10,23,5,22,5,23,10});
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Font resultable = FontFactory.getFont("./font/Montserrat-SemiBold.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
		resultable.setColor(BaseColor.WHITE);
		BaseColor resultborder = new BaseColor(130, 130, 130);
		float border = 1f;
		

		PdfPCell r1b = new PdfPCell(new Paragraph("",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r1b.setBorder(0);
//		r1b.setBorder(r1b.TOP | r1b.LEFT);
	//	r1b.setBorderColor(resultborder);
		//r1b.setBackgroundColor(backcellcoltable);
		r1b.setFixedHeight(18f);
		r1b.setHorizontalAlignment(Element.ALIGN_CENTER);
		r1b.setVerticalAlignment(Element.ALIGN_MIDDLE);

		
		
		PdfPCell r1 = new PdfPCell(new Paragraph("WVTR",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r1.setBorder(1);
		r1.setBorder(r1.TOP | r1.LEFT | r1.RIGHT);
		r1.setBorderColor(resultborder);
		r1.setBackgroundColor(backcellcoltable);
		r1.setFixedHeight(23f);
		r1.setHorizontalAlignment(Element.ALIGN_CENTER);
		r1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		
		PdfPCell r1b2 = new PdfPCell(new Paragraph("",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r1b2.setBorder(0);
		//r1b2.setBorder(r1b2.TOP | r1b2.LEFT);
		//r1b2.setBorderColor(resultborder);
		//r1b2.setBackgroundColor(backcellcoltable);
		r1b2.setFixedHeight(18f);
		r1b2.setHorizontalAlignment(Element.ALIGN_CENTER);
		r1b2.setVerticalAlignment(Element.ALIGN_MIDDLE);



		PdfPCell r2 = new PdfPCell(new Paragraph("Permeance",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r2.setBorder(1);
		r2.setBorder(r2.TOP | r2.LEFT |r2.RIGHT);
		r2.setBorderColor(resultborder);
		r2.setBackgroundColor(backcellcoltable);
		r2.setFixedHeight(23f);
		r2.setHorizontalAlignment(Element.ALIGN_CENTER);
		r2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		PdfPCell r1b3 = new PdfPCell(new Paragraph("",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r1b3.setBorder(0);
//		r1b3.setBorder(r1b3.TOP | r1b3.LEFT);
		//r1b3.setBorderColor(resultborder);
	//	r1b3.setBackgroundColor("H0");
		r1b3.setFixedHeight(18f);
		r1b3.setHorizontalAlignment(Element.ALIGN_CENTER);
		r1b3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		
		PdfPCell r2perm = new PdfPCell(new Paragraph("Permeability ",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r2perm.setBorder(1);
		r2perm.setBorder(r2perm.TOP | r2perm.LEFT |r2perm.RIGHT);
		r2perm.setBorderColor(resultborder);
		r2perm.setBackgroundColor(backcellcoltable);
		r2perm.setFixedHeight(23f);
		r2perm.setHorizontalAlignment(Element.ALIGN_CENTER);
		r2perm.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		PdfPCell r1bper2 = new PdfPCell(new Paragraph("",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r1bper2.setBorder(0);
//		r1bper2.setBorder(r1bper2.TOP | r1bper2.LEFT);
		//r1bper2.setBorderColor(resultborder);
	//	r1bper2.setBackgroundColor("H0");
		r1bper2.setFixedHeight(18f);
		r1bper2.setHorizontalAlignment(Element.ALIGN_CENTER);
		r1bper2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		resulttable.addCell(lblresult);

		
		/* LABEL */
		resulttable.addCell(r1b);
		resulttable.addCell(r1);
		resulttable.addCell(r1b2);
		resulttable.addCell(r2);
		resulttable.addCell(r1b3);

		resulttable.addCell(r2perm);
		resulttable.addCell(r1bper2);
		
		PdfPCell u1a= new PdfPCell(new Paragraph("  ", unitlab));
		u1a.setBorder(0);
		//u1a.setBorder(u1a.LEFT| u1a.BOTTOM);
		u1a.setBorderColor(resultborder);
		u1a.setFixedHeight(15f);
		//u1a.setBackgroundColor(backcellcoltable);
		u1a.setHorizontalAlignment(Element.ALIGN_CENTER);
		u1a.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		PdfPCell u1 = new PdfPCell(new Paragraph(" (g/m2/day) ", unitlab));
		u1.setBorder(1);
		u1.setBorder(u1.LEFT| u1.BOTTOM);
		u1.setBorderColor(resultborder);
		u1.setFixedHeight(17f);
		u1.setBackgroundColor(backcellcoltable);
		u1.setHorizontalAlignment(Element.ALIGN_CENTER);
		u1.setVerticalAlignment(Element.ALIGN_TOP);
		
		PdfPCell u1b= new PdfPCell(new Paragraph("  ", unitlab));
		u1b.setBorder(0);
		//u1b.setBorder(u1b.LEFT| u1b.BOTTOM);
		u1b.setBorderColor(resultborder);
		u1b.setFixedHeight(17f);
		//u1b.setBackgroundColor(backcellcoltable);
		u1b.setHorizontalAlignment(Element.ALIGN_CENTER);
		u1b.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell u2 = new PdfPCell(new Paragraph("(ng/m2.s.pa)", unitlab));
		u2.setBorder(0);
		//u2.setBorder(u2.BOTTOM);
		u2.setBorderColor(resultborder);
		u2.setFixedHeight(17f);
		u2.setBackgroundColor(backcellcoltable);
		u2.setHorizontalAlignment(Element.ALIGN_CENTER);
		u2.setVerticalAlignment(Element.ALIGN_TOP);
		
		PdfPCell u1c= new PdfPCell(new Paragraph("  ", unitlab));
		u1c.setBorder(0);
		//u1c.setBorder(u1c.LEFT| u1c.BOTTOM);
		u1c.setBorderColor(resultborder);
		u1c.setFixedHeight(17f);
		//u1c.setBackgroundColor(backcellcoltable);
		u1c.setHorizontalAlignment(Element.ALIGN_CENTER);
		u1c.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		PdfPCell u3 = new PdfPCell(new Paragraph("(ng/m.s.pa)", unitlab));
		//u3.setBorder(1);
		u3.setBorder(u3.LEFT| u3.BOTTOM);
		u3.setBorderColor(resultborder);
		u3.setFixedHeight(17f);
		u3.setBackgroundColor(backcellcoltable);
		u3.setHorizontalAlignment(Element.ALIGN_CENTER);
		u3.setVerticalAlignment(Element.ALIGN_TOP);
		
		PdfPCell u1d= new PdfPCell(new Paragraph("  ", unitlab));
		u1d.setBorder(0);
		//u1d.setBorder(u1d.LEFT| u1d.BOTTOM);
		u1d.setBorderColor(resultborder);
		u1d.setFixedHeight(15f);
		//u1d.setBackgroundColor(backcellcoltable);
		u1d.setHorizontalAlignment(Element.ALIGN_CENTER);
		u1d.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		resulttable.addCell(u1a);		
		resulttable.addCell(u1);
		resulttable.addCell(u1b);
		resulttable.addCell(u2);
		resulttable.addCell(u1c);
		resulttable.addCell(u3);
		resulttable.addCell(u1d);
		

				
		List<String> wvtr = d.getValuesOf("" + d.data.get("wvtr"));
		
		List<String> permeance= d.getValuesOf("" + d.data.get("permeance"));
		List<String> permaibility= d.getValuesOf("" + d.data.get("permiability"));
		

		
		
		PdfPCell r4r ;
		for (int j = 0; j < wvtr.size(); j++) {
		
			
			if (j == wvtr.size() - 1) {
			
				PdfPCell r1rb = new PdfPCell(new Paragraph("",
						sampleinfoans));
				r1rb.setBorder(0);
				//r1rb.setBorder(r1rb.BOTTOM | r1rb.LEFT | r1rb.RIGHT);
				//r1rb.setBorderColor(resultborder);
				//r1rb.setBackgroundColor(backcellcoltable1);
				r1rb.setFixedHeight(40f);
				r1rb.setHorizontalAlignment(Element.ALIGN_CENTER);
				r1rb.setVerticalAlignment(Element.ALIGN_MIDDLE);
				resulttable.addCell(r1rb);
				
				PdfPCell r1r = new PdfPCell(new Paragraph(wvtr.get(j),
					sampleinfoans));
			r1r.setBorder(1);
			r1r.setBorder(r1r.BOTTOM | r1r.LEFT | r1r.RIGHT);
			r1r.setBorderColor(resultborder);
			r1r.setBackgroundColor(backcellcoltable1);
			r1r.setFixedHeight(40f);
			r1r.setHorizontalAlignment(Element.ALIGN_CENTER);
			r1r.setVerticalAlignment(Element.ALIGN_MIDDLE);
			resulttable.addCell(r1r);
			
			PdfPCell r1rb2 = new PdfPCell(new Paragraph("",
					sampleinfoans));
			r1rb2.setBorder(0);
			//r1rb2.setBorder(r1rb2.BOTTOM | r1rb2.LEFT | r1rb2.RIGHT);
			r1rb2.setBorderColor(resultborder);
			//r1rb2.setBackgroundColor(backcellcoltable1);
			r1rb2.setFixedHeight(40f);
			r1rb2.setHorizontalAlignment(Element.ALIGN_CENTER);
			r1rb2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			resulttable.addCell(r1rb2);
			
		
				
			PdfPCell r2r = new PdfPCell(new Paragraph(""+permeance.get(j).toString() ,
					sampleinfoans));
			r2r.setBorder(1);
			r2r.setBorder(r2r.BOTTOM| r2r.RIGHT | r2r.LEFT);
			r2r.setBorderColor(resultborder);
			r2r.setBackgroundColor(backcellcoltable1);
			r2r.setFixedHeight(40f);
			r2r.setHorizontalAlignment(Element.ALIGN_CENTER);
			r2r.setVerticalAlignment(Element.ALIGN_MIDDLE);
			resulttable.addCell(r2r);
				
	
				 r4r = new PdfPCell(new Paragraph("",sampleinfoans));
				//r4r.setBackgroundColor(backcellcoltable1);
				r4r.setBorder(0);
				//r4r.setBorder(r4r.RIGHT | r4r.BOTTOM);
				//r4r.setBorderColor(resultborder);
				r4r.setRowspan(j);
				
				r4r.setFixedHeight(30f);
				r4r.setHorizontalAlignment(Element.ALIGN_CENTER);
				r4r.setVerticalAlignment(Element.ALIGN_MIDDLE);
				resulttable.addCell(r4r);
				
				
				PdfPCell r2rper = new PdfPCell(new Paragraph(""+permaibility.get(j).toString() ,
						sampleinfoans));
				r2rper.setBorder(1);
				r2rper.setBorder(r2rper.BOTTOM| r2rper.RIGHT | r2rper.LEFT);
				r2rper.setBorderColor(resultborder);
				r2rper.setBackgroundColor(backcellcoltable1);
				r2rper.setFixedHeight(40f);
				r2rper.setHorizontalAlignment(Element.ALIGN_CENTER);
				r2rper.setVerticalAlignment(Element.ALIGN_MIDDLE);
				resulttable.addCell(r2rper);
					
		
				PdfPCell r4rs = new PdfPCell(new Paragraph("",sampleinfoans));
					//r4r.setBackgroundColor(backcellcoltable1);
					r4rs.setBorder(0);
					//r4rs.setBorder(r4rs.RIGHT | r4rs.BOTTOM);
					//r4rs.setBorderColor(resultborder);
					r4rs.setRowspan(j);
					
					r4rs.setFixedHeight(30f);
					r4rs.setHorizontalAlignment(Element.ALIGN_CENTER);
					r4rs.setVerticalAlignment(Element.ALIGN_MIDDLE);
					resulttable.addCell(r4rs);
			
			}
		else
		{
			
			PdfPCell r1r = new PdfPCell(new Paragraph(wvtr.get(j),
					sampleinfoans));
			r1r.setBorder(1);
			r1r.setBorder(r1r.LEFT | r1r.RIGHT);
			r1r.setBorderColor(resultborder);
			r1r.setBackgroundColor(backcellcoltable1);
			r1r.setFixedHeight(18f);
			r1r.setHorizontalAlignment(Element.ALIGN_CENTER);
			r1r.setVerticalAlignment(Element.ALIGN_MIDDLE);
			resulttable.addCell(r1r);
			
			PdfPCell r2r = new PdfPCell(new Paragraph(""+Integer.parseInt(wvtr.get(j).toString()) / 60 ,
					
//			PdfPCell r2r = new PdfPCell(new Paragraph(btime.get(j),
					sampleinfoans));
			r2r.setBorder(1);
			r2r.setBorder(r2r.RIGHT);
			r2r.setBorderColor(resultborder);
			r2r.setBackgroundColor(backcellcoltable1);
			r2r.setFixedHeight(18f);
			r2r.setHorizontalAlignment(Element.ALIGN_CENTER);
			r2r.setVerticalAlignment(Element.ALIGN_MIDDLE);
			resulttable.addCell(r2r);
			
		
		
			if(j == 0) {
				 r4r = new PdfPCell(new Paragraph("" + d.data.get("result"),sampleinfoans));
				r4r.setBackgroundColor(backcellcoltable1);
				r4r.setBorder(1);
				r4r.setBorder(r4r.RIGHT | r4r.BOTTOM);
				r4r.setRowspan(6);
				r4r.setBorderColor(resultborder);
				r4r.setFixedHeight(18f);
				r4r.setHorizontalAlignment(Element.ALIGN_CENTER);
				r4r.setVerticalAlignment(Element.ALIGN_MIDDLE);
				resulttable.addCell(r4r);
				
			}
	
	
		}
			

			
		
			}		
		

		
					
	
		/* Unit */


		try {
			document.add(resulttable);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Paragraph p88 = new Paragraph();
		p88.add(new Chunk("\n"));
		try {
			document.add(p88);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// Notes and Image Table.

		if (!imgpath.equals("")) {
			PdfPTable notestable = new PdfPTable(2); // 4 columns.
			notestable.setWidthPercentage(100); // Width 100%
			notestable.setSpacingBefore(5f); // Space before table
			notestable.setSpacingAfter(0f); // Space after table

			// Set Column widths
			try {
				notestable.setWidths(new int[] { 50, 50 });
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			PdfPCell n11 = new PdfPCell(new Phrase());
			n11.setPaddingLeft(0);
			n11.setPaddingBottom(0);

			Image img = null;
			try {
				// img = Image.getInstance("img2.jpg");
				img = Image.getInstance(imgpath);
			} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// img.scalePercent(15);
			// img.scaleToFit(100, 50);
			img.scaleToFit(400, 160);

			n11.setBorder(0);
			n11.setHorizontalAlignment(Element.ALIGN_CENTER);
			n11.setVerticalAlignment(Element.ALIGN_TOP);
			n11.addElement(new Chunk(img, 0, 2, true));

			PdfPCell n2 = new PdfPCell(new Paragraph("Notes", noteslab));
			n2.setPaddingLeft(0);
			n2.setPaddingTop(0);
			n2.setBorder(0);
			// n2.setBorder(n2.LEFT | n2.RIGHT);
			n2.setFixedHeight(20f);
			// n2.setBackgroundColor(backcellcoltable1);
			n2.setBorderColor(new BaseColor(130, 130, 130));
			n2.setHorizontalAlignment(Element.ALIGN_CENTER);
			n2.setVerticalAlignment(Element.ALIGN_MIDDLE);

			// PdfPCell n3 = new PdfPCell(new
			// Paragraph("The following test Procedure is based on ASTM D6767 (Standard Test Method for Pore Size Characterization.)",notesdeslab));
			PdfPCell n3 = new PdfPCell(new Paragraph(notes, notesdeslab));

			n3.setPaddingLeft(10);
			n3.setPaddingTop(1);
			n3.setBorder(0);

			// n3.setBorder(n3.BOTTOM);
			n3.setFixedHeight(250f);
			// n3.setBackgroundColor(backcellcoltable1);
			n3.setBorderColor(new BaseColor(130, 130, 130));
			n3.setHorizontalAlignment(Element.ALIGN_TOP);
			n3.setVerticalAlignment(Element.ALIGN_LEFT);

			n11.setRowspan(2);
			notestable.addCell(n11);

			notestable.addCell(n2);
			notestable.addCell(n3);

			try {
				document.add(notestable);
			} catch (DocumentException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		} else {
			PdfPTable notestable = new PdfPTable(1); // 4 columns.
			notestable.setWidthPercentage(100); // Width 100%
			notestable.setSpacingBefore(2f); // Space before table
			notestable.setSpacingAfter(0f); // Space after table

			// Set Column widths
			try {
				notestable.setWidths(new int[] { 100 });
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			PdfPCell n2 = new PdfPCell(new Paragraph("Notes", noteslab));
			n2.setPaddingLeft(0);
			n2.setPaddingTop(0);
			n2.setBorder(0);
			// n2.setBorder(n2.LEFT | n2.RIGHT);
			n2.setFixedHeight(18f);
			// n2.setBackgroundColor(backcellcoltable1);
			n2.setBorderColor(new BaseColor(130, 130, 130));
			n2.setHorizontalAlignment(Element.ALIGN_CENTER);
			n2.setVerticalAlignment(Element.ALIGN_TOP);

			// PdfPCell n3 = new PdfPCell(new
			// Paragraph("The following test Procedure is based on ASTM D6767 (Standard Test Method for Pore Size Characterization.)",notesdeslab));
			PdfPCell n3 = new PdfPCell(new Paragraph(notes, notesdeslab));

			n3.setPaddingLeft(10);
			n3.setPaddingTop(1);
			n3.setBorder(0);

			// n3.setBorder(n3.BOTTOM);
			n3.setFixedHeight(230f);
			// n3.setBackgroundColor(backcellcoltable1);
			n3.setBorderColor(new BaseColor(130, 130, 130));
			n3.setHorizontalAlignment(Element.ALIGN_TOP);
			n3.setVerticalAlignment(Element.ALIGN_LEFT);

			notestable.addCell(n2);
			notestable.addCell(n3);

			try {
				document.add(notestable);
			} catch (DocumentException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		}

		/*
		 * Paragraph p88=new Paragraph(190); p88.add(new Chunk("\n")); try {
		 * document.add(p88); } catch (DocumentException e2) { // TODO
		 * Auto-generated catch block e2.printStackTrace(); } //Footer conten
		 */

		PdfPTable disctable = new PdfPTable(1); // 4 columns.
		disctable.setWidthPercentage(100); // Width 100%
		disctable.setSpacingBefore(0f); // Space before table
		disctable.setSpacingAfter(0f); // Space after table

		// Set Column widths
		try {
			disctable.setWidths(new int[] { 100 });
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PdfPCell d1 = new PdfPCell(new Paragraph(
				"This is a computer generated report, hence does not require signature.", sampleinfoq));
		d1.setPaddingLeft(10);
		d1.setPaddingTop(1);
		d1.setBorder(1);
		d1.setBorder(d1.BOTTOM);
		d1.setFixedHeight(15f);
		// d1.setBackgroundColor(backcellcoltable1);
		d1.setBorderColor(new BaseColor(130, 130, 130));
		d1.setHorizontalAlignment(Element.ALIGN_CENTER);
		d1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell d2 = new PdfPCell(
				new Paragraph(
						"Result relates to the sample tested. The report shall not be reproduced except in full, without the written approval of the laboratory. The report is strictly confidential and technical information of client only. Not for advertisement, promotion, publicity or litigation.",
						addresslab));
		d2.setPaddingLeft(0);
		d2.setPaddingTop(1);
		d2.setBorder(0);
		// d2.setBorder(d2.LEFT | d2.RIGHT);
		d2.setFixedHeight(50f);
		// d2.setBackgroundColor(backcellcoltable1);
		d2.setBorderColor(new BaseColor(130, 130, 130));
		d2.setHorizontalAlignment(Element.ALIGN_CENTER);
		d2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		disctable.addCell(d1);
		disctable.addCell(d2);

		try {
			document.add(disctable);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}
	/* Display Graph */
	void resultgraph(File f) {
		try {

			Paragraph pp5 = new Paragraph(15);
			pp5.add(new Chunk("\n"));
			document.add(pp5);

			PdfPTable resulttable = new PdfPTable(4); // 4 columns.
			resulttable.setWidthPercentage(100); // Width 100%

			resulttable.setWidths(new int[] { 25, 25, 25, 25 });

			Font headerdate = FontFactory.getFont(
					"./font/Montserrat-SemiBold.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED, 14);
			headerdate.setColor(getColor(14));

			String imagefilename = f.getName().substring(0,
					f.getName().indexOf('.'));

			System.out.println("Image Name--------->" + imagefilename);

			PdfPCell r1 = new PdfPCell(new Paragraph(imagefilename.substring(1,
					imagefilename.length()), headerdate));
			r1.setBorder(0);
			r1.setColspan(4);
			r1.setBorder(0);
			r1.setPaddingTop(4);
			r1.setFixedHeight(20f);
			r1.setHorizontalAlignment(Element.ALIGN_CENTER);
			r1.setVerticalAlignment(Element.ALIGN_CENTER);

			resulttable.addCell(r1);

			document.add(resulttable);

			Image image = Image.getInstance(f.getAbsolutePath());
			image.scaleAbsolute(520, 300);

			document.add(image);
		
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("File is created");

	}
	/* Row Data Table Header in Title name and unite */
	void addTableHeader(PdfPTable tablem) {
		
		tablem.setSpacingBefore(10f);
		String st = ""+ d.data.get("sample");
		
		Font headertestname = FontFactory.getFont(
				"./font/BebasNeue Regular.ttf", BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED, 15);
		headertestname.setColor(getColor(14));
		
		try {

			Chunk redText2 = new Chunk("Raw Data " , headertestname);
			Chunk redText = new Chunk("Sample ID : " + st, headertestname);

			Paragraph p2 = new Paragraph(redText2);
			Paragraph p1 = new Paragraph(redText);
			document.add(p2);			
			document.add(p1);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		
		
		backcellcoltable=new BaseColor(62, 64, 149);
		
		PdfPCell cell2 = new PdfPCell(new Paragraph("Time ", rowhed));
		cell2.setBackgroundColor(backcellcoltable);
		cell2.setBorder(0);
		cell2.setBorder(cell2.TOP | cell2.LEFT);
		cell2.setBorderColor(new BaseColor(130, 130, 130));
		cell2.setPaddingLeft(0);
		cell2.setPaddingTop(0);
		cell2.setFixedHeight(30f);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

	
		PdfPCell cell6 = new PdfPCell(new Paragraph("Chamber ", rowhed));
		cell6.setBackgroundColor(backcellcoltable);
		cell6.setBorder(1);
		 cell6.setBorder(cell6.RIGHT | cell6.BOTTOM |  cell6.LEFT | cell6.TOP);
		 cell6.setBorderColor(new BaseColor(130, 130, 130));
		cell6.setPaddingLeft(0);
		cell6.setPaddingTop(0);
		cell6.setColspan(2);
		cell6.setFixedHeight(30f);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell6b = new PdfPCell(new Paragraph("Bottom Cell", rowhed));
		cell6b.setBackgroundColor(backcellcoltable);
		cell6b.setBorder(1);
		 cell6b.setBorder(cell6b.RIGHT | cell6b.BOTTOM | cell6b.TOP);
		 cell6b.setBorderColor(new BaseColor(130, 130, 130));
		cell6b.setPaddingLeft(0);
		cell6b.setColspan(2);
		
		cell6b.setPaddingTop(0);
		cell6b.setFixedHeight(30f);
		cell6b.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6b.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		PdfPCell cell6t = new PdfPCell(new Paragraph("Top Cell ", rowhed));
		cell6t.setBackgroundColor(backcellcoltable);
		cell6t.setBorder(1);
		 cell6t.setBorder(cell6t.RIGHT | cell6t.BOTTOM | cell6t.TOP);
		 cell6t.setBorderColor(new BaseColor(130, 130, 130));
		cell6t.setPaddingLeft(0);
		cell6t.setPaddingTop(0);
		cell6t.setColspan(2);
		
		cell6t.setFixedHeight(30f);
		cell6t.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6t.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		PdfPCell cell6svpt = new PdfPCell(new Paragraph("SVP ", rowhed));
		cell6svpt.setBackgroundColor(backcellcoltable);
		cell6svpt.setBorder(1);
		 cell6svpt.setBorder(cell6svpt.RIGHT | cell6svpt.BOTTOM | cell6svpt.TOP);
		 cell6svpt.setBorderColor(new BaseColor(130, 130, 130));
		cell6svpt.setPaddingLeft(0);
		cell6svpt.setPaddingTop(0);
		cell6svpt.setFixedHeight(30f);
		cell6svpt.setRowspan(2);
		cell6svpt.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6svpt.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		
		PdfPCell cell6avpt= new PdfPCell(new Paragraph("AVP ", rowhed));
		cell6avpt.setBackgroundColor(backcellcoltable);
		cell6avpt.setBorder(1);
		cell6avpt.setBorder(cell6avpt.RIGHT | cell6avpt.BOTTOM | cell6avpt.TOP);
		cell6avpt.setBorderColor(new BaseColor(130, 130, 130));
		cell6avpt.setPaddingLeft(0);
		cell6avpt.setRowspan(2);
		cell6avpt.setPaddingTop(0);
		cell6avpt.setFixedHeight(30f);
		cell6avpt.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6avpt.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		
		PdfPCell cell6mass= new PdfPCell(new Paragraph("Mass ", rowhed));
		cell6mass.setBackgroundColor(backcellcoltable);
		cell6mass.setBorder(1);
		 cell6mass.setBorder(cell6mass.RIGHT | cell6mass.BOTTOM| cell6mass.TOP );
		cell6mass.setBorderColor(new BaseColor(130, 130, 130));
		cell6mass.setPaddingLeft(0);
		cell6mass.setPaddingTop(0);
		cell6mass.setFixedHeight(30f);
		cell6mass.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6mass.setVerticalAlignment(Element.ALIGN_MIDDLE);

		
		// Units

	

		PdfPCell ucell2 = new PdfPCell(new Paragraph("( min )", unitlabrow));
		ucell2.setBackgroundColor(backcellcoltable);
		ucell2.setBorder(1);
		ucell2.setBorder(ucell2.RIGHT);
		ucell2.setBorderColor(new BaseColor(130,130,130));
		ucell2.setPaddingLeft(0);
		ucell2.setPaddingTop(0);
		ucell2.setFixedHeight(15f);
		ucell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		
		PdfPCell ucelltc = new PdfPCell(new Paragraph(" °C ", unitlabrow));
		ucelltc.setBackgroundColor(backcellcoltable);
		ucelltc.setBorder(1);
		ucelltc.setBorder(ucelltc.RIGHT);
		ucelltc.setBorderColor(new BaseColor(130, 130, 130));
		ucelltc.setPaddingLeft(0);
		ucelltc.setPaddingTop(0);
		ucelltc.setFixedHeight(15f);
		ucelltc.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucelltc.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		
		PdfPCell ucellhc = new PdfPCell(new Paragraph(" RH ", unitlabrow));
		ucellhc.setBackgroundColor(backcellcoltable);
		ucellhc.setBorder(1);
		ucellhc.setBorder(ucellhc.RIGHT);
		ucellhc.setBorderColor(new BaseColor(130, 130, 130));
		ucellhc.setPaddingLeft(0);
		ucellhc.setPaddingTop(0);
		ucellhc.setFixedHeight(15f);
		ucellhc.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucellhc.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		PdfPCell ucelltb = new PdfPCell(new Paragraph(" °C ", unitlabrow));
		ucelltb.setBackgroundColor(backcellcoltable);
		ucelltb.setBorder(1);
		ucelltb.setBorder(ucelltb.RIGHT);
		ucelltb.setBorderColor(new BaseColor(130, 130, 130));
		ucelltb.setPaddingLeft(0);
		ucelltb.setPaddingTop(0);
		ucelltb.setFixedHeight(15f);
		ucelltb.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucelltb.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		
		PdfPCell ucellhb = new PdfPCell(new Paragraph(" RH ", unitlabrow));
		ucellhb.setBackgroundColor(backcellcoltable);
		ucellhb.setBorder(1);
		ucellhb.setBorder(ucellhb.RIGHT);
		ucellhb.setBorderColor(new BaseColor(130, 130, 130));
		ucellhb.setPaddingLeft(0);
		ucellhb.setPaddingTop(0);
		ucellhb.setFixedHeight(15f);
		ucellhb.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucellhb.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		
		PdfPCell ucelltt = new PdfPCell(new Paragraph(" °C ", unitlabrow));
		ucelltt.setBackgroundColor(backcellcoltable);
		ucelltt.setBorder(1);
		ucelltt.setBorder(ucelltt.RIGHT);
		ucelltt.setBorderColor(new BaseColor(130, 130, 130));
		ucelltt.setPaddingLeft(0);
		ucelltt.setPaddingTop(0);
		ucelltt.setFixedHeight(15f);
		ucelltt.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucelltt.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		
		PdfPCell ucellht = new PdfPCell(new Paragraph(" RH ", unitlabrow));
		ucellht.setBackgroundColor(backcellcoltable);
		ucellht.setBorder(1);
		ucellht.setBorder(ucellht.RIGHT);
		ucellht.setBorderColor(new BaseColor(130, 130, 130));
		ucellht.setPaddingLeft(0);
		ucellht.setPaddingTop(0);
		ucellht.setFixedHeight(15f);
		ucellht.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucellht.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		PdfPCell ucellmass = new PdfPCell(new Paragraph(" g ", unitlabrow));
		ucellmass.setBackgroundColor(backcellcoltable);
		ucellmass.setBorder(1);
		ucellmass.setBorder(ucellmass.RIGHT);
		ucellmass.setBorderColor(new BaseColor(130, 130, 130));
		ucellmass.setPaddingLeft(0);
		ucellmass.setPaddingTop(0);
		ucellmass.setFixedHeight(15f);
		ucellmass.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucellmass.setVerticalAlignment(Element.ALIGN_MIDDLE);
		

		
		tablem.addCell(cell2);
		tablem.addCell(cell6);
		tablem.addCell(cell6b);
		tablem.addCell(cell6t);
		tablem.addCell(cell6svpt);
		tablem.addCell(cell6avpt);
		tablem.addCell(cell6mass);
	

		// unite
		
		tablem.addCell(ucell2);
		tablem.addCell(ucelltc);
		tablem.addCell(ucellhc);
		tablem.addCell(ucelltb);
		tablem.addCell(ucellhb);
		tablem.addCell(ucelltt);
		tablem.addCell(ucellht);
		tablem.addCell(ucellmass);

		
	}
	/* Set Test Data in Table */
	void rowData() {
		
		
		List<String> t = d.getValuesOf("" + d.data.get("t"));
		List<String> tc= d.getValuesOf("" + d.data.get("tChamber"));
		List<String> hc = d.getValuesOf("" + d.data.get("hChamber"));
		List<String> tb= d.getValuesOf("" + d.data.get("tBottom"));
		List<String> hb = d.getValuesOf("" + d.data.get("hBottom"));
		List<String> tt= d.getValuesOf("" + d.data.get("tTop"));
		List<String> ht = d.getValuesOf("" + d.data.get("hTop"));
		List<String> svp= d.getValuesOf("" + d.data.get("svp"));
		List<String> avp = d.getValuesOf("" + d.data.get("avp"));
		List<String> mass= d.getValuesOf("" + d.data.get("mass"));
		

		Font tabledata = FontFactory.getFont("./font/Roboto-Light.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
		tabledata.setColor(new BaseColor(98, 98, 98));

		Font sampleinfoa = FontFactory.getFont(FontFactory.HELVETICA, 11,
				Font.NORMAL, new BaseColor(90, 90, 92));

		PdfPTable tablem = new PdfPTable(10); // 3 columns.
		tablem.setWidthPercentage(100); // Width 100%
		tablem.setSpacingBefore(0f); // Space before table
		tablem.setSpacingAfter(0f); // Space after table

		// Set Column widths
		float[] columnWidths = { 1f, 1f ,1f, 1f ,1f, 1f ,1f, 1f ,1f, 1f };

		try {
			tablem.setWidths(columnWidths);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		addTableHeader(tablem);
		
		List<List<String>> data = new ArrayList<List<String>>();

		for (int k = 0; k < t.size(); k++) {
			List<String> temp = new ArrayList<String>();

			

		//temp.add((k+1)+suffixes[k+1]+ " Bubble");
			temp.add(""+Myapp.getRound(t.get(k), DataStore.getRoundOff()));
			temp.add(""+Myapp.getRound(tc.get(k), DataStore.getRoundOff()));
			temp.add(""+Myapp.getRound(hc.get(k), DataStore.getRoundOff()));
			temp.add(""+Myapp.getRound(tb.get(k), DataStore.getRoundOff()));
			temp.add(""+Myapp.getRound(hb.get(k), DataStore.getRoundOff()));
			temp.add(""+Myapp.getRound(tt.get(k), DataStore.getRoundOff()));
			temp.add(""+Myapp.getRound(ht.get(k), DataStore.getRoundOff()));
			temp.add(""+Myapp.getRound(svp.get(k), DataStore.getRoundOff()));

			temp.add(""+Myapp.getRound(avp.get(k), DataStore.getRoundOff()));

			temp.add(""+Myapp.getRound(mass.get(k), DataStore.getRoundOff()));


			data.add(temp);
		}
		
		BaseColor bordercolor = new BaseColor(130, 130, 130);
		BaseColor backgroundcolor = new BaseColor(230, 230, 230);
		

		for (int j = 0; j < t.size(); j++) {

			
			if (j % 45 == 0 && j != 0) {

				j = j - 1;
				tablem.getRows().remove(tablem.getRows().size() - 1);

				// add last row

				addRowsToTable(tablem, data.get(j), 1, false, true, bordercolor, backgroundcolor, 14f, tabledata);

				j = j + 1;

				try {
					document.add(tablem);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				document.newPage();
				tablem = new PdfPTable(10); // 3 columns.
				tablem.setWidthPercentage(100); // Width 100%
				tablem.setSpacingBefore(0f); // Space before table
				tablem.setSpacingAfter(0f); // Space after table

				try {
					tablem.setWidths(columnWidths);
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				addTableHeader(tablem);

			}
			
			if (j % 2 == 0) {

				addRowsToTable(tablem, data.get(j), 1, false, false, bordercolor, null, 14f, tabledata);

				// withoutbackcolor
			}

			else {
				addRowsToTable(tablem, data.get(j), 1, false, false, bordercolor, backgroundcolor, 14f, tabledata);

				// second column
				// backcolor

			}
			
			if (j == t.size() - 1) {

				tablem.getRows().remove(tablem.getRows().size() - 1);

				if (j % 2 == 0) {
					addRowsToTable(tablem, data.get(j), 1, false, true, bordercolor, null, 14f, tabledata);
				} else {
					addRowsToTable(tablem, data.get(j), 1, false, true, bordercolor, backgroundcolor, 14f, tabledata);
				}

			}	
		
		}

		try {
			document.add(tablem);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	void addRowsToTable(PdfPTable tablem, List<String> data, int borderwidth, boolean isTopBorder,
			boolean isBottomBorder, BaseColor bordercolor, BaseColor backgroundcolor, float rowheight, Font datafont) {

		for (int i = 0; i < data.size(); i++) {

			PdfPCell r11 = new PdfPCell(new Paragraph(data.get(i), datafont));
			r11.setBorder(1);

			if (i == 0) {
				if (isTopBorder) {
					r11.setBorder(r11.LEFT | r11.TOP);

				} else {
					r11.setBorder(r11.LEFT);
				}
				if (isBottomBorder) {
					r11.setBorder(r11.LEFT | r11.BOTTOM);
				} else {
					r11.setBorder(r11.LEFT);
				}
			} else if (i == data.size() - 1) {
				if (isTopBorder) {
					r11.setBorder(r11.RIGHT | r11.TOP);

				} else {
					r11.setBorder(r11.RIGHT);
				}
				if (isBottomBorder) {
					r11.setBorder(r11.RIGHT | r11.BOTTOM);
				} else {
					r11.setBorder(r11.RIGHT);
				}
			} else {
				if (isTopBorder) {
					r11.setBorder(r11.TOP);

				} else if (isBottomBorder) {
					r11.setBorder(r11.BOTTOM);
				} else {
					r11.setBorder(0);
				}
			}
			r11.setBorderColor(bordercolor);
			r11.setBackgroundColor(backgroundcolor);
			r11.setPaddingTop(0);
			r11.setFixedHeight(rowheight);
			r11.setHorizontalAlignment(Element.ALIGN_CENTER);
			r11.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tablem.addCell(r11);

		}

	}



	
	
	/* Header and Footer */
	class HeaderFooterPageEvent extends PdfPageEventHelper {
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 15,
				Font.BOLD, new CMYKColor(92, 17, 0, 15));
		Font titledate = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8,
				Font.ITALIC, new BaseColor(99, 99, 99));

		private PdfTemplate t;

		// private Image total;

		/*
		 * public void onOpenDocument(PdfWriter writer, Document document) { t =
		 * writer.getDirectContent().createTemplate(30, 16); try { total =
		 * Image.getInstance(t); total.setRole(PdfName.ARTIFACT); } catch
		 * (DocumentException de) { throw new ExceptionConverter(de); } }
		 */

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			/*   try {
	                  
	                  //Use this method if you want to get image from your Local system
	                  //Image waterMarkImage = Image.getInstance("E:/tiger.jpg");
	                  
				
				      //Rotating the image 
				       

					Image img = Image.getInstance("logo.jpg");
					//img.setRotation(45); 
				    img.setRotationDegrees(45); 
	                 // String urlOfWaterMarKImage = "https://m19.io/m19/images/logo_black.png";
	                  
	                  
	                  //Get waterMarkImage from some URL
	                 
	                  
	                  //Get width and height of whole page
	                  float pdfPageWidth = document.getPageSize().getWidth()-200;
	                  float pdfPageHeight = document.getPageSize().getHeight()-500;
	 
	                  //Set waterMarkImage on whole page
	                  writer.getDirectContentUnder().addImage(img,
	                               pdfPageWidth, 0, 0, pdfPageHeight, 100, 250);
	 
	           }catch(Exception e){
	                  e.printStackTrace();
	           }*/
			
			
/*
			float absoluteX = document.getPageSize().getWidth() / 2,
					absoluteY = document.getPageSize().getHeight() / 2;
			System.out.format("Pase Sizes W, H : [%s , %s]\n", absoluteX, absoluteY);
			float textX = 200, textY = 200, testAngle = 25;
			ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_LEFT,
					new Phrase("", font), textX, textY, testAngle);
			
			String itextImage = System.getProperty("user.dir") + File.separator + "/images/logo1 - Copy.png";
			try {
				Image image = Image.getInstance( itextImage );
				image.setScaleToFitLineWhenOverflow(false);
				// The image must have absolute positioning.
				float imageWidth = 600, imageHeight = 190;
				image.setAbsolutePosition(50, 150);
				image.scaleAbsolute(imageWidth, imageHeight);
				image.setRotationDegrees(45);
				
		/*		PdfContentByte canvas = writer.getDirectContentUnder();
				 canvas.saveState();
				 
				PdfGState state = new PdfGState();
		        state.setFillOpacity(0.6f);		        
		        canvas.setGState(state);
		        canvas.addImage(image);
		        canvas.restoreState();*/
		        /*
				PdfContentByte pcb = writer.getDirectContentUnder();
				pcb.addImage(image);
		
				 // set transparency
	         /*   PdfGState state = new PdfGState();
	            state.setFillOpacity(0.2f);
	            state.setGState(state);  
	           */ 
	            
	          /*  
			} catch (BadElementException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_LEFT,
					new Phrase("", font), textX, absoluteY + textY, testAngle);
			
			
	        */
			addHeader(writer);
			addFooter(writer);

		}

		private void addHeader(PdfWriter writer) {
			try {
				// set defaults
				PdfPTable table = new PdfPTable(5);
				table.setTotalWidth(670);
				table.setLockedWidth(true);
				table.setWidths(new int[] { 2, 1, 1, 2, 2 });

				Font titleFont = FontFactory.getFont(testnamefont, 14);// Main
																		// Report
																		// Title
																		// name
				titleFont.setColor(getColor(6));

				Font headertestname = FontFactory.getFont(
						"./font/BebasNeue Regular.ttf", BaseFont.IDENTITY_H,
						BaseFont.EMBEDDED, 15);
				headertestname.setColor(getColor(14));

				PdfPCell cell;
				cell = new PdfPCell(new Phrase("ASTM E-96", headertestname));
				cell.setBorder(1);
				cell.setBorder(cell.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(new BaseColor(150, 150, 150));
				cell.setPaddingBottom(3);
				table.addCell(cell);

				Font headerdate = FontFactory.getFont(
						"./font/RobotoCondensed-Italic.ttf",
						BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7.5f);

				cell = new PdfPCell(new Phrase("", headerdate));
				cell.setBorder(1);
				cell.setBorder(cell.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderColor(new BaseColor(150, 150, 150));
				cell.setPaddingBottom(2);
				cell.setColspan(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase());
				cell.setPaddingLeft(10);
				cell.setPaddingBottom(10);
				Image img = Image.getInstance("logo.jpg");
				// img.scalePercent(15);
				// img.scaleToFit(100, 50);
				img.scaleToFit(50, 26);
				cell.setRowspan(2);
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.addElement(new Chunk(img, 0, 4, true));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(BaseColor.GRAY);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(BaseColor.GRAY);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(BaseColor.GRAY);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(BaseColor.GRAY);
				table.addCell(cell);

				// write content
				table.writeSelectedRows(0, -1, 0, 803,
						writer.getDirectContent());
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			} catch (MalformedURLException e) {
				throw new ExceptionConverter(e);
			} catch (IOException e) {
				throw new ExceptionConverter(e);
			}
		}

		private void addFooter(PdfWriter writer) {
			PdfPTable footer = new PdfPTable(3);
			try {
				// set defaults
				footer.setWidths(new int[] { 24, 2, 1 });
				footer.setTotalWidth(527);
				footer.setLockedWidth(true);
				footer.getDefaultCell().setFixedHeight(20);
				footer.getDefaultCell().setBorder(0);
				// footer.getDefaultCell().setBorder(Rectangle.TOP);
				footer.getDefaultCell().setBorderColor(BaseColor.BLACK);

				// add copyright
				footer.addCell(new Phrase("", new Font(
						Font.FontFamily.HELVETICA, 12, Font.BOLD)));

				// add current page count
				footer.getDefaultCell().setHorizontalAlignment(
						Element.ALIGN_RIGHT);
				footer.addCell(new Phrase(String.format("Page %d",
						writer.getPageNumber()), new Font(
						Font.FontFamily.HELVETICA, 8, Font.NORMAL,
						BaseColor.BLACK)));

				// add placeholder for total page count
				PdfPCell totalPageCount = new PdfPCell();
				totalPageCount.setBorder(0);
				// totalPageCount.setBorder(Rectangle.TOP);
				// totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
				footer.addCell(totalPageCount);

				// write page
				PdfContentByte canvas = writer.getDirectContent();
				canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
				footer.writeSelectedRows(0, -1, 34, 50, canvas);
				canvas.endMarkedContentSequence();
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			}
		}

		/*
		 * public void onCloseDocument(PdfWriter writer, Document document) {
		 * int totalLength = String.valueOf(writer.getPageNumber()).length();
		 * int totalWidth = totalLength * 5; ColumnText.showTextAligned(t,
		 * Element.ALIGN_RIGHT, new
		 * Phrase(String.valueOf(writer.getPageNumber()), new
		 * Font(Font.FontFamily.HELVETICA, 8)), totalWidth, 6, 0); }
		 */

	}
}
