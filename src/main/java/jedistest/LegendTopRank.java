package jedistest;

import java.util.Random;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * ģ��Ӣ��������Ϸ���ӵ�Ӣ�����а�ʵ��
 * 
 * @author
 * 
 */
public class LegendTopRank {

	// ���ݲɼ��̣߳���ȡӢ�ۣ���Ϸ��ɫ���������ݲ����뵽redis���ݿ���
	static class HeroScoreGenerate implements Runnable {

		public void run() {
			String[] heros = new String[] { "äɮ", "����", "ʯͷ", "����", "Ů��", "���", "��ʥ", "����" };

			Random random = new Random();
			Jedis jedis = new Jedis("192.168.2.199");
			// ģ�ⲻ�ϲ����µ�Ӣ�۳�������
			while (true) {
				try {
					Thread.sleep(1000 + random.nextInt(10) * 100);
					// �����ѡ��һ��Ӣ�ۣ���ʾ�������ѡ������Ӣ�۲�����һ����Ϸ
					int index = random.nextInt(8);
					String hero = heros[index];
					
					// ��redis���ݿ��и���Ӧ��Ӣ�����ӷ���
					jedis.zincrby("TopRankChuChang", 1, hero);
					
				} catch (InterruptedException e) {

					e.printStackTrace();
				}

			}

		}

	}

	// ��ȡ���а񵥵��߳�
	public static void main(String[] args) {
		new Thread(new HeroScoreGenerate()).start();
		Jedis jedis = new Jedis("192.168.2.199");
		int i = 0;
		// ���ϵ�ȥ��ȡ���µ����а�
		while (true) {

			try {
				Thread.sleep(3000);
				Set<Tuple> heros = jedis.zrevrangeWithScores("TopRankChuChang", 0, -1);
				System.out.printf("��%s�λ�ȡ���а�", i);
				System.out.println();
				
				for (Tuple t : heros) {
					System.out.println(t.getElement() + " : " + t.getScore());
				}

				i++;

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
