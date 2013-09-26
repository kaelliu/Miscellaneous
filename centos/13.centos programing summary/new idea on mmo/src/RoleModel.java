import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class RoleModel {
	public static final byte TBL_ROLE=0;
	public static final byte TBL_BAG=1;
	public static final byte TBL_TASK=2;
	public static final byte TBL_SIZE=16;
	
	public RoleMemoryData rmd;
	public LinkedList<ItemMemoryData> imds;
	public Set[] modifyedModule; 
	public void readFromDatabase(){
		// generate fake test data here
		
		modifyedModule = new Set[RoleModel.TBL_SIZE];
		for(int i=0;i<modifyedModule.length;++i){
			modifyedModule[i] = new LinkedHashSet<Object>();
		}
		
		rmd = new RoleMemoryData();
		rmd.exp=1;
		rmd.level=1;
		rmd.name="kael";
		rmd.packageSize=12;
		rmd.roleid=1;
		
		imds = new LinkedList<ItemMemoryData>();
		for(int i=0;i<8;i++){
			ItemMemoryData imd = new ItemMemoryData();
			imd.id=i+1;
			imd.itemid = i+1;
			imd.amount = 3;
			imd.place = i;
			imd.roleid =1;
			imds.add(imd);
		}
		System.out.println("fake database procedure over");
	}
	
	public void onGetItem(int iid,int amount){
		ItemMemoryData imd = new ItemMemoryData();
		imd.itemid = iid;
		imd.amount = amount;
		imd.id = -1;// tempuse
		imd.roleid = 1;
		// find empty slot for item
		int indexToInsert = 0;
		if(imds.size()==rmd.packageSize){
			// package full do nothing
			System.out.println("package full");
			return;
		}else{
			int currentSlot = imds.size();
			indexToInsert = currentSlot;
			for(int i=0;i<imds.size()-1;++i){
				if((imds.get(i+1).place - imds.get(i).place)>1){
					currentSlot = imds.get(i).place+1;
					indexToInsert = i + 1;
					break;
				}else{
					// if need this, bag should order by slot
					// and insert to right place
					//break;
					currentSlot = imds.get(i+1).place+1;
				}
			}
			System.out.println("get item on slot:"+currentSlot);
			imd.place = currentSlot;
		}
		
		imd.onCreate();
		imds.add(indexToInsert, imd);
		modifyedModule[TBL_BAG].add(imd);
	}
	
	public void onUseItem(int place,int id){
		for(int i=0;i<imds.size();++i){
			if(place == imds.get(i).place){
				imds.get(i).amount-=1;
				if(imds.get(i).amount<=0){
					removeItem(i);
				}else{
					imds.get(i).onUpdate();
					modifyedModule[TBL_BAG].add(imds.get(i));
				}
				break;
			}
		}
	}
	
	public void onRemoveItem(int place){
		for(int i=0;i<imds.size();++i){
			if(place == imds.get(i).place){
				removeItem(i);
				break;
			}
		}
	}
	
	private void removeItem(int index){
		imds.get(index).onDeCreate();
		modifyedModule[TBL_BAG].add(imds.get(index));
		imds.remove(index);
		// question 1
		// or just keep it and save a status of that slot,remove direct
		// will cause when i delete this,and get another one soon,it actually
		// make delete -> insert on it,actually it is one update step
		// question 2
		// batch insert/update,and need update whole object each time,not update changed object
		// batch mode can not be implement because insert / delete would have sequence
		// 
		// kael's principles
		// a period job from insert -> update (lot of times) -> delete should be optimize as no operation for database
		// a period job from update(a lot of times) show be optimize as one operation
		// a period job from update(a lot of times) -> delete should be optimize as one delete operation
		// a period job from delete -> insert in same data record should be optimize as one update operation(but not implement yet)
		// a data model be query should always keep in memory,query from database once
	}
	
	public void onMergeItems(int place1,int place2){
		
	}
	
	public void onSwapItems(int place1,int place2){
		if(place1 == place2){
			return;
		}
		ItemMemoryData itm1 = null;
		ItemMemoryData itm2 = null;
		for(int i=0;i<imds.size();++i){
			if(itm1 == null){
				if(place1 == imds.get(i).place){
					itm1 = imds.get(i);
				}
			}else{
				if(place2 == imds.get(i).place){
					itm2 = imds.get(i);
				}
			}
		}
		
		if(itm1 == null || itm2 == null){
			return;
		}
		
		ItemMemoryData tmp = new ItemMemoryData();
		tmp.amount = itm2.amount;
		tmp.id = itm2.id;
		tmp.itemid = itm2.itemid;
		tmp.place = itm2.place;
		
		itm2.amount = itm1.amount;
		itm2.id = itm1.id;
		itm2.itemid = itm1.itemid;
		itm2.place = itm1.place;
		
		itm1.amount = tmp.amount;
		itm1.id = tmp.id;
		itm1.itemid = tmp.itemid;
		itm1.place = tmp.place;
		
		// mark status
		itm1.onUpdate();
		itm2.onUpdate();
		// record to operation queue
		modifyedModule[TBL_BAG].add(itm1);
		modifyedModule[TBL_BAG].add(itm2);
	}
	
	public void onFlushToDb(){
		for(int i=0;i<modifyedModule.length;++i){
			{
				Iterator it = modifyedModule[i].iterator();
				while(it.hasNext()) 
				{
					MemoryData data = (MemoryData) it.next();
					data.doPersisit();
				}
			}
			modifyedModule[i].clear();
		}
	}
}
