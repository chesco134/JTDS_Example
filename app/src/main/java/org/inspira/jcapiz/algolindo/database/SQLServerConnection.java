package org.inspira.jcapiz.algolindo.database;

public class SQLServerConnection {

    private String url;
    private String user;
    private String psswd;
    private java.sql.Connection con;
    private java.sql.ResultSet rs;
    private java.sql.PreparedStatement pstmnt;

    public SQLServerConnection(String url, String user, String psswd){
        this.url = url;
        this.user = user;
        this.psswd = psswd;
        openConnection();
    }

    public void openConnection(){
        try{
            this.con = java.sql.DriverManager.getConnection(url, user, psswd);
        } catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public java.util.ArrayList<String> getHeader(){
        java.util.ArrayList<String> headers = new java.util.ArrayList<>();
        try{
            for(int i=1; i<rs.getMetaData().getColumnCount(); i++)
                headers.add(rs.getMetaData().getColumnName(i));
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        return headers;
    }

    public java.util.ArrayList<Object> getObjects(){
        java.util.ArrayList<Object> lst = new java.util.ArrayList<>();
        try{
            if(rs.next()){
                for(int i=1; i<= rs.getMetaData().getColumnCount(); i++){
                    lst.add(rs.getObject(i));
                }
            }
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        return lst;
    }

    public void executeUpdate(){
        try{
            pstmnt.executeUpdate();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public String getQuote(){
        try{
            return con.getMetaData().getIdentifierQuoteString();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        return "\"";
    }

    public void executeQuery(){
        try{
            rs = pstmnt.executeQuery();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public void prepare(String sql){
        try{
            pstmnt = con.prepareStatement(sql);
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public void set(int position, Object o, int targetType){
        try{
            pstmnt.setObject(position, o, targetType);
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public void close(){
        if(con != null)
            try{
                con.close();
            }catch(java.sql.SQLException ignore){}
    }
}
