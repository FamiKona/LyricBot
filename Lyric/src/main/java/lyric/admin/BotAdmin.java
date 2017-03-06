package lyric.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class BotAdmin {
	private final static BotAdmin instance = new BotAdmin();
	private BotAdmin(){
		loadAdmins();
	}
	public static BotAdmin getInstance() {
		return instance;
	}
	
	private final static String FILENAME = "Bot Admins.txt";
	
	// <ChatId, UserIds>
	private HashMap<Long, List<Integer>> pollAdmins = new HashMap<>();
	
	public void loadAdmins() {
		try {
			JSONArray arr = getAdmins();
			if (arr == null)
				return;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				JSONArray val = o.getJSONArray("value");
				List<Integer> list = new ArrayList<>();
				for (int j = 0; j < val.length(); j++)
					list.add(val.getInt(j));
				pollAdmins.put(o.getLong("key"), list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private JSONArray getAdmins() throws JSONException {
		String json = "";
		try {
			FileReader fr = new FileReader(new File(FILENAME));
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null)
				json += line;
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return new JSONArray(json);
	}
	
	public void saveAdmins() throws IOException {
		JSONArray arr = new JSONArray();
		for (Map.Entry<Long, List<Integer>> en : pollAdmins.entrySet()) {
			JSONObject o = new JSONObject();
			o.put("key", en.getKey());
			o.put("value", en.getValue());
			arr.put(o);
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME));
		bw.write(arr.toString());
		bw.close();
	}
	
	public boolean isUserAdmin(AbsSender bot, long chatId, int userId) {
		if (userId == 114800779) // that's me! @Morororo
			return true;
		List<Integer> admins = getChatAdmins(chatId, bot);
		if (admins == null)
			admins = new ArrayList<>();
		if (pollAdmins.get(chatId) != null)
			admins.addAll(pollAdmins.get(chatId));
		return admins.contains(userId);
	}
	
	private List<Integer> getChatAdmins(long chatId, AbsSender bot) {
		try {
			GetChatAdministrators g = new GetChatAdministrators();
			g.setChatId(chatId);
			List<Integer> admins = new ArrayList<>();
			for (ChatMember cm : bot.getChatAdministrators(g))
				admins.add(cm.getUser().getId());
			return admins;
		} catch (TelegramApiException e) {
			//e.printStackTrace();
			return null;
		}
	}
	
	public void addAdmin(String userId, long chatId) {
		if (pollAdmins.get(chatId) == null)
			pollAdmins.put(chatId, new ArrayList<Integer>());
		pollAdmins.get(chatId).add(Integer.parseInt(userId));
		try {
			saveAdmins();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
