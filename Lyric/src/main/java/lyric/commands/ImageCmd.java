package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.reddit.RedditApi;
import lyric.reddit.RedditException;
import lyric.servers.ImageServer;
import lyric.servers.TextServer;
import lyric.utils.Pair;

public class ImageCmd extends BotCommand {

	public ImageCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (arguments == null || arguments.length == 0)
			return;
		Pair<String, String> urls = null;
		try {
			urls = RedditApi.getInstance().getRandomImageFromSubreddit(arguments[0], chat.getId());
		} catch (RedditException e) {
			TextServer.sendString(e.getMessage(), chat.getId());
			return;
		}
		if (urls == null) 
			TextServer.sendString("Could not find an image to display", chat.getId());
		else
			try {
				ImageServer.sendImageFromUrl(urls, chat.getId());
			} catch (Exception e) {
				TextServer.sendString("Error displaying image", chat.getId());
			}
	}

}
