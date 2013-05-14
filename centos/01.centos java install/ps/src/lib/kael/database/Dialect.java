package lib.kael.database;
public interface Dialect {  
	public static enum Type{
		MYSQL,
		ORACLE
	}
    public boolean supportsLimit();  
    public String getLimitString(String sql, boolean hasOffset);  
    public String getLimitString(String sql, int offset, int limit);  
    public boolean supportsLimitOffset();  
}  