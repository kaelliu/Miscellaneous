package com.logic;

import java.util.HashMap;
import java.util.Map;

//import com.service.EquipRecService;
//import com.service.ExpRecService;
//import com.service.FoodRecService;
//import com.service.HotelRecService;
//import com.service.ItemRecService;
//import com.service.LampRecService;
//import com.service.MedalRecService;
//import com.service.MoneyRecService;
//import com.service.NumRecDto;
//import com.service.NumRecService;
//import com.service.RmbRecService;
//import com.service.ScoreRecService;
//import com.service.SvRecService;
//import com.service.TrainRecService;

public class RecordUpdater {
	
//	private MoneyRecService moneyRecService;
//	private NumRecService numRecService;
//	private FoodRecService foodRecService;
//	private EquipRecService equipRecService;
//	private ExpRecService expRecService;
//	private HotelRecService hotelRecService;
//	private ItemRecService itemRecService;
//	private LampRecService lampRecService;
//	private TrainRecService trainRecService;
//	private SvRecService svRecService;
//	private ScoreRecService scoreRecService;
//	private RmbRecService rmbRecService;
//	
//	//后台铜币记录统计   map
//	public static Map generateMoneyRec(int rid, int money, int type, String name){
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("money", money);
//		map.put("time", time);
//		map.put("type", type);
//		map.put("rn", name);
//		return map;
//	}
//	//后台装备记录统计
//	public static Map generateEquipRec(int rid, int ei, int sl, int pl,
//			int type, int re) {
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("time", time);
//		map.put("type", type);
//		map.put("ei",ei);
//		map.put("sl", sl);
//		map.put("pl", pl);
//		map.put("re", re);
//		return map;
//	}
//	//后台经验记录统计
//	public static Map generateExpRec(int rid, String hero, int type, int exp) {
//		// 
//		long time = System.currentTimeMillis() / 1000;
//		Map map =new HashMap();
//		map.put("rid", rid);
//		map.put("hids", hero);
//		map.put("type", type);
//		map.put("time", time);
//		map.put("exp", exp);
//		return map;
//	}
//	//后台粮草记录统计
//	public static Map generateFoodRec(int rid, int fo, int type, String rn) {
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("fo", fo);
//		map.put("time", time);
//		map.put("type", type);
//		map.put("rn", rn);
//		return map;
//	}
//	//后台客栈记录统计
//	public static Map generateHotelRec(int rid, String rn,int hid, int type, int mon, int rmb,int exp, int iid) {
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("rn", rn);
//		map.put("hid", hid);
//		map.put("type", type);
//		map.put("mon", mon);
//		map.put("rmb",rmb);
//		map.put("exp", exp);
//		map.put("iid", iid);
//		map.put("time", time);
//		return map;
//	}
//	//后台物品记录统计
//	public static  Map genrateItemRec(int rid,String rn,int iid, int pl,int am, int type) {
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("rn", rn);
//		map.put("iid", iid);
//		map.put("pl",pl);
//		map.put("am", am);
//		map.put("type", type);
//		map.put("time", time);
//		return map;
//	}
//	//后台孔明灯记录统计
//	public static  Map genrateLampRec(int rid, String rn,int type,int co, int torid1,
//			int torid2, int in, int re) {
//		// 
//		Map map =new HashMap();
//		map.put("rid", rid);
//		map.put("rn", rn);
//		map.put("type", type);
//		map.put("co", co);
//		map.put("torid1", torid1);
//		map.put("torid2",torid2);
//		map.put("in", in);
//		map.put("re", re);
//		return map;
//	}
//	//勋章
//	public static Map genrateMedalRec(int rid, int me, int type, String rn) {
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("me", me);
//		map.put("time", time);
//		map.put("type", type);
//		map.put("rn", rn);
//		return map;
//	}
//	//后台行为次数统计
//	public static Map genrateNumRec(int rid, int type, int num) {
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("type", type);
//		map.put("num", num);
//		map.put("time", time);
//		return map;
//	}
//	
//	
//	
//	
//	//后台元宝纪录统计
//	public static Map generateRmbRec(int rid, int rmb, int type, String rn) {
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("rmb", -rmb);
//		map.put("time", time);
//		map.put("type", type);
//		map.put("rn", rn);
//		return map;
//	}
//	
//	//后台功勋纪录统计
//	public static Map generateScoreRec(int rid, int sc, int type, String rn) {
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("sc", sc);
//		map.put("time", time);
//		map.put("type", type);
//		map.put("rn", rn);
//		return map;
//	}
//	
//	//后台技巧值纪录统计
//	public static Map generateSvRec(int rid, int sv, int type, String rn)
//	{
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("sv", sv);
//		map.put("time", time);
//		map.put("type", type);
//		map.put("rn", rn);
//		return map;
//	}
//	
//	//后台培养纪录统计
//	public static Map generateTrainRec(int rid, String rn,int hid, int num,int type, int re) {
//		Map map = new HashMap();
//		long time = System.currentTimeMillis() / 1000;
//		map.put("rid", rid);
//		map.put("rn", rn);
//		map.put("hid", hid);
//		map.put("num", num);
//		map.put("type", type);
//		map.put("re", re);
//		map.put("time", time);
//		return map;
//	}
//	//同个行为不同类型
//	public void handleRecordNum(Map numMap1,Map numMap2,Map numMap3){
//		if(numMap1!=null && !numMap1.isEmpty()){
//			numRecService.insert(numMap1);
//		}
//		if(numMap2!=null && !numMap2.isEmpty()){
//			numRecService.insert(numMap2);
//		}
//		if(numMap3!=null && !numMap3.isEmpty()){
//			numRecService.insert(numMap3);
//		}
//	}
//	/**
//	 * @param moneyMap  -- 铜币记录
//	 * @param equipMap  --装备
//	 * @param expMap  --经验
//	 * @param foodMap  --粮草
//	 * @param hotelMap  --客栈
//	 * @param itemMap  --物品
//	 * @param lampMap  --孔明灯
//	 * @param medalMap  --勋章
//	 * @param numMap  --行为
//	 * @param rmbMap  --后台元宝纪录统计
//	 * @param scoreMap --后台功勋纪录统计
//	 * @param svMap --后台技巧值纪录统计
//	 * @param trainMap --后台培养纪录统计
//	 * 
//	 */
//	public void handleRecordAll(Map moneyMap, Map equipMap, Map expMap, Map foodMap, Map hotelMap, Map itemMap, Map lampMap, Map medalMap, Map numMap, Map rmbMap, Map scoreMap, Map svMap, Map trainMap ){
//		if(moneyMap!=null && !moneyMap.isEmpty()){
//			moneyRecService.insert(moneyMap);
//		}
//		if(equipMap!=null && !equipMap.isEmpty()){
//			equipRecService.insert(equipMap);
//		}
//		if(expMap!=null && !expMap.isEmpty()){
//			expRecService.insert(expMap);
//		}
//		if(foodMap!=null && !foodMap.isEmpty()){
//			foodRecService.insert(foodMap);
//		}
//		if(hotelMap!=null && !hotelMap.isEmpty()){
//			hotelRecService.insert(hotelMap);
//		}
//		if(itemMap!=null && !itemMap.isEmpty()){
//			itemRecService.insert(itemMap);
//		}
//		if(lampMap!=null && !lampMap.isEmpty()){
//			lampRecService.insert(lampMap);
//		}
//		if(medalMap!=null && !medalMap.isEmpty()){
////			medalRecService.insert(medalMap);
//		}
//		if(numMap!=null && !numMap.isEmpty()){
//			NumRecDto nl = numRecService.getNum(numMap);
//			if(nl==null){
//				numRecService.insert(numMap);
//			}else{
//				numMap.put("num",nl.getNum()+Integer.parseInt(numMap.get("num").toString()));
//				numRecService.changeNum(numMap);
//			}
//		}
//		if(rmbMap!=null && !rmbMap.isEmpty()){
//			rmbRecService.insert(rmbMap);
//		}
//		if(scoreMap!=null && !scoreMap.isEmpty()){
//			scoreRecService.insert(scoreMap);
//		}
//		if(svMap!=null && !svMap.isEmpty()){
//			svRecService.insert(svMap);
//		}
//		if(trainMap!=null && !trainMap.isEmpty()){
//			trainRecService.insert(trainMap);
//		}
//	}
//
//	public MoneyRecService getMoneyRecService() {
//		return moneyRecService;
//	}
//	public void setMoneyRecService(MoneyRecService moneyRecService) {
//		this.moneyRecService = moneyRecService;
//	}
//	public NumRecService getNumRecService() {
//		return numRecService;
//	}
//	public void setNumRecService(NumRecService numRecService) {
//		this.numRecService = numRecService;
//	}
//	public FoodRecService getFoodRecService() {
//		return foodRecService;
//	}
//	public void setFoodRecService(FoodRecService foodRecService) {
//		this.foodRecService = foodRecService;
//	}
//	public EquipRecService getEquipRecService() {
//		return equipRecService;
//	}
//	public void setEquipRecService(EquipRecService equipRecService) {
//		this.equipRecService = equipRecService;
//	}
//	public ExpRecService getExpRecService() {
//		return expRecService;
//	}
//	public void setExpRecService(ExpRecService expRecService) {
//		this.expRecService = expRecService;
//	}
//	public HotelRecService getHotelRecService() {
//		return hotelRecService;
//	}
//	public void setHotelRecService(HotelRecService hotelRecService) {
//		this.hotelRecService = hotelRecService;
//	}
//	public ItemRecService getItemRecService() {
//		return itemRecService;
//	}
//	public void setItemRecService(ItemRecService itemRecService) {
//		this.itemRecService = itemRecService;
//	}
//	public LampRecService getLampRecService() {
//		return lampRecService;
//	}
//	public void setLampRecService(LampRecService lampRecService) {
//		this.lampRecService = lampRecService;
//	}
//	public void setTrainRecService(TrainRecService trainRecService) {
//		this.trainRecService = trainRecService;
//	}
//	public TrainRecService getTrainRecService() {
//		return trainRecService;
//	}
//	public void setSvRecService(SvRecService svRecService) {
//		this.svRecService = svRecService;
//	}
//	public SvRecService getSvRecService() {
//		return svRecService;
//	}
//	public void setScoreRecService(ScoreRecService scoreRecService) {
//		this.scoreRecService = scoreRecService;
//	}
//	public ScoreRecService getScoreRecService() {
//		return scoreRecService;
//	}
//	public void setRmbRecService(RmbRecService rmbRecService) {
//		this.rmbRecService = rmbRecService;
//	}
//	public RmbRecService getRmbRecService() {
//		return rmbRecService;
//	}
}
