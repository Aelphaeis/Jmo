package jmo.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class QueryResult {
	List<QueryColumn> columns;
	Map<Integer, List<Object>> values;
	
	public QueryResult(){
		columns = new ArrayList<>();
		values = new HashMap<>();
	}
	
	public QueryResult(ResultSet rs) throws SQLException{
		ResultSetMetaData meta = rs.getMetaData();
		int metaCount = meta.getColumnCount();
		List<QueryColumn> cols = this.getColumns();
		
		for(int i = 0; i < metaCount; i++){
			QueryColumn col = new QueryColumn();
			col.setColumnName(meta.getColumnName(i + 1));
			cols.add(col);
		}
		
		for(int c = 0; rs.next(); c++){
			List<Object> rowVals = new ArrayList<>();
			for(QueryColumn qc  : this.getColumns()){
				rowVals.add(rs.getObject(qc.getColumnName()));
			}
			this.values.put(c, rowVals);
		}
	}
	
	public List<Object> column(String columnName){
		QueryColumn qc = null;
		for(int i = 0; i < columns.size(); i++ ){
			qc = columns.get(i);
			if(qc.getColumnName().equalsIgnoreCase(columnName)){
				return column(i);
			}
		}
		return new ArrayList<>();
	}
	
	public List<Object> column(int columnIndex){
		List<Object> objs = new ArrayList<>();
		for(Entry<Integer, List<Object>> e : values.entrySet()) 
			objs.add(e.getValue().get(columnIndex));
		return objs;
	}
	
	public <T> T value(String columnName, int index){
		QueryColumn qc = null;
		for(int i = 0; i < columns.size(); i++ ){
			qc = columns.get(i);
			if(qc.getColumnName().equalsIgnoreCase(columnName)){
				return value(i, index);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T value(int columnIndex, int index){
		return (T) values.get(index).get(columnIndex);
	}

	public List<QueryColumn> getColumns() {
		return columns;
	}

	protected void setColumns(List<QueryColumn> columns) {
		this.columns = columns;
	}
}
