package lyric.commands;

import lyric.reddit.RedditApi;
import lyric.reddit.RedditException;
import lyric.reddit.RedditReply;
import lyric.servers.ImageServer;
import lyric.servers.TextServer;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class RanditCmd extends BotCommand {

    public RanditCmd(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }
    private int state = 0;
    public final SubGuessCmd subGuessCmd = new SubGuessCmd("subguess", "Guesses a subreddit with Randit");
    private String sub;

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        state = 1;
        sub = RanditList.getRandomSub();
        RedditReply rreply = null;
        try {
            rreply = RedditApi.getInstance().getRandomImageFromSubreddit(sub, chat.getId());
        } catch (RedditException e) {
            TextServer.sendString(e.getMessage(), chat.getId());
            return;
        }
        rreply.caption = "Guess the subreddit!";
        if (rreply == null)
            TextServer.sendString("Could not find an image to display", chat.getId());
        else
            try {
                ImageServer.sendImageFromUrl(rreply, chat.getId());
            } catch (Exception e) {
                TextServer.sendString("Error displaying image", chat.getId());
            }
    }

    public class SubGuessCmd extends BotCommand {

        private SubGuessCmd(String commandIdentifier, String description) {
            super(commandIdentifier, description);
        }

        @Override
        public void execute(AbsSender absSender, User user, Chat chat, String[] params) {
            if (state != 1 || params == null || params.length == 0)
                return;
            params[0] = params[0].toLowerCase();
            if (params[0].equals(sub.toLowerCase())) {
                TextServer.sendString("Nice! The sub was " + sub + ".", chat.getId());
            } else {
                TextServer.sendString("Sorry, the sub was " + sub + ".", chat.getId());
            }
            state = 0;
        }
    }
}
