package lib.kael;

import java.io.Console;

public class ThroughputMonitor extends Thread
{
	private Console console;
	private boolean consoleAvaliable = true;
	public ThroughputMonitor()
	{
		console = System.console();
		if(console == null)
			consoleAvaliable = false;
	}
	public void run()
	{
		try {
			if(consoleAvaliable)
			{
				for(;;)
				{
					if(console.readLine() == "quit")
						break;
					this.sleep(500);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}