package gmerg.db;

import java.util.ArrayList;

public interface EditHistoryDAO {
	public ArrayList getDates();
	public ArrayList getUsersInDates(String sort);
}
