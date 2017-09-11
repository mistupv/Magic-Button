package config;

import java.io.File;

public class Config extends misc.Config
{
	/********************************************************************************************************************************/
	/************************************************************ STATIC ************************************************************/
	/********************************************************************************************************************************/
	private static final Config config = new Config();

	public static Config getConfig()
	{
		return Config.config;
	}

	/********************************************************************************************************************************/
	/************************************************************ OBJECT ************************************************************/
	/********************************************************************************************************************************/
	protected final String resultsPath;
	protected final String benchmarksPath;

	public Config()
	{
		this.resultsPath = this.miscellaneaPath + "results" + File.separator;
		this.benchmarksPath = this.miscellaneaPath + "benchmarks" + File.separator;
	}

	public String getResultsPath()
	{
		return this.resultsPath;
	}
	public String getBenchmarksPath()
	{
		return this.benchmarksPath;
	}

	public File getResultsFile()
	{
		return new File(this.getResultsPath());
	}
	public File getBenchmarksFile()
	{
		return new File(this.getBenchmarksPath());
	}
}