package gmerg.db;

public interface TrackingIPDAO {
	public boolean updateIPLog(String ip, String viewid, String browser, String platform);
}
