import java.util.Random;
import java.util.TimerTask;


public class SimulateTimer extends TimerTask {
	private Random r = new Random(System.currentTimeMillis());
	private int count=0;
	@Override
	public void run() {
		if(hhe.serverPeopleCount > 0){
//			Thread.sleep(hhe.updateInterval);
//			int thisRoundNeedUpdatePeople = hhe.updateInterval/hhe.serverPeopleCount;
			int b = r.nextInt(100);
			System.out.println("current factor "+b);
			RoleModel rm = hhe.users.get(1);
			ItemMemoryData usedItem = rm.imds.get(r.nextInt(rm.imds.size()));
			count++;
			if(b < 20){
				// insert
				rm.onGetItem(b, (b % 9) + 1);
				System.out.println("onGetItem("+b+","+((b%9)+1)+")");
			}else if(b>=20 && b<95){
				// update
				if(usedItem != null){
					rm.onUseItem(usedItem.place, 0);
					System.out.println("onUseItem("+usedItem.place+")");
				}
			}else{
				// delete
				rm.onRemoveItem(usedItem.place);
				System.out.println("remove item:");
			}
			if(count >= 50){
				count = 0;
				rm.onFlushToDb();
			}
		}
	}

}
