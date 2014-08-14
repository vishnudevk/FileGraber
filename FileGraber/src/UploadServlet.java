// Import required java libraries
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadServlet extends HttpServlet {

	private static String proxyHost = "labcore-vip.network.fedex.com";
	private static String proxyPort = "3128";
	private static MatchDetail matchDetail;
	private static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	
	public void init() {
		if (proxyHost != null && !proxyHost.isEmpty())
			System.getProperties().put("http.proxyHost", proxyHost);
		if (proxyPort != null && !proxyPort.isEmpty())
			System.getProperties().put("http.proxyPort", proxyPort);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		boolean checkAgain=true;
		
		if(request.getParameter("img")==null){
			try {
				
				if(matchDetail!=null){
					long t=matchDetail.getLastCheckTime().getTime();
					Date afterAdding2hrs=new Date(t + (120 * ONE_MINUTE_IN_MILLIS));
					if(afterAdding2hrs.compareTo(new Date())>0){
						checkAgain = false;
					}else{
						if(matchDetail.getMatchTime().compareTo(new Date())>0){
							checkAgain = false;
						}
					}
				}
				if(checkAgain){
					matchDetail = SourceParser.getMatchDetails();
					matchDetail.setLastCheckTime(new Date());
					matchDetail.setClubCrestImg1(getOnMemoryImage(matchDetail.getClubCrest1(),request));
					matchDetail.setClubCrestImg2(getOnMemoryImage(matchDetail.getClubCrest2(),request));
					matchDetail.setClubCrest1(request.getRequestURL().append("?img=crest1").toString());
					matchDetail.setClubCrest2(request.getRequestURL().append("?img=crest2").toString());
				//	matchDetail.setClubCrest1(saveCrestLocally(matchDetail.getClubCrest1(),request));
				//	matchDetail.setClubCrest2(saveCrestLocally(matchDetail.getClubCrest2(),request));
				}
				
				PrintWriter out = response.getWriter();
				out.println(matchDetail.toString());
				
				out.close();
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			serveImg(request,response);
		}

	}

	private void serveImg(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String parm = request.getParameter("img");
		if(parm.equalsIgnoreCase("crest1")){
			BufferedImage image = matchDetail.getClubCrestImg1();
			response.setContentType("image/png");
			OutputStream outputStream = response.getOutputStream();
			ImageIO.write(image, "png", outputStream);
			outputStream.close();
		}else if(parm.equalsIgnoreCase("crest2")){
			BufferedImage image = matchDetail.getClubCrestImg2();
			response.setContentType("image/png");
			OutputStream outputStream = response.getOutputStream();
			ImageIO.write(image, "png", outputStream);
			outputStream.close();
		}
	}

	private String saveCrestLocally(String urlString, HttpServletRequest request)
			throws MalformedURLException, IOException {
		//request.getRequestURL()
		String filename = urlString.substring(urlString.lastIndexOf("/") + 1);
		String appPath = request.getServletContext().getRealPath("")+ File.separator + "pics"+ File.separator + filename;
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		File f = new File(appPath);
		if(!f.exists()){
			try {
				in = new BufferedInputStream(new URL(urlString).openStream());
				fout = new FileOutputStream(appPath);
				final byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1) {
					fout.write(data, 0, count);
				}
			} finally {
				if (in != null) {
					in.close();
				}
				if (fout != null) {
					fout.close();
				}
			}
		}
		String url = request.getRequestURL().toString();
		url = url.replace(UploadServlet.class.getName(), "");
		
		return url+"pics/" + filename;
	}
	
	private BufferedImage  getOnMemoryImage(String urlString, HttpServletRequest request) throws IOException{
		BufferedImage img = null;
	    URL url = new URL(urlString);
	    img = ImageIO.read(url);
		return img;
	}

}