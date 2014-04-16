/*
 * Copyright 2013 vagrant-maven-plugin contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nicoulaj.maven.plugins.vagrant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.codehaus.plexus.util.StringUtils.isEmpty;

/**
 * Invokes Vagrant {@code ssh-config} command.
 *
 * @author <a href="http://github.com/nicoulaj">Julien Nicoulaud</a>
 * @goal ssh-config
 * @since 1.0
 */
@SuppressWarnings("unused")
public final class SshConfigMojo extends AbstractVagrantMojo {

    /** Mojo/Vagrant command name. */
    public static final String NAME = "ssh-config";

    /**
     * VM name.
     *
     * @parameter
     */
    protected String vm;

    /**
     * Name the host for the config.
     *
     * @parameter
     */
    protected String host;

    @Override
    protected void doExecute() throws IOException{

        final List<String> args = new ArrayList<String>();

        args.add(NAME);

        if (!isEmpty(vm))
            args.add(vm);

        if (!isEmpty(host)) {
            args.add("--host");
            args.add(host);
        }

        cli(args);
    }
}
