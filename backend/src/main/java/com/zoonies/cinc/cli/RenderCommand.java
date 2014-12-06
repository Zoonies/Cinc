package com.zoonies.cinc.cli;

import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import com.zoonies.cinc.CincConfiguration;

public class RenderCommand extends ConfiguredCommand<CincConfiguration> {
  
    public RenderCommand() {
        super("render", "Render the template data to console");
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);
        subparser.addArgument("-i", "--include-default")
                 .action(Arguments.storeTrue())
                 .dest("include-default")
                 .help("Also render the template with the default name");
        subparser.addArgument("names").nargs("*");
    }

    @Override
    protected void run(Bootstrap<CincConfiguration> bootstrap,
                       Namespace namespace,
                       CincConfiguration configuration) throws Exception {

    }
}
