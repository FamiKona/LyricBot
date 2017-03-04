package lyric.poll;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
<<<<<<< HEAD
import java.util.Map;

import org.json.JSONArray;
=======

>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.api.objects.User;

import lyric.servers.TextServer;

public class Poll {
	public final static int 
	STATE_INACTIVE = 0,
	STATE_RUNNING = 1,
	STATE_BUILDING_QUESTION = 2,
	STATE_BUILDING_RESPONSES = 3;
	
	public int state;
	public ArrayList<String> options = new ArrayList<>();
	public String question;
	public HashMap<Integer, Integer> responses = new HashMap<>();
	private final static DecimalFormat percentFormatter = new DecimalFormat();
	static {
		percentFormatter.setMaximumFractionDigits(0);
	}
	
	public Poll() {
		state = STATE_BUILDING_QUESTION;
	}
	
	public Poll(JSONObject o) {
		state = o.getInt("state");
		question = o.getString("question");
<<<<<<< HEAD
		JSONArray arr = o.getJSONArray("options");
		for (int i = 0; i < arr.length(); i++)
			options.add(arr.getString(i));
		
		arr = o.getJSONArray("responses");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject en = arr.getJSONObject(i);
			responses.put(en.getInt("key"), en.getInt("value"));
		}
=======
		options = (ArrayList<String>) o.get("options");
		responses = (HashMap<Integer, Integer>) o.get("responses");
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("state", state);
		o.put("question", question);
		o.put("options", options);
<<<<<<< HEAD
		JSONArray arr = new JSONArray();
		for (Map.Entry<Integer, Integer> en : responses.entrySet()) {
			JSONObject j = new JSONObject();
			j.put("key", en.getKey());
			j.put("value", en.getValue());
			arr.put(j);
		}
		o.put("responses", arr);
=======
		o.put("responses", responses);
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
		return o;
	}
	
	public void setQuestion(String q) {
		question = q;
		state = STATE_BUILDING_RESPONSES;
	}
	
	public void addOption(String opt) {
		options.add(opt);
	}
	
	public String removeOption(int opt) {
		if (options.size() > opt) {
			options.remove(opt);
			return buildPollString();
		}
		return "Invalid option";
	}
	
	public void publishPoll() {
		state = STATE_RUNNING;
	}
	
<<<<<<< HEAD
	public boolean recordResponse(long chatId, User user, String resp) {
		int which = responseToInt(resp);
		if (which >= options.size()) {
			TextServer.sendString("Invalid option", chatId);
			return false;
=======
	public void recordResponse(long chatId, User user, String resp) {
		int which = responseToInt(resp);
		if (which >= options.size()) {
			TextServer.sendString("Invalid option", chatId);
			return;
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
		}
		if (responses.containsKey(user.getId())) {
			if (which != responses.get(user.getId()))
				TextServer.sendString(user.getFirstName() + " changed their vote to " + options.get(which), chatId);
			else
				TextServer.sendString(user.getFirstName() + " is still for " + options.get(which), chatId);
		} else
			TextServer.sendString(user.getFirstName() + " voted for " + options.get(which), chatId);
		responses.put(user.getId(), which);
<<<<<<< HEAD
		return true;
=======
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
	}
	
	public void endPoll(long chatId) {
		state = STATE_INACTIVE;
	}
	
	public String buildPollString() {
		String s = question + "\n\n";
		char c = 'a';
		for (String o : options)
			s += (c++) + ". " + o + "\n";
		return s.substring(0, s.length() - 1);
	}
	
	public void displayResults(long chatId, int userId) {
		if (!responses.containsKey(userId)) {
			TextServer.sendString("You must answer the poll first!", chatId);
			return;
		}
		String s = question + "\n\n";
		int totalVotes = responses.size();
		for (int i = 0; i < options.size(); i++) {
			s += options.get(i) + " - " + getVotesForOption(i) + "\n"; 
			for (int j = 0; j < getVotesForOption(i) / totalVotes * 10; j++)
				s += "\uD83D\uDC4D"; // thumbs up emoji
			s += percentFormatter.format(((double)getVotesForOption(i)) / ((double) totalVotes) * 100.0) + "%\n\n";
		}
		TextServer.sendString(s, chatId);
	}
	
	private int getVotesForOption(int opt) {
		int votes = 0;
		for (Integer i : responses.values())
			if (i == opt)
				votes++;
		return votes;
	}

	public boolean isOption(String msg) {
		return options.contains(msg);
	}
	
	private int responseToInt(String r) {
		for (int i = 0; i < options.size(); i++)
			if (options.get(i).equals(r))
				return i;
		return -1;
	}
	
}
