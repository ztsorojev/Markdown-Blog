/***
 * File        : AccessDB.java
 * Description : Create the connection with the database
 */

import java.sql.*;

public class AccessDB {

    /*
    public static void main(String[] args) {

        Timestamp created = new Timestamp(System.currentTimeMillis());
        Timestamp modified = new Timestamp(System.currentTimeMillis());

        if (args[0].equals("connectionDB"))
            connectionDB();
        else if (args[0].equals("queryINSERT"))
            queryINSERT("John", "Title 1", "Body 1", created, modified);
        else if (args[0].equals("querySELECT")) {
            //queryINSERT("John", "Title 1", "Body 1", created, modified);
            querySELECT("SELECT * FROM Posts WHERE username=?", "test", 0);
        }
    }
    */

    /*
     * Create the database connection
     */
    public static Connection connectionDB () {
        /* load the driver */
        try {
            Class.forName("com.mysql.jdbc.Driver");
            /* create an instance of a Connection object */
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/CS144", "cs144", ""); 

            System.out.println("Connection succeeded!");

            return c;

        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }


    /*
     * Close the database connection.
     */
    public static void closeDB(ResultSet rs, PreparedStatement ps, Connection c) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (c != null) c.close();     
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
     * Executes the query INSERT on the table Posts, 
     * while making sure every user has a unique postid starting from 1
     * @param   username    String of the user's username
     *          title       new title of the post
     *          body        new body of the post
     *          created     time when the post has been created
     *          modified    time when the post has been modified     
     */
    public static void queryINSERT(String username, String title, String body, Timestamp created, Timestamp modified) {
        Connection c = null;
        PreparedStatement ps = null; 
        ResultSet rs = null; 
        try {

            //we initialize postid in the case it's the first post of user (it will be updated later if it's not the case)
            int postid = 1;

            c = connectionDB();

            //If the user already exists, we update postid with (last postid + 1) for this user
            ps = c.prepareStatement("SELECT postid FROM Users WHERE username=?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            while (rs.next()) {
                postid = rs.getInt("postid") + 1;
            }

            

            String query = "INSERT INTO Posts VALUES(?,?,?,?,?,?)";
            ps = c.prepareStatement(query) ;
            ps.setString(1, username);
            ps.setInt(2, postid);
            ps.setString(3, title);
            ps.setString(4, body);
            ps.setTimestamp(5, created);
            ps.setTimestamp(6, modified);

            ps.executeUpdate();

            //If it's a new user, we insert a new row in table Users
            if (postid == 1) {
                ps = c.prepareStatement("INSERT INTO Users VALUES(?,?)");
                ps.setString(1, username);
                ps.setInt(2, postid);
                ps.executeUpdate();

            } 
            //Otherwise, update the new postid of the user
            else {
                ps = c.prepareStatement("UPDATE Users SET postid=? WHERE username=?");
                ps.setInt(1, postid);
                ps.setString(2, username); 
                ps.executeUpdate();      
            }

        } catch (SQLException ex){
            System.out.println("Message   : " + ex.getMessage());
        
        } finally {
            closeDB(rs, ps, c);
        }
    }


    /*
     * Executes the query UPDATE on the table Posts
     * @param   username    String of the user's username
     *          postid      id of post we want to update
     *          title       new title of the post
     *          body        new body of the post
     *          modified    time when the post has been modified
     */
    public static void queryUPDATE(String username, int postid, String title, String body, Timestamp modified) {
        Connection c = null;
        PreparedStatement ps = null; 
        ResultSet rs = null; 
        try {

            if (postid > 0) {
                String query = "UPDATE Posts SET title=?, body=?, modified=?  WHERE username=? AND postid=?";
                ps = connectionDB().prepareStatement(query);
                ps.setString(1, title);
                ps.setString(2, body);
                ps.setTimestamp(3, modified);
                ps.setString(4, username);
                ps.setInt(5, postid);

                ps.executeUpdate();

            } else {
                queryINSERT(username, title, body, modified, modified);
            }
            

        } catch (SQLException ex){

            System.out.println("Message   : " + ex.getMessage());
        
        } finally {
            closeDB(rs, ps, c);
        }
    }


    /*
     * Executes the query SELECT
     * @param   query       String of prepared statement
     *          username    String of the username
     *          postid      integer of postid; put 0 to ignore this parameter
     *
     * @return  rs          ResultSet containing the response to the query
     *
     */
    public static ResultSet querySELECT(PreparedStatement ps, String username, int postid) {
        ResultSet rs = null; 

        String title;
        String body;
        Timestamp created;

        try {

            ps.setString(1, username);

            if (postid>0) {
                ps.setInt(2, postid);
            }

            rs = ps.executeQuery();
            
            return rs;

        } catch (SQLException ex){
            System.out.println("Message   : " + ex.getMessage());
            return null;
        }
    }

    /*
     * Executes the query DELETE
     * @param   username    String of the user's username
     *          postid      id of post we want to delete
     */
    public static void queryDELETE(String username, int postid) {
        Connection c = null;
        PreparedStatement ps = null; 
        ResultSet rs = null; 
        try {

            String query = "DELETE FROM Posts WHERE username=? AND postid=?";

            ps = connectionDB().prepareStatement(query);

            ps.setString(1, username);
            ps.setInt(2, postid);

            ps.executeUpdate();

        } catch (SQLException ex){
            System.out.println("Message   : " + ex.getMessage());
        
        } finally {
            closeDB(rs, ps, c);
        }
    }
    
}
