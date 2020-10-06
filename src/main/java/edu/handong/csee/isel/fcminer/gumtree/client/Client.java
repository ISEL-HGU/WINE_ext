package edu.handong.csee.isel.fcminer.gumtree.client;

import org.atteo.classindex.IndexSubclasses;

@IndexSubclasses
public abstract class Client {
    @SuppressWarnings("unused")
    public Client(String[] args) {}

    public abstract void run() throws Exception;
}


