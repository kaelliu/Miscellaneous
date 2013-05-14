package com.toplogic;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.jboss.netty.channel.Channel;

import com.kael.PServer;

public class TimeTaskLogic {

	public static final byte WORLDMSG_DEF    = 1;
	public static final byte WORLDMSG_LAMP   = 2;
	public static final byte WORLDMSG_CHESS  = 3;
	public static final byte WORLDMSG_MANZU  = 4;
	public static final byte WORLDMSG_SIDE   = 5;
	public static final byte WORLDMSG_SWAR   = 6;
	public static final byte WORLDMSG_JTWAR  = 7;
	private int cnt = 0;
	
	/*ÿ��ִ��*/
	public void handleByMinute(){
		Calendar c = Calendar.getInstance();
		cnt++;
		if(cnt > (PServer.FlushDbTime / 60))
		{
			cnt = 0;
			recordInfo();
		}
	}
	
	private void recordInfo()
	{
		// д�����µķ���
		PServer.flushToDb();
	}
	

	//////////////////////////////////////////////////////////////////////////////////////
	//  ��ʱ������صĶ���ӿڣ����磺ˢ������  
	//////////////////////////////////////////////////////////////////////////////////////
	public void removeHangUpTask(String msg,Channel ch){
	}
	//���� ɨ��
	public void restartHangUpTask(String msg,Channel ch){
	}
	//ɨ������
	public void speedHangUpFight(String msg,Channel ch){
	}
	
	//Ӣ�۱�ɨ��
	public void hangUpByHero(String msg,Channel ch)
	{
		
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////
	//  �����ʱ��㷽����ڲ���   
	//////////////////////////////////////////////////////////////////////////////////////
	

	/*ÿ��19:00,19:10,19:20,19:30 ���ͱ�����Ϣ*/
	public void handleSociatyNotice1(){
		//Calendar c = Calendar.getInstance();
		//int minute = c.get(Calendar.MINUTE);
//		// delete this after test
//		////////////////////////////////////////
//		initSociatyDatas();
//		///////////////////////////////////////
		//if(minute < 22){
		//}
	}
	/*ÿ��19:05,19:15,19:25,19:35 ����ս����Ϣ*/
	public void handleSociatyNotice2(){
	}
	/*ÿ�����ʱִ��*/
	public void handleByHour(){
		//���򹫻�����
		//
		/**
		 * ����ã�����
		 * 
		 * 10.00 -- 10.30 �������ս
		 * 13.30 -- 14.00 ��
		 * 14.30 -- 15.00 ����
		 * 15.30 -- 16.00 ����
		 * 18.00 -- 18.30 ��Ӫ
		 * 21.00 -- 21.30 ����ս
		 * 22.00  �������������
		 */
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if(hour==0){//
			handleBy_24_00();//ϵͳ��24������
		}else if(hour==1){//
			//����һ�� �ɸ��е�����
		}else if(hour==2){//
			//���������¼�ĳ��ڼ�¼
		/* �ع��������δ�	���迪��ȡ��ע��
			clearSellGood();
		*/
		}else if(hour==10){
		}else if(hour==11){
		}else if(hour==12){
			handleBy_12_00();//ϵͳ��24������
		}else if(hour==13){
		}else if(hour==14){
		}else if(hour==15){
		}else if(hour==16){
		}else if(hour==17){
		}else if(hour==18){
			daliy_side_war();//��Ӫս����
//			WorldMessageLogic.worldMessage(WORLDMSG_SIDE);
		}else if(hour==19){
		}else if(hour == 21){
			
		}else if(hour == 20){
		}
		else if(hour == 22){	
		}
		
		// generateLampWorldBoss();//��ɻ���ħ��   clearLampBoss();//���?��ħ��
		// daliy_side_war();//��Ӫս����      daliy_side_war_end();//��Ӫս����
		// alarmDarkChessStart();//������ʼ      alermDarkChessEnd();//����ս����
		// daliy_world_boss();//��������ս��      daliy_world_boss_end();//�������ֽ����?��
		// daliy_defend_war();//�������ս��ʼ    daliy_defend_war_end();//�������ս����
		// initSociatyDatas();//��ʼ������ս��ʼ��Ϣ��clearSociatyWarDatas();//���?��ս��Ϣ
	}
	/* ÿ�� n��30�ֵ�ʱ��ִ�� */
	public void handleByThirty(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if(hour==10){
		}else if(hour==13){
		}else if(hour==14){
		}else if(hour==15){
		}else if(hour==17){
		}else if(hour==18){
			////////////////////////////
		}else if(hour == 20 )
		{
		}else if(hour==22){
		}
		// ����ThreadLocal,���У�ɾ�˻�����ͬ��������ƺ��ֲ����ڴ��������Ҫ��Դ
		//ThreadLocalCleanUtil.clearThreadLocals();
	}
	
	
	/*ÿ��ִ��*/
	public void handleBySecond(){
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//  �����̨���������
	//////////////////////////////////////////////////////////////////////////////////////
	//�ط����� 
	public boolean beforeServerClosed(){
		//ͬ�����͵�ͼ��
		return true;
	}
	public boolean startOneAction(String str){//����ĳ�����

		return true;
	}
	public boolean runBatchChanging(){
		handleBy_24_00();
		return true;
	}
	//////////////////////////////////////////////////////////////////////////////////////
	//  ������õĸ�˽�з������ڲ����?��
	//////////////////////////////////////////////////////////////////////////////////////
	// -------------   ��ͨ��     ----------
	private void handleBy_24_00(){
//		//3.�ݼ�����
//		doQuestion();
		//7.�������н�ɫ��������������
		recoverRole(1);
		//4.���⸱������
		//5.���þ��ų��г�Ա
		//6.����ÿ��ľ�������
		//1��VIP
		//2������
		//8.�ճ�����
		//9.������ȡ
		updateAllSessionDatas_2400(1);
	}
	private void handleBy_12_00(){
		recoverRole(2);
		updateAllSessionDatas_2400(2);
	}
	
	private void updateAllSessionDatas_2400(int range)
	{
		
	}

	private void recordOnLineInfo() {
//		Map param = new HashMap();
//		param.put("ts", System.currentTimeMillis() / 1000);
//		param.put("oc", GameServer.sessionDatas.size());
//		param.put("pc", GameServer.onlineMax);
//		GameServer.onlineMax = GameServer.sessionDatas.size();
//		onlineDataService.createOnlineData(param);
	}
	
	// ��Ӫս������ÿ������8��
	private void daliy_side_war()
	{
		// ֪ͨ������
		doAt_20_10();
	}
	
	// ��Ӫս����ÿ������8��30
	private void daliy_side_war_end()
	{
		doAt_20_40();
		// ֪ͨ����
		// ͳ��ս��ʤ��
		// ��ʤ��Ľ�����ս���˷����������ߵ���ҲҪ��֪ͨ��
		// �����ʱ���
	}
	
	/** �����ɫ�����  �ظ����ִ����  */
	private void recoverRole(int range) {
		/*birdNum = 5    ����ѷŷɴ���
		callNum = 5    ������ٻ�����
		curBirdNum = 0 ��ǰ�ŷɴ���
		curCallNum = 0 ��ǰ�ٻ�����
		ffNum = 3  �ŵƴ���
		fsNum = 3  ���մ���
		hsNum = 3  ���ʹ���
		salaryNum = 1  ��ٺ»����
		answerNum =0  �Ѵ������
		rmbLampNum = 0  ˢ�ƴ���
		zsNum = 6    �������˰����
		curFsNum = 0 ��ǰ���մ���
		curFfNum = 0 ��ǰ�ŵƴ���*/
		Map rm = new HashMap();
		if(range==1){
			rm.put("bn", 5);
			rm.put("cln", 5);
			rm.put("cbn", 0);
			rm.put("ccn", 0);
			rm.put("sun", 1);
			rm.put("an", 0);
			rm.put("rln", 0);
			rm.put("zn", 6);
			rm.put("cfn", 0);
			rm.put("csn", 0);
			rm.put("cn", 3);
			rm.put("olt", 0);// ��������ʱ�����
			rm.put("eds", 0);
			rm.put("slp", "0,0,0,0,0,0,0,0,0");
			rm.put("sln", 0);
			rm.put("rsln", 0);
		}else if(range==2){
			rm.put("fn", 3);
			rm.put("sn", 3);
			rm.put("hsn", 3);
		}
		rm = null;
	}
	
	private void changeMap(Map map, int lv){
		switch(lv)
		{
			case 1: //50000ͭǮ	
				map.put("money", 50000);
				break;		
			case 2: //20����
				map.put("ceng", 20);
				break;		
			case 3://TODO ��ɫ�����䣨�������3ID��
				break;		
			case 4: //20Ԫ��
				map.put("rmb", 20);
				break;
			case 5: //TODO �м����ᣨ�������5ID��
				break;
			default: break;
		}
	}
	
	//20:10�ֳ�ʼ����Ӫս��Ϣ
	private void doAt_20_10(){
	}

	//20:40����Ӫս����
	private void doAt_20_40(){
		
	}
}
