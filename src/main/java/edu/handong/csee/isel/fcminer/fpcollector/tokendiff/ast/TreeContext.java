package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class TreeContext {

    private Map<Integer, String> typeLabels = new HashMap<>();

    private final Map<String, Object> metadata = new HashMap<>();

//    private final MetadataSerializers serializers = new MetadataSerializers();

    private ITree root;

//    @Override
//    public String toString() {
//        return TreeIoUtils.toLisp(this).toString();
//    }

    public void setRoot(ITree root) {
        this.root = root;
    }

    public ITree getRoot() {
        return root;
    }

    public String getTypeLabel(ITree tree) {
        return getTypeLabel(tree.getType());
    }

    public String getTypeLabel(int type) {
        String tl = typeLabels.get(type);
        if (tl == null)
            tl = Integer.toString(type);
        return tl;
    }

    protected void registerTypeLabel(int type, String name) {
        if (name == null || name.equals(ITree.NO_LABEL))
            return;
        String typeLabel = typeLabels.get(type);
        if (typeLabel == null)
            typeLabels.put(type, name);
        else if (!typeLabel.equals(name))
            throw new RuntimeException(String.format("Redefining type %d: '%s' with '%s'", type, typeLabel, name));
    }

    public void importTypeLabels(TreeContext ctx) {
        for (Map.Entry<Integer, String> label : ctx.typeLabels.entrySet()) {
            if (!typeLabels.containsValue(label.getValue())) {
                typeLabels.put(label.getKey(), label.getValue());
            }
        }
    }

    public ITree createTree(int type, String label, String typeLabel) {
        registerTypeLabel(type, typeLabel);
        return new Tree(type, label);
    }

    public ITree createTree(ITree... trees) {
        return new AbstractTree.FakeTree(trees);
    }

    public void validate() {
        root.refresh();
        TreeUtils.postOrderNumbering(root);
    }

    public boolean hasLabelFor(int type) {
        return typeLabels.containsKey(type);
    }

    /**
     * Get a global metadata.
     * There is no way to know if the metadata is really null or does not exists.
     *
     * @param key of metadata
     * @return the metadata or null if not found
     */
    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    /**
     * Get a local metadata, if available. Otherwise get a global metadata.
     * There is no way to know if the metadata is really null or does not exists.
     *
     * @param key of metadata
     * @return the metadata or null if not found
     */
    public Object getMetadata(ITree node, String key) {
        Object metadata;
        if (node == null || (metadata = node.getMetadata(key)) == null)
            return getMetadata(key);
        return metadata;
    }

    /**
     * Store a global metadata.
     *
     * @param key   of the metadata
     * @param value of the metadata
     * @return the previous value of metadata if existed or null
     */
    public Object setMetadata(String key, Object value) {
        return metadata.put(key, value);
    }

    /**
     * Store a local metadata
     *
     * @param key   of the metadata
     * @param value of the metadata
     * @return the previous value of metadata if existed or null
     */
    public Object setMetadata(ITree node, String key, Object value) {
        if (node == null)
            return setMetadata(key, value);
        else {
            Object res = node.setMetadata(key, value);
            if (res == null)
                return getMetadata(key);
            return res;
        }
    }
    
    public Iterator<Entry<String, Object>> getMetadata() {
        return metadata.entrySet().iterator();
    }

    public static class Marshallers<E> {
        Map<String, E> serializers = new HashMap<>();

        public static final Pattern valid_id = Pattern.compile("[a-zA-Z0-9_]*");

        public void addAll(Marshallers<E> other) {
            addAll(other.serializers);
        }

        public void addAll(Map<String, E> serializers) {
            serializers.forEach((k, s) -> add(k, s));
        }

        public void add(String name, E serializer) {
            if (!valid_id.matcher(name).matches()) // TODO I definitely don't like this rule, we should think twice
                throw new RuntimeException("Invalid key for serialization");
            serializers.put(name, serializer);
        }

        public void remove(String key) {
            serializers.remove(key);
        }

        public Set<String> exports() {
            return serializers.keySet();
        }
    }

//    public static class MetadataSerializers extends Marshallers<MetadataSerializer> {
//
//        public void serialize(TreeFormatter formatter, String key, Object value) throws Exception {
//            MetadataSerializer s = serializers.get(key);
//            if (s != null)
//                formatter.serializeAttribute(key, s.toString(value));
//        }
//    }
//
//    public static class MetadataUnserializers extends Marshallers<MetadataUnserializer> {
//
//        public void load(ITree tree, String key, String value) throws Exception {
//            MetadataUnserializer s = serializers.get(key);
//            if (s != null) {
//                if (key.equals("pos"))
//                    tree.setPos(Integer.parseInt(value));
//                else if (key.equals("length"))
//                    tree.setLength(Integer.parseInt(value));
//                else
//                    tree.setMetadata(key, s.fromString(value));
//            }
//        }
//    }
}