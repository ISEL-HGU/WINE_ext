package edu.handong.csee.isel.fcminer.gumtree.core.tree.hash;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

import static edu.handong.csee.isel.fcminer.gumtree.core.tree.hash.HashUtils.*;

public abstract class StaticHashGenerator implements HashGenerator {

    @Override
    public void hash(ITree t) {
        for (ITree n: t.postOrder())
            n.setHash(nodeHash(n));
    }

    public abstract int nodeHash(ITree t);

    public static class StdHashGenerator extends StaticHashGenerator {

        @Override
        public int nodeHash(ITree t) {
            return t.toStaticHashString().hashCode();
        }

    }

    public static class Md5HashGenerator extends StaticHashGenerator {

        @Override
        public int nodeHash(ITree t) {
            return HashUtils.md5(t.toStaticHashString());
        }

    }

}
