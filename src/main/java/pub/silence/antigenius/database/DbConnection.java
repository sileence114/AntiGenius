package pub.silence.antigenius.database;

//public abstract class DbConnection {
//    DbConnection(String DriverClassName){
//        HikariConfig hikariConfig = new HikariConfig();
//
//        if("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(DriverClassName)){
//            hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/mydata");//mysql
//        }
//        else{
//            hikariConfig.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        }
//
//
//
//        hikariConfig.setUsername("whg");
//        hikariConfig.setPassword("whg");
//        hikariConfig.addDataSourceProperty("cachePrepStmts","true");
//        hikariConfig.addDataSourceProperty("prepStmtCacheSize","250");
//        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit","2048");
//
//        HikariDataSource ds = new HikariDataSource(hikariConfig);
//        Connection conn = null;
//        Statement statement = null;
//        ResultSet rs = null;
//
//        try {
//            //创建connection
//            conn = ds.getConnection();
//            statement = conn.createStatement();
//            //执行sql
//            rs = statement.executeQuery("select 100 s  from dual");
//            //取数据
//            if (rs.next()) {
//                System.out.println(rs.getString("s"));
//            }
//            //关闭connection
//            conn.close();
//        }
//        catch(SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
