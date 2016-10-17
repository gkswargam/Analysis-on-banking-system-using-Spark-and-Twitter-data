import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;

/* Servlet implementation class BankAnalysisQueries*/

@WebServlet("/BankAnalysisQueries")
public class BankAnalysisQueries extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	URL url = getClass().getResource("tweets.txt");
	
	public BankAnalysisQueries() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	int queryselection = Integer.parseInt(request.getParameter("query_selection"));
	System.out.println(queryselection);

		switch (queryselection) {
		
		case 1:query1();
			response.sendRedirect("query1.html");
			break;
		case 2:query2();
			response.sendRedirect("query2.html");
			break;
		case 3:query3();
			response.sendRedirect("query3.html");
			break;
		case 4:query4();
			response.sendRedirect("query4.html");
			break;
		case 5:query5();
			response.sendRedirect("query5.html");
			break;
		case 6:query6();
			response.sendRedirect("query6.html");
			break;
		case 7:query7();
			response.sendRedirect("query7.html");
			break;
		case 8:query8();
			response.sendRedirect("query8.html");
			break;
		default:
			JOptionPane.showMessageDialog(null, "Invalid Option please Enter from 1 to 8");
			break;
		}
		
	}
	
	/*-------------Query 1:Popular Banks based on followers--------------------*/

	public void query1() {

		String pathToFile = url.toString();

		SparkConf conf = new SparkConf().setAppName("Bank Analyisis").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaSQLContext sqlContext = new JavaSQLContext(sc);

		JavaSchemaRDD tweets = sqlContext.jsonFile(pathToFile);

		tweets.registerTempTable("tweetTable");

		tweets.printSchema();

		nbTweetByQuery1(sqlContext);

		sc.stop();

	}

	private void nbTweetByQuery1(JavaSQLContext sqlContext) {
		try {

			File outputFile = new File(getServletContext().getRealPath("/") + "/query1.csv");

			FileWriter fw = new FileWriter(outputFile);

			JavaSchemaRDD count = sqlContext.sql("SELECT user.name, user.followers_count AS c FROM tweetTable "
					+ "WHERE user.name LIKE '%Bank'" + "ORDER BY c");

			List<org.apache.spark.sql.api.java.Row> rows = count.collect();

			Collections.reverse(rows);

			String rows123 = rows.toString();

			String[] array = rows123.split("],");

			System.out.println(rows123);

			fw.append("Name");
			fw.append(',');
			fw.append("Count");
			fw.append("\n");

			for (int i = 0; i < 16; i++) {
				if (i == 0) {
					fw.append(array[0].substring(2));
					fw.append(',');
					fw.append("\n");
				} else {
					fw.append(array[i].substring(2));
					fw.append(',');
					fw.append("\n");
				}
			}

			fw.close();

		} catch (Exception exp) {
		}

	}

	/*-------------Query 2:Most Interested Type of Loan------------------------*/
	
	
	public void query2() {

		String pathToFile = url.toString();

		SparkConf conf = new SparkConf().setAppName("Bank Analyisis").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaSQLContext sqlContext = new JavaSQLContext(sc);

		JavaSchemaRDD tweets = sqlContext.jsonFile(pathToFile);

		tweets.registerTempTable("tweetTable");

		tweets.printSchema();

		nbTweetByQuery2(sqlContext);

		sc.stop();

	}

	private void nbTweetByQuery2(JavaSQLContext sqlContext) {

		try {
			File outputFile = new File(getServletContext().getRealPath("/") + "/query2.csv");

			FileWriter fw = new FileWriter(outputFile);

			JavaSchemaRDD count = sqlContext.sql("SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%car loan%'");
			
			JavaSchemaRDD count1 = sqlContext
					.sql("SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%home loan%'");
			JavaSchemaRDD count2 = sqlContext
					.sql("SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%education loan%'");
			JavaSchemaRDD count3 = sqlContext
					.sql("SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%personal loan%'");
			JavaSchemaRDD count4 = sqlContext
					.sql("SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%mortgages%'");

			List<Row> car = count.collect();
			String car12 = car.toString();
			String car1 = car12.substring(car12.indexOf("[") + 2, car12.indexOf("]"));

			List<Row> home = count1.collect();
			String home12 = home.toString();
			String home1 = home12.substring(home12.indexOf("[") + 2, home12.indexOf("]"));

			List<Row> education = count2.collect();
			String education12 = education.toString();
			String education1 = education12.substring(education12.indexOf("[") + 2, education12.indexOf("]"));

			List<Row> personal = count3.collect();
			String personal12 = personal.toString();
			String personal1 = personal12.substring(personal12.indexOf("[") + 2, personal12.indexOf("]"));

			List<Row> mortgages = count4.collect();
			String mortgages12 = mortgages.toString();
			String mortgages1 = mortgages12.substring(mortgages12.indexOf("[") + 2, mortgages12.indexOf("]"));

			fw.append("LoanType");
			fw.append(',');
			fw.append("Count");
			fw.append("\n");
			fw.append("car");
			fw.append(',');
			fw.append(car1);
			fw.append("\n");
			fw.append("home");
			fw.append(',');
			fw.append(home1);
			fw.append("\n");
			fw.append("education");
			fw.append(',');
			fw.append(education1);
			fw.append("\n");
			fw.append("personal");
			fw.append(',');
			fw.append(personal1);
			fw.append("\n");
			fw.append("mortgages");
			fw.append(',');
			fw.append(mortgages1);
			fw.append("\n");

			fw.close();

		} catch (Exception exp) {
		}

	}
	
	
	/*-------------Query 3:People's Opinion on Bank Security-------------------*/
	
	public void query3() {

		String pathToFile = url.toString();
		SparkConf conf = new SparkConf().setAppName("Bank Analyisis").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaSQLContext sqlContext = new JavaSQLContext(sc);

		JavaSchemaRDD tweets = sqlContext.jsonFile(pathToFile);

		tweets.registerTempTable("tweetTable");

		tweets.printSchema();

		nbTweetQuery3(sqlContext);

		sc.stop();

	}

	private void nbTweetQuery3(JavaSQLContext sqlContext) {

		try {
			File outputFile = new File(getServletContext().getRealPath("/") + "/query3.csv");

			FileWriter fw = new FileWriter(outputFile);

			JavaSchemaRDD count = sqlContext.sql("SELECT  COUNT(*) AS c FROM tweetTable "
					+ "WHERE text LIKE '%safe%' OR text LIKE '%secure%' OR text LIKE '%good%' OR text LIKE '%best%' OR text LIKE '%protected%' OR text LIKE '%okay%' OR text LIKE '%snug%' OR text LIKE '%buttonedup%' OR text LIKE '%appreciate%' OR text LIKE '%cherished%' OR text LIKE '%free from danger%' OR text LIKE '%guarded%' OR text LIKE '%impervious%' OR text LIKE '%impregnable%' OR text LIKE '%invioable%' OR text LIKE '%invulnarable%' OR text LIKE '%preserved%' OR text LIKE '%sheltered%' OR text LIKE '%shielded%' OR text LIKE '%unharmed%' OR text LIKE '%unhurt%' OR text LIKE '%immune%' OR text LIKE '%riskless%' OR text LIKE '%undamaged%' OR text LIKE '%unassailable%' OR text LIKE '%bestowal%' OR text LIKE '%finest%' OR text LIKE '%outstanding%' OR text LIKE '%super%' OR text LIKE '%prime%' OR text LIKE '%matchless%' OR text LIKE '%unrivaled%' OR text LIKE '%perfect%' OR text LIKE '%intact%' OR text LIKE '%fair%' OR text LIKE '%vindicated%' OR text LIKE '%watched%' OR text LIKE '%hale%' OR text LIKE '%well%' OR text LIKE '%healthy%' OR text LIKE '%watchful%' OR text LIKE '%vigilant%' OR text LIKE '%cagey%' OR text LIKE '%attentive%' OR text LIKE '%prudent%' OR text LIKE '%concerned%' OR text LIKE '%apprehensive%' OR text LIKE '%rigorous%' OR text LIKE '%honest%' OR text LIKE '%benefit%' OR text LIKE '%aid%' OR text LIKE '%support%' OR text LIKE '%helping hand%' OR text LIKE '%cooperation%' OR text LIKE '%like%' OR text LIKE '%interested%' OR text LIKE '%guidance%' OR text LIKE '%succor%' OR text LIKE '%assist%' OR text LIKE '%sophisticated%' OR text LIKE '%exceptional%' OR text LIKE '%prominent%' OR text LIKE '%superior%' OR text LIKE '%preferred%' OR text LIKE '%effective%' OR text LIKE '%efficient%' OR text LIKE '%restored%'  ");
			JavaSchemaRDD count1 = sqlContext.sql("SELECT  COUNT(*) AS c FROM tweetTable "
					+ "WHERE text LIKE '%insecure%' OR text LIKE '%worst%' OR text LIKE '%unhappy%' OR text LIKE '%bad%' OR text LIKE '%unprotected%' OR text LIKE '%theft%' OR text LIKE '%shaky%' OR text LIKE '%unassured%' OR text LIKE '%vague%' OR text LIKE '%touchy%' OR text LIKE '%careless%' OR text LIKE '%extroverted%' OR text LIKE '%uncareful%' OR text LIKE '%unsuspicious%' OR text LIKE '%rash%' OR text LIKE '%bold%' OR text LIKE '%incautious%' OR text LIKE '%harmful%' OR text LIKE '%poisionous%' OR text LIKE '%complain%' OR text LIKE '%dangerous%' OR text LIKE '%unreliable%' OR text LIKE '%precarious%' OR text LIKE '%unconfident%' OR text LIKE '%poor%' OR text LIKE '%corruption%' OR text LIKE '%corrupt%' OR text LIKE '%worthless%' OR text LIKE '%cruel%' OR text LIKE '%sad%' OR text LIKE '%danger%' OR text LIKE '%misbehaving%' OR text LIKE '%decline%' OR text LIKE '%deceive%' OR text LIKE '%inferior%' OR text LIKE '%insignificant%' OR text LIKE '%unskilled%' OR text LIKE '%fake%' OR text LIKE '%evil%' OR text LIKE '%mean%' OR text LIKE '%vile%' OR text LIKE '%wicked%' OR text LIKE '%immoral%' OR text LIKE '%froged%' OR text LIKE '%disappointed%' OR text LIKE '%unacceptable%' OR text LIKE '%second-rate%' OR text LIKE '%gross%' OR text LIKE '%rough%' OR text LIKE '%lousy%' OR text LIKE '%cheap%' OR text LIKE '%stink%' OR text LIKE '%fuck%' OR text LIKE '%horrible%' OR text LIKE '%junky%' OR text LIKE '%bummer%' OR text LIKE '%inefficient%' OR text LIKE '%awful%' OR text LIKE '%faulty%' OR text LIKE '%irritate%' OR text LIKE '%atrocious%' OR text LIKE '%lag%' OR text LIKE '%lie%' OR text LIKE '%malignant%' OR text LIKE '%malign%' OR text LIKE '%rough%' OR text LIKE '%ugly%' OR text LIKE '%robbery%' OR text LIKE '%piracy%' OR text LIKE '%fraud%' OR text LIKE '%cheat%' OR text LIKE '%mugging%' OR text LIKE '%extortion%'");

			List<Row> positive = count.collect();
			String positive12 = positive.toString();
			String positive1 = positive12.substring(positive12.indexOf("[") + 2, positive12.indexOf("]"));

			List<Row> negative = count1.collect();
			String negative12 = negative.toString();
			String negative1 = negative12.substring(negative12.indexOf("[") + 2, negative12.indexOf("]"));

			fw.append("Words");
			fw.append(',');
			fw.append("Count");
			fw.append("\n");
			fw.append("Secure");
			fw.append(',');
			fw.append(positive1);
			fw.append("\n");
			fw.append("Insecure");
			fw.append(',');
			fw.append("-" + negative1);
			fw.append("\n");
			fw.close();

		} catch (Exception exp) {
		}

	}
	
	/*-------------Query 4:Languages mostly used to tweet on Banks-------------*/
	
	public void query4() {

		String pathToFile = url.toString();
		SparkConf conf = new SparkConf().setAppName("Bank Analyisis").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaSQLContext sqlContext = new JavaSQLContext(sc);

		JavaSchemaRDD tweets = sqlContext.jsonFile(pathToFile);
		System.out.println("hiiiii");
		tweets.registerTempTable("tweetTable");

		tweets.printSchema();

		nbTweetByQuery4(sqlContext);

		sc.stop();

	}

	private void nbTweetByQuery4(JavaSQLContext sqlContext) {

		try {
			File outputFile = new File(getServletContext().getRealPath("/") + "/query4.csv");

			FileWriter fw = new FileWriter(outputFile);

			JavaSchemaRDD count = sqlContext
					.sql("SELECT lang, COUNT(*) AS c FROM tweetTable " + "Group By lang " + "order by c");

			List<org.apache.spark.sql.api.java.Row> rows = count.collect();

			Collections.reverse(rows);

			String rows123 = rows.toString();

			String[] array = rows123.split("],");

			System.out.println(rows123);

			fw.append("Language");
			fw.append(',');
			fw.append("Count");
			fw.append("\n");

			for (int i = 0; i < 8; i++) {
				if (i == 0) {
					continue;
				} else if (i == array.length - 1) {
					fw.append(array[i].substring(2, array[i].length() - 2));
					fw.append(',');
					fw.append("\n");
				} else {
					fw.append(array[i].substring(2));
					fw.append(',');
					fw.append("\n");
				}
			}

			fw.close();

		} catch (Exception exp) {
		}

	}
	
	/*-------------Query 5:Most tweeted Time About Banks-----------------------*/
	
	public void query5() {

		String pathToFile = url.toString();
		SparkConf conf = new SparkConf().setAppName("Bank Analyisis").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true");

		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaSQLContext sqlContext = new JavaSQLContext(sc);

		JavaSchemaRDD tweets = sqlContext.jsonFile(pathToFile);

		tweets.registerTempTable("tweetTable");

		tweets.printSchema();

		nbTweetByBankTime(sqlContext);

		sc.stop();

	}

	private void nbTweetByBankTime(JavaSQLContext sqlContext) {
		try {
			File outputFile = new File(getServletContext().getRealPath("/") + "/query5.csv");

			FileWriter fw = new FileWriter(outputFile);

			JavaSchemaRDD count = sqlContext
					.sql("SELECT created_at, COUNT(*) AS c FROM tweetTable " + "Group By created_at " + "order by c");

			List<org.apache.spark.sql.api.java.Row> rows = count.collect();

			Collections.reverse(rows);

			String rows123 = rows.toString();

			String[] array = rows123.split("],");

			System.out.println(rows123);

			fw.append("Time");
			fw.append(',');
			fw.append("Count");
			fw.append("\n");

			for (int i = 0; i < 8; i++) {
				if (i == 0) {
					continue;
				} else if (i == array.length - 1) {
					fw.append(array[i].substring(2, array[i].length() - 2));
					fw.append(',');
					fw.append("\n");
				} else {
					fw.append(array[i].substring(2));
					fw.append(',');
					fw.append("\n");
				}
			}

			fw.close();

		} catch (Exception exp) {
		}

	}

	/*-------------Query 6:Type of Investment People Interested in-------------*/
	
	public void query6() {

		String pathToFile = url.toString();

		SparkConf conf = new SparkConf().setAppName("Bank Analyisis").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaSQLContext sqlContext = new JavaSQLContext(sc);

		JavaSchemaRDD tweets = sqlContext.jsonFile(pathToFile);

		tweets.registerTempTable("tweetTable");

		tweets.printSchema();

		nbTweetByInvestment(sqlContext);

		sc.stop();

	}

	private void nbTweetByInvestment(JavaSQLContext sqlContext)

	{
		try {

			File outputFile = new File(getServletContext().getRealPath("/") + "/query6.csv");

			FileWriter fw = new FileWriter(outputFile);

			JavaSchemaRDD count = sqlContext
					.sql("SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%stock%'");
			JavaSchemaRDD count1 = sqlContext.sql(
					"SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%real estate%' OR text LIKE '%Land%'");
			JavaSchemaRDD count2 = sqlContext
					.sql("SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%business%'");
			JavaSchemaRDD count3 = sqlContext
					.sql("SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%bonds%'");
			JavaSchemaRDD count4 = sqlContext.sql("SELECT  COUNT(*) AS c FROM tweetTable "
					+ "WHERE text LIKE '%precious objects%' OR text LIKE '%gold%' OR text LIKE '%diamonds%' OR text LIKE '%silver%' OR text LIKE '%platinum%'");
			JavaSchemaRDD count5 = sqlContext.sql("SELECT  COUNT(*) AS c FROM tweetTable "
					+ "WHERE text LIKE '%commodities%' OR text LIKE '%natural gas%' OR text LIKE '%gasoline%' OR text LIKE '%crude oil%'");
			JavaSchemaRDD count6 = sqlContext
					.sql("SELECT  COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%funds%'");

			List<Row> stocks = count.collect();
			String stocks12 = stocks.toString();
			String stocks1 = stocks12.substring(stocks12.indexOf("[") + 2, stocks12.indexOf("]"));

			List<Row> realestate = count1.collect();
			String realestate12 = realestate.toString();
			String realestate1 = realestate12.substring(realestate12.indexOf("[") + 2, realestate12.indexOf("]"));

			List<Row> business = count2.collect();
			String business12 = business.toString();
			String business1 = business12.substring(business12.indexOf("[") + 2, business12.indexOf("]"));

			List<Row> bonds = count3.collect();
			String bonds12 = bonds.toString();
			String bonds1 = bonds12.substring(bonds12.indexOf("[") + 2, bonds12.indexOf("]"));

			List<Row> gold = count4.collect();
			String gold12 = gold.toString();
			String gold1 = gold12.substring(gold12.indexOf("[") + 2, gold12.indexOf("]"));

			List<Row> commodities = count5.collect();
			String commodities12 = commodities.toString();
			String commodities1 = commodities12.substring(commodities12.indexOf("[") + 2, commodities12.indexOf("]"));

			List<Row> funds = count6.collect();
			String funds12 = funds.toString();
			String funds1 = funds12.substring(funds12.indexOf("[") + 2, funds12.indexOf("]"));

			fw.append("InvestmentType");
			fw.append(',');
			fw.append("Count");
			fw.append("\n");
			fw.append("stocks");
			fw.append(',');
			fw.append(stocks1);
			fw.append("\n");
			fw.append("realestate");
			fw.append(',');
			fw.append(realestate1);
			fw.append("\n");
			fw.append("business");
			fw.append(',');
			fw.append(business1);
			fw.append("\n");
			fw.append("bonds");
			fw.append(',');
			fw.append(bonds1);
			fw.append("\n");
			fw.append("preciousObjects");
			fw.append(',');
			fw.append(gold1);
			fw.append("\n");
			fw.append("commodities");
			fw.append(',');
			fw.append(commodities1);
			fw.append("\n");
			fw.append("funds");
			fw.append(',');
			fw.append(funds1);
			fw.append("\n");

			fw.close();

		} catch (Exception exp) {
		}

	}
	
	/*-------------Query 7:Service wise top 7 Banks----------------------------*/
	
	public void query7() {
		String pathToFile = url.toString();

		SparkConf conf = new SparkConf().setAppName("Bank Analyisis").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaSQLContext sqlContext = new JavaSQLContext(sc);

		JavaSchemaRDD tweets = sqlContext.jsonFile(pathToFile);

		tweets.registerTempTable("tweetTable");

		tweets.printSchema();

		nbTweetByTweets(sqlContext);

		sc.stop();

	}

	private void nbTweetByTweets(JavaSQLContext sqlContext) {

		try {
			File outputFile = new File(getServletContext().getRealPath("/") + "/query7.csv");

			FileWriter fw = new FileWriter(outputFile);

			JavaSchemaRDD count = sqlContext.sql("SELECT user.name, user.statuses_count AS c FROM tweetTable "
					+ "WHERE user.name LIKE '%Bank' " + "ORDER BY c");

			List<org.apache.spark.sql.api.java.Row> rows = count.collect();

			Collections.reverse(rows);

			String rows123 = rows.toString();

			String[] array = rows123.split("],");

			System.out.println(rows123);

			fw.append("Name");
			fw.append(',');
			fw.append("Count");
			fw.append("\n");

			for (int i = 0; i < 25; i++) {
				if (i == 0) {
					fw.append(array[0].substring(2));
					fw.append(',');
					fw.append("\n");
				} else {
					fw.append(array[i].substring(2));
					fw.append(',');
					fw.append("\n");
				}
			}

			fw.close();

		} catch (Exception exp) {
		}

	}
	
	/*-------------Query 8:Commercial  Vs Investment Banks Tweets----------------------*/

	public void query8() {

		String pathToFile = url.toString();

		SparkConf conf = new SparkConf().setAppName("Bank Analyisis").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaSQLContext sqlContext = new JavaSQLContext(sc);

		JavaSchemaRDD tweets = sqlContext.jsonFile(pathToFile);

		tweets.registerTempTable("tweetTable");

		tweets.printSchema();

		nbTweetByStatus(sqlContext);

		sc.stop();

	}

	private void nbTweetByStatus(JavaSQLContext sqlContext) {
	
	 try {
	 File outputFile = new File(getServletContext().getRealPath("/") + "/query8.csv");
	
	 FileWriter fw = new FileWriter(outputFile);
	
	 JavaSchemaRDD totalcount = sqlContext.sql("SELECT COUNT(*) AS c FROM tweetTable " + "WHERE text LIKE '%Bank%'");
	
	 JavaSchemaRDD count = sqlContext.sql("SELECT COUNT(*) AS c1 FROM tweetTable " + "WHERE text LIKE '%JPMorgan%' OR text LIKE '%Wells Fargo%' OR text LIKE '%Citibank%' OR text LIKE '%PNC Bank%' OR text LIKE '%Bank of New York Mellon Corp%' OR text LIKE '%Capital One%' OR text LIKE '%TD Bank%' OR text LIKE '%HSBC Bank%' OR text LIKE '%Chase Bank%' OR text LIKE '%Goldman Sachs Bank%' OR text LIKE '%Manufacturers and Traders Trust%' OR text LIKE '%Santander Bank%' OR text LIKE '%Deutsche Bank%' OR text LIKE '%First Republic Bank%' OR text LIKE '%City National Bank%' OR text LIKE '%Morgan Stanley Private Bank%' OR text LIKE '%Signature Bank%' OR text LIKE '%BNY Mellon%' OR text LIKE '%BankUnited%' OR text LIKE '%Valley National Bank%' OR text LIKE '%Israel Discount Bank%' OR text LIKE '%Sterling National Bank%' OR text LIKE '%Safra National Bank%' OR text LIKE '%Mizuho Bank%' OR text LIKE '%Ally Bank%' OR text LIKE '%CIT BANK%' OR text LIKE '%iGObanking.com%' OR text LIKE '%AloStar Bank of Commerce%' OR text LIKE '%MySavingsDirect%' OR text LIKE '%American Express Bank%' OR text LIKE '%Discover Bank%' OR text LIKE '%Nationwide Bank%' OR text LIKE '%Arvest Bank%' OR text LIKE '%EverBank%' OR text LIKE '%Bank of America%' OR text LIKE '%First Midwest Bank%' OR text LIKE '%Sallie Mae Bank%' OR text LIKE '%Simple Bank%' OR text LIKE '%Bank of Internet%' OR text LIKE '%Bank5 Connect%' OR text LIKE '%First Niagara Bank%' OR text LIKE '%Barclays Bank%' OR text LIKE '%FNBO Direct%' OR text LIKE '%Great Western Bank%' OR text LIKE '%U.S. Bank%' OR text LIKE '%Wells Fargo Bank%' OR text LIKE '%AB Bank%' OR text LIKE '%ABN Amro Bank%' OR text LIKE '%Abu Dhabi Commercial Bank%' OR text LIKE '%Allahabad Bank%' OR text LIKE '%American Express Banking Corporation%' OR text LIKE '%Andhra Bank%' OR text LIKE '%Antwerp Diamond Bank%' OR text LIKE '%Axis Bank%' OR text LIKE '%Bank of Bahrain & Kuwait%' OR text LIKE '%Bank of Ceylon%' OR text LIKE '%Bank of India%' OR text LIKE '%Bank of Nova Scotia%' OR text LIKE '%Bank of Punjab%' OR text LIKE '%Bank of Rajasthan%' OR text LIKE '%Bank of Tokyo Mitsubishi UFJ%' OR text LIKE '%Barclays%' OR text LIKE '%BNP Paribas%' OR text LIKE '%Calyon Bank%' OR text LIKE '%Chinatrust%' OR text LIKE '%City Union Bank%' OR text LIKE '%Corporation Bank%' OR text LIKE '%Cosmos Bank%' OR text LIKE '%DBS Bank%' OR text LIKE '%Dena Bank%' OR text LIKE '%Development Credit Bank Limited%' OR text LIKE '%Export-Import%' OR text LIKE '%Federal Bank%' OR text LIKE '%Global Trust Bank%' OR text LIKE '%ICICI Bank%' OR text LIKE '%IDBI Bank%' OR text LIKE '%Indian Overseas Bank%' OR text LIKE '%IndusInd Bank Limited%' OR text LIKE '%JSC VTB Bank%' OR text LIKE '%Krung Thai Bank%' OR text LIKE '%Mandvi Co-operative Bank Ltd.%' OR text LIKE '%Mashreq Bank%' OR text LIKE '%National Bank for Agriculture and Rural Development%' OR text LIKE '%Nedungadi Bank Ltd%' OR text LIKE '%Oman International Bank%' OR text LIKE '%Oriental Bank of Commerce%' OR text LIKE '%Punjab & Sind Bank%' OR text LIKE '%Reserve Bank of India%' OR text LIKE '%Shinhan Bank%' OR text LIKE '%Societe Generale%' OR text LIKE '%Sonali Bank%' OR text LIKE '%Standard Chartered Bank%' OR text LIKE '%State Bank of Mauritius%' OR text LIKE '%State Bank of Travancore%' OR text LIKE '%Syndicate Bank%' OR text LIKE '%TimesBank%' OR text LIKE '%UBS AG%' OR text LIKE '%UCO Bank%' OR text LIKE '%Union Bank of India%' OR text LIKE '%Vysya Bank%' OR text LIKE '%Lloyds Bank%'");

	 JavaSchemaRDD count1 = sqlContext.sql("SELECT COUNT(*) AS c2 FROM tweetTable " + "WHERE text LIKE '%JPMorgan%' OR text LIKE '%Goldman Sachs%' OR text LIKE '%Bank of America%' OR text LIKE '%Morgan Stanley%' OR text LIKE '%Citigroup%' OR text LIKE '%Deutsche Bank%' OR text LIKE '%Credit Suisse%' OR text LIKE '%Barclays%' OR text LIKE '%UBS%' OR text LIKE '%Wells Fargo%' OR text LIKE '%RBC Capital Markets%' OR text LIKE '%Jefferies Group%' OR text LIKE '%BNP Paribas%' OR text LIKE '%Nomura Holdings%' OR text LIKE '%Mizuho%' OR text LIKE '%Lazard%' OR text LIKE '%Sumitomo Mitsui%' OR text LIKE '%Mitsubishi UFJ%' OR text LIKE '%Societe Generale%' OR text LIKE '%HSBC%' OR text LIKE '%Allen & Company%' OR text LIKE '%BBY Ltd%' OR text LIKE '%Blackstone Group%' OR text LIKE '%Brown Brothers Harriman%' OR text LIKE '%Brown, Shipley & Co.%' OR text LIKE '%BTG Pactual%' OR text LIKE '%Cain Brothers%' OR text LIKE '%Cantor Fitzgerald%' OR text LIKE '%Canaccord Financial Inc%' OR text LIKE '%Capstone Partners%' OR text LIKE '%Centerview Partners%' OR text LIKE '%China International Capital Corporation%' OR text LIKE '%CITIC Securities%' OR text LIKE '%Close Brothers Group%' OR text LIKE '%CLSA%' OR text LIKE '%Cowen Group%' OR text LIKE '%C.W. Downer & Co.%' OR text LIKE '%Daewoo Securities%' OR text LIKE '%Defoe Fournier & Cie.%' OR text LIKE '%Duff & Phelps%' OR text LIKE '%Europa Partners%' OR text LIKE '%Evercore Partners%' OR text LIKE '%FBR Capital Markets%' OR text LIKE '%Financo%' OR text LIKE '%Foros Group%' OR text LIKE '%Gleacher & Co.%' OR text LIKE '%Greenhill & Co.%' OR text LIKE '%Guggenheim Partners%' OR text LIKE '%Guosen Securities%' OR text LIKE '%Houlihan Lokey%' OR text LIKE '%Imperial Capital%' OR text LIKE '%Investec%' OR text LIKE '%Investment Technology Group%' OR text LIKE '%Janney Montgomery Scott%' OR text LIKE '%Keefe, Bruyette & Woods%' OR text LIKE '%Ladenburg Thalmann%' OR text LIKE '%Lincoln International%' OR text LIKE '%M.M.Warburg & CO%' OR text LIKE '%Marathon Capital%' OR text LIKE '%Mediobanca%' OR text LIKE '%Miller Buckfire & Co.%' OR text LIKE '%Moelis & Company%' OR text LIKE '%Morgan Keegan & Company%' OR text LIKE '%N M Rothschild & Sons%' OR text LIKE '%Needham and Company%' OR text LIKE '%Newedge%' OR text LIKE '%Oppenheimer & Co.%' OR text LIKE '%Panmure Gordon%' OR text LIKE '%Park Lane%' OR text LIKE '%Perella Weinberg Partners%' OR text LIKE '%Peter J. Solomon Company%' OR text LIKE '%Piper Jaffray%' OR text LIKE '%Pottinger%' OR text LIKE '%Raymond James%' OR text LIKE '%Robert W. Baird & Company%' OR text LIKE '%Sagent Advisors%' OR text LIKE '%Sanford Bernstein%' OR text LIKE '%Stephens Inc.%' OR text LIKE '%Stone Key Partners%' OR text LIKE '%Thomas Weisel Partners%' OR text LIKE '%Vermilion Partners%' OR text LIKE '%Wedbush Securities%' OR text LIKE '%William Blair & Company%' OR text LIKE '%Kotak Mahindra Bank%' OR text LIKE '%YES Bank%'");

	 List<Row> totalrows = totalcount.collect();
	 String totalrows12 = totalrows.toString();
	 String totalrows1 = totalrows12.substring(totalrows12.indexOf("[") + 2,
	 totalrows12.indexOf("]"));
	
	 List<Row> commercial = count.collect();
	 String commercial12 = commercial.toString();
	 String commercial1 = commercial12.substring(commercial12.indexOf("[")+ 2,
	 commercial12.indexOf("]"));
	
	 List<Row> investment = count1.collect();
	 String investment12 = investment.toString();
	 String investment1 = investment12.substring(investment12.indexOf("[") + 2,
	 investment12.indexOf("]"));
	
	 System.out.println(totalrows1);
	 
	 System.out.println(commercial1);
	 System.out.println(investment1);
	
	 int totalrows123 = Integer.parseInt(totalrows1);
	 int commercial123 = Integer.parseInt(commercial1);
	 int investment123 = Integer.parseInt(investment1);
	 int foreigncount = totalrows123 - (commercial123+investment123);
	
	 double commercialPercentage = ((commercial123 * 100) / totalrows123);
	 double investmentPercentage = ((investment123 * 100) / totalrows123);
	 float foreignPercentage = ((foreigncount * 100) / totalrows123);
	
	 System.out.println(commercialPercentage);
	 System.out.println(investmentPercentage);
	 System.out.println(foreignPercentage);
	
	 String commercialPercentage1 = Double.toString(commercialPercentage);
	 String investmentPercentage1 = Double.toString(investmentPercentage);
	 String foreignPercentage1 = Float.toString(foreignPercentage);
	
	 fw.append("TweetStatus");
	 fw.append(',');
	 fw.append("Percentage");
	 fw.append("\n");
	 fw.append("CommercialBanks%");
	 fw.append(',');
	 fw.append(commercialPercentage1);
	 fw.append("\n");
	 fw.append("InvestmentBanks%");
	 fw.append(',');
	 fw.append(investmentPercentage1);
	 fw.append("\n");
	 fw.append("others%");
	 fw.append(',');
	 fw.append(foreignPercentage1);
	 fw.append("\n");
	
	 fw.close();
	
	 } catch (Exception exp) {
	 }
 }
}