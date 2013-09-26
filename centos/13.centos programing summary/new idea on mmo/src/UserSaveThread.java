
public class UserSaveThread extends Thread{
	private long totalNeedSeekBack = 0;
	public void run() {
		if(hhe.serverPeopleCount > 0){
			long start = System.currentTimeMillis();
			// 当前用户计划时间
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
			// 计算当前用户消耗时间
			long end = System.currentTimeMillis();
			long cost = end - start;
			// 当前用户操作后剩余时间
			nextStartInterval -= cost;
			// 上一次调度还欠时间
			long lastLeftCost = totalNeedSeekBack;
			// 这次操作导致进度需要追回多少时间
			totalNeedSeekBack -= nextStartInterval;
			if(nextStartInterval < 0 ){
				// 如果此用户工作超过计划时间,本次休眠时间为0,后续用户补上前面用户多消耗的计划时间
				nextStartInterval = 0;
			}
			// 如果计划时间仍有盈余,不亏欠总体时间进度，休眠掉一定时间
			// totalNeedSeekBack为负表示不需要追回时间，由后续时间空余补偿前面时间缺失
			// 不采用前面时间空余补偿总体时间做法
			if(totalNeedSeekBack < 0){
				totalNeedSeekBack = 0;
			}

			try {
				// 另外补偿上一个用户多消耗的时间,只休眠nextStartInterval - lastLeftCost时间
				Thread.sleep(totalNeedSeekBack > 0 ? 0 : (nextStartInterval - lastLeftCost));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
