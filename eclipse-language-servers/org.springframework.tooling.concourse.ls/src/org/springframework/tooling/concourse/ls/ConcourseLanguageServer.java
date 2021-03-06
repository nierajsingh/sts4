/*******************************************************************************
 * Copyright (c) 2016, 2017 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.tooling.concourse.ls;

import static org.springframework.tooling.ls.eclipse.commons.preferences.LanguageServerConsolePreferenceConstants.CONCOURSE_SERVER;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.springframework.tooling.ls.eclipse.commons.JRE;
import org.springframework.tooling.ls.eclipse.commons.LanguageServerCommonsActivator;
import org.springframework.tooling.ls.eclipse.commons.STS4LanguageServerProcessStreamConnector;

import com.google.common.collect.ImmutableList;

/**
 * @author Martin Lippert
 */
public class ConcourseLanguageServer extends STS4LanguageServerProcessStreamConnector {

	public ConcourseLanguageServer() {
		super(CONCOURSE_SERVER);
		setCommands(JRE.currentJRE().jarLaunchCommand(getLanguageServerJARLocation(), ImmutableList.of(
				//"-Xdebug",
				//"-agentlib:jdwp=transport=dt_socket,address=8899,server=y,suspend=n",
				"-Dlsp.lazy.completions.disable=true",
				"-Dlsp.completions.indentation.enable=true"
		)));
		setWorkingDirectory(getWorkingDirLocation());
	}
	
	protected String getLanguageServerJARLocation() {
		String languageServer = "concourse-language-server-" + Constants.LANGUAGE_SERVER_VERSION + ".jar";

		Bundle bundle = Platform.getBundle(Constants.PLUGIN_ID);
		File dataFile = bundle.getDataFile(languageServer);
//		if (!dataFile.exists()) {
			try {
				copyLanguageServerJAR(languageServer);
			}
			catch (Exception e) {
				if (bundle.getVersion().getQualifier().equals("qualifier")) {
					dataFile = new File(System.getProperty("user.home")+"/git/sts4/headless-services/concourse-language-server/target/concourse-language-server-"+Constants.LANGUAGE_SERVER_VERSION+".jar");
					if (!dataFile.exists()) {
						LanguageServerCommonsActivator.logError(e, "Problem locating Concourse language server jar");
					}
				} else {
					LanguageServerCommonsActivator.logError(e, "Problem locating Concourse language server jar");
				}
			}
//		}
		
		return dataFile.getAbsolutePath();
	}
	
	protected void copyLanguageServerJAR(String languageServerJarName) throws Exception {
		Bundle bundle = Platform.getBundle(Constants.PLUGIN_ID);
		InputStream stream = FileLocator.openStream( bundle, new Path("servers/" + languageServerJarName), false );
		
		File dataFile = bundle.getDataFile(languageServerJarName);
		Files.copy(stream, dataFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

}
