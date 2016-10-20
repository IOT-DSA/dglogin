package com.dglogik.login;

import com.tridium.web.*;

import java.io.*;
import java.util.*;

import javax.baja.file.*;
import javax.baja.naming.*;
import javax.baja.sys.*;
import javax.baja.web.*;
import javax.servlet.http.*;

/**
 * Login template for post Niagara AX 3.8 security changes.  This module and 
 * class name should be changed so to not conflict with any other modules
 * based on this code.
 *
 * @author Aaron Hansen
 */
public class BDGLogin 
  extends BLoginTemplate
  implements IStateLoginTemplate
{

  /////////////////////////////////////////////////////////////////
  // Constants - in alphabetical order by field name.
  /////////////////////////////////////////////////////////////////

  private static final String LOGIN_PAGE = "login.html";
  private static final String LOGIN_RESET_PAGE = "loginReset.html";

  private static int LOGIN_STATE_OK = 0;
  private static int LOGIN_STATE_RETRY = 1;
  private static int LOGIN_STATE_UNKNOWN_ERROR = 2;
  private static int LOGIN_STATE_PASSWORD_RESET = 3;
  private static int LOGIN_STATE_ILLEGAL_NETWORK_USER = 5;

  private static final String RESOURCE_DIR = "dg/";
  private static final String STARTUP = System.currentTimeMillis() + ""; 

  private static String VAR_START = "<?";
  private static String VAR_END = "?>";


  /////////////////////////////////////////////////////////////////
  // Properties
  /////////////////////////////////////////////////////////////////

  /*-
  class BDGLogin
  {
    properties
    {
    }
    actions
    {
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.dglogik.login.BDGLogin(962318706)1.0$ @*/
/* Generated Tue Oct 18 14:54:47 PDT 2016 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BDGLogin.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  /////////////////////////////////////////////////////////////////
  // Constructors
  /////////////////////////////////////////////////////////////////

  public BDGLogin() {}


  /////////////////////////////////////////////////////////////////
  // Methods - Public and in alphabetical order by method name.
  /////////////////////////////////////////////////////////////////

  /**
   * Get the BOrd for a file referenced in the login page.  I can't get
   * files from the station directory to work for some reason.
   */
  public BOrd resourceToOrd(String path)
  {
    if (path.startsWith(RESOURCE_DIR))
      return BOrd.make("module://dglogin/rsc/" + path.substring(RESOURCE_DIR.length()));
    return BOrd.make("module://web/com/tridium/web/rc/" + path);
  }

  /**
   * Pre-security changes, not sure if this is used but it's still in the 
   * super class and you have to subclass BLoginTemplate.  
   */
  public void write(
      BWebService svc, 
      HttpServletRequest req, 
      HttpServletResponse res, 
      boolean retry)
    throws Exception
  {
    int state = LOGIN_STATE_OK;
    if (retry) state = LOGIN_STATE_RETRY;
    doWrite(svc,req,res,state);
  }

  /**
   * Sends the login page to the client, post-security changes.
   */
  public void write(
      BWebService service, 
      HttpServletRequest req, 
      HttpServletResponse resp, 
      LoginState state)
    throws Exception
  {
    doWrite(service, req, resp, state.getState());
  }


  /////////////////////////////////////////////////////////////////
  // Methods - Protected and in alphabetical order by method name.
  /////////////////////////////////////////////////////////////////

  /**
   * Handles both pre and post security changes.
   */
  protected void doWrite(
      BWebService svc, 
      HttpServletRequest req, 
      HttpServletResponse res, 
      int state)
    throws Exception
  {
    BIFile f = getTemplate(state);
    BufferedReader in = null;
    try
    {
      in = new BufferedReader(new InputStreamReader(f.getInputStream()));
      PrintWriter out = res.getWriter();
      Map vars = getVariables(svc,req,res,state);
      String line = in.readLine();
      StringBuffer buf = new StringBuffer();
      while (line != null)
      {
        line = parseVariables(line,vars,buf);
        out.println(line);
        line = in.readLine();
      }
      out.flush();
    } 
    catch (Exception x)
    {
      x.printStackTrace();
    }
    finally 
    {
      if (in != null) in.close();
    }
  }

  /**
   * Load variables that can be used by the template.
   */
  protected Map getVariables(
      BWebService svc, 
      HttpServletRequest req, 
      HttpServletResponse res,
      int state)
  {
    TreeMap vars = new TreeMap();
    //Default values, you can add more if you want...
    vars.put("ABS_PATH_BASE","/");
    vars.put("AUTO_LOGIN","false"); 
    vars.put("COOKIE_POSTFIX","");
    vars.put("CONTENT_PATH","/login/dg/");
    vars.put("MESSAGE","");
    vars.put("SCHEME","cookie"); 
    vars.put("STATION_NAME","Niagara");
    vars.put("WEB_VERSION",STARTUP);
    String str = null;
    try
    {
      str = com.tridium.web.WebUtil.getAbsolutePathBase(req);
      vars.put("ABS_PATH_BASE",str);
      vars.put("CONTENT_PATH",str+"login/dg/");
    }
    catch (Throwable x) { }
    try
    {
      vars.put("AUTO_LOGIN",svc.getAutoLoginEnabled()+"");
    }
    catch (Throwable x) { }
    try
    {
      vars.put("COOKIE_POSTFIX", com.tridium.httpd.CookieUtil.getCookieName(req,""));
    }
    catch (Throwable x) { }
    if (state == LOGIN_STATE_RETRY) 
    {
      vars.put("MESSAGE","<b>Incorrect username and/or password</b>");
    }
    try
    {
      vars.put("SCHEME",svc.getAuthenticationScheme().getTag());
    }
    catch (Throwable x) { }
    try
    {
      vars.put("STATION_NAME",Sys.getStation().getStationName());
    }
    catch (Throwable x) { }
    try
    {
      vars.put("WEB_VERSION", Sys.getRegistry().getModule("web").getVendorVersion());
    }
    catch (Throwable x) {}
    try
    {
      str = BHttpAuthAgent.getAuthAgent()
        .getLoginMessageContent(req,res,LoginState.make(state)); 
      if (str != null) vars.put("MESSAGE",str);
    }
    catch (Throwable t) {}
    return vars;
  }

  /**
   * Return the appropriate template for the given state.
   */
  protected BIFile getTemplate(int state)
    throws Exception
  {
    String fileName = LOGIN_PAGE;
    if (state == LOGIN_STATE_PASSWORD_RESET)
      fileName = LOGIN_RESET_PAGE;
    return (BIFile) BOrd.make("module://dglogin/rsc/"+fileName).get();
  }


  /////////////////////////////////////////////////////////////////
  // Methods - Default and in alphabetical order by method name.
  /////////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////////
  // Methods - Private and in alphabetical order by method name.
  /////////////////////////////////////////////////////////////////

  /**
   * Replace variable tokens in the input line with values from the map.
   */
  private static String parseVariables(String line, Map vars, StringBuffer buf)
  {
    int idx = line.indexOf(VAR_START);
    if (idx < 0) return line;
    String orig = line;
    int start;
    int end;
    String key;
    Object val;
    int count = 0;
    while (idx >= 0)
    {
      if (++count > 500)
        throw new IllegalArgumentException("Infinite loop on line: " + orig);
      end = line.indexOf(VAR_END,idx+2);
      if (end < 0) break;
      key = line.substring(idx+2,end).trim();
      val = vars.get(key);
      if (val != null)
      {
        if (val instanceof Object[])
        {
          Object[] tmp = (Object[]) val;
          for (int i = 0, len = tmp.length; i < len; i++)
          {
            if (tmp[i] != null)
            {
              val = tmp[i];
              break;
            }
          }
        }
        buf.setLength(0);
        buf.append(line.substring(0,idx));
        if (val instanceof BObject)
          buf.append(((BObject)val).toString(null));
        else
          buf.append(val);
        buf.append(line.substring(end+2));
        line = buf.toString();
        start = 0;
      }
      else
      {
        start = idx + 1;
      }
      idx = line.indexOf(VAR_START,start);
    }
    return line;
  }


  /////////////////////////////////////////////////////////////////
  // Inner Classes - in alphabetical order by class name.
  /////////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////////
  // Attributes - in alphabetical order by field name.
  /////////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////////
  // Initialization
  /////////////////////////////////////////////////////////////////


}
