

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * @author Vishnudev.K
 *this method parse the next match date from the source code.
 * In future the sniplets should be added for getting the club crest
 */
public class SourceParser {

	private static String pageSrc;
	private static String siteUrl = "http://barcelonastream.com";
	private static String proxyHost = "labcore-vip.network.fedex.com";
	private static String proxyPort = "3128";
	
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM,dd,yyyy,HH:mm" );
	private static TimeZone timeZone = TimeZone.getTimeZone("UTC");//default time zone from Barcelonastream is UTC
	
	
	public static void main(String[] args) throws ParseException, IOException {
		System.out.println(new Date());
		pageSrc = getBSPageSrc();
		getNetMatchSchedule(pageSrc);
		getClubCrestUrls(pageSrc);
		System.out.println(new Date());
	}
	
	/**
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static MatchDetail getMatchDetails() throws IOException, ParseException{
		MatchDetail matchDetail = new MatchDetail();
		
		pageSrc = getBSPageSrc();
		String[] crestUrls =getClubCrestUrls(pageSrc);
		
		matchDetail.setMatchTitle("some match Vs Barcelona");
		matchDetail.setMatchTime(getNetMatchSchedule(pageSrc));
		matchDetail.setClubCrest1(crestUrls[0]);
		matchDetail.setClubCrest2(crestUrls[1]);

		return matchDetail;
	}
	
	/**Method used for getting page source of this Barcelona stream.
	 * set proxyHost and proxyPort if you proxy to access net
	 * @return
	 * @throws IOException
	 */
	private static String getBSPageSrc() throws IOException{
		StringBuilder pageSrc = new StringBuilder();
		if(proxyHost!=null && ! proxyHost.isEmpty())
			System.getProperties().put("http.proxyHost", proxyHost);
		if(proxyPort!=null && ! proxyPort.isEmpty())
			System.getProperties().put("http.proxyPort", proxyPort);
		
		if(System.getProperties().get("http.proxyHost")!=null && System.getProperties().get("http.proxyPort")!=null)
			System.out.println("Systme proxy settings : "+System.getProperties().get("http.proxyHost")+":"+System.getProperties().get("http.proxyPort"));
		
		URL url = new URL(siteUrl);
        URLConnection urlConnection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            //System.out.println(inputLine);
        	pageSrc.append(inputLine);
        in.close();
        
		return pageSrc.toString();
	}
	
	
	/**Method used for getting the next match schedule date
	 * @param pageSrc
	 * @return
	 * @throws ParseException
	 */
	private static Date getNetMatchSchedule(String pageSrc) throws ParseException{
		int eventTimePosition = pageSrc.indexOf("eventTime: \""); 
		int dateEndPostion = pageSrc.indexOf("\",",eventTimePosition);
		String dateStr = pageSrc.substring(eventTimePosition+ 12 , dateEndPostion);
		System.out.println("Date going to be parsed "+dateStr);
		dateFormat.setTimeZone(timeZone);
	    Date date = dateFormat.parse(dateStr);
		System.out.println("Parsed date "+date);
		
		return date;
	}
	
	
	private static String[] getClubCrestUrls(String pageSrc){
		int matchShieldsStartPosition = pageSrc.indexOf("<div class=\"match-shields\">"); 
		int matchShieldsEndPosition = pageSrc.indexOf("</div>",matchShieldsStartPosition);
		String matchSheildString = pageSrc.substring(matchShieldsStartPosition,matchShieldsEndPosition+6);
		System.out.println(matchSheildString);
		
		int crest1StartPosition = matchSheildString.indexOf("src=\""); 
		int crest1EndPosition = matchSheildString.indexOf("\"",crest1StartPosition+5);
		String crest1String = matchSheildString.substring(crest1StartPosition+5 ,crest1EndPosition);
		System.out.println(crest1String);
		
		int crest2StartPosition = matchSheildString.indexOf("src=\"",crest1StartPosition+5); 
		int crest2EndPosition = matchSheildString.indexOf("\"",crest2StartPosition+5);
		String crest2String = matchSheildString.substring(crest2StartPosition+5 ,crest2EndPosition);
		System.out.println(crest2String);
		
		String[] urls = new String[2];
		urls[0]=crest1String;
		urls[1]=crest2String;
		return urls;
	} 
	
}
