package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class JdbcContext {
	private DataSource dataSource;

	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException{
		Connection c = null;
		PreparedStatement ps = null;
		
		try{
			c = this.dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			
			ps.executeUpdate();
		} catch(SQLException e){
			throw e;
		} finally {
			if (ps != null) {try {ps.close();} catch(SQLException e){} }
			if (c != null) {try {c.close();} catch(SQLException e){} }
		}
	}
	
	
	public void executeSql(final String...query) throws SQLException{
		this.workWithStatementStrategy(new StatementStrategy(){
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				String q = query[0];
				PreparedStatement ps = c.prepareStatement(q);
				
				for(int i=1;i<query.length;i++){
					ps.setString(i,  query[i]);
				}
				
				return ps;
			}
		});
	}

}
