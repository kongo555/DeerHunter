package deerhunter.managers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import deerhunter.data.EntityData;
import deerhunter.data.MapData;

public class DataManager {
	private static String mainPath = "data/";
	private static String map = "Map.json";
	private static String assets = "Assets.json";
	private static String player = "Player.json";
	private static String Mobs = "Mobs.json";
	
	public static MapData loadMap(String path) {
		String fileText = readFile(mainPath + path + map);
		Json json = new Json();
		MapData mapData = json.fromJson(MapData.class,
				fileText);

		return mapData;
	}

	public static EntityData loadEntityData(String path) {
		String fileText = readFile(mainPath + path + player);
		Json json = new Json();

		return json.fromJson(EntityData.class, fileText);
		
	}
	
/*	
	public static HashMap<String, MobData> loadMobsHashMapData(String path) {
		String fileText = readFile(mainPath + path + Mobs);
		Json json = new Json();
		MobsListData mobsListData = json.fromJson(MobsListData.class,
				fileText);
		HashMap<String, MobData> mobsHashMapData = new HashMap<String, MobData>();
		
		System.out.println(mobsListData.mobsData.get(0).attackDamage);
		
		if (mobsListData.mobsData != null) {
			for (MobData mobData : mobsListData.mobsData) {
				mobsHashMapData.put(mobData.name, mobData);
			}
		}
		

		return mobsHashMapData;
	}
*/
	public static class AssetsListData {
		public ArrayList<AssetsNameData> assetsList = new ArrayList<AssetsNameData>();
	}

	public static class AssetsNameData {
		public String name;
	}

	public static ArrayList<String> loadAssetsList(String path) {
		String fileText = readFile(mainPath + path + assets);
		Json json = new Json();
		AssetsListData assetsListData = json.fromJson(AssetsListData.class,
				fileText);

		ArrayList<String> assetsList = new ArrayList<String>();
		if (assetsListData.assetsList != null) {
			for (AssetsNameData AssetsNameData : assetsListData.assetsList) {
				assetsList.add(AssetsNameData.name);
			}
		}

		return assetsList;
	}

	public static String readFile(String fileName) {
		FileHandle file = Gdx.files.local(fileName);
		if (file != null && file.exists()) {
			String s = file.readString();
			if (!s.isEmpty()) {
				return s;
			}
		}
		return "";
	}

	public static void writeFile(String fileName, String s) {
		FileHandle file = Gdx.files.internal(fileName);
		file.writeString(com.badlogic.gdx.utils.Base64Coder.encodeString(s),
				false);
	}
}
