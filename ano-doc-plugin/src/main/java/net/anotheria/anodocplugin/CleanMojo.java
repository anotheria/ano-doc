package net.anotheria.anodocplugin;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.LifecyclePhase;

@Mojo(name = "clean", defaultPhase = LifecyclePhase.CLEAN)
public class CleanMojo extends AbstractMojo {

	@Parameter
	private String outputDir;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Cleaning " + outputDir);
		File f = new File(outputDir);
		if (!f.isDirectory()) {
			getLog().warn(f.getAbsolutePath() + " is not a directory");
			return;
		}
		delete(f);
	}

	private void delete(File f) {
		if (f.isDirectory())
			deleteDir(f);
		if (f.isFile())
			deleteFile(f);
	}

	private void deleteFile(File f) {
		f.delete();
	}

	private void deleteDir(File f) {
		for (File ff : f.listFiles())
			delete(ff);
		f.delete();
	}

}
