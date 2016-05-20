package deerhunter.data;

import deerhunter.managers.DataManager;

public class Data {
	public static final float UNIT_SCALE = 1 / 32f;

	//public final EntityData playerData;
	//public final ArrayList<EntityData> entities;
	//public final HashMap<String, MobData> mobsData;
	public final MapData mapData;

	public Data(String path) {
		//playerData = DataManager.loadEntityData(path);
		//mobsData = DataManager.loadMobsHashMapData(path);
		mapData = DataManager.loadMap(path);
	}
}
