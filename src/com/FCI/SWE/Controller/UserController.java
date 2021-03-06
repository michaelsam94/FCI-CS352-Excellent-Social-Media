package com.FCI.SWE.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Vector;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.Message;
import com.FCI.SWE.Models.Requests;
import com.FCI.SWE.Models.User;
import com.FCI.SWE.ServicesModels.UserEntity;
import com.google.appengine.labs.repackaged.org.json.JSONException;

/**
 * This class contains REST services, also contains action function for web
 * application
 * 
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 *
 */
@Path("/")
@Produces("text/html")
public class UserController {

	/*
	 * @GET
	 * 
	 * @Path("/sharePost") public Response sharePost() { return Response.ok(new
	 * Viewable("/jsp/SharePost")).build(); }
	 */

	@GET
	@Path("/Notification")
	public Response Notification() {
		return Response.ok(new Viewable("/jsp/Notification")).build();
	}

	/*
	 * Msg Notifiaction it's working till reach to the html page (Show MN) then
	 * the error begain to appear the error in the final part of the code circle
	 */
	
	// this function to get the notification with the type message from the datastore
	@POST
	@Path("/msgNotification")
	public Response msgList(@FormParam("uemail") String uemail) {

		String serviceUrl = "http://localhost:8888/rest/mshNS"; // service url
		String urlParameters = "uemail=" + uemail;// take the name of the user that want to get his notification
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");

		JSONParser parser = new JSONParser();
		Map<String, Vector<Message>> passed_msgs = new HashMap<String, Vector<Message>>();
		//vector hold the user and message send to him
		
		try {
			JSONArray arr = (JSONArray) parser.parse(retJson);
			Vector<Message> msgs = new Vector<Message>();
			
			for (int i = 0; i < arr.size(); i++) {
				JSONObject object;
				object = (JSONObject) arr.get(i);
				msgs.add(Message.ParsemsgInfo(object.toJSONString()));
			}
			System.out.println(msgs.toString());
			passed_msgs.put("msgList", msgs);
			return Response.ok(new Viewable("/jsp/showMN", passed_msgs))
					.build();
		} catch (ParseException e) {
			System.out.println("Out of here");
			e.printStackTrace();
		}

		return null;
	}

	@POST
	@Path("/RNotification")
	public Response reqList(@FormParam("uemail") String uemail) {
		String serviceUrl = "http://localhost:8888/rest/getRS";
		String urlParameters = "uemail=" + uemail;
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");

		JSONParser parser = new JSONParser();
		Map<String, Vector<Requests>> passedReqests = new HashMap<String, Vector<Requests>>();
		try {
			System.out.println("Fady 2");
			JSONArray arr = (JSONArray) parser.parse(retJson);
			Vector<Requests> reqS = new Vector<Requests>();
			System.out.println(arr.size() + " Size");
			for (int i = 0; i < arr.size(); i++) {
				JSONObject object;
				object = (JSONObject) arr.get(i);
				reqS.add(Requests.ParsereqInfo(object.toJSONString()));
			}
			System.out.println(reqS.toString());
			passedReqests.put("reqList", reqS);
			return Response.ok(new Viewable("/jsp/showRN",passedReqests))
					.build();
		} catch (ParseException e) {
			System.out.println("Out of here");
			e.printStackTrace();
		}

		return null;
	}

	// ////////////////////////////////////////////////////////////

	@GET
	@Path("/signup")
	public Response signUp() {
		return Response.ok(new Viewable("/jsp/register")).build();
	}


	
	@GET
	@Path("/pagePost")
	public Response pagePost() {
		return Response.ok(new Viewable("/jsp/pagePost")).build();
	}

	@GET
	@Path("/userPost")
	public Response userPost() {
		return Response.ok(new Viewable("/jsp/userPost")).build();
	}
@GET
	@Path("/SendFriendRequest")
	public Response SFR() {
		return Response.ok(new Viewable("/jsp/SendFriendRequest")).build();
	}

	@GET
	@Path("/search")
	public Response search() {
		return Response.ok(new Viewable("/jsp/search")).build();
	}

	@GET
	@Path("/createPost")
	public Response creatPost() {
		return Response.ok(new Viewable("/jsp/CreatePost")).build();
	}
	@GET
	@Path("/ChangePostPrivacy")
	public Response ChangePostPrivacy(){
		return Response.ok(new Viewable("/jsp/ChangePostPrivacy")).build();
	}
	

	@GET
	@Path("/SendMSG")
	public Response sendmsg() {
		return Response.ok(new Viewable("/jsp/SendMSG")).build();

	}

	@GET
	@Path("/acceptFriend")
	public Response accept() {
		return Response.ok(new Viewable("/jsp/acceptFriend")).build();
	}

	/**
	 * Action function to render home page of application, home page contains
	 * only signup and login buttons
	 * 
	 * @return enty point page (Home page of this application)
	 */
	@GET
	@Path("/")
	public Response index() {
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}

	/**
	 * Action function to render login page this function will be executed using
	 * url like this /rest/login
	 * 
	 * @return login page
	 */
	@GET
	@Path("/login")
	public Response login() {
		return Response.ok(new Viewable("/jsp/login")).build();
	}

	@GET
	@Path("/entryPoint")
	public Response exit() {
		User.setCurrentActiveUserToNull();
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}

	/**
	 * Action function to response to signup request, This function will act as
	 * a controller part and it will calls RegistrationService to make
	 * registration
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided user password
	 * @return Status string
	 */

	
	@POST
	@Path("/changePri")
	public Response changePri(@FormParam("pID") String PrivacyID ,@FormParam("pPR") String Privacy) {
		String serviceUrl = "http://localhost:8888/rest/chPService";
		
		String urlParameters = "pID=" + PrivacyID + "&pPR=" +Privacy;
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Map<String, Vector<User>> passed_users = new HashMap<String, Vector<User>>();
		try {
			JSONArray arr = (JSONArray) parser.parse(retJson);
			Vector<User> users = new Vector<User>();
			for (int i = 0; i < arr.size(); i++) {
				JSONObject object;
				object = (JSONObject) arr.get(i);
				users.add(User.ParseUserInfo(object.toJSONString()));
			}
			passed_users.put("usersList", users);
			return Response.ok(new Viewable("/jsp/showUsers", passed_users))
					.build();
		} catch (ParseException e) {
			System.out.println("Out of here");
			e.printStackTrace();
		}

		return null;
	}

	
	
	@POST
	@Path("/doSearch")
	public Response usersList(@FormParam("uname") String uname) {
		System.out.println(uname);
		String serviceUrl = "http://localhost:8888/rest/SearchService";
		String urlParameters = "uname=" + uname;
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		System.out.println(retJson);
		JSONParser parser = new JSONParser();
		Map<String, Vector<User>> passed_users = new HashMap<String, Vector<User>>();
		try {
			JSONArray arr = (JSONArray) parser.parse(retJson);
			Vector<User> users = new Vector<User>();
			for (int i = 0; i < arr.size(); i++) {
				JSONObject object;
				object = (JSONObject) arr.get(i);
				users.add(User.ParseUserInfo(object.toJSONString()));
			}
			passed_users.put("usersList", users);
			return Response.ok(new Viewable("/jsp/showUsers", passed_users))
					.build();
		} catch (ParseException e) {
			System.out.println("Out of here");
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Action function to response to login request. This function will act as a
	 * controller part, it will calls login service to check user data and get
	 * user from datastore
	 * 
	 * @param uname
	 *            provided user name
	 * @param pass
	 *            provided user password
	 * @return Home page view
	 */
	@POST
	@Path("/home")
	@Produces("text/html")
	public static Response home(@FormParam("uname") String uname,
			@FormParam("password") String pass) {

		String urlParameters = "uname=" + uname + "&password=" + pass;

		String retJson = Connection.connect(
				"http://localhost:8888/rest/LoginService", urlParameters,
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			Map<String, String> map = new HashMap<String, String>();
			User user = User.getUser(object.toJSONString());
			map.put("name", user.getName());
			map.put("email", user.getEmail());
			return Response.ok(new Viewable("/jsp/home", map)).build();
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return null;

	}

	@POST
	@Path("/response")
	@Produces(MediaType.TEXT_PLAIN)
	public static String response(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {

		String serviceUrl = "http://localhost:8888/rest/RegistrationService";
		String urlParameters = "uname=" + uname + "&email=" + email
				+ "&password=" + pass;
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try {

			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Registered Successfully";

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return "Failed";
	}

	@POST
	@Path("/sendingmyrequest")
	@Produces(MediaType.TEXT_PLAIN)
	public static String response_sendmyrequest(
			@FormParam("senderemail") String Senderemail,
			@FormParam("reciveremail") String Reviveremail) {

		String urlParameters = "senderemail=" + Senderemail + "&reciveremail="
				+ Reviveremail;

		String retJson = Connection.connect(
				"http://localhost:8888/rest/SendrequestService", urlParameters,
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");

		JSONParser parser = new JSONParser();
		Object obj;

		try {

			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;

			if (object.get("Status").equals("OK")) {

				return "Your Request Sent Successfully";
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Failed";
	}

	@POST
	@Path("/createpost")
	@Produces(MediaType.TEXT_PLAIN)

	public static String  response_post(@FormParam("postContent") String postContent,
			@FormParam("postPrivacy") String postPrivacy,
			@FormParam("feeling") String postFeeling,
			@FormParam("pageName") String pageName,
			@FormParam("customNames") String customNames) {
		String serviceUrl = "http://localhost:8888/rest/createPostService";
		String urlParameters = "postPrivacy=" + postPrivacy + "&postContent="
				+ postContent + "&feeling=" + postFeeling + "&pageName="
				+ pageName + "&cutomNames" + customNames;
	String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Your post saved successfully";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "Failed";
	}

	@POST
	@Path("/sendmymsg")
	@Produces(MediaType.TEXT_PLAIN)
	public static String response_msg(@FormParam("reciveremail") String ReciverN,
			@FormParam("msg") String msg) {

		String serviceUrl = "http://localhost:8888/rest/sendmsgService";
		String urlParameters = "reciveremail=" + ReciverN + "&msg=" + msg;

		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try {

			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Your message sent Successfully";

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "Failed";

	}




	@POST
	@Path("/createPagepost")
	@Produces(MediaType.TEXT_PLAIN)
	public static String response_pagePost(@FormParam("Post") String Post,
			@FormParam("Page") String Page) {

		String serviceUrl = "http://localhost:8888/rest/pagePostService";
		String urlParameters = "Post=" + Post + "&Page=" + Page;

		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try {

			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Your Post Posted Successfully";

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "Failed";

	}

	
	@POST
	@Path("/createUserpost")
	@Produces(MediaType.TEXT_PLAIN)
	public static String response_userPost(@FormParam("Post") String Post,
			@FormParam("Felling") String Felling) {

		String serviceUrl = "http://localhost:8888/rest/userPostService";
		String urlParameters = "Post=" + Post + "&Felling=" + Felling;

		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try {

			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Your Post Posted Successfully";

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "Failed";

	}

	
	@GET
	@Path("/hashtag")
	public Response hashtag() {
		return Response.ok(new Viewable("/jsp/hashtag")).build();
	}
	

	@POST
	@Path("/getHashtagPost")
	@Produces(MediaType.TEXT_PLAIN)
	public static String response_hashtag(@FormParam("hash") String hash) {

		String serviceUrl = "http://localhost:8888/rest/userPostService";
		String urlParameters = "hash=" + hash ;

		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try {

			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "hashtaggets";

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "Failed";

	}
	
	
	
	

}