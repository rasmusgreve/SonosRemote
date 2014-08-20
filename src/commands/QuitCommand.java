package commands;

public class QuitCommand extends Command {

	@Override
	public String[] commandStrings() {
		return new String[]{"q","quit"};
	}

	@Override
	public void execute(String cmd) {
		System.out.println("Bye!");
		System.exit(0);
	}

	@Override
	public String help() {
		return "Quit the program";
	}

}
