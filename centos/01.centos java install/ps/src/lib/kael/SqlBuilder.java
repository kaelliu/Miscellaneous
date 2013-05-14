package lib.kael;

import static org.apache.ibatis.jdbc.SelectBuilder.*;
public class SqlBuilder {
	public String selectBlogsSql() {
		BEGIN(); // Clears ThreadLocal variable
		SELECT("*");
		FROM("BLOG");
		return SQL();
		}
	
}
