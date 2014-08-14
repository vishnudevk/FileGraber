import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * @author Vishnudev.K
 *This is the bean containing the detials about the match
 */
public class MatchDetail {

	private String matchTitle;
	private String team1;
	private String team2;
	private Date matchTime;
	private String clubCrest1;
	private String clubCrest2;
	
	private transient BufferedImage clubCrestImg1;
	private transient BufferedImage clubCrestImg2;
	
	private Date lastCheckTime;
	
	@Override
	public String toString() {
		GsonBuilder builder = new GsonBuilder();
		builder.disableHtmlEscaping();
		Gson gson = builder.create();
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(this);
		System.out.println("MatchDetails "+json);
		return json;
	}
	public String getMatchTitle() {
		return matchTitle;
	}
	public void setMatchTitle(String matchTitle) {
		this.matchTitle = matchTitle;
	}
	public String getTeam1() {
		return team1;
	}
	public void setTeam1(String team1) {
		this.team1 = team1;
	}
	public String getTeam2() {
		return team2;
	}
	public void setTeam2(String team2) {
		this.team2 = team2;
	}
	public Date getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(Date matchTime) {
		this.matchTime = matchTime;
	}
	public String getClubCrest1() {
		return clubCrest1;
	}
	public void setClubCrest1(String clubCrest1) {
		this.clubCrest1 = clubCrest1;
	}
	public String getClubCrest2() {
		return clubCrest2;
	}
	public void setClubCrest2(String clubCrest2) {
		this.clubCrest2 = clubCrest2;
	}
	public Date getLastCheckTime() {
		return lastCheckTime;
	}
	public void setLastCheckTime(Date lastCheckTime) {
		this.lastCheckTime = lastCheckTime;
	}
	public BufferedImage getClubCrestImg1() {
		return clubCrestImg1;
	}
	public void setClubCrestImg1(BufferedImage clubCrestImg1) {
		this.clubCrestImg1 = clubCrestImg1;
	}
	public BufferedImage getClubCrestImg2() {
		return clubCrestImg2;
	}
	public void setClubCrestImg2(BufferedImage clubCrestImg2) {
		this.clubCrestImg2 = clubCrestImg2;
	}
	
	
	
}
