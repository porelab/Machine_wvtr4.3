package pdfreport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import application.DataStore;
import application.Myapp;

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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import data_read_write.DatareadN;

public class Multiplepororeport {
	Document document = new Document(PageSize.A4, 36, 36, 90, 36);
	PdfWriter writer;
	List<DatareadN> allfiles;
	DatareadN d;
	
	String imgpath1;

	List<String> flow, dt, p1, p2, dp, ans;

	// Row Data
	BaseColor backcellcoltable = new BaseColor(130, 130, 130);
	BaseColor backcellcol = new BaseColor(230, 230, 230);
	BaseColor backcellcoldark = new BaseColor(122, 122, 122);
	Font tablemean = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD,
			new BaseColor(100, 100, 100));

	Font rowhed = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD,
			new BaseColor(255, 255, 255));

	String companyname;
	String notes;

	Font blueFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9,
			Font.NORMAL, new CMYKColor(0, 0, 0, 0));
	Font blueFont1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD,
			new CMYKColor(255, 255, 255, 0));
	Font normal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8.5f,
			Font.NORMAL, new CMYKColor(255, 255, 255, 0));

	// First page new
	Font filetypes = FontFactory.getFont(FontFactory.HELVETICA, 10,
			Font.NORMAL, new CMYKColor(0f, 0f, 0f, 0f));
	Font testname = FontFactory.getFont(FontFactory.HELVETICA, 25, Font.BOLD,
			new CMYKColor(1f, 0.34f, 0f, 0.29f));
	Font ftime = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.ITALIC,
			new BaseColor(96, 96, 98));
	Font fname = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL,
			new BaseColor(96, 96, 98));

	//
	Font sampleinfoq = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD,
			new BaseColor(81, 81, 83));
	Font sampleinfoa = FontFactory.getFont(FontFactory.HELVETICA, 9,
			Font.NORMAL, new BaseColor(90, 90, 92));

	Font graphtitle = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD,
			getColor(14));
	Font legendtitle = FontFactory.getFont(FontFactory.HELVETICA, 12,
			Font.NORMAL, new BaseColor(96, 96, 98));
	Font legenddot = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);

	// result table data
	Font tablehed = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD,
			new CMYKColor(255, 255, 255, 0));
	Font normal1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.NORMAL,
			new CMYKColor(255, 255, 255, 0));
	Font tableans = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD,
			new BaseColor(255, 255, 255));

	// Notes
	Font noteslab = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD,
			new BaseColor(81, 81, 83));

	// Notes desc
	Font notesdeslab = FontFactory.getFont(FontFactory.HELVETICA, 11,
			Font.NORMAL, new BaseColor(81, 81, 83));

	public static final String FONT = "OpenSansCondensed-Light.ttf";
	public static final String FONTbb = "OpenSansCondensed-Light.ttf";
	public static final String flegend = "FiraSans-Regular.ttf";
	public static final String testtype = "/font/Roboto-Black.ttf";
	public static final String testnamefont = "/font/BebasNeue Book.ttf";

	Font unitlabrow = FontFactory.getFont(FontFactory.HELVETICA, 8,
			Font.NORMAL, new BaseColor(255, 255, 255));

	List<String> clr = new ArrayList<String>();

	public Multiplepororeport() {
		clr.add("#000080");
		clr.add("#0000FF");
		clr.add("#00FFFF");
		clr.add("#008000");
		clr.add("#00FF00");
		clr.add("#FF00FF");
	}
	/* Main Function Create Report */
	public void Report(String path, List<DatareadN> d, String notes,
			String comname, List<String> graphs, Boolean btabledata, Boolean bcoverpage,String imgpath1) {
		allfiles = d;

		this.companyname = comname;
		this.notes = notes;
		this.imgpath1 = imgpath1;

		try {
			writer = PdfWriter
					.getInstance(document, new FileOutputStream(path));

			// add header and footer

			// write to document
			document.open();


			if (bcoverpage == true) {
				coverpage();
			}

			document.newPage();
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			writer.setPageEvent(event);

			boolean isSame=true;
			String sid=d.get(0).data.get("sample").toString();
			for(int i=1;i<d.size();i++)
			{
				if(!sid.equals(d.get(i).data.get("sample").toString()))
				{
					isSame=false;
					break;
				}
			}
			
			
			resulttable(d,isSame);

			document.newPage();

			File folder = new File("mypic");
			File[] listOfFiles = folder.listFiles();
			int nflag = 0;

			for (int i = 0; i < 1; i++) {

				resultgraph(listOfFiles[i]);

				if (i % 2 == 1) {
					document.newPage();

				} else {
					Paragraph pp5 = new Paragraph(15);
					pp5.add(new Chunk("\n"));
					document.add(pp5);

				}
				nflag++;

			}
			File sclaeimg = new File("mypic/scale.png");
			Image imgs = Image.getInstance(sclaeimg.toURI().toString());
			imgs.scaleAbsolute(500, 70);
			imgs.setAbsolutePosition(60f, 300f);
			//document.add(imgs);

			if (btabledata == true) {
				document.newPage();
				rowData(allfiles);
			}

			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/* RGB Color Code */
	BaseColor getColor(int i) {
		List<BaseColor> clrs = new ArrayList<>();
		clrs.add(new BaseColor(219, 186, 79));
		clrs.add(new BaseColor(63, 118, 181));
		clrs.add(new BaseColor(214, 116, 121));
		clrs.add(new BaseColor(18, 181, 159));
		clrs.add(new BaseColor(245, 144, 61));
		clrs.add(new BaseColor(118, 70, 68));
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
	void coverpage() {

		try {

			Image img = Image.getInstance("dddd.jpg");
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
			System.out.println("ghhghghghg"+imgpath1);
			if (!imgpath1.equals("")) {
				img1 = Image.getInstance(imgpath1);
			} else {
				img1 = Image.getInstance("f1.jpg");
			}
			img1.scaleAbsolute(595, 500);
			img1.setAbsolutePosition(0f, 200f);
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

		Font testt = FontFactory.getFont(testtype, 10);// Main Report Title name
		testt.setColor(BaseColor.WHITE);

		Font date = FontFactory.getFont(testtype, 10);// Main Report Title name
		date.setColor(BaseColor.BLACK);

		PdfPCell r3 = new PdfPCell(new Paragraph("BUBBLE POINT TEST REPORT",
				testt));
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
		t22.setBorderWidth(3f);
		t22.setFixedHeight(100f);
		t22.setRowspan(2);
		t22.setHorizontalAlignment(Element.ALIGN_RIGHT);
		t22.setVerticalAlignment(Element.ALIGN_TOP);
		testnametab.addCell(t22);

		PdfPCell t2 = new PdfPCell(new Paragraph("", date));
		t2.setBorder(0);
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

		DateFormat dateFormat = new SimpleDateFormat(" MMMM yyyy|HH:mm aa");
		Calendar cal = Calendar.getInstance();

		// System.out.println(dateFormat.format(cal.getTime()));

		return dayStr + dateFormat.format(cal.getTime());

	}
	/* Set Result Data in Table */
	void resulttable(List<DatareadN> d,boolean isSame) {

		
		BaseColor backcellcoltable = new BaseColor(130, 130, 130);

		Font tablehed = FontFactory.getFont(FontFactory.HELVETICA, 10,
				Font.BOLD, new BaseColor(81, 81, 83));

		Font whitecol = FontFactory.getFont(FontFactory.HELVETICA, 10,
				Font.BOLD, new BaseColor(255, 255, 255));
		
		Font whitecolu = FontFactory.getFont(FontFactory.HELVETICA, 6,
				Font.BOLD, new BaseColor(255, 255, 255));

		Font tablemeanhed = FontFactory.getFont("./font/Roboto-Bold.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 11);
		tablemeanhed.setColor(BaseColor.WHITE);

		Font tablemean = FontFactory.getFont(FontFactory.HELVETICA, 15,
				Font.BOLD, new BaseColor(100, 100, 100));
		tablemean.setColor(BaseColor.WHITE);

		Font meanerror = FontFactory.getFont(FontFactory.HELVETICA, 10,
				Font.BOLD, new BaseColor(100, 100, 100));
		meanerror.setColor(BaseColor.WHITE);

		// Address
		Font addresslab = FontFactory.getFont(FontFactory.HELVETICA, 11,
				Font.NORMAL, new BaseColor(81, 81, 83));

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
		
		PdfPCell a4 = new PdfPCell(new Paragraph("+91 8140308833", addresslab));
		a4.setPaddingLeft(10);
		a4.setPaddingTop(1);
		a4.setBorder(0);
		// a4.setBorder(a4.LEFT | a4.RIGHT);
		a4.setFixedHeight(15f);
		// a4.setBackgroundColor(backcellcoltable1);
		a4.setBorderColor(new BaseColor(130, 130, 130));
		a4.setHorizontalAlignment(Element.ALIGN_RIGHT);
		a4.setVerticalAlignment(Element.ALIGN_MIDDLE);

		addresstable.addCell(a1);
		addresstable.addCell(a2);
		addresstable.addCell(a3);
		addresstable.addCell(a4);

		try {
			document.add(addresstable);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Paragraph pp5 = new Paragraph(15);
		pp5.add(new Chunk("\n"));
		try {
			document.add(pp5);
		} catch (DocumentException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		PdfPTable infotable = new PdfPTable(6); // 3 columns.
		infotable.setWidthPercentage(100f); // Width 100%
		infotable.setSpacingBefore(0f); // Space before table
		infotable.setSpacingAfter(0f); // Space after table

		// Set Column widths
		float[] infotablewidth = { 16, 16, 16, 16, 16, 18 };

		try {
			infotable.setWidths(infotablewidth);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		PdfPCell c6 = new PdfPCell(new Paragraph("Test Date", whitecol));
		c6.setBackgroundColor(getColor(14));
		c6.setBorder(1);
		c6.setBorder(c6.LEFT);
		c6.setBorderColor(new BaseColor(130, 130, 130));
		c6.setPaddingLeft(10);
		c6.setPaddingTop(0);
		c6.setFixedHeight(20f);
		c6.setHorizontalAlignment(Element.ALIGN_CENTER);
		c6.setVerticalAlignment(Element.ALIGN_BOTTOM);
		
		PdfPCell c1 = new PdfPCell(new Paragraph("Sample ID", whitecol));
		c1.setBackgroundColor(getColor(14));
		c1.setBorder(0);
//		c1.setBorder(c1.LEFT);
		c1.setBorderColor(new BaseColor(130, 130, 130));
		c1.setPaddingLeft(10);
		c1.setPaddingTop(0);
		c1.setFixedHeight(20f);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell c2 = new PdfPCell(new Paragraph("Diameter", whitecol));
		c2.setBackgroundColor(getColor(14));
		c2.setBorder(0);
		// c2.setBorder(c2.TOP | c2.BOTTOM | c2.LEFT);
		c2.setBorderColor(getColor(6));
		c2.setPaddingLeft(10);
		c2.setPaddingTop(0);
		c2.setFixedHeight(20f);
		c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		c2.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell c3 = new PdfPCell(new Paragraph("Thickness", whitecol));
		c3.setBackgroundColor(getColor(14));
		c3.setBorder(0);
		// c3.setBorder(c3.TOP | c3.BOTTOM | c3.LEFT);
		c3.setBorderColor(getColor(6));
		c3.setPaddingLeft(10);
		c3.setPaddingTop(0);
		c3.setFixedHeight(20f);
		c3.setHorizontalAlignment(Element.ALIGN_CENTER);
		c3.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell c4 = new PdfPCell(new Paragraph("Wetting Fluid", whitecol));
		c4.setBackgroundColor(getColor(14));
		c4.setBorder(0);
		// c4.setBorder(c4.TOP | c4.BOTTOM | c4.LEFT);
		c4.setBorderColor(getColor(6));
		c4.setPaddingLeft(10);
		c4.setPaddingTop(0);
		c4.setFixedHeight(20f);
		c4.setHorizontalAlignment(Element.ALIGN_CENTER);
		c4.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell c5 = new PdfPCell(new Paragraph("Surface Tension", whitecol));
		c5.setBackgroundColor(getColor(14));
		c5.setBorder(1);
		c5.setBorder(c5.RIGHT);
		c5.setBorderColor(new BaseColor(130, 130, 130));
		c5.setPaddingLeft(10);
		c5.setPaddingTop(0);
		c5.setFixedHeight(20f);
		c5.setHorizontalAlignment(Element.ALIGN_CENTER);
		c5.setVerticalAlignment(Element.ALIGN_BOTTOM);



		infotable.addCell(c6);
		infotable.addCell(c1);
		infotable.addCell(c2);
		infotable.addCell(c3);
		infotable.addCell(c4);
		infotable.addCell(c5);

		/*Add Unite*/
		
		PdfPCell c66 = new PdfPCell(new Paragraph("", whitecolu));
		c66.setBackgroundColor(getColor(14));
		c66.setBorder(1);
		c66.setBorder(c66.LEFT);
		c66.setBorderColor(new BaseColor(130, 130, 130));
		c66.setPaddingLeft(10);
		c66.setPaddingTop(0);
		c66.setFixedHeight(10f);
		c66.setHorizontalAlignment(Element.ALIGN_CENTER);
		c66.setVerticalAlignment(Element.ALIGN_TOP);
		
		PdfPCell c11 = new PdfPCell(new Paragraph("", whitecolu));
		c11.setBackgroundColor(getColor(14));
		c11.setBorder(0);
//		c11.setBorder(c11.LEFT);
		c11.setBorderColor(new BaseColor(130, 130, 130));
		c11.setPaddingLeft(10);
		c11.setPaddingTop(0);
		c11.setFixedHeight(10f);
		c11.setHorizontalAlignment(Element.ALIGN_CENTER);
		c11.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell c22 = new PdfPCell(new Paragraph(" (cm)", whitecolu));
		c22.setBackgroundColor(getColor(14));
		c22.setBorder(0);
		// c22.setBorder(c22.TOP | c22.BOTTOM | c22.LEFT);
		c22.setBorderColor(getColor(6));
		c22.setPaddingLeft(10);
		c22.setPaddingTop(0);
		c22.setFixedHeight(10f);
		c22.setHorizontalAlignment(Element.ALIGN_CENTER);
		c22.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell c33 = new PdfPCell(new Paragraph("(cm)", whitecolu));
		c33.setBackgroundColor(getColor(14));
		c33.setBorder(0);
		// c33.setBorder(c33.TOP | c33.BOTTOM | c33.LEFT);
		c33.setBorderColor(getColor(6));
		c33.setPaddingLeft(10);
		c33.setPaddingTop(0);
		c33.setFixedHeight(10f);
		c33.setHorizontalAlignment(Element.ALIGN_CENTER);
		c33.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell c44 = new PdfPCell(new Paragraph("", whitecolu));
		c44.setBackgroundColor(getColor(14));
		c44.setBorder(0);
		// c44.setBorder(c44.TOP | c44.BOTTOM | c44.LEFT);
		c44.setBorderColor(getColor(6));
		c44.setPaddingLeft(10);
		c44.setPaddingTop(0);
		c44.setFixedHeight(10f);
		c44.setHorizontalAlignment(Element.ALIGN_CENTER);
		c44.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell c55 = new PdfPCell(new Paragraph("(dyne/cm)", whitecolu));
		c55.setBackgroundColor(getColor(14));
		c55.setBorder(1);
		c55.setBorder(c55.RIGHT);
		c55.setBorderColor(new BaseColor(130, 130, 130));
		c55.setPaddingLeft(10);
		c55.setPaddingTop(0);
		c55.setHorizontalAlignment(Element.ALIGN_CENTER);
		c55.setVerticalAlignment(Element.ALIGN_TOP);



		infotable.addCell(c66);
		infotable.addCell(c11);
		infotable.addCell(c22);
		infotable.addCell(c33);
		infotable.addCell(c44);
		infotable.addCell(c55);

		
		Font tabledata = FontFactory.getFont("./font/Roboto-Light.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
		tablehed.setColor(new BaseColor(98, 98, 98));

		Font sampleinfoa = FontFactory.getFont(FontFactory.HELVETICA, 11,
				Font.NORMAL, new BaseColor(90, 90, 92));

		for (int j = 0; j < d.size(); j++) {

		

			DatareadN dr1 = d.get(j);
			// first column
			String st = "" + dr1.data.get("sample");

			String sampledia = Myapp
					.getRound(
							Double.parseDouble(""
									+ dr1.data.get("samplediameter")), 2);
			String samplethik = Myapp.getRound(
					Double.parseDouble("" + dr1.data.get("thikness")), 2);
			String testfluid = "" + dr1.data.get("fluidname");
			String surfacet = Myapp.getRound(
					Double.parseDouble("" + dr1.data.get("fluidvalue")), 2);
			String testdate = "" + dr1.data.get("testdate");
			
        BaseColor backcellcol;

			
			if (j % 2 == 0) {
			
				
				backcellcol = new BaseColor(255, 255, 255);
				
				

			} else {

				
				backcellcol = new BaseColor(230, 230, 230);
				

			}
			
			
			
			
			PdfPCell record6 = new PdfPCell(new Paragraph("" + testdate,
					tabledata));
			record6.setBorder(1);
			record6.setBorder(record6.LEFT);
			record6.setBorderColor(new BaseColor(130, 130, 130));
			record6.setBackgroundColor(backcellcol);
			record6.setPaddingTop(0);
			record6.setFixedHeight(30f);
			record6.setHorizontalAlignment(Element.ALIGN_CENTER);
			record6.setVerticalAlignment(Element.ALIGN_MIDDLE);
			infotable.addCell(record6);

			
			PdfPCell record1 = new PdfPCell(new Paragraph(st, tabledata));
			record1.setBorder(0);
			//record1.setBorder(record1.LEFT);
			record1.setBorderColor(new BaseColor(130, 130, 130));
			record1.setBackgroundColor(backcellcol);
			record1.setPaddingTop(0);
			record1.setFixedHeight(30f);
			record1.setHorizontalAlignment(Element.ALIGN_CENTER);
			record1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			infotable.addCell(record1);


			// second column
			PdfPCell record2 = new PdfPCell(new Paragraph(
					sampledia + " ", tabledata));
			record2.setBorder(0);
			// record2.setBorder(record2.LEFT);
			record2.setBorderColor(getColor(6));
			record2.setBackgroundColor(backcellcol);
			record2.setPaddingTop(0);
			record2.setFixedHeight(30f);
			record2.setHorizontalAlignment(Element.ALIGN_CENTER);
			record2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			infotable.addCell(record2);

			// second column
			PdfPCell record3 = new PdfPCell(new Paragraph(samplethik
					+ "", tabledata));
			record3.setBorder(0);
			// record3.setBorder(record3.LEFT);
			record3.setBorderColor(getColor(6));
			record3.setBackgroundColor(backcellcol);
			record3.setPaddingTop(0);
			record3.setFixedHeight(30f);
			record3.setHorizontalAlignment(Element.ALIGN_CENTER);
			record3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			infotable.addCell(record3);

			PdfPCell record4 = new PdfPCell(new Paragraph("" + testfluid,
					tabledata));
			record4.setBorder(0);
			// record4.setBorder(record4.LEFT);
			record4.setBorderColor(getColor(6));
			record4.setBackgroundColor(backcellcol);
			record4.setPaddingTop(0);
			record4.setFixedHeight(30f);
			record4.setHorizontalAlignment(Element.ALIGN_CENTER);
			record4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			infotable.addCell(record4);

			PdfPCell record5 = new PdfPCell(new Paragraph("" + surfacet,
					tabledata));
			record5.setBorder(0);
			record5.setBorder(record5.RIGHT);
			record5.setBorderColor(new BaseColor(130, 130, 130));
			record5.setBackgroundColor(backcellcol);
			record5.setPaddingTop(0);
			record5.setFixedHeight(30f);
			record5.setHorizontalAlignment(Element.ALIGN_CENTER);
			record5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			infotable.addCell(record5);
		}

		PdfPCell bootom = new PdfPCell(new Paragraph("", tablemean));
		bootom.setBorder(1);
		bootom.setBorder(bootom.TOP);
		bootom.setBorderColor(new BaseColor(130, 130, 130));
		bootom.setPaddingTop(4);
		bootom.setFixedHeight(20f);
		bootom.setColspan(6);
		bootom.setHorizontalAlignment(Element.ALIGN_CENTER);
		bootom.setVerticalAlignment(Element.ALIGN_MIDDLE);
		infotable.addCell(bootom);

		try {
			document.add(infotable);

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPTable tablem = new PdfPTable(3); // 3 columns.
		tablem.setWidthPercentage(100); // Width 100%
		tablem.setSpacingBefore(0f); // Space before table
		tablem.setSpacingAfter(0f); // Space after table

		// Set Column widths
		float[] columnWidths = { 1f, 1f, 1f };

		try {
			tablem.setWidths(columnWidths);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PdfPCell cell1 = new PdfPCell(new Paragraph("Sample ID", whitecol));
		cell1.setBackgroundColor(getColor(14));
		cell1.setBorder(0);
		// cell1.setBorder(cell1.TOP | cell1.BOTTOM);
		cell1.setBorderColor(getColor(6));
		cell1.setPaddingLeft(10);
		cell1.setPaddingTop(0);
		cell1.setFixedHeight(20f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell cell4 = new PdfPCell(
				new Paragraph("Bubble Pressure", whitecol));
		cell4.setBackgroundColor(getColor(14));
		cell4.setBorder(0);
		// cell4.setBorder(cell4.TOP | cell4.BOTTOM | cell4.LEFT);
		cell4.setBorderColor(getColor(6));
		cell4.setPaddingLeft(10);
		cell4.setPaddingTop(0);
		cell4.setFixedHeight(20f);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell cell5 = new PdfPCell(
				new Paragraph("Bubble Diameter", whitecol));
		cell5.setBackgroundColor(getColor(14));
		cell5.setBorder(0);
		// cell5.setBorder(cell5.TOP | cell5.BOTTOM | cell5.LEFT);
		cell5.setBorderColor(getColor(6));
		cell5.setPaddingLeft(10);
		cell5.setPaddingTop(0);
		cell5.setFixedHeight(20f);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setVerticalAlignment(Element.ALIGN_BOTTOM);

		tablem.addCell(cell1);
		tablem.addCell(cell4);
		tablem.addCell(cell5);
		
		
		PdfPCell cell11 = new PdfPCell(new Paragraph("", whitecolu));
		cell11.setBackgroundColor(getColor(14));
		cell11.setBorder(0);
		// cell11.setBorder(cell11.TOP | cell11.BOTTOM);
		cell11.setBorderColor(getColor(6));
		cell11.setPaddingLeft(10);
		cell11.setPaddingTop(0);
		cell11.setFixedHeight(10f);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell cell44 = new PdfPCell(
				new Paragraph("(" + DataStore.getUnitepressure() + ")", whitecolu));
		cell44.setBackgroundColor(getColor(14));
		cell44.setBorder(0);
		// cell44.setBorder(cell44.TOP | cell44.BOTTOM | cell44.LEFT);
		cell44.setBorderColor(getColor(6));
		cell44.setPaddingLeft(10);
		cell44.setPaddingTop(0);
		cell44.setFixedHeight(10f);
		cell44.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell44.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell cell55 = new PdfPCell(
				new Paragraph("(" + DataStore.getUnitediameter() + ")", whitecolu));
		cell55.setBackgroundColor(getColor(14));
		cell55.setBorder(0);
		// cell55.setBorder(cell55.TOP | cell55.BOTTOM | cell55.LEFT);
		cell55.setBorderColor(getColor(6));
		cell55.setPaddingLeft(10);
		cell55.setPaddingTop(0);
		cell55.setFixedHeight(10f);
		cell55.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell55.setVerticalAlignment(Element.ALIGN_TOP);

		tablem.addCell(cell11);
		tablem.addCell(cell44);
		tablem.addCell(cell55);
		

		List<Double> bubbplist=new ArrayList<Double>();
		List<Double> bubbdlist=new ArrayList<Double>();

		for (int j = 0; j < d.size(); j++) {

			tablehed.setColor(new BaseColor(98, 98, 98));

			DatareadN dr = d.get(j);
			// first column
			String st = "" + dr.data.get("sample");
			// st = st.substring(0, st.indexOf('.'));
			String bpp = Myapp.getRound(
					Double.parseDouble("" + dr.data.get("bpressure")), 2);
			String bpd = Myapp.getRound(
					Double.parseDouble("" + dr.data.get("bdiameter")), 2);
			BaseColor backcellcol;

			
			
			if(isSame)
			{
				bubbplist.add(Double.parseDouble(bpp));
				bubbdlist.add(Double.parseDouble(bpd));
				
			}

			
			if (j % 2 == 0) {
			
				
				backcellcol = new BaseColor(255, 255, 255);
				
				

			} else {

				
				backcellcol = new BaseColor(230, 230, 230);
				

			}
			
			
			PdfPCell record1 = new PdfPCell(new Paragraph(st, tabledata));
			record1.setBorder(1);
			record1.setBorder(record1.LEFT);
			record1.setBorderColor(new BaseColor(130, 130, 130));
			record1.setBackgroundColor(backcellcol);
			record1.setPaddingTop(0);
			record1.setFixedHeight(30f);
			record1.setHorizontalAlignment(Element.ALIGN_CENTER);
			record1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tablem.addCell(record1);


			PdfPCell record4 = new PdfPCell(new Paragraph(bpp + " ",
					tabledata));
			record4.setBorder(0);
			// record4.setBorder(record4.LEFT);
			record4.setBorderColor(getColor(6));
			record4.setBackgroundColor(backcellcol);
			record4.setPaddingTop(0);
			record4.setFixedHeight(30f);
			record4.setHorizontalAlignment(Element.ALIGN_CENTER);
			record4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tablem.addCell(record4);

			PdfPCell record5 = new PdfPCell(new Paragraph(bpd + "",
					tabledata));
			record5.setBorder(1);
			record5.setBorder(record5.RIGHT);
			record5.setBorderColor(new BaseColor(130, 130, 130));
			record5.setBackgroundColor(backcellcol);
			record5.setPaddingTop(0);
			record5.setFixedHeight(30f);
			record5.setHorizontalAlignment(Element.ALIGN_CENTER);
			record5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tablem.addCell(record5);
		}

		PdfPCell bootom1 = new PdfPCell(new Paragraph("", tablemean));
		bootom1.setBorder(1);
		bootom1.setBorder(bootom1.TOP);
		bootom1.setBorderColor(new BaseColor(130, 130, 130));
		bootom1.setPaddingTop(0);
		bootom1.setFixedHeight(5f);
		bootom1.setColspan(6);
		bootom1.setHorizontalAlignment(Element.ALIGN_CENTER);
		bootom1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tablem.addCell(bootom1);

		if(isSame)
		{
	
			Double bpd = bubbdlist.stream().mapToDouble(val -> val).average().orElse(0.0);
			double bpdstd=getSd(bubbdlist, bpd);	
			
			Double bpp = bubbplist.stream().mapToDouble(val -> val).average().orElse(0.0);
			double bppstd=getSd(bubbplist, bpp);	
			
			PdfPCell record1 = new PdfPCell(new Paragraph("Average +- SD", whitecol));
			record1.setBorder(1);
			record1.setBorder(record1.LEFT | record1.TOP |record1.BOTTOM);
			record1.setBorderColor(new BaseColor(130, 130, 130));
			record1.setBackgroundColor(backcellcoldark);
			record1.setPaddingTop(0);
			record1.setFixedHeight(30f);
			record1.setHorizontalAlignment(Element.ALIGN_CENTER);
			record1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tablem.addCell(record1);

	

			PdfPCell record4 = new PdfPCell(new Paragraph(bpp + " +- "+bppstd,
					whitecol));
			record4.setBorder(0);
			record4.setBorder(record4.TOP | record4.BOTTOM);
			record4.setBorderColor(new BaseColor(130, 130, 130));
			record4.setBackgroundColor(backcellcoldark);
			record4.setPaddingTop(0);
			record4.setFixedHeight(30f);
			record4.setHorizontalAlignment(Element.ALIGN_CENTER);
			record4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tablem.addCell(record4);

			PdfPCell record5 = new PdfPCell(new Paragraph(bpd + " +- "+bpdstd,
					whitecol));
			record5.setBorder(1);
			record5.setBorder(record5.TOP | record5.BOTTOM| record5.RIGHT);
			record5.setBorderColor(new BaseColor(130, 130, 130));
			record5.setBackgroundColor(backcellcoldark);
			record5.setPaddingTop(0);
			record5.setFixedHeight(30f);
			record5.setHorizontalAlignment(Element.ALIGN_CENTER);
			record5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tablem.addCell(record5);
			
			
			
		
			
		}
		
		
		
		try {
			document.add(tablem);

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPTable notestable = new PdfPTable(1); // 4 columns.
		notestable.setWidthPercentage(100); // Width 100%
		notestable.setSpacingBefore(5f); // Space before table
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
		n3.setFixedHeight(160f);
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

		// Footer conten

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
				"* * ISO : 17025 Acceredited Laboratories * *", sampleinfoq));
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
						"Sample specimen are not drawn by M19 Lab. Result relates to the sample tested. The report shall not be reproduced except in full, without the written approval of the laboratory. The report is strically confidential and technical inforamtion of client only. Not for advertisement, promotion, publicity or litigation.",
						addresslab));
		d2.setPaddingLeft(0);
		d2.setPaddingTop(1);
		d2.setBorder(0);
		// d2.setBorder(d2.LEFT | d2.RIGHT |d2.BOTTOM | d2.TOP);
		d2.setFixedHeight(50f);
		// d2.setBackgroundColor(backcellcoltable1);
		d2.setBorderColor(new BaseColor(130, 130, 130));
		d2.setHorizontalAlignment(Element.ALIGN_CENTER);
		d2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		disctable.addCell(d1);
		disctable.addCell(d2);

		writeFooterTable(writer, document, disctable);

		try {
			document.add(new Paragraph("\n"));
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}
	/* Display Graph */
	void resultgraph(File f) {
		try {

			Font graphtitle = FontFactory.getFont(
					"./font/Montserrat-SemiBold.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED, 14);
			graphtitle.setColor(getColor(14));

			Paragraph pp5 = new Paragraph(35);
			// pp5.add(new Chunk("\n"));
			// document.add(pp5);

			PdfPTable resulttable = new PdfPTable(4); // 4 columns.
			resulttable.setWidthPercentage(100); // Width 100%

			resulttable.setWidths(new int[] { 25, 25, 25, 25 });

			String imagefilename = f.getName().substring(0,
					f.getName().indexOf('.'));

			PdfPCell r1 = new PdfPCell(new Paragraph(f.getName().substring(1,
					imagefilename.length()), graphtitle));
			r1.setBorder(0);
			r1.setColspan(4);
			r1.setBorderColor(getColor(14));
			r1.setPaddingTop(4);
			r1.setFixedHeight(20f);
			r1.setHorizontalAlignment(Element.ALIGN_CENTER);
			r1.setVerticalAlignment(Element.ALIGN_CENTER);

			resulttable.addCell(r1);

			document.add(resulttable);

			Image image = Image.getInstance(f.getAbsolutePath());
			image.scaleAbsolute(520, 280);

			document.add(image);

			PdfPTable legendtab = new PdfPTable(12); // 4 columns.
			legendtab.setWidthPercentage(100); // Width 100%
			legendtab.setWidths(new int[] { 4, 12, 4, 13, 4, 13, 4, 13, 4, 13,
					4, 12 });

			Font lt = FontFactory.getFont(flegend, 8);// Main Report Title name
			lt.setColor(new BaseColor(96, 96, 98));

			for (int i = 1; i <= allfiles.size(); i++) {

				System.out.println((i % 6) + "");
				if ((i == 1 && i == allfiles.size())
						|| (i == 7 && i == allfiles.size())) {

					PdfContentByte cb = writer.getDirectContent();

					PdfTemplate p = cb.createTemplate(20, 20);
					// p.setRGBColorFill(0xFF, 0x00, 0x00);
					p.setColorFill(getColor(i - 1));
					p.circle(10, 10, 5);
					p.fill();

					Image img = Image.getInstance(p);
					System.out.println("Hello");

					PdfPCell l1 = new PdfPCell(img);
					l1.setBorder(1);
					l1.setBorder(l1.BOTTOM | l1.TOP);
					l1.setBorderColor(new BaseColor(150, 150, 150));
					l1.setFixedHeight(21f);
					l1.setHorizontalAlignment(Element.ALIGN_RIGHT);
					l1.setVerticalAlignment(Element.ALIGN_CENTER);
					legendtab.addCell(l1);

					PdfPCell l2 = new PdfPCell(new Paragraph(""
							+ allfiles.get(i - 1).filename, lt));
					l2.setBorder(1);
					l2.setBorder(l2.BOTTOM | l2.TOP | l2.RIGHT);
					l2.setBorderColor(new BaseColor(150, 150, 150));
					l2.setFixedHeight(21f);
					l2.setHorizontalAlignment(Element.ALIGN_LEFT);
					l2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					legendtab.addCell(l2);

					PdfPCell l3 = new PdfPCell(new Paragraph("", graphtitle));
					l3.setBorder(1);
					l3.setColspan(10);
					l3.setBorder(l2.BOTTOM | l2.TOP);
					l3.setBorderColor(new BaseColor(150, 150, 150));
					l3.setFixedHeight(21f);
					l3.setHorizontalAlignment(Element.ALIGN_LEFT);
					l3.setVerticalAlignment(Element.ALIGN_CENTER);
					legendtab.addCell(l3);
				} else if (i == 1 || i == 7 || i == 13) {

					PdfContentByte cb = writer.getDirectContent();
					PdfTemplate p = cb.createTemplate(20, 20);
					// p.setRGBColorFill(0xFF, 0x00, 0x00);
					p.setColorFill(getColor(i - 1));
					p.circle(10, 10, 5);
					p.fill();

					Image img = Image.getInstance(p);
					System.out.println("Hello");

					PdfPCell l2 = new PdfPCell(img);
					l2.setBorder(1);
					l2.setBorder(l2.BOTTOM | l2.TOP);
					l2.setBorderColor(new BaseColor(150, 150, 150));
					l2.setFixedHeight(21f);
					l2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					l2.setVerticalAlignment(Element.ALIGN_CENTER);
					legendtab.addCell(l2);

					PdfPCell l1 = new PdfPCell(new Paragraph(""
							+ allfiles.get(i - 1).data.get("sample"), lt));
					l1.setBorder(1);
					l1.setBorder(l1.BOTTOM | l1.TOP);
					l1.setBorderColor(new BaseColor(150, 150, 150));
					l1.setFixedHeight(21f);
					l1.setHorizontalAlignment(Element.ALIGN_LEFT);
					l1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					legendtab.addCell(l1);

				} else {
					if (i == allfiles.size()) {

						if (i % 6 == 0 && i == 6) {

							PdfContentByte cb = writer.getDirectContent();

							PdfTemplate p = cb.createTemplate(20, 20);
							// p.setRGBColorFill(0xFF, 0x00, 0x00);
							p.setColorFill(getColor(i - 1));
							p.circle(10, 10, 5);
							p.fill();

							Image img = Image.getInstance(p);
							System.out.println("Hello");

							PdfPCell l1 = new PdfPCell(img);
							l1.setBorder(1);
							l1.setBorder(l1.BOTTOM | l1.TOP | l1.LEFT);
							l1.setBorderColor(new BaseColor(150, 150, 150));
							l1.setFixedHeight(21f);
							l1.setHorizontalAlignment(Element.ALIGN_RIGHT);
							l1.setVerticalAlignment(Element.ALIGN_CENTER);
							legendtab.addCell(l1);

							PdfPCell l2 = new PdfPCell(new Paragraph(""
									+ allfiles.get(i - 1).filename, lt));
							l2.setBorder(1);
							l2.setBorder(l2.BOTTOM | l2.TOP);
							l2.setBorderColor(new BaseColor(150, 150, 150));
							l2.setFixedHeight(21f);
							l2.setHorizontalAlignment(Element.ALIGN_RIGHT);
							l2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							legendtab.addCell(l2);

						} else {

							PdfContentByte cb = writer.getDirectContent();

							PdfTemplate p = cb.createTemplate(20, 20);
							// p.setRGBColorFill(0xFF, 0x00, 0x00);
							p.setColorFill(getColor(i - 1));
							p.circle(10, 10, 5);
							p.fill();

							Image img = Image.getInstance(p);
							System.out.println("Hello");

							PdfPCell l1 = new PdfPCell(img);
							l1.setBorder(1);
							l1.setBorder(l1.BOTTOM | l1.TOP | l1.LEFT);
							l1.setBorderColor(new BaseColor(150, 150, 150));
							l1.setFixedHeight(21f);
							l1.setHorizontalAlignment(Element.ALIGN_RIGHT);
							l1.setVerticalAlignment(Element.ALIGN_CENTER);
							legendtab.addCell(l1);

							PdfPCell l2 = new PdfPCell(new Paragraph(""
									+ allfiles.get(i - 1).filename, lt));
							l2.setBorder(1);
							l2.setColspan((12 - (i % 6)) + 1);
							l2.setBorder(l2.BOTTOM | l2.TOP);
							l2.setBorderColor(new BaseColor(150, 150, 150));
							l2.setFixedHeight(21f);
							l2.setHorizontalAlignment(Element.ALIGN_LEFT);
							l2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							legendtab.addCell(l2);

						}

					} else {
						PdfContentByte cb = writer.getDirectContent();

						PdfTemplate p = cb.createTemplate(20, 20);
						// p.setRGBColorFill(0xFF, 0x00, 0x00);
						p.setColorFill(getColor(i - 1));
						p.circle(10, 10, 5);
						p.fill();

						Image img = Image.getInstance(p);
						System.out.println("Hello");

						PdfPCell l1 = new PdfPCell(img);
						l1.setBorder(1);
						l1.setBorder(l1.BOTTOM | l1.TOP | l1.LEFT);
						l1.setBorderColor(new BaseColor(150, 150, 150));
						l1.setFixedHeight(21f);
						l1.setHorizontalAlignment(Element.ALIGN_RIGHT);
						l1.setVerticalAlignment(Element.ALIGN_CENTER);
						legendtab.addCell(l1);

						PdfPCell l2 = new PdfPCell(new Paragraph(""
								+ allfiles.get(i - 1).filename, lt));
						l2.setBorder(1);
						l2.setBorder(l2.BOTTOM | l2.TOP);
						l2.setBorderColor(new BaseColor(150, 150, 150));
						l2.setFixedHeight(21f);
						l2.setHorizontalAlignment(Element.ALIGN_LEFT);
						l2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						legendtab.addCell(l2);

					}

				}

			}

			document.add(legendtab);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("File is created");
		// document.newPage();

	}
	/* Row Data Table Header in Title name and unite */
	void addTableHeader(PdfPTable tablem) {
		PdfPCell cell1 = new PdfPCell(new Paragraph("Flow", rowhed));
		cell1.setBackgroundColor(backcellcoltable);
		cell1.setBorder(1);
		cell1.setBorder(cell1.LEFT);
		cell1.setBorderColor(new BaseColor(130, 130, 130));
		cell1.setPaddingLeft(0);
		cell1.setPaddingTop(0);
		cell1.setFixedHeight(30f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell2 = new PdfPCell(
				new Paragraph("Differential Time", rowhed));
		cell2.setBackgroundColor(backcellcoltable);
		cell2.setBorder(0);
		// cell2.setBorder(cell2.TOP | cell2.BOTTOM | cell2.LEFT);
		cell2.setBorderColor(getColor(6));
		cell2.setPaddingLeft(0);
		cell2.setPaddingTop(0);
		cell2.setFixedHeight(30f);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell5 = new PdfPCell(new Paragraph("Differential Pressure",
				rowhed));
		cell5.setBackgroundColor(backcellcoltable);
		cell5.setBorder(0);
		// cell5.setBorder(cell5.TOP | cell5.BOTTOM | cell5.LEFT);
		cell5.setBorderColor(getColor(6));
		cell5.setPaddingLeft(0);
		cell5.setPaddingTop(0);
		cell5.setFixedHeight(30f);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell6 = new PdfPCell(new Paragraph("F/PT", rowhed));
		cell6.setBackgroundColor(backcellcoltable);
		cell6.setBorder(0);
		// cell6.setBorder(cell6.TOP | cell6.BOTTOM | cell6.LEFT);
		cell6.setBorderColor(getColor(6));
		cell6.setPaddingLeft(0);
		cell6.setPaddingTop(0);
		cell6.setFixedHeight(30f);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

		// Units

		PdfPCell ucell1 = new PdfPCell(new Paragraph("(" + DataStore.getUniteflow() + ")", unitlabrow));
		ucell1.setBackgroundColor(backcellcoltable);
		ucell1.setBorder(1);
		ucell1.setBorder(ucell1.LEFT);
		ucell1.setBorderColor(new BaseColor(130, 130, 130));
		ucell1.setPaddingLeft(0);
		ucell1.setPaddingTop(0);
		ucell1.setFixedHeight(15f);
		ucell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucell1.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell ucell2 = new PdfPCell(new Paragraph("(sec)", unitlabrow));
		ucell2.setBackgroundColor(backcellcoltable);
		ucell2.setBorder(0);
		// ucell2.setBorder(ucell2.TOP | ucell2.BOTTOM | ucell2.LEFT);
		ucell2.setBorderColor(getColor(6));
		ucell2.setPaddingLeft(0);
		ucell2.setPaddingTop(0);
		ucell2.setFixedHeight(15f);
		ucell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucell2.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell ucell3 = new PdfPCell(new Paragraph("(" + DataStore.getUnitepressure() + ")", unitlabrow));
		ucell3.setBackgroundColor(backcellcoltable);
		ucell3.setBorder(0);
		// ucell3.setBorder(ucell3.TOP | ucell3.BOTTOM | ucell3.LEFT);
		ucell3.setBorderColor(getColor(6));
		ucell3.setPaddingLeft(0);
		ucell3.setPaddingTop(0);
		ucell3.setFixedHeight(15f);
		ucell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucell3.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell ucell9 = new PdfPCell(new Paragraph("", unitlabrow));
		ucell9.setBackgroundColor(backcellcoltable);
		ucell9.setBorder(1);
		ucell9.setBorder(ucell9.RIGHT);
		ucell9.setBorderColor(new BaseColor(130, 130, 130));
		ucell9.setPaddingLeft(0);
		ucell9.setPaddingTop(0);
		ucell9.setFixedHeight(15f);
		ucell9.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucell9.setVerticalAlignment(Element.ALIGN_TOP);

		tablem.addCell(cell1);
		tablem.addCell(cell5);
		//tablem.addCell(cell2);
		tablem.addCell(cell6);

		// unite
		tablem.addCell(ucell1);
		tablem.addCell(ucell3);
		//tablem.addCell(ucell2);
		tablem.addCell(ucell9);

	}
	/* Set Test Data in Table */
	void rowData(List<DatareadN> d) {

		for (int k = 0; k < d.size(); k++) {

			Font sampleinfoa = FontFactory.getFont(FontFactory.HELVETICA, 11,
					Font.NORMAL, new BaseColor(90, 90, 92));

			DatareadN dr1 = d.get(k);
			// first column
			String st = "" + dr1.data.get("sample");

			document.newPage();

			Font headertestname = FontFactory.getFont(
					"./font/BebasNeue Regular.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED, 15);
			headertestname.setColor(getColor(14));

			try {

				Chunk redText = new Chunk("File name : " + st, headertestname);

				Paragraph p1 = new Paragraph(redText);
				document.add(p1);
			} catch (DocumentException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			flow = dr1.getValuesOf("" + dr1.data.get("flow"));
			dp = dr1.getValuesOf("" + dr1.data.get("Dp"));
			ans = dr1.getValuesOf("" + dr1.data.get("ans"));

			PdfPTable tablem = new PdfPTable(3); // 3 columns.
			tablem.setWidthPercentage(100); // Width 100%
			tablem.setSpacingBefore(10f); // Space before table
			tablem.setSpacingAfter(0f); // Space after table

			Font tabledata = FontFactory.getFont("./font/Roboto-Light.ttf",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 9);
			tabledata.setColor(new BaseColor(98, 98, 98));

			// Set Column widths
			float[] columnWidths = { 1f, 1f, 1f };

			try {
				tablem.setWidths(columnWidths);
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			addTableHeader(tablem);
			
			List<List<String>> data = new ArrayList<List<String>>();

			for (int s = 0; s < flow.size(); s++) {
				List<String> temp = new ArrayList<String>();

				
				String wetflow = ""+DataStore.ConvertFlow(flow.get(s));

				temp.add(wetflow);
				
				
				String press = ""+DataStore.ConvertPressure(dp.get(s));

				temp.add(press);

				

				temp.add(""+Myapp.getRound(ans.get(s), DataStore.getRoundOff()));
				data.add(temp);
			}
			
			BaseColor bordercolor = new BaseColor(130, 130, 130);
			BaseColor backgroundcolor = new BaseColor(230, 230, 230);

			for (int j = 0; j < flow.size(); j++) {

				if (j % 45 == 0 && j != 0) {

					j = j - 1;
					tablem.getRows().remove(tablem.getRows().size() - 1);

					addRowsToTable(tablem, data.get(j), 1, false, true, bordercolor, backgroundcolor, 14f, tabledata);

					j = j + 1;

					try {
						document.add(tablem);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					document.newPage();
					tablem = new PdfPTable(3); // 3 columns.
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

				if (j == flow.size() - 1) {

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

	
	
	/*Set test in Footer */
	private void writeFooterTable(PdfWriter writer, Document document,
			PdfPTable table) {
		final int FIRST_ROW = 0;
		final int LAST_ROW = -1;
		// Table must have absolute width set.
		if (table.getTotalWidth() == 0) {
			table.setTotalWidth((document.right() - document.left())
					* table.getWidthPercentage() / 100f);

		}
		table.writeSelectedRows(FIRST_ROW, LAST_ROW, document.left(),
				document.bottom() + table.getTotalHeight() + 20,
				writer.getDirectContent());
	}

	double getSd(List<Double> ls, Double mean) {
		List<Double> sq = new ArrayList<Double>();
		double sqmean = 0;
		for (int i = 0; i < ls.size(); i++) {
			double dd = ls.get(i) - mean;
			sq.add(dd * dd);
			sqmean = sqmean + sq.get(i);
		}

		sqmean = sqmean / sq.size();
		sqmean = Math.sqrt(sqmean);
		double sp3 = Math.round(Double.parseDouble("" + sqmean) * 10000) / 10000D;
		sqmean = sp3;

		return sqmean;
	}
	/* Header and Footer */
	class HeaderFooterPageEvent extends PdfPageEventHelper {
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 15,
				Font.BOLD, new CMYKColor(92, 17, 0, 15));
		Font titledate = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8,
				Font.ITALIC, new BaseColor(99, 99, 99));

		private PdfTemplate t;
		private Image total;

		public void onOpenDocument(PdfWriter writer, Document document) {
			t = writer.getDirectContent().createTemplate(30, 16);
			try {
				total = Image.getInstance(t);
				total.setRole(PdfName.ARTIFACT);
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			}
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
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
				cell = new PdfPCell(new Phrase("ASTM F316", headertestname));
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
				headerdate.setColor(new BaseColor(98, 98, 98));

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
				footer.getDefaultCell().setFixedHeight(15);
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
						Font.FontFamily.HELVETICA, 7, Font.NORMAL,
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

		public void onCloseDocument(PdfWriter writer, Document document) {
			int totalLength = String.valueOf(writer.getPageNumber()).length();
			int totalWidth = totalLength * 5;
			// ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
			// new Phrase(String.valueOf(writer.getPageNumber()), new
			// Font(Font.FontFamily.HELVETICA, 8)),
			// totalWidth, 6, 0);
		}

	}
}
