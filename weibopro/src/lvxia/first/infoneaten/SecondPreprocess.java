package lvxia.first.infoneaten;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SecondPreprocess {

	/**
	 * step 2:
	 * 读取到的数据 预处理
	 */
	private static Statement statement;
	static {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://127.0.0.1:3306/data2016?userUnicode=true&characterEncoding=utf8";
			String user = "root";
			String password = "";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			statement = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 统计每个用户的微博数据count，并插入到user表
	 */
	public static void statCount(){
		
	}
	/*
	 * 统计occupation1中不同类别的用户分布有多少个
	 */
	public static void statDistribution(){
        String sql = "select occupation1,count(*) as count from data2016.user group by occupation1";
		try {
			ResultSet rset = statement.executeQuery(sql);
			HashMap<String,Integer> map=new HashMap<String,Integer>();
			while(rset.next()){
				map.put(rset.getString("occupation1"), Integer.parseInt(rset.getString("count")));
			}
			
			List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(
					map.entrySet());
			Collections.sort(infoIds,
					new Comparator<Map.Entry<String, Integer>>() {
						public int compare(Map.Entry<String, Integer> o1,
								Map.Entry<String, Integer> o2) {
							return (o2.getValue() - o1.getValue());
							//return (o1.getKey()).toString().compareTo(o2.getKey());
						}
					});

			// 将占比信息存入job_distribution.txt文档
			// FileWriter write = new FileWriter(dist, true);
			// DecimalFormat df = new DecimalFormat("0.00000");
			// double dis = (double)entry.getValue()/count;
			for (int i = 0; i < infoIds.size(); i++) {
				String str = infoIds.get(i).toString();
				System.out.println(str);
				//write.write(str + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
 			return;
		}
	}
	
	/* 
	 * 只保留前14种类别，后面的行业类别数目占比太小，不超过1000
	         传媒=15956   政府官员=9401   娱乐=5523    房地产业=5088
                       财经=4361  IT通信=4005 体育=3492  教育=2502
                       时尚=2447  游戏动漫=2343 文学出版=2148 商业服务业=2119
                      人文艺术=1949  健康医疗=1081
                      保存完整的用户信息到user_all表，重新过滤有效用户
       insert into user (select * from user_all where occupation1 like "传媒" );
                      借鉴：insert into feature (select * from user_effective_all where  weiboNumber>=9 and weiboNumber<=300 and occupation like "A1" ORDER BY RAND() limit 1300);
	 */
	
	/*
	 * 识别occupation2 具体职业种类太多，先不管了吧，先用新方法识别出类别再说
	 */
	public static void recogOccup(){
		/*
		 * 借鉴：update user_verified set occupation="A4" where verifiedReason like "%作家%";
		 */
		String sql = "select profession3 from data2016.user_all where occupation1 like \"健康医疗\"";
		try {
			ResultSet rset = statement.executeQuery(sql);
			HashSet<String> set=new HashSet<String>();
			while(rset.next()){
				set.add(rset.getString("profession3"));
			}
			for(Iterator it=set.iterator();it.hasNext();)
			{
			   System.out.print(it.next()+"、");
			}
			
		} catch (Exception e) { 
			e.printStackTrace();
 			return;
		}
	}
    /*
     * 传媒{体育媒体、主笔、传媒高管、总编主编、总编辑、其他、主持人、编导、名嘴解说、时尚媒体人、传媒专家、DJ、编辑、记者、旅游媒体人、育儿媒体人、导演、汽车媒体人、专家记者、记者编辑、电视宣传推广、娱记、出品人、制片人、电台宣传推广}
     * 娱乐{市场/营销、影视演员、影视导演、配音演员、网络红人、娱评人、经纪人、童星、流行歌手、音乐人/乐队组合、其他、编剧制片、原生态、娱乐高管、公关/媒介、民族歌手、美声歌手、策划/宣传}
     * 房地产业{高管、市场/营销、投资顾问、物业管理专业人员、人事/行政、开发商、物业高管、广告/企划、工程师、其他、营销策划师、房产媒体人、学者专家}
     * 财经{分析师、高管、管理咨询、经济学人、保险业内、注册会计师、理财师、银行业内、太阳能、精算师、商界名人、煤炭、生物燃料、财经行业高管、业内人士、石油、外汇专家、其他、投资人、培训师、客户服务、水能}
     * IT通信{运营商、程序员、设备商、站长、电信SP、IT高管、其他、专家学者、家电企业、网站高管、业内、网络游戏、手机厂商、电子商务}
     * 体育{冰雪运动员、相关人士、体育高管、球友、赛车手、彩票专家、教练、运动员、高尔夫、其他、俱乐部人员、棋手、球员、高尔夫媒体人、水上项目}
     * 教育{大学教师、学生会副主席、留学顾问、职工、中小学校长、培训师、教育学者专家、学生会主席、幼儿园园长、大学校长、中小学教师}
     * 时尚{周易风水、造型师、模特经纪人、其他设计师、品牌创始人、珠宝、服装设计师、车模、占星师、T台模特、游艇、珠宝设计师、腕表、影视广告模特、美容师、平面模特、行业协会、时尚类高管}
     * 游戏动漫{Coser、市场/营销、游戏主创、漫画家、动画形象作者、游戏showgirl、解说、音乐人、模型、游戏玩家、业内人士、游戏官方、战队、主创、游戏公司高层、声优、动漫媒体人、cosplay社团、选手、评论员、插画师}
     * 文学出版{新锐财经作家、新锐青春作家、专栏作家、文化名家、新锐绘本作家、网络婚恋作家、新锐励志作家、网络官场作家、出版社、新锐情感作家、网络军事作家、网络都市作家、网络穿越作家、传统作家、网络古言作家、新锐言情作家、网络玄幻作家、网络校园作家、网络科幻作家、网络武侠作家、网络职场商战作家、网络悬疑恐怖作家、童书作家}
     * 商业服务业{高管、营销专家、营销业高管、公关专家、广告设计师、律师、公关业内、猎头、广告业高管、公关业高管、广告业内}
     * 人文艺术{其他宗教、水彩画家、国标舞演员、素描画家、业内名流、平面设计师、当代艺术家、国画家、艺术策展人、艺术市场从业者、戏曲演员、小品演员、油画家、其他、民族舞演员、杂技演员、书法家、收藏家、相声演员、魔术师、学者、民间艺术家、高管、摩登舞演员、道教、佛教、玉雕艺术家、历史学家、诗人、拉丁舞演员、现代舞演员、话剧演员、芭蕾演员、摄影师、版画家、歌剧演员、街舞演员、音乐剧演员、陶瓷艺术家、舞剧演员、曲艺演员}
     * 健康医疗{高管、护理人员、医生、其他、营养师、情感咨询师、医疗技术辅助人员、养生/医学专家、整形/容师、药剂师、心理咨询师}
     */
	
	public static void main(String[] args) {
		//statDistribution();//统计职业类别分布，选择14中最高占比的
		recogOccup();
	}

}
