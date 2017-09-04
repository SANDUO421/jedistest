package jedistest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class JedisDemo {
	Jedis jedis = null;

	@Before
	public void init() {
		jedis = new Jedis("192.168.142.128");
	}
	
	

	/**
	 * String�������ݵ�api����
	 */
	@Test
	public void testString() {
		// ����һ��string���͵�����
		String res = jedis.set("jedis-s-key-01", "itcast ia the greatest it school");
		System.out.println(res);

		// ��ȡһ��string���͵�����
		String value = jedis.get("jedis-s-key-01");
		System.out.println(value);

		// ���ַ����Ļ�ȡ
		String value2 = jedis.getrange("jedis-s-key-01", 0, 5);
		System.out.println(value2);

		// ���ַ������滻
		jedis.setrange("jedis-s-key-01", 0, "itheima");
		String value3 = jedis.get("jedis-s-key-01");
		System.out.println(value3);

		jedis.setrange("jedis-s-key-01", 26, "trainningschool");
		String value4 = jedis.get("jedis-s-key-01");
		System.out.println(value4);
		
		//���ƫ���������ַ������ȣ�����Զ���\0x00
		jedis.setrange("jedis-s-key-01", 50, "very good");
		String value5 = jedis.get("jedis-s-key-01");
		System.out.println(value5);

		//setnx���ж�ָ����key�Ƿ���ڣ�����Ѵ��ڣ��򲻻��������
		for (int i = 0; i < 5; i++) {
			jedis.setnx("jedis-s-key-0" + i, "000-" + i);
		}
		

	}
	
	/**
	 * ����List���ݽṹ�Ĳ���
	 */
	@Test
	public void testList(){
		
		//�������ͷ������Ԫ��
//		Long count = jedis.lpush("jedis-l-key-01", "zhangsan","lisi","wangwu","zhaoliu");
//		System.out.println("�����Ԫ�ظ���Ϊ�� " + count);
		
		List<String> res = jedis.lrange("jedis-l-key-01", 0, -1);
		System.out.println("�����������е�Ԫ��Ϊ��");
		for(String s:res){
			
			System.out.println(s);
		}
		
		//��������м������Ԫ��
		jedis.linsert("jedis-l-key-01", LIST_POSITION.BEFORE, "lisi", "����");
		
		System.out.println("������Ԫ��֮�������Ԫ��Ϊ�� ");
		
		List<String> res2 = jedis.lrange("jedis-l-key-01", 0, -1);
		for(String s:res2){
			
			System.out.println(s);
		}
		
		Long count = jedis.lrem("jedis-l-key-01", 3, "zhangsan");
		System.out.println("�ɹ�ɾ����" + count + "��zhangsan");
		List<String> res3 = jedis.lrange("jedis-l-key-01", 0, -1);
		for(String s:res3){
			
			System.out.println(s);
		}
		
	}
	
	/**
	 * ����Hash���ݽṹ
	 */
	@Test
	public void testHash(){
		
		jedis.hset("jedis-h-key-01", "name", "zhangsan");
		
		String name = jedis.hget("jedis-h-key-01", "name");
		System.out.println(name);
		
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("password", "123");
		fields.put("age", "18");
		jedis.hmset("jedis-h-key-01", fields);
		
		
		Set<String> keys = jedis.hkeys("jedis-h-key-01");
		System.out.println("���е�key���£�");
		for(String key :keys){
			System.out.println(key);
			
		}
		
		List<String> hvals = jedis.hvals("jedis-h-key-01");
		System.out.println("���е�value���£� ");
		for(String val:hvals){
			
			System.out.println(val);
		}
		
		System.out.println("һ����ȡ�����е�key-value�ԣ�");
		Map<String, String> kvs = jedis.hgetAll("jedis-h-key-01");
		Set<Entry<String, String>> entrySet = kvs.entrySet();
		for(Entry<String, String> ent :entrySet){
			System.out.println(ent.getKey() + " : " + ent.getValue());
			
		}
		
	}
	
	/**
	 * ����set���͵����ݽṹ
	 */
	@Test
	public void testSet(){
		
		//����һ��set���͵����ݲ�����һЩmembers
		Long sadd = jedis.sadd("jedis-set-key-01", "java","c","c++","js","ruby");
		System.out.println("������һ��set���͵����ݣ����Ҳ���"+sadd+"����Ա");
		
		//��ȡһ��set���͵����ݵĳ�Ա
		Set<String> members = jedis.smembers("jedis-set-key-01");
		System.out.println("��ȡ���ĳ�ԱΪ��");
		for(String m:members){
			System.out.println(m);
		}
		
		jedis.sadd("jedis-set-key-02", "c#","c",".net","python","ruby");
		
		
		//���������ϵĲ
		Set<String> set12 = jedis.sdiff("jedis-set-key-01","jedis-set-key-02");
		
		System.out.println("set01 ��ȥ set02 �Ĳ���Ϊ��");
		
		for(String s:set12){
			System.out.println(s);
		}
		
		
		
		Set<String> set21 = jedis.sdiff("jedis-set-key-02","jedis-set-key-01");
		
		System.out.println("set02 ��ȥ set01 �Ĳ���Ϊ��");
		
		for(String s:set21){
			System.out.println(s);
		}
		
		//���������ϵĲ���
		Set<String> union12 = jedis.sunion("jedis-set-key-01","jedis-set-key-02");
		System.out.println("set01��set02�Ĳ������Ϊ:");
		for(String s:union12){
			System.out.println(s);
		}
		
		
		//���������ϵĽ���
		
		
	}
	
	/**
	 * ����sortedset���ݽṹ
	 */
	@Test
	public void testSortedSet(){
		
		HashMap<String, Double> scoreMembers = new HashMap<String, Double>();
		scoreMembers.put("zhangsan", 100.00);
		scoreMembers.put("lisi", 90.00);
		scoreMembers.put("wangwu", 80.00);
		scoreMembers.put("zhaoliu", 70.00);
		scoreMembers.put("tianqi", 60.00);
		
		jedis.zadd("jedis-zset-key-01", scoreMembers);
		
		
		
		//��ȡָ��������������г�Ա��˳��Ϊ�������ɵ͵���
		Set<String> allMembers = jedis.zrange("jedis-zset-key-01", 0, -1);
		System.out.println("���еĳ�ԱΪ��");
		for(String m: allMembers){
			
			System.out.println(m);
		}
		
		
		System.out.println("��������40��֮�󣬳�Ա���������Ϊ��");
		//��ָ���ĳ�Ա���ӷ���
		jedis.zincrby("jedis-zset-key-01", 40, "zhaoliu");
		
		//��ȡָ��������������г�Ա��������ķ�����˳��Ϊ�������ɵ͵���
		Set<Tuple> zrangeWithScores = jedis.zrangeWithScores("jedis-zset-key-01", 0, -1);
		for(Tuple t:zrangeWithScores){
			
			System.out.println(t.getElement() + " : " +t.getScore());
		}
		
		
		System.out.println("���շ����ɸߵ��͵�˳���ӡ���а�");
		
		//��ȡָ��������������г�Ա��������ķ�����˳��Ϊ�������ɸߵ���
		Set<Tuple> zrevrangeWithScores = jedis.zrevrangeWithScores("jedis-zset-key-01", 0, -1);
		for(Tuple t:zrevrangeWithScores){
			
			System.out.println(t.getElement() + " : " +t.getScore());
		}
		
		
		//��չ��ϰ��������
		
	}
	
	
	/**
	 * �������key��ͨ�ò���
	 */
	@Test
	public void testGeneralKey(){
		
		Set<String> keys = jedis.keys("*");
		for(String key:keys){
			System.out.println(key);
		}
	}
	
	
	
	

	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.142.128", 6379);

		// ���Կͻ�����redis����������ͨ��
		String ping = jedis.ping();
		System.out.println(ping);
	}

}
