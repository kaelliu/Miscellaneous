
public class UserSaveThread extends Thread{
	private long totalNeedSeekBack = 0;
	public void run() {
		if(hhe.serverPeopleCount > 0){
			long start = System.currentTimeMillis();
			// ��ǰ�û��ƻ�ʱ��
			int nextStartInterval = hhe.updateInterval/hhe.serverPeopleCount;
			int thisRoundNeedUpdatePeople = 1;
			// do job
			while(thisRoundNeedUpdatePeople != 0){
				RoleModel rm = (RoleModel) hhe.users.entrySet().toArray()[hhe.currentUpdateCursor];
				rm.onFlushToDb();
				hhe.currentUpdateCursor++;
				if(hhe.currentUpdateCursor >= hhe.serverPeopleCount){
					hhe.currentUpdateCursor = 0;
					totalNeedSeekBack = 0;
				}
				thisRoundNeedUpdatePeople--;
			}
			// ���㵱ǰ�û�����ʱ��
			long end = System.currentTimeMillis();
			long cost = end - start;
			// ��ǰ�û�������ʣ��ʱ��
			nextStartInterval -= cost;
			// ��һ�ε��Ȼ�Ƿʱ��
			long lastLeftCost = totalNeedSeekBack;
			// ��β������½�����Ҫ׷�ض���ʱ��
			totalNeedSeekBack -= nextStartInterval;
			if(nextStartInterval < 0 ){
				// ������û����������ƻ�ʱ��,��������ʱ��Ϊ0,�����û�����ǰ���û������ĵļƻ�ʱ��
				nextStartInterval = 0;
			}
			// ����ƻ�ʱ������ӯ��,����Ƿ����ʱ����ȣ����ߵ�һ��ʱ��
			// totalNeedSeekBackΪ����ʾ����Ҫ׷��ʱ�䣬�ɺ���ʱ����ಹ��ǰ��ʱ��ȱʧ
			// ������ǰ��ʱ����ಹ������ʱ������
			if(totalNeedSeekBack < 0){
				totalNeedSeekBack = 0;
			}

			try {
				// ���ⲹ����һ���û������ĵ�ʱ��,ֻ����nextStartInterval - lastLeftCostʱ��
				Thread.sleep(totalNeedSeekBack > 0 ? 0 : (nextStartInterval - lastLeftCost));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
