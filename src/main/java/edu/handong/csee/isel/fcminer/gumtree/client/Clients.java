package edu.handong.csee.isel.fcminer.gumtree.client;

import edu.handong.csee.isel.fcminer.gumtree.core.gen.Registry;

public class Clients extends Registry<String, Client, Register> {
    private static Clients registry;

    public static Clients getInstance() {
        if (registry == null)
            registry = new Clients();
        return registry;
    }

    protected String getName(Register annotation, Class<? extends Client> clazz) {
        String name = annotation.name();
        if (Register.no_value.equals(name))
            name = clazz.getSimpleName().toLowerCase();
        return name;
    }

    @Override
    protected Entry newEntry(Class<? extends Client> clazz, Register annotation) {
        String name = annotation.name().equals(Register.no_value)
                ? clazz.getSimpleName() : annotation.name();
        return new Entry(name.toLowerCase(), clazz, defaultFactory(clazz, String[].class),  annotation.priority()) {
            @Override
            protected boolean handle(String key) {
                return id.equalsIgnoreCase(key);
            }

            final String description;
            {
                description = annotation.description();
            }

            @Override
            public String toString() {
                return String.format("%s: %s", id, description);
            }
        };
    }
}
