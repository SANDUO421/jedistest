package jedistest;

import java.util.ArrayList;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class ShardClusterTest {
	
	public static void main(String[] args) {
		
		//poolConfig�����ӳص����ò���
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		
		//shards�Ƿ�Ƭ��Ⱥ�����з�Ƭ��������Ϣ�б�,JedisShardInfo�Ƿ�Ƭ��������Ϣ
		ArrayList<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		
		//����Ⱥ�е���̨shard��������Ϣ��װ������JedisShardInfo������ 
		JedisShardInfo shard1 = new JedisShardInfo("192.168.2.199", 6379);
		JedisShardInfo shard2 = new JedisShardInfo("192.168.2.199", 6380);
		
		//����Ƭ��������Ϣ������ӵ���Ƭ��������Ϣ�б�shards��
		shards.add(shard1);
		shards.add(shard2);
		
		//����һ�������ݷ�Ƭ���ܵ�jedis���ӳ�
		ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, shards);
		
		//�����ӳ��л�ȡһ��(�����ݷ�Ƭ���ܵ�)jedis����
		ShardedJedis jedis = shardedJedisPool.getResource();
		
		for(int i=0;i<1000;i++){
			jedis.set("string-key-"+i, "1000" + i);
		}
		
		jedis.close();
		
		shardedJedisPool.close();
		
	}
	
	

}
