import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Servlet implementation class for Servlet: ConfigurationTest
 *
 */
public class Editor extends HttpServlet {
    /**
     * The Servlet constructor
     * 
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public Editor() {}

    public void init() throws ServletException
    {
        /*  write any servlet initialization code here or remove this function */
    }
    
    public void destroy()
    {
        /*  write any servlet cleanup code here or remove this function */
    }

    /**
     * Handles HTTP GET requests
     * 
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException 
    {

        String username = request.getParameter("username");
        String action = request.getParameter("action");

        checkParameters(username, action, request, response);

        int postid = 0; //default value

        Connection c = AccessDB.connectionDB();
        PreparedStatement ps = null; 
        ResultSet rs = null;

        if(action.equals("list")) {
            getAllPosts(request, response, c, username);
        }

        if(action.equals("open")) {
            postid = checkPostid(request, response);
            actionOpen(postid, username, c, ps, rs, request, response);
        }
        
    }
    
    /**
     * Handles HTTP POST requests
     * 
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException 
    {
        
        String username = request.getParameter("username");
        String action = request.getParameter("action");

        checkParameters(username, action, request, response);

        Connection c = null;
        PreparedStatement ps = null; 
        ResultSet rs = null;

        String query;
        int postid = 0; //default value

        try {
            c = AccessDB.connectionDB();

            /*
             * OPEN
             */
            if(action.equals("open")) {

                postid = checkPostid(request, response);
                actionOpen(postid, username, c, ps, rs, request, response);
                
            }

            /*
             * SAVE
             */
            if(action.equals("save")) {

                postid = checkPostid(request, response);

                String title = request.getParameter("title");
                String body = request.getParameter("body");

                if(title != null || title.isEmpty() || body != null || body.isEmpty()) {
                    Timestamp modified = new Timestamp(System.currentTimeMillis());
                    AccessDB.queryUPDATE(username, postid, title, body, modified);
                }

                getAllPosts(request, response, c, username);
         
            }

            /*
             * LIST (close btn)
             */
            if(action.equals("list")) {
                getAllPosts(request, response, c, username);
            }

            /*
             * DELETE
             */
            if(action.equals("delete")) {
                
                postid = checkPostid(request, response);
                AccessDB.queryDELETE(username, postid);
                getAllPosts(request, response, c, username);

            } 
        } finally {
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { c.close(); } catch (Exception e) { /* ignored */ }
        }

        /*
         * PREVIEW
         */
        if(action.equals("preview")) {
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();

            String markdown = request.getParameter("body");
            String html = renderer.render(parser.parse(markdown)); 

            request.setAttribute("body_markdown", html); 

            request.getRequestDispatcher("/preview.jsp").forward(request, response);
        }
        
         
    }

    /*
     * This function adds all of the user's posts titles, postids, created and modified times as attributes to the request
     * so that they can be printed on the list page
     *
     * @param request   the request from doGet() or doPost()
     *        response  the response from doGet() or doPost()
     *        c         the connection to the database
     *        username  the username of the current user
     */
    public void getAllPosts(HttpServletRequest request, HttpServletResponse response, Connection c, String username)
        throws ServletException, IOException 
    {
        
        int nPosts = 0; //count number of posts the user has
        int postid;
        String title;
        Timestamp created;
        Timestamp modified;

        ArrayList <Integer> postids = new ArrayList<>();
        ArrayList <String> titles = new ArrayList<>();
        ArrayList <Timestamp> createdTimes = new ArrayList<>();
        ArrayList <Timestamp> modifiedTimes = new ArrayList<>();

        PreparedStatement ps = null; 
        ResultSet rs = null;

        String query = "SELECT * FROM Posts WHERE username=?";

        try {
            ps = c.prepareStatement(query);
            rs = AccessDB.querySELECT(ps, username, 0);

            while( rs.next() ){
                postid = rs.getInt("postid");
                title = rs.getString("title");
                created = rs.getTimestamp("created");
                modified = rs.getTimestamp("modified");

                postids.add(postid);
                titles.add(title);
                createdTimes.add(created);
                modifiedTimes.add(modified);

                nPosts++;
            }

        } catch (SQLException ex){
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ) {
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        } finally {
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { c.close(); } catch (Exception e) { /* ignored */ }
        }
        
        request.setAttribute("postid", postids);
        request.setAttribute("title", titles);
        request.setAttribute("created", createdTimes);
        request.setAttribute("modified", modifiedTimes);
        request.setAttribute("nPosts", nPosts);

        request.getRequestDispatcher("/list.jsp").forward(request, response);
    }

    

    /* 
     * Perform the action "Open" post.
     * Checks if we are opening an existing post or creating a new one. Assigns the title and body accordingly.
     * @param postid    the postid of the post we want to open,
     *                  value of 0 in the case of a new post
     *        username  the username of the current user     
     *        action    the action in the url request, should not be empty
     *        c         the connection to the database
     *        ps        the prepared statement we will initialize
     *        rs        the resultSet we will initialize
     *        request   the request from doGet() or doPost()
     *        response  the response from doGet() or doPost()
     */
    public void actionOpen(int postid, String username, Connection c, PreparedStatement ps, ResultSet rs, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {

        // If we are opening an existing post: 
        // retrieve the title and body of the post
        if (postid > 0 && request.getParameter("title") == null) {
            
            try {
                String query = "SELECT * FROM Posts WHERE username=? AND postid=?";
                ps = c.prepareStatement(query);
                rs = AccessDB.querySELECT(ps, username, postid);

                if (!rs.next()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                } else {
                    String title = rs.getString("title");
                    String body = rs.getString("body");

                    request.setAttribute("title", title);
                    request.setAttribute("body", body); 

                    request.getRequestDispatcher("/edit.jsp").forward(request, response);
                } 

            } catch (SQLException ex){
                System.out.println("Message   : " + ex.getMessage());
            
            }

        } else {
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
        }

    }


    /* 
     * Checks whether the parameters entered in the url are correct.
     * @param username  the username in the url request, should not be empty     
     *        action    the action in the url request, should not be empty
     *        request   the request from doGet() or doPost()
     *        response  the response from doGet() or doPost()
     */
    public void checkParameters(String username, String action, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String errorMsg = "";
        if(username == null || username.isEmpty()) {
            errorMsg = errorMsg + "Please, enter a username in the url.";    
        }

        if(action == null || action.isEmpty()) {
            errorMsg = errorMsg + "<br/> Please, enter an 'action' in the url. The options are: list, open, preview, save, delete.";
        }

        if(!errorMsg.isEmpty()) {
            String errorMsgFull = "Please, make sure the url you entered is correct." + "<br/>" + errorMsg;
            request.setAttribute("error", errorMsgFull);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }


    /* 
     * Checks whether the parameter postid is integer
     * @param  postid    the parameter postid of the request     
     *         request   the request from doGet() or doPost()
     *         response  the response from doGet() or doPost()
     * @return postid as an int value if the parameter was an integer.
     *         Otherwise, send user to page error.jsp
     */
    public static int checkPostid(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException
    {
        int postid = 0;
        try {
            postid = Integer.parseInt(request.getParameter("postid"));
        } catch (NumberFormatException ex) {
            request.setAttribute("error", "Please, make sure that the postid you entered in the url is a number.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }

        return postid;
    }



}


