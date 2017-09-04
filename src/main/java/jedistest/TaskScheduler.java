package jedistest;

import java.util.Random;
import java.util.UUID;

import redis.clients.jedis.Jedis;

public class TaskScheduler {

	/**
	 * �����������߳��߼�
	 * 
	 * @author
	 * 
	 */
	static class TaskProducer implements Runnable {

		public void run() {
			Jedis jedis = new Jedis("192.168.2.199");
			System.out.println("��������������.......");
			jedis.del("task-list");
			
			while (true) {

				UUID newTaskId = UUID.randomUUID();
				
				//"{"id":"100","operate":"insert"}"
				jedis.lpush("task-list", newTaskId.toString());
				System.out.println("�����߲�����һ�������� " + newTaskId);
				try {
					int nextInt = new Random().nextInt(2);
					Thread.sleep(1000 + nextInt * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	/**
	 * ���������߳��߼�
	 * 
	 * @author
	 * 
	 */
	static class TaskWorker implements Runnable {

		public void run() {

			Jedis jedis = new Jedis("192.168.2.199");
			System.out.println("������������.......");
			jedis.del("status-list");

			while (true) {
				try {
					Thread.sleep(1000);
					// ��task-list����һ�����񣬲����뵽status-list������
					String taskId = jedis.rpoplpush("task-list", "status-list");
					// ����������߼�......
					int nextInt = new Random().nextInt(9);
					// ģ��������ɹ������
					if (nextInt % 4 != 0) {

						// ��status-list�����е����������ɹ�������
						jedis.lpop("status-list");
						System.out.println(taskId + ": ����ɹ��������������ϵͳ�г���ɾ��");

					} else {
						// ģ��������ʧ�ܵ����
						jedis.rpoplpush("status-list", "task-list");
						System.out.println(taskId + ": ����ʧ�ܣ�����status-list�е���task-list");

					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	public static void main(String[] args) throws Exception {

		new Thread(new TaskProducer()).start();
		Thread.sleep(200);
		new Thread(new TaskWorker()).start();

	}

}
