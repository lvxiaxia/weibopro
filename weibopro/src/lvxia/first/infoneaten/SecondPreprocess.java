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
	 * ��ȡ�������� Ԥ����
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
	 * ͳ��ÿ���û���΢������count�������뵽user��
	 */
	public static void statCount(){
		
	}
	/*
	 * ͳ��occupation1�в�ͬ�����û��ֲ��ж��ٸ�
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

			// ��ռ����Ϣ����job_distribution.txt�ĵ�
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
	 * ֻ����ǰ14����𣬺������ҵ�����Ŀռ��̫С��������1000
	         ��ý=15956   ������Ա=9401   ����=5523    ���ز�ҵ=5088
                       �ƾ�=4361  ITͨ��=4005 ����=3492  ����=2502
                       ʱ��=2447  ��Ϸ����=2343 ��ѧ����=2148 ��ҵ����ҵ=2119
                      ��������=1949  ����ҽ��=1081
                      �����������û���Ϣ��user_all�����¹�����Ч�û�
       insert into user (select * from user_all where occupation1 like "��ý" );
                      �����insert into feature (select * from user_effective_all where  weiboNumber>=9 and weiboNumber<=300 and occupation like "A1" ORDER BY RAND() limit 1300);
	 */
	
	/*
	 * ʶ��occupation2 ����ְҵ����̫�࣬�Ȳ����˰ɣ������·���ʶ��������˵
	 */
	public static void recogOccup(){
		/*
		 * �����update user_verified set occupation="A4" where verifiedReason like "%����%";
		 */
		String sql = "select profession3 from data2016.user_all where occupation1 like \"����ҽ��\"";
		try {
			ResultSet rset = statement.executeQuery(sql);
			HashSet<String> set=new HashSet<String>();
			while(rset.next()){
				set.add(rset.getString("profession3"));
			}
			for(Iterator it=set.iterator();it.hasNext();)
			{
			   System.out.print(it.next()+"��");
			}
			
		} catch (Exception e) { 
			e.printStackTrace();
 			return;
		}
	}
    /*
     * ��ý{����ý�塢���ʡ���ý�߹ܡ��ܱ����ࡢ�ܱ༭�������������ˡ��ർ�������˵��ʱ��ý���ˡ���ýר�ҡ�DJ���༭�����ߡ�����ý���ˡ�����ý���ˡ����ݡ�����ý���ˡ�ר�Ҽ��ߡ����߱༭�����������ƹ㡢��ǡ���Ʒ�ˡ���Ƭ�ˡ���̨�����ƹ�}
     * ����{�г�/Ӫ����Ӱ����Ա��Ӱ�ӵ��ݡ�������Ա��������ˡ������ˡ������ˡ�ͯ�ǡ����и��֡�������/�ֶ���ϡ������������Ƭ��ԭ��̬�����ָ߹ܡ�����/ý�顢������֡��������֡��߻�/����}
     * ���ز�ҵ{�߹ܡ��г�/Ӫ����Ͷ�ʹ��ʡ���ҵ����רҵ��Ա������/�����������̡���ҵ�߹ܡ����/�󻮡�����ʦ��������Ӫ���߻�ʦ������ý���ˡ�ѧ��ר��}
     * �ƾ�{����ʦ���߹ܡ�������ѯ������ѧ�ˡ�����ҵ�ڡ�ע����ʦ�����ʦ������ҵ�ڡ�̫���ܡ�����ʦ���̽����ˡ�ú̿������ȼ�ϡ��ƾ���ҵ�߹ܡ�ҵ����ʿ��ʯ�͡����ר�ҡ�������Ͷ���ˡ���ѵʦ���ͻ�����ˮ��}
     * ITͨ��{��Ӫ�̡�����Ա���豸�̡�վ��������SP��IT�߹ܡ�������ר��ѧ�ߡ��ҵ���ҵ����վ�߹ܡ�ҵ�ڡ�������Ϸ���ֻ����̡���������}
     * ����{��ѩ�˶�Ա�������ʿ�������߹ܡ����ѡ������֡���Ʊר�ҡ��������˶�Ա���߶������������ֲ���Ա�����֡���Ա���߶���ý���ˡ�ˮ����Ŀ}
     * ����{��ѧ��ʦ��ѧ���ḱ��ϯ����ѧ���ʡ�ְ������СѧУ������ѵʦ������ѧ��ר�ҡ�ѧ������ϯ���׶�԰԰������ѧУ������Сѧ��ʦ}
     * ʱ��{���׷�ˮ������ʦ��ģ�ؾ����ˡ��������ʦ��Ʒ�ƴ�ʼ�ˡ��鱦����װ���ʦ����ģ��ռ��ʦ��T̨ģ�ء���ͧ���鱦���ʦ�����Ӱ�ӹ��ģ�ء�����ʦ��ƽ��ģ�ء���ҵЭ�ᡢʱ����߹�}
     * ��Ϸ����{Coser���г�/Ӫ������Ϸ�����������ҡ������������ߡ���Ϸshowgirl����˵�������ˡ�ģ�͡���Ϸ��ҡ�ҵ����ʿ����Ϸ�ٷ���ս�ӡ���������Ϸ��˾�߲㡢���š�����ý���ˡ�cosplay���š�ѡ�֡�����Ա���廭ʦ}
     * ��ѧ����{����ƾ����ҡ������ഺ���ҡ�ר�����ҡ��Ļ����ҡ�����汾���ҡ�����������ҡ�������־���ҡ�����ٳ����ҡ������硢����������ҡ�����������ҡ����綼�����ҡ����紩Խ���ҡ���ͳ���ҡ�����������ҡ������������ҡ������������ҡ�����У԰���ҡ�����ƻ����ҡ������������ҡ�����ְ����ս���ҡ��������ɿֲ����ҡ�ͯ������}
     * ��ҵ����ҵ{�߹ܡ�Ӫ��ר�ҡ�Ӫ��ҵ�߹ܡ�����ר�ҡ�������ʦ����ʦ������ҵ�ڡ���ͷ�����ҵ�߹ܡ�����ҵ�߹ܡ����ҵ��}
     * ��������{�����ڽ̡�ˮ�ʻ��ҡ���������Ա�����軭�ҡ�ҵ��������ƽ�����ʦ�����������ҡ������ҡ�������չ�ˡ������г���ҵ�ߡ�Ϸ����Ա��СƷ��Ա���ͻ��ҡ���������������Ա���Ӽ���Ա���鷨�ҡ��ղؼҡ�������Ա��ħ��ʦ��ѧ�ߡ���������ҡ��߹ܡ�Ħ������Ա�����̡���̡���������ҡ���ʷѧ�ҡ�ʫ�ˡ���������Ա���ִ�����Ա��������Ա��������Ա����Ӱʦ���滭�ҡ������Ա��������Ա�����־���Ա���մ������ҡ������Ա��������Ա}
     * ����ҽ��{�߹ܡ�������Ա��ҽ����������Ӫ��ʦ�������ѯʦ��ҽ�Ƽ���������Ա������/ҽѧר�ҡ�����/��ʦ��ҩ��ʦ��������ѯʦ}
     */
	
	public static void main(String[] args) {
		//statDistribution();//ͳ��ְҵ���ֲ���ѡ��14�����ռ�ȵ�
		recogOccup();
	}

}
