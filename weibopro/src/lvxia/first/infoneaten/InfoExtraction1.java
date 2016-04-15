package lvxia.first.infoneaten;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.json.JSONObject;//必须用这个JSON包才能解析对象
import org.json.JSONArray;//必须用这个JSON包才能解析数组


/*
 * 获取数据~
 */
public class InfoExtraction1 {

	private static Statement statement;
	private static PrintStream pErrorUser;
	private static Set<String> uidSet;

	static {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://127.0.0.1:3306/data2016?userUnicode=true&characterEncoding=utf8";
			String user = "root";
			String password = "";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			statement = conn.createStatement();

			File errorUserFile = new File("error_user.txt");
			FileOutputStream outUserFile = new FileOutputStream(errorUserFile,
					true);
			pErrorUser = new PrintStream(outUserFile, false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符,只允许汉字字母数字和某些常见符号出现
		String regEx = "[^0-9a-zA-Z\u4e00-\u9fa5~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll(" ").trim().replaceAll("s+", " ");
	}
	
    /*
     * 插入认证理由
     */
	public static void insertVerified(String uid, String verifiedtext, String occupation1, Statement statement)
			throws Exception {
		if (uid == null)
			return;

		int len1 = uidSet.size();
		uidSet.add(uid);
		int len2 = uidSet.size();
		if((len2-len1)==0){
			return;
		}
		String sql = "insert into data2016.user(uid,verifiedtext,occupation1) values(\"" + uid+  "\",\""	+ verifiedtext +  "\",\""	+ occupation1 +"\")";
		
		try {
			statement.execute(sql);
		} catch (Exception e) {
			pErrorUser.println("verified:"+uid);                                                                   
			e.getMessage();
			System.out.println("\n"+sql);
			return;
		}
	}

	public static void readUserUid() throws Exception{
		String regEx = "[^a-zA-Z0-9\u4e00-\u9fa5]";
		Pattern p = Pattern.compile(regEx);

		File file = new File("data/source/first/verifiedText.json");   
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件  
		
		String line = "";
		int count = 0;
		while((line = reader.readLine()) != null){
			count++;
			JSONObject verifiedObject = new JSONObject(line);
            String uid = verifiedObject.getString("id");
		    String verifiedtext = verifiedObject.getString("verifiedText").replaceAll("[\r\n\"]", " ");
		    String profession = verifiedObject.getString("profession");
			Matcher m = p.matcher(profession);
//		    System.out.println(uid + " "+ verifiedtext+ " " +profession);
		    insertVerified(uid,verifiedtext,m.replaceAll("").trim(),statement);
		    if(count%500==0){
		    	System.out.print(m+"...");
		    }
		}
	}
	
	/*
	 * 插入自我介绍
	 */
	public static void insertIntro(String uid, String introduction, Statement statement)
			throws Exception {
		if (uid == null)
			return;
		String sql = "update data2016.user set introduction=\"" + introduction+  "\" where uid=\""	+ uid  +"\"";
		
		try {
			statement.execute(sql);
		} catch (Exception e) {
			pErrorUser.println("intro:"+uid);                                                                   
			e.getMessage();
			System.out.println("\n"+sql);
			return;
		}
	}
	
	
	public static void readUserIntro() throws Exception{
		String regEx = "[^0-9a-zA-Z\u4e00-\u9fa5~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+";
		Pattern p = Pattern.compile(regEx);

		File file = new File("data/source/first/introduction.json");   
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件  
		
		String line = "";
		String temp = "";
		int count = 0;
		while((line = reader.readLine()) != null){
			count++;
			JSONObject introObject = new JSONObject(line);
            String uid = introObject.getString("id");
		    String introduction = introObject.getString("introduction").replaceAll("[\r\n\"]", " ");
			Matcher m = p.matcher(introduction);
            temp = m.replaceAll(" ").replaceAll("\\s+"," ");
		    insertIntro(uid,temp,statement);
		    if(count%500==0){
		    	System.out.print(count +"...");
		    }
		}
	}
	
	/*
	 * 插入标签信息
	 */
	public static void insertTags(String uid, String tags, Statement statement)
			throws Exception {
		if (uid == null)
			return;
		String sql = "update data2016.user set tags=\"" + tags+  "\" where uid=\""	+ uid  +"\"";
		
		try {
			statement.execute(sql);
		} catch (Exception e) {
			pErrorUser.println("tags:"+uid);                                                                   
			e.getMessage();
			System.out.println("\n"+sql);
			return;
		}
	}
	
	public static void readUserTags() throws Exception{
		File file = new File("data/source/first/tags.json");   
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件  
		
		String line = "";
		int count = 0;
		while((line = reader.readLine()) != null){
			count++;
			JSONObject tagsObject = new JSONObject(line);
            String uid = tagsObject.getString("id");
		    String tags = tagsObject.getString("tags").replaceAll("[\r\n\"]", " ");
		    insertTags(uid,tags,statement);
		    if(count%500==0){
		    	System.out.print(count +"...");
		    }
		}
	}
	
	public static void insertProfession(String uid, String name, String profession1, String profession2, String profession3, Statement statement)
			throws Exception {
		if (uid == null)
			return;
		String sql = "update data2016.user u set u.name=\"" + name+  "\",u.profession1=\""+ profession1 + "\",u.profession2=\""+ profession2 + "\",u.profession3=\""+ profession3+ "\" where uid=\""	+ uid  +"\"";
		
		try {
			statement.execute(sql);
		} catch (Exception e) {
			pErrorUser.println("profession:"+uid);                                                                   
			e.getMessage();
			System.out.println("\n"+sql);
			return;
		}
	}
	
	public static void readUserProfession() throws Exception{
		String regEx = "[^0-9a-zA-Z\u4e00-\u9fa5~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+";
		Pattern p = Pattern.compile(regEx);
		
		File file = new File("data/source/first/profession.json");   
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件  
		
		String line = "";
		String temp = "";
		int count = 0;
		while((line = reader.readLine()) != null){
			count++;
			int n = line.indexOf("letter");
            StringBuffer sb = new StringBuffer(line);
            sb.insert(n+8, "\"");
            sb.insert(n+10, "\"");
//            System.out.println(sb.toString());
			JSONObject proObject = new JSONObject(sb.toString());
            String uid = proObject.getString("uid");
            String profession1 = proObject.getString("profession1");
            String profession2 = proObject.getString("profession2");
            String profession3 = proObject.getString("profession3");
		    String name = proObject.getString("name").replaceAll("[\r\n\"]", " ");
		    Matcher m = p.matcher(name);
            temp = m.replaceAll(" ").replaceAll("\\s+"," ");
//		    System.out.println(uid + " "+ verifiedtext);
            insertProfession(uid,temp,profession1,profession2,profession3,statement);
		    if(count%500==0){
		    	System.out.print(count +"...");
		    } 
		}
	}
	
	/*
	 *用户关注列表 
	 */
	public static void insertNetwork(String uid, String network, Statement statement)
			throws Exception {
		if (uid == null)
			return;
		String selectsql = "select count(*) from data2016.user where uid = \""+ uid+ "\"";
		String updatesql = "update data2016.user u set u.network=\"" + network+  "\" where uid=\""	+ uid  +"\"";
		String insertsql = "insert into data2016.user(uid,network) values(\"" + uid+  "\",\"" + network +"\")";
		try {
		   ResultSet rset = statement.executeQuery(selectsql);
		   int rowCount = 0; //记录查询结果记录数
		   if(rset.next()) { 
		      rowCount=rset.getInt(1); 
		   }
		   
		   if(rowCount==0){
			   statement.execute(insertsql);
//			   System.out.println(insertsql);
		   }else {
			   statement.execute(updatesql);
//			   System.out.println(updatesql);
		   }
		  
		} catch (Exception e) {
			pErrorUser.println("network:"+uid);                                                                   
			e.printStackTrace();
 			return;
		}
	}
	
	public static void readUserNetwork() throws Exception{
		File file = new File("data/source/first/network.txt");   
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件  
		
		String line = "";
		int count = 0;
		while((line = reader.readLine()) != null){
			count++;
			String[] strs = line.split("\t");
			String uid="";
			String network="";
			if(strs.length==2){
				uid = strs[0];
				network = strs[1];
			}else{
				uid = strs[0];
				network = "";
			}
            insertNetwork(uid,network,statement);
		    if(count%500==0){
		    	System.out.print(count +"...");
		    }
		}
	}

	public static void insertMicroblogs(String uid, String mid, String time,String geo,String source,
			String repost,String comment,String attitude,String text,int flag,Statement statement)
			throws Exception {
		if (uid == null)
			return;
		String sql = "insert into data2016.microblog1 values(\"" + mid+  "\",\"" + uid +  "\",\"" + time
				+  "\",\"" + geo+  "\",\"" + source +  "\",\"" +repost +  "\",\"" + comment +  "\",\"" + attitude
				+  "\",\"" + text+ "\"," + flag + ")";
		String selectsql = "select count(*) from data2016.microblog1 where uid = \""+ uid+ "\" and mid=\"" + mid + "\"";
		
		try {
			ResultSet rset = statement.executeQuery(selectsql);
			int rowCount = 0; //记录查询结果记录数
			if(rset.next()) { 
			    rowCount=rset.getInt(1); 
			}
			if(rowCount == 0){
				statement.execute(sql);
			}
		  
		} catch (Exception e) {
			pErrorUser.println("microblog:"+uid); 
//			System.out.println(sql);
			e.printStackTrace();
 			return;
		}
	}
	public static void readUserMicroblogs() throws Exception{
		//0 1
		File file = new File("data/source/first/microblogs/microblogs1.txt");   
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件  
		
		String line = "";
		int count = 0;
		while((line = reader.readLine()) != null){
			count++;
			JSONObject blogObject = new JSONObject(line);
			String uid = blogObject.getString("uid");
			String microblogs = blogObject.getString("microblogs");
			JSONArray microblogsArray = new JSONArray(microblogs);
			int size = microblogsArray.length();
//			System.out.println("Size:" + size);
			for (int i = 0; i < size; i++) {
			  JSONObject jsonObj = microblogsArray.getJSONObject(i);
			  String mid=jsonObj.get("mid").toString();
			  String created_at=jsonObj.get("created_at").toString();
			  String geo=StringFilter(jsonObj.get("geo").toString().replaceAll("\"", " "));
			  String source=jsonObj.get("source").toString().replaceAll("\"", " ");
			  String reposts_count=jsonObj.get("reposts_count").toString();
			  String comments_count=jsonObj.get("comments_count").toString();
			  String attitudes_count=jsonObj.get("attitudes_count").toString();
			  String text;
			  int flag;
			  if(jsonObj.has("retweeted_status")){
				  flag=1;
				  String retweet = jsonObj.get("retweeted_status").toString();
				  JSONObject weibo = new JSONObject(retweet);
				  text=StringFilter(jsonObj.getString("text").toString().replaceAll("\"", " ")+" "+weibo.get("text").toString().replaceAll("\"", " "));
			  }else{
				  flag=0; 
				  text=StringFilter(jsonObj.get("text").toString().replaceAll("\"", " "));
			  }
			  insertMicroblogs(uid,mid,created_at,geo,source,reposts_count,comments_count,attitudes_count,text,flag,statement);
			  
			}

		    if(count%50==0){
		    	System.out.print(count +"...");
		    }
		    if(count%500==0){
		    	System.out.println();
		    }
		}
	}

	public static void main(String[] args) {

		try {
			uidSet = new HashSet<String>();
			long start = System.currentTimeMillis();
//			readUserUid();//插入uid\verifiedtext\occupation1
//			readUserIntro();
//			readUserTags();
//			readUserProfession();
//			readUserNetwork();
			readUserMicroblogs();
			long end = System.currentTimeMillis();
			System.out.println("time:" + (end - start) + "ms");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
