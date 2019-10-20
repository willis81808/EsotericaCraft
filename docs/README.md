## Welcome to GitHub Pages

Test Content Here:
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ornare iaculis sem id mollis. Curabitur sollicitudin lobortis sapien id pellentesque. Phasellus imperdiet, ex in tempus ullamcorper, tortor erat dictum libero, quis maximus lacus ex at magna. Fusce scelerisque faucibus imperdiet. Nunc ornare sodales sem nec sodales. Proin porta tempus libero vitae dictum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Fusce egestas eros suscipit sem auctor, ac accumsan erat ultrices. Aenean semper ultricies rhoncus. Pellentesque pulvinar, arcu a interdum placerat, eros justo mollis magna, non lobortis est turpis in magna. Phasellus porttitor porta enim, id euismod enim congue ut. Maecenas efficitur mattis dolor, ut pharetra sapien pharetra ut. 

```Java
public static void messageAllPlayers(PlayerList players, ITextComponent message, TextFormatting... formattings)
{
	if (formattings != null && formattings.length > 0)
	{
		message.applyTextStyles(formattings);
	}

	// send message
	players.sendMessage(message);
}
```

# Header 1
## Header 2
### Header 3