package de.leximon.api.command;

public abstract class BrigadierCommand {

	protected String[] names;
	protected String[] permissions;
	protected CommandUser[] user;
	
	public abstract void command(Commands c);
	
}
