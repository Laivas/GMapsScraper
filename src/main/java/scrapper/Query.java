package scrapper;

import java.util.List;

public interface Query {

	public List<String[]> getQuerys();
//	
//	public List<String> getQuerysList();
//	
//	public String[] getQuerysArray();
	
	public String getQuery();
	
	public String getSecondQuery();
	
	public void setQuerys(List<String[]> querys);
	
	public void setQuery(String query);
	
	public void setSecondQuery(String secondQuery);
	
	public void setZoomLevel(String zoomLevel);
	
	public String getZoomLevel();
	
//	public void setQuerysArray(String[] query);
//		
//	public void setQuerysList(List<String> querys);
	
	
	
}
