package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.hash;

import static edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.hash.HashUtils.*;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;

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
