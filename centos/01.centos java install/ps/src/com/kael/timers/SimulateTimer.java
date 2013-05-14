package com.kael.timers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import com.kael.PServer;
import com.kael.datastruct.RoleDto;
import com.service.RoleModel;

public class SimulateTimer extends TimerTask{

	private Random r = new Random(System.currentTimeMillis());
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int b =r.nextInt(100);
		if(b < 10){
			// insert
		}else {
			// update
			RoleModel rm = (RoleModel) PServer.modelEntry.get("role");
			RoleDto rd = (RoleDto) rm.GetObject(1, 1);
			Map modify = new HashMap();
			rd.setRolehp(rd.getRolehp()-1);
			if(rd.getRolehp()<0)
				rd.setRolehp(123);
			rd.setRolex((short) (rd.getRolex()+1));
			if(rd.getRolex()>1920)
				rd.setRolex((short) 0);
			rd.setRoley((short) (rd.getRoley()+1));
			if(rd.getRoley()>1080)
				rd.setRoley((short) 0);
			modify.put("rolex", rd.getRolex());
			modify.put("roley", rd.getRoley());
			modify.put("rolehp", rd.getRolehp());
			PServer.modelEntry.get("role").PutObject(1, 1, rd, modify);
			System.out.println("updating role...hp:"+rd.getRolehp());
		}
	}

}
