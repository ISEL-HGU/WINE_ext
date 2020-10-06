package edu.handong.csee.isel.fcminer.gumtree.core.gen;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Generators extends Registry<String, TreeGenerator, Register> {

    private static Generators registry;

    public static final Generators getInstance() {
        if (registry == null)
            registry = new Generators();
        return registry;
    }

    public TreeContext getTree(String file) throws UnsupportedOperationException, IOException {
        TreeGenerator p = get(file);
        if (p == null)
            throw new UnsupportedOperationException("No generator found for file: " + file);
        return p.generateFromFile(file);
    }

    public TreeContext getTree(String generator, String file) throws UnsupportedOperationException, IOException {
        for (Entry e : entries)
            if (e.id.equals(generator))
                return e.instantiate(null).generateFromFile(file);
        throw new UnsupportedOperationException("No generator \"" + generator + "\" found.");
    }

    @Override
    protected Entry newEntry(Class<? extends TreeGenerator> clazz, Register annotation) {
        return new Entry(annotation.id(), clazz, defaultFactory(clazz), annotation.priority()) {
            final Pattern[] accept;

            {
                String[] accept = annotation.accept();
                this.accept = new Pattern[accept.length];
                for (int i = 0; i < accept.length; i++)
                    this.accept[i] = Pattern.compile(accept[i]);
            }

            @Override
            protected boolean handle(String key) {
                for (Pattern pattern : accept)
                    if (pattern.matcher(key).find())
                        return true;
                return false;
            }

            @Override
            public String toString() {
                return String.format("%d\t%s: %s", priority, Arrays.toString(accept), clazz.getCanonicalName());
            }
        };
    }
}