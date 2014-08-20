package commands;

public class HelpCommand extends Command {
	
	public String[] commandStrings()
	{
		return new String[]{"?", "h", "help"};
	}
	
	public String help()
	{
		return "Show this help";
	}
	public void execute(String inputCommand)
	{
		for (Command cmd : Command.Commands)
		{
			String cmds = concat(cmd.commandStrings());
			System.out.println(String.format("%30s - %s", cmds, cmd.help()));
		}
	}
	private String concat(String[] ss)
	{
		StringBuilder sb = new StringBuilder();
		for (String s : ss)
			sb.append(s).append(", ");
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
}
