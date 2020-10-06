package edu.handong.csee.isel.fcminer.gumtree.client;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import org.atteo.classindex.ClassIndex;

import edu.handong.csee.isel.fcminer.gumtree.core.gen.Generators;
import edu.handong.csee.isel.fcminer.gumtree.core.gen.Registry;
import edu.handong.csee.isel.fcminer.gumtree.core.gen.TreeGenerator;

public class Run {

    public static class Options implements Option.Context {
        @Override
        public Option[] values() {
            return new Option[]{
                    new Option("-c", "Set global property (-c property value). "
                            + "Properties do not need to be prefixed by gumtree.", 2) {

                        @Override
                        protected void process(String name, String[] args) {
                            String key = args[0].startsWith("gt.") ? args[0] : "gt." + args[0];
                            System.setProperty(key, args[1]);
                        }
                    },
                    new Option.Verbose(),
                    new Help(this)
            };
        }
    }

    public static void initGenerators() {
        ClassIndex.getSubclasses(TreeGenerator.class).forEach(
                gen -> {
                	edu.handong.csee.isel.fcminer.gumtree.core.gen.Register a =
                            gen.getAnnotation(edu.handong.csee.isel.fcminer.gumtree.core.gen.Register.class);
                    if (a != null)
                        Generators.getInstance().install(gen, a);
                });
    }

    public static void initClients() {
        ClassIndex.getSubclasses(Client.class).forEach(
                cli -> {
                	edu.handong.csee.isel.fcminer.gumtree.client.Register a =
                            cli.getAnnotation(edu.handong.csee.isel.fcminer.gumtree.client.Register.class);
                    if (a != null)
                        Clients.getInstance().install(cli, a);
                });
    }

    static {
        initGenerators();
    }

    public static void startClient(String name, Registry.Factory<? extends Client> client, String[] args) {
        try {
            Client inst = client.newInstance(new Object[]{args});
            try {
                inst.run();
            } catch (Exception e) {
                System.err.printf("** Error while running client %s: %s\n", name, e);
            }
        } catch (InvocationTargetException e) {
            System.err.printf("** Error while parsing option for %s:\n%s\n", name, e.getCause());
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.printf("Can't instantiate client: '%s'\n%s\n", name, e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Options opts = new Options();
        args = Option.processCommandLine(args, opts);

        initClients();

        Registry.Factory<? extends Client> client;
        if (args.length == 0) {
            System.err.println("** No command given.");
            displayHelp(System.err, opts);
        } else if ((client = Clients.getInstance().getFactory(args[0])) == null) {
            System.err.printf("** Unknown sub-command '%s'.\n", args[0]);
            displayHelp(System.err, opts);
        } else {
            String[] a = new String[args.length - 1];
            System.arraycopy(args, 1, a, 0, a.length);
            startClient(args[0], client, a);
        }
    }

    public static void displayHelp(PrintStream out, Option.Context ctx) {
        out.println("Available Options:");
        Option.displayOptions(out, ctx);
        out.println("");
        listCommand(out);
    }

    public static void listCommand(PrintStream out) {
        out.println("Available Commands:");
        for (Registry.Entry cmd: Clients.getInstance().getEntries())
            out.println("* " + cmd);
    }

    static class Help extends Option.Help {
        public Help(Context ctx) {
            super(ctx);
        }

        @Override
        public void process(String name, String[] args) {
            displayHelp(System.out, context);
            System.exit(0);
        }
    }
}
